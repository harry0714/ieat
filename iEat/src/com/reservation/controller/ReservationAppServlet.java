package com.reservation.controller;

import static com.ieat.gcm.MyData.SERVER_KEY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookmark_meal.model.BMAppService;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ieat.gcm.GCMMessageType;
import com.ieat.gcm.MyData;
import com.member.model.MemberAppService;
import com.member.model.MemberVO;
import com.ord.model.OrdVO;
import com.reservation.model.ReservationAppService;
import com.reservation.model.ReservationVO;
import com.store.controller.ImageUtil;
import com.store.model.StoreAppService;
import com.store.model.StoreVO;

@SuppressWarnings("serial")
@WebServlet("/ReservationAppServlet.do")
public class ReservationAppServlet extends HttpServlet {

	private ServletContext context;
	private static Executor threadPool;

	@Override
	public void init() throws ServletException {
		super.init();
		context = getServletContext();
		int numberOfProcessors = Runtime.getRuntime().availableProcessors();
		threadPool = Executors.newFixedThreadPool(numberOfProcessors);
	}

	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); // 把傳過來的資料轉換回來

		BufferedReader br = req.getReader();

		StringBuilder jsonIn = new StringBuilder(); // 把資料用BufferedReader的readLine()去取出來
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line); // 把資料用StringBuilder串起來
		}

		// 從JsonObject裡拿出Json字串
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class); // 這邊toString()是為了把他轉成String類別

		String action = jsonObject.get("action").getAsString(); // 取JsonObject裡面value的值，再把他轉成json字串

		System.out.println("action: " + action);

		ReservationAppService reservationSvc = new ReservationAppService();

		if ("insert".equals(action)) {
			Type listType = new TypeToken<ReservationVO>() {
			}.getType();

			ReservationVO reservationVO = gson.fromJson(jsonObject.get("reservationVO").getAsString(), listType);
			int booked = reservationSvc.checkSeat(reservationVO.getReservation_date(),
					reservationVO.getReservation_hour(),reservationVO.getStore_no());
			StoreVO store = (new StoreAppService()).getOneStore(reservationVO.getStore_no());
			String[] str = store.getStore_book_amt().split("-");

			String result = "fail";

			if ((booked + reservationVO.getReservation_guests()) <= (Integer
					.parseInt(str[Integer.parseInt(reservationVO.getReservation_hour())]))) {

				try {
					reservationSvc.addReservation(reservationVO);
					result = "success";

					MemberAppService memSvc = new MemberAppService();
					String mem_key = memSvc.getMemberKey(reservationVO.getMem_no());
					if (mem_key != null) {
						asyncSendSingle(mem_key, "New Reservation has been made",
								String.valueOf(GCMMessageType.新增的訂位通知.ordinal()));
					}

					String store_no = reservationVO.getStore_no();
					StoreAppService storeSvc = new StoreAppService();
					String store_key = storeSvc.getStoreKey(store_no);
					if (store_key != null) {
						asyncSendSingle(store_key, "New Reservation", String.valueOf(GCMMessageType.新增的訂位通知.ordinal()));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			writeText(res, result);
		} else if ("getMemBooking".equals(action)) {
			String mem_no = jsonObject.get("mem_no").getAsString();
			Map<String, ReservationVO> reserlist = reservationSvc.getMemBooking(mem_no);

			writeText(res, gson.toJson(reserlist));

		} else if ("getQrcode".equals(action)) {
			String reser_no = jsonObject.get("reser_no").getAsString();
			int imageSize = jsonObject.get("imageSize").getAsInt();

			byte[] qrcode = reservationSvc.getQrcode(reser_no);

			if (qrcode != null) {
				qrcode = ImageUtil.shrink(qrcode, imageSize);
				res.setContentType("image/jpeg");
				res.setContentLength(qrcode.length);
				OutputStream os = res.getOutputStream();
				os.write(qrcode);
			}

		} else if ("updateQrcodeStatus".equals(action)) {
			String reser_no = jsonObject.get("reser_no").getAsString();
			int type = jsonObject.get("reser_status").getAsInt();
			String result = "fail";
			try {
				reservationSvc.updateQrcodeStatus(reser_no, type);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ("success".equals(result)) {
				switch (type) {
				case 3:
					StoreAppService storeSvc = new StoreAppService();
					String store_key = storeSvc.getStoreKey(jsonObject.get("store_no").getAsString());
					if (store_key != null) {
						asyncSendSingle(store_key, "Reservation cancelled",
								String.valueOf(GCMMessageType.訂位取消.ordinal()));
					}
					break;
				}
			}
			writeText(res, result);
		} else if ("getShopBooking".equals(action)) {
			String store_no = jsonObject.get("store_no").getAsString();
			Map<String, ReservationVO> reserlist = reservationSvc.getStoreBooking(store_no);

			writeText(res, gson.toJson(reserlist));

		} else if ("reservationGetVO".equals(action)) {
			String reservation_no = jsonObject.get("reservation_no").getAsString();
			String store_no = jsonObject.get("store_no").getAsString();
			ReservationVO reservationVO = reservationSvc.getOneReservation(reservation_no);
			Map<String,ReservationVO> result = null;
			
			if (!store_no.equals(reservationVO.getStore_no())) {
				reservationVO = null;
			}else{
				MemberAppService memSvc = new MemberAppService();
				MemberVO memberVO = memSvc.getOneMember(reservationVO.getMem_no());
				String mem_name = memberVO.getMem_name().charAt(0)+(("1".equals(memberVO.getMem_sex()))?"先生":"小姐");
				result = new HashMap<String,ReservationVO>();
				result.put(reservation_no+","+mem_name, reservationVO);
			}

			writeText(res, gson.toJson(result));
		}
	}

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
	}

	private void asyncSendSingle(String regId, final String messageSend, String type) {
		threadPool.execute(new Runnable() {
			public void run() {
				Message message = new Message.Builder().addData("message", messageSend).addData("type", type).build();
				Result result;
				Sender sender = new Sender(SERVER_KEY);
				try {
					// after sending a message to many devices,
					// GCM server will return MulticastResult storing each
					// result for a device
					result = sender.send(message, regId, 5);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				String messageId = result.getMessageId();
				if (messageId != null) {
					System.out.println("Succesfully sent message to device: " + regId + "; messageId = " + messageId);
					String canonicalRegId = result.getCanonicalRegistrationId();
					if (canonicalRegId != null) {
						// same device has more than one registration id:
						// update it
						// see
						// https://developer.android.com/google/gcm/adv.html#canonical
						System.out.println("canonicalRegId: " + canonicalRegId);
						MyData.updateRegistration(regId, canonicalRegId);
						System.out.println("Updating " + regId + " to " + canonicalRegId);
					}
				} else {
					String error = result.getErrorCodeName();
					// the user has uninstalled the Android app or turned
					// off notifications. Sender should stop sending
					// messages to this device and delete the registration
					// id.
					if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
						System.out.println("Unregistered device: " + regId);
					} else {
						System.out.println("Error sending message to " + regId + ": " + error);
					}
				}
			}

		});
	}

}

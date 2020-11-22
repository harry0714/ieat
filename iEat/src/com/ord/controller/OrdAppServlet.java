package com.ord.controller;

import static com.ieat.gcm.MyData.SERVER_KEY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.ord.model.OrdAppService;
import com.ord.model.OrdVO;

import com.ord_meal.model.Ord_mealVO;
import com.store.controller.ImageUtil;
import com.store.model.StoreAppService;

@SuppressWarnings("serial")
@WebServlet("/OrdAppServlet.do")
public class OrdAppServlet extends HttpServlet {

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
		doGet(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		BufferedReader br = req.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);

		String action = jsonObject.get("action").getAsString();
		System.out.println("action: " + action);
		OrdAppService ordSvc = new OrdAppService();

		if ("insert".equals(action)) {

			Type listType = new TypeToken<Map<String, OrdVO>>() {
			}.getType();
			Type listType2 = new TypeToken<Map<String, List<Ord_mealVO>>>() {
			}.getType();

			Map<String, OrdVO> ordlist = gson.fromJson(jsonObject.get("ordlist").getAsString(), listType);
			Map<String, List<Ord_mealVO>> itemlist = gson.fromJson(jsonObject.get("itemlist").getAsString(), listType2);

			String result = "fail";
			try {
				ordSvc.insertOrd(ordlist, itemlist);
				result = "success";

				MemberAppService memSvc = new MemberAppService();
				String mem_key = memSvc.getMemberKey(jsonObject.get("mem_no").getAsString());
				if (mem_key != null) {
					asyncSendSingle(mem_key, "New Order has been made",
							String.valueOf(GCMMessageType.新增的訂餐通知.ordinal()));
				}

				StoreAppService storeSvc = new StoreAppService();
				List<String> store_keys = new ArrayList<String>();
				for (String store_no : ordlist.keySet()) {
					String store_key = storeSvc.getStoreKey(store_no);
					if (store_key != null) {
						store_keys.add(store_key);
					}
				}
				if (store_keys.size() > 0) {
					asyncSend(store_keys, "New Order comming", String.valueOf(GCMMessageType.新增的訂餐通知.ordinal()));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			writeText(res, result);
		} else if ("getMemOrd".equals(action)) {
			String mem_no = jsonObject.get("mem_no").getAsString();
			Map<String, OrdVO> orderlist = ordSvc.getMemOrd(mem_no);

			writeText(res, gson.toJson(orderlist));
		} else if ("getMemOrdDetail".equals(action)) {
			String ord_no = jsonObject.get("ord_no").getAsString();
			Map<String, Ord_mealVO> meallist = ordSvc.getMemOrdMeal(ord_no);

			writeText(res, gson.toJson(meallist));
		} else if ("updateQrcodeStatus".equals(action)) {
			String ord_no = jsonObject.get("ord_no").getAsString();
			int type = jsonObject.get("ord_status").getAsInt();
			String result = "fail";
			try {
				ordSvc.updateQrcodeStatus(ord_no, type);
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
						asyncSendSingle(store_key, "Order cancelled", String.valueOf(GCMMessageType.消費者取消訂餐.ordinal()));
					}
					break;
				}
			}
			writeText(res, result);
		} else if ("getQrcode".equals(action)) {
			String ord_no = jsonObject.get("ord_no").getAsString();
			int imageSize = jsonObject.get("imageSize").getAsInt();

			byte[] qrcode = ordSvc.getQrcode(ord_no);

			if (qrcode != null) {
				qrcode = ImageUtil.shrink(qrcode, imageSize);
				res.setContentType("image/jpeg");
				res.setContentLength(qrcode.length);
				OutputStream os = res.getOutputStream();
				os.write(qrcode);
			}
		} else if ("getShopOrd".equals(action)) {
			String shop_no = jsonObject.get("shop_no").getAsString();
			Map<String, OrdVO> orderlist = ordSvc.getStoreOrd(shop_no);

			writeText(res, gson.toJson(orderlist));
		} else if ("ordGetOrdVO".equals(action)) {
			String ord_no = jsonObject.get("ord_no").getAsString().trim();
			String store_no = jsonObject.get("store_no").getAsString().trim();
			OrdVO ordVO = ordSvc.getOneOrd(ord_no);
			Map<String,OrdVO> result = null;
			
			if (!store_no.equals(ordVO.getStore_no())) {
				ordVO = null;
			}else{
				MemberAppService memSvc = new MemberAppService();
				MemberVO memberVO = memSvc.getOneMember(ordVO.getMem_no());
				String mem_name = memberVO.getMem_name().charAt(0)+(("1".equals(memberVO.getMem_sex()))?"先生":"小姐");
				result = new HashMap<String,OrdVO>();
				result.put(ord_no+","+mem_name, ordVO);
			}

			writeText(res, gson.toJson(result));
		} else if ("updatePaiedStatus".equals(action)) {
			String ord_no = jsonObject.get("ord_no").getAsString().trim();
			String result = "fail";
			try {
				ordSvc.updatePaiedStatus(ord_no);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			writeText(res, result);
		} else if ("getUnconfirm".equals(action)) {
			String store_no = jsonObject.get("store_no").getAsString().trim();
			String result = String.valueOf(ordSvc.getUnconfirm(store_no));

			writeText(res, result);
		}

	}

	private void writeText(HttpServletResponse res, String outText) throws IOException {
		res.setContentType(CONTENT_TYPE);
		PrintWriter out = res.getWriter();
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

	private void asyncSend(List<String> partialDevices, final String messageSend, String type) {
		final List<String> devices = new ArrayList<String>(partialDevices);
		threadPool.execute(new Runnable() {
			public void run() {
				Message message = new Message.Builder().addData("message", messageSend).addData("type", type).build();
				MulticastResult multicastResult;
				Sender sender = new Sender(SERVER_KEY);
				try {
					// after sending a message to many devices,
					// GCM server will return MulticastResult storing each
					// result for a device
					multicastResult = sender.send(message, devices, 5);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				List<Result> results = multicastResult.getResults();
				// analyze the results
				for (int i = 0; i < devices.size(); i++) {
					String regId = devices.get(i);
					Result result = results.get(i);
					String messageId = result.getMessageId();
					if (messageId != null) {
						System.out
								.println("Succesfully sent message to device: " + regId + "; messageId = " + messageId);
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
							devices.remove(regId);
							System.out.println("Unregistered device: " + regId);
						} else {
							System.out.println("Error sending message to " + regId + ": " + error);
						}
					}
				}
			}
		});
	}
}

package com.ieat.gcm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
import com.member.model.MemberAppService;
import com.store.model.StoreAppService;

import static com.ieat.gcm.MyData.*;

/*
 see 
 http://developer.android.com/google/gcm/server.html#params
 http://developer.android.com/google/gcm/adv.html#collapsible
 */
@SuppressWarnings("serial")
public class SendGcmNoticeServlet extends HttpServlet {

	private ServletContext context;
	private static Executor threadPool;

	@Override
	public void init() throws ServletException {
		super.init();
		context = getServletContext();
		int numberOfProcessors = Runtime.getRuntime().availableProcessors();
		threadPool = Executors.newFixedThreadPool(numberOfProcessors);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String action = req.getParameter("action");

		if ("orderSuccess".equals(action)) {
			String mem_no = req.getParameter("mem_no");
			String store_no = req.getParameter("store_no");

			MemberAppService memSvc = new MemberAppService();
			String mem_key = memSvc.getMemberKey(mem_no);

			if (mem_key != null) {
				String message = "a order has been made";
				asyncSendSingle(mem_key, message);
			}

			StoreAppService storeSvc = new StoreAppService();
			String store_key = storeSvc.getStoreKey(store_no);
			if (store_key != null) {
				String message = "a new order coming";
				asyncSendSingle(store_key, message);
			}

		}

//		List<String> devicesSend = new ArrayList<String>();
//		String[] regIds_send = req.getParameterValues("devicesSend");
//		String messageSend = req.getParameter("messageSend");
//
//		devicesSend = Arrays.asList(regIds_send);
//		// send a multicast message using JSON
//		// must split in chunks of 1000 devices (GCM limit)
//		int total = devicesSend.size();
//		List<String> partialDevices = new ArrayList<String>(total);
//		int counter = 0;
//		int tasks = 0;
//		for (String device : devicesSend) {
//			counter++;
//			partialDevices.add(device);
//			int partialSize = partialDevices.size();
//			// send messages when the number of devices >= 1000 or
//			// >= the total amount of devices to be sent
//			if (partialSize >= MULTICAST_SIZE || counter >= total) {
//				asyncSend(partialDevices, messageSend);
//				partialDevices.clear();
//				tasks++;
//			}
//		}

	}

	private void asyncSendSingle(String regId, final String messageSend) {
		threadPool.execute(new Runnable() {
			public void run() {
				Message message = new Message.Builder().addData("message", messageSend).build();
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

	private void asyncSend(List<String> partialDevices, final String messageSend) {
		final List<String> devices = new ArrayList<String>(partialDevices);
		threadPool.execute(new Runnable() {
			public void run() {
				Message message = new Message.Builder().addData("message", messageSend).build();
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

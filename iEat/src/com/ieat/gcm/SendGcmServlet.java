package com.ieat.gcm;

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

import static com.ieat.gcm.MyData.*;

/*
 see 
 http://developer.android.com/google/gcm/server.html#params
 http://developer.android.com/google/gcm/adv.html#collapsible
 */
@SuppressWarnings("serial")
public class SendGcmServlet extends HttpServlet {

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
	protected void doGet(HttpServletRequest rq, HttpServletResponse rp)
			throws ServletException, IOException {
		doPost(rq, rp);
	}

	@Override
	protected void doPost(HttpServletRequest rq, HttpServletResponse rp)
			throws ServletException, IOException {
		String msgDeviceNotSelected = "";
		String msgSendResult = "";
		List<String> devicesSend = new ArrayList<String>();
		String[] regIds_send = rq.getParameterValues("devicesSend");
		String messageSend = rq.getParameter("messageSend");
		if (regIds_send == null || regIds_send.length <= 0) {
			msgDeviceNotSelected = "No device is selected!";
		} else {
			devicesSend = Arrays.asList(regIds_send);
			// send a multicast message using JSON
			// must split in chunks of 1000 devices (GCM limit)
			int total = devicesSend.size();
			List<String> partialDevices = new ArrayList<String>(total);
			int counter = 0;
			int tasks = 0;
			for (String device : devicesSend) {
				counter++;
				partialDevices.add(device);
				int partialSize = partialDevices.size();
				// send messages when the number of devices >= 1000 or
				// >= the total amount of devices to be sent
				if (partialSize >= MULTICAST_SIZE || counter >= total) {
					asyncSend(partialDevices, messageSend);
					partialDevices.clear();
					tasks++;
				}
			}
			msgSendResult = "Asynchronously sending " + tasks
					+ " multicast messages to " + total + " device(s)";
		}

		String contentType = context.getInitParameter("contentType");
		rp.setContentType(contentType);
		PrintWriter out = rp.getWriter();
		out.println("<HTML>");
		out.println("<HEAD><TITLE>SendGcmServlet</TITLE></HEAD>");
		out.println("<style> "
				+ "table, th, td {border: 1px solid black; border-collapse: collapse; }"
				+ "th, td {padding: 15px;}" + "</style>");
		out.println("<BODY>");
		out.println("<H3>List of Registration ID of the Devices that Will Receive the Message</H3>");
		out.println("<P>" + msgDeviceNotSelected + "</P>");
		out.println("<FORM ACTION='SendGcmServlet' NAME='form' METHOD='post'>");
		out.println("<TABLE>");
		out.println("<TR><TH>Select</TH><TH>Registration ID of Registered Devices</TH></TR>");

		Set<String> devices = MyData.getDevices();
		if (devices != null && devices.size() > 0) {
			for (String regId : devices) {
				out.println("<TR>");
				out.println("<TD><INPUT TYPE='checkbox' NAME='devicesSend' VALUE='"
						+ regId + "'></TD>");
				out.println("<TD>" + regId + "</TD>");
				out.println("</TR>");
			}
		} else {
			out.println("<TR><TD colspan='2'>No device is registered!</TD></TR>");
		}

		out.println("</TABLE>");
		out.println("<BR><BR>");
		out.println("Message: <INPUT TYPE='text' NAME='messageSend'>");
		out.println("<INPUT TYPE='submit' NAME='submit' VALUE='Send' >");
		out.println("<INPUT TYPE='reset' NAME='reset' VALUE='Reset' >");
		out.println("</FORM>");
		out.println("<P>" + msgSendResult + "</P>");
		out.println("</BODY>");
		out.println("</HTML>");
		out.close();
	}

	private void asyncSend(List<String> partialDevices, final String messageSend) {
		final List<String> devices = new ArrayList<String>(partialDevices);
		threadPool.execute(new Runnable() {
			public void run() {
				Message message = new Message.Builder().addData("message",
						messageSend).build();
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
								.println("Succesfully sent message to device: "
										+ regId + "; messageId = " + messageId);
						String canonicalRegId = result
								.getCanonicalRegistrationId();
						if (canonicalRegId != null) {
							// same device has more than one registration id:
							// update it
							// see
							// https://developer.android.com/google/gcm/adv.html#canonical
							System.out.println("canonicalRegId: "
									+ canonicalRegId);
							MyData.updateRegistration(regId, canonicalRegId);
							System.out.println("Updating " + regId + " to "
									+ canonicalRegId);
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
							System.out.println("Error sending message to "
									+ regId + ": " + error);
						}
					}
				}
			}
		});
	}
}

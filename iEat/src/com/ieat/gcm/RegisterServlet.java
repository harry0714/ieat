package com.ieat.gcm;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;

import static com.ieat.gcm.MyData.*;

/*
 see 
 http://developer.android.com/google/gcm/server.html#params
 http://developer.android.com/google/gcm/adv.html#collapsible
 */
@SuppressWarnings("serial")
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

	private static final String PARAMETER_REG_ID = "regId";

	private class SingleDeviceThread extends Thread {
		private Message message;
		private String regId;

		public SingleDeviceThread(Message message, String regId) {
			this.message = message;
			this.regId = regId;
		}

		@Override
		public void run() {
			Sender sender = new Sender(SERVER_KEY);
			System.out.println("GCM sent!");
			try {
				sender.send(message, regId, RETRIES);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doPost(HttpServletRequest rq, HttpServletResponse rp)
			throws ServletException {
		final String regId = rq.getParameter(PARAMETER_REG_ID);
		System.out.println(regId);
		MyData.register(regId);

		final Message message = new Message.Builder().addData("message",
				"This device has registered to receive GCM messages!")
		// optional, if there
		// is already a message with the same collapse key (and registration ID)
		// stored and waiting for delivery, the old message will be
		// discarded and the new message will take its place
				// .collapseKey(collapseKey)

				// optional (default is 4 weeks)
				// .timeToLive(3)

				// optional (default is false), the message
				// should not be sent immediately if the device is idle
				// .delayWhileIdle(true)

				// optional (default is false), allows developers to
				// test their request without actually sending a message
				// .dryRun(true)

				// optional, messages will only be sent to registration IDs that
				// match the package name
				// .restrictedPackageName(restrictedPackageName)
				.build();
		new SingleDeviceThread(message, regId).start();
		rp.setStatus(HttpServletResponse.SC_OK);
		rp.setContentType("text/plain");
		rp.setContentLength(0);
	}
}

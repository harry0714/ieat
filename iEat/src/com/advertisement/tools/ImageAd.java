package com.advertisement.tools;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.advertisement.model.*;

public class ImageAd extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		OutputStream out = res.getOutputStream();
		try {
			String ad_no = req.getParameter("ad_no");
			AdvertisementService adSvc = new AdvertisementService();
			byte[] ad_image = adSvc.getAdImage(ad_no);
			res.setContentType("image/jpg");
			out.write(ad_image);
			out.flush();
			out.close();
			
		} catch (Exception e) {
			InputStream in = getServletContext().getResourceAsStream("/images/default.png");
			byte[] buf = new byte[in.available()];
			in.read(buf);
			out.write(buf);
			in.close();
			out.close();
		}
	}
}

package com.tools;

import java.io.IOException;
import java.io.*;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adm.model.AdmService;
import com.adm.model.AdmVO;

public class PhotoReader extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		OutputStream out = res.getOutputStream();
		try{
			String adm_no = req.getParameter("adm_no");
			System.out.println(adm_no);
			AdmService admSvc = new AdmService();
			byte[] adm_photo = admSvc.getAdmImage(adm_no);
			res.setContentType("image/jpg");
			out.write(adm_photo);
			out.flush();
			out.close();
		}catch(Exception e){
			InputStream in = getServletContext().getResourceAsStream("images/default.png");
			byte[] buf = new byte[in.available()];
			in.read(buf);
			out.write(buf);
			in.close();
		}
	}
}

package com.tools;

import java.io.IOException;
import java.util.List;
import java.io.*;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.store_photo.model.*;



public class StorePhotoReader2 extends HttpServlet {

	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		OutputStream out = res.getOutputStream();
		
		try{
			String store_no = req.getParameter("store_no");
			Integer rownum = new Integer(req.getParameter("rownum"));
			Store_photoService store_photoSvc = new Store_photoService();
			Store_photoVO store_photoVO = store_photoSvc.getOneStorePhoto(store_no,rownum);
			byte [] store_photo = store_photoVO.getPhoto();
			
			res.setContentType("image/jpg");
			out.write(store_photo);
			out.close();
		}catch(Exception e){
			InputStream in = getServletContext().getResourceAsStream("/images/default.png");
			byte[] buf = new byte[in.available()];
			in.read(buf);
			out.write(buf);
			in.close();
			out.close();
		}
	}

}

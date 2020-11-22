package com.tools;

import java.io.IOException;
import java.io.*;


import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.store_photo.model.*;


// 取得所有店家照片
public class StorePhotoReader extends HttpServlet {

	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		OutputStream out = res.getOutputStream();
		
		try{
			String photo_no = req.getParameter("photo_no");
			System.out.println(photo_no);
			Store_photoService store_photoSvc = new Store_photoService();
			Store_photoVO store_photoVO = store_photoSvc.getOneStore_photo(photo_no);
			
			
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

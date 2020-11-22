package com.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.store_photo.model.Store_photoService;
import com.store_photo.model.Store_photoVO;

// 只取店家第一張照片 (顯示全部用)
public class StorePhotoRead extends HttpServlet{
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		OutputStream out = res.getOutputStream();
		
		try{
			String photo_no = req.getParameter("store_no"); 
			Store_photoService store_photoSvc = new Store_photoService(); 
			Store_photoVO store_photoVO = store_photoSvc.getfindFirstStore(photo_no); 
			byte[] photo = store_photoVO.getPhoto(); 
			res.setContentType("image/jpg");
			out.write(photo);
			
			out.flush();
			out.close();
			
		} catch(Exception e) { 
			//若沒有抓到圖片  就給他一張圖
			InputStream in = getServletContext().getResourceAsStream("/images/default_image.jpeg"); 
				byte[] buf = new byte[in.available()]; 
				in.read(buf); 
				res.setContentType("image/jpg");
				out.write(buf); 
				in.close();
				out.close();
			}
		}
	}
	

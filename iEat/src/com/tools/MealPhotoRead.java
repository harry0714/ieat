package com.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.meal.model.MealService;
import com.meal.model.MealVO;

public class MealPhotoRead extends HttpServlet{
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		OutputStream out = res.getOutputStream();
		
		try{
			String photo_no = req.getParameter("photo_no");
			MealService mealSvc = new MealService(); 
			MealVO mealVO = mealSvc.getOneMeal(photo_no); 
			byte[] photo = mealVO.getMeal_photo(); 
			res.setContentType("image/jpg");
			out.write(photo);
			
			out.flush();
			out.close();
			
		} catch(Exception e) { 
			//若沒有抓到圖片  就給他一張圖
			InputStream in = getServletContext().getResourceAsStream("/images/default_image.jpeg"); 
				byte[] buf = new byte[in.available()];  //判斷圖片的大小
				in.read(buf); 
				res.setContentType("image/jpg");
				out.write(buf); 
				in.close();
				out.close();
			}
		}
	}
	

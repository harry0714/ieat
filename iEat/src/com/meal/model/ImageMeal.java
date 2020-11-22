package com.meal.model;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.advertisement.model.*;

public class ImageMeal extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		OutputStream out = res.getOutputStream();
		
		try {
			String meal_no = req.getParameter("meal_no");

			MealService mealSvc = new MealService();
			MealVO mealVO = mealSvc.getOneMeal(meal_no);

			byte[] image = mealVO.getMeal_photo();

			res.setContentType("image/jpg");
			out.write(image);
			out.flush();
			out.close();
			
		} catch (Exception e) {
			
			InputStream in = getServletContext().getResourceAsStream("/images/meal_preview.png");
			
			byte[] buf = new byte[in.available()];
			
			in.read(buf);
			out.write(buf);
			in.close();
			out.close();
		}
	}
}

package com.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meal.model.MealService;
import com.meal.model.MealVO;


@WebServlet("/meal/mealImageReader.do")
public class mealPicRead extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		OutputStream out = res.getOutputStream();

		try {
			String meal_no = req.getParameter("meal_no");
			MealService mealSvc = new MealService();
			MealVO mealVO = mealSvc.getOneMeal(meal_no);
			byte [] meal_photo = mealVO.getMeal_photo();
			res.setContentType("image/png");
			out.write(meal_photo);

			out.flush();
			out.close();

		} catch (Exception e) {
			// 若沒有抓到圖片 就給他一張圖
			InputStream in = getServletContext().getResourceAsStream("/images/default.png");
			byte[] buf = new byte[in.available()];
			in.read(buf);
			res.setContentType("image/jpg");
			out.write(buf);
			in.close();
			out.close();
		}
	}
}

package com.meal.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.meal.model.MealAppService;
import com.meal.model.MealVO;
import com.store.controller.ImageUtil;

/**
 * Servlet implementation class MealAppServlet
 */
@WebServlet("/MealAppServlet.do")
public class MealAppServlet extends HttpServlet {

	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		BufferedReader br = req.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		MealAppService mealSvc = new MealAppService();
		String action = jsonObject.get("action").getAsString();
		System.out.println("action:" + action);

		if ("getAllStoreMeals".equals(action)) {
			System.out.println(jsonObject.get("store_no").getAsString());
			Set<MealVO> meals = mealSvc.getFindByMeal(jsonObject.get("store_no").getAsString());
			System.out.println(meals.size());
			writeText(res, gson.toJson(meals));
		} else if ("getMealImage".equals(action)) {
			OutputStream os = res.getOutputStream();
			String meal_no = jsonObject.get("meal_no").getAsString();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = mealSvc.getMealImg(meal_no);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				res.setContentType("image/jpeg");
				res.setContentLength(image.length);
				os.write(image);
				os.close();
			}
		} else if ("getOneMeal".equals(action)) {
			System.out.println(jsonObject.get("meal_no").getAsString());
			MealVO meal = mealSvc.getOneAvailableMeal(jsonObject.get("meal_no").getAsString());
			writeText(res, gson.toJson(meal));

		} else if ("getTopMeal".equals(action)) {
			Map<String, MealVO> map = mealSvc.getTopMeals();
			writeText(res, gson.toJson(map));

		} else if ("updateMealStatus".equals(action)) {
			String meal_no = jsonObject.get("meal_no").getAsString();
			int status = jsonObject.get("status").getAsInt();
			String result = "fail";
			try {
				mealSvc.updateMealStatus(meal_no, status);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			writeText(res, result);

		} else if ("updateMeal".equals(action)) {
			String meal_name = jsonObject.get("meal_name").getAsString();
			String meal_descr = jsonObject.get("meal_descr").getAsString();
			int meal_price = jsonObject.get("meal_price").getAsInt();
			int meal_status = jsonObject.get("meal_status").getAsInt();
			String meal_no = jsonObject.get("meal_no").getAsString();
			String store_no = jsonObject.get("store_no").getAsString();
			
			
			String imageBase64 = jsonObject.get("imageBase64").getAsString();
			byte[] meal_photo = null;
			if (imageBase64.length()>0) {
				meal_photo = Base64.decodeBase64(imageBase64);
			}else if(meal_no.length()>0){
				meal_photo = mealSvc.getMealImg(meal_no);
			}


			String result = "fail";
			if (meal_no.length()>0) {
				mealSvc.updateMeal(meal_no, store_no, meal_photo, meal_name, meal_descr, meal_price, meal_status, 0);
				result = "success";
			} else {
				mealSvc.addMeal(store_no, meal_photo, meal_name, meal_descr, meal_price, meal_status, 0);
				result = "success";
			}

			writeText(res, result);
		}else if ("getStoreMeals".equals(action)) {
			System.out.println(jsonObject.get("store_no").getAsString());
			List<MealVO> meals = mealSvc.getStoreMeal(jsonObject.get("store_no").getAsString());
			System.out.println(meals.size());
			writeText(res, gson.toJson(meals));
		}
	}

	private void writeText(HttpServletResponse res, String outText) throws IOException {
		res.setContentType(CONTENT_TYPE);
		PrintWriter out = res.getWriter();
		out.print(outText);
	}
}

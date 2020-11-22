package com.bookmark_meal.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookmark_meal.model.BMAppService;
import com.bookmark_meal.model.Bookmark_MealVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.meal.model.MealAppService;
import com.meal.model.MealVO;
import com.store.model.StoreAppService;
import com.store.model.StoreVO;

@WebServlet("/Bookmark_MealAppServlet")
public class BMAppServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8"); // 先
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create(); // 把傳過來的資料轉換回來

		BufferedReader br = request.getReader();

		StringBuilder jsonIn = new StringBuilder(); // 把資料用BufferedReader的readLine()去取出來
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line); // 把資料用StringBuilder串起來
		}

		// 從JsonObject裡拿出Json字串
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class); // 這邊toString()是為了把他轉成String類別

		String action = jsonObject.get("action").getAsString(); // 取JsonObject裡面value的值，再把他轉成json字串

		System.out.println("action =" + action);

		BMAppService bookmark_mealSvc = new BMAppService();

		if (action.equals("bmGetAllByMem_no")) {
			String mem_no = jsonObject.get("mem_no").getAsString();

			List<String> list = bookmark_mealSvc.getMealNoByMemNo(mem_no);
			List<MealVO> meallist = null;

			if (list != null) {
				MealAppService mealSvc = new MealAppService();
				meallist = new ArrayList<MealVO>();
				for (String meal_no : list) {
					MealVO meal = mealSvc.getOneAvailableMeal(meal_no);
					if (meal != null) {
						meallist.add(meal);
					}
				}
			}

			writeText(response, gson.toJson(meallist));
			
		} else if (action.equals("bmDelete")) {
			
			String mem_no = jsonObject.get("mem_no").getAsString();
			String meal_no = jsonObject.get("meal_no").getAsString();
			String result = "fail";
			try {
				bookmark_mealSvc.deleteBookmark_Meal(mem_no, meal_no);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("一筆最愛餐點成功刪除");

			writeText(response, result);
			
		} else if (action.equals("bmAdd")) {
			String mem_no = jsonObject.get("mem_no").getAsString();
			String meal_no = jsonObject.get("meal_no").getAsString();
			String result = "fail";
			try {
				bookmark_mealSvc.addBookmark_Meal(mem_no, meal_no);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("一筆最愛餐點成功刪除");

			writeText(response, result);
		}
	}

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
	}

}

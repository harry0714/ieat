package com.bookmark_meal.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookmark_meal.model.Bookmark_MealService;

import com.meal.model.MealService;
import com.meal.model.MealVO;

public class Bookmark_MealServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		String action = (String) req.getParameter("action");
		
		if ("enterbookmarkmeal".equals(action)) {
			Map<String,String> errorMessage = new TreeMap<>();
			try {
				HttpSession session = req.getSession();
				List<String> bookmark_MealNo = (List<String>)session.getAttribute("bookmark_MealNo");
				
				MealService mealSvc = new MealService();
				List<MealVO> mealList = new ArrayList();
				for(String meal_no : bookmark_MealNo){
					mealList.add(mealSvc.getOneMeal(meal_no));
				}
				req.setAttribute("mealList", mealList);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/bookmarkmeal/bookmarkmeal.jsp");
				failureView.forward(req, res);
			} catch (Exception e) {
				errorMessage.put("elseError","有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/bookmarkmeal/bookmarkmeal.jsp");
				failureView.forward(req, res);
			}
		}
		
		if("delete".equals(action)){
			Map<String,String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			String requestURL = req.getParameter("requestURL"); // 送出刪除的來源網頁路徑
			try{
				HttpSession session = req.getSession();
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				String meal_no = (String)req.getParameter("meal_no");
				Bookmark_MealService bmmSvc = new Bookmark_MealService();
				bmmSvc.deleteBookmark_Meal(user.get("no"), meal_no);
				session.setAttribute("bookmark_MealNo",bmmSvc.getMealNoByMemNo(user.get("no")));
				
				if(requestURL.equals("/front-end/bookmarkmeal/bookmarkmeal.jsp")){
					List<String>  bookmark_MealNo = (List<String>)session.getAttribute("bookmark_MealNo");
					List<MealVO> mealList = new ArrayList<>();
					MealService mealSvc = new MealService();
					for(String meal_no1 : bookmark_MealNo){
						mealList.add(mealSvc.getOneMeal(meal_no1));
					}
					req.setAttribute("mealList", mealList);
				}
				
				String url = requestURL;
				RequestDispatcher successView = req.getRequestDispatcher(url); // 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
				
				
			}catch(Exception e){
				errorMessage.put("elseError", "有錯誤:"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}
		}
		
		
		//加入最愛
		if("add".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			String requestURL = (String)req.getParameter("requestURL");
			try{
				HttpSession session = req.getSession();
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				String meal_no = (String)req.getParameter("meal_no");
				Bookmark_MealService bmmSvc = new Bookmark_MealService();
				bmmSvc.addBookmark_Meal(user.get("no"), meal_no);
				session.setAttribute("bookmark_MealNo",bmmSvc.getMealNoByMemNo(user.get("no")));
				
				RequestDispatcher successView = req.getRequestDispatcher(requestURL); // 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
			}catch(Exception e){
				errorMessage.add(e.toString());
				RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}
		}
		
		if ("addAjax".equals(action)) {

			try {
				HttpSession session = req.getSession();
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				String meal_no = (String)req.getParameter("meal_no");
				Bookmark_MealService bmmSvc = new Bookmark_MealService();
				bmmSvc.addBookmark_Meal(user.get("no"), meal_no);
				session.setAttribute("bookmark_MealNo",bmmSvc.getMealNoByMemNo(user.get("no")));
				out.write("add");
				out.flush();
				out.close();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				out.write("error");
				out.flush();
				out.close();
			}

		}

		if ("deleteAjax".equals(action)) {

			try {
				HttpSession session = req.getSession();
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				String meal_no = (String)req.getParameter("meal_no");
				Bookmark_MealService bmmSvc = new Bookmark_MealService();
				bmmSvc.deleteBookmark_Meal(user.get("no"), meal_no);
				session.setAttribute("bookmark_MealNo",bmmSvc.getMealNoByMemNo(user.get("no")));
				out.write("delete");
				out.flush();
				out.close();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				out.write("error");
				out.flush();
				out.close();
			}

		}
	}
	


}

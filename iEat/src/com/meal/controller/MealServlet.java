package com.meal.controller;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

import com.meal.model.*;
import com.store_photo.model.Store_photoVO;

import net.sf.json.JSONObject;
@MultipartConfig(fileSizeThreshold=1024*1024,
maxFileSize=5*1024*1024,maxRequestSize=5*5*1024*1024)
public class MealServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		HttpSession session = req.getSession();
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		Map<String,Map<String,Integer>> shoppinglist = (Map<String,Map<String,Integer>>) session.getAttribute("shoppinglist");
		
		
		if ("getOne_For_Display".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
			try{
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String str = req.getParameter("meal_no");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入餐點編號");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/meal/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				String meal_no = null;
				try {
					meal_no = new String(str);
				} catch (Exception e) {
					errorMsgs.add("餐點編號格式不正確");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/meal/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				/***************************2.開始查詢資料*****************************************/
				MealService mealSvc = new MealService();
				MealVO mealVO = mealSvc.getOneMeal(meal_no);
				
				if (mealVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/meal/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("mealVO", mealVO); // 資料庫取出的mealVO物件,存入req
				String url = "/meal/listOneMeal.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneEmp.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
				
			}catch (Exception e){
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/meal/select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
		/************************ 店家專區  取得要修改的餐點 *****************************/ 
		if ("getOne_For_Update".equals(action)) { // 來自listAllEmp.jsp 或  /dept/listEmps_ByDeptno.jsp 的請求

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
				/***************************1.接收請求參數****************************************/
				String meal_no = req.getParameter("meal_no").trim();
				
				/***************************2.開始查詢資料****************************************/
				MealService mealSvc = new MealService();
				MealVO mealVO = mealSvc.getOneMeal(meal_no);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("mealVO", mealVO); 
				JSONObject json = new JSONObject();
				json.put("meal_no", mealVO.getMeal_no());	
				json.put("store_no", mealVO.getStore_no());	
				json.put("meal_name", mealVO.getMeal_name());	
				json.put("meal_price", mealVO.getMeal_price());	
				json.put("meal_status", mealVO.getMeal_status());
				json.put("meal_descr", mealVO.getMeal_descr());	
				json.put("meal_discount", mealVO.getMeal_discount());	
				json.put("meal_photo", mealVO.getMeal_photo());	
				
				res.setContentType("text/plain");
				res.setCharacterEncoding("UTF-8");
				PrintWriter out = res.getWriter();
				out.write(json.toString());
				out.flush();
				out.close();
		}
		
		if ("update".equals(action)) { // 來自update_emp_input.jsp的請求
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			session = req.getSession();
			Map<String, String> store = (Map<String, String>) session.getAttribute("store");
			String store_no = store.get("store_no"); //從session取得店家主鍵
			
			MealService mealSvc = new MealService(); 
			
//			try {
				/**************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
				String meal_no = req.getParameter("meal_no").trim();
				String meal_name = req.getParameter("meal_name").trim();
				String meal_descr = req.getParameter("meal_descr").trim();
				Integer meal_status = new Integer(req.getParameter("meal_status"));			
				Integer meal_price = null;
				try {
					meal_price = new Integer(req.getParameter("meal_price").trim());
				} catch (NumberFormatException e) {
					meal_price = 0;
					errorMsgs.add("餐點價格請填數字");
				}
				
				int meal_discount = 0;

				MealVO mealVO = mealSvc.getOneMeal(meal_no);				
				Part part = req.getPart("meal_photo");
				InputStream in = part.getInputStream();
				byte[] meal_photo = new byte[in.available()]; 
				if(meal_photo.length==0) { //若沒有新增圖片，抓回資料庫的原始圖檔
					meal_photo = mealVO.getMeal_photo(); 
				}
				else{
					in.read(meal_photo); 
					in.close();
				}				
				mealVO = new MealVO();
				mealVO.setMeal_no(meal_no);
				mealVO.setMeal_discount(meal_discount);
				mealVO.setMeal_name(meal_name);
				mealVO.setMeal_descr(meal_descr);
				mealVO.setMeal_photo(meal_photo);
				mealVO.setMeal_status(meal_status);
				mealVO.setMeal_price(meal_price);
					
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("mealVO", mealVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/update_meal_input.jsp");
					failureView.forward(req, res);
					return; // 程式中斷
				}

				/***************************
				 * 2.開始修改資料
				 *****************************************/
				mealSvc = new MealService();
					mealVO = mealSvc.updateMeal(meal_no, store_no, meal_photo, meal_name, meal_descr, meal_price, meal_status, meal_discount);
				/***************************
				 * 3.修改完成,準備轉交(Send the Success view)
				 *************/
				
				req.setAttribute("mealVO", mealVO);
				 // 資料庫update成功後,正確的的empVO物件,存入req
				String url = "/front-end/store/listAllMeal.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneEmp.jsp
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 *************************************/
//			} catch (Exception e) {
//				errorMsgs.add("修改資料失敗:" + e.getMessage());
//				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/update_meal_input.jsp");
//				failureView.forward(req, res);
//			}
		}
		
//		if("insert".equals(action)){ // 來自addMeal.jsp的請求
//			System.out.println("action="+action);
//			List<String> errorMsgs = new LinkedList<String>();
//			// Store this set in the request scope, in case we need to
//			// send the ErrorPage view.
//			req.setAttribute("errorMsgs", errorMsgs);
//			
//			try {
//			/***************************1.接收請求參數 - 輸入格式的錯誤處理****************************************/
//			String store_no = req.getParameter("store_no").trim();
//			
//			Part part = req.getPart("meal_photo");
//			
//			String meal_name = req.getParameter("meal_name").trim();
//			
//			String meal_descr = req.getParameter("meal_descr").trim();
//			
//			
//			Integer meal_price = null;
//			
//			try {
//				meal_price = new Integer(req.getParameter("meal_price").trim());
//			} catch (NumberFormatException e) {
//				meal_price = 0;
//				errorMsgs.add("餐點價格請填數字.");
//			}
//			
//			Integer meal_status = null;
//			
//			try {
//				meal_status = new Integer(req.getParameter("meal_status").trim());
//			} catch (NumberFormatException e) {
//				meal_status = 1;  //1代表未上架
//				errorMsgs.add("餐點上架狀態請填數字.");
//			}
//			
//			Integer meal_discount = null;
//			
//			try {
//				meal_discount = new Integer(req.getParameter("meal_discount").trim());
//			} catch (NumberFormatException e) {
//				meal_discount = 0;  
//				errorMsgs.add("優惠價請填數字.");
//			}
//				
//			MealVO mealVO  = new MealVO();
//			mealVO.setStore_no(store_no);
//			mealVO.setMeal_name(meal_name);
//			mealVO.setMeal_descr(meal_descr);
//			mealVO.setMeal_price(meal_price);
//			mealVO.setMeal_status(meal_status);
//			mealVO.setMeal_discount(meal_discount);
//			
//			// Send the use back to the form, if there were errors
//			if(!errorMsgs.isEmpty()){
//				req.setAttribute("mealVO",mealVO);  // 含有輸入格式錯誤的empVO物件,也存入req
//				RequestDispatcher failureView = req
//						.getRequestDispatcher("/meal/addMeal.jsp");
//				failureView.forward(req, res);
//				return;
//			}
//			/***************************2.開始新增資料***************************************/
//			InputStream in = part.getInputStream();
//			byte[] meal_photo = new byte[in.available()];
//			in.read(meal_photo);	
//			in.close();
//			
//			MealService mealSvc = new MealService();
//			mealVO = mealSvc.addMeal(store_no,meal_photo,meal_name,meal_descr, meal_price, meal_status, meal_discount);
//			
//			/***************************3.新增完成,準備轉交(Send the Success view)***********/
//			String url = "/meal/listAllMeal.jsp";
//			
//			RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllMeal.jsp
//			successView.forward(req, res);				
//			
//			/***************************其他可能的錯誤處理**********************************/
//			}catch (Exception e){
//				errorMsgs.add(e.getMessage());
//				RequestDispatcher failureView = req
//						.getRequestDispatcher("/meal/addMeal.jsp");
//				failureView.forward(req, res);
//			}
//		}
		
		if ("insert".equals(action)) { // 來自addEmp.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				Map<String, String> store = (Map<String, String>) session.getAttribute("store");
				String store_no = store.get("store_no"); //從session取得店家主鍵
				
				String meal_name = req.getParameter("meal_name").trim();
				String meal_descr = req.getParameter("meal_descr").trim();
				Integer meal_status = new Integer(req.getParameter("meal_status").trim());
				Integer meal_price = null;
				try {
					meal_price = new Integer(req.getParameter("meal_price").trim());
				} catch (NumberFormatException e) {
					meal_price = 0;
					errorMsgs.add("餐點價格請填數字");
				}
				int meal_discount = 0;

				Part part = req.getPart("meal_photo");
				InputStream is = part.getInputStream();

				byte[] meal_photo = new byte[is.available()];
				is.read(meal_photo);
				is.close();
				
				MealVO mealVO = new MealVO();
				mealVO.setMeal_name(meal_name);
				mealVO.setStore_no(store_no);
				mealVO.setMeal_descr(meal_descr);
				mealVO.setMeal_discount(meal_discount);
				mealVO.setMeal_status(meal_status);
				mealVO.setMeal_price(meal_price);
				mealVO.setMeal_photo(meal_photo);
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("mealVO", mealVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/listAllMeal.jsp");
					failureView.forward(req, res);
					return;
				}

				MealService mealSvc = new MealService();
				mealVO = mealSvc.addMeal(store_no, meal_photo, meal_name, meal_descr, meal_price, meal_status, meal_discount);
				
				String url = "/front-end/store/listAllMeal.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/listAllMeal.jsp");
				failureView.forward(req, res);
			}
		}
		
		if("delete".equals(action)){ // 來自listAllMeal.jsp
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數***************************************/
				String meal_no = new String(req.getParameter("meal_no"));
				
				/***************************2.開始刪除資料***************************************/
				MealService MealSvc = new MealService();
				MealSvc.deleteMeal(meal_no);
				
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
				String url = "/meal/listAllMeal.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/meal/listAllMeal.jsp");
				failureView.forward(req, res);
			}
		}
		/***********************來自店家專區  餐點下架*************************/
		if ("get_one_meal_status_down".equals(action)) { // �Ӧ�select_page.jsp���ШD
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
		
				String meal_no = req.getParameter("meal_no");
				Integer meal_status = 1;
				System.out.println(meal_no);
				/***************************2.�}�l�d�߸��*****************************************/
				MealService mealSvc = new MealService();
				MealVO mealVO = mealSvc.updateMealStatus(meal_status,meal_no);
				
				req.setAttribute("mealVO", mealVO);
				/***************************3.�d�ߧ���,�ǳ����(Send the Success view)*************/
				String url = "/front-end/store/listAllMeal.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // ���\��� listOneEmp.jsp
				successView.forward(req, res);
			}
		/********************** 來自店家專區 餐點上架 *************************/ 
		if ("get_one_meal_status".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
				String meal_no = req.getParameter("meal_no");
				Integer meal_status = 0;
				
				MealService mealSvc = new MealService();
				MealVO mealVO = mealSvc.updateMealStatus(meal_status,meal_no);
				req.setAttribute("mealVO", mealVO);
				String url = "/front-end/store/listAllMeal.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // ���\��� listOneEmp.jsp
				successView.forward(req, res);
			}
	 }
   }


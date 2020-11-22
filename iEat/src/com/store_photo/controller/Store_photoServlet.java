package com.store_photo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.store_photo.model.Store_photoService;
import com.store_photo.model.Store_photoVO;

import net.sf.json.JSONObject;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)
public class Store_photoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res); 
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8" );
		String action = req.getParameter("action"); 
		
		/*****************查詢單筆照片資料*******************/ 
		if("getOne_For_Display".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>(); 
			req.setAttribute("errorMsgs", errorMsgs);
			
			try{
				/*************************接收請求參數，輸入格式錯誤處理******************/
				String str = req.getParameter("photo_no"); 
				if(str == null || (str.trim()).length()==0) {
					errorMsgs.add("請輸入照片編號"); 
				}
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store_photo/select_page.jsp"); 
					failureView.forward(req, res); 
					return; //程式中斷
				}
				if(!str.matches("[P]{1}[0-9]{9}")) {
					errorMsgs.add("照片編號格式不正確，需為P開頭，後面有9位數字"); 
				}
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store_photo/select_page.jsp"); 
					failureView.forward(req, res); 
					return; //程式中斷
				}
				String photo_no = str; 
				/*****************格式無錯誤  開始查詢資料(永續層存取)************************/
				Store_photoService store_photoSvc = new Store_photoService(); 
				Store_photoVO store_photoVO = store_photoSvc.getOneStore_photo(photo_no); 
				if(store_photoVO == null) {
					errorMsgs.add("查無資料"); 
				}
				if(!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store_photo/select_page.jsp"); 
					failureView.forward(req, res); 
					return; //程式中斷
				}
				/************************查詢完成  準備轉交*******************/
				req.setAttribute("store_photoVO", store_photoVO);
				String url = "/front-end/store_photo/listOneStore_photo.jsp"; 
				RequestDispatcher successView = req.getRequestDispatcher(url); 
				successView.forward(req, res); 
				/******************其他可能的錯誤處理**************************/
			}  catch(Exception e) {
				errorMsgs.add("無法取得資料：" +  e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/select_page.jsp"); 
				failureView.forward(req, res);
			}
		}
		
		
		/************************取得一筆要修改的資料*********************************/
		if("getOne_For_Update".equals(action)) { //來自listAllStore_photo.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); 
			req.setAttribute("errorMsgs", errorMsgs);
			try{
				/**************1.接收請求參數***************************/
				String photo_no = new String(req.getParameter("photo_no")); 
				/**************2. 開始查詢資料****************************/
				Store_photoService store_photoSvc = new Store_photoService();
				Store_photoVO store_photoVO = store_photoSvc.getOneStore_photo(photo_no); 
				/*****************3.查詢完成  準備轉交*********************/ 
				req.setAttribute("store_photoVO", store_photoVO); //資料庫取出的store_photoVO物件，存入req
				JSONObject json = new JSONObject();
				json.put("photo_no", store_photoVO.getPhoto_no());
				json.put("photo_name", store_photoVO.getPhoto_name());
				json.put("photo_des", store_photoVO.getPhoto_des()); 
				json.put("store_no", store_photoVO.getPhoto_no());
				json.put("photo", store_photoVO.getPhoto()); 
				
				res.setContentType("text/plain");
				res.setCharacterEncoding("utf-8");
				PrintWriter out = res.getWriter();
				out.write(json.toString());
				out.flush();
				out.close();
				/*****************************其他可能的錯誤處理**********************/
			} catch (Exception e) {
				errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/store/listOneStorePhoto.jsp");
				failureView.forward(req, res);
			}
		}
		
		/***************開始修改資料*******************************/
		if("update".equals(action)) { //來自update_store_photo_input.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); 
			req.setAttribute("errorMsgs", errorMsgs);
			
//			try{
				/**********************1. 接收請求參數-輸入格式錯誤的處理******************/
				String photo_no = new String(req.getParameter("photo_no").trim()); 
				String photo_name = req.getParameter("photo_name").trim();
				String photo_des = req.getParameter("photo_des").trim(); 		
				String store_no = req.getParameter("store_no").trim(); 
				System.out.println("store_no+++++++++++++++++++++++++++"+store_no);
				Store_photoService store_photoSvc = new Store_photoService(); 
				Store_photoVO store_photoVO = store_photoSvc.getOneStore_photo(photo_no); 
				Part part = req.getPart("photo");
								
				InputStream in = part.getInputStream();
				byte[] photo = new byte[in.available()]; 
				if(photo.length==0) { //若沒有新增圖片，抓回資料庫的原始圖檔 
					photo = store_photoVO.getPhoto(); 
				}
				else{
					in.read(photo); 
					in.close();
				}
				if(photo_name.length()==0) {
					errorMsgs.add("請輸入照片名稱");
				}
				
				store_photoVO.setPhoto_name(photo_name);
				store_photoVO.setPhoto(photo);
				store_photoVO.setPhoto_des(photo_des);
				store_photoVO.setStore_no(store_no);
				store_photoVO.setPhoto_no(photo_no); 
			
				if(!errorMsgs.isEmpty()) {
					req.setAttribute("store_photoVO", store_photoVO); 
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/listOneStorePhoto.jsp"); 
					failureView.forward(req, res); 
					return; //程式中斷
				}				
			/************************2. 開始修改資料**********************/
			store_photoVO = store_photoSvc.updateStore_photo(photo_no, store_no, photo_name, photo, photo_des); 
			/***************************3.修改完成，準備轉交資料********************/
			req.setAttribute("store_photoVO", store_photoVO); 
			String url ="/front-end/store/listOneStorePhoto.jsp"; 
			RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneEmp.jsp
			successView.forward(req, res);			
			/**************************其他可能的錯誤處理*****************************/
//			} catch (Exception e) {
//				errorMsgs.add("修改資料失敗:"+e.getMessage());
//				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/listOneStorePhoto.jsp");
//				failureView.forward(req, res);
//			}
		}
		
//		/***********************新增資料*******************************/
//		if("insert".equals(action)) { //來自addStore_photo.jsp的請求
//			List<String> errorMsgs = new LinkedList<String>(); 
//			req.setAttribute("errorMsgs", errorMsgs);
//			
//			try{
//				/********************1.接受請求參數，輸入格式的錯誤處理***********************/
//				String photo_name = req.getParameter("photo_name").trim();
//				String photo_des = req.getParameter("photo_des").trim(); 				
//				Part part = req.getPart("photo");
//				
//				InputStream in = part.getInputStream();
//				byte[] photo = new byte[in.available()]; 
//				if(photo.length==0) {
//					errorMsgs.add("請選擇要新增的圖片");
//				}
//				else{
//					in.read(photo); 
//					in.close();
//				}
//				if(!errorMsgs.isEmpty()) {
//					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/listOneStorePhoto.jsp"); 
//					failureView.forward(req, res); 
//					return; //程式中斷
//				}
//				if(photo_name.length()==0) {
//					errorMsgs.add("請輸入照片名稱");
//				}
//				if(!errorMsgs.isEmpty()) {
//					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/listOneStorePhoto.jsp"); 
//					failureView.forward(req, res); 
//					return; //程式中斷
//				}
//				String store_no = new String(req.getParameter("store_no").trim()); 
//				
//				Store_photoVO store_photoVO = new Store_photoVO(); 
//				store_photoVO.setPhoto_name(photo_name);
//				store_photoVO.setPhoto(photo);
//				store_photoVO.setPhoto_des(photo_des);
//				store_photoVO.setStore_no(store_no);
//				
//				/**************2. 開始新增資料  永續層存取******************/
//				Store_photoService store_photoSvc = new Store_photoService();
//				store_photoVO = store_photoSvc.addStore_photo(store_no, photo_name, photo, photo_des); 
//				/**********************3.新增完成，準備轉交******************/
//				String url = "/front-end/store/listOneStorePhoto.jsp"; 
//				RequestDispatcher successView = req.getRequestDispatcher(url); 
//				successView.forward(req, res);	
//				/********************其他可能的錯誤處理********************/
//			} catch(Exception e) {
//				errorMsgs.add(e.getMessage());
//				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/listOneStorePhoto.jsp"); 
//				failureView.forward(req, res);
//			}
//		}
		
		if ("insert".equals(action)) { // 來自addEmp.jsp的請求  
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				String store_no = req.getParameter("store_no").trim();
				System.out.println("Servlete store_no==========="+store_no);
				String photo_name = req.getParameter("photo_name").trim();
				System.out.println("Servlete photo_name==========="+photo_name);
				String photo_des = req.getParameter("photo_des").trim();
				Part part = req.getPart("photo");
				InputStream in = part.getInputStream();
				byte[] photo = new byte[in.available()]; 
					in.read(photo); 
					in.close();
				Store_photoVO store_photoVO = new Store_photoVO(); 
			
				store_photoVO.setPhoto_des(photo_des);
				store_photoVO.setPhoto(photo);
				store_photoVO.setStore_no(store_no);
				store_photoVO.setPhoto_name(photo_name);
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("store_photoVO", store_photoVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/listOneStorePhoto.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************2.開始新增資料***************************************/
				Store_photoService store_photoSvc = new Store_photoService();
				store_photoVO = store_photoSvc.addStore_photo(store_no, photo_name, photo, photo_des);
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				String url = "/front-end/store/listOneStorePhoto.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
				successView.forward(req, res);				
				
				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/store/listOneStorePhoto.jsp");
				failureView.forward(req, res);
			}
		}		
		
		
		/***********************刪除資料 僅供店家刪除店家的照片*****************************/
		if("delete".equals(action)) {  //來自listAllStore_photo.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); 
			req.setAttribute("errorMsgs", errorMsgs);
			
			// String requestURL = req.getParameter("requestURL"); //送出刪除的來源網頁路徑 
			
			try {
				/**************************************接收請求參數****************/
				String photo_no = new String(req.getParameter("photo_no")); 
				/************************開始刪除資料********************************/
				Store_photoService store_photoSvc = new Store_photoService(); 
				store_photoSvc.deleteStore_photo(photo_no);
				/**************3. 刪除完畢  準備轉交********************************/
				String url = "/front-end/store/listOneStorePhoto.jsp"; 
				RequestDispatcher successView = req.getRequestDispatcher(url); 
				successView.forward(req, res);
				/*****************其他可能的錯誤處理*****************************/				
			} catch(Exception e) {
				errorMsgs.add("刪除資料失敗："+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/listOneStorePhoto.jsp"); 
				failureView.forward(req, res);
			}
		}
	}
}

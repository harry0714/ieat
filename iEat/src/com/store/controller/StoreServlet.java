package com.store.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.discount.model.DiscountVO;
import com.email.model.MailService;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.reservation.model.ReservationVO;
import com.store.model.*;
import com.store_comment.model.StoreCommentVO;
import com.store_photo.model.Store_photoService;
import com.store_photo.model.Store_photoVO;
import com.store_report.model.Store_reportVO;
import com.store_type.model.Store_typeService;

import net.sf.json.JSONObject;

public class StoreServlet extends HttpServlet { 
	public void doGet(HttpServletRequest req, HttpServletResponse res) 
	throws ServletException, IOException {
		doPost(req, res); 
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8"); 
		String action = req.getParameter("action"); 

		
		/**************************** 按下我要訂位  取出資料 進入訂位的jsp檔內 ************************/ 
		if("get_reservation_info".equals(action)) {
				// 從Session 取得店家是否登入
				HttpSession session = req.getSession();
				Map<String, String> store = (Map<String, String>) session.getAttribute("store");
				// 若店家為登入狀態  重導回前一頁
				if(store!=null) { 
					String url = req.getParameter("servletPath"); 
					req.setAttribute("noStore", "noStore");
					RequestDispatcher failureView = req.getRequestDispatcher(url); 
					failureView.forward(req, res);
					return; 
				}
		
				/*************無錯誤 開始查詢資料************************/
				String store_no = req.getParameter("store_no"); 
				StoreService storeSvc = new StoreService(); 
				StoreVO storeVO = storeSvc.getOneStore(store_no); 
				String[] store_booking = storeVO.getStore_booking().split("-"); //取得開放訂位時段  切割成字串
				Set<ReservationVO> set = storeSvc.getReservationByStore_no(store_no); //根據店家主鍵  查詢出所有的訂位紀錄
				req.setAttribute("listReservationByStore_no", set);								
				
				/****************查詢完成 準備轉交***************************/
				req.setAttribute("storeVO", storeVO);
				req.setAttribute("store_booking", store_booking);
				String url = "/front-end/reservation/Reservations.jsp"; 
				RequestDispatcher successView = req.getRequestDispatcher(url); 
				successView.forward(req, res); 
		}
		
		
		/****************************根據店家主鍵查詢店家照片***********************************/
		if("listOnePhoto".equals(action)) {
			try{
				
				HttpSession session = req.getSession(); 
				Map<String, String> store = (Map<String, String>) session.getAttribute("store");
				StoreService storeSvc = new StoreService(); 
				String store_no = store.get("store_no"); 
				Set<Store_photoVO> set = storeSvc.getStore_photosByStore_no(store_no);
				
				System.out.println(store_no);
				
				req.setAttribute("listStore_photoByStore_no", set); //資料庫取出的set物件，存入request
				String url = "/front-end/store/update_photo.jsp"; 
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
				 
		
//		// 來自select_page.jsp的請求                                      // 來自 store/listAllDept.jsp的請求
//		if("listStore_photo_ByStore_no_A".equals(action) || "listStore_photo_ByStore_no_B".equals(action)) {
//			List<String> errorMsgs = new LinkedList<String>();
//			req.setAttribute("errorMsgs", errorMsgs);
//		try{
//				/**********************1. 接收請求參數********************************/
//				String store_no = new String(req.getParameter("store_no"));
//				// System.out.println(store_no);
//				/**********************2.開始查詢資料*********************************/
//				StoreService storeSvc = new StoreService(); 
//				Set<Store_photoVO> set = storeSvc.getStore_photosByStore_no(store_no);
//				/**********************3. 查詢完成，準備轉交(Send the Success View)******************/
//				req.setAttribute("listStore_photoByStore_no", set); //資料庫取出的set物件，存入request
//				String url = null; 
//				if("listStore_photo_ByStore_no_A".equals(action)) {
//					url = "/front-end/store/listStore_photo_ByStore_no.jsp";
//				}
//				else if("listStore_photo_ByStore_no_B".equals(action)) {
//					url = "/front-end/store/listAllStore.jsp";
//				}
//					 
//				RequestDispatcher successView = req.getRequestDispatcher(url);
//				successView.forward(req, res);				
//			} catch (Exception e) {
//				throw new ServletException(e);
//			}
//		}
		
		/************************根據店家主鍵去查詢店家的檢舉資訊****************************/ 
		if("listStore_report_ByStore_no_A".equals(action) || "listStore_report_ByStore_no_B".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try{
				/*******************************1. 接收請求參數*******************************/
				String store_no = new String(req.getParameter("store_no")); 
				/*******************************2. 開始查詢資料*******************************/
				StoreService storeSvc = new StoreService(); 
				Set<Store_reportVO> set = storeSvc.getStore_reportByStore_no(store_no); 
				
				/*******************************3. 查詢完成，準備轉交*********************/ 
				req.setAttribute("listStore_report_ByStore_no", set); 
				
				String url = null; 				
				if("listStore_report_ByStore_no_A".equals(action)) 
					url="/front-end/store/listStore_report_ByStore_no.jsp";
				if("listStore_report_ByStore_no_B".equals(action)) 
					url="/front-end/store/listAllStore.jsp"; //轉交給原畫面
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);				
				/************************** 其他可能的錯誤處理**********************/
			} catch(Exception e){
				throw new ServletException(e);
			}
		}
		
		/*****************************根據店家主鍵 查詢店家的訂位紀錄****************/
		if("listReservation_ByStore_no_A".equals(action) || "listReservations_ByStore_no_B".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try{
				/*****************************1. 接收請求參數 *****************************/ 
				String store_no = new String(req.getParameter("store_no"));
				/*****************************2. 開始查詢資料******************************/
				StoreService storeSvc = new StoreService(); 
				Set<ReservationVO> set = storeSvc.getReservationByStore_no(store_no); 
				/*****************************3.查詢完成 準備轉交 *************************/
				req.setAttribute("listReservationByStore_no", set); 
				String url = null;  
						if("listReservation_ByStore_no_A".equals(action)) 
							url="/front-end/store/listReservationByStore_no.jsp"; 
						if("listReservations_ByStore_no_B".equals(action)) 
							url = "/front-end/store/listAllStore.jsp"; //轉交回原畫面
						RequestDispatcher successView = req.getRequestDispatcher(url); 
						successView.forward(req, res);
			} catch(Exception e) {
				throw new ServletException(e);
			}
		}
		
		/*********************查詢單筆店家資料  來自listAll 不需要錯誤處理******************************/
		if("getOne_For_Display".equals(action)) { //來自Restaurants.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); 
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***********接收請求參數，輸入格式錯誤處理***************************************/
				String store_no = req.getParameter("store_no").trim(); 
				/*************無錯誤 開始查詢資料************************/
				StoreService storeSvc = new StoreService(); 
				StoreVO storeVO = storeSvc.getOneStoreAndStoreStar(store_no); 
				Set<Store_photoVO> set = storeSvc.getStore_photosByStore_no(store_no); //根據店家主鍵 查詢出店家照片
				String[] store_open = storeVO.getStore_open().split("-");  //將字串先在Servlet這邊切好 以免forTokens出問題
				req.setAttribute("store_open", store_open);
				req.setAttribute("listStore_photoByStore_no", set);
				List<StoreCommentVO> scList = storeSvc.getByStoreNo(store_no);
				req.setAttribute("scList", scList);
				List<DiscountVO> disList = storeSvc.getDiscountByStoreNo(store_no);
				req.setAttribute("disList", disList);
				/****************查詢完成 準備轉交***************************/
				req.setAttribute("storeVO", storeVO); 
				String servletPath = req.getServletPath()+"?"+req.getQueryString();
				req.setAttribute("servletPath", servletPath); //取得此網頁的路徑 可以重導回來
				String url = "/front-end/store/display_oneStore.jsp"; 
				RequestDispatcher successView = req.getRequestDispatcher(url); 
				successView.forward(req, res); 
				/****************其他可能錯誤處理**************************/
			} catch(Exception e) {
				errorMsgs.add("無法取得資料：" +  e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/restaurants/Restaurants.jsp"); 
				failureView.forward(req, res);
			}
		}
		
		/************************取得一筆店家的資料  進行修改 店家主鍵來自session*******************************/
		if("getOne_For_Update".equals(action)) {
			Map<String, String> errorMsgs = new TreeMap<>(); 
			// 把errorMsgs設在request scope內
			req.setAttribute("errorMsgs", errorMsgs); 
			
			try{ 
				/***************************1.接收請求參數****************************************/
				HttpSession session = req.getSession(); 
				Map<String, String> store = (Map<String, String>) session.getAttribute("store"); 
				System.out.println(store);
// 				String store_no = new String(req.getParameter("store_no")); 
				
				/***************************2.開始查詢資料，修改這邊 不需要錯誤驗證****************************************/
				StoreService storeSvc = new StoreService(); 
				StoreVO storeVO = storeSvc.getOneStore(store.get("store_no"));   //從session取得資料 從店家主鍵 查詢資料
				
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("storeVO", storeVO);
				//重設地址
				String store_ads =  storeVO.getStore_ads(); 
				String store_open = storeVO.getStore_open(); 
				String store_booking = storeVO.getStore_booking(); 
				String store_book_amt = storeVO.getStore_book_amt(); 
				// 重設店家營業時間  開放訂位時間  開放訂位人數
				String[] open = store_open.split("-"); 
				req.setAttribute("time", open);
				String[] book = store_booking.split("-"); 
				for(int i=0; i<book.length; i++) {
					req.setAttribute("bookOrNot"+i, book[i]);
				}
				String[] book_amt = store_book_amt.split("-"); 
				for(int i=0; i<book_amt.length; i++) {
					req.setAttribute("bookAmt"+i, book_amt[i]);
				}
				//重設店家地址
				String split_address[] = store_ads.split("-"); 
				req.setAttribute("zip", split_address[0]);
				req.setAttribute("county", split_address[1]);
				req.setAttribute("city", split_address[2]);
				req.setAttribute("address", split_address[3]);
				String url = "/front-end/store/storeUpdate.jsp"; 
				RequestDispatcher successView = req.getRequestDispatcher(url); //轉交給update_store_input 
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			} catch(Exception e) {
				errorMsgs.put("elseErro", "無法取得要修改的資料\n" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/personal.jsp"); 
				failureView.forward(req,res); 
			}
		}
		
		/******************************來自店家專區 店家基本資料修改***********************************/
		if("store_update".equals(action)) {
			HttpSession session = req.getSession(); 
			Map<String,String> errorMsgs = new HashMap<String,String>();  			
			Map<String, String> store = (Map<String, String>) session.getAttribute("store");
			StoreService storeSvc = new StoreService(); 
			StoreVO storeVO = storeSvc.getOneStore(store.get("store_no"));  
			
			try {				
				String store_no = req.getParameter("store_no"); 
				String store_id = req.getParameter("store_id").trim(); 
				String store_psw = req.getParameter("store_psw").trim();
				String store_name = req.getParameter("store_name").trim(); 
				String store_phone = req.getParameter("store_phone").trim();
				String store_owner = req.getParameter("store_owner").trim();
				//取得地址資料
				String zip = req.getParameter("zip").trim(); 
				String county = req.getParameter("county").trim(); 
				String city = req.getParameter("city").trim(); 
				String address = req.getParameter("address").trim(); 
				String store_ads = zip + "-" + county + "-" + city + "-" + address;
				
				String store_email = req.getParameter("store_email").trim(); 
				String store_intro = req.getParameter("store_intro"); 
				String store_type_no = req.getParameter("store_type_no");
				String store_status = req.getParameter("store_status"); 
				Double store_star = new Double (req.getParameter("store_star"));
				
				//取得店家開放時間 開放訂位時間  訂位人數
				String[] time = req.getParameterValues("time");
				StringBuffer sb_open = new StringBuffer(); 
				StringBuffer sb_booking = new StringBuffer();
				StringBuffer sb_bookAmt = new StringBuffer(); 
				sb_open.append("0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0");
				sb_booking.append("0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0");
				sb_bookAmt.append("00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00"); 
				try {
					for (int i = 0; i < time.length; i++) {
						int index = new Integer(time[i]);
						String bookOrNot = req.getParameter("bookOrNot" + index);
							if (bookOrNot == null) {
								errorMsgs.put("bookOrNot" + index,"請選擇是否開放訂位");
							}
							if (bookOrNot.equals("1")) {
								String bookAmt = req.getParameter("bookAmt"+index);
								try {
									Integer number = new Integer(bookAmt);
									if(number<10) {
										bookAmt = "0"+bookAmt; 
									}
								} catch(Exception e) {
									errorMsgs.put("bookOrNot"+index, "訂位人數請填數字"); 
									bookAmt = "00"; 
								}
								sb_bookAmt.replace(index*3, index*3+ 2, bookAmt);
							}
						int index1 = index * 2;
						sb_open.replace(index1, index1 + 1, "1");
						sb_booking.replace(index1, index1 + 1, bookOrNot);
					} 
				} catch (Exception e) {
					errorMsgs.put("store_open","請選擇店家營業時段"); 
				}
				
				String store_open = sb_open.toString(); 
				String store_booking = sb_booking.toString();
				String store_book_amt = sb_bookAmt.toString();
				
				String store_latlng = null; 
				if(store_name.length()==0) { 
					errorMsgs.put("store_name", "店家名稱不可為空白"); 
				}
				if(store_phone.length()==0) {
					errorMsgs.put("store_phone", "電話不可為空白"); 
				} 
				if(store_owner.length()==0) {
					errorMsgs.put("store_owner","未輸入店家負責人姓名"); 
				} else if(!store_owner.matches("[\u4e00-\u9fa5]{1,5}")) { 
					errorMsgs.put("store_owner","輸入格式錯誤，請輸入中文名稱");
				}
				if(county.length()==0 || city.length()==0 || address.length()==0) {
					errorMsgs.put("store_address", "請提供完整地址資訊"); 
				}		
				if(store_type_no=="") {
					errorMsgs.put("store_type_no", "請選擇店家格式"); 
				}		
				if(!errorMsgs.isEmpty()) {
					store_name = storeVO.getStore_name();
					store.put("store_id", store_id); 
					store.put("store_name", store_name); 
					session.setAttribute("store", store); 					
					req.setAttribute("storeVO", storeVO);
					// 重設地址
					req.setAttribute("zip", zip);
					req.setAttribute("county", county);
					req.setAttribute("city", county);
					req.setAttribute("address", address);
					req.setAttribute("errorMsgs", errorMsgs);
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/storeUpdate.jsp"); 
					failureView.forward(req, res);
					return; 
				}
				
				storeVO = new StoreVO(); 
				storeVO.setStore_id(store_id);
				storeVO.setStore_psw(store_psw);
				storeVO.setStore_name(store_name);
				storeVO.setStore_phone(store_phone); 
				storeVO.setStore_owner(store_owner);
				storeVO.setStore_intro(store_intro);
				storeVO.setStore_ads(store_ads); 
				storeVO.setStore_type_no(store_type_no); 
				storeVO.setStore_booking(store_booking); 
				storeVO.setStore_open(store_open);
				storeVO.setStore_book_amt(store_book_amt);
				storeVO.setStore_email(store_email);
				storeVO.setStore_star(store_star); 
				storeVO.setStore_status(store_status);
				storeVO.setStore_latlng(store_latlng);
				storeVO.setStore_no(store_no);
				
								
				/************************************修改完成  準備轉交畫面*************************************/
				storeVO = storeSvc.updateStore(store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, store_star, store_latlng);
				String url = "/front-end/store/personal.jsp"; 
				req.setAttribute("update_success", "update_success");
				RequestDispatcher successView = req.getRequestDispatcher(url); 
				successView.forward(req, res);
				
			} catch(Exception e) {
				errorMsgs.put("elseError", e.getMessage());
				String store_name = storeVO.getStore_name();
				String store_id = storeVO.getStore_id(); 
				store.put("store_id", store_id); 
				store.put("store_name", store_name); 
				session.setAttribute("store", store); 
				req.setAttribute("errorMsgs", errorMsgs);
				req.setAttribute("storeVO", storeVO); 
				RequestDispatcher failureView = req.getRequestDispatcher("/store/store.do?action=getOne_For_Update"); 
				failureView.forward(req, res);
				return; 
			}
		}
				
	/****************************店家註冊 基本同新增店家的功能************************************/	
		if("storeRegister".equals(action)) {
			Map<String,String> errorMsgs = new HashMap<String,String>(); 
			req.setAttribute("errorMsgs", errorMsgs); 
			
			StoreVO storeVO = new StoreVO(); 
			StoreService storeSvc = new StoreService(); 
			
			try{
				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				String store_id = req.getParameter("store_id").trim(); 
				String store_psw = req.getParameter("store_psw").trim(); 
				String store_psw_check = req.getParameter("store_psw_check").trim(); 
				String store_name = req.getParameter("store_name").trim(); 
				String store_phone = req.getParameter("store_phone").trim();
				String store_owner = req.getParameter("store_owner").trim();
				
				String zip = req.getParameter("zip").trim(); 
				String county = req.getParameter("county").trim(); 
				String city = req.getParameter("city").trim(); 
				String address = req.getParameter("address").trim();
				String store_ads = null; 
				if(zip.length() !=0 && address.length()!=0) {
				store_ads = zip + "-" + county + "-" + city + "-" + address;
				}
				String store_email = req.getParameter("store_email").trim(); 
				String store_intro = req.getParameter("store_intro"); 
				String store_type_no = req.getParameter("store_type_no").trim();
				String store_latlng = req.getParameter("store_latlng").trim();
				System.out.println("store_latlng=" + store_latlng);
								
				if(store_id.length()==0) { 
					errorMsgs.put("store_id", "未輸入帳號"); 
				} else if(store_id.length()<6) {
					errorMsgs.put("store_id", "帳號格式不正確，至少需要6碼"); 
				} 
				else {
				// 帳號驗證  是否有重複
				Map<String, String[]> map = new TreeMap<>(); 
				map.put("store_id", new String[] {store_id}); 
				List<StoreVO> list = storeSvc.getAll(map); //用複合查詢
				if(!(list.size()==0)) {
					errorMsgs.put("store_id", "帳號重複，請選擇其他帳號"); 
					}
				}
				if(store_psw.length()==0) {
					errorMsgs.put("store_psw", "未輸入密碼"); 
				} else if(store_psw.length()<6) {
					errorMsgs.put("store_psw", "密碼格式不正確，至少需要6碼"); 
				}
				if(store_psw_check.length()==0) {
					errorMsgs.put("store_psw_check", "請再次輸入密碼，以驗證密碼"); 
				} else if(!(store_psw_check.equals(store_psw))) {
					errorMsgs.put("store_psw_check", "密碼和確認密碼不一致"); 
				}
				if(store_name.length()==0) {
					errorMsgs.put("store_name", "店家名稱 不可為空白"); 
				}
				if(store_phone.length()==0) {
					errorMsgs.put("store_phone", "電話不可為空白"); 
				} else if(!(store_phone.matches("[0-9]{2}[0-9]{7,8}") || store_phone.matches("[0-9]{4}[0-9]{6}"))) {
					errorMsgs.put("store_phone", "電話格式有誤 可輸入家用電話或手機號碼"); 
				}
				if(store_owner.length()==0) {
					errorMsgs.put("store_owner","未輸入店家負責人姓名"); 
				} else if(!store_owner.matches("[\u4e00-\u9fa5]{1,5}")) { 
					errorMsgs.put("store_owner","輸入格式錯誤，請輸入中文名稱");
				}
				if(county.length()==0 || city.length()==0 || address.length()==0) {
					errorMsgs.put("store_address", "請提供完整地址資訊"); 
				}								
				if(store_email.length()==0) {
					errorMsgs.put("store_email", "未輸入email"); 
				} else if(!store_email.contains("@")) {
					errorMsgs.put("store_email", "email格式不正確"); 
				}				
				if(store_type_no=="") {
					errorMsgs.put("store_type_no", "請選擇店家格式"); 
				}
				
				//取得店家開放時間 開放訂位時間  訂位人數
				String[] time = req.getParameterValues("time");
				StringBuffer sb_open = new StringBuffer(); 
				StringBuffer sb_booking = new StringBuffer();
				StringBuffer sb_bookAmt = new StringBuffer(); 
				sb_open.append("0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0");
				sb_booking.append("0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0");
				sb_bookAmt.append("00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00-00"); 
				try {
					for (int i = 0; i < time.length; i++) {
						int index = new Integer(time[i]);
						String bookOrNot = req.getParameter("bookOrNot" + index);
							if (bookOrNot == null) {
								errorMsgs.put("bookOrNot" + index,"請選擇是否開放訂位");
							}
							if (bookOrNot.equals("1")) {
								String bookAmt = req.getParameter("bookAmt"+index);
								try {
									Integer number = new Integer(bookAmt);
									if(number<10) {
										bookAmt = "0"+bookAmt; 
									}
								} catch(Exception e) {
									errorMsgs.put("bookOrNot"+index, "訂位人數請填數字"); 
									bookAmt = "00"; 
								}
								sb_bookAmt.replace(index*3, index*3+ 2, bookAmt);
							}
						int index1 = index * 2;
						sb_open.replace(index1, index1 + 1, "1");
						sb_booking.replace(index1, index1 + 1, bookOrNot);
						
					} 
				} catch (Exception e) {
					errorMsgs.put("store_open","請選擇店家營業時段"); 
				}
				String store_open = sb_open.toString(); 
				String store_booking = sb_booking.toString();
				String store_book_amt = sb_bookAmt.toString();									
				
				storeVO = new StoreVO(); 
				storeVO.setStore_id(store_id);
				storeVO.setStore_psw(store_psw);
				storeVO.setStore_name(store_name);
				storeVO.setStore_phone(store_phone); 
				storeVO.setStore_owner(store_owner);
				storeVO.setStore_intro(store_intro);
				storeVO.setStore_ads(store_ads); 
				storeVO.setStore_type_no(store_type_no); 
				storeVO.setStore_booking(store_booking); 
				storeVO.setStore_open(store_open);
				storeVO.setStore_book_amt(store_book_amt);
				storeVO.setStore_email(store_email);
				storeVO.setStore_latlng(store_latlng);				
				
				// 重設訂位
				if(!errorMsgs.isEmpty()) {
					String[] open = store_open.split("-"); 
					req.setAttribute("time", open);
					String[] book = store_booking.split("-"); 
					for(int i=0; i<book.length; i++) {
						req.setAttribute("bookOrNot"+i, book[i]);
					}
					String[] book_amt = store_book_amt.split("-"); 
					for(int i=0; i<book_amt.length; i++) {
						req.setAttribute("bookAmt"+i, book_amt[i]);
					}
					// 重設地址
					if(store_ads != null){
					String split_address[] = store_ads.split("-"); 
					req.setAttribute("zip", split_address[0]);
					req.setAttribute("county", split_address[1]);
					req.setAttribute("city", split_address[2]);
					req.setAttribute("address", split_address[3]);
					req.setAttribute("store_latlng", store_latlng);
					}
					req.setAttribute("errorMsgs", errorMsgs);
					req.setAttribute("storeVO", storeVO);
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/storeRegister.jsp"); 
					failureView.forward(req, res);
					return; 
				}
				/*****************************2. 資料新增成功***********************************************/
				storeSvc = new StoreService(); 
				storeVO = storeSvc.addStore(store_id, store_psw, 
						store_name, store_phone, store_owner, store_intro, store_ads, 
						store_type_no, store_booking, store_open, store_book_amt, store_email, store_latlng);
				
				//取得新增後的資料  自動登入
				StoreVO storeVO1 = new StoreVO(); 
				storeVO1 = storeSvc.getOneStoreIdAndPsw(store_id); 
				HttpSession session = req.getSession(); 
				Map<String, String> store = new TreeMap<>();
				store_name = storeVO1.getStore_name();
				String store_no = storeVO1.getStore_no();
				store.put("store_id", store_id); 
				store.put("store_name", store_name); 
				store.put("store_no", store_no); 
				session.setAttribute("store", store); 
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				req.setAttribute("success", "success");				
				String url = "/front-end/index.jsp";  //重導回首頁
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
			} catch(Exception e) {
				errorMsgs.put("elseError",e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/store/storeRegister.jsp");
				failureView.forward(req, res);
			}
		}
  	
	
		/******************************複合查詢 來自selectPage.jsp的請求***************************/
		if("listStores_ByCompositeQuery".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try{
				/***************************1. 將輸入資料轉為map********************************/
				//採用Map<String,String[]> getParameterMap()的方法
				//注意: an immutable java.util.Map 
				HttpSession session = req.getSession();
				Map<String, String[]> map = (Map<String, String[]>)session.getAttribute("map");
				if (req.getParameter("whichPage") == null){
					HashMap<String, String[]> map1 = (HashMap<String, String[]>)req.getParameterMap();
					HashMap<String, String[]> map2 = new HashMap<String, String[]>();
					map2 = (HashMap<String, String[]>)map1.clone();
					session.setAttribute("map",map2);
					map = (HashMap<String, String[]>)req.getParameterMap();
				}
				/***************************2. 開始複合查詢 *************************************/
				StoreService storeSvc = new StoreService(); 
				List<StoreVO> list = storeSvc.getAll(map); //傳入物件為map 
				/***************************3. 查詢完成，準備轉交 *******************************/ 
				req.setAttribute("list", list); 
				RequestDispatcher successView = req.getRequestDispatcher("/front-end/restaurants/listStores_ByCompositeQuery.jsp"); //成功轉交
				successView.forward(req, res);
				
				/***************************4. 其他可能的錯誤處理 *******************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/index.jsp"); //若錯誤  重導回首頁
				failureView.forward(req, res);
			}
		}
		
		/***********************首頁 店家登入******************************************/
		if("storeLogin".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>(); 
			req.setAttribute("errorMsgs", errorMsgs);
			
			String store_id = req.getParameter("store_id").trim();
			String store_psw = req.getParameter("store_psw").trim();
			
			if(store_id.length()==0|| store_psw.length()==0) {
				errorMsgs.add("請輸入帳號和密碼");
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/login.jsp");
				failureView.forward(req, res);
				return;
			}
			StoreService storeSvc = new StoreService(); 
			StoreVO storeVO = new StoreVO(); 
			storeVO = storeSvc.getOneStoreIdAndPsw(store_id); 
			if(storeVO == null) {
				errorMsgs.add("無此帳號 請重新輸入");
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/login.jsp");
				failureView.forward(req, res);
				return;				
			}
			if(!(storeVO.getStore_psw().equals(store_psw))) {
				req.setAttribute("storeVO", storeVO);
				errorMsgs.add("密碼錯誤 請重新輸入");
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/login.jsp");
				failureView.forward(req, res);
				return;			
			}
						
			HttpSession session = req.getSession(); 
			Map<String, String> store = new TreeMap<>();
			String store_name = storeVO.getStore_name();
			String store_no = storeVO.getStore_no(); // 取得店家的主鍵  放進session 後面好查詢
			store.put("store_id", store_id); 
			store.put("store_name", store_name);
			store.put("store_no", store_no); 
			session.setAttribute("store", store); 
			System.out.println("store==========="+store);
			res.sendRedirect(req.getContextPath() + "/front-end/index.jsp"); 
		}
		
		/***************************** 店家登出 ***********************************/
		if("storeLogout".equals(action)) {
			HttpSession session = req.getSession(); 
			session.removeAttribute("store");
			String url = req.getContextPath()+"/front-end/index.jsp"; //登出後  重導回首頁
			res.sendRedirect(url);
		}
		
		/******************************* 忘記密碼 **********************************/ 
		if("forgetPsw".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>(); 
			req.setAttribute("errorMsgs", errorMsgs);
			
			String store_email = req.getParameter("store_email").trim();
			if(store_email.length()==0) {
				errorMsgs.add("請輸入Email"); 
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/forgetPsw.jsp");
				failureView.forward(req, res);
				return;
			}
			StoreService storeSvc = new StoreService(); 
			StoreVO storeVO = new StoreVO(); 
			storeVO = storeSvc.getOneStoreByEmail(store_email); 
						
			if(storeVO == null) {
				errorMsgs.add("信箱輸入錯誤 請重新輸入");
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/forgetPsw.jsp");
				failureView.forward(req, res);
				return;			
			}
			String store_name = storeVO.getStore_name();
			String store_id = storeVO.getStore_id(); 			
			
			// 將新密碼以email寄出
			try {	
			String codes = String.valueOf((int) (Math.random() * 1000000)).trim();
			storeVO.setStore_psw(codes);
			storeSvc.updateStore(storeVO);
			String subject = "iEat 密碼重設通知"; 
			String messageText = store_name + "您好\n" + "您的帳號為"  + store_id + "\n您的新密碼如下：" + codes + "\n請以此組新密碼進行登入";
			MailService mailService = new MailService(); 
			mailService.sendMail(store_email, subject, messageText);
			
			req.setAttribute("resetPsw", "resetPsw");
			RequestDispatcher successView = req.getRequestDispatcher("/front-end/login.jsp");
			successView.forward(req, res);
			
			} catch(Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/forgetPsw.jsp");
				failureView.forward(req, res);
				return;	
			}			
		}
		
		/*****************************修改密碼 ***************************/
		if("passwordUpdate".equals(action)) {
			Map<String, String> errorMsgs = new TreeMap<>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				HttpSession session = req.getSession();
				Map<String, String> store = (Map<String, String>) session.getAttribute("store");
				
				StoreService storeSvc = new StoreService();
				StoreVO storeVO = storeSvc.getOneStore(store.get("store_no")); 
				String store_psw = req.getParameter("store_psw");
				if (!store_psw.equals(storeVO.getStore_psw())) {
					errorMsgs.put("store_psw", "密碼不正確");
				}
				String pwdRegEx = "[\\w]{6,15}";
				String store_psw_new = req.getParameter("store_psw_new");
				String store_psw_new_check = req.getParameter("store_psw_new_check");
				if (store_psw_new.isEmpty()) {
					errorMsgs.put("store_psw_new", "請輸入新密碼");
				} else if (!store_psw_new.matches(pwdRegEx)) {
					errorMsgs.put("store_psw_new", "密碼格式錯誤");
				} else if (!store_psw_new.equals(store_psw_new_check)) {
					errorMsgs.put("store_psw_new_check", "輸入不一致");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/passwordupdate.jsp");
					failureView.forward(req, res);
					return;
				}
				storeVO.setStore_psw(store_psw_new);
				storeSvc.updateStore(storeVO);
				String store_name = storeVO.getStore_name();
				String store_email = storeVO.getStore_email(); 
				
				String subject = "iEat 密碼修改通知"; 
				String messageText = store_name + "您好\n" + "\n您的新密碼如下：" + store_psw_new + "\n請以此組新密碼進行登入" + "\n若不是您本人提出修改密碼，請盡速與我們平台聯絡";
				MailService mailService = new MailService(); 
				mailService.sendMail(store_email, subject, messageText);
				
				req.setAttribute("success", "success");
				RequestDispatcher successView = req.getRequestDispatcher("/front-end/store/passwordupdate.jsp");
				successView.forward(req, res);
			} catch (Exception e) {
				errorMsgs.put("elseError", "請修正以下錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/passwordupdate.jsp");
				failureView.forward(req, res);
			}
		}
		
		/*******************************註冊時  檢查帳號是否重複  用ajax傳送 ***************************/
		if("checkid".equals(action)) {
			String store_id = (String) req.getParameter("store_id"); 
			Map<String, String> errorMsgs = new TreeMap<>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			String check = "false";
			System.out.println("store_id="+store_id + "--------------");
			
			String acctpwdRegEx = "[\\w]{6,15}";// 給帳號和密碼用的正則表示，只能輸入英文或數字
			if (store_id.isEmpty()) {
				errorMsgs.put("store_id","未輸入帳號");
			} else if (store_id.matches(acctpwdRegEx)) { //若符合正規表示式 則進入此區塊
				Map<String, String[]> map = new TreeMap<>();
				map.put("store_id", new String[] { store_id });
				StoreService storeSvc = new StoreService();
				List<StoreVO> list = storeSvc.getAll(map);
				if (list.size() == 0) {
					check = "true";
				} else {
					errorMsgs.put("store_id", "帳號有重複");
				}
			} else {
				errorMsgs.put("store_id","輸入格式錯誤");
			}
			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.write(check);
			out.flush();
			out.close();
			return;
		}
		
		/********************************** 註冊時  驗證E-mail是否重複 *************************/
		if("checkemail".equals(action)) {
			String store_email = (String) req.getParameter("store_email"); 
			String errorMessage = null;
			String check = "false";
			
			String emailRegEx = "[\\w][\\w_.-]+@[\\w.]+";
			if (store_email.isEmpty()) {
				errorMessage = "未輸入email";
			} else if (!store_email.matches(emailRegEx)) {
				errorMessage = "email格式錯誤";
			} else {
				Map<String, String[]> map = new TreeMap<>();
				map.put("store_email", new String[] { store_email });
				StoreService storeSvc = new StoreService();
				List<StoreVO> list = storeSvc.getAll(map);
				if (list.size() == 0) {
					check = "true";

				} else {
					errorMessage = "email有重複";
				}
			}
			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.write(check);
			out.flush();
			out.close();
			return;		
		}
		
		/********************** 店家註冊  檢驗電話號碼是否有重複 ajax***************************/
		if ("checkphone".equals(action)) {
			String store_phone = (String) req.getParameter("store_phone");
			String errorMessage = null;
			String check = "false";
			
			String phoneRegEx = "09[0-9]{8}";
			String homePhoneRegEx = "0[2-8]{1}[0-9]{6,8}"; 
			if (store_phone.isEmpty()) {
				errorMessage = "未輸入電話號碼";
			} else if (!store_phone.matches(phoneRegEx) && !store_phone.matches(homePhoneRegEx)) {
				errorMessage = "電話號碼格式錯誤";
			} else {
				Map<String, String[]> map = new TreeMap<>();
				map.put("store_phone", new String[] { store_phone });
				StoreService storeSvc = new StoreService();
				List<StoreVO> list = storeSvc.getAll(map);
				if (list.size() == 0) {
					check = "true";
				} else {
					errorMessage = "電話有重複";
				}
			}
			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.write(check);
			out.flush();
			out.close();
			return;
		}
		
		/************************* 來自後端  取得店家全部資訊 **********************************/ 
		if("getOne_display".equals(action)){
			String store_no = req.getParameter("store_no");
			StoreService storeSvc = new StoreService();
			StoreVO findStore =storeSvc.getOneStore(store_no);
			Store_typeService store_typeSvc = new Store_typeService();
			Store_photoService store_photoSvc = new Store_photoService();
			JSONObject json = new JSONObject();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			json.put("store_no", findStore.getStore_no());			
			json.put("store_name", findStore.getStore_name());			
			json.put("store_phone", findStore.getStore_phone());			
			json.put("store_owner", findStore.getStore_owner());			
			json.put("store_intro", findStore.getStore_intro());
			json.put("store_type_no", store_typeSvc.getOneStore_type(findStore.getStore_type_no()).getStore_type_name());			
			json.put("store_booking", findStore.getStore_booking());			
			json.put("store_open", findStore.getStore_open());			
			json.put("store_book_amt", findStore.getStore_book_amt());			
			json.put("store_email", findStore.getStore_email());			
			json.put("store_status", findStore.getStore_status());			
			json.put("store_email", findStore.getStore_email());
			json.put("store_photo", store_photoSvc.countStorePhoto(store_no));

			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.write(json.toString());
			out.flush();
			out.close();
		}
		/****************** 名稱重複  暫時拿掉 **********************/
//		if("getOne_For_Display".equals(action)) { //來自select_page.jsp的請求
//			List<String> errorMsgs = new LinkedList<String>(); 
//			req.setAttribute("errorMsgs", errorMsgs);
//			
//			try {
//				/***********接收請求參數，輸入格式錯誤處理***************************************/
//				String str = req.getParameter("store_no"); 
//				if(str == null || (str.trim()).length() == 0) {
//					errorMsgs.add("請輸入店家編號"); 
//				}
//				String store_no = null; 
//				store_no = new String(str); 
//				if(!str.matches("[S]{1}[0-9]{9}")) {
//					errorMsgs.add("餐廳編號格式不正確，需為S開頭 後面有9位數字");
//				}
//				if (!errorMsgs.isEmpty()) {
//					RequestDispatcher failureView = req
//							.getRequestDispatcher("/front-end/store/select_page.jsp");
//					failureView.forward(req, res);
//					return;//程式中斷
//				}
//				/*************無錯誤 開始查詢資料************************/
//				StoreService storeSvc = new StoreService(); 
//				StoreVO storeVO = storeSvc.getOneStore(store_no); 
//				if(storeVO == null) { 
//					errorMsgs.add("查無資料"); 
//				}
//				if (!errorMsgs.isEmpty()) {
//					RequestDispatcher failureView = req
//							.getRequestDispatcher("/front-end/store/select_page.jsp");
//					failureView.forward(req, res);
//					return;//程式中斷
//				}
//				/****************查詢完成 準備轉交***************************/
//				req.setAttribute("storeVO", storeVO); 
//				String url = "/front-end/store/listOneStore.jsp"; 
//				RequestDispatcher successView = req.getRequestDispatcher(url); 
//				successView.forward(req, res); 
//				/****************其他可能錯誤處理**************************/
//			} catch(Exception e) {
//				errorMsgs.add("無法取得資料：" +  e.getMessage());
//				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/select_page.jsp"); 
//				failureView.forward(req, res);
//			}
//		}
		/****************** 來自後端  店家審核通過 ****************************/ 
		if ("pass".equals(action)) { // 來自update_emp_input.jsp的請求
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
		
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String store_no = req.getParameter("store_no").trim();
				String store_status ="1";
				
				/***************************2.開始修改資料*****************************************/
				StoreService storeSvc = new StoreService();
				StoreVO store=new StoreVO();
				java.sql.Date store_validate = new java.sql.Date(System.currentTimeMillis());
				StoreVO storeVO = storeSvc.updateStoreStatus(store_status,store_validate,store_no);
				
				System.out.println("信箱發送前");
				String to=storeVO.getStore_email();
				String subject="IEAT客服部";
				String messageText="您好~!貴公司"+storeVO.getStore_name()+"的餐廳資料已審核通過，歡迎使用IEAT平台";
				MailService mailService = new MailService();
				mailService.sendMail(to, subject, messageText);
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("storeVO", storeVO); // 資料庫update成功後,正確的的empVO物件,存入req
				String url = "/back-end/store/notreviewed.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneEmp.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back-end/store/notreviewed.jsp");
				failureView.forward(req, res);
			}
		}
		if ("nopass".equals(action)) { // 來自listAllEmp.jsp
			String store_no =req.getParameter("store_no");

			/***************************
			 * 2.開始刪除資料
			 ***************************************/
			StoreService storeSvc = new StoreService();
			StoreVO storeVO=storeSvc.getOneStore(store_no);
			
			String to=storeVO.getStore_email();
			String subject="IEAT客服部";
			String messageText="您好~!貴公司"+storeVO.getStore_name()+"的餐廳資料不完整，請將資料準備完再申請";
			MailService mailService = new MailService();
			
			mailService.sendMail(to, subject, messageText);
			storeSvc.deleteStoreVO(store_no);
			/***************************
			 * 3.刪除完成,準備轉交(Send the Success view)
			 ***********/
			String url = "/back-end/store/notreviewed.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
			successView.forward(req, res);
		}
		/*********************** 來自後端  店家停權 **********************/ 
		if("getOne_For_disable".equals(action)) {
			try{
				/***************************1.接收請求參數****************************************/
				String store_no = req.getParameter("store_no"); 
				String store_status = "0";
				/***************************2.開始查詢資料，修改這邊 不需要錯誤驗證****************************************/
				StoreService storeSvc = new StoreService(); 
				StoreVO store=storeSvc.getOneStore(store_no);
				long store_validate = store.getStore_validate().getTime()+ 30*24*60*60*1000L;
                java.sql.Date store_stop = new java.sql.Date(store_validate);
				StoreVO storeVO = storeSvc.updateStoreStatus(store_status,store_stop,store_no);
				
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("storeVO", storeVO);
				String url = "/back-end/store/reviewed.jsp"; 
				RequestDispatcher successView = req.getRequestDispatcher(url); //轉交給update_store_input 
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch(Exception e) {
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/store/reviewed.jsp"); 
				failureView.forward(req,res); 
			}
		}
		
		/*************************** 來自後端  回復店家停權狀態 **********************/ 
		if("getOne_For_return".equals(action)) {
			try{
				/***************************1.接收請求參數****************************************/
				String store_no = req.getParameter("store_no"); 
				String store_status = "1";
				
				/***************************2.開始查詢資料，修改這邊 不需要錯誤驗證****************************************/
				StoreService storeSvc = new StoreService(); 
				StoreVO store = storeSvc.getOneStore(store_no);
                java.sql.Date store_validate = new java.sql.Date(System.currentTimeMillis());
				StoreVO storeVO = storeSvc.updateStoreStatus(store_status,store_validate,store_no);
				
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("storeVO", storeVO);
				String url = "/back-end/store/reviewed.jsp"; 
				RequestDispatcher successView = req.getRequestDispatcher(url); //轉交給update_store_input 
				successView.forward(req, res);
				
				/***************************其他可能的錯誤處理**********************************/
			} catch(Exception e) {
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/store/reviewed.jsp"); 
				failureView.forward(req,res); 
			}
		}
		
		/******************************* 來自後端  開店 ************************************/ 
		if("getOne_For_enable".equals(action)) {
			try{
				/***************************1.接收請求參數****************************************/
				String store_no = req.getParameter("store_no"); 
				String store_status = "1";
				
				
				/***************************2.開始查詢資料，修改這邊 不需要錯誤驗證****************************************/
				StoreService storeSvc = new StoreService(); 
				
				StoreVO store=new StoreVO();
				java.sql.Date store_validate = new java.sql.Date(System.currentTimeMillis());
				StoreVO storeVO = storeSvc.updateStoreStatus(store_status,store_validate,store_no);
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("storeVO", storeVO);
				String url = "/back-end/store/reviewed.jsp"; 
				RequestDispatcher successView = req.getRequestDispatcher(url); //轉交給update_store_input 
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			} catch(Exception e) {
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/store/reviewed.jsp"); 
				failureView.forward(req,res); 
			}
		}
		/****************************** 來自後端  關店 ******************************/ 
		if("getOne_For_stop".equals(action)) {
			try{
				/***************************1.接收請求參數****************************************/
				String store_no = req.getParameter("store_no"); 
				String store_status = "3";
				/***************************2.開始查詢資料，修改這邊 不需要錯誤驗證****************************************/
				StoreService storeSvc = new StoreService(); 
				StoreVO store=new StoreVO();
				java.sql.Date store_validate = null;
				StoreVO storeVO = storeSvc.updateStoreStatus(store_status,store_validate,store_no);
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("storeVO", storeVO);
				String url = "/back-end/store/reviewed.jsp"; 
				RequestDispatcher successView = req.getRequestDispatcher(url); //轉交給update_store_input 
				successView.forward(req, res);
				/***************************其他可能的錯誤處理**********************************/
			} catch(Exception e) {
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/store/reviewed.jsp"); 
				failureView.forward(req,res); 
			}
		}
		
	}
}


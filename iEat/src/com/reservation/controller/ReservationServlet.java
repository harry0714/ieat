package com.reservation.controller;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

import com.email.model.MailService;
import com.ord.model.OrdVO;
import com.reservation.model.*;
import com.store.model.StoreService;
import com.store.model.StoreVO; 
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)
public class ReservationServlet extends HttpServlet{ 
	public void doGet(HttpServletRequest req, HttpServletResponse res) 
	throws ServletException, IOException {
		doPost(req, res); 		
	}
	public void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException{
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		
		if ("getOne_For_Display".equals(action)) { // 來自select_page.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String str = req.getParameter("reservation_no");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入訂位編號");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/reservation/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				String reservation_no = null;				
					reservation_no = new String(str);
					if(!str.matches("[R]{1}[0-9]{9}")) {
						errorMsgs.add("訂位編號格式不正確，請從R開頭 後面有9位數字");
					}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/reservation/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************2.開始查詢資料*****************************************/
				ReservationService reservationSvc = new ReservationService();
				ReservationVO reservationVO = reservationSvc.getOneReservation(reservation_no);
				if (reservationVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/reservation/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("reservationVO", reservationVO); // 資料庫取出的empVO物件,存入req
				String url = "/front-end/reservation/listOneReservation.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneEmp.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/reservation/select_page.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("getOne_For_Update".equals(action)) { // 來自listAllEmp.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數****************************************/
				String reservation_no = new String(req.getParameter("reservation_no"));
				
				/***************************2.開始查詢資料****************************************/
				ReservationService reservationSvc = new ReservationService();
				ReservationVO reservationVO = reservationSvc.getOneReservation(reservation_no);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("reservationVO", reservationVO);         // 資料庫取出的empVO物件,存入req
				String url = "/front-end/reservation/update_reservation_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_reservation_input.jsp
				successView.forward(req, res);

				/***************************其他可能的錯誤處理**********************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/reservation/listAllReservation.jsp");
				failureView.forward(req, res);
			}
		}
		
		
		/*************************確認修改********************************/
		if("update".equals(action)) { //來自update_reservation_input.jsp的請求
			List<String> errorMsgs = new LinkedList<String>(); 
			req.setAttribute("errorMsgs", errorMsgs);
			
			try{
				/***********************1.接收請求參數，輸入格式的錯誤處理******************/
				String reservation_no = new String(req.getParameter("reservation_no").trim()); 
				String mem_no = new String(req.getParameter("mem_no").trim()); 
				String store_no = new String(req.getParameter("store_no").trim()); 
				String reservation_qrcode_status = new String(req.getParameter("reservation_qrcode_status").trim()); 
				String reservation_report = new String(req.getParameter("reservation_report").trim()); 
				String reservation_report_status = new String(req.getParameter("reservation_report_status").trim()); 

				String str3 = new String(req.getParameter("reservation_qrcode").trim()); 
				java.sql.Timestamp reservation_time = null; 
				reservation_time = java.sql.Timestamp.valueOf(req.getParameter("reservation_time").trim()); 
				
				ReservationService reservationSvc = new ReservationService();
				ReservationVO reservationVO = reservationSvc.getOneReservation(reservation_no);
				
				java.sql.Date reservation_date = null; 
				try{
					reservation_date = java.sql.Date.valueOf(req.getParameter("reservation_date").trim()); 
				} catch (IllegalArgumentException e) {
					reservation_date=new java.sql.Date(System.currentTimeMillis()); //若日期格是錯誤 改成當天的日期
					errorMsgs.add("訂位日期請填日期，且不可留空");
				}
				
				String str = req.getParameter("reservation_guests"); 				
				Integer reservation_guests = null; 
				if(str==null||(str.trim()).length()==0) {
					reservation_guests = 1;
					errorMsgs.add("訂位人數不可為空值");
				}
				try {
					reservation_guests = new Integer(str); 	
					if(!(reservation_guests>0)) {
						reservation_guests = 1; 
						errorMsgs.add("訂位人數不可小於1");
					}
				} catch(Exception e) {
					reservation_guests = 1; 
					errorMsgs.add("訂位人數格式不正確，必須為數字！");
				}
				String reservation_hour = req.getParameter("reservation_hour");
				if(!reservation_hour.matches("[0-2]{1}[0-9]{1}")){
					reservation_hour = "00"; 
					errorMsgs.add("訂位時段格是不正確 請填入00~23的時間");
				}
				//修改圖片
				
				Part part = req.getPart("reservation_qrcode");
				
				InputStream in = part.getInputStream();
				byte[] reservation_qrcode = new byte[in.available()];
				if(reservation_qrcode.length==0) { //若沒有新增圖片，抓回資料庫的原始圖檔
					reservation_qrcode = reservationVO.getReservation_qrcode(); 
				}
				else{ //有新增  就用新增的圖
					in.read(reservation_qrcode);
					in.close();
				} 

				// 若有錯誤  跳轉回原畫面
				if(!errorMsgs.isEmpty()) {
					req.setAttribute("reservationVO", reservationVO); 
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/reservation/update_reservation_input.jsp"); 
					failureView.forward(req, res);
					return; //程式中斷
				}
				reservationVO = new ReservationVO(); 
				reservationVO.setReservation_no(reservation_no);
				reservationVO.setMem_no(mem_no);
				reservationVO.setStore_no(store_no);
				reservationVO.setReservation_date(reservation_date);
				reservationVO.setReservation_hour(reservation_hour); 
				reservationVO.setReservation_guests(reservation_guests); 
				reservationVO.setReservation_time(reservation_time); 
				reservationVO.setReservation_qrcode(reservation_qrcode);
				reservationVO.setReservation_qrcode_status(reservation_qrcode_status);
				reservationVO.setReservation_report(reservation_report); 
				reservationVO.setReservation_report_status(reservation_report_status);
				
				/***********************2.開始修改資料*************************/
				reservationVO = reservationSvc.updateReservation(reservation_no, mem_no, store_no, reservation_date, reservation_hour, reservation_guests, reservation_time, reservation_qrcode, reservation_qrcode_status, reservation_report, reservation_report_status); 
				
				req.setAttribute("reservationVO", reservationVO); 
				String url = "/front-end/reservation/listOneReservation.jsp"; 
				// System.out.println("資料修改成功");
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改完成後 轉交給listOneReservation.jsp
				successView.forward(req, res);
			} catch(Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/reservation/update_reservation_input.jsp");
				failureView.forward(req, res);
			}
		}
		
//		/***************************刪除指令  刪除的指令用不到********************************/
//		if ("delete".equals(action)) { // 來自listAllEmp.jsp
//
//			List<String> errorMsgs = new LinkedList<String>();
//			// Store this set in the request scope, in case we need to
//			// send the ErrorPage view.
//			req.setAttribute("errorMsgs", errorMsgs);
//	
//			try {
//				/***************************1.接收請求參數***************************************/
//				String reservation_no = new String(req.getParameter("reservation_no"));
//				
//				/***************************2.開始刪除資料***************************************/
//				ReservationService reservationSvc = new ReservationService();
//				reservationSvc.deleteReservation(reservation_no);
//				
//				/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
//				String url = "/front-end/reservation/listAllReservation.jsp";
//				RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
//				successView.forward(req, res);
//				
//				/***************************其他可能的錯誤處理**********************************/
//			} catch (Exception e) {
//				errorMsgs.add("刪除資料失敗:"+e.getMessage());
//				RequestDispatcher failureView = req
//						.getRequestDispatcher("/front-end/reservation/listAllReservation.jsp");
//				failureView.forward(req, res);
//			}
//		}
		
		
		/********************** 來自會員  新增訂位資訊 或者修改訂位資訊******************/
		if("add_Reservation".equals(action)) {
			Map<String, String> errorMsgs = new TreeMap<String, String>(); 
			req.setAttribute("errorMsgs", errorMsgs);						
			Integer reservation_guests = null; 
			java.sql.Date reservation_date = null;
			
			StoreService storeSvc = new StoreService(); 
			String store_no = req.getParameter("store_no");
			StoreVO storeVO = storeSvc.getOneStore(store_no);
			String[] store_booking = storeVO.getStore_booking().split("-");
			String reservation_hour = null; 
			
			try{
				reservation_hour = req.getParameter("reservation_hour").trim(); 
				String str = req.getParameter("reservation_guests").trim(); 
				if(reservation_hour.length()==0) {
					errorMsgs.put("reservation_hour", "請輸入訂位時段");
				}
				if(str.length()==0) {
					errorMsgs.put("reservation_guests", "請輸入訂位人數");	
					reservation_guests = 1; 
				}				
				
				try{
					reservation_date = java.sql.Date.valueOf(req.getParameter("reservation_date").trim());					
					// java.sql.Date today = new java.sql.Date(System.currentTimeMillis()); 
					Long sysdate = System.currentTimeMillis();
					Integer hour = new Integer(reservation_hour); 
					Long reservationLong = reservation_date.getTime() + hour*60*60*1000L - 30*60*1000;  //取得當下小時 的半個小時前
					
					if(sysdate > reservationLong) {
						reservation_date = new java.sql.Date(System.currentTimeMillis() + 24*60*60*1000);
						errorMsgs.put("reservation_date", "請勿超過訂位時間，且請於30分鐘前訂位");
					}
				} catch(Exception e) {
					reservation_date = new java.sql.Date(System.currentTimeMillis() + 24*60*60*1000);  // 若日期錯誤  設成明天的日期
					errorMsgs.put("reservation_date", "請輸入日期"); 
				}
				
				Integer remaining = null; 
				try{ 
					remaining = new Integer(req.getParameter("remaining"));
					reservation_guests = new Integer(str); 	
					if(remaining < reservation_guests){
						errorMsgs.put("reservation_guests", "訂位人數超出上限");
						reservation_guests = 1;
					}
				} catch(Exception e) {
					errorMsgs.put("reservation_guests", "訂位人數  請輸入數字");
					reservation_guests = 1; 
				}
				
				ReservationVO reservationVO = new ReservationVO(); 
				reservationVO.setReservation_date(reservation_date); 
				reservationVO.setReservation_hour(reservation_hour);
				reservationVO.setReservation_guests(reservation_guests);
				
				if(!errorMsgs.isEmpty()) {
					req.setAttribute("store_booking", store_booking);
					req.setAttribute("storeVO", storeVO);
					req.setAttribute("reservationVO", reservationVO);
					req.setAttribute("remaining", remaining);
					RequestDispatcher failureView = req.getRequestDispatcher("/store/store.do?action=get_reservation_info&store_no="+store_no+"&servletPath=/front-end/restaurants/Restaurants.jsp");
					failureView.forward(req, res);
					return; 
				}
				
				HttpSession session = req.getSession(); 
				session.setAttribute("storeVO", storeVO);
				session.setAttribute("reservationVO", reservationVO);
				RequestDispatcher successView = req.getRequestDispatcher("/front-end/reservation/confirmReservation.jsp"); 
				successView.forward(req, res);			
				
				
			} catch(Exception e) {
				/***************** 其他可能的錯誤處理 *****************************/
				errorMsgs.put("elseError", e.getMessage()); 
				RequestDispatcher failureView = req.getRequestDispatcher("/store/store.do?action=get_reservation_info&store_no="+store_no+"&servletPath=/front-end/restaurants/Restaurants.jsp");
				failureView.forward(req, res);
				return; 
			}						
		}

		/**********************確認訂位資訊  來自 confirm_reservation.jsp**************/ 
		if("confirm_reservation".equals(action)) {
			// 需要錯誤處理  以驗證最後一次 人數是否有衝突到
			Map<String, String> errorMsgs = new TreeMap<String, String>(); 
			req.setAttribute("errorMsgs", errorMsgs);	
			
			// 從Session取出訂位資料  店家資料
			HttpSession session = req.getSession(); 
			ReservationService reservationSvc = new ReservationService(); 
			
			
			try {
			StoreVO storeVO = (StoreVO) session.getAttribute("storeVO");
			String store_no = storeVO.getStore_no(); //取得店家編號
			ReservationVO reservationVO = (ReservationVO) session.getAttribute("reservationVO"); //取出訂位的資料
			
			Map<String, String> user = (Map<String, String>) session.getAttribute("user");
			String mem_no = user.get("no"); //從Session取得會員編號
			
			java.sql.Date reservation_date = (Date) reservationVO.getReservation_date(); // 取得要訂位日期
			String reservation_hour = reservationVO.getReservation_hour().toString();  //取得要訂位時段
			Integer reservation_guests = reservationVO.getReservation_guests(); //取得要訂位人數
			Timestamp reservation_time = new Timestamp(System.currentTimeMillis()); //取得系統日期
			
			// 以下  再度驗證是否有超出訂位人數
			StoreService storeSvc = new StoreService(); 
			StoreVO storeVO1 = storeSvc.getOneStore(store_no); 
			String[] store_booking = storeVO.getStore_booking().split("-"); //取出訂位時段  切割成字串陣列(為了錯誤時回去)
			String store_book_amt = storeVO1.getStore_book_amt(); //取出訂位人數
			String[] amts = store_book_amt.split("-"); //將資料庫的資料 切成字串陣列
			Integer index = new Integer(reservation_hour).intValue(); //將訂位時段轉成數字 已取得index
			Integer book_amt = new Integer(amts[index]).intValue(); //根據訂位的時段 取出該時段開放的訂位人數
			ArrayList<Integer> guests = reservationSvc.getGuests(reservation_hour, reservation_date, store_no); //取得所有訂位的人數
			Integer remain_amt = book_amt; //剩餘人數 = 總訂位人數
			
			for (Integer element : guests) { //跑迴圈  減掉已經訂位的訂單的人數  取得真正的剩餘人數
				remain_amt = remain_amt - element; 
			}
			if(remain_amt<=0) {
				remain_amt = 0; 
				errorMsgs.put("NoRemainings", "NoRemainings"); 
			}
			
			reservationVO = new ReservationVO(); 
			reservationVO.setMem_no(mem_no); 
			reservationVO.setStore_no(store_no);
			reservationVO.setReservation_date(reservation_date);
			reservationVO.setReservation_hour(reservation_hour);
			reservationVO.setReservation_guests(reservation_guests);
			reservationVO.setReservation_time(reservation_time);
			
			if(!errorMsgs.isEmpty()) {
				req.setAttribute("store_booking", store_booking);
				req.setAttribute("storeVO", storeVO);
				req.setAttribute("reservationVO", reservationVO);
				req.setAttribute("remaining", remain_amt);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/reservation/Reservations.jsp");
				failureView.forward(req, res);
				return; 
			}			
			
			// 開始新增資料  會自動產生訂位的QRCode
			reservationSvc.addReservation(mem_no, store_no, reservation_date, reservation_hour, reservation_guests, reservation_time);
			
			// 寄信給店家  通知有訂位的訂單			
			String store_name = storeVO.getStore_name(); 
			String store_email = storeVO.getStore_email();
			String mem_name = user.get("name");
			
//			String subject = "iEat訂位通知"; 
//			String messageText = store_name + "您好\n" + "\n您有一筆來自" + mem_name + "的訂位訂單" + "\n請前往店家專區確認訂單"+"\n再次謝謝您使用iEat官方網站";
//			MailService mailService = new MailService(); 
//			mailService.sendMail(store_email, subject, messageText);			
			
			session.removeAttribute("storeVO");
			session.removeAttribute("reservationVO");

			RequestDispatcher successView = req.getRequestDispatcher("/front-end/reservation/ReservationOK.jsp");
			successView.forward(req, res);
			
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		/************************** 在新增訂位記錄前  先查詢出剩餘的人數 用ajax送資料*****************************/
		if("getRemaining".equals(action)) {
			String reservation_hour =  req.getParameter("reservation_hour"); //取得訂位的時段
			java.sql.Date reservation_date = java.sql.Date.valueOf(req.getParameter("reservation_date"));  //取得要訂位的日期
			String store_no = req.getParameter("store_no"); //取得訂位的店家編號
			
			StoreService storeSvc = new StoreService(); 
			StoreVO storeVO = storeSvc.getOneStore(store_no); // 從店家主鍵   取得店家的資料  訂位總人數 
			String store_book_amt = storeVO.getStore_book_amt(); 
			String[] amts = store_book_amt.split("-"); //將資料庫的資料 切成字串陣列
			Integer index = new Integer(reservation_hour).intValue(); //將訂位時段轉成數字 已取得index
			Integer book_amt = new Integer(amts[index]).intValue(); //根據訂位的時段 取出該時段開放的訂位人數
			ReservationService reservationSvc = new ReservationService(); 
			ArrayList<Integer> guests = reservationSvc.getGuests(reservation_hour, reservation_date, store_no); //取得所有訂位的人數
			Integer remain_amt = book_amt; 
			
			for (Integer element : guests) {
				remain_amt = remain_amt - element; 
			}
			String remainings = remain_amt.toString(); 
			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.write(remainings);
			out.flush();
			out.close();
			return;
		}
		/**********************來自會員專區  會員查詢訂位紀錄*******************/
		if("memberreservation".equals(action)){
			Map<String,String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				
				HttpSession session = req.getSession();
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				ReservationService reservationSvc = new ReservationService();
				List<ReservationVO> reservationList = reservationSvc.getReservationsByMember(user.get("no"));
				req.setAttribute("reservationList", reservationList);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/reservation/memberreservation.jsp");
				failureView.forward(req, res);
				
			}catch(Exception e){
				errorMessage.put("elseError", "有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/personal.jsp"); //若有錯誤  回到原來的頁面 (會員專區)
				failureView.forward(req, res);
			}
		}
		/************************* 來自店家專區  店家查詢訂位紀錄**********************/
		if("storereservation".equals(action)) {
			Map<String,String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				
				HttpSession session = req.getSession();
				Map<String,String> store = (Map<String,String>)session.getAttribute("store");
				ReservationService reservationSvc = new ReservationService();
				List<ReservationVO> reservationList = reservationSvc.getReservationsByStore(store.get("store_no"));
				req.setAttribute("reservationList", reservationList);
				RequestDispatcher successView = req.getRequestDispatcher("/front-end/reservation/storereservation.jsp");
				successView.forward(req, res);
				
			}catch(Exception e){
				errorMessage.put("elseError", "有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/reservation/storereservation.jsp");
				failureView.forward(req, res);
			}
		}
		
		/*********************** 來自會員專區  會員檢視訂位資訊 取消訂位**************************/
		if("cancel".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String requestURL = req.getParameter("requestURL");
			ReservationService reservationSvc = new ReservationService(); 
			HttpSession session = req.getSession();
			Map<String, String> user = (Map<String, String>) session.getAttribute("user");
			
			try{
				
				String reservation_no = req.getParameter("reservation_no"); 
				ReservationVO reservationVO = reservationSvc.getOneReservation(reservation_no);
				java.sql.Date reservation_date = reservationVO.getReservation_date(); 
				Integer reservation_hour = new Integer(reservationVO.getReservation_hour()).intValue();
				String reservation_qrcode_status = reservationVO.getReservation_qrcode_status();
				Long Sysdate = System.currentTimeMillis();  //取得當前系統時間的毫秒數
				Long reservationLong = reservation_date.getTime() + reservation_hour*60*60*1000L - 30*60*1000;  //取得當下小時 的半個小時前
				// 若超過系統時間 or 在30分鐘前 則無法取消 
				if(Sysdate > reservationLong) {
					errorMsgs.add("訂位時間已過，無法取消"); 
				}
				if(!reservation_qrcode_status.equals("1")) {
					errorMsgs.add("訂單已確認  無法取消");
				}
				
				if (!errorMsgs.isEmpty()) {
					List<ReservationVO> reservationList = reservationSvc.getReservationsByMember(user.get("no")); 
					req.setAttribute("reservationList", reservationList);
					req.setAttribute("errorMsgs", errorMsgs);
					RequestDispatcher failureView = req
							.getRequestDispatcher(requestURL);
					failureView.forward(req, res);
					return;//程式中斷
				}
				//成功後  轉交回原頁面
				reservationVO.setReservation_qrcode_status("3");
				reservationSvc.updateReservation(reservationVO);
				List<ReservationVO> reservationList = reservationSvc.getReservationsByMember(user.get("no")); 
				req.setAttribute("reservationList", reservationList);
				RequestDispatcher successView = req.getRequestDispatcher(requestURL);
				successView.forward(req, res);
				
			} catch(Exception e) {
				List<ReservationVO> reservationList = reservationSvc.getReservationsByMember(user.get("no")); 
				req.setAttribute("reservationList", reservationList);
				errorMsgs.add(e.getMessage()); 
				RequestDispatcher failureView = req
						.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}
		}
	}
}

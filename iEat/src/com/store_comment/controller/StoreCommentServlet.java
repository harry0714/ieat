package com.store_comment.controller;

import java.io.*;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.*;

import com.discount.model.DiscountVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;
import com.store_comment.model.*;
import com.store_photo.model.Store_photoVO;


public class StoreCommentServlet extends HttpServlet {
	

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		String store_no = req.getParameter("store_no");
		String comment_time = req.getParameter("comment_time");

		//更改這整個insert
		if("insert".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				HttpSession session = req.getSession();
				String comment_conect = req.getParameter("comment_conect");
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				if(user==null){
					StoreService storeSvc = new StoreService(); 
					StoreVO storeVO = storeSvc.getOneStoreAndStoreStar(store_no);
					List<StoreCommentVO> scList = storeSvc.getByStoreNo(store_no);
					Set<Store_photoVO> set = storeSvc.getStore_photosByStore_no(store_no); //根據店家主鍵 查詢出店家照片
					String[] store_open = storeVO.getStore_open().split("-");  //將字串先在Servlet這邊切好 以免forTokens出問題
					List<DiscountVO> disList = storeSvc.getDiscountByStoreNo(store_no);
					req.setAttribute("disList", disList);
					req.setAttribute("storeVO", storeVO);
					req.setAttribute("store_open", store_open);
					req.setAttribute("listStore_photoByStore_no", set);
					req.setAttribute("scList", scList);
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/display_oneStore.jsp");
					failureView.forward(req, res);
					return;
				}
				String mem_no = user.get("no");
					
				if (comment_conect.isEmpty()){
					errorMsgs.add("請填入內容!!");
				}
								
				Double comment_level = new Double(req.getParameter("comment_level").trim());
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				
				StoreCommentVO storecommentVO= new StoreCommentVO();
				storecommentVO.setStore_no(store_no);
				storecommentVO.setMem_no(mem_no);
				storecommentVO.setComment_conect(comment_conect);
				storecommentVO.setComment_time(ts);
				storecommentVO.setComment_level(comment_level);
				
				if(!errorMsgs.isEmpty()) {
					req.setAttribute("storecommentVO", storecommentVO);
					StoreService storeSvc = new StoreService(); 
					StoreVO storeVO = storeSvc.getOneStoreAndStoreStar(store_no);
					List<StoreCommentVO> scList = storeSvc.getByStoreNo(store_no);
					Set<Store_photoVO> set = storeSvc.getStore_photosByStore_no(store_no); //根據店家主鍵 查詢出店家照片
					String[] store_open = storeVO.getStore_open().split("-");  //將字串先在Servlet這邊切好 以免forTokens出問題
					List<DiscountVO> disList = storeSvc.getDiscountByStoreNo(store_no);
					req.setAttribute("disList", disList);
					req.setAttribute("storeVO", storeVO);
					req.setAttribute("store_open", store_open);
					req.setAttribute("listStore_photoByStore_no", set);
					req.setAttribute("scList", scList);
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/store/display_oneStore.jsp");
					failureView.forward(req, res);
					return;
				}
				
				
				/***************************2.開始新增資料***************************************/
				StoreCommentService storecommentSvc = new StoreCommentService();
				storecommentVO = storecommentSvc.addStoreComment(store_no, mem_no, comment_conect, ts, comment_level );
				
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				StoreService storeSvc = new StoreService(); 
				StoreVO storeVO = storeSvc.getOneStoreAndStoreStar(store_no);
				List<StoreCommentVO> scList = storeSvc.getByStoreNo(store_no);
				Set<Store_photoVO> set = storeSvc.getStore_photosByStore_no(store_no); //根據店家主鍵 查詢出店家照片
				String[] store_open = storeVO.getStore_open().split("-");  //將字串先在Servlet這邊切好 以免forTokens出問題
				List<DiscountVO> disList = storeSvc.getDiscountByStoreNo(store_no);
				req.setAttribute("disList", disList);
				req.setAttribute("storeVO", storeVO);
				req.setAttribute("store_open", store_open);
				req.setAttribute("listStore_photoByStore_no", set);
				req.setAttribute("scList", scList);
				
				/****************查詢完成 準備轉交***************************/
				req.setAttribute("storeVO", storeVO); 
				String servletPath = req.getServletPath()+"?"+req.getQueryString();
				
				String url = "/front-end/store/display_oneStore.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
				successView.forward(req, res);	
				/***************************其他可能的錯誤處理**********************************/
			}catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/store/display_oneStore.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("delete".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數***************************************/
				String comment_no = req.getParameter("comment_no");
				System.out.println("comment_no="+comment_no);
				/***************************2.開始刪除資料***************************************/
				StoreCommentService storecommentSvc = new StoreCommentService();
				storecommentSvc.deleteStoreComment(comment_no);
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/
				String url = "/store_comment/listAllStore_Comment.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch(Exception e) {
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/store_comment/listAllStore_Comment.jsp");
				failureView.forward(req, res);
			}
		}
		
		if("getOne_For_Update".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String comment_no = req.getParameter("comment_no").trim();
				
				/***************************2.開始查詢資料****************************************/
				StoreCommentService storecommentSvc = new StoreCommentService();
				StoreCommentVO storecommentVO = storecommentSvc.getOneStoreComment(comment_no);
				
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("storecommentVO", storecommentVO);
				String url = "/store_comment/update_storecomment_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_emp_input.jsp
				successView.forward(req, res);
				
			} catch(Exception e) {
				errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/store_comment/listAllStore_Comment.jsp");
				failureView.forward(req, res);
			}
		}
		
		if("update".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String comment_conect = null;
				comment_conect =req.getParameter("comment_conect");
				
				if (comment_conect.isEmpty()){
					comment_conect = "真的很好吃!";
					errorMsgs.add("評論未填!!");
				}
				
				Double comment_level = new Double(req.getParameter("comment_level").trim());
				Timestamp ts = Timestamp.valueOf(comment_time);
				
				StoreCommentVO storecommentVO= new StoreCommentVO();
				storecommentVO.setStore_no(store_no);
				//storecommentVO.setMem_no(mem_no);
				storecommentVO.setComment_conect(comment_conect);
				storecommentVO.setComment_time(ts);
				storecommentVO.setComment_level(comment_level);
				
				if(!errorMsgs.isEmpty()) {
					req.setAttribute("storecommentVO", storecommentVO);
					RequestDispatcher failureView = req.getRequestDispatcher("/store_comment/update_storecomment_input.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************2.開始修改資料*****************************************/
				StoreCommentService storecommentSvc = new StoreCommentService();
				String comment_no = req.getParameter("comment_no");
				System.out.println("comment_no="+comment_no);
				//storecommentVO = storecommentSvc.updateStoreComment(comment_no, store_no, mem_no, comment_conect, ts, comment_level);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("storecommentVO", storecommentVO);
				String url = "/store_comment/listOneStore_Comment.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/store_comment/update_storecomment_input.jsp");
				failureView.forward(req, res);
			}
		}
		
	}

}

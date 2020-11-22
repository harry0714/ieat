package com.bookmark_store.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookmark_store.model.Bookmark_StoreService;
import com.bookmark_store.model.Bookmark_StoreVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;

public class Bookmark_StoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		String action = (String) req.getParameter("action");

		if ("enterbookmarkstore".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try {
				HttpSession session = req.getSession();
				List<String> bookmark_StoreNo = (List<String>) session.getAttribute("bookmark_StoreNo");
				List<StoreVO> storeList = new ArrayList<>();
				StoreService storeSvc = new StoreService();
				for (String store_no : bookmark_StoreNo) {
					storeList.add(storeSvc.getOneStoreAndStoreStar(store_no));
				}
				req.setAttribute("storeList", storeList);
				RequestDispatcher successView = req.getRequestDispatcher("/front-end/bookmarkstore/bookmarkstore.jsp");
				successView.forward(req, res);

			} catch (Exception e) {
				errorMessage.put("elseError", "有錯誤:" + e.getMessage()); //若有錯誤  回到會員頁面
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/personal.jsp");
				failureView.forward(req, res);
			}
		}

		if ("delete".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			String requestURL = req.getParameter("requestURL");
			try {
				HttpSession session = req.getSession();
				Map<String, String> user = (Map<String, String>) session.getAttribute("user");
				String store_no = req.getParameter("store_no");
				Bookmark_StoreService bmsSvc = new Bookmark_StoreService();
				bmsSvc.deleteBookmark_Store(user.get("no"), store_no);
				session.setAttribute("bookmark_StoreNo", bmsSvc.getStoreNoByMemNo(user.get("no")));

				if (requestURL.equals("/front-end/bookmarkstore/bookmarkstore.jsp")) {
					List<String> bookmark_StoreNo = (List<String>) session.getAttribute("bookmark_StoreNo");
					List<StoreVO> storeList = new ArrayList<>();
					StoreService storeSvc = new StoreService();
					for (String store_no1 : bookmark_StoreNo) {
						storeList.add(storeSvc.getOneStoreAndStoreStar(store_no1));
					}
					req.setAttribute("storeList", storeList);
				}
				String url = requestURL;
				RequestDispatcher successView = req.getRequestDispatcher(url); // 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);

			} catch (Exception e) {
				errorMessage.put("elseError", "有錯誤" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}
		}

		if ("add".equals(action)) {
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			String requestURL = req.getParameter("requestURL");
			try {
				HttpSession session = req.getSession();
				Map<String, String> user = (Map<String, String>) session.getAttribute("user");
				String store_no = req.getParameter("store_no");
				Bookmark_StoreService bmsSvc = new Bookmark_StoreService();
				bmsSvc.addBookmark_Store(user.get("no"), store_no);
				session.setAttribute("bookmark_StoreNo", bmsSvc.getStoreNoByMemNo(user.get("no")));

				if (requestURL.equals("/front-end/bookmarkstore/bookmarkstore.jsp")) {
					List<String> bookmark_StoreNo = (List<String>) session.getAttribute("bookmark_StoreNo");
					List<StoreVO> storeList = new ArrayList<>();
					StoreService storeSvc = new StoreService();
					for (String store_no1 : bookmark_StoreNo) {
						storeList.add(storeSvc.getOneStoreAndStoreStar(store_no1));
					}
					req.setAttribute("storeList", storeList);
				}
				String url = requestURL;
				RequestDispatcher successView = req.getRequestDispatcher(url); // 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);

			} catch (Exception e) {
				errorMessage.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
			}

		}

		if ("addAjax".equals(action)) {

			try {
				String store_no = (String) req.getParameter("store_no");
				HttpSession session = req.getSession();
				Map<String, String> user = (Map<String, String>) session.getAttribute("user");
				Bookmark_StoreService bmsSvc = new Bookmark_StoreService();
				bmsSvc.addBookmark_Store(user.get("no"), store_no);
				session.setAttribute("bookmark_StoreNo", bmsSvc.getStoreNoByMemNo(user.get("no")));
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
				String store_no = (String) req.getParameter("store_no");
				HttpSession session = req.getSession();
				Map<String, String> user = (Map<String, String>) session.getAttribute("user");
				Bookmark_StoreService bmsSvc = new Bookmark_StoreService();
				bmsSvc.deleteBookmark_Store(user.get("no"), store_no);
				session.setAttribute("bookmark_StoreNo", bmsSvc.getStoreNoByMemNo(user.get("no")));
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

package com.article.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.article.model.ArticleService;
import com.article.model.ArticleVO;
import com.article_response.model.Article_ResponseVO;
import com.member.model.MemberService;

public class ArticleServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req,res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		
		//新增文章
		if("submit".equals(action)){
			Map<String,String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String art_name = (String)req.getParameter("art_name");
				java.sql.Timestamp art_date = java.sql.Timestamp.valueOf(req.getParameter("art_date").replace("T", " ")+":00");
				String art_context = (String)req.getParameter("art_context");
				HttpSession session = req.getSession();
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				String mem_no = user.get("no");
				ArticleVO artVO = new ArticleVO();
				if(art_name.isEmpty()){
					errorMessage.put("art_name", "此欄位為必填");
				}
				if(art_context.isEmpty()){
					errorMessage.put("art_context","請填寫內容");
				}
				artVO.setArt_name(art_name);
				artVO.setArt_context(art_context);
				artVO.setArt_date(art_date);
				if(!errorMessage.isEmpty()){
					req.setAttribute("artVO", artVO);
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/writearticle.jsp");
					failureView.forward(req, res);
					return;
				}
				
				ArticleService artSvc = new ArticleService();
				byte[] art_image=null;
				artSvc.addarticle(art_name, art_date, art_context, mem_no);
				MemberService memberSvc = new MemberService();
				List<ArticleVO> artList = memberSvc.getArtByMemno(mem_no);
				req.setAttribute("artList", artList);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/memberarticle.jsp");
				failureView.forward(req, res);
				
			}catch(Exception e){
				errorMessage.put("elseError",e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/writearticle.jsp");
				failureView.forward(req, res);
			}
		}
		
		//會員文章列表頁面
		if("memberarticle".equals(action)){
			Map<String,String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				HttpSession session = req.getSession();
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				String mem_no = user.get("no");
				MemberService memberSvc = new MemberService();
				List<ArticleVO> artList = memberSvc.getArtByMemno(mem_no);
				req.setAttribute("artList",artList);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/memberarticle.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				errorMessage.put("elseError", e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/memberarticle.jsp");
				failureView.forward(req, res);
			}
		}
		
		//進入編輯頁面
		if("entereditarticle".equals(action)){
			Map<String,String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String art_no = (String)req.getParameter("art_no");
				ArticleService artSvc = new ArticleService();
				ArticleVO artVO = artSvc.getOneArticle(art_no);
				req.setAttribute("artVO", artVO);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/editarticle.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				errorMessage.put("elseError", e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/editarticle.jsp");
				failureView.forward(req, res);
			}
		}
		
		//更新文章
		if("updatearticle".equals(action)){
			Map<String,String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String art_name = (String)req.getParameter("art_name");
				java.sql.Timestamp art_date = java.sql.Timestamp.valueOf(req.getParameter("art_date").replace("T", " ")+":00");
				String art_context = (String)req.getParameter("art_context");
				String art_no =(String)req.getParameter("art_no");
				HttpSession session = req.getSession();
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				String mem_no = user.get("no");
				if(art_name.isEmpty()){
					errorMessage.put("art_name", "此欄位為必填");
				}
				if(art_context.isEmpty()){
					errorMessage.put("art_context","請填寫內容");
				}
				if(!errorMessage.isEmpty()){
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/editarticle.jsp");
					failureView.forward(req, res);
					return;
				}
				
				ArticleService artSvc = new ArticleService();
				byte[] art_image=null;
				artSvc.updatarticle(art_no, art_name, art_date, art_context, mem_no);
				MemberService memberSvc = new MemberService();
				List<ArticleVO> artList = memberSvc.getArtByMemno(mem_no);
				req.setAttribute("artList", artList);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/memberarticle.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				errorMessage.put("elseError", e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/editarticle.jsp");
				failureView.forward(req, res);
			}
		}
		
		//刪除文章
		if("deletearticle".equals(action)){
			Map<String,String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String art_no = (String)req.getParameter("art_no");
				
				ArticleService artSvc = new ArticleService();
				artSvc.deleteArticle(art_no);
				
				HttpSession session = req.getSession();
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				String mem_no = user.get("no");
				System.out.println(mem_no);
				MemberService memberSvc = new MemberService();
				List<ArticleVO> artList = memberSvc.getArtByMemno(mem_no);
				
				req.setAttribute("artList", artList);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/memberarticle.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				errorMessage.put("elseError", e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/memberarticle.jsp");
				failureView.forward(req, res);
			}
		}
		
		//預覽前先將文章存入session中讓其在preview動作 可以提出
		if("store".equals(action)){
			ArticleVO artVO = new ArticleVO();
			String art_context = (String)req.getParameter("art_context");
			String art_name = (String)req.getParameter("art_name");
			String art_date = (String)req.getParameter("art_date");
			
			HttpSession session = req.getSession();
			session.setAttribute("art_context", art_context);
			session.setAttribute("art_date",art_date);
			session.setAttribute("art_name", art_name);
			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.write("ok");
			out.flush();
			out.close();
		}
		
		//將存進session中的文章拿出並forward到頁面上
		if("preview".equals(action)){
			HttpSession session = req.getSession();
			String art_context = (String)session.getAttribute("art_context");
			String art_date = (String)session.getAttribute("art_date");
			String art_name = (String)session.getAttribute("art_name");
			session.removeAttribute("art_context");
			session.removeAttribute("art_date");
			session.removeAttribute("art_name");
			req.setAttribute("art_context", art_context);
			req.setAttribute("art_date", art_date);
			req.setAttribute("art_name", art_name);
			RequestDispatcher failureView = req.getRequestDispatcher("/front-end/article/article_display.jsp");
			failureView.forward(req, res);
		}
		
		if ("getFor_Display".equals(action)) { // 嚙諉佗蕭select_page.jsp嚙踝蕭嚙請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String art_no = req.getParameter("art_no");
				if (art_no == null || (art_no.trim()).length() == 0) {
					errorMsgs.add("請輸入食記編號");
				}
				
				/***************************2.開始查詢資料�*****************************************/
				ArticleService artSvc = new ArticleService();
				ArticleVO articleVO = artSvc.getOneArticle(art_no);
				
				if (articleVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/article/SelectArticlePage.jsp");
					failureView.forward(req, res);
					return;//嚙緹嚙踝蕭嚙踝蕭嚙稻
				}
				
				Set<Article_ResponseVO> responseSet = artSvc.getArticle_ResponsesByArt_no(articleVO.getArt_no());
				/***************************3.嚙範嚙賠改蕭嚙踝蕭,嚙褒喉蕭嚙踝蕭嚙�(Send the Success view)*************/
				req.setAttribute("articleVO", articleVO); // 嚙踝蕭w嚙踝蕭嚙碼嚙踝蕭empVO嚙踝蕭嚙踝蕭,嚙編嚙皚req
				req.setAttribute("responseSet", responseSet);
				String url = "/front-end/article/listOneArticle.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 嚙踝蕭嚙穀嚙踝蕭嚙� listOneEmp.jsp
				successView.forward(req, res);

				/***************************嚙踝蕭L嚙箠嚙賞的嚙踝蕭嚙羯嚙畿嚙緲*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/article/SelectArticlePage.jsp");
				failureView.forward(req, res);
			}
		}
		
		

		if ("getOne_For_Display".equals(action)) { // 嚙諉佗蕭select_page.jsp嚙踝蕭嚙請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String art_no = req.getParameter("art_no");
				if (art_no == null || (art_no.trim()).length() == 0) {
					errorMsgs.add("請輸入食記編號");
				}
				
				
				
				
				
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/SelectArticlePage.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				
				
				/***************************2.開始查詢資料?*****************************************/
				ArticleService artSvc = new ArticleService();
				ArticleVO articleVO = artSvc.getOneArticle(art_no);
				
				if (articleVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/SelectArticlePage.jsp");
					failureView.forward(req, res);
					return;//嚙緹嚙踝蕭嚙踝蕭嚙稻
				}
				
				Set<Article_ResponseVO> responseSet = artSvc.getArticle_ResponsesByArt_no(articleVO.getArt_no());
				/***************************3.嚙範嚙賠改蕭嚙踝蕭,嚙褒喉蕭嚙踝蕭嚙?(Send the Success view)*************/
				req.setAttribute("articleVO", articleVO); // 嚙踝蕭w嚙踝蕭嚙碼嚙踝蕭empVO嚙踝蕭嚙踝蕭,嚙編嚙皚req
				req.setAttribute("responseSet", responseSet);
				System.out.println("aaaa" +art_no);
				
				String url = "/front-end/Article/listAllArticle.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 嚙踝蕭嚙穀嚙踝蕭嚙? listOneEmp.jsp
				successView.forward(req, res);

				/***************************嚙踝蕭L嚙箠嚙賞的嚙踝蕭嚙羯嚙畿嚙緲*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/SelectArticlePage.jsp");
				failureView.forward(req, res);
			}
		}
		if("listArticle_Responses_ByArt_NO_A".equals(action) || "listArticle_Responses_ByArt_NO_B".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*************************** 1.嚙踝蕭嚙踝蕭嚙請求嚙諸潘蕭 ****************************************/
				String art_no =new String(req.getParameter("art_no"));

				/*************************** 2.嚙罷嚙締嚙範嚙賠賂蕭嚙� ****************************************/
				ArticleService artSvc = new ArticleService();
				Set<Article_ResponseVO> set = artSvc.getArticle_ResponsesByArt_no(art_no);

				/*************************** 3.嚙範嚙賠改蕭嚙踝蕭,嚙褒喉蕭嚙踝蕭嚙�(Send the Success view) ************/
				req.setAttribute("listArticle_Responses_ByArt_NO", set);    // 嚙踝蕭w嚙踝蕭嚙碼嚙踝蕭set嚙踝蕭嚙踝蕭,嚙編嚙皚request

				String url = null;
				if ("listArticle_Responses_ByArt_NO_A".equals(action))
					url = "/front-end/article/listArticle_Responses_ByArt_NO.jsp";        // 嚙踝蕭嚙穀嚙踝蕭嚙� dept/listEmps_ByDeptno.jsp
				else if ("listArticle_Responses_ByArt_NO_B".equals(action))
					url = "/front-end/article/listAllArticle.jsp";              // 嚙踝蕭嚙穀嚙踝蕭嚙� dept/listAllDept.jsp

				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 嚙踝蕭L嚙箠嚙賞的嚙踝蕭嚙羯嚙畿嚙緲 ***********************************/
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		if ("listArticle_ByCompositeQuery".equals(action)) { // 嚙諉佗蕭select_page.jsp嚙踝蕭嚙複合嚙範嚙賠請求
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***************************1.嚙瞇嚙踝蕭J嚙踝蕭嚙踝蕭酮衽ap**********************************/ 
				//嚙衝伐蕭Map<String,String[]> getParameterMap()嚙踝蕭嚙踝蕭k 
				//嚙窯嚙瞇:an immutable java.util.Map 
				Map<String, String[]> map = req.getParameterMap();
				/***************************2.嚙罷嚙締嚙複合嚙範嚙踝蕭***************************************/
				ArticleService artSvc = new ArticleService();
				List<ArticleVO> list  = artSvc.getAll(map);
				
				/***************************3.嚙範嚙賠改蕭嚙踝蕭,嚙褒喉蕭嚙踝蕭嚙�(Send the Success view)************/
				req.setAttribute("listArticle_ByCompositeQuery", list); // 嚙踝蕭w嚙踝蕭嚙碼嚙踝蕭list嚙踝蕭嚙踝蕭,嚙編嚙皚request
				RequestDispatcher successView = req.getRequestDispatcher("/front-end/article/listArticle_ByCompositeQuery.jsp"); // 嚙踝蕭嚙穀嚙踝蕭嚙締istArticle_ByCompositeQuery
				successView.forward(req, res);
				
				/***************************嚙踝蕭L嚙箠嚙賞的嚙踝蕭嚙羯嚙畿嚙緲**********************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/SelectArticlePage.jsp");
				failureView.forward(req, res);
			}
		}

	}
}

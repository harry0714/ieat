package com.article_response.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.article.model.ArticleService;
import com.article.model.ArticleVO;
import com.article_response.model.Article_ResponseService;
import com.article_response.model.Article_ResponseVO;
import com.article_response_report.model.Article_rs_reVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;

public class Article_ResponseServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");

		String art_no = req.getParameter("art_no");

		String art_rs_date = req.getParameter("art_rs_date");
		String mem_no = req.getParameter("mem_no");

		
		
		
		if ("insert".equals(action)) { // 嚙諉佗蕭addEmp.jsp嚙踝蕭嚙請求
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			MemberService memSvc = new MemberService();
			try {

				String art_rs_context = null;
				// HttpSession session = req.getSession();
				// Map<String,String> user =
				// (Map<String,String>)session.getAttribute("user");
				// String mem_no = user.get("mem_no");

				/***********************
				 * 1.嚙踝蕭嚙踝蕭嚙請求嚙諸潘蕭 - 嚙踝蕭J嚙賣式嚙踝蕭嚙踝蕭嚙羯嚙畿嚙緲
				 *************************/
				art_rs_context = req.getParameter("art_rs_context").trim();
				if(art_rs_context.length()==0) {
					errorMsgs.add("請輸入食記回覆內容!");
				}
				
				art_no = req.getParameter("art_no");
				Timestamp tss = new java.sql.Timestamp(System.currentTimeMillis());
				ArticleVO articleVO = new ArticleVO();
				Article_ResponseVO article_responseVO = new Article_ResponseVO();
				
				article_responseVO.setArt_no(art_no);				
				article_responseVO.setMem_no(mem_no);
				article_responseVO.setArt_rs_context(art_rs_context);
				article_responseVO.setArt_rs_date(tss);
			
				/***************************
				 * 2.嚙罷嚙締嚙編嚙磕嚙踝蕭嚙�
				 ***************************************/
				HttpSession session = req.getSession();
				Map<String, String> user = new TreeMap<>();
				req.setAttribute("articleVO", new ArticleService().getOneArticle(req.getParameter("art_no")));
				req.setAttribute("article_responseVO", article_responseVO);
				Article_ResponseService artrsSvc = new Article_ResponseService();
				article_responseVO = artrsSvc.addarticle_response(art_no, mem_no, art_rs_context, tss);

				if (!errorMsgs.isEmpty()) {
					req.setAttribute("article_responseVO", article_responseVO); 
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/article/listOneArticle.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************
				 * 3.嚙編嚙磕嚙踝蕭嚙踝蕭,嚙褒喉蕭嚙踝蕭嚙�(Send the Success view)
				 ***********/
				String url = "/front-end/article/listOneArticle.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				// Send the use back to the form, if there were errors
				
				/*************************** 嚙踝蕭L嚙箠嚙賞的嚙踝蕭嚙羯嚙畿嚙緲 **********************************/
			} catch (Exception e) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/article/listOneArticle.jsp");
				failureView.forward(req, res);
			}
		}

		if ("update".equals(action)) { // 嚙諉佗蕭update_emp_input.jsp嚙踝蕭嚙請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			String art_rs_context = null;

			try {
				/***************************
				 * 1.嚙踝蕭嚙踝蕭嚙請求嚙諸潘蕭 - 嚙踝蕭J嚙賣式嚙踝蕭嚙踝蕭嚙羯嚙畿嚙緲
				 **********************/

				art_rs_context = req.getParameter("art_rs_context");

				String art_rs_no = new String(req.getParameter("art_rs_no"));

				if (art_rs_context == null || (art_rs_context.trim()).length() == 0) {
					errorMsgs.add("請輸入食記回覆內容!");
				}

				Timestamp tss = Timestamp.valueOf(art_rs_date);

				Article_ResponseVO article_responseVO = new Article_ResponseVO();

				article_responseVO.setArt_rs_no(art_rs_no);
				article_responseVO.setArt_no(art_no);
				article_responseVO.setMem_no(mem_no);
				article_responseVO.setArt_rs_context(art_rs_context);
				article_responseVO.setArt_rs_date(tss);

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("article_responseVO", article_responseVO); // 嚙緣嚙踝蕭嚙踝蕭J嚙賣式嚙踝蕭嚙羯嚙踝蕭empVO嚙踝蕭嚙踝蕭,嚙稽嚙編嚙皚req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/Article_Response/update_article_rs_input.jsp");
					failureView.forward(req, res);
					return; // 嚙緹嚙踝蕭嚙踝蕭嚙稻
				}

				/***************************
				 * 2.嚙罷嚙締嚙論改蕭嚙踝蕭
				 *****************************************/
				Article_ResponseService artrsSvc = new Article_ResponseService();
				article_responseVO = artrsSvc.updatarticle_response(art_rs_no, art_no, mem_no, art_rs_context, tss);

				/***************************
				 * 3.嚙論改完嚙踝蕭,嚙褒喉蕭嚙踝蕭嚙�(Send the Success view)
				 *************/

				req.setAttribute("article_responseVO", article_responseVO); // 嚙踝蕭wupdate嚙踝蕭嚙穀嚙踝蕭,嚙踝蕭嚙確嚙踝蕭嚙踝蕭empVO嚙踝蕭嚙踝蕭,嚙編嚙皚req
				String url = "/front_end/Article_Response/listOneArticle_Response.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/***************************
				 * 嚙踝蕭L嚙箠嚙賞的嚙踝蕭嚙羯嚙畿嚙緲***********************
				 **************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/Article_Response/update_article_rs_input.jsp");
				failureView.forward(req, res);
			}
		}

		if ("getOne_For_Display".equals(action)) { // 嚙諉佗蕭select_page.jsp嚙踝蕭嚙請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***************************
				 * 1.嚙踝蕭嚙踝蕭嚙請求嚙諸潘蕭 - 嚙踝蕭J嚙賣式嚙踝蕭嚙踝蕭嚙羯嚙畿嚙緲
				 **********************/

				String str = req.getParameter("art_rs_no");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入食記編號");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/SelectArticle_RS_Page.jsp");
					failureView.forward(req, res);
					return;// 嚙緹嚙踝蕭嚙踝蕭嚙稻
				}

				String art_rs_no = null;
				try {
					art_rs_no = str;
				} catch (Exception e) {
					errorMsgs.add("輸入食記編號");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/SelectArticle_RS_Page.jsp");
					failureView.forward(req, res);
					return;// 嚙緹嚙踝蕭嚙踝蕭嚙稻
				}

				/***************************
				 * 2.嚙罷嚙締嚙範嚙賠賂蕭嚙�
				 *****************************************/

				Article_ResponseService artrsSvc = new Article_ResponseService();

				Article_ResponseVO article_responseVO = artrsSvc.getOneArticle_Response(art_rs_no);

				if (article_responseVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors

				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/SelectArticle_RS_Page.jsp");
					failureView.forward(req, res);
					return;// 嚙緹嚙踝蕭嚙踝蕭嚙稻
				}

				/***************************
				 * 3.嚙範嚙賠改蕭嚙踝蕭,嚙褒喉蕭嚙踝蕭嚙�(Send the Success view)
				 *************/
				req.setAttribute("article_responseVO", article_responseVO); // 嚙踝蕭w嚙踝蕭嚙碼嚙踝蕭empVO嚙踝蕭嚙踝蕭,嚙編嚙皚req
				String url = "/front_end/Article_Response/listOneArticle_Response.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 嚙踝蕭嚙穀嚙踝蕭嚙�
																				// listOneEmp.jsp
				successView.forward(req, res);

				/*************************** 嚙踝蕭L嚙箠嚙賞的嚙踝蕭嚙羯嚙畿嚙緲 *************************************/

			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/SelectArticle_RS_Page.jsp");
				failureView.forward(req, res);
			}
		}

		if ("delete".equals(action)) { // 嚙諉佗蕭listAllEmp.jsp

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String requestURL = req.getParameter("requestURL"); // 送出刪除的來源網頁路徑: 可能為【/emp/listAllEmp.jsp】 或  【/dept/listEmps_ByDeptno.jsp】 或 【 /dept/listAllDept.jsp】 或 【 /emp/listEmps_ByCompositeQuery.jsp】
			try {
				/***************************
				 * 1.嚙踝蕭嚙踝蕭嚙請求嚙諸潘蕭
				 ***************************************/
				String art_rs_no = new String(req.getParameter("art_rs_no"));

				/***************************
				 * 2.嚙罷嚙締嚙磋嚙踝蕭嚙踝蕭嚙�
				 ***************************************/
				Article_ResponseService artrsSvc = new Article_ResponseService();

				req.setAttribute("articleVO", new ArticleService().getOneArticle(req.getParameter("art_no")));
				

				artrsSvc.deletearticle_response(art_rs_no);

			
				/***************************
				 * 3.嚙磋嚙踝蕭嚙踝蕭嚙踝蕭,嚙褒喉蕭嚙踝蕭嚙�(Send the Success view)
				 ***********/

				RequestDispatcher successView = req.getRequestDispatcher(requestURL);// 嚙磋嚙踝蕭嚙踝蕭嚙穀嚙踝蕭,嚙踝蕭嚙稷嚙箴嚙碼嚙磋嚙踝蕭嚙踝蕭嚙諉瘀蕭嚙踝蕭嚙踝蕭
				successView.forward(req, res);
					

			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/Article/listOneArticle.jsp");
				failureView.forward(req, res);
			}
		}
		
		
		
		if ("getOne_For_Update".equals(action)) { // 嚙諉佗蕭listAllEmp.jsp嚙踝蕭嚙請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***************************
				 * 1.嚙踝蕭嚙踝蕭嚙請求嚙諸潘蕭
				 ****************************************/
				String art_rs_no = new String(req.getParameter("art_rs_no"));

				/***************************
				 * 2.嚙罷嚙締嚙範嚙賠賂蕭嚙�
				 ****************************************/
				Article_ResponseService artrsSvc = new Article_ResponseService();
				Article_ResponseVO article_responseVO = artrsSvc.getOneArticle_Response(art_rs_no);

				/***************************
				 * 3.嚙範嚙賠改蕭嚙踝蕭,嚙褒喉蕭嚙踝蕭嚙�(Send the Success view)
				 ************/
				req.setAttribute("article_responseVO", article_responseVO); // 嚙踝蕭w嚙踝蕭嚙碼嚙踝蕭empVO嚙踝蕭嚙踝蕭,嚙編嚙皚req
				String url = "/front_end/Article_Response/update_article_rs_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 嚙踝蕭嚙穀嚙踝蕭嚙�
																				// update_emp_input.jsp
				successView.forward(req, res);

				/*************************** 嚙踝蕭L嚙箠嚙賞的嚙踝蕭嚙羯嚙畿嚙緲 **********************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/Article_Response_Report/listAllArticle_Response.jsp");
				failureView.forward(req, res);
			}
		}

		if ("listArticle_rs_re_ByArt_RS_NO_A".equals(action) || "listArticle_rs_re_ByArt_RS_NO_B".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/***************************
				 * 1.嚙踝蕭嚙踝蕭嚙請求嚙諸潘蕭
				 ****************************************/
				String art_rs_no = new String(req.getParameter("art_rs_no"));

				/***************************
				 * 2.嚙罷嚙締嚙範嚙賠賂蕭嚙�
				 ****************************************/
				Article_ResponseService artrsSvc = new Article_ResponseService();
				Set<Article_rs_reVO> set = artrsSvc.getArticle_Responses_ReportByArt_rs_no(art_rs_no);

				/***************************
				 * 3.嚙範嚙賠改蕭嚙踝蕭,嚙褒喉蕭嚙踝蕭嚙�(Send the Success view)
				 ************/
				req.setAttribute("listArticle_rs_re_ByArt_RS_NO", set); // 嚙踝蕭w嚙踝蕭嚙碼嚙踝蕭set嚙踝蕭嚙踝蕭,嚙編嚙皚request

				String url = null;
				if ("listArticle_rs_re_ByArt_RS_NO_A".equals(action))
					url = "/back_end/Article_Response_Report/listArticle_rs_re_ByArt_RS_NO.jsp"; // 嚙踝蕭嚙穀嚙踝蕭嚙�
																									// dept/listEmps_ByDeptno.jsp
				else if ("listArticle_rs_re_ByArt_RS_NO_B".equals(action))
					url = "/back_end/Article_Response_Report/listAllArticle_Response.jsp"; // 嚙踝蕭嚙穀嚙踝蕭嚙�
																							// dept/listAllDept.jsp
				// url = "/Article_Response/listArticle_rs_re_ByArt_RS_NO.jsp";

				System.out.println(url);
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 嚙踝蕭L嚙箠嚙賞的嚙踝蕭嚙羯嚙畿嚙緲 ***********************************/
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}
}

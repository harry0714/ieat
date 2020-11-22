package com.article_response_report.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.article.model.ArticleService;
import com.article_response.model.Article_ResponseService;
import com.article_response.model.Article_ResponseVO;
import com.article_response_report.model.Article_rs_reService;
import com.article_response_report.model.Article_rs_reVO;
import com.email.model.MailService;
import com.member.model.MemberService;
import com.member.model.MemberVO;

public class ArticleResponseReportServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String action = req.getParameter("action");
		
		//進入後端食記回覆檢舉頁面
		if("enterarticleresponsereport".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String status_selected = (String)req.getParameter("status_selected");
				Article_rs_reService arrSvc = new Article_rs_reService();
				List<Article_rs_reVO> arrList = null;
				if(status_selected.isEmpty()){
					arrList = arrSvc.getAll();
				}else{
					arrList = arrSvc.getMoreByArticelResponseReportStatus(status_selected);
				}
				req.setAttribute("arrList", arrList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-response-report/articleresponsereport.jsp");
				failureView.forward(req, res);
				
			}catch(Exception e){
				errorMessage.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-response-report/articleresponsereport.jsp");
				failureView.forward(req, res);
			}
		}
		
		//檢舉通過
		if("checkpass".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String art_rs_re_no = (String)req.getParameter("art_rs_re_no");
				Article_rs_reService arrSvc = new Article_rs_reService();
				Article_rs_reVO arrVO = arrSvc.getOneArticle_RS_RE(art_rs_re_no);
				if(!arrVO.getArt_rs_re_sta().equals("0")){
					errorMessage.add("此食記回覆檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-response-report/articleresponsereport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更店家檢舉狀態為1
				arrVO.setArt_rs_re_sta("1");
				arrSvc.updatart_rs_re(arrVO.getMem_no(), arrVO.getArt_rs_no(), arrVO.getArt_rs_re_date(), arrVO.getArt_rs_re_con(), arrVO.getArt_rs_re_sta(), arrVO.getArt_rs_re_no());
				//
				String status_selected = (String)req.getParameter("status_selected");
				List<Article_rs_reVO> arrList = null;
				if(status_selected.isEmpty()){
					arrList = arrSvc.getAll();
				}else{
					arrList = arrSvc.getMoreByArticelResponseReportStatus(status_selected);
				}
				req.setAttribute("arrList", arrList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-response-report/articleresponsereport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-response-report/articleresponsereport.jsp");
				failureView.forward(req, res);
			}
		}
		
		//檢舉不通過
		if("checkfail".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String art_rs_re_no = (String)req.getParameter("art_rs_re_no");
				Article_rs_reService arrSvc = new Article_rs_reService();
				Article_rs_reVO arrVO = arrSvc.getOneArticle_RS_RE(art_rs_re_no);
				if(!arrVO.getArt_rs_re_sta().equals("0")){
					errorMessage.add("此食記回覆檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-response-report/articleresponsereport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更店家檢舉狀態為1
				arrVO.setArt_rs_re_sta("2");
				arrSvc.updatart_rs_re(arrVO.getMem_no(), arrVO.getArt_rs_no(), arrVO.getArt_rs_re_date(), arrVO.getArt_rs_re_con(), arrVO.getArt_rs_re_sta(), arrVO.getArt_rs_re_no());
				//
				String status_selected = (String)req.getParameter("status_selected");
				List<Article_rs_reVO> arrList = null;
				if(status_selected.isEmpty()){
					arrList = arrSvc.getAll();
				}else{
					arrList = arrSvc.getMoreByArticelResponseReportStatus(status_selected);
				}
				req.setAttribute("arrList", arrList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-response-report/articleresponsereport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-response-report/articleresponsereport.jsp");
				failureView.forward(req, res);
			}
		}
		
		/********************** 新增檢舉 ***************************************/
		if ("insert".equals(action)) { // =來自addEmp.jsp的請求  
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			String mem_no = req.getParameter("mem_no");
			String art_rs_no = req.getParameter("art_rs_no");
	        String art_rs_re_date = req.getParameter("art_rs_re_date");
	        
			try {
				String art_rs_re_con = null;
				String art_rs_re_sta = req.getParameter("art_rs_re_sta");
				/***********************1.�����ШD�Ѽ� - ��J�榡�����~�B�z*************************/
				art_rs_re_con =req.getParameter("art_rs_re_con");
				
				try {
					art_rs_re_con = req.getParameter("art_rs_re_con");
				} catch (Exception e) {
					errorMsgs.add("請輸入回覆檢舉內容!");
				}		
				Article_rs_reVO article_rs_reVO = new Article_rs_reVO();
				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date current = new Date();
				String sampleDate = sdFormat.format(current);
				Timestamp tss = java.sql.Timestamp.valueOf(sampleDate);					
								
				article_rs_reVO.setMem_no(mem_no);
				article_rs_reVO.setArt_rs_no(art_rs_no);
				article_rs_reVO.setArt_rs_re_date(tss);
				article_rs_reVO.setArt_rs_re_con(art_rs_re_con);
			

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("article_rs_reVO", article_rs_reVO); // �t����J�榡���~��empVO����,�]�s�Jreq
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back-end/Article_Response_Report/addArticle_Response_Resport.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************2.�}�l�s�W���***************************************/
				HttpSession session = req.getSession();
				Map<String, String> user = new TreeMap<>();
				req.setAttribute("articleVO", new ArticleService().getOneArticle(req.getParameter("art_no")));
				req.setAttribute("article_responseVO", new Article_ResponseService().getOneArticle_Response(req.getParameter("art_rs_no")));
				req.setAttribute("article_rs_reVO", article_rs_reVO);
				Article_rs_reService artrsreSvc = new Article_rs_reService();
				article_rs_reVO = artrsreSvc.addarticle_rs_re(mem_no,art_rs_no,  tss , art_rs_re_con,art_rs_re_sta);
				
				/***************************3.�s�W����,�ǳ����(Send the Success view)***********/
				String url = "/front-end/article/listOneArticle.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); 
				successView.forward(req, res);				
				
				/***************************��L�i�઺���~�B�z**********************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/article/listOneArticle.jsp");
				failureView.forward(req, res);
			}
		}
		
	}
}

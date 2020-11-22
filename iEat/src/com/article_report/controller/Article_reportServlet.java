package com.article_report.controller;

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
import com.article.model.ArticleVO;
import com.article_report.model.Article_ReportService;
import com.article_report.model.Article_reportVO;
import com.article_response.model.Article_ResponseService;
import com.article_response.model.Article_ResponseVO;
import com.email.model.MailService;
import com.member.model.MemberService;
import com.member.model.MemberVO;


public class Article_reportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String action = req.getParameter("action");
		
		//取的食記檢舉資料
		if("enterarticlereport".equals(action)){
			
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String status_selected = (String)req.getParameter("status_selected");
				Article_ReportService arSvc = new Article_ReportService();
				List<Article_reportVO> arList = null;
				if(status_selected.isEmpty()){

					arList = arSvc.getAll(); //取的所有食記檢舉資料

				}else{
					arList = arSvc.getMoreByArticleReportStatus(status_selected); //取得在status_selected狀態下的食記檢舉資料
				}
				req.setAttribute("arList", arList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
				failureView.forward(req, res);
				
			}catch(Exception e){
				errorMessage.add(e.getMessage());
				System.out.println(e);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
				failureView.forward(req, res);
			}
		}
		
		// 後端 進入食記檢舉管理的頁面  避免和前端的混淆  所以用另外的
		if("backEndenterarticlereport".equals(action)) {
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String status_selected = (String)req.getParameter("status_selected");
				Article_ReportService arSvc = new Article_ReportService();
				List<Article_reportVO> arList = null;
				if(status_selected.isEmpty()){
					arList = arSvc.getAll();
				}else{
					arList = arSvc.getMoreByArticleReportStatus(status_selected);
				}
				req.setAttribute("arList", arList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
				failureView.forward(req, res);
				
			}catch(Exception e){
				errorMessage.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
				failureView.forward(req, res);
			}
		}
		
		
		//檢舉通過
//		if("checkpass".equals(action)){
//			List<String> errorMessage = new ArrayList<>();
//			req.setAttribute("errorMessage", errorMessage);
//			try{
//				String art_re_no = (String)req.getParameter("art_re_no");
//				Article_ReportService arSvc = new Article_ReportService();
//				Article_reportVO arVO = arSvc.getOnearticle(art_re_no);
//				if(!arVO.getArt_re_status().equals("0")){
//					errorMessage.add("此店家檢舉已被審核");
//					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
//					failureView.forward(req, res);
//					return;
//				}
//				//變更食記檢舉狀態為1
//				arVO.setArt_re_status("1");
//				arSvc.updatarticle_report(arVO.getArt_re_no(), arVO.getArt_no(), arVO.getArt_re_date(), arVO.getArt_re_context(), arVO.getMem_no(), arVO.getArt_re_status());
//				
//				//寄信給被檢舉的會員
//				MailService mailSvc = new MailService();
//				ArticleService articleSvc = new ArticleService();
//				ArticleVO articleVO = articleSvc.getOneArticle(arVO.getArt_no());
//				MemberService memberSvc = new MemberService();
//				MemberVO flagged = memberSvc.getOneMember(articleVO.getMem_no());
//				String flaggedMessage = flagged.getMem_name()+" 你好!\n你所撰寫的食記\n 編號:'"+articleVO.getArt_no()+"'\n標題:'"+articleVO.getArt_name()+"'\n以被檢舉，檢舉內容為:'"+arVO.getArt_re_context()+"'\n請盡速改善謝謝";
//				
//				mailSvc.sendMail(flagged.getMem_email(),"iEat 檢舉通知", flaggedMessage);
//				
//				//寄信給檢舉人
//				MemberVO prosecutor = memberSvc.getOneMember(arVO.getMem_no());
//				String prosecutorMessage = prosecutor.getMem_name()+" 你好!\n你檢舉的食記:'"+articleVO.getArt_name()+"'\n已審核通過，已請撰寫者盡速處理。";
//				mailSvc.sendMail(prosecutor.getMem_email(), "iEat 檢舉通知", prosecutorMessage);
//				//
//				String status_selected = (String)req.getParameter("status_selected");
//				List<Article_reportVO> arList = null;
//				if(status_selected.isEmpty()){
//					arList = arSvc.getAll();
//				}else{
//					arList = arSvc.getMoreByArticleReportStatus(status_selected);
//				}
//				req.setAttribute("arList", arList);
//				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
//				failureView.forward(req, res);
//			}catch(Exception e){
//				System.out.println();
//				errorMessage.add("有錯誤:" + e.getMessage());
//				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
//				failureView.forward(req, res);
//				
//			}
//		}
		
		//檢舉通過
		if("checkpass".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String art_re_no = (String)req.getParameter("art_re_no");
				
				Article_ReportService arSvc = new Article_ReportService();
				Article_reportVO arVO = arSvc.getOnearticle(art_re_no);
				if(!arVO.getArt_re_status().equals("0")){
					errorMessage.add("此店家檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更食記檢舉狀態為1
				arVO.setArt_re_status("1");
				arSvc.updatarticle_report(arVO.getArt_re_no(), arVO.getArt_no(), arVO.getArt_re_date(), arVO.getArt_re_context(), arVO.getMem_no(), arVO.getArt_re_status());
				
				//
				String status_selected = (String)req.getParameter("status_selected");
				List<Article_reportVO> arList = null;
				if(status_selected.isEmpty()){
					arList = arSvc.getAll();
				}else{
					arList = arSvc.getMoreByArticleReportStatus(status_selected);
				}
				req.setAttribute("arList", arList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				System.out.println();
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
				failureView.forward(req, res);
			}
		}
		
		//檢舉不通過
		if("checkfail".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String art_re_no = (String)req.getParameter("art_re_no");
				System.out.println(art_re_no+"----");
				Article_ReportService arSvc = new Article_ReportService();
				Article_reportVO arVO = arSvc.getOnearticle(art_re_no);
				if(!arVO.getArt_re_status().equals("0")){
					
					errorMessage.add("此店家檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更食檢舉狀態為2
				arVO.setArt_re_status("2");
				arSvc.updatarticle_report(arVO.getArt_re_no(), arVO.getArt_no(), arVO.getArt_re_date(), arVO.getArt_re_context(), arVO.getMem_no(), arVO.getArt_re_status());
				
				String status_selected = (String)req.getParameter("status_selected");
				List<Article_reportVO> arList = null;
				if(status_selected.isEmpty()){
					arList = arSvc.getAll();
				}else{
					arList = arSvc.getMoreByArticleReportStatus(status_selected);
				}
				req.setAttribute("arList", arList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				System.out.println();
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/article-report/articlereport.jsp");
				failureView.forward(req, res);
			}
		}
		
		/***********************新增食記檢舉  來自單筆食記查詢***********************************/
		if ("insert".equals(action)) { // =來自addEmp.jsp的請求  
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String art_no = req.getParameter("art_no");
			String mem_no = req.getParameter("mem_no");
			try {
				String art_re_context = null;
				String art_re_status = req.getParameter("art_re_status");
				
				art_re_context =req.getParameter("art_re_context");
				try {
					art_re_context = req.getParameter("art_re_context");
				} catch (Exception e) {
					errorMsgs.add("請輸入檢舉內容!");
				}
				Article_reportVO article_reportVO = new Article_reportVO();
				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date current = new Date(); //取得日期
				String sampleDate = sdFormat.format(current);
				Timestamp tss =Timestamp.valueOf(sampleDate);
				ArticleVO articleVO = new ArticleVO();
				
				article_reportVO.setArt_no(art_no);
				article_reportVO.setMem_no(mem_no);
				article_reportVO.setArt_re_context(art_re_context);
				article_reportVO.setArt_re_date(tss);

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("article_reportVO", article_reportVO); // �t����J�榡���~��empVO����,�]�s�Jreq
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back-end/Article_Report/addArticle_Report.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************2.�}�l�s�W���***************************************/
				HttpSession session = req.getSession();
				Map<String, String> user = new TreeMap<>();
				req.setAttribute("articleVO", new ArticleService().getOneArticle(req.getParameter("art_no")));
				req.setAttribute("article_reportVO", article_reportVO);
				Article_ReportService artreSvc = new Article_ReportService();
				article_reportVO = artreSvc.addarticle_report(art_no, mem_no, art_re_context, tss, art_re_status);
				
				/***************************3.�s�W����,�ǳ����(Send the Success view)***********/
				String url = "/front-end/article/listOneArticle.jsp";
				req.setAttribute("reportSuccess", "reportSuccess");
				RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
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

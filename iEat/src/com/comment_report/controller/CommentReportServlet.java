package com.comment_report.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.comment_report.model.CommentReportService;
import com.comment_report.model.CommentReportVO;
import com.email.model.MailService;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;
import com.store_comment.model.StoreCommentService;
import com.store_comment.model.StoreCommentVO;


public class CommentReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		String action = (String) req.getParameter("action");
		
		//進入後端店家評論檢舉頁面
		if("entercommentreport".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String status_selected = (String)req.getParameter("status_selected");
				CommentReportService crSvc = new CommentReportService();
				List<CommentReportVO> crList = null;
				if(status_selected.isEmpty()){
					crList = crSvc.getAll();
				}else{
					crList = crSvc.getMoreByCommentReportStatus(status_selected);
				}
				req.setAttribute("crList", crList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/comment-report/commentreport.jsp");
				failureView.forward(req, res);
				
			}catch(Exception e){
				errorMessage.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/comment-report/commentreport.jsp");
				failureView.forward(req, res);
			}
		}
		
		//檢舉通過
		if("checkpass".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String comment_report_no = (String)req.getParameter("comment_report_no");
				CommentReportService crSvc = new CommentReportService();
				CommentReportVO crVO = crSvc.getOneCommentReport(comment_report_no);
				if(!crVO.getComment_report_status().equals("0")){
					errorMessage.add("此店家檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/comment-report/commentreport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更店家檢舉狀態為1
				crVO.setComment_report_status("1");
				crSvc.updateCommentReportVO(crVO.getComment_report_no(), crVO.getComment_no(), crVO.getMem_no(), crVO.getComment_report_context(), crVO.getComment_report_date(), crVO.getComment_report_status());
				//
				String status_selected = (String)req.getParameter("status_selected");
				List<CommentReportVO> crList = null;
				if(status_selected.isEmpty()){
					crList = crSvc.getAll();
				}else{
					crList = crSvc.getMoreByCommentReportStatus(status_selected);
				}
				req.setAttribute("crList", crList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/comment-report/commentreport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				System.out.println();
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/comment-report/commentreport.jsp");
				failureView.forward(req, res);
				
			}
		}
		
		//檢舉不通過
		if("checkfail".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String comment_report_no = (String)req.getParameter("comment_report_no");
				CommentReportService crSvc = new CommentReportService();
				CommentReportVO crVO = crSvc.getOneCommentReport(comment_report_no);
				if(!crVO.getComment_report_status().equals("0")){
					errorMessage.add("此店家檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/comment-report/commentreport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更店家檢舉狀態為2
				crVO.setComment_report_status("2");
				crSvc.updateCommentReportVO(crVO.getComment_report_no(), crVO.getComment_no(), crVO.getMem_no(), crVO.getComment_report_context(), crVO.getComment_report_date(), crVO.getComment_report_status());
				//
				String status_selected = (String)req.getParameter("status_selected");
				List<CommentReportVO> crList = null;
				if(status_selected.isEmpty()){
					crList = crSvc.getAll();
				}else{
					crList = crSvc.getMoreByCommentReportStatus(status_selected);
				}
				
				req.setAttribute("crList", crList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/comment-report/commentreport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				System.out.println();
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/comment-report/commentreport.jsp");
				failureView.forward(req, res);
				
			}
		}
		
		if("addAjax".equals(action)){
			try{
				String comment_no = (String)req.getParameter("comment_no");
				String comment_report_context = (String)req.getParameter("comment_report_context");
				HttpSession session = req.getSession();
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				if(user == null){
					out.write("login");
					out.flush();
					out.close();
					return;
				}
				String mem_no = user.get("no");
				CommentReportService crSvc = new CommentReportService();
				crSvc.addCommentReport(comment_no, mem_no, comment_report_context,"0");
				out.write("add");
				out.flush();
				out.close();
				
			}catch(Exception e){
				out.write("error"+e.toString());
				out.flush();
				out.close();
			}
		}
	}

}

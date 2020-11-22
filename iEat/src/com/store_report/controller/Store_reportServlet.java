package com.store_report.controller;

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

import com.email.model.MailService;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;
import com.store_report.model.Store_reportService;
import com.store_report.model.Store_reportVO;


public class Store_reportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = res.getWriter();
		String action = (String) req.getParameter("action");
		
		//後端用 取得店家檢舉資料
		if("enterstorereport".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String status_selected = (String)req.getParameter("status_selected");
				Store_reportService srSvc = new Store_reportService();
				List<Store_reportVO> srList = null;
				if(status_selected.isEmpty()){
					srList = srSvc.getAll();
				}else{
					srList = srSvc.getMoreByStoreReportStatus(status_selected);
				}
				req.setAttribute("srList", srList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/store-report/storereport.jsp");
				failureView.forward(req, res);
				
			}catch(Exception e){
				errorMessage.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/store-report/storereport.jsp");
				failureView.forward(req, res);
			}
		}
		
		//檢舉通過
		if("checkpass".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String store_report_no = (String)req.getParameter("store_report_no");
				System.out.println(store_report_no);
				Store_reportService srSvc = new Store_reportService();
				Store_reportVO srVO = srSvc.getOneStore_report(store_report_no);
				if(!srVO.getStore_report_status().equals("0")){
					errorMessage.add("此店家檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/store-report/storereport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更店家檢舉狀態為1
				srVO.setStore_report_status("1");
				srSvc.udpateStore_report(srVO.getStore_report_no(), srVO.getStore_no(), srVO.getMem_no(), srVO.getStore_report_content(), srVO.getStore_report_date(), srVO.getStore_report_status());
				//
				String status_selected = (String)req.getParameter("status_selected");
				List<Store_reportVO> srList = null;
				if(status_selected.isEmpty()){
					srList = srSvc.getAll();
				}else{
					srList = srSvc.getMoreByStoreReportStatus(status_selected);
				}
				req.setAttribute("srList", srList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/store-report/storereport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/store-report/storereport.jsp");
				failureView.forward(req, res);
			}
		}
		
		//檢舉不通過
		if("checkfail".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String store_report_no = (String)req.getParameter("store_report_no");

				Store_reportService srSvc = new Store_reportService();
				Store_reportVO srVO = srSvc.getOneStore_report(store_report_no);
				if(!srVO.getStore_report_status().equals("0")){
					errorMessage.add("此店家檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/store-report/storereport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更店家檢舉狀態為2
				srVO.setStore_report_status("2");
				srSvc.udpateStore_report(srVO.getStore_report_no(), srVO.getStore_no(), srVO.getMem_no(), srVO.getStore_report_content(), srVO.getStore_report_date(), srVO.getStore_report_status());
				
				//
				String status_selected = (String)req.getParameter("status_selected");
				List<Store_reportVO> srList = null;
				if(status_selected.isEmpty()){
					srList = srSvc.getAll();
				}else{
					srList = srSvc.getMoreByStoreReportStatus(status_selected);
				}
				
				req.setAttribute("srList", srList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/store-report/storereport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/store-report/storereport.jsp");
				failureView.forward(req, res);
			}
		}
		
		if("addAjax".equals(action)){
			try{
				String store_no = (String)req.getParameter("store_no");
				String store_report_content = (String)req.getParameter("store_report_content");
				HttpSession session = req.getSession();
				Map<String,String> user = (Map<String,String>)session.getAttribute("user");
				String mem_no = user.get("no");
				Store_reportService srSvc = new Store_reportService();
				srSvc.addStore_report(store_no, mem_no, store_report_content, "0");
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

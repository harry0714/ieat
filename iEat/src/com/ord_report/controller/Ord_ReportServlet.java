package com.ord_report.controller;

import java.io.IOException;
import java.sql.Date;
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

import com.email.model.MailService;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.ord.model.OrdService;
import com.ord.model.OrdVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;

public class Ord_ReportServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String action = req.getParameter("action");
		//後端 取得訂餐檢舉資料
		if("enterorderreport".equals(action)){
			
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String status_selected = (String)req.getParameter("status_selected");
				OrdService ordSvc = new OrdService();
				List<OrdVO> ordList = null;
				if(status_selected.isEmpty()){
					ordList = ordSvc.getgetMoreByOrderReportStatus(); //取的所有訂餐檢舉資料
				}else{
					ordList = ordSvc.getgetMoreByOrderReportStatus(status_selected); //取的在status_selected狀態下的訂餐檢舉資料
				}
				req.setAttribute("ordList", ordList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/order-report/orderreport.jsp");
				failureView.forward(req, res);
				
			}catch(Exception e){
				errorMessage.add(e.getMessage());
				System.out.println(e);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/order-report/orderreport.jsp");
				failureView.forward(req, res);
			}
		}
		
		//審核通過
		if("checkpass".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String ord_no = (String)req.getParameter("ord_no");
				OrdService ordSvc = new OrdService();
				OrdVO ordVO = ordSvc.getOneMeal(ord_no);
				if(!ordVO.getOrd_report_status().equals("0")){
					errorMessage.add("此訂餐檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/order-report/orderreport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更食記檢舉狀態為1
				ordVO.setOrd_report_status("1");
				ordSvc.updateOrd(ordVO.getMem_no(), ordVO.getStore_no(), ordVO.getOrd_date(), ordVO.getOrd_pickup(), ordVO.getOrd_qrcode_status(), ordVO.getOrd_paid(), ordVO.getOrd_report(), ordVO.getOrd_report_status(), ordVO.getOrd_no());
				String status_selected = (String)req.getParameter("status_selected");
				List<OrdVO> ordList = null;
				if(status_selected.isEmpty()){
					ordList = ordSvc.getgetMoreByOrderReportStatus();
				}else{
					ordList = ordSvc.getgetMoreByOrderReportStatus(status_selected);
				}
				req.setAttribute("ordList", ordList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/order-report/orderreport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				System.out.println();
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/order-report/orderreport.jsp");
				failureView.forward(req, res);
				
			}
		}
		
		//審核不通通過
		if("checkfail".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String ord_no = (String)req.getParameter("ord_no");
				OrdService ordSvc = new OrdService();
				OrdVO ordVO = ordSvc.getOneMeal(ord_no);
				if(!ordVO.getOrd_report_status().equals("0")){
					errorMessage.add("此訂餐檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/order-report/orderreport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更食檢舉狀態為2
				ordVO.setOrd_report_status("2");
				ordSvc.updateOrd(ordVO.getMem_no(), ordVO.getStore_no(), ordVO.getOrd_date(), ordVO.getOrd_pickup(), ordVO.getOrd_qrcode_status(), ordVO.getOrd_paid(), ordVO.getOrd_report(), ordVO.getOrd_report_status(), ordVO.getOrd_no());
				String status_selected = (String)req.getParameter("status_selected");
				List<OrdVO> ordList = null;
				if(status_selected.isEmpty()){
					ordList = ordSvc.getgetMoreByOrderReportStatus();
				}else{
					ordList = ordSvc.getgetMoreByOrderReportStatus(status_selected);
				}
				req.setAttribute("ordList", ordList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/order-report/orderreport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/order-report/orderreport.jsp");
				failureView.forward(req, res);
				
			}
		}
	}

}

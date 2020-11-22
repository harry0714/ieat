package com.tousweb.controller;

import java.io.IOException;
import java.util.LinkedList;
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
import com.email.model.tousMailService;

@WebServlet("/tousweb.do")
public class TouswebbServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res) 
	throws ServletException, IOException {
		doPost(req, res); 
	}	
	public void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8"); 
		String action = req.getParameter("action"); 
		
		
	System.out.println("111");
		/********************************新增信件*****************/
		if("Send_Mail".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			String yourmail = req.getParameter("yourmail");
			String messageText = req.getParameter("messageText");
			String subject = req.getParameter("subject");
			String to = req.getParameter("to");
					
			
			
			
System.out.println("222");		
			req.setAttribute("errorMsgs", errorMsgs);
//		try {	
		
			
System.out.println("55555");			
			tousMailService tmailService = new tousMailService(); 
			tmailService.sendMail(to, subject, messageText,yourmail);
			
System.out.println("eoljffjoewewfo[lewkffew]");

			
			req.setAttribute("success", "success");
			RequestDispatcher successView = req.getRequestDispatcher("/front-end/contact/iEatcontact.jsp");
			successView.forward(req, res);
		
				
		/**************************資料有誤***********************************/		
			
//			} catch (Exception e) {
//		
//				errorMsgs.add("無法取得資料:" + e.getMessage());
//				RequestDispatcher failureView = req
//						.getRequestDispatcher("/front_end/Tousweb/iEatcontact.jsp");
//			
//				failureView.forward(req, res);
//			}
			
			
		}
	}
}
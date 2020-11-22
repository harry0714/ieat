package com.qa.controller;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

import com.qa.model.QaService;
import com.qa.model.QaVO;
import com.qa.model.QaService;

import com.qa.model.*;



//��ƾڶq�j��fileSizeThreshold�ȮɡA���e�N�Q�g�J�Ϻ�
//�W�ǹL�{���L�׬O��Ӥ��W�LmaxFileSize�ȡA�Ϊ̤W�Ǫ��`�q�j��maxRequestSize �ȳ��|�ߥXIllegalStateException ���`
public class QaServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		
		if ("getOne_For_Display".equals(action)) { // �Ӧ�select_page.jsp���ШD
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.�����ШD�Ѽ� - ��J�榡�����~�B�z**********************/
				String str = req.getParameter("qa_no");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入編號");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back-end/qa/listAllQa.jsp");
					failureView.forward(req, res);
					return;//�{�����_
				}
				
				String qa_no = null;
				try {
					qa_no = new String(str);
				} catch (Exception e) {
					errorMsgs.add("編號格式不正確");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back-end/qa/listAllQa.jsp");
					failureView.forward(req, res);
					return;//�{�����_
				}
				
				/***************************2.�}�l�d�߸��*****************************************/
				QaService qaSvc = new QaService();
				QaVO qaVO = qaSvc.getOneQa(qa_no);
				if (qaVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back-end/qa/listAllQa.jsp");
					failureView.forward(req, res);
					return;//�{�����_
				}
				
				/***************************3.�d�ߧ���,�ǳ����(Send the Success view)*************/
				req.setAttribute("qaVO", qaVO); // ��Ʈw���X��empVO����,�s�Jreq
				String url = "/back-end/qa/listOneQa.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // ���\��� listOneEmp.jsp
				successView.forward(req, res);

				/***************************��L�i�઺���~�B�z*************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back-end/qa/listAllQa.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("delete".equals(action)) { // �Ӧ�listAllEmp.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
	
			try {
				/***************************1.�����ШD�Ѽ�***************************************/
				String qa_no = new String(req.getParameter("qa_no"));
				
				/***************************2.�}�l�R�����***************************************/
				QaService qaSvc = new QaService();
				qaSvc.deleteQa(qa_no);
				
				/***************************3.�R������,�ǳ����(Send the Success view)***********/								
				String url = "/back-end/qa/listAllQa.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// �R�����\��,���^�e�X�R�����ӷ�����
				successView.forward(req, res);
				
				/***************************��L�i�઺���~�B�z**********************************/
			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back-end/qa/listAllQa.jsp");
				failureView.forward(req, res);
			}
		}
		if ("getOne_For_Update".equals(action)) { // �Ӧ�listAllEmp.jsp���ШD
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************1.�����ШD�Ѽ�****************************************/
				String qa_no = new String(req.getParameter("qa_no"));
				
				/***************************2.�}�l�d�߸��****************************************/
				QaService qaSvc = new QaService();
				QaVO qaVO = qaSvc.getOneQa(qa_no);
								
				/***************************3.�d�ߧ���,�ǳ����(Send the Success view)************/
				req.setAttribute("qaVO", qaVO);         // ��Ʈw���X��empVO����,�s�Jreq
				String url = "/back-end/qa/update_qa_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// ���\��� update_emp_input.jsp
				successView.forward(req, res);

				/***************************��L�i�઺���~�B�z**********************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back-end/qa/listAllQa.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("update".equals(action)) { // �Ӧ�update_emp_input.jsp���ШD
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************1.�����ШD�Ѽ� - ��J�榡�����~�B�z**********************/
				String qa_no = req.getParameter("qa_no").trim();
				String qa_type_no = req.getParameter("qa_type_no").trim();			
				String q_context = req.getParameter("q_context").trim();			
				String a_context = req.getParameter("a_context").trim();
				
				QaVO qaVO = new QaVO();
				qaVO.setQa_no(qa_no);
				qaVO.setQa_type_no(qa_type_no);
				qaVO.setQ_context(q_context);
				qaVO.setA_context(a_context);
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("qaVO", qaVO); // �t����J�榡���~��empVO����,�]�s�Jreq
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back-end/qa/update_qa_input.jsp");
					failureView.forward(req, res);
					return; //�{�����_
				}
				
				/***************************2.�}�l�ק���*****************************************/
				QaService qaSvc = new QaService();
				qaVO = qaSvc.updateQa(qa_no,qa_type_no,q_context ,a_context);
				
				/***************************3.�ק粒��,�ǳ����(Send the Success view)*************/
				req.setAttribute("qaVO", qaVO); // ��Ʈwupdate���\��,���T����empVO����,�s�Jreq
				String url = "/back-end/qa/listAllQa.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // �ק令�\��,���listOneEmp.jsp
				successView.forward(req, res);

				/***************************��L�i�઺���~�B�z*************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back-end/qa/update_qa_input.jsp");
				failureView.forward(req, res);
			}
		}
	
	if ("insert".equals(action)) { // �Ӧ�addEmp.jsp���ШD  
		List<String> errorMsgs = new LinkedList<String>();
		// Store this set in the request scope, in case we need to
	    // send the ErrorPage view.
		req.setAttribute("errorMsgs", errorMsgs);
//	try {
		/***********************1.�����ШD�Ѽ� - ��J�榡�����~�B�z*************************/
			String qa_type = req.getParameter("qa_type").trim();			
			String q_context = req.getParameter("q_context").trim();			
			String a_context = req.getParameter("a_context").trim();
		
			QaVO qaVO = new QaVO();
			qaVO.setQa_type_no(qa_type);
			qaVO.setQ_context(q_context);
			qaVO.setA_context(a_context);
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("qaVO", qaVO); // �t����J�榡���~��empVO����,�]�s�Jreq
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back-end/qa/addQa.jsp");
				failureView.forward(req, res);
				return;
			}
		/***************************2.�}�l�s�W���***************************************/
			QaService qaSvc = new QaService();
			qaVO = qaSvc.addQa(qa_type,q_context ,a_context);
			/***************************3.�s�W����,�ǳ����(Send the Success view)***********/
			String url = "/back-end/qa/listAllQa.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);				
			/***************************��L�i�઺���~�B�z**********************************/
	//	} catch (Exception e) {
	//		errorMsgs.add(e.getMessage());
	//		RequestDispatcher failureView = req
	//				.getRequestDispatcher("/back-end/qa/addQa.jsp");
	//		failureView.forward(req, res);
	//	}
}

	}
}

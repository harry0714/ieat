package com.adm.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

import com.adm.model.*;
import com.adm.model.AdmService;
import com.adm.model.AdmVO;
import com.advertisement.model.AdvertisementService;
import com.email.model.MailService;

import net.sf.json.JSONObject;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)

public class AdmServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		
		if ("login".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			
			String account = req.getParameter("adm_user");
			String password = req.getParameter("adm_psd");
			
			
			AdmService admSvc = new AdmService();
			AdmVO admVO = admSvc.getUserAdm(account);
			if (admVO == null) {
				res.sendRedirect(req.getContextPath() + "/back-end/login.jsp");
				return;
			}
			if (!((account.equals(admVO.getAdm_user())) && (password.equals(admVO.getAdm_psd())))) {
	
				res.sendRedirect(req.getContextPath() + "/back-end/login.jsp");
				return;

			} else {
				HttpSession session = req.getSession();
				Map<String, String> user = new TreeMap<>();

				String adm_user = account;
				
				String adm_name = admVO.getAdm_name();
				String adm_level = admVO.getAdm_level();
				
				user.put("adm_user", adm_user);
				user.put("adm_name", adm_name);
				user.put("adm_level", adm_level);
				session.setAttribute("admuser", user);

				res.sendRedirect(req.getContextPath() + "/back-end/index.jsp"); // *�u�@3:
																				// (-->�p�L�ӷ�����:�h���ɦ�login_success.jsp)
			}

		}
		if ("admlogout".equals(action)) {
			
			
			HttpSession session = req.getSession();
			session.invalidate();
			String url = req.getContextPath() + "/back-end/login.jsp";
			res.sendRedirect(url);
		}
		

		if ("delete".equals(action)) { // �Ӧ�listAllEmp.jsp
			String requestURL = req.getParameter("requestURL");
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

				String adm_no = new String(req.getParameter("adm_no"));
				AdmService admSvc = new AdmService();
				admSvc.deleteAdm(adm_no);
				String url = requestURL;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

		}

		if ("getOne_For_Update".equals(action)) { // �Ӧ�listAllEmp.jsp���ШD
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				String adm_no = req.getParameter("adm_no");
				AdmService admSvc = new AdmService();
				AdmVO admVO = admSvc.getOneAdm(adm_no);
				
				req.setAttribute("admVO", admVO); 
				JSONObject json = new JSONObject();
				json.put("adm_no", admVO.getAdm_no());	
				json.put("adm_name", admVO.getAdm_name());	
				json.put("adm_phone", admVO.getAdm_phone());
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				json.put("adm_bd", df.format(admVO.getAdm_bd()));	
				json.put("adm_addr", admVO.getAdm_addr());	
				json.put("adm_user", admVO.getAdm_user());	
				json.put("adm_level", admVO.getAdm_level());	
				json.put("adm_email", admVO.getAdm_email());	
				json.put("adm_psd", admVO.getAdm_psd());	
				json.put("adm_sex", admVO.getAdm_sex());
				json.put("adm_photo", admVO.getAdm_photo());
				
				res.setContentType("text/plain");
				res.setCharacterEncoding("UTF-8");
				PrintWriter out = res.getWriter();
				out.write(json.toString());
				out.flush();
				out.close();

				/*************************** ��L�i�઺��~�B�z **********************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/adm/adm.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("update".equals(action)) { // �Ӧ�update_emp_input.jsp���ШD

			List<String> errorMsgs = new LinkedList<String>();
			
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
				/***************************
				 * 1.�����ШD�Ѽ� - ��J�榡����~�B�z
				 **********************/
				
				String adm_no = req.getParameter("adm_no").trim();
				String adm_name = req.getParameter("adm_name").trim();
				String adm_user = req.getParameter("adm_user").trim();
				String adm_psd = req.getParameter("adm_psd").trim();
				// String adm_sex = req.getParameter("adm_sex").trim();
				String adm_email = req.getParameter("adm_email").trim();
				String adm_phone = req.getParameter("adm_phone").trim();
				// String adm_level = req.getParameter("adm_level").trim();
				String adm_addr = req.getParameter("adm_addr").trim();

				java.sql.Date adm_bd = null;
					adm_bd = java.sql.Date.valueOf(req.getParameter("adm_bd").trim());
				String  adm_sex = new String(req.getParameter("adm_sex").trim());
				String adm_level = null;
				try {
					adm_level = new String(req.getParameter("adm_level").trim());
				
				} catch (Exception e) {

					errorMsgs.add("�v�������");
				}
				
				Part part = req.getPart("adm_photo");
				byte[] adm_photo = null;
				
				if(part.getSize()==0){
					AdmService admSvc = new AdmService();
					adm_photo = admSvc.getAdmImage(adm_no);				
				}else if((part.getSize()!=0)&&(!("image/jpeg".equals(part.getContentType()))&&!("image/png".equals(part.getContentType())))){
					errorMsgs.add("請選擇jpg或png圖檔");
				}else{
					InputStream in = part.getInputStream();
					adm_photo = new byte[in.available()];
					in.read(adm_photo);
					in.close();
				}
				
				
				

				AdmVO admVO = new AdmVO();
				admVO.setAdm_no(adm_no);
				admVO.setAdm_name(adm_name);
				admVO.setAdm_user(adm_user);
				admVO.setAdm_psd(adm_psd);
				admVO.setAdm_bd(adm_bd);
				admVO.setAdm_sex(adm_sex);
				admVO.setAdm_email(adm_email);
				admVO.setAdm_phone(adm_phone);
				admVO.setAdm_level(adm_level);
				admVO.setAdm_addr(adm_addr);
				


				if (!errorMsgs.isEmpty()) {
					req.setAttribute("admVO", admVO); // �t����J�榡��~��empVO����,�]�s�Jreq
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/adm/adm.jsp");
					failureView.forward(req, res);
					return; // �{�����_
				}

				AdmService admSvc = new AdmService();
				admVO = admSvc.updateAdm(adm_no, adm_name, adm_user, adm_psd, adm_sex, adm_bd, adm_email, adm_phone,
						adm_level, adm_addr, adm_photo);

				req.setAttribute("admVO", admVO); // ��Ʈwupdate���\��,���T����empVO����,�s�Jreq
				String url = "/back-end/adm/adm.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // �ק令�\��,���listOneEmp.jsp		
				successView.forward(req, res);

			} catch (Exception e) {
				errorMsgs.add("�ק��ƥ���:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/adm/adm.jsp");
				failureView.forward(req, res);
			}
		}

		if ("insert".equals(action)) { // �Ӧ�addEmp.jsp���ШD

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			
			try {
			
				String adm_phone = req.getParameter("adm_phone").trim();
				String adm_addr = req.getParameter("adm_addr").trim();
				Part part = req.getPart("adm_photo");
				InputStream is = part.getInputStream();

				byte[] adm_photo = new byte[is.available()];
				is.read(adm_photo);
				is.close();
				
				String adm_user = null;
				try {
					adm_user = new String(req.getParameter("adm_user").trim());

				} catch (Exception e) {

					errorMsgs.add("請填寫帳號");
				}
				
				String adm_name = null;
				try {
					adm_name = new String(req.getParameter("adm_name").trim());
					
				} catch (Exception e) {
					errorMsgs.add("名字請勿空白");
				}
				String adm_email = null;
				try {
					adm_email = new String(req.getParameter("adm_email").trim());
				} catch (Exception e) {

					errorMsgs.add("信箱請勿空白");
				}

				java.sql.Date adm_bd = null;
				try {
					adm_bd = java.sql.Date.valueOf(req.getParameter("adm_bd").trim());
				} catch (Exception e) {
					adm_bd = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("出生年月日未選");
				}

				String adm_sex = null;
				try {
					adm_sex = new String(req.getParameter("adm_sex").trim());
					

				} catch (Exception e) {

					errorMsgs.add("性別未選");
				}

				String adm_level = null;
				try {
					adm_level = new String(req.getParameter("adm_level").trim());
					

				} catch (Exception e) {

					errorMsgs.add("權限未選");
					
				}
				
				
				String adm_psd = String.valueOf((int) (Math.random() * 10000)).trim();
				
				String subject = "iEat 員工密碼";
				String messageText = "你的密碼如下:" + adm_psd;
				
				
				MailService mailService = new MailService();
				mailService.sendMail(adm_email, subject, messageText);
				AdmVO admVO = new AdmVO();
			
				
				admVO.setAdm_name(adm_name);
				admVO.setAdm_user(adm_user);
				
				admVO.setAdm_bd(adm_bd);
				admVO.setAdm_sex(adm_sex);
				admVO.setAdm_email(adm_email);
				admVO.setAdm_phone(adm_phone);
				admVO.setAdm_level(adm_level);
				admVO.setAdm_addr(adm_addr);
				admVO.setAdm_photo(adm_photo);
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("admVO", admVO); 
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/adm/adm.jsp");
					failureView.forward(req, res);
					return;
				}

				AdmService admSvc = new AdmService();
				admVO = admSvc.addAdm(adm_name, adm_user, adm_psd, adm_bd, adm_sex, adm_email, adm_phone, adm_level,
						adm_addr, adm_photo);
	
				String url = "/back-end/adm/adm.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
				successView.forward(req, res);
				
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/adm/adm.jsp");
				failureView.forward(req, res);
			}
		}

	}
}

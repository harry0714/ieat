package com.member.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.bookmark_meal.model.Bookmark_MealService;
import com.bookmark_store.model.Bookmark_StoreService;
import com.email.model.MailService;
import com.member.model.MemberService;
import com.member.model.MemberVO;


@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 25 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)
// 當數據量大於fileSizeThreshold值時，內容將被寫入磁碟
// 上傳過程中無論是單個文件超過maxFileSize值，或者上傳的總量大於maxRequestSize 值都會拋出IllegalStateException
// 異常
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");

		String action = req.getParameter("action");

		if ("memberlogin".equals(action)) {
			HttpSession session = req.getSession();
			session.removeAttribute("memberVO");
			String mem_acct = req.getParameter("mem_acct");
			String mem_pwd = req.getParameter("mem_pwd");
			List<String> errorMessage = new LinkedList<String>();
			req.setAttribute("errorMessage", errorMessage);
			if (mem_acct == null || mem_pwd == null || (mem_acct.trim()).length() == 0
					|| (mem_pwd.trim()).length() == 0) {
				// out.println("帳號或密碼輸入錯誤");
				errorMessage.add("請輸入帳號和密碼");
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/login.jsp");
				failureView.forward(req, res);
				return;
			}
			MemberService memSve = new MemberService();
			MemberVO memberVO = new MemberVO();
			memberVO = memSve.getOneMemberByAcctPwd(mem_acct, mem_pwd);
			if (memberVO == null) {
				// out.println("帳號或密碼輸入錯誤2");
				errorMessage.add("帳號或密碼輸入錯誤");
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/login.jsp");
				failureView.forward(req, res);
				return;
			}
			if ((!memberVO.getMem_acct().equals(mem_acct)) || (!memberVO.getMem_pwd().equals(mem_pwd))) {
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/login.jsp");
				failureView.forward(req, res);
				return;
			}
			Date today = new Date(new java.util.Date().getTime());
			if (memberVO.getMem_validate().after(today)) {
				errorMessage.add("此帳號已被停權，請在" + memberVO.getMem_validate() + "之後登入");
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/login.jsp");
				failureView.forward(req, res);
				return;
			}
			
			Map<String, String> user = new TreeMap<>();

			String account = mem_acct;
			String usersex = memberVO.getMem_sex().equals("1") ? "先生" : "小姐";
			String username = memberVO.getMem_name().charAt(0) + usersex;
			String email = memberVO.getMem_email();
			user.put("no", memberVO.getMem_no());
			user.put("account", account);
			user.put("name", username);
			user.put("email", email);
			session.setAttribute("user", user);

			// 最愛餐點放入Session
			Bookmark_MealService bmmSvc = new Bookmark_MealService();
			List<String> bookmark_MealNo = bmmSvc.getMealNoByMemNo(memberVO.getMem_no());
			session.setAttribute("bookmark_MealNo", bookmark_MealNo);

			// 最愛餐廳放入Session
			Bookmark_StoreService bmsSvc = new Bookmark_StoreService();
			List<String> bookmark_StoreNo = bmsSvc.getStoreNoByMemNo(memberVO.getMem_no());
			session.setAttribute("bookmark_StoreNo", bookmark_StoreNo);

			String location = (String) session.getAttribute("location");
			// 針對食記跳轉 
			String art_no_XX = (String)session.getAttribute("art_no_XX");
			if (art_no_XX!=null) {
				location = req.getContextPath()+"/article/article.do?action=getFor_Display&art_no="+art_no_XX;
			}
			
			if (location != null) {
				session.removeAttribute("location");
				res.sendRedirect(location);
				return;
			}

			res.sendRedirect(req.getContextPath() + "/front-end/index.jsp");

		}

		/* 會員登出 */
		if ("memberlogout".equals(action)) {

			HttpSession session = req.getSession();
			session.invalidate();
			String url = req.getContextPath()+"/front-end/index.jsp"; 
			res.sendRedirect(url);
		}

		/* 會員註冊 */
		if ("memberregister".equals(action)) {
			String mem_name = req.getParameter("mem_name");
			String mem_acct = req.getParameter("mem_acct");
			String mem_pwd = req.getParameter("mem_pwd");
			String mem_pwd_check = req.getParameter("mem_pwd_check");
			String mem_sex = req.getParameter("mem_sex");
			// String mem_bd = req.getParameter("mem_bd");
			String mem_email = req.getParameter("mem_email");
			String mem_phone = req.getParameter("mem_phone");
			String mem_addr1 = req.getParameter("mem_addr1");
			String mem_addr2 = req.getParameter("mem_addr2");
			String mem_addr3 = req.getParameter("mem_addr3");
			String mem_zip = req.getParameter("mem_zip");

			MemberVO memberVO = new MemberVO();

			Map<String, String> errorMessage = new HashMap<>();
			req.setAttribute("errorMessage", errorMessage);
			req.setAttribute("memberVO", memberVO);
			MemberService memSvc = new MemberService();
			try {
				// 姓名驗證
				String nameRegEx = "[\u4e00-\u9fa5]{1,5}"; // 給名字用的正則表示，只能輸入中文
				if (mem_name.isEmpty()) {
					errorMessage.put("mem_name", "未輸入姓名");
				} else if (!mem_name.matches(nameRegEx)) {
					errorMessage.put("mem_name", "輸入格式錯誤，請輸入中文");
				} else {
					memberVO.setMem_name(mem_name);

				}

				// 帳號驗證
				String acctpwdRegEx = "[\\w]{6,15}";// 給帳號和密碼用的正則表示，只能輸入英文或數字
				if (mem_acct.isEmpty()) {
					errorMessage.put("mem_acct", "未輸入帳號");

				} else if (mem_acct.matches(acctpwdRegEx)) {

					Map<String, String[]> map = new TreeMap<>();
					map.put("mem_acct", new String[] { mem_acct });
					List<MemberVO> list = memSvc.getAll(map);
					if (list.size() == 0) {
						memberVO.setMem_acct(mem_acct);

					} else {
						errorMessage.put("mem_acct", "帳號有重複");
					}

				} else {
					errorMessage.put("mem_acct", "輸入格式錯誤");

				}

				// 密碼驗證
				if (mem_pwd.isEmpty()) {
					errorMessage.put("mem_pwd", "未輸入密碼");
				} else if (mem_pwd.matches(acctpwdRegEx)) {
					memberVO.setMem_pwd(mem_pwd);
					if (mem_pwd_check.isEmpty()) {
						errorMessage.put("mem_pwd_check", "請再次輸入密碼");
					} else if (mem_pwd.equals(mem_pwd_check)) {
						// memberVO.setMem_pwd(mem_pwd);
					} else {
						errorMessage.put("mem_pwd_check", "密碼和確認密碼不一致");
					}

				} else {
					errorMessage.put("mem_pwd", "輸入格式錯誤");

				}

				// 性別驗證
				if (mem_sex == null) {
					errorMessage.put("mem_sex", "未選擇性別");

				} else {
					memberVO.setMem_sex(mem_sex);

				}

				// 生日驗證
				System.out.println(req.getParameter("mem_bd"));
				Date mem_bd = null;
				try {
					mem_bd = Date.valueOf(req.getParameter("mem_bd").trim());
					Date today = new Date(new java.util.Date().getTime());
					if (mem_bd.after(today)) {
						errorMessage.put("mem_bd", "請選擇正確的生日");
					} else {
						memberVO.setMem_bd(mem_bd);

					}
				} catch (IllegalArgumentException e) {
					errorMessage.put("mem_bd", "請輸入日期");
				}

				// email驗證
				String emailRegEx = "[\\w][\\w_.-]+@[\\w.]+";
				if (mem_email.isEmpty()) {
					errorMessage.put("mem_email", "未輸入email");
				} else if (!mem_email.matches(emailRegEx)) {
					errorMessage.put("mem_email", "email格式錯誤");
				} else {
					Map<String, String[]> map = new TreeMap<>();
					map.put("mem_email", new String[] { mem_email });
					List<MemberVO> list = memSvc.getAll(map);
					if (list.size() == 0) {
						memberVO.setMem_email(mem_email);

					} else {
						errorMessage.put("mem_email", "email有重複");
					}
				}

				// 電話驗證
				String phoneRegEx = "09[0-9]{8}";
				if (mem_phone.isEmpty()) {
					errorMessage.put("mem_phone", "未輸入手機號碼");
				} else if (!mem_phone.matches(phoneRegEx)) {
					errorMessage.put("mem_phone", "手機格式錯誤");
				} else {
					Map<String, String[]> map = new TreeMap<>();
					map.put("mem_phone", new String[] { mem_phone });
					List<MemberVO> list = memSvc.getAll(map);
					if (list.size() == 0) {
						memberVO.setMem_phone(mem_phone);

					} else {
						errorMessage.put("mem_phone", "電話有重複");
					}
				}

				// 地址
				String mem_addr = null;
				memberVO.setMem_zip(mem_zip);
				if (mem_addr1.isEmpty()) {
					mem_addr = mem_addr3;
				} else {
					mem_addr = mem_addr1 + "-" + mem_addr2 + "-" + mem_addr3;
				}
				memberVO.setMem_addr(mem_addr);
				req.setAttribute("mem_addr1", mem_addr1);
				req.setAttribute("mem_addr2", mem_addr2);
				req.setAttribute("mem_addr3", mem_addr3);

				// 照片
				byte[] mem_photo = null;
				Part part = req.getPart("mem_photo");
				if (part.getContentType() != null && (part.getSize() != (long) 0)) {
					if (part.getContentType().equals("image/jpeg") || part.getContentType().equals("image/png")) {
						InputStream in = part.getInputStream();
						mem_photo = new byte[in.available()];
						in.read(mem_photo);
						in.close();
						memberVO.setMem_photo(mem_photo);
					} else {
						errorMessage.put("mem_photo", "請選圖片格式的檔案 " + part.getContentType() + part.getSize());
					}
				}

				if (!errorMessage.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/memberregister.jsp");
					failureView.forward(req, res);
					return;
				}
				String codes = String.valueOf((int) (Math.random() * 10000)).trim();
				String subject = "iEat 驗證碼";
				String messageText = "你的驗證碼如下:" + codes + "  請於信箱驗證頁面下的驗證碼欄位輸入";
				MailService mailService = new MailService();
				mailService.sendMail(mem_email, subject, messageText);

				HttpSession session = req.getSession();
				session.setAttribute("memberVO", memberVO);
				session.setAttribute("codes", codes);
				System.out.println("codes==========="+codes);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/authenticate.jsp");
				failureView.forward(req, res);

				// memSvc.addMember(mem_name, mem_acct, mem_pwd, mem_sex,
				// mem_bd, mem_email, mem_phone, mem_zip, mem_addr,
				// mem_photo);
			} catch (Exception e) {
				errorMessage.put("elseError", "有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/memberregister.jsp");
				failureView.forward(req, res);
			}
		}

		// 會員註冊輸入驗證碼
		if ("authenticate".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try {
				HttpSession session = req.getSession();
				String codes = (String) session.getAttribute("codes");
				if (!codes.equals(req.getParameter("codes"))) {
					errorMessage.put("codes", "輸入錯誤請重新輸入");					
					// return;
				}
				
				if(!errorMessage.isEmpty()){
					req.setAttribute("errorMessage", errorMessage);
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/authenticate.jsp");
					failureView.forward(req, res);
					return; 
				}
				MemberVO memberVO = (MemberVO) session.getAttribute("memberVO");
				MemberService memberSvc = new MemberService();
				memberSvc.addMember(memberVO.getMem_name(), memberVO.getMem_acct(), memberVO.getMem_pwd(),
						memberVO.getMem_sex(), memberVO.getMem_bd(), memberVO.getMem_email(), memberVO.getMem_phone(),
						memberVO.getMem_zip(), memberVO.getMem_addr(), memberVO.getMem_photo());
				session.removeAttribute("memberVO");
				session.removeAttribute("codes");
				res.sendRedirect(req.getContextPath() + "/front-end/member/completed.jsp");
			} catch (Exception e) {
				errorMessage.put("elseError", "有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/authenticate.jsp");
				failureView.forward(req, res);
			}
		}

		// 會員要進入修改資料頁面
		if ("member_for_update".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try {
				HttpSession session = req.getSession();
				Map<String, String> user = (Map<String, String>) session.getAttribute("user");
				MemberService memSvc = new MemberService();
				MemberVO memberVO = memSvc.getOneMember(user.get("no"));
				req.setAttribute("memberVO", memberVO);

				if (memberVO.getMem_addr() != null && (!memberVO.getMem_addr().isEmpty())) {
					String[] addr = memberVO.getMem_addr().split("-");
					if (addr.length == 1) {
						req.setAttribute("mem_addr3", addr[0]);
					} else if (addr.length == 2) {
						req.setAttribute("mem_addr1", addr[0]);
						req.setAttribute("mem_addr2", addr[1]);
					} else {
						req.setAttribute("mem_addr1", addr[0]);
						req.setAttribute("mem_addr2", addr[1]);
						req.setAttribute("mem_addr3", addr[2]);
					}
				}
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/memberupdate.jsp");
				failureView.forward(req, res);

			} catch (Exception e) {
				errorMessage.put("elseError", "修改資料取出時失敗:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/personal.jsp");
				failureView.forward(req, res);
			}
		}

		// 會員修改資料
		if ("memberupdate".equals(action)) {

			Map<String, String> errorMessage = new HashMap<>();
			req.setAttribute("errorMessage", errorMessage);

			try {

				HttpSession session = req.getSession();
				Map<String, String> user = (Map<String, String>) session.getAttribute("user");
				MemberService memSvc = new MemberService();
				MemberVO memberVO = memSvc.getOneMember(user.get("no"));
				req.setAttribute("memberVO", memberVO);

				String mem_name = req.getParameter("mem_name");
				String mem_sex = req.getParameter("mem_sex");
				String mem_bd = req.getParameter("mem_bd");
				String mem_email = req.getParameter("mem_email");
				String mem_phone = req.getParameter("mem_phone");
				String mem_addr1 = req.getParameter("mem_addr1");
				String mem_addr2 = req.getParameter("mem_addr2");
				String mem_addr3 = req.getParameter("mem_addr3");
				String mem_zip = req.getParameter("mem_zip");

				// 姓名驗證
				if (!mem_name.equals(memberVO.getMem_name())) {
					String nameRegEx = "[\u4e00-\u9fa5]{1,5}"; // 給名字用的正則表示，只能輸入中文
					if (mem_name.isEmpty()) {
						errorMessage.put("mem_name", "未輸入姓名");
					} else if (!mem_name.matches(nameRegEx)) {
						errorMessage.put("mem_name", "輸入格式錯誤，請輸入中文");
					} else {
						memberVO.setMem_name(mem_name);

					}
				}

				// 性別驗證
				if (mem_sex == null) {
					errorMessage.put("mem_sex", "未選擇性別");

				} else {
					memberVO.setMem_sex(mem_sex);

				}

				// 電話驗證
				if (!mem_phone.equals(memberVO.getMem_phone())) {
					String phoneRegEx = "09[0-9]{8}";
					if (mem_phone.isEmpty()) {
						errorMessage.put("mem_phone", "未輸入手機號碼");
					} else if (!mem_phone.matches(phoneRegEx)) {
						errorMessage.put("mem_phone", "手機格式錯誤");
					} else {
						Map<String, String[]> mapphone = new TreeMap<>();
						mapphone.put("mem_phone", new String[] { mem_phone });
						List<MemberVO> list1 = memSvc.getAll(mapphone);
						if (list1.size() == 0) {
							memberVO.setMem_phone(mem_phone);

						} else {
							errorMessage.put("mem_phone", "電話有重複");
						}
					}
				}

				// 地址
				String mem_addr = null;
				memberVO.setMem_zip(mem_zip);
				if (mem_addr1.isEmpty()) {
					mem_addr = mem_addr3;
				} else {
					mem_addr = mem_addr1 + "-" + mem_addr2 + "-" + mem_addr3;
				}
				memberVO.setMem_addr(mem_addr);
				req.setAttribute("mem_addr1", mem_addr1);
				req.setAttribute("mem_addr2", mem_addr2);
				req.setAttribute("mem_addr3", mem_addr3);

				// 照片
				byte[] mem_photo = null;
				Part part = req.getPart("mem_photo");
				if (part.getContentType() != null && (part.getSize() != (long) 0)) {
					if (part.getContentType().equals("image/jpeg") || part.getContentType().equals("image/png")) {
						InputStream in = part.getInputStream();
						mem_photo = new byte[in.available()];
						in.read(mem_photo);
						in.close();
						memberVO.setMem_photo(mem_photo);
					} else {
						errorMessage.put("mem_photo", "請選圖片格式的檔案 " + part.getContentType() + part.getSize());
					}
				}

				if (!errorMessage.isEmpty()) {
					System.out.println("有錯");
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/memberupdate.jsp");
					failureView.forward(req, res);
					return;
				}

				memSvc.updateMemberForMember(memberVO.getMem_no(), memberVO.getMem_name(), memberVO.getMem_acct(),
						memberVO.getMem_pwd(), memberVO.getMem_sex(), memberVO.getMem_bd(), memberVO.getMem_email(),
						memberVO.getMem_phone(), memberVO.getMem_zip(), memberVO.getMem_addr(), memberVO.getMem_photo(),
						memberVO.getMem_bd());

				String usersex = memberVO.getMem_sex().equals("1") ? "先生" : "小姐";
				String username = memberVO.getMem_name().charAt(0) + usersex;
				user.put("username", username);
				session.setAttribute("user", user);
				req.setAttribute("success", "success");
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/memberupdate.jsp");
				failureView.forward(req, res);
			} catch (Exception e) {
				System.out.println("有錯" + e.getMessage());
				errorMessage.put("elseError", "修改資料取出時失敗:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/memberupdate.jsp");
				failureView.forward(req, res);
			}

		}

		// 密碼修改
		if ("passwordupdate".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try {
				HttpSession session = req.getSession();
				Map<String, String> user = (Map<String, String>) session.getAttribute("user");

				MemberService memSvc = new MemberService();
				MemberVO memberVO = memSvc.getOneMember(user.get("no"));
				String mem_pwd = req.getParameter("mem_pwd");
				if (!mem_pwd.equals(memberVO.getMem_pwd())) {
					errorMessage.put("mem_pwd", "密碼不正確");
				}
				String pwdRegEx = "[\\w]{6,15}";
				String mem_pwd_new = req.getParameter("mem_pwd_new");
				String mem_pwd_new_check = req.getParameter("mem_pwd_new_check");
				if (mem_pwd_new.isEmpty()) {
					errorMessage.put("mem_pwd_new", "請輸入新密碼");
				} else if (!mem_pwd_new.matches(pwdRegEx)) {
					errorMessage.put("mem_pwd_new", "密碼格式錯誤");
				} else if (!mem_pwd_new.equals(mem_pwd_new_check)) {
					errorMessage.put("mem_pwd_new_check", "輸入不一致");
				}

				if (!errorMessage.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/passwordupdate.jsp");
					failureView.forward(req, res);
					return;
				}

				memberVO.setMem_pwd(mem_pwd_new);
				memSvc.updateMemberForMember(memberVO.getMem_no(), memberVO.getMem_name(), memberVO.getMem_acct(),
						memberVO.getMem_pwd(), memberVO.getMem_sex(), memberVO.getMem_bd(), memberVO.getMem_email(),
						memberVO.getMem_phone(), memberVO.getMem_zip(), memberVO.getMem_addr(), memberVO.getMem_photo(),
						memberVO.getMem_validate());
				req.setAttribute("success", "success");
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/passwordupdate.jsp");
				failureView.forward(req, res);

			} catch (Exception e) {
				errorMessage.put("elseError", "請修正以下錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/passwordupdate.jsp");
				failureView.forward(req, res);
			}
		}

		// email修改
		if ("emailupdate".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try {
				String mem_email = req.getParameter("mem_email");
				String mem_email_new = req.getParameter("mem_email_new");
				if (mem_email_new.isEmpty()) {
					errorMessage.put("mem_email_new", "沒有輸入新的eamil");
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/emailupdate.jsp");
					failureView.forward(req, res);
					return;
				}
				if (mem_email.equals(mem_email_new)) {
					errorMessage.put("mem_email_new", "此email跟你的舊email一樣");
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/emailupdate.jsp");
					failureView.forward(req, res);
					return;
				}
				Map<String, String[]> emailMap = new TreeMap<>();
				emailMap.put("mem_email", new String[] { mem_email_new });
				MemberService memberSvc = new MemberService();
				List<MemberVO> list = memberSvc.getAll(emailMap);
				if (list.size() != 0) {
					errorMessage.put("mem_email_new", "email已有人使用");
					RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/emailupdate.jsp");
					failureView.forward(req, res);
					return;
				}

				req.setAttribute("mem_email_new", mem_email_new);

				String codes = String.valueOf((int) (Math.random() * 10000)).trim();
				String subject = "iEat 驗證碼";
				String messageText = "你的電子信箱變更驗證碼如下:" + codes + "  請於驗證頁面下的驗證碼欄位輸入";
				MailService mailService = new MailService();
				mailService.sendMail(mem_email_new, subject, messageText);
				HttpSession session = req.getSession();
				session.setAttribute("codes", codes);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/updateauthenticate.jsp");
				failureView.forward(req, res);
				// memberSvc.updateMemberForMember(mem_no, mem_name, mem_acct,
				// mem_pwd, mem_sex, mem_bd, mem_email_new, mem_phone, mem_zip,
				// mem_addr, mem_photo, mem_validate);

			} catch (Exception e) {
				errorMessage.put("elseError", "有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/emailupdate.jsp");
				failureView.forward(req, res);
			}
		}

		// email修改驗證碼確認
		if ("updateauthenticate".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try {

				HttpSession session = req.getSession();
				String codes = (String) session.getAttribute("codes");
				if (!codes.equals(req.getParameter("codes"))) {
					errorMessage.put("codes", "輸入錯誤請重新輸入");
					return;
				}

				String mem_email_new = req.getParameter("mem_email_new");
				Map<String, String> user = (Map<String, String>) session.getAttribute("user");
				MemberService memberSvc = new MemberService();
				MemberVO memberVO = memberSvc.getOneMember(user.get("no"));
				memberVO.setMem_email(mem_email_new);

				/* 會員資料修改 */
				memberSvc.updateMemberForMember(memberVO.getMem_no(), memberVO.getMem_name(), memberVO.getMem_acct(),
						memberVO.getMem_pwd(), memberVO.getMem_sex(), memberVO.getMem_bd(), memberVO.getMem_email(),
						memberVO.getMem_phone(), memberVO.getMem_zip(), memberVO.getMem_addr(), memberVO.getMem_photo(),
						memberVO.getMem_validate());

				user.put("email", memberVO.getMem_email());
				session.setAttribute("user", user);
				session.removeAttribute("codes");
				System.out.println(user.get("email"));
				req.setAttribute("success", "success");
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/emailupdate.jsp");
				failureView.forward(req, res);

			} catch (Exception e) {
				errorMessage.put("elseError", "有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/updateauthenticate.jsp");
				failureView.forward(req, res);
			}
		}
		
		/******************************* 忘記密碼 **********************************/ 
		if("forgetPsw".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>(); 
			req.setAttribute("errorMsgs", errorMsgs);
			
			String mem_email = req.getParameter("mem_email").trim();
			if(mem_email.length()==0) {
				errorMsgs.add("請輸入Email"); 
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/forgetPsw.jsp");
				failureView.forward(req, res);
				return;
			}
			Map<String,String[]> map = new TreeMap<String,String[]>();
			map.put("mem_email",new String[] {mem_email});
			MemberService memberSvc = new MemberService(); 
			List<MemberVO> memberList = memberSvc.getAll(map);
			
						
			if(memberList.size()==0) {
				errorMsgs.add("信箱輸入錯誤 請重新輸入");
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/forgetPsw.jsp");
				failureView.forward(req, res);
				return;			
			}
			MemberVO memberVO = (MemberVO)memberList.get(0);
			String mem_name = memberVO.getMem_name();
			String mem_acct = memberVO.getMem_acct(); 			
			
			// 將新密碼以email寄出
			try {	
			String codes = String.valueOf((int) (Math.random() * 1000000)).trim();
			memberVO.setMem_pwd(codes);
			memberSvc.updateMemberForMember(memberVO.getMem_no(), memberVO.getMem_name(),memberVO.getMem_acct(), memberVO.getMem_pwd(), memberVO.getMem_sex(), memberVO.getMem_bd(), memberVO.getMem_email(), memberVO.getMem_phone(), memberVO.getMem_zip(), memberVO.getMem_addr(), memberVO.getMem_photo(), memberVO.getMem_validate());
			String subject = "iEat 密碼重設通知"; 
			String messageText = mem_name + "您好\n" + "您的帳號為"  + mem_acct + "\n您的新密碼如下：" + codes + "\n請以此組新密碼進行登入";
			MailService mailService = new MailService(); 
			mailService.sendMail(mem_email, subject, messageText);
			
			req.setAttribute("resetPsw", "resetPsw");
			RequestDispatcher successView = req.getRequestDispatcher("/front-end/login.jsp");
			successView.forward(req, res);
			
			} catch(Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/member/forgetPsw.jsp");
				failureView.forward(req, res);
				return;	
			}			
		}
		
		// 檢查帳號是否重複ajax
		if ("checkacct".equals(action)) {
			String mem_acct = (String) req.getParameter("mem_acct");
			String errorMessage = null;
			String check = "false";
			System.out.println(mem_acct + "--------------");

			String acctpwdRegEx = "[\\w]{6,15}";// 給帳號和密碼用的正則表示，只能輸入英文或數字
			if (mem_acct.isEmpty()) {
				errorMessage = "未輸入帳號";

			} else if (mem_acct.matches(acctpwdRegEx)) {

				Map<String, String[]> map = new TreeMap<>();
				map.put("mem_acct", new String[] { mem_acct });
				MemberService memSvc = new MemberService();
				List<MemberVO> list = memSvc.getAll(map);
				if (list.size() == 0) {
					check = "true";
				} else {
					errorMessage = "帳號有重複";
				}

			} else {
				errorMessage = "輸入格式錯誤";

			}

			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.write(check);
			out.flush();
			out.close();
			return;
		}

		// 檢查email是否重複ajax
		if ("checkemail".equals(action)) {
			String mem_email = (String) req.getParameter("mem_email");
			String errorMessage = null;
			String check = "false";

			String emailRegEx = "[\\w][\\w_.-]+@[\\w.]+";
			if (mem_email.isEmpty()) {
				errorMessage = "未輸入email";
			} else if (!mem_email.matches(emailRegEx)) {
				errorMessage = "email格式錯誤";
			} else {
				Map<String, String[]> map = new TreeMap<>();
				map.put("mem_email", new String[] { mem_email });
				MemberService memSvc = new MemberService();
				List<MemberVO> list = memSvc.getAll(map);
				if (list.size() == 0) {
					check = "true";

				} else {
					errorMessage = "email有重複";
				}
			}

			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.write(check);
			out.flush();
			out.close();
			return;
		}

		// 檢查電話是否重複ajax
		if ("checkphone".equals(action)) {
			String mem_phone = (String) req.getParameter("mem_phone");
			String errorMessage = null;
			String check = "false";

			String phoneRegEx = "09[0-9]{8}";
			if (mem_phone.isEmpty()) {
				errorMessage = "未輸入手機號碼";
			} else if (!mem_phone.matches(phoneRegEx)) {
				errorMessage = "手機格式錯誤";
			} else {
				Map<String, String[]> map = new TreeMap<>();
				map.put("mem_phone", new String[] { mem_phone });
				MemberService memSvc = new MemberService();
				List<MemberVO> list = memSvc.getAll(map);
				if (list.size() == 0) {
					check = "true";

				} else {
					errorMessage = "電話有重複";
				}
			}

			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.write(check);
			out.flush();
			out.close();
			return;
		}
		/******************** 來自後端  會員停權 **********************************/ 
		if("Disable".equals(action)) {
			try{
				/***************************1.�����ШD�Ѽ�****************************************/
				String mem_no = req.getParameter("mem_no"); 
				/***************************2.�}�l�d�߸�ơA�ק�o�� ���ݭn��~����****************************************/
				MemberService memSvc = new MemberService();
				MemberVO member = memSvc.getOneMember(mem_no);
				long mem_validateStop = member.getMem_validate().getTime()+ 30*24*60*60*1000L;
				java.sql.Date mem_validate = new java.sql.Date(mem_validateStop);
				
				String to=member.getMem_email();
				String subject="IEAT客服中心";
				String messageText="您好~"+member.getMem_name()+"!您的帳號已被停權，如有任何疑問，請致電至本公司詢問";
				
				MailService mailService = new MailService();
				mailService.sendMail(to, subject, messageText);
				MemberVO memberVO=memSvc.updateDate(mem_no, mem_validate);
				/***************************3.�d�ߧ���,�ǳ����(Send the Success view)************/
				req.setAttribute("memberVO", memberVO);
				String url = "/back-end/member/member.jsp"; 
				RequestDispatcher successView = req.getRequestDispatcher(url); //��浹update_store_input 
				successView.forward(req, res);
				/***************************��L�i�઺��~�B�z**********************************/
			} catch(Exception e) {
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/store/reviewed.jsp"); 
				failureView.forward(req,res); 
			}
		}
		
		/*************************** 來自後端的方法  停權會員重新啟用 ******************************/ 
		if("Enable".equals(action)) {
			try{
					/***************************1.�����ШD�Ѽ�****************************************/
					String mem_no = req.getParameter("mem_no"); 
					/***************************2.�}�l�d�߸�ơA�ק�o�� ���ݭn��~����****************************************/
					MemberService memSvc = new MemberService();
					MemberVO member = memSvc.getOneMember(mem_no);
					java.sql.Date mem_validate = new java.sql.Date(System.currentTimeMillis());
					String to=member.getMem_email();
					String subject="IEAT客服中心";
					String messageText="您好~"+member.getMem_name()+"!您的帳號已開啟，歡迎再次光臨本網站";
					MailService mailService = new MailService();
					mailService.sendMail(to, subject, messageText);
					MemberVO memberVO=memSvc.updateDate(mem_no, mem_validate);
					/***************************3.�d�ߧ���,�ǳ����(Send the Success view)************/
					req.setAttribute("memberVO", memberVO);
					String url = "/back-end/member/member.jsp"; 
					RequestDispatcher successView = req.getRequestDispatcher(url); //��浹update_store_input 
					successView.forward(req, res);
					/***************************��L�i�઺��~�B�z**********************************/
			} catch(Exception e) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/store/reviewed.jsp"); 
					failureView.forward(req,res); 
				}
			}
	}
}

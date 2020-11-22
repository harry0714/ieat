package com.member.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookmark_meal.model.BMAppService;
import com.bookmark_store.model.BSAppService;
import com.google.android.gcm.server.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.member.model.MemberAppService;
import com.member.model.MemberVO;
import com.store.controller.ImageUtil;
import com.store_comment.model.StoreCommentAppService;

@WebServlet("/MemAppServlet.do")
public class MemAppServlet extends HttpServlet {

	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		BufferedReader br = req.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		MemberAppService memberSvc = new MemberAppService();
		String action = jsonObject.get("action").getAsString();
		System.out.println("action: " + action);

		if ("checkMember".equals(action)) {
			String mem_acct = jsonObject.get("mem_acct").getAsString();
			String mem_pwd = jsonObject.get("mem_pwd").getAsString();

			String mem_no = memberSvc.fingByAcctPwd(mem_acct, mem_pwd);
			if (mem_no != null && !(mem_no.length() < 10)) {
				String mem_key = jsonObject.get("mem_key").getAsString();
				memberSvc.updateMemberKey(mem_no, mem_key);
			}
			writeText(res, mem_no);

		} else if ("getMemImage".equals(action)) {
			OutputStream os = res.getOutputStream();
			String mem_no = jsonObject.get("mem_no").getAsString();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = memberSvc.getMemImage(mem_no);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				res.setContentType("image/jpeg");
				res.setContentLength(image.length);
				os.write(image);
				os.close();
			}
		} else if ("getMemberVO".equals(action)) {
			String mem_no = jsonObject.get("mem_no").getAsString();
			MemberVO memberVO = memberSvc.findMemberByNo(mem_no);

			writeText(res, gson.toJson(memberVO));
		} else if ("memberUpdate".equals(action)) {
			String mem_no = jsonObject.get("mem_no").getAsString();
			String result = "fail";

			try {
				MemberVO memberVO = memberSvc.getOneMember(mem_no);
				byte[] image = memberSvc.getMemImage(mem_no);
				String mem_name = jsonObject.get("mem_name").getAsString();
				String mem_email = jsonObject.get("mem_email").getAsString();
				String mem_phone = jsonObject.get("mem_phone").getAsString();

				memberVO.setMem_name(mem_name);
				memberVO.setMem_email(mem_email);
				memberVO.setMem_phone(mem_phone);

				String mem_addr = memberVO.getMem_addr();
				String mem_acct = memberVO.getMem_acct();
				String mem_pwd = memberVO.getMem_pwd();
				Date mem_bd = memberVO.getMem_bd();
				String mem_zip = memberVO.getMem_zip();
				String mem_sex = memberVO.getMem_sex();

				memberSvc.updateMemberForMember(mem_no, mem_name, mem_acct, mem_pwd, mem_sex, mem_bd, mem_email,
						mem_phone, mem_zip, mem_addr, image, memberVO.getMem_validate());
				result = "success";
				System.out.println(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			writeText(res, result);

		} else if ("getPref".equals(action)) {
			String mem_no = jsonObject.get("mem_no").getAsString();

			Map<String, List<String>> memProfile = new HashMap<String, List<String>>();
			List<String> memlist = new ArrayList<String>();
			memlist.add(mem_no);
			memProfile.put("mem_no", memlist);
			if (mem_no != null) {
				BMAppService bookmark_mealSvc = new BMAppService();
				List<String> meallist = bookmark_mealSvc.getMealNoByMemNo(mem_no);
				BSAppService bookmark_storeSvc = new BSAppService();
				List<String> storelist = bookmark_storeSvc.getStoreNoByMemNo(mem_no);
				memProfile.put("favorMeal", meallist);
				memProfile.put("favorStore", storelist);
			}

			writeText(res, gson.toJson(memProfile));

		} else if (action.equals("memberIdExit")) {
			String mem_acct = jsonObject.get("mem_acct").getAsString();
			int count = memberSvc.checkMemberByAcct(mem_acct);

			writeText(res, String.valueOf(count));
		} else if (action.equals("createMember")) {
			String mem_no = null;
			
			String userId = jsonObject.get("userId").getAsString();
			String userPwd = jsonObject.get("userPwd").getAsString();
			String userName = jsonObject.get("userName").getAsString();
			String userBd = jsonObject.get("userBd").getAsString();
			String mem_key = jsonObject.get("mem_key").getAsString();

			System.out.print(userId);

			Date mem_Bd = Date.valueOf(userBd);

			String userEmail = jsonObject.get("userEmail").getAsString();
			String userPhone = jsonObject.get("userPhone").getAsString();
			// String userZip = jsonObject.get("userZip").getAsString();
			// String userAddr = jsonObject.get("userAddr").getAsString();
			String sex = jsonObject.get("sex").getAsString();

//			try {
				mem_no = memberSvc.addMember(userName, userId, userPwd, sex, mem_Bd, userEmail, userPhone, null, null,
						null);
				memberSvc.updateMemberKey(mem_no, mem_key);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			writeText(res, mem_no);

		} else if ("uploadKey".equals(action)) {
			String result = "fail";
			String mem_no = jsonObject.get("mem_no").getAsString();
			String mem_key = jsonObject.get("mem_key").getAsString();
			try {
				memberSvc.updateMemberKey(mem_no, mem_key);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			writeText(res, result);
		} else if ("updatePassword".equals(action)) {
			String result = "fail";
			String mem_acct = jsonObject.get("mem_acct").getAsString();
			String mem_pwd = jsonObject.get("mem_pwd").getAsString();
			String new_pwd = jsonObject.get("new_pwd").getAsString();

			String mem_no = memberSvc.fingByAcctPwd(mem_acct, mem_pwd);
			if (mem_no != null && !(mem_no.length() < 10)) {
				try {
					memberSvc.updatePwd(mem_no, new_pwd);
					result = "success";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			writeText(res, result);
		}
	}

	private void writeText(HttpServletResponse res, String outText) throws IOException {
		res.setContentType(CONTENT_TYPE);
		PrintWriter out = res.getWriter();
		out.print(outText);
	}
}

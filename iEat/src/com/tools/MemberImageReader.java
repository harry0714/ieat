package com.tools;

import java.io.IOException;
import java.io.InputStream;

import javax.naming.Context;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.member.model.MemberService;

public class MemberImageReader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("image/gif");
		ServletOutputStream out = res.getOutputStream();
		try{
		String mem_no = req.getParameter("mem_no");
		MemberService memberSvc = new MemberService();
		byte[] mem_photo = memberSvc.getOneMember(mem_no).getMem_photo();
		out.write(mem_photo);
		out.flush();
		out.close();
		}catch(Exception e){
			ServletContext context = getServletContext();
			InputStream in = context.getResource("images/user_default_full.jpg").openStream();
			byte[] buf = new byte[in.available()];
			in.read(buf);
			in.close();
			out.write(buf);
			out.flush();
			out.close();
		}
	}


}

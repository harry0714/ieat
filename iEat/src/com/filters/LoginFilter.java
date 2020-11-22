// Login Filter 過濾是否為會員登入狀態的濾器
package com.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	private FilterConfig config;

	public void init(FilterConfig config) {
		this.config = config;
	}

	public void destroy() {
		config = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException, IOException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		// 【取得 session】
		HttpSession session = req.getSession();
		// 【從 session 判斷此user是否登入過】
		Object user = session.getAttribute("user");
		if (user == null) {
			session.setAttribute("location", req.getRequestURI());
			//req.setAttribute("needLogin", "needLogin");
			 session.setAttribute("needLogin", "needLogin");
			res.sendRedirect(req.getContextPath() + "/front-end/login.jsp");
			return;
		} else {
			chain.doFilter(request, response);
		}
	}
}

// 店家登入的濾器
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

public class StoreLoginFilter implements Filter{

	private FilterConfig config; 
	
	@Override
	public void init(FilterConfig config){
		this.config = config; 
	}

	@Override
	public void destroy() {
		config = null; 		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response; 
		// 【取得 session】
		HttpSession session = req.getSession();
		// 【從 session 判斷是否為店家登入的狀態】
		Object store = session.getAttribute("store"); 	
		// 【若是非登入狀態  重導回登入頁面】
		if(store == null) {
			session.setAttribute("location", req.getRequestURI());
			res.sendRedirect(req.getContextPath() + "/front-end/login.jsp");
			return;
		} else { //【若是已登入狀態  繼續下一步驟】
		chain.doFilter(request, response);
		}
	} 
}

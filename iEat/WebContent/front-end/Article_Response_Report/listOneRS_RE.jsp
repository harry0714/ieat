<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="com.article_response_report.model.*"%>
<%
Article_rs_reVO article_rs_reVO = (Article_rs_reVO) request.getAttribute("article_rs_reVO"); //EmpServlet.java(Concroller), 存入req的empVO物件
%>    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>食記回覆檢舉 - listOneArticle_Response_Report</title>
</head>
<body bgcolor='white'>

<table border='1' cellpadding='5' cellspacing='0' width='600'>
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20'>
		<td>
		<h3>食記檢舉資料 - listOneArticle_Report.jsp</h3>
		<a href="<%=request.getContextPath()%>/back_end/SelectArticle_RS_RE_Page.jsp"><img src="<%=request.getContextPath()%>/front_end/Article/images/3283809_1.jpg" width="100" height="32" border="0">回首頁</a>
		</td>
	</tr>
</table>

<table border='1' bordercolor='#CCCCFF' width='600'>
	<tr>
	
		<th>食記回覆檢舉編號</th>
		<th>會員編號</th>
		<th>食記回覆編號</th>
		<th>食記回覆檢舉時間</th>
		<th>食記回覆檢舉內容</th>
		<th>食記回覆檢舉狀態</th>
	</tr>
	<tr align='center' valign='middle'>
	
		<td><%=article_rs_reVO.getArt_rs_re_no()%></td>
		<td><%=article_rs_reVO.getMem_no()%></td>
		<td><%=article_rs_reVO.getArt_rs_no()%></td>
		<td><%=article_rs_reVO.getArt_rs_re_date()%></td>
		<td><%=article_rs_reVO.getArt_rs_re_con()%></td>
		<td><%=article_rs_reVO.getArt_rs_re_sta()%></td>
		
	</tr>
</table>
</body>
</html>
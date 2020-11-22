<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.article_response.model.*"%>
<%
Article_ResponseVO article_responseVO = (Article_ResponseVO) request.getAttribute("article_responseVO");
%>    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>食記回覆資料 - listOneArticle_Response.jsp</title>
</head>
<body bgcolor='white'>

<table border='1' cellpadding='5' cellspacing='0' width='600'>
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20'>
		<td>
		<h3>食記回覆資料 - ListOneArticle_Response.jsp</h3>
		<a href="<%=request.getContextPath()%>/front_end/SelectArticle_RS_Page.jsp"><img src="<%=request.getContextPath()%>/front_end/Article/images/3283809_1.jpg" width="100" height="32" border="0">回首頁</a>
		</td>
	</tr>
</table>

<table border='1' bordercolor='#CCCCFF' width='600'>
	<tr>
		<th>食記回覆編號</th>
		<th>食記編號</th>
		<th>會員編號</th>
		<th>食記回覆內容</th>
		<th>食記回覆時間</th>
		
	</tr>
	<tr align='center' valign='middle'>
		<td><%=article_responseVO.getArt_rs_no()%></td>
		<td><%=article_responseVO.getArt_no()%></td>
		<td><%=article_responseVO.getMem_no()%></td>
		<td><%=article_responseVO.getArt_rs_context()%></td>
		<td><%=article_responseVO.getArt_rs_date()%></td>
	</tr>
</table>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="UTF-8"%>
<%@ page import="com.article_report.model.*"%>
<%
Article_reportVO article_reportVO = (Article_reportVO) request.getAttribute("article_reportVO"); //EmpServlet.java(Concroller), 存入req的empVO物件
%>    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>食記檢舉資料 - listOneArticle_Report.jsp</title>
</head>
<body bgcolor='white'>

<table border='1' cellpadding='5' cellspacing='0' width='600'>
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20'>
		<td>
		<h3>食記檢舉資料 - listOneArticle_Report.jsp</h3>
		<a href="<%=request.getContextPath()%>/back_end/SelectArticle_RE_Page.jsp"><img src="<%=request.getContextPath()%>/front_end/Article/images/beef.jpg" width="100" height="32" border="0">回首頁</a>
		</td>
	</tr>
</table>

<table border='1' bordercolor='#CCCCFF' width='600'>
	<tr>
	
		<th>食記檢舉編號</th>
		<th>食記編號</th>
		<th>會員編號</th>
		<th>食記檢舉內容</th>
		<th>食記檢舉時間</th>
		<th>食記檢舉狀態</th>
	</tr>
	<tr align='center' valign='middle'>
	
	
		<td><%=article_reportVO.getArt_re_no()%></td>
		<td><%=article_reportVO.getArt_no()%></td>
		<td><%=article_reportVO.getMem_no()%></td>
		<td><%=article_reportVO.getArt_re_context()%></td>
		<td><%=article_reportVO.getArt_re_date()%></td>
		
		<td><%=article_reportVO.getArt_re_status()%></td>
		
	</tr>
</table>

</body>
</html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.article.model.*"%>
<%@ page import="com.article_report.model.*"%>
<%@ page import="com.article_response_report.model.*"%>


<%-- 此頁練習採用 EL 的寫法取值 --%>

<%
	Article_rs_reService artrsreSvc = new Article_rs_reService();
    List<Article_rs_reVO> list = artrsreSvc.getAll();
    pageContext.setAttribute("list",list);
%>

<html>
<head>
<title>所有食記資料 - listAllArticle_Rs_Re.jsp</title>
</head>
<body bgcolor='white'>
<b><font color=red>此頁練習採用 EL 的寫法取值:</font></b>
<table border='1' cellpadding='5' cellspacing='0' width='800'>
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20'>
		<td>
		<h3>所有食記回覆檢舉 - ListAllArticle_Response_Report.jsp</h3>
		<a href="<%=request.getContextPath()%>/SelectArticle_RS_RE_Page.jsp"><img src="<%=request.getContextPath()%>/front_end/Article/images/3283809_1.jpg" width="100" height="32" border="0">回首頁</a>
		</td>
	</tr>
</table>

<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>請修正以下錯誤:
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>

<table border='1' bordercolor='#CCCCFF' width='800'>
	<tr>
		<th>食記回復檢舉編號</th>
		<th>會員編號</th>
		<th>食記回覆編號</th>
		<th>食記回覆檢舉時間</th>
		<th>食記回覆檢舉內容</th>
		<th>食記回覆檢舉狀態</th>
		
		<th>修改</th>
		<th>刪除</th>
	</tr>
	<%@ include file="Asrfile01" %> 
	<c:forEach var="article_rs_reVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
		<tr align='center' valign='middle'>
			<td>${article_rs_reVO.art_rs_re_no}</td>
			<td>${article_rs_reVO.mem_no}</td>
			<td>${article_rs_reVO.art_rs_no}</td>
			<td>${article_rs_reVO.art_rs_re_date}</td>
			<td>${article_rs_reVO.art_rs_re_con}</td>
			<td>${article_rs_reVO.art_rs_re_sta}</td>
			
			
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Response_Report/article_response_report.do">
			     <input type="submit" value="修改">
			     <input type="hidden" name="art_rs_re_no" value="${article_rs_reVO.art_rs_re_no}">
			     <input type="hidden" name="action"	value="getOne_For_Update"></FORM>
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Response_Report/article_response_report.do">
			    <input type="submit" value="刪除">
			    <input type="hidden" name="art_rs_re_no" value="${article_rs_reVO.art_rs_re_no}">
			    <input type="hidden" name="action"value="delete"></FORM>
			</td>
		</tr>
	</c:forEach>
</table>
<%@ include file="Asrfile02" %>

</body>
</html>
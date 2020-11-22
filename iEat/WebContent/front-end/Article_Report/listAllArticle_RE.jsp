<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.article.model.*"%>
<%@ page import="com.article_report.model.*"%>
<%@ page import="com.member.model.*"%>
<%-- 此頁練習採用 EL 的寫法取值 --%>

<%
	Article_ReportService artreSvc = new Article_ReportService();
    List<Article_reportVO> list = artreSvc.getAll();
    pageContext.setAttribute("list",list);
%>
<jsp:useBean id="memberSvc" scope="page" class="com.member.model.MemberService"/>
<html>
<head>
<title>所有食記資料 - listAllArticle_Report.jsp</title>
</head>
<body bgcolor='white'>
<b><font color=red>此頁練習採用 EL 的寫法取值:</font></b>
<table border='1' cellpadding='5' cellspacing='0' width='800'>
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20'>
		<td>
		<h3>所有食記檢舉資料 - ListAllArticle_Report.jsp</h3>
		<a href="<%=request.getContextPath()%>/back_end/SelectArticle_RE_Page.jsp"><img src="images/steak.jpg" width="100" height="32" border="0">回首頁</a>
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
		<th>食記檢舉編號</th>
		<th>食記編號</th>
		<th>會員編號</th>
		<th>食記檢舉內容</th>
		<th>食記檢舉時間</th>
		<th>食記檢舉狀態</th>
		
		<th>修改</th>
		<th>刪除</th>
	</tr>
	<%@ include file="Afile01" %> 
	<c:forEach var="article_reportVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
		<tr align='center' valign='middle'>
			<td>${article_reportVO.art_re_no}</td>
			<td>${article_reportVO.art_no}</td>
			
			<td width="100">
				<c:forEach var="memberVO" items="${memberSvc.all}">
                    <c:if test="${article_reportVO.mem_no==memberVO.mem_no}">
	                    ${memberVO.mem_no}【<font color=orange>${memberVO.mem_name}</font>】
                    </c:if>
                </c:forEach>
			</td>		
			
			
			<td>${article_reportVO.art_re_context}</td>
			<td>${article_reportVO.art_re_date}</td>
			<td>
			<c:if test="${article_reportVO.art_re_status  == '0'}">
				尚未審核!
			</c:if>
			<c:if test="${article_reportVO.art_re_status  == '1'}">
				通過審核!
			</c:if>
			<c:if test="${article_reportVO.art_re_status  == '2'}">
				未通過審核!
			</c:if>
			</td>
				
			
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Report/article_report.do">
			     <input type="submit" value="修改">
			     <input type="hidden" name="art_re_no" value="${article_reportVO.art_re_no}">
			     <input type="hidden" name="action"	value="getOne_For_Update"></FORM>
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Report/article_report.do">
			    <input type="submit" value="刪除">
			    <input type="hidden" name="art_re_no" value="${article_reportVO.art_re_no}">
			    <input type="hidden" name="action"value="delete"></FORM>
			</td>
		</tr>
	</c:forEach>
</table>
<%@ include file="Afile02" %>

</body>
</html>
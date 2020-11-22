<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.article.model.*"%>
<%@ page import="com.article_response.model.*"%>
<%@ page import="com.article_report.model.*"%>
<%@ page import="com.member.model.*"%>

<%-- 此頁練習採用 EL 的寫法取值 --%>


<%
ArticleService artSvc = new ArticleService();
List<ArticleVO> list = artSvc.getAll();
pageContext.setAttribute("list", list);
	
%>
<jsp:useBean id="memberSvc" scope="page" class="com.member.model.MemberService"/>

<html>
<head>
<title>所有食記資料 - listAllArticle.jsp</title>
</head>
<body bgcolor='white'>
<b><font color=red>此頁練習採用 EL 的寫法取值:</font></b>
<table border='1' cellpadding='5' cellspacing='0' width='800'>
	<tr bgcolor='orange' align='center' valign='middle' height='20'>
		<td>
		<h3>所有食記 - ListAllArticle.jsp</h3>
		<a href="<%=request.getContextPath()%>/front_end/SelectArticlePage.jsp"><img src="<%=request.getContextPath()%>/front_end/Article/images/beef.jpg" width="100" height="32" border="0">回首頁</a>
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
		<th>食記編號</th>
		<th width="100" >食記名稱</th>
		<th>食記時間</th>
		<th>食記內容</th>
		<th>會員編號</th>
		<th>修改</th>
		<th>刪除<font color=red>(關聯回覆與檢舉)</font></th>
		<th>查詢檢舉</th>
		
	</tr>
	<%@ include file="Afile01" %> 
	<c:forEach var="articleVO" items="${list}"  begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
		<tr align='center' valign='middle'>
			<td>${articleVO.art_no}</td>
			<td>${articleVO.art_name}</td>
			<td>${articleVO.art_date}</td>
			<td>${articleVO.art_context}</td>
			
			<td>${articleVO.mem_no}</td>
			
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article/article.do">
			    <input type="submit" value="修改" > 
			    <input type="hidden" name="art_no" value="${articleVO.art_no}">
			    <input type="hidden" name="action" value="getOne_For_Update">
			</td></FORM>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article/article.do">
			    <input type="submit" value="刪除">
			    <input type="hidden" name="art_no" value="${articleVO.art_no}">
			    <input type="hidden" name="action" value="delete">
			</td></FORM>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article/article.do">
			    <input type="submit" value="送出查詢"> 
			    <input type="hidden" name="art_no" value="${articleVO.art_no}">
			    <input type="hidden" name="action" value="listArticle_Reports_ByArt_NO_B">
			</td></FORM>
		</tr>
	</c:forEach>
</table>
<%@ include file="Afile02" %>
<%if (request.getAttribute("listArticle_Reports_ByArt_NO")!=null){%>
       <jsp:include page="listArticle_Reports_ByArt_NO.jsp" />
<%} %>

</body>
</html>
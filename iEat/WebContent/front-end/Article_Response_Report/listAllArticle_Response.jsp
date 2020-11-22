<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.article.model.*"%>
<%@ page import="com.article_response.model.*"%>
<%@ page import="com.article_response_report.model.*"%>
<%@ page import="com.member.model.*"%>
<%-- 此頁練習採用 EL 的寫法取值 --%>

<%
    Article_ResponseService artrsSvc = new Article_ResponseService();
    List<Article_ResponseVO> list = artrsSvc.getAll();
    pageContext.setAttribute("list",list);
%>
<jsp:useBean id="artSvc" scope="page" class="com.article.model.ArticleService"/>
<jsp:useBean id="memberSvc" scope="page" class="com.member.model.MemberService"/>
<html>
<head>
<title>所有食記回覆資料 - listAllArticle_Response.jsp</title>
</head>
<body bgcolor='white'>
<b><font color=red>此頁練習採用 EL 的寫法取值:</font></b>
<table border='1' cellpadding='5' cellspacing='0' width='800'>
	<tr bgcolor='orange' align='center' valign='middle' height='20'>
		<td>
		<h3>所有食記回覆資料 - listAllArticle_Response.jsp</h3>
		<a href="<%=request.getContextPath()%>/front_end/SelectArticle_RS_Page.jsp"><img src="<%=request.getContextPath()%>/front_end/Article/images/beef.jpg" width="100" height="32" border="0">回首頁</a>
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
		<th width="200px">食記</th>
		<th width="200px">食記回覆編號</th>
		<th width="200px">會員編號</th>
		<th width="200px" >食記回覆內容</th>
		<th width="200px">食記回覆時間</th>

<!-- 		<th>查詢回覆檢舉</th> -->
	</tr>
	
	<%@ include file="Asrfile01" %> 
	<c:forEach var="article_responseVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
		<tr align='center' valign='middle'>
		
			<td>
			<c:forEach var="articleVO" items="${artSvc.all}">
                    <c:if test="${article_responseVO.art_no==articleVO.art_no}">
	                    【${articleVO.art_name}】 
                    </c:if>
                </c:forEach>
			</td>
			
			
			<td>${article_responseVO.art_rs_no}</td>
			
			
			<td>
			<c:forEach var="memberVO" items="${memberSvc.all}">
                    <c:if test="${article_responseVO.mem_no==memberVO.mem_no}">
	                    【${memberVO.mem_name}】 
                    </c:if>
                </c:forEach>
			</td>	
					
			<td>${article_responseVO.art_rs_context}</td>
			<td>${article_responseVO.art_rs_date}</td>
			
			
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Response/article_response.do">
			     <input type="submit" value="修改">
			     <input type="hidden" name="art_rs_no" value="${article_responseVO.art_rs_no}">
			     <input type="hidden" name="action"	value="getOne_For_Update"></FORM>
			</td>
			<td>
			    <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Response/article_response.do">
			    <input type="submit" value="刪除">
			    <input type="hidden" name="art_rs_no" value="${article_responseVO.art_rs_no}">
			    <input type="hidden" name="action"value="delete"></FORM>
			    
			</td>
<!-- 			<td> -->
<%-- 				<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Response/article_response.do"> --%>
<!-- 			    <input type="submit" value="送出查詢">  -->
<%-- 			    <input type="hidden" name="art_rs_no" value="${article_responseVO.art_rs_no}"> --%>
<!-- 			    <input type="hidden" name="action" value="listArticle_rs_re_ByArt_RS_NO_B"></FORM> -->
<!-- 			</td> -->
			
		</tr>
	</c:forEach>
</table>
<%@ include file="Asrfile02" %>

<%if (request.getAttribute("listArticle_rs_re_ByArt_RS_NO")!=null){%>
       <jsp:include page="listArticle_rs_re_ByArt_RS_NO.jsp" />
<%} %>

</body>
</html>

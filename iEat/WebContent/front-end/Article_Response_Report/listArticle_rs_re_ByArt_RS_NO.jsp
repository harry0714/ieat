<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<jsp:useBean id="listArticle_rs_re_ByArt_RS_NO" scope="request" type="java.util.Set" />
<jsp:useBean id="artrsSvc" scope="page" class="com.article_response.model.Article_ResponseService" />
<jsp:useBean id="memberSvc" scope="page" class="com.member.model.MemberService" />
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>回覆食記檢舉 -listOneArticle_RS_RE.jsp </title>
</head>
<body bgcolor='white'>

<b><font color=red>此頁練習採用 EL 的寫法取值:</font></b>
<table border='1' cellpadding='5' cellspacing='0' width='800'>
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20'>
		<td>
		<h3>回覆食記檢舉 - listOneArticle_RS_RE.jsp</h3>
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
		<th>食記回覆編號</th>
		<th>食記回覆檢舉編號</th>
		<th width="100px">會員編號</th>
		<th>食記回覆檢舉時間</th>
		<th>食記回覆檢舉內容</th>
		<th>食記回覆檢舉狀態</th>
		<th>修改</th>
		<th>刪除</th>
	</tr>
	
	<c:forEach var="article_rs_reVO" items="${listArticle_rs_re_ByArt_RS_NO}" >
		<tr align='center' valign='middle'>
		
			<td>
				<c:forEach var="article_responseVO" items="${artrsSvc.all}">
                    <c:if test="${article_rs_reVO.art_rs_no==article_responseVO.art_rs_no}">
	                    ${article_responseVO.art_rs_no}【<font color=orange>${article_responseVO.art_rs_context}</font>】
                    </c:if>
                </c:forEach>
			</td>
			
			<td>${article_rs_reVO.art_rs_re_no}</td>
			
			<td width="100">
				<c:forEach var="memberVO" items="${memberSvc.all}">
                    <c:if test="${article_rs_reVO.mem_no==memberVO.mem_no}">
	                    ${memberVO.mem_no}【<font color=orange>${memberVO.mem_name}</font>】
                    </c:if>
                </c:forEach>
			</td>
			
			<td>${article_rs_reVO.art_rs_re_con}</td>
			<td>${article_rs_reVO.art_rs_re_date}</td>
	
			<td>
				<c:if test="${article_rs_reVO.art_rs_re_sta  == '0'}">
					尚未審核!
				</c:if>
				<c:if test="${article_rs_reVO.art_rs_re_sta  == '1'}">
					通過審核!
				</c:if>
				<c:if test="${article_rs_reVO.art_rs_re_sta  == '2'}">
					未通過審核!
				</c:if>
			</td>



			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Response_Report/article_response_report.do">
			    <input type="submit" value="修改"> 
			    <input type="hidden" name="art_rs_re_no"value="${article_rs_reVO.art_rs_re_no}">
			    <input type="hidden" name="requestURL"	value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller--><!-- 目前尚未用到  -->
			    <input type="hidden" name="action"	value="getOne_For_Update"></FORM>
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Response_Report/article_response_report.do">
			    <input type="submit" value="刪除">
			    <input type="hidden" name="art_rs_re_no" value="${article_rs_reVO.art_rs_re_no}">
			    <input type="hidden" name="requestURL"	value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller-->
			    <input type="hidden" name="action"value="delete"></FORM>
			</td>
		</tr>
	</c:forEach>
</table>

<br>本網頁的路徑:<br><b>
   <font color=red>request.getServletPath():</font> <%= request.getServletPath()%><br>
   <font color=red>request.getRequestURI(): </font> <%= request.getRequestURI()%> </b>
</body>
</html>

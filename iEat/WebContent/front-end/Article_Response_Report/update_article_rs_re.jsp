<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="com.article_response_report.model.*"%>
<%@ page import="com.article_response.model.*"%>
<%@ page import="com.article.model.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>

<%
	Article_rs_reVO article_rs_reVO = (Article_rs_reVO) request.getAttribute("article_rs_reVO"); //EmpServlet.java (Concroller), 存入req的empVO物件 (包括幫忙取出的empVO, 也包括輸入資料錯誤時的empVO物件)
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>食記回覆檢舉修改- update_article_rs_input.jsp</title>
</head>
<body bgcolor='white'>

<table border='1' cellpadding='5' cellspacing='0' width='400'>
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20'>
		<td>
		<h3>食記回覆檢舉修改 - update_article_rs_input.jsp</h3>
		<a href="<%=request.getContextPath()%>/back_end/SelectArticle_RS_RE_Page.jsp"><img src="<%=request.getContextPath()%>/front_end/Article/images/3283809_1.jpg" width="100" height="32" border="0">回首頁</a></td>
	</tr>
</table>

<h3>資料修改:</h3>
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

<FORM METHOD="post" ACTION="article_response_report.do" name="form1">
<table border="0">

	<tr>
		<td>食記回覆檢舉編號:<font color=red><b>*</b></font></td>
		<td><%=article_rs_reVO.getArt_rs_re_no()%></td>
	</tr>
	
	<%
	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	Date current = new Date();
	String sampleDate = sdFormat.format(current);
	%>
	
	<tr>
		<td>食記檢舉回覆內容:</td>
		<td><input type="TEXT" name="art_rs_re_con" size="45" value="<%=article_rs_reVO.getArt_rs_re_con()%>" /></td>
	</tr>

	<tr>
			<td>食記回覆檢舉狀態</td>
		<td>
		<select size="1" name="art_rs_re_sta">
		  <option value=0>未審核</option>
  		  <option value=1>審核通過</option>
  		  <option value=2>審核未通過</option>
		</select>
		</td>
	</tr>


</table>
<br>
<input type="hidden" name="action" value="update">
<input type="hidden" name="art_rs_re_no" value="<%=article_rs_reVO.getArt_rs_re_no()%>">
<input type="hidden" name="mem_no" value="<%=article_rs_reVO.getMem_no()%>">
<input type="hidden" name="art_rs_no" value="<%=article_rs_reVO.getArt_rs_no()%>">
<input type="hidden" name="art_rs_re_date" value="<%= java.sql.Timestamp.valueOf(sampleDate)%>">
<input type="submit" value="送出修改"></FORM>

</body>
</html>
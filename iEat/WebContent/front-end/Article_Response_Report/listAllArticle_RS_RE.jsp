<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.article.model.*"%>
<%@ page import="com.article_report.model.*"%>
<%@ page import="com.article_response_report.model.*"%>


<%-- �����m�߱ĥ� EL ���g�k���� --%>

<%
	Article_rs_reService artrsreSvc = new Article_rs_reService();
    List<Article_rs_reVO> list = artrsreSvc.getAll();
    pageContext.setAttribute("list",list);
%>

<html>
<head>
<title>�Ҧ����O��� - listAllArticle_Rs_Re.jsp</title>
</head>
<body bgcolor='white'>
<b><font color=red>�����m�߱ĥ� EL ���g�k����:</font></b>
<table border='1' cellpadding='5' cellspacing='0' width='800'>
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20'>
		<td>
		<h3>�Ҧ����O�^�����| - ListAllArticle_Response_Report.jsp</h3>
		<a href="<%=request.getContextPath()%>/SelectArticle_RS_RE_Page.jsp"><img src="<%=request.getContextPath()%>/front_end/Article/images/3283809_1.jpg" width="100" height="32" border="0">�^����</a>
		</td>
	</tr>
</table>

<%-- ���~��C --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>�Эץ��H�U���~:
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>

<table border='1' bordercolor='#CCCCFF' width='800'>
	<tr>
		<th>���O�^�_���|�s��</th>
		<th>�|���s��</th>
		<th>���O�^�нs��</th>
		<th>���O�^�����|�ɶ�</th>
		<th>���O�^�����|���e</th>
		<th>���O�^�����|���A</th>
		
		<th>�ק�</th>
		<th>�R��</th>
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
			     <input type="submit" value="�ק�">
			     <input type="hidden" name="art_rs_re_no" value="${article_rs_reVO.art_rs_re_no}">
			     <input type="hidden" name="action"	value="getOne_For_Update"></FORM>
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Response_Report/article_response_report.do">
			    <input type="submit" value="�R��">
			    <input type="hidden" name="art_rs_re_no" value="${article_rs_reVO.art_rs_re_no}">
			    <input type="hidden" name="action"value="delete"></FORM>
			</td>
		</tr>
	</c:forEach>
</table>
<%@ include file="Asrfile02" %>

</body>
</html>
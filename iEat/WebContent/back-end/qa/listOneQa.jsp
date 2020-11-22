<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.qa.model.*"%>
<%
QaVO qaVO = (QaVO) request.getAttribute("qaVO"); //EmpServlet.java(Concroller), 存入req的empVO物件
%>
<html>
<head>
<title>員工資料 - listOneEmp.jsp</title>
</head>
<body bgcolor='white'>

<table border='1' cellpadding='5' cellspacing='0' width='600'>
	<tr bgcolor='#CCCCFF' align='center' valign='middle' height='20'>
		<td>
		<h3>員工資料 - ListOneEmp.jsp</h3>
		<a href="<%=request.getContextPath()%>/back-end/qa/listAllQa.jsp">回首頁</a>
		</td>
	</tr>
</table>

<table border='1' bordercolor='#CCCCFF' width='600'>
	<tr>
		<th>Q&A編號</th>
		<th>Q&A種類</th>
		<th>問題</th>
		<th>答案</th>
		
	</tr>
	<tr align='center' valign='middle'>
			<td>${qaVO.qa_no}</td>
			<td>${qaVO.qa_type_no}</td>
			<td>${qaVO.q_context}</td>
			<td>${qaVO.a_context}</td>
	</tr>
</table>

</body>
</html>

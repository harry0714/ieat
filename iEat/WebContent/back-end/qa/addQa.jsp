<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.qa.model.*"%>

<%
	QaVO qaVO = (QaVO) request.getAttribute("qaVO");
%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- CSS -->
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/jquery-ui.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/sweetalert.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/metisMenu.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/timeline.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/sb-admin-2.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/morris.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/ie10-viewport-bug-workaround.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/dashboard.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/c3.css" rel="stylesheet" type="text/css">
<!-- CSS -->

<!-- JS -->  
<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>
<script src="<%=request.getContextPath()%>/js/d3.min.js" charset="utf-8"></script>
<script src="<%=request.getContextPath()%>/js/c3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script src="<%=request.getContextPath()%>/js/indexsocket.js"></script>
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>
<script type="text/javascript" src="https://maps.google.com/maps/api/js?key=AIzaSyANhvv_DDadWfAzIGurcl8fZixWAdfrgQk"></script>
<!-- JS -->

<!-- Logo --><link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/eat.png" /><!-- Logo -->
<title>iEat - 後端Q&A新增</title>
</head>
<body>
<%@ include file="/back-end/page/head.jsp" %> 
<%@ include file="/back-end/page/body.jsp" %> 
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

<div class="container-fluid">
	<div class="row">
		<div class="col-sm-9 col-sm-offset-3  col-md-offset-2 main">
		<a href="<%=request.getContextPath()%>/back-end/qa/listAllQa.jsp">回首頁</a>
			<h1 class="page-header"></h1>
			
			<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/qa/qa.do" name="form1">
				<table border="0">
					<jsp:useBean id="qatypeSvc" scope="page" class="com.qa_type.model.Qa_typeService" />
					<tr>
						<td>Q&A type:</td>
						<td><b>問題種類編號:</b> <select size="1" name="qa_type">
								<c:forEach var="qatypeVO" items="${qatypeSvc.all}">
									<option value="${qatypeVO.qa_type_no}">${qatypeVO.qa_type_name}
								</c:forEach>
						</select></td>
					</tr>

					<tr>
						<td>A：</td>
						<td>
							<textarea rows="4" cols="50" name="q_context" value="${qaVO.getQ_context}"></textarea>
						</td>
					</tr>
					<tr>
						<td>Q:</td>
						<td>
							<textarea rows="4" cols="50" name="a_context" value="${qaVO.getA_context}"></textarea>
						</td>	
					</tr>
					<jsp:useBean id="qaSvc" scope="page" class="com.qa.model.QaService" />
				</table>
				<br> <input type="hidden" name="action" value="insert">
				<input type="submit" value="送出新增">
			</FORM>
		</div>
	</div>
</div>
</body>
</html>

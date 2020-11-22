<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.qa.model.*"%>
<%
    QaService qaSvc = new QaService();
    List<QaVO> list = qaSvc.getAll();
    pageContext.setAttribute("list",list);
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
<title>iEat - 後端Q&A管理</title>
<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
								<script>
								$(document).ready(function(){
									$(".glyphicon-trash").click(function(){
										$(this).closest("form").submit();
									})
								})
							

								$(document).ready(function(){
									$(".glyphicon-pencil").click(function(){
										$(this).closest("form").submit();
									})
								})
								</script>								
</head>								
<%@ include file="/back-end/page/head.jsp" %> 
<%@ include file="/back-end/page/body.jsp" %>
<c:if test="${not empty errorMsgs}">
	<font color='red'>請修正以下錯誤:
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>


			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h1 class="page-header">Q&A</h1>
				<table class="table table-striped">
				<ul>
					<a style="float:right" class="glyphicon glyphicon-plus" href="<%=request.getContextPath()%>/back-end/qa/addQa.jsp" role="button">新增Q&A</a>
					
				</ul>		
	<thead>			
	<tr>
		<th>Q&A編號</th>
		<th>Q&A種類</th>
		<th>問題</th>
		<th>答案</th>
		<th>刪除</th>
	</tr>
	</thead>	
<tbody>					
	<%@ include file="/back-end/page/page1.file" %> 	
	<c:forEach var="qaVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
		<tr>
			<td>${qaVO.qa_no}</td>
			<td><c:if test="${qaVO.qa_type_no=='QT01'}">軟體問題 </c:if>
			<c:if test="${qaVO.qa_type_no=='QT02'}">訂餐問題</c:if> 
			<c:if test="${qaVO.qa_type_no=='QT03'}">訂位問題</c:if> </td>
			<td>${qaVO.q_context}</td>
			<td>${qaVO.a_context}</td>
			
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/qa/qa.do">
				<a href="#"><i class="glyphicon glyphicon-trash"></i></a>
<!-- 			    <input type="submit" value="刪除"> -->
			    <input type="hidden" name=qa_no value="${qaVO.qa_no}">
			    <input type="hidden" name="action"value="delete">
			    </FORM>
			</td>
		</tr>
	</c:forEach>
	</tbody>
	<%@ include file="/back-end/page/page2.file" %>	
	</table>
	</div>
</html>
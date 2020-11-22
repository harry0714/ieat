<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.member.model.*"%>

<%
	MemberService memSvc = new MemberService();
	List<MemberVO> list = memSvc.getAll();
	pageContext.setAttribute("list", list);
%>
<% java.sql.Date mem_sysdate = new java.sql.Date(System.currentTimeMillis());
	  pageContext.setAttribute("mem_sysdate", mem_sysdate);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="<%=request.getContextPath()%>/back-end/index.jsp">
<title>iEat - 後端會員管理</title>
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
<script>
$(document).ready(function(){
    $(".glyphicon-exclamation-sign").click(function(){
    	$(this).closest("form").submit();
	});
    $(".glyphicon-ok").click(function(){
    	$(this).closest("form").submit();
	});
});
</script>
</head>
<body>
<%@ include file="/back-end/page/head.jsp"%>
<%@ include file="/back-end/page/body.jsp"%>
<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1 class="page-header">會員帳號管理</h1>
	<ul class="nav nav-pills">

		<table class="table table-striped">
			<thead>
				<tr>
					<th>會員編號</th>
					<th>會員姓名</th>
					<th>會員帳號</th>
					<th>性別</th>
					<th>信箱</th>					
					<th>會員狀態</th>
					<th>照片</th>
					<th>停權</th>
					<th>啟用</th>
					
				</tr>

			</thead>
			<tbody>

				<%@ include file="/back-end/page//page1.file"%>
				<c:forEach var="memVO" items="${list}" begin="<%=pageIndex%>"
					end="<%=pageIndex+rowsPerPage-1%>">
					<tr>
						<td>${memVO.mem_no}</td>
						<td>${memVO.mem_name}</td>
						<td>${memVO.mem_acct}</td>
						<td>${memVO.mem_sex %2== 1 ? '男': '女'}</td>
						
						<td>${memVO.mem_email}</td>
						
					
						<td>${(memVO.mem_validate>mem_sysdate)?"已停權":"使用中"}</td>
					
						
						<td><img
							src="<%=request.getContextPath()%>/member/memberImageReader.do?mem_no=${memVO.mem_no}"
							width=100 height=100></td>
						
						<td>
							<FORM METHOD="post"
								ACTION="<%=request.getContextPath()%>/member/member.do">
								<A HREF="#"><I CLASS="glyphicon glyphicon-exclamation-sign"></I></A>
<!-- 								<input type="button" value="停權" class="glyphicon glyphicon-exclamation-sign" > -->
								 <input type="hidden" name="mem_no" value="${memVO.mem_no}"> 
								 <input type="hidden" name="action" value="Disable">
							</FORM>
						</td>
						
						
						<td>
							<FORM METHOD="post"
								ACTION="<%=request.getContextPath()%>/member/member.do">
								<A HREF="#"><I CLASS="glyphicon glyphicon-ok"></I></A>
<!-- 								<input type="submit" value="啟用" class="btn btn-danger btn-xm">  -->
								<input type="hidden" name="mem_no" value="${memVO.mem_no}">
								<input type="hidden" name="action" value="Enable">
							</FORM>
						</td>
						
					</tr>
				</c:forEach>
			</tbody>
			<%@ include file="/back-end/page/page2.file"%>

		</table>
</div>



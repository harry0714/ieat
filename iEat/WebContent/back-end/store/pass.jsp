<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="com.store.model.*"%>
<% StoreVO storeVO =(StoreVO) request.getAttribute("storeVO"); %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" href="images/eat.png">
<title>IEat</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/ie10-viewport-bug-workaround.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/dashboard.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>

</head>
<body>

			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h1 class="page-header"></h1>
					<tbody>
			<table class="table table-striped">
				<tr>
					<th>店家編號</th>
					<td>${storeVO.store_no}</td>
				</tr>
				<tr>
					<th>店家帳號</th>
					<td>${storeVO.store_id}</td>
				</tr>
				<tr>
					<th>店家名稱</th>
					<td>${storeVO.store_name}</td>
				</tr>
				<tr>
					<th>店家電話</th>
					<td>${storeVO.store_phone}</td>
				</tr>
				<tr>
					<th>店家負責人</th>
					<td>${storeVO.store_owner}</td>
				</tr>
				<tr>
					<th>店家信箱</th>
					<td>${storeVO.store_email}</td>
				</tr>
				<tr>
					<th>店家狀態</th>
					
					<td> <button type="button" class="btn btn-success btn-xs">${storeVO.store_status %2== 0 ? "未審核": "已審核"}</button></td>
				</tr>
			</table>
			
			</div>
		</div>
	

	
</body>
</html>

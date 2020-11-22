<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.store.model.*"%>
<%@ page import="com.store_photo.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	StoreVO storeVO = (StoreVO) request.getAttribute("storeVO");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath()%>/images/iEat_logo.png" />
<link href="<%= request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath()%>/css/meal.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=request.getContextPath()%>/css/memberprefecture.css"rel="stylesheet" type="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>		
<jsp:useBean id="storeSvc" scope="page" class="com.store.model.StoreService"></jsp:useBean>
<title>iEat - 店家照片更新</title>
</head>
<body>
<!-- header -->
<jsp:include page="/front-end/head.jsp"/>
<!-- header -->

<!-- about -->
<div class="main-1">
	<div class="container">
		<!--  menu -->
		<div class="col-md-3">
			<jsp:include page="/front-end/store/storeMenu.jsp"/>
		</div>
		<!--  menu -->	
		<!--  顯示店家照片  供新增修改刪除用 -->
		<div class="col-md-9">
			<table border='1' bordercolor='#CCCCFF'>
				<tr>
					<th>照片名稱</th>
					<th>照片</th>
					<th>照片描述</th>
				</tr>
				<c:forEach var="store_photoVO" items="${listStore_photoByStore_no }">
				<tr align='center' valign='middle'>
					<td>${store_photoVO.photo_name }</td>
		 			<td>
		 				<img src="<%=request.getContextPath()%>/photo_read/photo_read.do?photo_no=${store_photoVO.photo_no}" width="150" height="150"/>
		 			</td> 
					<td>${store_photoVO.photo_des }</td>
					<td> 
						<form method="post" action="<%= request.getContextPath()%>/store_photo/store_photo.do">
							<input type="submit" value="修改" class="btn btn-default"> 
							<input type="hidden" name="store_photo_no" value="${store_reportVO.store_photo_no}">
							<input type="hidden" name="action" value="getOne_For_Update">			 
						</form>
					</td>
					<td>
					  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/store_photo/store_photo.do">
					    <input type="submit" value="刪除" class="btn btn-default">
					    <input type="hidden" name="store_photo_no" value="${store_reportVO.store_photo_no}">
					    <input type="hidden" name="action"value="delete">
					  </FORM>
					</td>				
				</tr>
				</c:forEach>
			</table>
			<p><input type="submit" value="新增照片" class="btn btn-default" id="insert_photo"></p>			
		</div>
	</div>
</div>
<!-- about -->
	
<!-- footer-->
<jsp:include page="/front-end/footer.html"/>
<!-- footer-->	
</body>
</html>
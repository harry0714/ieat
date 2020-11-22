<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.member.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/sweetalert.css">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/boostrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/previewimage.js"></script>
<script src="<%=request.getContextPath()%>/js/address.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<link href="<%=request.getContextPath()%>/css/memberprefecture.css"rel="stylesheet" type="text/css" />
</head>
<body>
	<!-- header -->
	<jsp:include page="/front-end/head.jsp" />
	<!-- header -->
	<!-- register -->

	<div class="main-1">
		<div class="container">
			<div class="col-md-3">
		<jsp:include page="/front-end/member/membermenu.jsp" />
		</div>
			<div class="col-md-9">
				<c:if test="${! empty errorMessage.elseError }">
					<div>
						<font color='red' size="5">
							<p>${errorMessage.elseError}</p>
						</font>
					</div>
				</c:if>
				<div class="panel panel-default">
				<div class="panel-heading" style="color:#d15a15;font-size:20px">基本資料</div>
				<div class="panel-body" style="padding:10%">
				<form action="<%=request.getContextPath()%>/member/member.do"
					method=post enctype="multipart/form-data" class="form-horizontal" role="form">
					<div class="col-sm-offset-2">
					<div  class="pic" style="margin:10px" >
					<img
						src="<%= request.getContextPath() %>/member/memberImageReader.do?mem_no=${memberVO.mem_no}"
						id="picture" width="150" height="150" name="img"> <input
						type="hidden" name="requestURL" value="">
						</div>
						</div>
 					<div class="form-group">
						<label for="InputName" class="control-label col-sm-2">上傳大頭照</label>
						<div class="col-sm-10">
							<input type="file" id="photo" name="mem_photo">
														<p class="bg-danger" style="visibility:${(empty errorMessage.mem_photo)?'hidden':'visible'};">
							<font color="#bb55ee">請選擇圖片格式的檔案</font>&nbsp;<font color="red">${errorMessage.mem_photo}</font>
							</p>
						</div>
					</div>
					<div class="form-group">
						<label for="InputName" class="control-label col-sm-2">姓名*</label>
						<div class="col-sm-10">
						<input type="text" class="form-control" id="InputName" placeholder="Name" name="mem_name" value="${memberVO.mem_name}">
						<p class="bg-danger show"
							style="visibility:${(empty errorMessage.mem_name)?'hidden':'visible'};">
							<font color="#bb55ee">請輸入中文名</font>&nbsp;<font color="red">${errorMessage.mem_name}</font>
						</p>
						</div>
					</div>

					<div class="form-group">
						<label for="InputEmail1" class="control-label col-sm-2">電子信箱*</label>
						<div class="col-sm-10">
						<input type="text" class="form-control" placeholder="Email" name="mem_email" id="InputEmail1" value="${memberVO.mem_email}" readonly>
						</div>
					</div>
					
					<div class="form-group has-feedback">
						<label for="InputPhone" class="control-label col-sm-2">手機號碼*</label>
						<div class="col-sm-10">						
							<input type="tel" class="form-control" placeholder="Phone Number" name="mem_phone" id="mem_phone" value="${ memberVO.mem_phone}">
							<p class="bg-danger"
							style="visibility:${(empty errorMessage.mem_phone)?'hidden':'visible'};">
							<font color="#bb55ee">請正確填寫</font>&nbsp;<font color="red">${errorMessage.mem_phone}</font>
								</p>
							<span class="glyphicon  form-control-feedback" aria-hidden="true" id="phonecheckstate"></span>
						</div>

					</div>

					<div class="form-group">
						<label for="mem_sex" class="control-label col-sm-2">性別*</label>
						<div class="col-sm-10">	
						<div class="form-inline">
							<label class="radio-inline"> <input type="radio"
								name="mem_sex" id="sex1" value="1"
								${(memberVO.mem_sex==1)?'checked':''}> 男
							</label> <label class="radio-inline"> <input type="radio"
								name="mem_sex" id="sex2" value="2"
								${(memberVO.mem_sex==2)?'checked':''}> 女
							</label>
						</div>
						<p class="bg-danger"
							style="visibility:${(empty errorMessage.mem_sex)?'hidden':'visible'};">
							<font color="#bb55ee">請正確填寫</font>&nbsp;<font color="red">${errorMessage.mem_sex}</font>
						</p>
						</div>
					</div>

					<div class="form-group">
						<label for="InputBirthday" class="control-label col-sm-2">出生年月日*</label>
						<div class="col-sm-10">	
						<input type="text"
							class="form-control" id="InputBirthday" name="mem_bd"
							value="${memberVO.mem_bd }" readonly>
						</div>
					</div>

					<div class="form-group">
						<label for="InputAddr" class="control-label col-sm-2">地址</label>
						<div class="col-sm-10">	
						<div class="form-inline">
							<span>縣市</span> <select class="form-control" name="mem_addr1"
								id="zone1"></select> <input type="hidden" id="addr1"
								value="${mem_addr1}"> <span>鄉鎮[市]區</span> <select
								class="form-control" name="mem_addr2" id="zone2"></select> <input
								type="hidden" id="addr2" value="${mem_addr2}"> <input
								type="hidden" name="mem_zip" value="${memberVO.mem_zip}"
								id="zipcode">
						</div>
						<input type="text" class="form-control" name="mem_addr3"
							style="margin-top: 2px" value="${mem_addr3}">
						</div>
					</div>
					
					<button type="submit" class="btn btn-success">修改</button>
					<font size="2" color="red"><span>*為必填</span></font> <input
						type="hidden" name="action" value="memberupdate">
				</form>
				<script>
					$(document).ready(function() {
						$("input").focus(function() {
							$(this).next().css({
								"visibility" : "visible"
							});
						});
						$("input").blur(function() {
							$(this).next().css({
								"visibility" : "hidden"
							});
						});
						
						<c:if test="${! empty success}">
						swal("Good job!", "你已成功更新資料!", "success");
						</c:if>

					});
					</script>
				</div>
			</div><!-- 9md -->
		</div>
	</div>
</div>
	<!-- register -->
	<!-- footer-->
	<jsp:include page="/front-end/footer.html" />
	<!-- footer-->
</body>
</html>
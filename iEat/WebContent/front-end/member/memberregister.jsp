<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Register</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/previewimage.js"></script>
<script src="<%=request.getContextPath()%>/js/address.js"></script>
<script src="http://code.jquery.com/jquery-1.12.3.min.js"></script>
<link href="<%=request.getContextPath()%>/css/memberprefecture.css"
	rel="stylesheet" type="text/css" />
<script>
$(document).ready(function() {
	$("#mem_acct").blur(function() {
		var mem_acct = $(this).val();
		
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath()%>/member/member.do",
				data : {action : "checkacct",mem_acct : mem_acct},
				dataType : "text",
				success : function(data) {
					if(data == "true"){
						$("#acctcheckstate").removeClass("glyphicon-remove");
						$("#acctcheckstate").addClass("glyphicon-ok");
					}
					if(data == "false"){
						$("#acctcheckstate").removeClass("glyphicon-ok");
						$("#acctcheckstate").addClass("glyphicon-remove");
					}
				},
				error : function() {
					alert("error");
				}
			

		});
	});
	
	$("#mem_email").blur(function() {
		var mem_email = $(this).val();
		
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath()%>/member/member.do",
				data : {action : "checkemail",mem_email : mem_email},
				dataType : "text",
				success : function(data) {
					if(data == "true"){
						$("#emailcheckstate").removeClass("glyphicon-remove");
						$("#emailcheckstate").addClass("glyphicon-ok");
					}
					if(data == "false"){
						$("#emailcheckstate").removeClass("glyphicon-ok");
						$("#emailcheckstate").addClass("glyphicon-remove");
					}
				},
				error : function() {
					alert("error");
				}
			

		});
	});
	
	$("#mem_phone").blur(function() {
		var mem_phone = $(this).val();
		
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath()%>/member/member.do",
				data : {action : "checkphone",mem_phone : mem_phone},
				dataType : "text",
				success : function(data) {
					if(data == "true"){
						$("#phonecheckstate").removeClass("glyphicon-remove");
						$("#phonecheckstate").addClass("glyphicon-ok");
					}
					if(data == "false"){
						$("#phonecheckstate").removeClass("glyphicon-ok");
						$("#phonecheckstate").addClass("glyphicon-remove");
					}
				},
				error : function() {
					alert("error");
				}
			

		});
	});
	
	$("#magical").click(function(){
		$("#mem_name").val("包先生");
		$("#mem_acct").val("mrbob101g4");
		$("#mem_pwd").val("123456");
		$("#mem_pwd_check").val("123456");
		$("#mem_email").val("mrbob101g4@gmail.com");
		$("#mem_phone").val("0982215706");
		$("#sex1").attr("checked",true); //設定打勾
		$("#mem_bd").val("1989-06-19");

		
	});

		$("input").focus(function() {
			$(this).parents(".form-group").children("p").css({
				"visibility" : "visible"
			});
		});
		$("input").blur(function() {
			$(this).parents(".form-group").children("p").css({
				"visibility" : "hidden"
			});
		});

});

</script>
</head>
<body>
	<c:if test="${not empty user }">
		<c:redirect url="/front-end/index.jsp"></c:redirect>
	</c:if>
	<!-- header -->
	<jsp:include page="/front-end/head.jsp" />
	<!-- header -->
	<!-- register -->
	<div class="main-1">
		<div class="container">	
						<div class="col-md-1"></div>
						<div class="col-md-10">
									<div class="panel panel-default">
				<div class="panel-body" style="padding:5%">
				<div id="process">
				<div class="step active">1輸入基本資料</div>
				&#10151;
				<div class="step">2信箱驗證</div>
				&#10151;
				<div class="step">3完成註冊</div>
			</div>

			<div class="well">
				<c:if test="${! empty errorMessage.elseError }">
					<div>
						<font color='red'>請修正以下錯誤:
							<p>${errorMessage.elseError}</p>
						</font>
					</div>
				</c:if>
				
				<form action="<%=request.getContextPath()%>/member/member.do" method=post enctype="multipart/form-data">
					<img class="col-sm-offset-2"
						src="<%=request.getContextPath()%>/images/user_default_full.jpg"
						id="picture" width="150" height="150" name="img">
					<div class="form-group">
						<label for="InputName" class="control-label col-sm-2">上傳大頭照</label>
						<div class="form-inline col-sm-10">
							<input type="file" id="photo" name="mem_photo">
						</div>
						<p class="bg-danger"
							style="visibility:${(empty errorMessage.mem_photo)?'hidden':'visible'};">
							<font color="#bb55ee">請選擇圖片格式的檔案</font>&nbsp;<font color="red">${errorMessage.mem_photo}</font>
						</p>

					</div>

					<div class="form-group">
						<label for="mem_name" class="control-label col-sm-2">姓名*</label>
						<div class="col-sm-10">
						<input type="text" class="form-control" id="mem_name" placeholder="Name"
							name="mem_name" value="${(empty errorMessage.mem_name)?(param.mem_name):''}" >

						<p class="bg-danger show"
							style="visibility:${(empty errorMessage.mem_name)?'hidden':'visible'};">
							<font color="#bb55ee">請輸入中文名</font>&nbsp;<font color="red">${errorMessage.mem_name}</font>
						</p>
						</div>

					</div>

					<div class="form-group  has-feedback">
						<label for="mem_acct" class="control-label col-sm-2">帳號*</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" placeholder="Account"
								name="mem_acct" id="mem_acct" value="${(empty errorMessage.mem_acct)?(param.mem_acct):''}">
							<span class="glyphicon  form-control-feedback" aria-hidden="true" id="acctcheckstate"></span>
						
						<p class="bg-danger"
							style="visibility:${(empty errorMessage.mem_acct)?'hidden':'visible'};">
							<font color="#bb55ee">請輸入英文或數字組合的6~15個字元</font>&nbsp;<font
								color="red">${errorMessage.mem_acct}</font>
						</p>
						</div>
					</div>

					<div class="form-group">
						<label for="mem_pwd" class="control-label col-sm-2">密碼*</label>
						<div class="col-sm-10">
						<input type="password"
							class="form-control" id="mem_pwd" placeholder="Password"
							name="mem_pwd" value="${(empty errorMessage.mem_pwd)?(param.mem_pwd):''}">
						<p class="bg-danger"
							style="visibility:${(empty errorMessage.mem_pwd)?'hidden':'visible'};">
							<font color="#bb55ee">請輸入英文或數字組合的6~15個字元</font>&nbsp;<font
								color="red">${errorMessage.mem_pwd}</font>
						</p>
						</div>
					</div>
					<div class="form-group">
						<label for="mem_pwd_check" class="control-label col-sm-2">確認密碼*</label>
						<div class="col-sm-10">
						<input type="password"
							class="form-control" id="mem_pwd_check" placeholder="Password"
							name="mem_pwd_check">
						<p class="bg-danger"
							style="visibility:${(empty errorMessage.mem_pwd_check)?'hidden':'visible'};">
							<font color="#bb55ee">請再一次輸入密碼</font>&nbsp;<font color="red">${errorMessage.mem_pwd_check}</font>
						</p>
						</div>
					</div>
					<div class="form-group has-feedback">
						<label for="InputEmail1" class="control-label col-sm-2">電子信箱*</label>
							<div class="col-sm-10">
							<input type="email" class="form-control" placeholder="Email"
								name="mem_email" id="mem_email" value="${(empty errorMessage.mem_email)?(param.mem_email):''}">
						<span class="glyphicon  form-control-feedback" aria-hidden="true" id="emailcheckstate"></span>
						<p class="bg-danger"
							style="visibility:${(empty errorMessage.mem_email)?'hidden':'visible'};">
							<font color="#bb55ee">請盡量不要使用免費信箱，以免收不到信件</font>&nbsp;<font
								color="red">${errorMessage.mem_email}</font>
						</p>
						</div>
					</div>
					<div class="form-group has-feedback">
						<label for="InputPhone" class="control-label col-sm-2">手機號碼*</label>
							<div class="col-sm-10">						
							<input type="tel" class="form-control" placeholder="Phone Number"
								name="mem_phone" id="mem_phone" value="${(empty errorMessage.mem_phone)?(param.mem_phone):''}">
							<span class="glyphicon  form-control-feedback" aria-hidden="true" id="phonecheckstate"></span>
						
						<p class="bg-danger"
							style="visibility:${(empty errorMessage.mem_phone)?'hidden':'visible'};">
							<font color="#bb55ee">請正確填寫</font>&nbsp;<font color="red">${errorMessage.mem_phone}</font>
						</p>
						</div>
					</div>

					<div class="form-group">
						<label for="mem_sex" class="control-label col-sm-2">性別*</label>
						<div class="form-inline col-sm-10">
							<label class="radio-inline"> <input type="radio"
								name="mem_sex" id="sex1" value="1"
								${(param.mem_sex==1)?'checked':''}> 男
							</label> <label class="radio-inline"> <input type="radio"
								name="mem_sex" id="sex2" value="2"
								${(param.mem_sex==2)?'checked':''}> 女
							</label>
						</div>
						<p class="bg-danger"
							style="visibility:${(empty errorMessage.mem_sex)?'hidden':'visible'};">
							<font color="#bb55ee">請正確填寫</font>&nbsp;<font color="red">${errorMessage.mem_sex}</font>
						</p>
					</div>

					<div class="form-group">
						<label for="mem_bd" class="control-label col-sm-2">出生年月日*</label>
						<div class="form-inline col-sm-10">
						<input type="date"
							class="form-control" id="mem_bd" name="mem_bd"
							value="${(empty errorMessage.mem_bd)?(param.mem_bd):''}">
						<p class="bg-danger"
							style="visibility:${(empty errorMessage.mem_bd)?'hidden':'visible'};">
							<font color="#bb55ee">請正確填寫</font>&nbsp;<font color="red">${errorMessage.mem_bd}</font>
						</p>
						</div>
					</div>

					<div class="form-group">
						<label for="InputAddr" class="control-label col-sm-2">地址</label>
						<div class="form-inline col-sm-10">
							<span>縣市</span> <select class="form-control" name="mem_addr1"
								id="zone1"></select> <input type="hidden" id="addr1"
								value="${mem_addr1}"> <span>鄉鎮[市]區</span> <select
								class="form-control" name="mem_addr2" id="zone2"></select> <input
								type="hidden" id="addr2" value="${mem_addr2}"> <input
								type="hidden" name="mem_zip" value="${param.mem_zip}"
								id="zipcode">
											</div>

					</div>
					<div class="form-group">
						<label for="InputAddr" class="control-label col-sm-2"></label>
						<div class="col-sm-10">
						<input type="text" class="form-control" name="mem_addr3" value="${mem_addr3}">
						</div>
					</div>
					<button type="submit" class="btn btn-success col-sm-offset-3">下一步</button>
					<font size="2" color="red"><span>*為必填</span></font> 
					<input type="hidden" name="action" value="memberregister">
					<input type="button" id="magical" class="btn btn-success" value="神奇小按鈕" style="margin-left:5em">
				</form>

			</div>
		</div>
	</div>
	</div>
						<div class="col-md-1"></div>
</div>
</div>
	<!-- register -->
	<!-- footer-->

	<jsp:include page="/front-end/footer.html" />
	<!-- footer-->
</body>
</html>

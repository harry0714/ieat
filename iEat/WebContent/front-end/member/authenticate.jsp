<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Register</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/previewimage.js"></script>
<link href="<%=request.getContextPath()%>/css/memberprefecture.css" rel="stylesheet" type="text/css" />
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
				<div class="step">1輸入基本資料</div>
				&#10151;
				<div class="step active">2信箱驗證</div>
				&#10151;
				<div class="step">3完成註冊</div>
			</div>
				<c:if test="${! empty errorMessage.elseError }">
					<div>
						<font color='red'>請修正以下錯誤:
							<p>${errorMessage.elseError}</p>
						</font>
					</div>
				</c:if>
				<div class="col-md-8">

				<div class="well">
				<form action="<%=request.getContextPath()%>/member/member.do"
					method=post>


					<div class="form-group">
						<label for="InputEmail1">電子信箱</label> <input type="text"
							class="form-control" placeholder="Email" name="mem_email"
							id="InputEmail1" value="${memberVO.mem_email}" readonly>

					</div>

					<div class="form-group">
						<label for="codes">驗證碼</label> <input type="text"
							class="form-control" placeholder="請輸入收到的驗證碼" name="codes"
							id="codes" readyonly>
						<p class="bg-danger">
							<font color="red">${errorMessage.codes}</font>
						</p>
					</div>

					<input type="hidden" name="action" value="authenticate">
					<button type="submit" class="btn btn-success">下一步</button>

					</form>
					</div>		
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
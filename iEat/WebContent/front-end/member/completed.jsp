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
<script src="<%=request.getContextPath()%>/js/address.js"></script>
<link href="<%=request.getContextPath()%>/css/memberprefecture.css"
	rel="stylesheet" type="text/css" />
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
					<div class="panel-body" style="padding: 5%">
						<div id="process">
							<div class="step">1輸入基本資料</div>
							&#10151;
							<div class="step">2信箱驗證</div>
							&#10151;
							<div class="step active"">3完成註冊</div>
						</div>
						<div>
							<font size="5" color=green><p>你已成功加入iEat會員，按下確認鍵返回首頁</p></font> <br>

							<a href="<%=request.getContextPath()%>/front-end/index.jsp"><button
									class="btn btn-success btn-lg">確認</button></a>


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
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=request.getContextPath()%>/css/memberprefecture.css"rel="stylesheet" type="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<title></title>
</head>
<body>
	<!-- head -->
	<jsp:include page="/front-end/head.jsp" />
	<!-- /head -->
	<!-- register -->
	<div class="main-1">
		<div class="container">
			<div class="col-md-3">
				<jsp:include page="/front-end/member/membermenu.jsp" />
			</div>
			<div class="col-md-9">
				<c:if test="${! empty errorMessage }">
					<font color='red'>請修正以下錯誤:
						<p>${errorMessage.elseError}</p>
					</font>
				</c:if>
			</div>
		</div>
	</div>

	<!-- /register -->
	<!-- footer-->
	<jsp:include page="/front-end/footer.html" />
	<!-- /footer-->
</body>
</html>
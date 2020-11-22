<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.store.model.*" %> 
<%
	StoreVO storeVO = (StoreVO) request.getAttribute("storeVO"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>iEat - MemberForgetPsw</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=request.getContextPath()%>/css/memberprefecture.css"rel="stylesheet" type="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<!-- Logo --><link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/eat.png" /><!-- Logo -->
<style>
.login_form{
	padding:20px;
	margin:10px;
}
</style>		
</head>
<body>
<c:if test="${not empty user }">
	<c:redirect url="/front-end/index.jsp"></c:redirect>
</c:if>
<c:if test="${not empty store }">
	<c:redirect url="/front-end/index.jsp"></c:redirect>
</c:if>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>iEat - 忘記密碼</title>
</head>
<body>
<!-- header -->
<jsp:include page="/front-end/head.jsp"/>
<!-- header -->

<!-- register -->
	<div class="main-1">
		<div class="container">
		
			<div id="process">
				<div class="step active">1: 輸入Email</div>
					&#10151;
				<div class="step">2: 收取信件</div>
					&#10151;				
				<div class="step">3: 取得新密碼</div>										
			</div>
			
			<div class="col-md-6">
				<div class="login_form" id="store_form">
					<form action="<%=request.getContextPath()%>/member/member.do" method="post">
						<div class="form-group">
							<label for="username">請輸入Email</label>
							<input type="text" class="form-control" id="username" placeholder="username" name="mem_email"  >
						</div>						
						<c:if test="${not empty errorMsgs }">
							<c:forEach var="errorMsg" items="${errorMsgs }">
								<font color='red'><p>${errorMsg }</p></font>
							</c:forEach>
						</c:if>						
						<input type="hidden" name="action" value="forgetPsw">
						<button type="submit" class="btn btn-default">送出查詢</button>
					</form>
					<a href="<%=request.getContextPath() %>/front-end/index.jsp">回首頁</a> | 
					<a href="<%=request.getContextPath() %>/front-end/member/memberregister.jsp">註冊為會員</a>
				</div>	
			</div>
		</div>
	</div>
<!-- register -->

<!-- footer-->
<jsp:include page="/front-end/footer.html"/>
<!-- footer-->	

</body>
</html>
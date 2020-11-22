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
<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath() %>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath() %>/css/sweetalert.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<script src="<%=request.getContextPath() %>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath() %>/js/simpleCart.min.js"> </script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<!-- Logo --><link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/eat.png" /><!-- Logo -->	
<title>iEat - 登入頁面</title>
<style>
.login_form{
	padding:20px;
	margin:10px;
}
</style>
<script type="text/javascript">
$(document).ready(function() {
	<c:if test="${not empty resetPsw}">
		swal("Success", "密碼已重設，請以新密碼登入", "success");
	</c:if>
	
	<c:if test="${not empty needStoreLogin}">
		swal("請先登入或註冊店家 再執行此動作!"); 
	</c:if>
	
	<c:if test="${not empty needLogin}">
		swal("請先登入為或註冊會員 再執行此動作!"); 
		<%session.removeAttribute("needLogin");%>
	</c:if>
});
</script>	
</head>
<body>
<!-- 若已是登入狀態 (店家or會員)  重導回首頁 -->
<c:if test="${not empty user }">
	<c:redirect url="/front-end/index.jsp"></c:redirect>
</c:if>

<c:if test="${not empty store }">
	<c:redirect url="/front-end/index.jsp"></c:redirect>
</c:if>

<!-- header -->
<jsp:include page="/front-end/head.jsp"/>
<!-- header -->
<!-- register -->
	<div class="main-1">
		<div class="container">

			<div class="col-md-6">
				<div class="login_form" id="store_form">
					<h3>店家登入</h3>
					<form action="<%=request.getContextPath()%>/store/store.do" method="post">
						<div class="form-group">
							<label for="username">帳號</label>
							<input type="text" class="form-control" id="username" placeholder="username" name="store_id"
							value="<%= (storeVO == null) ?  "" : storeVO.getStore_id() %>" >
						</div>
						<div class="form-group">
							<label for="password">密碼</label>
							<input type="password" class="form-control" id="password" placeholder="password" name="store_psw">
						</div>
						<c:if test="${not empty errorMsgs }">
							<c:forEach var="errorMsg" items="${errorMsgs }">
								<font color='red'><p>${errorMsg }</p></font>
							</c:forEach>
						</c:if>
						<input type="hidden" name="action" value="storeLogin">
						<button type="submit" class="btn btn-default">登入</button>
					</form>
					<a href="<%=request.getContextPath() %>/front-end/store/forgetPsw.jsp">忘記密碼?</a> | 
					<a href="<%=request.getContextPath() %>/front-end/store/storeRegister.jsp">註冊為店家</a>
				</div>	
			</div>
			<div class="col-md-6">
				<div class="login_form" id="mem_form">
					<h3>會員登入</h3>
					<form action="<%= request.getContextPath() %>/member/member.do" method="post">
						<div class="form-group">
							<label for="username">帳號</label>
							<input type="text" class="form-control" name="mem_acct" id="username" placeholder="username">
						</div>
						<div class="form-group">
							<label for="password">密碼</label>
							<input type="password" class="form-control" name="mem_pwd" id="password" placeholder="password">
						</div>						
						<c:if test="${not empty errorMessage}">
							<c:forEach var="message" items="${errorMessage}">
							<font color='red'><p>${message}</p></font>
							</c:forEach>
						</c:if>
						<button type="submit" class="btn btn-default" name="login" value="memberlogin">登入</button>
						<input type="hidden" name="action" value="memberlogin">
					</form>
					<a href="<%=request.getContextPath() %>/front-end/member/forgetPsw.jsp">忘記密碼?</a> | <a href="<%= request.getContextPath() %>/front-end/member/memberregister.jsp">註冊為會員</a>
				</div>	
			</div>	
			<div class="clearfix"> </div>

		</div>
	</div>


<!-- register -->	
<!-- footer-->
<jsp:include page="/front-end/footer.html"/>
<!-- footer-->	

</body>
</html>
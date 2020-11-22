<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.member.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="application/x-javascript">
	
	 addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } 

</script>
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/previewimage.js"></script>
<script src="<%=request.getContextPath()%>/js/address.js"></script>
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
						<font color='red'>
							<p>${errorMessage.elseError}</p>
						</font>
					</div>
				</c:if>
				<div class="panel panel-default">
					<div class="panel-heading" style="color:#d15a15;font-size:20px">修改認證信箱</div>
					<div class="panel-body" style="padding: 10%">
						<form action="<%=request.getContextPath()%>/member/member.do"
							method=post>


							<div class="form-group">
								<label for="InputEmail1">新電子信箱</label> <input type="text"
									class="form-control" placeholder="Email" name="mem_email_new"
									id="InputEmail1" value="${mem_email_new}" readonly>

							</div>

							<div class="form-group">
								<label for="codes">驗證碼</label> <input type="text"
									class="form-control" placeholder="請輸入收到的驗證碼" name="codes"
									id="codes" readyonly>
								<p class="bg-danger">
									<font color="red">${errorMessage.codes}</font>
								</p>
							</div>

							<input type="hidden" name="action" value="updateauthenticate">
							<button type="submit" class="btn btn-success">確認</button>

						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- register -->
	<!-- footer-->
	<jsp:include page="/front-end/footer.html" />
	<!-- footer-->
</body>
</html>
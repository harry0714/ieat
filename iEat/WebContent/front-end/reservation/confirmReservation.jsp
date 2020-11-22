<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.reservation.model.*" %>
<%
	ReservationVO reservationVO = (ReservationVO) session.getAttribute("reservationVO"); 
%>  
<!DOCTYPE html>
<html>
<head>
<title>iEat - 餐廳訂位</title>
<link href="<%= request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath() %>/css/jquery-ui.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath() %>/css/sweetalert.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
</head>
<body>
<!-- header -->
<jsp:include page="/front-end/head.jsp"/>
<!-- header -->


<!-- container -->
<div class="container" style="min-height:800px;margin-top:30px;">
		<!-- 店家左邊列表 -->
			<jsp:include page="/front-end/store/store_left_bar.jsp"/>
		<!-- 店家左邊列表 -->	
		<div id="process">
			<div class="step">1: 輸入訂位資訊</div>
				&#10151;
			<div class="step active">2: 確認訂位</div>
				&#10151;
			<div class="step">3: 訂位完成</div>
		</div>
		<br><br>
		
		<div class="col-md-8">
			<form method="post" action="<%=request.getContextPath() %>/reservation/reservation.do">
				<div class="col-md-9" >
						<div class="form-group">
							<label for="InputName">訂位日期：</label> 
							${reservationVO.reservation_date }
						</div>
						<div class="form-group">
							<label for="InputReservationTime">訂位時段*：</label>		
								${reservationVO.reservation_hour}:00~${reservationVO.reservation_hour+1}:00
						</div>
						
						<div class="form-group">
							<label for="InputGuests">訂位人數*：</label>
							${reservationVO.reservation_guests }	
							<p class="bg-danger show" style="visibility:${(empty errorMsgs.NoRemainings)?'hidden':'visible'};">
								<font color="red">${errorMsgs.NoRemainings }</font></p>
						</div>						
						<div>
							<a href="<%=request.getContextPath() %>/store/store.do?action=get_reservation_info&store_no=${storeVO.store_no}" class="btn btn-default btn-md active" role="button">修改</a>
							<a href="<%=request.getContextPath() %>/reservation/reservation.do?action=confirm_reservation" class="btn btn-primary btn-md active" role="button">確認訂位</a>
						</div>
						</div>
				</form>
			</div>			
	</div>	
<!-- container -->
<br>
<!-- footer -->
<jsp:include page="/front-end/footer.html"/>
<!-- footer -->  
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.reservation.model.*" %>    
<%@ page import="com.store.model.*" %>
<%
	ReservationVO reservationVO = (ReservationVO) request.getAttribute("reservationVO");
	StoreVO storeVO = (StoreVO) request.getAttribute("storeVO"); 
%>    
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath() %>/images/eat.png" />
<link href="<%= request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath() %>/css/jquery-ui.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath() %>/css/sweetalert.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%= request.getContextPath() %>/js/jquery.min.js"></script>
<script src="<%= request.getContextPath() %>/js/jquery-ui.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>

<!-- Logo --><link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/eat.png" /><!-- Logo -->
<title>iEat - 餐廳訂位</title>
<script>
// JQueryUI 日期選擇器
  $(function() {
		$("#datepicker").datepicker({
			defaultDate: "+1d",
			dateFormat : "yy-mm-dd",
			maxDate: "+1M" ,
			minDate: 0,
		});

  }); 
</script>
<script>
$(document).ready(function(){ //取得剩餘人數
	$("#reservation_hour").blur(function() {
		var reservation_hour = $(this).val(); 
		var reservation_date = $("#datepicker").val(); 
		var store_no = $("#store_no").val(); //送出店家編號
		// 用ajax到後端查詢資料
		$.ajax({
			type:"POST", 
			url:"<%=request.getContextPath()%>/reservation/reservation.do", 
			data : {
				action:"getRemaining", 
				store_no : store_no, 
				reservation_date : reservation_date, 
				reservation_hour : reservation_hour, }, 				
				dataType : "text", 
				success : function(data) {
						$("#remaining").val(data); 
				}, 
				error : function() {
					swal("請選擇訂位日期與時段!");
				}
		}); 
	}); 
}); 
</script>
<script type="text/javascript">
$(document).ready(function(){
	<c:if test="${not empty errorMsgs.NoRemainings}">
		swal("訂位人數已滿\n請改選擇其他時段");
	</c:if>
}); 	
</script>
</head>
<body>
<!-- header -->
	<jsp:include page="/front-end/head.jsp"/>
<!-- header -->  
<div class="main-1">
	<div class="container">
		<!-- 店家左邊列表 -->
			<jsp:include page="/front-end/store/store_left_bar.jsp"/>
		<!-- 店家左邊列表 -->	

		<div class="col-md-10">
				<div id="process">
			<div class="step active">1: 輸入訂位資訊</div>
				&#10151;
			<div class="step">2: 確認訂位</div>
				&#10151;
			<div class="step">3: 訂位完成</div>
		</div>
				<div class="col-md-9 col-sm-12">
					<c:if test="${not empty errorMsgs.elseError }">
						<div>
							<font color='red'>請修正以下錯誤: <p>${errorMsgs.elseError }</p></font>
						</div>
					</c:if>
					<br><br>
					<!-- 店家有開放訂位  才顯示Form表單 -->
					<c:if test="${(storeVO.store_booking).contains('1')}">
					<div class="panel panel-default">
					<div class="panel-heading" style="color:#d15a15;font-size:20px">線上訂位</div>
					<div class="panel-body" style="padding:5%">
					<form method="post" class="well"
						action="<%=request.getContextPath()%>/reservation/reservation.do">						
						<div class="form-group">
							<label for="InputName">請選擇訂位日期*</label> 
	
							<input type="text" id="datepicker" class="form-control" name="reservation_date" placeholder="date" value="${reservationVO.reservation_date}">
							<p class="bg-danger show" style="visibility:${(empty errorMsgs.reservation_date)? 'hidden' : 'visible'};">
								<font color="red">${errorMsgs.reservation_date }</font></p>
						</div>
						
						<div class="form-group">
							<label for="InputReservationTime">請選擇訂位時段*</label>		
								<select size="1" class="form-control" name="reservation_hour" id="reservation_hour">
									<option value="">請選擇</option>
<%-- 不知道為什麼forTokens會一直跳錯...... 所以改在 Controller那邊將字串切好 然後forEach取值 --%>
<%-- 錯誤訊息 The method setItems(String) in the type ForTokensTag is not applicable for the arguments (Object) --%>
<%-- 									<c:forTokens items="${(storeVO.store_booking)}" delims="-" var="booking" varStatus="varStatus"> --%>
<%-- 										<c:if test="${booking==1}"> --%>
<%-- 											<option value="${varStatus.index }" ${(reservationVO.reservation_hour==varStatus.index) ? 'selected' : '' }>${varStatus.index }:00~${varStatus.index+1}:00</option> --%>
<%-- 										</c:if> --%>
<%-- 									</c:forTokens> --%>
<%-- 用forEach取值  結果和用forTokens相同 --%>
									<c:forEach items="${store_booking }" var="booking" varStatus="varStatus">
										<c:if test="${booking==1 }">
											<option value="${varStatus.index }" ${(reservationVO.reservation_hour==varStatus.index) ? 'selected' : '' }>${varStatus.index }:00~${varStatus.index+1}:00</option>
										</c:if>
									</c:forEach>
								</select>	
								<p class="bg-danger show" style="visibility:${(empty errorMsgs.reservation_hour)?'hidden':'visible'};">
								<font color="red">${errorMsgs.reservation_hour }</font></p>												
						</div>
						
						<div class="form-group">
							<label for="InputGuests">請輸入訂位人數*</label>
								<div class="input-group">
									<input type="text" class="form-control" style="z-index:1" placeholder="Guests" name="reservation_guests" id="InputGuests" maxlength=2 value="${reservationVO.reservation_guests }">
 										<font color="orange">剩餘人數：<input name="remaining" type="text" id="remaining" size="2" value="${remaining }"  readonly></font> 
									<p class="bg-danger show" style="visibility:${(empty errorMsgs.reservation_guests)?'hidden':'visible'};">
									<font color="red">${errorMsgs.reservation_guests }</font></p>										
								</div>		
						</div>
						<input type="hidden" id="store_no" name="store_no" value="${storeVO.store_no }">						
						<input type="hidden" name="action" value="add_Reservation">
						<button type="submit" class="btn btn-success" id="RegisterBtn">訂位</button>					
					</form>
					</div>
				</div>				
				</c:if>
			</div>			
		<div class="clearfix"></div>
	</div>
</div> <!-- container -->	
</div>
<!-- footer-->
	<jsp:include page="/front-end/footer.html"/>
<!-- footer-->	
</body>
</html>
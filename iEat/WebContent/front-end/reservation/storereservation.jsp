<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.tools.*"%>

<%@ page import="com.reservation.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.text.SimpleDateFormat"%>
<jsp:useBean id="memberSvc" scope="page"
	class="com.member.model.MemberService" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/sweetalert.css">
<link href="<%=request.getContextPath()%>/css/memberprefecture.css"
	rel="stylesheet" type="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/previewimage.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<!-- Logo -->
<link rel="shortcut icon" type="image/x-icon"
	href="<%=request.getContextPath()%>/images/eat.png" />
<!-- Logo -->
<title>iEat - 店家頁面 店家訂位管理</title>
</head>
<body>
	<!-- header -->
	<jsp:include page="/front-end/head.jsp" />
	<!-- header -->
	<!-- register -->
	<div class="main-1">
		<div class="container">
			<div class="col-md-3">
				<jsp:include page="/front-end/store/storeMenu.jsp" />
			</div>
			<div class="col-md-8">
				<c:if test="${! empty errorMessage.elseError }">
					<div>
						<font color='red'>
							<p>${errorMessage.elseError}</p>
						</font>
					</div>
				</c:if>

				<div class="panel panel-default">
					<div class="panel-heading" style="color: #d15a15; font-size: 20px">訂位紀錄</div>
						<table class="table table-bordered">
							<thead>
								<tr>
									<th>#</th>
									<th>訂位編號</th>
									<th>會員姓名</th>
									<th>用餐日期</th>
									<th>訂位時段</th>
									<th>訂位人數</th>
									<th>訂位狀態</th>
									<th>訂位成立日期</th>
								</tr>
							</thead>

							<tbody>
								<c:forEach var="reservationVO" items="${reservationList}"
									varStatus="s">
									<c:set var="reservationVO" value="${reservationVO}" />
									<%
										ReservationVO reservationVO = (ReservationVO) pageContext.getAttribute("reservationVO");
											SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
											SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
											String reservation_date = sdf2.format(reservationVO.getReservation_date());
											String reservation_time = sdf1.format(reservationVO.getReservation_time());

											String reservation_qrcode_status = null;
											switch (reservationVO.getReservation_qrcode_status().charAt(0)) {
												case '0' :
													reservation_qrcode_status = "已完成";
													break;
												case '1' :
													reservation_qrcode_status = "已成立";
													break;
												case '2' :
													reservation_qrcode_status = "逾期";
													break;
												case '3' :
													reservation_qrcode_status = "已取消";
													break;

											}
											pageContext.setAttribute("reservation_date", reservation_date);
											pageContext.setAttribute("reservation_time", reservation_time);
											pageContext.setAttribute("reservation_qrcode_status", reservation_qrcode_status);
									%>
									<tr
										class="${((reservationVO.reservation_no==param.reservation_no))?'info':((s.index%2)==0?'success':'') }">
										<td scope="row" style="vertical-align: middle;">${s.count}</td>
										<td class="reservation_no" style="vertical-align: middle;">${reservationVO.reservation_no}</td>
										<td style="vertical-align: middle;"><c:forEach
												var="memberVO" items="${memberSvc.all}">
												<c:if test="${reservationVO.mem_no==memberVO.mem_no}">
	                   						 ${memberVO.mem_name}
                    					</c:if>
											</c:forEach></td>
										<td style="vertical-align: middle;">${reservation_date }</td>
										<td style="vertical-align: middle;">${reservationVO.reservation_hour }點</td>
										<td style="vertical-align: middle;">${reservationVO.reservation_guests }</td>
										<td style="vertical-align: middle;">${reservation_qrcode_status }</td>
										<td style="vertical-align: middle;">${reservation_time}</td>										
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>

			</div>
		</div>
		<script type="text/javascript">
		$(function(){
		 	$(".btn").click(function (){
		 		var reservation_no = $(this).parent().siblings("td.reservation_no").text();
		 		swal({   
		 			title:"",
		 			allowOutsideClick:true,
		 			imageUrl:"<%=request.getContextPath()%>/reservation/reservationImageReader.do?reservation_no="
												+ reservation_no,
										imageSize : "200x200",
										showConfirmButton : false
									});
								});
			})
		</script>
	<!-- register -->
	<!-- footer-->
	<jsp:include page="/front-end/footer.html" />
	<!-- footer-->
</body>
</html>
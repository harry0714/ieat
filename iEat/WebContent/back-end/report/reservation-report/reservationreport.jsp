<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.reservation.model.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="com.store.model.*"%>
<%@ page import="java.text.SimpleDateFormat"%>

<jsp:useBean id="memberSvc" scope="page"
	class="com.member.model.MemberService" />
<jsp:useBean id="storeSvc" scope="page"
	class="com.store.model.StoreService" />

<%
	List<MemberVO> memberList = memberSvc.getAll();
	List<StoreVO> storeList = storeSvc.getAll();
	pageContext.setAttribute("memberList", memberList);
	pageContext.setAttribute("storeList", storeList);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- CSS -->
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/jquery-ui.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/sweetalert.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/metisMenu.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/timeline.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/sb-admin-2.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/morris.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/ie10-viewport-bug-workaround.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/dashboard.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/c3.css" rel="stylesheet" type="text/css">
<!-- CSS -->

<!-- JS -->  
<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>
<script src="<%=request.getContextPath()%>/js/d3.min.js" charset="utf-8"></script>
<script src="<%=request.getContextPath()%>/js/c3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script src="<%=request.getContextPath()%>/js/indexsocket.js"></script>
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>
<script type="text/javascript" src="https://maps.google.com/maps/api/js?key=AIzaSyANhvv_DDadWfAzIGurcl8fZixWAdfrgQk"></script>
<!-- JS -->

<!-- Logo --><link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/eat.png" />
<title>iEat - 後端訂位檢舉管理</title>
<script>
$(document).ready(function() {
	
	$("#status_selected").change(function(){
		$("#statusform").submit();
	});
});
</script>
</head>
<body>
	<!-- header -->
<%@ include file="/back-end/page/head.jsp" %> 
	<!-- header -->

	<div class="container-fluid">
		<div class="row"></div>
		
<!-- SideBar --><%@ include file="/back-end/page/body.jsp" %><!-- SideBar --> 
		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<c:if test="${not empty errorMessage}">
				<font color='red'>請修正以下錯誤:
					<ul>
						<c:forEach var="message" items="${errorMessage}">
							<li>${message}</li>
						</c:forEach>
					</ul>
				</font>
			</c:if>
			<div class="panel panel-default">
				<div class="panel-heading">
					
					<h2>訂位檢舉</h2>
				</div>
				<table class="table">
					<thead>
						<form action="<%=request.getContextPath()%>/reservation_report/reservation_report.do" id="statusform" method="post">
							<input type="hidden" name=action value="enterreservationreport">
							<tr>
								<th>篩選:
								
									<select name="status_selected" id="status_selected">
										<option value="" ${(param.status_selected == "")?"selected": ""}>全部</option>
										<option value="0" ${(param.status_selected == "0")?"selected": ""}>未審核</option>
										<option value="1" ${(param.status_selected == "1")?"selected": ""}>通過</option>
										<option value="2" ${(param.status_selected == "2")?"selected": ""}>不通過</option>
									</select>
								</th>
							</tr>
						</form>
						<tr>
							<th>#</th>
							<th>訂位編號</th>
							<th>餐廳編號/名稱</th>
							<th>會員編號/姓名</th>
							<th>用餐日期</th>
							<th>訂位時段</th>
							<th>訂位人數</th>
							<th>訂位狀態</th>
							<th>訂位成立日期</th>
							<th>QRcode</th>
							<th>檢舉內容</th>
							<th>審核狀態</th>
							<th colspan="2">審核</th>

						</tr>
					</thead>
					<tbody>
						<c:forEach var="reservationVO" items="${reservationList}" varStatus="s">
							<c:set var="reservationVO" value="${reservationVO}" />
							<%
							ReservationVO reservationVO = (ReservationVO)pageContext.getAttribute("reservationVO");
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
							String reservation_date = sdf2.format(reservationVO.getReservation_date());
							String reservation_time = sdf1.format(reservationVO.getReservation_time());
							
							String reservation_qrcode_status = null;
							switch(reservationVO.getReservation_qrcode_status().charAt(0)){
								case '0':
									reservation_qrcode_status = "已完成";
									break;
								case '1':
									reservation_qrcode_status = "已成立";
									break;
								case '2':
									reservation_qrcode_status = "逾期";
									break;
								case '3':
									reservation_qrcode_status = "已取消";
									break;
									
							}
							pageContext.setAttribute("reservation_date",reservation_date);
							pageContext.setAttribute("reservation_time",reservation_time);
							pageContext.setAttribute("reservation_qrcode_status",reservation_qrcode_status);
								
									String reservation_report_status = null;
									switch (reservationVO.getReservation_report_status().charAt(0)) {
									case '0':
										reservation_report_status = "未審核";
										break;
									case '1':
										reservation_report_status = "通過";
										break;
									case '2':
										reservation_report_status = "不通過";
										break;
									}
									pageContext.setAttribute("reservation_report_status", reservation_report_status);
							%>
							<tr
								class="${((reservationVO.reservation_no==param.reservation_no))?'info':((s.index%2)==0?'success':'') }">
								<td scope="row" style="vertical-align:middle;">${s.count}</td>
								<td class="reservation_no" style="vertical-align:middle;">${reservationVO.reservation_no}</td>
								<td style="vertical-align:middle;"><c:forEach var="storeVO" items="${storeSvc.all}">
										<c:if test="${reservationVO.store_no==storeVO.store_no}">
											${reservationVO.store_no }<br/>
	                   						 ${storeVO.store_name}
                    					</c:if>
								</c:forEach></td>
								<td style="vertical-align:middle;"><c:forEach var="memberVO" items="${memberSvc.all}">
										<c:if test="${reservationVO.mem_no==memberVO.mem_no}">
											${reservationVO.mem_no }<br/>
	                   						 ${memberVO.mem_name}
                    					</c:if>
								</c:forEach></td>
								<td style="vertical-align:middle;">${reservation_date }</td>
								<td style="vertical-align:middle;">${reservationVO.reservation_hour }點</td>
								<td style="vertical-align:middle;">${reservationVO.reservation_guests }</td>
								<td style="vertical-align:middle;">${reservation_qrcode_status }</td>
								<td style="vertical-align:middle;">${reservation_time}</td>
								
								<td><button class="btn imageread ${((s.index%2)==0)?'bg-success':'btn-default' }"><span class="glyphicon glyphicon-picture"></span></button></td>
								
								<td style="vertical-align: middle">${reservationVO.reservation_report }</td>
								
								<td style="vertical-align: middle">${reservation_report_status }</td>

								<td style="vertical-align: middle"><c:if
										test="${reservationVO.reservation_report_status == '0'}">
										<form action="<%=request.getContextPath()%>/reservation_report/reservation_report.do" method="post">
											<input type="hidden" name="action" value="checkpass">
											<input type="hidden" name="reservation_no" value="${reservationVO.reservation_no}">
											<input type="hidden" name="store_no" value="${reservationVO.store_no}">
											<input type="hidden" name="mem_no" value="${reservationVO.mem_no}">
											<input type="hidden" name="status_selected" value="${param.status_selected }">
											<button type="submit" class="btn btn-success">通過</button>
										</form>
									</c:if></td>
								<td style="vertical-align: middle"><c:if
										test="${reservationVO.reservation_report_status == '0'}">
										<form
											action="<%=request.getContextPath()%>/reservation_report/reservation_report.do" method="post">
											<input type="hidden" name="action" value="checkfail">
											<input type="hidden" name="reservation_no"
												value="${reservationVO.reservation_no}">
											<input type="hidden" name="status_selected" value="${param.status_selected }">
											<button type="submit" class="btn btn-success">不通過</button>
										</form>
									</c:if></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<script type="text/javascript">
					$(function(){
		 				$(".imageread").click(function (){
		 					var reservation_no = $(this).parent().siblings("td.reservation_no").text();
		 					swal({   
		 						title:"",
		 						allowOutsideClick:true,
		 						imageUrl:"<%=request.getContextPath()%>/reservation/reservationImageReader.do?reservation_no="+reservation_no,	
		 						imageSize:"200x200",
		 						showConfirmButton:false
		 					});
						});
					})
	</script>
			</div>
		</div>
	</div>
</body>

<%
	memberList = null;
	storeList = null;
%>
</html>
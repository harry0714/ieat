<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.tools.*"%>

<%@ page import="com.reservation.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.tools.StatusToString"%>
<jsp:useBean id="storeSvc" scope="page"
	class="com.store.model.StoreService" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/previewimage.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/sweetalert.css">
<link href="<%=request.getContextPath()%>/css/memberprefecture.css" rel="stylesheet" type="text/css" />
<style>
table{
overflow-x:auto;
}
th{
white-space:nowrap;
}
</style>
</head>
<body>

	<jsp:include page="/front-end/head.jsp" />
	<!-- header -->
	<!-- register -->
	<div class="main-1">
		<div class="container">
			<div class="col-md-3">
			<jsp:include page="/front-end/member/membermenu.jsp" />
		</div>

				<c:if test="${! empty errorMsgs }">
					<div>
						<font color='red'>
							<p>${errorMsgs}</p>
						</font>
					</div>
				</c:if>
				
				<div class="col-md-9">	
				<div class="panel panel-default">
					<div class="panel-heading" style="color:#d15a15;font-size:20px">訂位紀錄<%@ include file="page1.file"%></div>

				<table class="table table-bordered">

					<thead>
						<tr>
							<th>#</th>
							<th>訂位編號</th>
							<th>餐廳名稱</th>
							<th>用餐日期</th>
							<th>訂位時段</th>
							<th>訂位人數</th>
							<th>訂位狀態</th>
							<th>訂位成立日期</th>
							<th>取消訂位</th>
							<th>QRcode</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach var="reservationVO" items="${reservationList}" varStatus="s"
							begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
							<c:set var="reservationVO" value="${reservationVO}" />
							<%
								ReservationVO reservationVO = (ReservationVO)pageContext.getAttribute("reservationVO");
								SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
								SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
								String reservation_date = sdf2.format(reservationVO.getReservation_date());
								String reservation_time = sdf1.format(reservationVO.getReservation_time());
								
								String reservation_qrcode_status = StatusToString.getReservationQRcodeStatusString(reservationVO.getReservation_qrcode_status());
								
								pageContext.setAttribute("reservation_date",reservation_date);
								pageContext.setAttribute("reservation_time",reservation_time);
								pageContext.setAttribute("reservation_qrcode_status",reservation_qrcode_status);
							%>
							<tr class="${((reservationVO.reservation_no==param.reservation_no))?'info':((s.index%2)==0?'success':'') }" >
								<td scope="row" style="vertical-align:middle;">${s.index+1}</td>
								<td class="reservation_no" style="vertical-align:middle;">${reservationVO.reservation_no}</td>
								<!-- 取得店家名稱 -->
								<td style="vertical-align:middle;">
									<c:forEach var="storeVO" items="${storeSvc.all}">
											<c:if test="${reservationVO.store_no==storeVO.store_no}">
	                   							 ${storeVO.store_name}
                    						</c:if>
									</c:forEach>
								</td>
								<td style="vertical-align:middle;">${reservation_date }</td>
								<td style="vertical-align:middle;">${reservationVO.reservation_hour }點</td>
								<td style="vertical-align:middle;">${reservationVO.reservation_guests }</td>
								<td style="vertical-align:middle;">${reservation_qrcode_status }</td>
								<td style="vertical-align:middle;">${reservation_time}</td>
								<td style="vertical-align:middle;">
									<c:if test="${reservationVO.getReservation_qrcode_status() == '1' }">
										<form method="post" action="<%=request.getContextPath()%>/reservation/reservation.do" >
											<input type="hidden" name="action" value="cancel"> 
											<input type="hidden" name="reservation_no" value="${reservationVO.reservation_no}">
											<input type="hidden" name="whichPage" value="<%=whichPage%>">
											<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>">
											<a class="cancel"href="#">取消</a>
										</form>
									</c:if>
									<c:if test="${reservationVO.getReservation_qrcode_status() != '1' }">
										--
									</c:if>
								</td>
								<td><button class="btn  ${((s.index%2)==0)?'bg-success':'btn-default' }"><span class="glyphicon glyphicon-picture"></span></button></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<%@ include file="page2.file"%>
			
		</div>
		<script type="text/javascript">
		$(function(){
		 	$(".btn").click(function (){
		 		var reservation_no = $(this).parent().siblings("td.reservation_no").text();
		 		swal({   
		 			title:"",
		 			allowOutsideClick:true,
		 			imageUrl:"<%=request.getContextPath()%>/reservation/reservationImageReader.do?reservation_no="+reservation_no,	
		 			imageSize:"200x200",
		 			showConfirmButton:false
		 		});
			});
		 	
		 	$(".cancel").click(function(){
				var cancel = $(this);
				swal({   
					title: "",   
					text: "確定要取消訂位",   
					type: "warning",   
					showCancelButton: true,   
					closeOnConfirm: false,
					confirmButtonText:"是",
					confirmButtonColor: "#DD6B55",
					cancelButtonText:"否"
					}, 
					function(){			
							cancel.parent().submit();								
				});
			});
		})
	</script>
	</div>
</div>
</div>


	<!-- register -->
	<!-- footer-->
	<jsp:include page="/front-end/footer.html" />
	<!-- footer-->
</body>
</html>
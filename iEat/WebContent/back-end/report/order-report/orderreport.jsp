<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ord.model.*"%>
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
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
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

<title>iEat - 後端訂餐檢舉管理</title>
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
	<!-- SideBar -->
	<%@ include file="/back-end/page/body.jsp" %> 
	<!-- SideBar -->
	<div class="container-fluid">
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
					
					<h2>訂餐檢舉</h2>
				</div>
				<table class="table">
					<thead>
						<form action="<%=request.getContextPath()%>/ord_report/ord_report.do" id="statusform" method="post">
							<input type="hidden" name=action value="enterorderreport">
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
							<th>訂餐編號</th>
							<th>餐廳編號/名稱</th>
							<th>會員編號/名稱</th>
							<th>訂餐時間</th>
							<th>取餐時間</th>
							<th>付款狀態</th>
							<th>訂餐狀態</th>
							<th>明細</th>
							<th>QRcode</th>
							<th>檢舉內容</th>
							<th>審核狀態</th>
							<th colspan="2">審核</th>

						</tr>
					</thead>
					<tbody>

						<c:forEach var="ordVO" items="${ordList}" varStatus="s">
							<c:set var="ordVO" value="${ordVO}" />
							<%
								OrdVO ordVO = (OrdVO) pageContext.getAttribute("ordVO");
									SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
									SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
									String ord_date = sdf1.format(ordVO.getOrd_date());
									String ord_pickup = sdf2.format(ordVO.getOrd_pickup());
									pageContext.setAttribute("ord_date", ord_date);
									pageContext.setAttribute("ord_pickup", ord_pickup);

									String ord_paid = null;
									switch (ordVO.getOrd_paid()) {
										case 0 :
											ord_paid = "已付款";
											break;
										case 1 :
											ord_paid = "未付款";
											break;
									}
									pageContext.setAttribute("ord_paid", ord_paid);

									String ord_qrcode_status = null;
									switch (ordVO.getOrd_qrcode_status().charAt(0)) {
										case '0' :
											ord_qrcode_status = "已完成";
											break;
										case '1' :
											ord_qrcode_status = "逾期";
											break;
										case '2' :
											ord_qrcode_status = "店家以確認";
											break;
										case '3' :
											ord_qrcode_status = "已取消";
											break;
										case '4' :
											ord_qrcode_status = "待確認";
											break;
									}
									pageContext.setAttribute("ord_qrcode_status", ord_qrcode_status);
									
									String ord_report_status = null;
									switch (ordVO.getOrd_report_status().charAt(0)) {
									case '0':
										ord_report_status = "未審核";
										break;
									case '1':
										ord_report_status = "通過";
										break;
									case '2':
										ord_report_status = "不通過";
										break;
									}
									pageContext.setAttribute("ord_report_status", ord_report_status);
							%>
							<tr class="ord ${(ordVO.ord_no==param.ord_no)?'info':((s.index%2)==0?'success':'') }" >
								<td scope="row" style="vertical-align:middle;">${s.count}</td>
								<td class="ord_no" style="vertical-align:middle;">${ordVO.ord_no}</td>
								<td style="vertical-align:middle;"><c:forEach var="storeVO" items="${storeList}">
										<c:if test="${ordVO.store_no==storeVO.store_no}">
	                   						 ${ordVO.store_no}<br/>
	                   						 ${storeVO.store_name}
                    					</c:if>
									</c:forEach></td>
								<td style="vertical-align:middle;"><c:forEach var="memberVO" items="${memberList}">
										<c:if test="${ordVO.mem_no==memberVO.mem_no}">
	                   						 ${ordVO.mem_no}<br/>
	                   						 ${memberVO.mem_name}
                    					</c:if>
									</c:forEach></td>
								<td style="vertical-align:middle;">${ord_date }</td>
								<td style="vertical-align:middle;">${ord_pickup }</td>
								<td style="vertical-align:middle;">${ord_paid }</td>
								<td style="vertical-align:middle;">${ord_qrcode_status}</td>
								<td style="vertical-align:middle;"><button class="btn details ${((s.index%2)==0)?'bg-success':'btn-default' }"><span class="glyphicon glyphicon-list-alt"></span></button></td>
								<td style="vertical-align:middle;"><button class="btn qrcode ${((s.index%2)==0)?'bg-success':'btn-default' }"><span class="glyphicon glyphicon-picture"></span></button></td>
								<td style="vertical-align:middle;">${ordVO.ord_report}</td>
								<td style="vertical-align:middle;">${ord_report_status }</td>
								<td style="vertical-align: middle"><c:if
										test="${ordVO.ord_report_status == '0'}">
										<form action="<%=request.getContextPath()%>/ord_report/ord_report.do" method="post">
											<input type="hidden" name="action" value="checkpass">
											<input type="hidden" name="ord_no" value="${ordVO.ord_no}">
											<input type="hidden" name="store_no" value="${ordVO.store_no}">
											<input type="hidden" name="mem_no" value="${ordVO.mem_no}">
											<input type="hidden" name="status_selected" value="${param.status_selected }">
											<button type="submit" class="btn btn-success">通過</button>
										</form>
									</c:if></td>
								<td style="vertical-align: middle"><c:if
										test="${ordVO.ord_report_status == '0'}">
										<form
											action="<%=request.getContextPath()%>/ord_report/ord_report.do" method="post">
											<input type="hidden" name="action" value="checkfail">
											<input type="hidden" name="ord_no"
												value="${ordVO.ord_no}">
											<input type="hidden" name="status_selected" value="${param.status_selected }">
											<button type="submit" class="btn btn-success">不通過</button>
										</form>
									</c:if></td>
							</tr>
							<tr class="ord_meal ${((s.index%2)==0)?'success':'' }" hidden>
								<td colspan="14">
									<div class="panel panel-default">
										<div class="panel-heading">明細</div>
										<table class="table">
											<thead>
												<tr>
													<th>#</th>
													<th>餐點名稱</th>
													<th>單價</th>
													<th>數量</th>
													<th>商品總額</th>
												</tr>
											</thead>
											<tbody class="ord_meal_in">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<script>
				$(document).ready(function() {
					$(".details").click(function() {
						$(this).parents(".ord").next().toggle("fast");
						var ord_no = $(this).parent().siblings("td.ord_no").text();
						var whoclick = $(this).parents(".ord").next().find(".ord_meal_in");
						
						if(jQuery.trim(whoclick.text()) == ""){
							$.ajax({
								type:"POST",
								url:"<%=request.getContextPath()%>/ord/ord.do",
								data : {action : "getOrdMeal",ord_no1:ord_no},
								dataType : "json",
								success : function(data) {
								drawTable(data,whoclick);
								},
								error : function() {
									alert("error");
								}
							});
						}
					});
					
					//QRcode顯示     還沒做好
					$(".qrcode").click(function (){
				 		var ord_no = $(this).parent().siblings("td.ord_no").text();
				 		swal({   
				 			title:"",
				 			allowOutsideClick:true,
				 			imageUrl:"<%=request.getContextPath()%>/ord/ordImageReader.do?ord_no="+ord_no,	
				 			imageSize:"200x200",
				 			showConfirmButton:false
				 		});
					});

				});
					function drawTable(oJson, whoclick) {
						var i = 0;
						var totalprice = 0;
						$.each(oJson, function() {

							addTableRow(oJson[i].ord_meal_no, oJson[i].ord_no,
									oJson[i].meal_no, oJson[i].ord_meal_qty,
									oJson[i].ord_meal_price,
									oJson[i].meal_name, whoclick, (i + 1));

							totalprice += oJson[i].ord_meal_qty
									* oJson[i].ord_meal_price;
							i++;
						});
						var text = "<tr><td colspan='5' align='right'><font size='3'>總金額 :  "
								+ totalprice + " 元</font></td></tr>";
						whoclick.append(text);
					}
					function addTableRow(ord_meal_no, ord_no, meal_no,
							ord_meal_qty, ord_meal_price, meal_name, whoclick,
							i) {
						var oTable = whoclick;

						if (i % 2 == 0) {
							var text = "<tr><td>" + i + "</td><td>" + meal_name
									+ "</td><td>" + ord_meal_price
									+ "</td><td>" + ord_meal_qty + "</td><td>"
									+ (ord_meal_price * ord_meal_qty)
									+ "</td></tr>";
						} else {
							var text = "<tr class='info'><td>" + i
									+ "</td><td>" + meal_name + "</td><td>"
									+ ord_meal_price + "</td><td>"
									+ ord_meal_qty + "</td><td>"
									+ (ord_meal_price * ord_meal_qty)
									+ "</td></tr>";
						}

						oTable.append(text);
					}
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
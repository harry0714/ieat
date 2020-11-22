<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>

<%@ page import="com.ord.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.tools.StatusToString"%>

<jsp:useBean id="storeSvc" scope="page" class="com.store.model.StoreService" />
<jsp:useBean id="memberSvc" scope="page" class="com.member.model.MemberService"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/sweetalert.css">
<link href="<%=request.getContextPath()%>/css/memberprefecture.css" rel="stylesheet" type="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<!-- Logo --><link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/eat.png" /><!-- Logo -->
<title>iEat - 店家頁面 店家訂餐管理</title>
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
				<div class="panel-heading" style="color:#d15a15;font-size:20px">訂餐紀錄<%@ include file="page1.file"%></div>
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>#</th>
							<th>訂餐編號</th>
							<th>會員姓名</th>
							<th>訂餐時間</th>
							<th>取餐時間</th>
							<th>付款狀態</th>
							<th>訂餐狀態</th>
							<th>確認訂單</th>
							<th>取消訂單</th>
							<th>明細</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach var="ordVO" items="${ordList}" varStatus="s" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
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
											ord_qrcode_status = "已確認";
											break;
										case '2' :
											ord_qrcode_status = "逾期";
											break;
										case '3' :
											ord_qrcode_status = "已取消";
											break;
										case '4' :
											ord_qrcode_status = "待確認";
											break;
									}
									pageContext.setAttribute("ord_qrcode_status", ord_qrcode_status);
							%>
							<tr class="ord ${(ordVO.ord_no==param.ord_no)?'info':((s.index%2)==0?'success':'') }" >
								<td scope="row" style="vertical-align:middle;">${s.count}</td>
								<td class="ord_no" style="vertical-align:middle;">${ordVO.ord_no}</td>
								<td style="vertical-align:middle;"><c:forEach var="memberVO" items="${memberSvc.all}">
										<c:if test="${ordVO.mem_no==memberVO.mem_no}">
	                   						 ${memberVO.mem_name}
                    					</c:if>
									</c:forEach></td>
								<td style="vertical-align:middle;">${ord_date }</td>
								<td style="vertical-align:middle;">${ord_pickup }</td>
								<td style="vertical-align:middle;">${ord_paid }</td>
								<td style="vertical-align:middle;">${ord_qrcode_status}</td>
								<td style="vertical-align:middle;">
									<c:if test="${ordVO.ord_qrcode_status == '4'}">
									 
									 <a href="<%=request.getContextPath()%>/ord/ord.do?action=enterOrd&ord_no=${ordVO.ord_no}&whichPage=<%=whichPage%>&requestURL=<%=request.getServletPath()%>">確認訂單</a>
									
									</c:if>
									<c:if test="${ordVO.ord_qrcode_status != '4'}">
									 --
									</c:if>
								</td>
								<td style="vertical-align:middle;">
									<c:if test="${ordVO.ord_qrcode_status == '4'}">
									 
									
									 <a href="<%=request.getContextPath()%>/ord/ord.do?action=cancelOrd&ord_no=${ordVO.ord_no}&whichPage=<%=whichPage%>&requestURL=<%=request.getServletPath()%>">取消訂單</a>
									</c:if>
									<c:if test="${ordVO.ord_qrcode_status != '4'}">
									 --
									</c:if>
								</td>
								<td style="vertical-align:middle;"><button class="btn details ${((s.index%2)==0)?'bg-success':'btn-default' }"><span class="glyphicon glyphicon-list-alt"></span></button></td>
							</tr>
							<tr class="ord_meal ${((s.index%2)==0)?'success':'' }" hidden>
								<td colspan="10">
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
				<%@ include file="page2.file"%>
				<script>
				$(document).ready(function() {
					$(".cancel").click(function(){
						var cancel = $(this);
						swal({   
							title: "",   
							text: "確定要取消訂餐", 
							type: "warning",   
							showCancelButton: true,   
							closeOnConfirm: false,

							confirmButtonText:"YES",
							cancelButtonText:"NO"
							}, 
							function(){			
									cancel.parent().submit();								
						});
					});
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
				 			imageUrl:"<%=request.getContextPath()%>/ord/ordImageReader.do?ord_no="+ ord_no,
							imageSize : "200x200",
							showConfirmButton : false
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
				});
				</script>
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
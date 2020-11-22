<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.* , com.meal.model.*" %>
<%@ page import="java.util.* , com.store.model.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>iEat - 結帳</title>
<link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath()%>/images/iEat_logo.png" />
<link href="<%= request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>	

</head>
<body>
<%Map<String,Map<String,Integer>> shoppinglist = (Map<String,Map<String,Integer>>) session.getAttribute("shoppinglist");%>
<%System.out.println("shoppinglistCart="+shoppinglist); %>
<jsp:useBean id="storeSvc" scope="page" class="com.store.model.StoreService"/>
<jsp:useBean id="mealSvc" scope="page" class="com.meal.model.MealService"/>
<!-- header -->
<jsp:include page="/front-end/head.jsp"/>
<!-- header -->
<!-- checkout -->
<div class="main-1">
	<div class="container">

		<!-- process -->	
		<div class="col-md-1"></div>
			<div class="col-md-10">
			<c:if test="${shoppinglist==null||shoppinglist.size()==0}">
			<h3>尚無訂單</h3>
			</c:if>
			<c:if test="${shoppinglist.size()>0}" var="order">
				<div class="panel panel-default">
				<div class="panel-body" style="padding:5%">
						<div id="process">
					<div class="step active">
						1訂單確認
					</div>
					&#10151;
					<div class="step">
						2付款方式確認
					</div>
					&#10151;
					<div class="step">
						3訂單完成
					</div>
				</div>	
		<!-- process -->
<%
	int subtotal = 0;
	pageContext.setAttribute("subtotal", subtotal);
	int total = 0;
	pageContext.setAttribute("total", total);
%>
<c:forEach varStatus="s" var="store" items="${shoppinglist}">
<div class="panel panel-success">
				<div class="panel-heading">
					訂單No.${s.count}
					<div class="cur-right">
						<a href="<%= request.getContextPath()%>/checkout/shopping.do?action=DELETE_ORD&store_no=${store.key}"><i class="glyphicon glyphicon-remove"></i></a>
					</div>
				</div>
				<div class="panel-body" style="font-size:1.2em;">
					<div class="col-md-3">
						<div class="checkout_store">
							<h4>
								${storeSvc.getOneStore(store.key).store_name}
							</h4>
						</div>
					</div>
					<div class="col-md-9 order_line">

	<c:forEach varStatus="m" var="meal" items="${store.value}">	
	<c:set var="mealVO" value="${mealSvc.getOneMeal(meal.key)}"/>	
						<div class="row">
							<div class="col-md-3 col-sm-2">
								<div class="pic">
									<img src="<%= request.getContextPath()%>/mealphoto_read/mealphoto_read.do?photo_no=${mealSvc.getOneMeal(meal.key).meal_no}" class="img-responsive" alt="">
								</div>
							</div>
							<div class="col-md-4 col-sm-5">
								<h4>${mealVO.meal_name}</h4>
								<p>
									<c:if test="${(mealVO.meal_discount==0)}">
										價格$ ${mealVO.meal_price}
									</c:if>
									<c:if test="${(mealVO.meal_discount>0)}">
										<strike>價格$ ${mealVO.meal_price}</strike>
										<font color="red">優惠價$ ${mealVO.meal_discount}</font>
									</c:if>															
								</p>
							</div>
							<div class="col-md-5 col-sm-5">
								<form class="form-horizontal" method="post" action="<%= request.getContextPath()%>/checkout/shopping.do">
										<div class="form-group">
										<label for="qty" class="col-sm-4 control-label">數量</label>
										<div class="col-sm-5">										
											<input type="number" class="form-control" id="qty" name="qty" value="${meal.value}" onchange="this.form.submit()" >
											<input type="hidden" name="meal_no" value="${meal.key}">
											<input type="hidden" name="store_no" value="${store.key}">
											<input type="hidden" name="action" value="changeQty">
										</div>
											<a href="<%= request.getContextPath()%>/checkout/shopping.do?action=DELETE&meal_no=${meal.key}"><i class="glyphicon glyphicon-remove"></i></a>
										</div>						
								</form>
							</div>
						</div><!--end of order_line-->
						<c:set var="subtotal" value="${((mealVO.meal_discount>0)?mealVO.meal_discount:mealVO.meal_price)*meal.value+subtotal}" />				
	</c:forEach>
	
						<div class="cur-right">小結${subtotal}</div>
						<c:set var="total" value="${total+subtotal}" />	
						<%
							subtotal=0;
							pageContext.setAttribute("subtotal", subtotal);
						%>									
					</div>					
				</div><!--end of panel body-->
			</div>		
</c:forEach>
<!-- --			 -->
	
		</div>
		</div>
					<div class="cur">
				<div class="cur-left">
					<a class="morebtn hvr-rectangle-in" href="<%= request.getContextPath()%>/front-end/restaurants/Restaurants.jsp" >繼續點餐</a>
				</div>
			</div>
			<div class="cur">
				<div class="cur-right">
					<h3>總計 共${shoppinglist.size()}筆訂單 ${total}元</h3>
					<a class="morebtn hvr-rectangle-in" href="<%= request.getContextPath()%>/checkout/CheckOut.jsp?mem_no=${user.no}" style="text-align:center;">結帳</a>
				</div>
				<div class="clearfix"> </div>
			</div>	
		</c:if>
		</div>
		<div class="col-md-1"></div>	
	</div>
</div>

<!-- checkout -->	
<!-- footer-->
<jsp:include page="/front-end/footer.html"/>
<!-- footer-->

</body>
</html>
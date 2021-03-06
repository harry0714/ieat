<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>優惠資料新增 - addDiscount.jsp</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/jquery-ui.min.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/memberprefecture.css"
	rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js"></script>
<script src="<%=request.getContextPath()%>/js/discount.js"></script>
</head>
<body>

	<c:if test="${check_list.size()>0}">
		<jsp:useBean id="check_list" scope="request" type="java.util.List" />
	</c:if>
	<!-- Modal -->
	<div class="modal fade" id="myModal" role="dialog">
		<div class="modal-dialog modal-lg">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="btn_meal_close" class="close"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title">選擇餐點</h4>
				</div>
				<div class="modal-body">
					<div id="meals_menu">
						<jsp:useBean id="mealSvc" scope="page"
							class="com.meal.model.MealService" />
						<c:forEach var="mealVO"
							items="${mealSvc.getFindByMeal(param.store_no)}">
							<input type="checkbox" value="${mealVO.meal_no}">
							<div class="meal_option"
								<c:if test="${check_list.contains(mealVO.meal_no)}">
							style="display:none"
						</c:if>>
								<img src="ImageMeal?meal_no=${mealVO.meal_no}" width="150">
								<h4>${mealVO.meal_name}</h4>
								售價$<span>${mealVO.meal_price}</span>
							</div>
						</c:forEach>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" id="btn_meal_confirm" class="btn btn-default"
						data-dismiss="modal">確定</button>
				</div>
			</div>

		</div>
	</div>

	<!-- header -->
	<jsp:include page="/front-end/head.jsp" />
	<!-- header -->

	<div class="main-1">
		<div class="container">
			<div class="col-md-3">
				<jsp:include page="/front-end/store/storeMenu.jsp" />
			</div>
			<div class="col-md-9">
				<c:if test="${not empty errorMsgs}">
					<font color="red">
						<ul>
							<c:forEach var="message" items="${errorMsgs}">
								<li>${message}</li>
							</c:forEach>
						</ul>
					</font>
				</c:if>
				<div class="panel panel-default">
					<div class="panel-heading" style="color: #d15a15; font-size: 20px">新增優惠表單</div>
					<div class="panel-body">

						<form class="form-horizontal" method="post" action="discount.do">
							<div class="form-group">
								<label class="control-label col-sm-3" for="discount_title">優惠標題</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" id="discount_title"
										name="discount_title" value="${discountVO.discount_title}">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="discount_startdate">開始日期</label>
								<div class="col-sm-4">
									<div class="input-group">
										<span class="input-group-addon icon_calendar"><i
											class="glyphicon glyphicon-calendar"></i></span> <input type="text"
											class="form-control" id="discount_startdate"
											name="discount_startdate"
											value="${discountVO.discount_startdate}" readonly>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="discount_enddate">結束日期</label>
								<div class="col-sm-4">
									<div class="input-group">
										<span class="input-group-addon icon_calendar"><i
											class="glyphicon glyphicon-calendar"></i></span> <input type="text"
											class="form-control" id="discount_enddate"
											name="discount_enddate"
											value="${discountVO.discount_enddate}" readonly>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="discount_enddate">優惠餐點</label>
								<div class="col-sm-9">
									<a href="#" data-toggle="modal" data-target="#myModal"><i
										class="glyphicon glyphicon-plus"></i>選擇餐點</a>
								</div>
							</div>
							<!-- meals table -->
							<div class="form-group">
								<table id="discount_meals" class="table table-bordered">
									<thead>
										<tr>
											<th>餐點圖片</th>
											<th>餐點名稱</th>
											<th>原價</th>
											<th>優惠價</th>
											<th>刪除</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty meal_list}">
											<jsp:useBean id="meal_list" scope="request"
												type="java.util.List" />
										</c:if>
										<c:forEach var="discount_mealVO" items="${meal_list}">
											<tr>
												<td><img
													src="ImageMeal?meal_no=${discount_mealVO.meal_no}"
													width="100px"></td>
												<td><input type="hidden"
													value="${discount_mealVO.meal_no}" name="meal_no">${mealSvc.getOneMeal(discount_mealVO.meal_no).meal_name}</td>
												<td>&#36;${mealSvc.getOneMeal(discount_mealVO.meal_no).meal_price}</td>
												<td><div class="input-group" style="width: 150px">
														<div class="input-group-addon">$</div>
														<input class="form-control input-sm" type="number"
															name="${discount_mealVO.meal_no}"
															value="${discount_mealVO.discount_meal_price}">
													</div></td>
												<td><a><i class="glyphicon glyphicon-remove"></i></a></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<!-- meals table -->
							<div class="form-group">
								<div id="btn_get_meals" class="col-sm-10">
									<input type="hidden" name="requestURL"
										value="<%=request.getServletPath()%>"> <input
										type="hidden" name="action" value="insert"> <input
										type="submit" class="btn btn-success" value="新增優惠">
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>


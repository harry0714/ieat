<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>


<%@ page import="com.store.model.*"%>



<jsp:useBean id="storeSvc" scope="page"
	class="com.store.model.StoreService" />

<%
	List<StoreVO> List = storeSvc.getAll();
	pageContext.setAttribute("List", List);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->

<link rel="shortcut icon" type="image/x-icon"
	href="<%=request.getContextPath()%>/images/eat.png" />

<title>IEat</title>


<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>

<link
	href="<%=request.getContextPath()%>/back-end/css/ie10-viewport-bug-workaround.css"
	rel="stylesheet">


<link href="<%=request.getContextPath()%>/back-end/css/dashboard.css"
	rel="stylesheet">


<script
	src="<%=request.getContextPath()%>/back-end/js/ie-emulation-modes-warning.js"></script>



<link href="<%=request.getContextPath()%>/back-end/css/c3.css"
	rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/back-end/js/d3.min.js"
	charset="utf-8"></script>
<script src="<%=request.getContextPath()%>/back-end/js/c3.min.js"></script>
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
<%@ include file="/back-end/page/body.jsp" %> 


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
					
					<h2>店家檢舉</h2>
				</div>
				<table class="table">
					<thead>
						<form action="<%=request.getContextPath()%>/store/store.do" id="statusform" method="post">
							<input type="hidden" name=action value="pass">
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
							<th>店家編號</th>
							
							<th>店家名稱</th>
							<th>店家電話</th>
							<th>店家負責人</th>
							<th>店家信箱</th>
							<th>店家種類</th>
							<th>店家狀態</th>
							<th colspan="2">審核</th>

						</tr>
					</thead>
					<tbody>
						<c:forEach var="storeVO" items="${List}" varStatus="s">
							<c:set var="storeVO" value="${storeVO}" />
							<%
								StoreVO storeVO = (StoreVO) pageContext.getAttribute("storeVO");
									String store_status = null;
									switch (storeVO.getStore_status().charAt(0)) {
									case '0':
										store_status = "已停權";
										break;
									case '1':
										store_status = "正常營業";
										break;
									case '2':
										store_status = "待審核";
										break;
									case '3':
										store_status = "停止營業";
										break;
									}
									pageContext.setAttribute("store_status", store_status);
							%>
							<tr
								class="${((storeVO.store_no==param.store_no))?'info':((s.index%2)==0?'success':'') }">
								<td style="vertical-align: middle">${s.count}</td>
								<td style="vertical-align: middle">${storeVO.store_no }</td>
								
								<td style="vertical-align: middle">${storeVO.store_name}</td>
								<td style="vertical-align: middle">${storeVO.store_phone}</td>
								<td style="vertical-align: middle">${storeVO.store_owner}</td>
								<td style="vertical-align: middle">${storeVO.store_email}</td>
								
								<jsp:useBean id="store_typeSvc" scope="page" class="com.store_type.model.Store_typeService" />
								<td>
									<c:forEach var="store_typeVO" items="${store_typeSvc.all }">
										<c:if test="${storeVO.store_type_no==store_typeVO.store_type_no}">
											${store_typeVO.store_type_name} 
										</c:if>
									</c:forEach>
								</td>
								<td style="vertical-align: middle">${store_status }</td>	
								<td style="vertical-align: middle"><c:if
										test="${storeVO.store_status == '2'}">
										<form action="<%=request.getContextPath()%>/store/store.do" method="post">
											<input type="hidden" name="action" value="pass">
											
											<input type="hidden" name="store_no" value="${storeVO.store_no}">

											<input type="hidden" name="status_selected" value="${param.status_selected }">
											<button type="submit" class="btn btn-success">通過</button>
										</form>
									</c:if></td>
								<td style="vertical-align: middle"><c:if
										test="${storeVO.store_status == '2'}">
										<form
											action="<%=request.getContextPath()%>/store/store.do" method="post">
											<input type="hidden" name="action" value="nopass">
											<input type="hidden" name="store_no"
												value="${storeVO.store_no}">
											<input type="hidden" name="status_selected" value="${param.status_selected }">
											<button type="submit" class="btn btn-success">不通過</button>
										</form>
									</c:if></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>

<%
List = null;
%>
</html>
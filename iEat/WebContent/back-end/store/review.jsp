<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="com.store.model.*"%>
<% StoreVO storeVO =(StoreVO) request.getAttribute("storeVO"); %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="images/eat.png">
<title>IEat</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/ie10-viewport-bug-workaround.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/dashboard.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>
<link href="css/c3.css" rel="stylesheet" type="text/css">
<script src="js/d3.min.js" charset="utf-8"></script>
<script src="js/c3.min.js"></script>
</head>
<body>
<%@ include file="/back-end/page/head.jsp" %> 
	
<%@ include file="/back-end/page/body.jsp" %> 

			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
 <a href="<%=request.getContextPath()%>/back-end/store/notreviewed.jsp">上一頁</a>
				<h1 class="page-header"></h1>
					<tbody>
			<table class="table table-striped">
				<tr>
					<th>店家編號</th>
					<td>${findStore.store_no}</td>
				</tr>
				<tr>
					<th>店家帳號</th>
					<td>${findStore.store_id}</td>
				</tr>
				<tr>
					<th>店家名稱</th>
					<td>${findStore.store_name}</td>
				</tr>
				<tr>
					<th>店家電話</th>
					<td>${findStore.store_phone}</td>
				</tr>
				<tr>
					<th>店家負責人</th>
					<td>${findStore.store_owner}</td>
				</tr>
				<tr>
					<th>店家信箱</th>
					<td>${findStore.store_email}</td>
				</tr>
				<tr>
					<th>店家種類</th>
					<jsp:useBean id="store_typeSvc" scope="page" class="com.store_type.model.Store_typeService" />
						<td>
							<c:forEach var="store_typeVO" items="${store_typeSvc.all }">
								<c:if test="${findStore.store_type_no==store_typeVO.store_type_no}">
									${store_typeVO.store_type_name} 
								</c:if>
							</c:forEach>
						</td>
				</tr>
				
				<tr>
					<th>訂位時段</th>
					<td>
						<c:forTokens items="${(storeVO.store_book_amt)}" delims="-" var="book_amt" varStatus="varStatus">			
							<c:if test="${!(book_amt=='00')}">
									${varStatus.index}:00 ：${book_amt }					
			                 </c:if>		
						</c:forTokens>
						<c:if test="${not (storeVO.store_booking).contains('1')}">
			                 	該店家無開放訂位
			             </c:if>		
					</td>
				</tr>
				<tr>
					<th>營業時間</th>
				<td>	<c:forTokens items="${(findStore.store_open)}" delims="-" var="open" varStatus="varStatus">			
						<c:if test="${open==1}">
						${varStatus.index}:00~${varStatus.index+1}:00
                 		</c:if>		
					</c:forTokens>
					<c:if test="${not (findStore.store_open).contains('1')}">
                 	該店家無營業時段資訊
             		</c:if>
             		</td>		
				</tr>
				
				<tr>
					<th>訂位人數</th>
					<td>
					<c:forTokens items="${(findStore.store_booking)}" delims="-" var="booking" varStatus="varStatus">			
						<c:if test="${booking==1}">
								${varStatus.index}:00~${varStatus.index+1}:00
		                 </c:if>		
					</c:forTokens>
					<c:if test="${not (findStore.store_booking).contains('1')}">
		                 	該店家無開放訂位
		             </c:if>		
					</td>		
				</tr>
				
				
				<tr>
					<th>店家狀態</th>
					
					<td> <button type="button" class="btn btn-danger btn-xs">${s1.store_status %2== 0 ? "未審核": "已審核"}</button></td>
				</tr>
				
		
				
			</table>
			</tbody>
			</div>
		</div>
	</div>
	</div>

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->

	<%=request.getContextPath()%>
	<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/holder.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath()%>/css/meal.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/addressforindex.js"></script>	
<link href="<%=request.getContextPath()%>/css/memberprefecture.css" rel="stylesheet" type="text/css" />
<!-- Logo --><link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/eat.png" /><!-- Logo -->
<title>iEat - 會員專區 最愛餐廳</title>
<script>
$(document).ready(function(){
	$(".bookmark").click(function(){
		$(this).parent().submit();
	});
});
</script>
</head>
<body>
	<!-- head -->
	<jsp:include page="/front-end/head.jsp" />
	<!-- head -->
	<!-- register -->
	<div class="main-1">
		<div class="container">
			<div class="col-md-3">
		<jsp:include page="/front-end/member/membermenu.jsp" />
			</div>
		<c:if test="${! empty errorMessage }">
				<font color='red'>請修正以下錯誤:
					<p>${errorMessage.elseError}</p>
				</font>
			</c:if>
			<div class="col-md-9">
					<div class="panel panel-default">
					<div class="panel-heading" style="color:#d15a15;font-size:20px">最愛餐廳  <%@ include file="page1.file"%></div>
					<div class="panel-body">

				<c:forEach var="storeVO" items="${storeList}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
			<div class="store_explore" id="${storeVO.store_no }">
				<div class="col-md-5 col-sm-6">
					<div class="pic">
						<a href="<%= request.getContextPath() %>/store/store.do?action=getOne_For_Display&store_no=${storeVO.store_no}">
							<!-- 抓到店家的照片  只顯示第一張 -->
							<img src="<%= request.getContextPath()%>/StorePhotoRead/StorePhotoRead.do?store_no=${storeVO.store_no}" class="img-responsive" alt="">	  																			
<%-- 							<img src="<%=request.getContextPath()%>/photo_read/photo_read.do?photo_no=${storeSvc2.getFirstPhoto(store_no)}" class="img-responsive" alt=""> --%>
						</a>
					</div>
				</div>

					<div class="col-md-7 col-sm-6">
					<h4>${storeVO.store_name}</h4>
						<p><i class="glyphicon glyphicon-home"></i> ${storeVO.store_ads}</p>
						<p><i class="glyphicon glyphicon-phone-alt"></i> ${storeVO.store_phone}</p>
						<p><i class="glyphicon glyphicon-star"></i> <fmt:formatNumber value="${storeVO.store_star}" pattern="#.##" type="number"/></p>
						
						<c:if test="${! empty user }">
							<c:if test="${! bookmark_StoreNo.contains(storeVO.store_no)}">
								<p>
									<form ACTION="<%=request.getContextPath()%>/bookmarkstore/bookmarkstore.do" METHOD="post">
										<input type="hidden" name="store_no" value="${storeVO.store_no}"> <input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller-->
										<input type="hidden" name="whichPage" value="<%=whichPage%>"><!--送出當前是第幾頁給Controller-->
										<input type="hidden" name="action" value="add">
										<a href="#" style="color:red;" class="bookmark"><i class="glyphicon glyphicon-heart-empty" title="加入最愛"></i></a>
									</form>
								</p>
							</c:if>
							<c:if test="${bookmark_StoreNo.contains(storeVO.store_no)}">
								<p>
									<form ACTION="<%=request.getContextPath()%>/bookmarkstore/bookmarkstore.do" METHOD="post">
										<input type="hidden" name="store_no" value="${storeVO.store_no}"> <input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"><!--送出本網頁的路徑給Controller-->
										<input type="hidden" name="whichPage" value="<%=whichPage%>"><!--送出當前是第幾頁給Controller-->
										<input type="hidden" name="action" value="delete">
										<a href="#" style="color:red;" class="bookmark"><i class="glyphicon glyphicon-heart" title="移除最愛"></i></a>
									</form>	
								</p>
							</c:if>
						</c:if>
					</div>
					<div class="col-md-4 col-sm-6">
						<div class="btn-left">
							<a class="morebtn hvr-rectangle-in" href="<%= request.getContextPath()%>/checkout/Orders.jsp?store_no=${storeVO.store_no}">點餐</a>
							<c:if test="${empty store }">  <!-- 若為店家不是登入狀態  則顯示訂位按鈕 -->
								<a href="<%=request.getContextPath()%>/store/store.do?action=get_reservation_info&store_no=${storeVO.store_no}&servletPath=<%=request.getServletPath() %>" 
								${storeVO.store_booking.contains('1') ? 'class="morebtn hvr-rectangle-in"' : 'class="morebtn hvr-rectangle-in disabled"'}>訂位</a>
							</c:if>								
						</div>
					</div>	

				<div class="clearfix" ></div>				
			</div>	
</c:forEach>
				<%@ include file="page2.file"%>
			</div>
			</div>
			</div>
		</div>
	</div>
	<!-- /register -->
	<!-- footer-->
	<jsp:include page="/front-end/footer.html" />
	<!-- /footer-->
</body>
</html>
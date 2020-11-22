<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.store.model.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.store_type.model.*"%>
<%
	List<StoreVO> list = (List<StoreVO>)request.getAttribute("list");

	Store_typeService stSvc = new Store_typeService();
	List<Store_typeVO> stList =  stSvc.getAll();
	request.setAttribute("stList", stList);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath()%>/images/iEat_logo.png" />
<link href="<%= request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath()%>/css/meal.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/addressforindex.js"></script>	
<script src="<%= request.getContextPath()%>/js/bookmark-store-jquery.js"></script>
<!-- Logo --><link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/eat.png" /><!-- Logo -->	
<title>iEat - 餐廳查詢</title>
<style>
        /*讓超連結disable掉 */
        a.disabled {
  			pointer-events: auto;
   			cursor: no-drop;
   			opacity: 0.3;   	
   			background:	grey;	  
		}
</style>
<script>
// 讓標籤為 disable的連結 失效
$(document).ready(function(){
    $('a.disabled').click(function(e){
        e.preventDefault();
    })
});
</script>	
<title>iEat - 查詢餐廳</title>

</head>
<body>
<!-- header -->
<jsp:include page="/front-end/head.jsp"/>
<!-- header -->
<!-- about -->
<div class="main-1">
	<div class="container">
		<div class="col-md-2">
			<div class="sub_nav">
			<!-- SearchBar -->
				<jsp:include page="/front-end/SearchBar.jsp"></jsp:include> 
			<!-- SearchBar -->	
			</div>
		</div>
		<div class="col-md-10">
		
<%@ include file="pages/page1ByCQ.file" %> 
	<c:forEach var="storeVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
			<div class="store_explore" id="${storeVO.store_no }">
				<div class="col-md-4 col-sm-6">
					<div class="pic"> 		
						<a href="<%= request.getContextPath() %>/store/store.do?action=getOne_For_Display&store_no=${storeVO.store_no}">							
							<!-- 抓到店家的照片  只顯示第一張 -->	
							<img src="<%= request.getContextPath()%>/StorePhotoRead/StorePhotoRead.do?store_no=${storeVO.store_no}" class="img-responsive" alt="">  																			
<%-- 							<img src="<%=request.getContextPath()%>/photo_read/photo_read.do?photo_no=${storeSvc2.getFirstPhoto(store_no)}" class="img-responsive" alt=""> --%>
						</a>
					</div>
				</div>
				<div class="col-md-8">
					<div class="col-md-8 col-sm-6">
					<h3>${storeVO.store_name}</h3>
						<p><i class="glyphicon glyphicon-home"></i> ${storeVO.store_ads}</p>
						<p><i class="glyphicon glyphicon-phone-alt"></i> ${storeVO.store_phone}</p>
						<p><i class="glyphicon glyphicon-star"></i> <fmt:formatNumber value="${storeVO.store_star}" pattern="#.##" type="number"/></p>
						<!-- 顯示加入最愛的圖示 -->
						<c:if test="${! empty user }">
							<c:if test="${! bookmark_StoreNo.contains(storeVO.store_no)}">
								<p>																			
									<a style="color:red;cursor: pointer;" class="bookmark">
										<input type="hidden" class="store_no" name="store_no" value="${storeVO.store_no}" >
										<input type="hidden" class="action" name="action" value="addAjax">
										<input type="hidden" class="path" value="<%= request.getContextPath()%>">
										<i class="glyphicon glyphicon-heart-empty" title="加入最愛"></i>
									</a>
								</p>
							</c:if>
							<c:if test="${bookmark_StoreNo.contains(storeVO.store_no)}">
								<p>																		
									<a style="color:red;cursor:pointer;" class="bookmark">
										<input type="hidden" class="store_no" name="store_no" value="${storeVO.store_no}" >
										<input type="hidden" class="action" name="action" value="deleteAjax">
										<input type="hidden" class="path" value="<%= request.getContextPath()%>">
										<i class="glyphicon glyphicon-heart" title="移除最愛"></i>
									</a>
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
				</div>
				<div class="clearfix" ></div>				
			</div>	
</c:forEach>			
<div style="text-align:center"><%@ include file="pages/page2ByCQ.file" %></div>			
		</div>
	</div>
</div>
<!-- about -->	
<!-- footer-->
<jsp:include page="/front-end/footer.html"/>
<!-- footer-->	
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!-- -- -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.meal.model.*"%>
<%@ page import="com.store.model.*"%>
<%@ page import="java.util.*"%>

<!-- -- -->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Orders</title>
<link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath()%>/images/iEat_logo.png" />
<link href="<%= request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath()%>/css/meal.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%= request.getContextPath()%>/js/bookmark-meal-jquery.js"></script>

<script>
$(document).ready(function(){
	
    $(".add_to_cart").click(function() {
    	$.ajax({
            type: "POST",
            url: "shopping.do",
            data: { action:"ADD",
            		meal_no:createQueryString(this),
            		meal_price:createQueryString1(this)},
            
           	success: function(){
                   announce();
               },

            error:function (xhr, ajaxOptions, thrownError) {
                console.log(xhr);
                alert(xhr.status);
                alert(thrownError)
               }
        })     
    });
    
    function createQueryString(e){
    	
    	
		return $(e).find("input[name='meal_no']").val();
    } 
    function createQueryString1(e){
		return $(e).find("input[name='meal_price']").val();
    } 
    
    function announce(){
    	$("#cart").show().delay(1000).fadeOut();;
    }
    
})
</script>		
</head>
<body>
<!-- header -->
<jsp:include page="/front-end/head.jsp"/>
<!-- header -->
<div id="cart"><img src="<%= request.getContextPath()%>/images/add_cart.png">SUCCESS</div>
<!-- about -->
<jsp:useBean id="storeSvc" scope="page" class="com.store.model.StoreService"/>
<jsp:useBean id="mealSvc" scope="page" class="com.meal.model.MealService"/>
<c:if test="${not empty storeSvc.getOneStore(param.store_no)}">
<div class="main-1">
	<div class="container">
		<!-- 左邊欄 -->
		<jsp:include page="/front-end/store/store_left_bar.jsp"/>
<div class="col-md-10">

						<h3>【${storeSvc.getOneStore(param.store_no).store_name}】</h3>
<c:forEach var="mealVO" items="${mealSvc.getFindByMeal(param.store_no)}">
<div class="col-md-3 col-sm-6">
				<div class="meal">
					<div class="pic">
						<img src="<%= request.getContextPath()%>/mealphoto_read/mealphoto_read.do?photo_no=${mealVO.meal_no}" class="img-responsive"></div>
					<h4>${mealVO.meal_name}</h4>
					<c:if test="${! empty user }">	
								<div style="margin-left:90%;margin-top:-15%;float:left;">						
								<c:if test="${! bookmark_MealNo.contains(mealVO.meal_no) }">
								<a  style="color: red;cursor: pointer;" class="bookmark">
									
										<input type="hidden" class="meal_no" name="meal_no" value="${mealVO.meal_no}">
										<input type="hidden" class="path"  value="<%=request.getContextPath()%>">
										<input type="hidden" class="action" name="action" value="addAjax">
										<span class="glyphicon glyphicon-heart-empty" title="加入最愛" aria-hidden="true"></span>
									
								</a>
								</c:if>
								
								<c:if test="${bookmark_MealNo.contains(mealVO.meal_no) }">
									<a  style="color: red;cursor: pointer;" class="bookmark" >							
										<input type="hidden" class="meal_no" name="meal_no" value="${mealVO.meal_no}">
										<input type="hidden" class="path"  value="<%=request.getContextPath()%>"> 	
										<input type="hidden" class="action" name="action" value="deleteAjax">
										<span class="glyphicon glyphicon-heart" title="移除最愛" aria-hidden="true"></span>
									</a>
								</c:if>	
								</div>							
						</c:if>
							<div class="price">
								<c:if test="${(mealVO.meal_discount==0)}">
									價格$ ${mealVO.meal_price}
								</c:if>
								<c:if test="${(mealVO.meal_discount>0)}">
									<strike>價格$ ${mealVO.meal_price}</strike>
									<font color="red">優惠價$ ${mealVO.meal_discount}</font>
								</c:if>
							</div>						
							<div class="cur-left add_to_cart">
							<input type="hidden" name="meal_no" value="${mealVO.meal_no}">
							<a class="morebtn hvr-rectangle-in"style="cursor: pointer;">加入餐車</a>
							</div>		
								<div class="clearfix"> </div>
									
				</div>
			</div>
</c:forEach>	
</div>
<!-- 		 -->
		<div class="clearfix"></div>
	</div>
</div>
</c:if>
<!-- about -->
<!-- footer-->
<jsp:include page="/front-end/footer.html"/>
<!-- footer-->
</body>
</html>
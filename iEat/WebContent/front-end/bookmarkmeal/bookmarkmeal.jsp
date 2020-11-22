<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
<link href="<%=request.getContextPath()%>/css/memberprefecture.css"
	rel="stylesheet" type="text/css" />
<title></title>
<script>
$(document).ready(function(){
	
    $(".add_to_cart").click(function() {
    	$.ajax({
            type: "POST",
            url: "<%=request.getContextPath()%>/checkout/shopping.do",
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
    
    $(".bookmark").click(function(){
    	$(this).parent().submit();
    });
})
</script>
</head>
<body>

	<!-- head -->
	<jsp:include page="/front-end/head.jsp" />
	<!-- /head -->
	<div id="cart"><img src="<%= request.getContextPath()%>/images/add_cart.png">SUCCESS</div>
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
					<div class="panel-heading" style="color:#d15a15;font-size:20px">最愛美食 <%@ include file="page1.file" %></div>
					<div class="panel-body">
				<font size="5" color="#d15a15"><h7></h7></font><br />

				<c:forEach var="mealVO" items="${mealList}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
					<div class="col-md-4 col-sm-6">
				<div class="meal">
					<div class="pic"><img src="<%= request.getContextPath()%>/mealphoto_read/mealphoto_read.do?photo_no=${mealVO.meal_no}" class="img-responsive"></div>
					
					<h4>${mealVO.meal_name} </h4>						
						<c:if test="${! empty user }">	
								<div style="margin-left:90%;margin-top:-15%;float:left;">						
								<c:if test="${! bookmark_MealNo.contains(mealVO.meal_no) }">
								<a href="#" style="color: red;">
									<form ACTION="<%=request.getContextPath()%>/bookmarkmeal/bookmarkmeal.do" METHOD="post">
										<input type="hidden" name="meal_no" value="${mealVO.meal_no}">
										<input type="hidden" name="store_no" value="${param.store_no }">
										<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"> <!--送出本網頁的路徑給Controller-->
									 	<input type="hidden" name="whichPage" value="<%=whichPage%>"> <!--送出當前是第幾頁給Controller-->		
										<input type="hidden" name="action" value="add">
										<span class="glyphicon glyphicon-heart-empty bookmark" title="加入最愛" aria-hidden="true"></span>
									</form>
								</a>
								</c:if>
								
								<c:if test="${bookmark_MealNo.contains(mealVO.meal_no) }">
									<a href="#" style="color: red;">
									<form ACTION="<%=request.getContextPath()%>/bookmarkmeal/bookmarkmeal.do" METHOD="post">
										<input type="hidden" name="meal_no" value="${mealVO.meal_no}">
										<input type="hidden" name="store_no" value="${param.store_no }">
										<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"> <!--送出本網頁的路徑給Controller-->
									 	<input type="hidden" name="whichPage" value="<%=whichPage%>"> <!--送出當前是第幾頁給Controller-->		
										<input type="hidden" name="action" value="delete">
										<span class="glyphicon glyphicon-heart bookmark" title="移除最愛" aria-hidden="true"></span>
									</form>
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
							<input type="hidden" name="meal_price" value="${mealVO.meal_price}">
							<a class="morebtn hvr-rectangle-in" href="#">加入餐車</a>
							</div>		
								<div class="clearfix"> </div>		
				</div>
			</div>
				</c:forEach>
				<footer class="aaaaa"></footer>
				<%@ include file="page2.file" %>
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
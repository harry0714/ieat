<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Checkout</title>
<link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath()%>/images/iEat_logo.png" />
<link href="<%= request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath()%>/css/pay.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>	
<script>
$(document).ready(function(){
	
    $(".add_to_cart").click(function() {
    	$.ajax({
            type: "POST",
            url: "shopping.do",
            data: {action:"ADD",meal_no:createQueryString(this)},
            
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
<!-- checkout -->
<div class="main-1" >
	<div class="container" style="min-height:800px;margin-top:30px;">
		<!-- 店家左邊列表 -->
			<jsp:include page="/front-end/store/store_left_bar.jsp"/>
		<!-- 店家左邊列表 -->	
		<div class="col-md-8">
			<div id="process">
				<div class="step">1: 輸入訂位資訊</div>
					&#10151;
				<div class="step">2: 確認訂位</div>
					&#10151;
				<div class="step active">3: 訂位完成</div>
			</div>		
		</div>
		<br><br>
		<div class="col-md-8">
		<h3>訂位成功</h3>
			<div class="cur">
				<div class="cur-left">
					<a class="morebtn hvr-rectangle-in" href="<%= request.getContextPath()%>/front-end/restaurants/Restaurants.jsp" >繼續瀏覽餐聽</a>
				</div>
			</div>
		</div>
		</div>
</div>
<!-- checkout -->	
<!-- footer-->
<jsp:include page="/front-end/footer.html"/>
<!-- footer-->
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>iEat - 訂單確認成功</title>
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
<div class="main-1">
	<div class="container">
		<div class="col-md-1"></div>
			<div class="col-md-10">
				<div class="panel panel-default">
				<div class="panel-body" style="padding:5%">
				<div id="process">
					<div class="step">
						1訂單確認
					</div>
					&#10151;
					<div class="step">
						2付款方式確認
					</div>
					&#10151;
					<div class="step active">
						3訂單完成
					</div>
				</div>		

		
		<h3>訂餐成功</h3>
		
			
	
				</div>
				</div>
			<div class="cur">
				<div class="cur-left">
					<a class="morebtn hvr-rectangle-in" href="<%= request.getContextPath()%>/front-end/restaurants/Restaurants.jsp" >繼續點餐</a>
				</div>
			</div>
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
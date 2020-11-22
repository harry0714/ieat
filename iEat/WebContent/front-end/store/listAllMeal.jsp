<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.meal.model.*"%>
<%@ page import="com.store.model.*"%>

<!DOCTYPE HTML>
<html>
<head>
<title>Orders</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath() %>/css/sweetalert.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/memberprefecture.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script>

$(document).ready(function(){
	$(".glyphicon-chevron-down").click(function(){
		$(this).closest("form").submit();
		
	})
	$(".glyphicon-chevron-up").click(function(){
		$(this).closest("form").submit();
	})
    $(".glyphicon-pencil").click(function(){
    	$.ajax({
            type: "POST",
            url: "<%=request.getContextPath()%>/meal/meal.do",
            data: {action:"getOne_For_Update",meal_no:createQueryString(this)},
            dataType: "json",
            
           	success: function(json){
                   drawForm(json);
               },

            error:function (xhr, ajaxOptions, thrownError) {
                console.log(xhr);
                alert(xhr.status);
                alert(thrownError)
               }
        })     
	  
	});
    function createQueryString(e){
		console.log($(e).parent().next().val());
		return $(e).parent().next().val();
    }
    function drawForm(json){
		$("#meal_no").val(json.meal_no);
		$("#meal_name").val(json.meal_name);
		$("#meal_price").val(json.meal_price);
		$("#meal_discount").val(json.meal_discount);
		$("#meal_descr").val(json.meal_descr);
		$("#store_no").val(json.store_no);
		$("#meal_status").val(json.meal_status);
		$("#mealphoto").attr("src",'<%=request.getContextPath()%>/mealPicRead/mealPicRead.do?meal_no='+json.meal_no);
		
	}
    <c:if test="${not empty errorMsgs}">
	$("#myModal").modal('show');
	</c:if>
});
</script>
<%
    MealService mealSvc = new MealService();
	Map<String, String> store = (Map<String, String>) session.getAttribute("store");
	String store_no = store.get("store_no"); 
    List<MealVO> top = mealSvc.getOneStoreNoTop(store_no, 0);//以上架
    List<MealVO> notop = mealSvc.getOneStoreNoTop(store_no, 1);
    pageContext.setAttribute("notop",notop);
    pageContext.setAttribute("top",top);
%>
<jsp:useBean id="storeSvc" class="com.store.model.StoreService"/>
</head>
<body>
<!-- header -->
<jsp:include page="/front-end/head.jsp"/>
<!-- header -->

<!-- about -->
<div class="main-1">
	<div class="container">
		<!-- StoreMenu -->
		<div class="col-md-3">
			<jsp:include page="/front-end/store/storeMenu.jsp" />
		</div>
		<!-- StoreMenu -->
		<div class="col-md-9">
			<c:if test="${empty top and empty notop}">
				<font color="red">尚無餐點</font>
			</c:if>
			<br>
			<a style="float:right;font-size:1.5em;" id="addBtn" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-plus"></i>新增餐點</a>
			<div class="clearfix"> </div>
			<br>
			<c:if test="${not empty top}">
			<div class="panel panel-default">
			<div class="panel-heading" style="color:#d15a15;font-size:20px">上架餐點</div>			
				<table class="table table-bordered">
					<tr>
						<th>餐點照片</th>
						<th>餐點名稱</th>
						<th>餐點描述</th>
						<th>餐點價格</th>
						<th>餐點狀態</th>
						<th>修改餐點</th>
						<th>餐點下架</th>
					</tr>
			<c:forEach var="MealVO" items="${top}">
			<tr>
	   		<td id="result"> <img src="<%=request.getContextPath()%>/mealPicRead/mealPicRead.do?meal_no=${MealVO.meal_no}" width=50 height=50 id="xxx">
			</td>
			<td>${MealVO.meal_name}</td>
			<td>${MealVO.meal_descr}</td>
			<td>${MealVO.meal_price}</td>
			<td><c:if test="${MealVO.meal_status=='0'}">已上架</c:if> 
					<c:if test="${MealVO.meal_status=='1'}">已下架</c:if> 
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/meal/meal.do">
			   <a href="#" data-toggle="modal" data-target="#myModal1"><i class="glyphicon glyphicon-pencil"></i></a><!-- 修改 -->
			     <input type="hidden" name="meal_no" value="${MealVO.meal_no}">
			     <input type="hidden" name="action"	value="getOne_For_Update">
			   </FORM>
			</td>
			<td>
			  <FORM  METHOD="post" ACTION="<%=request.getContextPath()%>/meal/meal.do">
			     <a href="#"><i class="glyphicon glyphicon-chevron-down"></i></a><!-- 下架 -->
			    <input type="hidden" name="meal_no" value="${MealVO.meal_no}">
			    <input type="hidden" name="action" value="get_one_meal_status_down">
			  </FORM>
			</td>
		</tr>
		</c:forEach>
		</table>
		</div><!-- col-md-8 -->
		</c:if>
	
			<!-- 顯示所有下架的餐點 -->
		<c:if test="${not empty notop }">
		<div class="panel panel-default">
			<div class="panel-heading" style="color:#d15a15;font-size:20px">下架餐點</div>			
			<table class="table table-bordered">
				<tr>
					<th>餐點照片</th>
					<th>餐點名稱</th>
					<th>餐點描述</th>
					<th>餐點價格</th>
					<th>餐點狀態</th>
					<th>修改餐點</th>
					<th>餐點上架</th>
				</tr>
				<c:forEach var="MealVO" items="${notop}">
				<tr>
					<td id="result"> 
						<img src="<%=request.getContextPath()%>/mealPicRead/mealPicRead.do?meal_no=${MealVO.meal_no}" width=50 height=50 id="xxx">
					</td>
					<td>${MealVO.meal_name}</td>
					<td>${MealVO.meal_descr}</td>
					<td>${MealVO.meal_price}</td>
					<td><c:if test="${MealVO.meal_status=='0'}">已上架</c:if> 
							<c:if test="${MealVO.meal_status=='1'}">已下架</c:if> 
					</td>
					<td>
					  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/meal/meal.do">
					     <a href="#" data-toggle="modal" data-target="#myModal1"><i class="glyphicon glyphicon-pencil"></i></a>
		<!-- 			     <input type="submit" value="修改"> -->
					     <input type="hidden" name="meal_no" value="${MealVO.meal_no}">
					     <input type="hidden" name="action"	value="getOne_For_Update">
					  </FORM>
					</td>
					<td>
					  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/meal/meal.do">
					     <a href="#"><i class="glyphicon glyphicon-chevron-up"></i></a>
		<!-- 			    <input type="submit" value="上架"> -->
					    <input type="hidden" name="meal_no" value="${MealVO.meal_no}">
					    <input type="hidden" name="action" value="get_one_meal_status">
					  </FORM>
					</td>
				 </tr>
				</c:forEach>
			</table>
			</div>
			</c:if>
		</div>
</div>

<!-- Modal -->
<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">
	<!-- 新增餐點的跳窗 -->
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">新增餐點:</h4>
      </div>
      <div class="modal-body">
       	<jsp:include page="addmeal.jsp"/>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>

<div id="myModal1" class="modal fade" role="dialog">
  <div class="modal-dialog">
  
  	<!-- 修改餐點的跳窗 -->
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">修改餐點:</h4>
      </div>
      <div class="modal-body">
       	<jsp:include page="update_meal_input.jsp"/>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    		</div>
  		</div>
	</div>
</div>

<!-- main -->
<!-- footer -->
	<jsp:include page="/front-end/footer.html"/>
<!-- footer -->

<script>


function readFile(){ 
    var file = this.files[0]; 
    if(!/image\/\w+/.test(file.type)){ 
        alert("Error!!!"); 
        return false; 
    } 
   
    var reader = new FileReader(); 
    reader.readAsDataURL(file); 
    reader.onload = function(e){ 
    	photoResult.innerHTML ='<img src="'+this.result+'" alt="" width=100 height=100 />';
    	$("#mealphoto").attr("src",this.result);
    } 
}

function doFirst(){
	var photoResult = document.getElementById("photoResult"); 
	var input = document.getElementById("fileinput");
	var input2 = document.getElementById("fileinput2"); 
	
	if(typeof FileReader==='undefined'){ 
	    result.innerHTML = "no suppor FileReader"; 
	    input.setAttribute('disabled','disabled'); 
	    input2.setAttribute('disabled','disabled');
	}else{ 
	    input.addEventListener('change',readFile,false); 
	    input2.addEventListener('change',readFile,false); 
	} 
}

window.addEventListener('load',doFirst,false);
</script>
</body>
</html>
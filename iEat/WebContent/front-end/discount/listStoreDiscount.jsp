<%@page import="com.store.model.StoreVO"%>
<%@page import="com.store.model.StoreService"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.discount.model.DiscountVO"%>
<%@page import="java.util.*"%>
<%@page import="com.discount.model.DiscountService"%>
<%@page import="com.advertisement.tools.AdStatusType"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>所有優惠資料 - listAllDiscount.jsp</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/jquery-ui.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/sweetalert.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/memberprefecture.css"rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script src="<%=request.getContextPath()%>/js/discount.js"></script>
<script>
$(document).ready(function(){
	
	<c:if test="${not empty success}">
		swal("Good job!", "${success}", "success")
	</c:if>
	
	<c:if test="${empty errorMsgs}">
		$("#update_page").hide();
	</c:if>
	
	<c:if test="${not empty errorMsgs}">
		activeBtn();
	</c:if>		
	
	$(".glyphicon-pencil").click(function() {
		resetBtn();
		$("#message").empty();
    	$("#discount_list_table").find("tr").removeClass("success");
    	$(this).closest("tr").addClass("success");		
	 	$.ajax({
	         type: "POST",
	         url: "discount.do",
	         data: {action:"get_update_form",discount_no:createQueryString(this)},
	         dataType: "json",
	         
	        	success: function(data){
	        		$("#update_page").show();
	                drawForm(data[0]);
	                drawTable(data[1]);
	            },
	
	         error:function (xhr, ajaxOptions, thrownError) {
	             console.log(xhr);
	             alert(xhr.status);
	             alert(thrownError)
	            }
	     })     
	});
   
   function createQueryString(e){
		return $(e).closest("tr").children("td:first").text();
   }
   
   function drawForm(discountVO){
	   $("#discount_title").val(discountVO.discount_title);
	   $("#discount_startdate").val(discountVO.discount_startdate);
	   $("#discount_enddate").val(discountVO.discount_enddate);
	   $("#discount_no").val(discountVO.discount_no);	   
   }
   
   function drawTable(meals){
	    $("#meals_menu input[type=checkbox]").prop('checked',false).next().show();//uncheck all checkboxes and show all the option meals
		$("#discount_meals tbody").empty();
		var htmlStr = '';
		var i=0;
		$.each(meals,function(){			
			$("#meals_menu input[type=checkbox][value="+meals[i].meal_no+"]").next().hide();//hide meal_optinos which are already on the table
			htmlStr+='<tr><td><img src="ImageMeal?meal_no='+meals[i].meal_no+'" width="100px"></td><td>'
					+meals[i].meal_name+'<input type="hidden" value="'+meals[i].meal_no+'" name="meal_no"></td><td>$'
					+meals[i].meal_price+'</td><td><div class="input-group" style="width:150px"><div class="input-group-addon">$</div><input class="form-control input-sm" type="number" name="'+meals[i].meal_no+'" value="'+meals[i].discount_meal_price+'"></div></td><td>'
					+'<a><i class="glyphicon glyphicon-remove"></i></a></td></tr>';
			i++;
		});		
		$("#discount_meals tbody").append(htmlStr);
		check_table();
		delete_register();
		change_register();
   }
	
	function check_table(){
		if($("#discount_meals tbody").children('tr').length==0){
			$("#discount_meals").hide();
		}
		else{
			$("#discount_meals").show();			
		}
	}
	
	function delete_register(){
	    $(".glyphicon-remove").on('click',function(){
	    	var val = $(this).closest('tr').find('input[name=meal_no]').val();
	    	$(this).closest('tr').remove();
	    	$("#meals_menu input[type=checkbox][value="+val+"]").next().show();
	    	check_table();
	    	activeBtn();
	    })				
	}

	$("#btn_meal_confirm").on('click', activeBtn);
			
	function change_register(){
		$("#form_update :input").off('change').one('change', activeBtn);
	}
	
	function resetBtn(){
		$("#btn_update").prop('disabled', true).removeClass("btn-success").addClass("btn-default");
	}
	
	function activeBtn(){
		$("#btn_update").prop('disabled', false).removeClass("btn-default").addClass("btn-success");
	}
	
	$(".btn_delete").click(function(e){
		e.preventDefault();
		confirmCheck($(this));
	})
	
	function confirmCheck(obj){
			swal({
				  title: "確定要刪除嗎?",
				  type: "warning",
				  showCancelButton: true,
				  confirmButtonClass: "btn-danger",
				  confirmButtonText: "確定",
				  cancelButtonText: "取消",
				},
				function(isConfirm) {
				  if (isConfirm) {
					  document.location.assign(obj.attr('href'))
				  }
				});	
	}		
	
})
</script>
<jsp:useBean id="discountSvc" scope="page" class="com.discount.model.DiscountService"/>
<%
	Map<String, String> store = (Map<String, String>) session.getAttribute("store");
	pageContext.setAttribute("store_no", store.get("store_no"));
	List<DiscountVO> list = discountSvc.getStoreDiscount(store.get("store_no"));
	pageContext.setAttribute("list", list);
%>
</head>
<body>
<!-- header -->
<jsp:include page="/front-end/head.jsp"/>
<!-- header -->

<div class="main-1">
<div class="container">
		<div class="col-md-3">
			<jsp:include page="/front-end/store/storeMenu.jsp"/>
		</div>
		<div class="col-md-9">
<c:if test="${list.size()>0}">
			<br>
<%@ include file="pages/page1.file" %>
			<a style="float:right;font-size:1.5em;" href="addDiscount.jsp?store_no=${store_no}"><i class="glyphicon glyphicon-edit"></i>新增優惠</a>
			<div class="clearfix"> </div>
			<br>
<div class="panel panel-default">
<div class="panel-heading" style="color: #d15a15; font-size: 20px">優惠管理</div>
<table id="discount_list_table" class="table table-striped">
	<tr>
		<th>優惠編號</th>
		<th>優惠標題</th>
		<th>優惠期間</th>
		<th>狀態</th>
		<th>優惠餐點</th>		
		<th>修改</th>
		<th>刪除</th>			
	</tr>
	<c:forEach var="discountVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
	<tr>
		<td>${discountVO.discount_no}</td>
		<td>${discountVO.discount_title}</td>
		<td>${discountVO.discount_startdate}~${discountVO.discount_enddate}</td>
		<c:set var="status" value="${AdStatusType.getCode(discountVO.discount_startdate,discountVO.discount_enddate)}"/>
		<td><span class="label ${AdStatusType.getCssClass(status)}">${AdStatusType.values()[status]}</span></td>		
		<td><span class="badge">${discountSvc.getCountMeal(discountVO.discount_no)}</span></td>
		<td><a href="#"><i class="glyphicon glyphicon-pencil"></i></a></td>
		<td><a class="btn_delete" href="discount.do?action=delete_B&discount_no=${discountVO.discount_no}&whichPage=${whichPage}"><i class="glyphicon glyphicon-trash"></i></a></td>		
	</tr>
	</c:forEach>
</table>
</div>
<%@ include file="pages/page2.file" %>
</c:if>

</div>
</div>

<div id="update_page">
	<jsp:include page="update_discount_input.jsp" />
</div>

</div>
</body>
</html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.store.model.*"%>
<%@ page import="com.store_photo.model.*"%>


<%
    StoreService storeSvc = new StoreService();
    List<StoreVO> list = storeSvc.findByNot_reviewed();
    pageContext.setAttribute("list",list);  
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="<%=request.getContextPath()%>/back-end/index.jsp">
<title>iEat - 後端店家審核</title>

<!-- CSS -->
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/jquery-ui.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/sweetalert.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/metisMenu.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/timeline.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/sb-admin-2.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/morris.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/ie10-viewport-bug-workaround.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/dashboard.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/c3.css" rel="stylesheet" type="text/css">
<!-- CSS -->

<!-- JS -->  
<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>
<script src="<%=request.getContextPath()%>/js/d3.min.js" charset="utf-8"></script>
<script src="<%=request.getContextPath()%>/js/c3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script src="<%=request.getContextPath()%>/js/indexsocket.js"></script>
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>
<script type="text/javascript" src="https://maps.google.com/maps/api/js?key=AIzaSyANhvv_DDadWfAzIGurcl8fZixWAdfrgQk"></script>
<!-- JS -->

	<script>
								$(document).ready(function(){
									
									$(".glyphicon-ok").click(function(){
										$(this).closest("form").submit();
									})

									$(".glyphicon-remove").click(function(){
										$(this).closest("form").submit();
									})

									$(".glyphicon-list-alt").click(function(){
										$("#store_img").find("img").remove();
								    	$.ajax({
								            type: "POST",
								            url: "<%=request.getContextPath()%>/store/store.do",
								            data: {action:"getOne_display",store_no:createQueryString(this)},
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
									})
									
								    function createQueryString(e){
										return $(e).closest("tr").children("td:first").text();
								    }   
									function drawForm(json){
										var stat=json.store_status;
										var status;
										switch(status){
										 case 0:
											 status = "已停權";
										        break;
										    case 1:
										    	status = "正常營業";
										        break;
										    case 2:
										    	status = "待審核";
										        break;
										   
										}
										var s=json.store_open;
										var arr = s.split("-");
										var openhours ="";
									
										var flag = false;
										for(var i=0;i<arr.length;i++){
											if(arr[i]!=0&&flag==false){
												flag = true;
												openhours+=i +":00";
											}if(flag==true&&arr[i]==0){
												flag = false;
												openhours+="~"+i +":00"+"<br>";
											}
										}
										if(flag==true){
											openhours+="~"+arr.length+":00"+"<br>";
										}
										
										var c=json.store_book_amt;
										var arr2 = c.split("-");
										var a=json.store_booking;
										var arr1 = a.split("-");
										var bookings ="";
										var flag1 = false;
										for(var i=0;i<arr1.length;i++){
											if(arr1[i]!=0&&flag1==false){
												flag1 = true;
												bookings+=i +":00";
											}if(flag1==true&&arr1[i]==0){
												flag1 = false;
												bookings+="~"+i +":00"+" 定位人數:"+(arr2[i-1]==00?'此時段無開放訂位':arr2[i-1])+"<br>";
											}if(flag1==true&&i==arr1.length-1){
												flag1 = false;
												bookings+="~"+(i+1)+":00"+" 定位人數:"+(arr2[i-1]==00?'此時段無開放訂位':arr2[i-1])+"<br>";
											}
										}
										
										
										
										var bookingams ="";
										var flag2 = false;
										for(var i=0;i<arr2.length;i++){
											if(arr2[i]!=0&&flag2==false){
												flag2 = true;
												bookingams+=i +":00";
											}if(flag2==true&&arr2[i]==0){
												flag2 = false;
												bookingams+="~"+i +":00"+"<br>";
											}if(flag2==true&&i==arr2.length-1){
												flag2 = false;
												bookingams+="~"+(i+1)+":00"+"<br>";
											}
										}
										
										
										var images = "";
									
										
										if(json.store_photo==0 && stat==2){
											
											$("#store_img").html("<img class='mySlides' src='<%=request.getContextPath()%>/storePhotoReader2?store_no='+json.store_no+'&rownum='+i+'' style='width:100%; '>");
										}else{
											for(var i=1; i<json.store_photo+1;i++){
												var display = (i!=1)?'display:none':'';
												images+='<img class="mySlides" src="<%=request.getContextPath()%>/storePhotoReader2?store_no='+json.store_no+'&rownum='+i+'" style="width:100%;'+display+'">';
											}
										}
											
										
								
										$("#store_no").text(json.store_no);
										$("#store_name").text(json.store_name);
										$("#store_phone").text(json.store_phone);
										$("#store_owner").text(json.store_owner);
										$("#store_email").text(json.store_email);
										$("#store_type_no").text(json.store_type_no);
										$("#store_open").html(openhours);
										$("#store_book_amt").html(bookingams);
										$("#store_booking").html(bookings);
										$("#store_status").text(status);
										$("#store_img").prepend(images);
										
										$("#btn_right").click(function(){
											showDivs(slideIndex += 1);
										})
										
										$("#btn_left").click(function(){
											showDivs(slideIndex -= 1);
										})
										var slideIndex = 1;
										console.log("slideIndex outside"+slideIndex);
										function showDivs(n) {
										  var i;
										  var x = document.getElementsByClassName("mySlides");

										  if (n > x.length) {
											  slideIndex = 1
											  }    
										  if (n < 1) {
											  slideIndex = x.length
											  } ;
										  for (i = 0; i < x.length; i++) {
											 
										     x[i].style.display = "none";  
										  }
										  x[slideIndex-1].style.display = "block"; 
										
										  console.log("slideIndex inside"+slideIndex);										  
										}
									}
								
								})
								
								</script>

</head>
<body>
<%@ include file="/back-end/page/head.jsp" %> 
<%@ include file="/back-end/page/body.jsp" %> 

<!-- 顯示店家詳細資料的跳窗 -->
<!-- Modal -->
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">    
      <!-- Modal content-->
      <div class="modal-content" onload="getTime();">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">店家資料</h4>
        </div>
        <div class="modal-body">

		<table class="table table-striped">
	<tr>
	  <td>
<!-- 	  	<div id="fuckyou"> -->
<!-- 	  	</div> -->
		<div id="store_img" class="w3-content" style="max-width:300px;position:relative; left: 150px" >
		
		</div>
	  </td>
	</tr>
	<tr>
		<th style="position:relative; left: 100px">店家名稱</th>
		<td id ="store_name" style="position:relative; right: 100px"></td>
	</tr>
	<tr>
		<th style="position:relative; left: 100px">店家電話</th>
		<td id="store_phone" style="position:relative; right: 100px"></td>
	</tr>
	<tr>
		<th style="position:relative; left: 100px">店家負責人</th>
		<td id="store_owner" style="position:relative; right: 100px"></td>
	</tr>
	<tr>
		<th style="position:relative; left: 100px">店家信箱</th>
		<td id="store_email" style="position:relative; right: 100px"></td>
	</tr>
	<tr>
		<th style="position:relative; left: 100px">店家種類</th>
		<td id="store_type_no" style="position:relative; right: 100px"></td>
	</tr>

	<tr>
		<th style="position:relative; left: 100px">訂位人數</th>
		<td id="store_book_amt" style="position:relative; right: 100px"></td>
			     
			
	</tr>
	<tr>
		<th style="position:relative; left: 100px">營業時間</th>
		<td id="store_open" style="position:relative; right: 100px"></td>
	</tr>

	<tr>
		<th style="position:relative; left: 100px">訂位時段</th>
		<td id="store_booking" style="position:relative; right: 100px"></td>
	</tr>


	<tr>
		<th style="position:relative; left: 100px">店家狀態</th>

		<td id="store_status" style="position:relative; right: 100px">
			<button type="button" class="btn btn-danger btn-xs">${s1.store_status %2== 0 ? "未審核": "已審核"}</button>
		</td>
	</tr>
</table>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</div>

			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h1 class="page-header">店家審核</h1>
				<table class="table table-striped">
				<thead>			
				<tr>
					<th>店家編號</th>
					<th>店家帳號</th>
					<th>店家名稱</th>
					<th>店家電話</th>
					
					<th>店家地址</th>
					<th>店家種類</th>
					<th>店家狀態</th>
					<th>詳細資料</th>
					<th>審核通過</th>
					<th>審核不通過</th>
				</tr>
				</thead>	
		<tbody>					
	<%@ include file="/back-end/page/page1.file" %> 
	<c:forEach var="storeVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
		<tr>
			<td>${storeVO.store_no}</td>
			<td>${storeVO.store_id}</td>
			<td>${storeVO.store_name}</td>
			<td>${storeVO.store_phone}</td>
			
			<td>${storeVO.store_ads}</td>
			
			<jsp:useBean id="store_typeSvc" scope="page" class="com.store_type.model.Store_typeService" />
		<td>
			<c:forEach var="store_typeVO" items="${store_typeSvc.all }">
				<c:if test="${storeVO.store_type_no==store_typeVO.store_type_no}">
					${store_typeVO.store_type_name} 
				</c:if>
			</c:forEach>
		</td>
			
			<td>
			 <button type="button" class="btn btn-danger btn-xs">${storeVO.store_status %2== 0 ? "未審核": "已審核"}</button>
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/store/store.do">
			 
			    <a href="#" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-list-alt"></i></a>
			     <input type="hidden" name="store_no" value="${storeVO.store_no}">
			     <input type="hidden" name="action"	value="getOne_display">
			  </FORM>
			</td>
			
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/store/store.do">
<!-- 			     <input type="submit" value="通過"> -->
				<a href="#"><i class="glyphicon glyphicon-ok"></i></a>
			     <input type="hidden" name="store_no" value="${storeVO.store_no}">
			 
			     <input type="hidden" name="action"	value="pass">
			  </FORM>
			</td>

			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/store/store.do">
<!-- 			    <input type="submit" value="未通過"> -->
 				<a href="#"><i class="glyphicon glyphicon-remove"></i></a>
			    <input type="hidden" name="store_no" value="${storeVO.store_no}">
			    <input type="hidden" name="action"value="nopass">
			    </FORM>
			</td>
		</tr>
	</c:forEach>
	</tbody>
<%@ include file="/back-end/page/page2.file" %>	

</table>
			</div>
	</body>
</html>




<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.store.model.*" %>
<%
	StoreVO storeVO = (StoreVO) request.getAttribute("storeVO"); 
%>
<jsp:useBean id="store_typeSvc" scope="page" class="com.store_type.model.Store_typeService"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Store Register</title>
<link href="<%= request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath() %>/css/sweetalert.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%= request.getContextPath() %>/js/jquery.min.js"></script>
<script src="<%= request.getContextPath() %>/js/address.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>

<script>	
	$(document).ready(function() {

		$('.bookable').hide();
		$('.time').click(function(){
			$(this).next().toggle(500); 
		}); 
		$(".time").each(function(){
			 if($(this).is(":checked")){
				 $(this).parent().attr('open','open');
				 $(this).next().show();
			 }
		})
	}); 	
</script>

<script>
// 這裡驗證帳號是否有重複
$(document).ready(function() {
	$("#store_id").blur(function() {
		var store_id = $(this).val();
		
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath()%>/store/store.do",
				data : {action : "checkid",store_id : store_id},
				dataType : "text",
				success : function(data) {
					if(data == "true"){
						$("#acctcheckstate").removeClass("glyphicon-remove");
						$("#acctcheckstate").addClass("glyphicon-ok");
					}
					if(data == "false"){
						$("#acctcheckstate").removeClass("glyphicon-ok");
						$("#acctcheckstate").addClass("glyphicon-remove");
					}
				},
				error : function() {
					alert("error");
				}
		});
	});
	// 驗證店家e-mail是否有重複 
	$("#store_email").blur(function() {
		var store_email = $(this).val();
		
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath()%>/store/store.do",
				data : {action : "checkemail",store_email : store_email},
				dataType : "text",
				success : function(data) {
					if(data == "true"){
						$("#emailcheckstate").removeClass("glyphicon-remove");
						$("#emailcheckstate").addClass("glyphicon-ok");
					}
					if(data == "false"){
						$("#emailcheckstate").removeClass("glyphicon-ok");
						$("#emailcheckstate").addClass("glyphicon-remove");
					}
				},
				error : function() {
					alert("error");
				}
		});
	});
	//驗證店家電話  是否有重複
	$("#store_phone").blur(function() {
		var store_phone = $(this).val();
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath()%>/store/store.do",
				data : {action : "checkphone",store_phone : store_phone},
				dataType : "text",
				success : function(data) {
					if(data == "true"){
						$("#phonecheckstate").removeClass("glyphicon-remove");
						$("#phonecheckstate").addClass("glyphicon-ok");
					}
					if(data == "false"){
						$("#phonecheckstate").removeClass("glyphicon-ok");
						$("#phonecheckstate").addClass("glyphicon-remove");
					}
				},
				error : function() {
					alert("error");
				}
		});
	});
	// 神奇小按鈕 
	$("#magical").click(function(){
		$("#store_name").val("小蒙牛頂級麻辣養生鍋");
		$("#store_id").val("mongobeef");
		$("#store_psw").val("mongobeef");
		$("#store_psw_check").val("mongobeef");
		$("#store_email").val("aa101ieat@gmail.com");
		$("#store_phone").val("0978123456");
		$("#store_owner").val("達拉");
		$("#zone1").val("桃園市");
		$("#zone2 option").remove();
		$("#zone2").append($("<option></option>").attr("value", "桃園區").text("桃園區"));
		$("#zipcode").val("330");
		$("#zone3").val("縣府路168號");
		$("#store_type_no").val("ST01");
		$("#latlng").val("24.9924290,121.3017370");
		
		$("#lunchDetails").attr("open","");
		$("#nightDetails").attr("open","");
		$("#check11").attr("checked","");
		$("#check11").next().show();
		$("#check11").next().children(":radio:first").attr("checked","");
		$("#check11").next().children(":text").val("30");
		$("#check12").attr("checked","");
		$("#check12").next().show();
		$("#check12").next().children(":radio:first").attr("checked","");
		$("#check12").next().children(":text").val("30");
		$("#check13").attr("checked","");
		$("#check13").next().show();
		$("#check13").next().children(":radio:first").attr("checked","");
		$("#check13").next().children(":text").val("30");
		$("#check14").attr("checked","");
		$("#check14").next().show();
		$("#check14").next().children(":radio:first").attr("checked","");
		$("#check14").next().children(":text").val("30");
		$("#check17").attr("checked","");
		$("#check17").next().show();
		$("#check17").next().children(":radio:first").attr("checked","");
		$("#check17").next().children(":text").val("30");
		$("#check18").attr("checked","");
		$("#check18").next().show();
		$("#check18").next().children(":radio:first").attr("checked","");
		$("#check18").next().children(":text").val("30");
		$("#check19").attr("checked","");
		$("#check19").next().show();
		$("#check19").next().children(":radio:first").attr("checked","");
		$("#check19").next().children(":text").val("30");
		$("#check20").attr("checked","");
		$("#check20").next().show();
		$("#check20").next().children(":radio:first").attr("checked","");
		$("#check20").next().children(":text").val("30");
		
		$("#store_intro").val("小蒙牛麻辣養生火鍋秉持著以客為尊的服務態度，讓貴賓們品嚐到更好更美味的鍋食美餚。");
		
	});
});
</script>

<script src="https://maps.googleapis.com/maps/api/js" async defer></script> 
<script>
// 改成按下註冊後  產生經緯度
function getLatlng() {
// 地址自動轉經緯度
	var geocoder = new google.maps.Geocoder();
	var zone1 = $('#zone1').val(); 
	var zone2 = $('#zone2').val();	
	var addr = zone1 + zone2 + $("#zone3").val();
	console.log("地址為="+addr); 	
	geocoder.geocode({
        	'address': addr
    	}, function (results, status) {
        	if (status == google.maps.GeocoderStatus.OK) {
        		var lat = results[0].geometry.location.lat();
        		var lng = results[0].geometry.location.lng(); 
        		var latlng = lat+','+lng        		
        		console.log(latlng); 
        		$('#latlng').val(latlng); 
    	}
	});
	// $("#registerForm").submit(); 
}
</script>
</head>
<body>

<!-- header -->
<jsp:include page="/front-end/head.jsp" />
<!-- header -->

<c:if test="${not empty store }">
	<c:redirect url="/front-end/index.jsp"></c:redirect>
</c:if>

<!-- register -->
<div class="main-1">
	<div class="container">
		<div class="col-md-1"></div>
			<div class="col-md-10">
				<div class="panel panel-default">
				<div class="panel-body" style="padding:5%">
		<div id="process">
			<div class="step active">1: 輸入基本資料</div>
				&#10151;
			<div class="step">2: 等待後台驗證</div>
				&#10151;
			<div class="step">3: 驗證完成</div>
		</div>
		<div class="well">
			<c:if test="${not empty errorMsgs.elseError }">
				<div>
					<font color='red'>請修正以下錯誤: <p>${errorMsgs.elseError }</p></font>
				</div>
			</c:if>

			<form action="<%=request.getContextPath() %>/store/store.do" method="post" id="registerForm" class="form-horizontal" role="form"> 
				<div class="form-group">
					<label for="store_name" class="control-label col-sm-2">店家名稱*</label>
					<div class="col-sm-10">
					<input type="text" class="form-control" id="store_name" placeholder="Name" name="store_name" id="store_name" value="<%= (storeVO == null) ?  "" : storeVO.getStore_name() %>">
					<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_name)?'hidden':'visible'};">
					<font color="red">${errorMsgs.store_name }</font></p>
					</div>
				</div>
				
				<div class="form-group">
					<label for="InputAccount" class="control-label col-sm-2">帳號*</label>
					<div class="col-sm-10">					
						<input type="text" class="form-control" placeholder="Account" name="store_id" id="store_id" value="<%= (storeVO == null) ?  "" : storeVO.getStore_id() %>">
					<span class="glyphicon  form-control-feedback" style="margin-right:10px" aria-hidden="true" id="acctcheckstate"></span>
					<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_id)?'hidden':'visible'};">
					<font color="red">${errorMsgs.store_id }</font></p>
					</div>
				</div>
					
					<div class="form-group">
						<label for="store_psw" class="control-label col-sm-2">密碼*</label>
						<div class="col-sm-10">	
						<input type="password" class="form-control" id="store_psw" placeholder="Password" name="store_psw" id="store_psw">					
					<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_psw)?'hidden':'visible'};">
					<font color="red">${errorMsgs.store_psw }</font></p>
					</div>
				</div>
				
			<div class="form-group">
				<label for="store_psw_check" class="control-label col-sm-2">確認密碼*</label>
				<div class="col-sm-10">				
				<input type="password" class="form-control" id="store_psw_check" placeholder="Password" name="store_psw_check" id="store_psw_check">
				<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_psw_check)?'hidden':'visible'};">
					<font color="red">${errorMsgs.store_psw_check }</font></p>
					</div>
			</div>
			
			<div class="form-group">
				<label for="InputEmail1" class="control-label col-sm-2">電子信箱*<br><font color="red">確認後無法修改</font></label>
				<div class="col-sm-10">
					<input type="text" class="form-control" placeholder="Email" name="store_email" id="store_email" value="<%= (storeVO == null) ?  "" : storeVO.getStore_email() %>">
					<span class="glyphicon  form-control-feedback" style="margin-right:10px" aria-hidden="true" id="emailcheckstate"></span>
				</div>				
				<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_email)?'hidden':'visible'};">
					<font color="red">${errorMsgs.store_email }</font></p>
			</div>
			
			<div class="form-group">
				<label for="InputPhone" class="control-label col-sm-2">聯絡電話*</label>
				<div class="col-sm-10">
					<input type="tel" class="form-control" placeholder="Phone Number" name="store_phone" id="store_phone" value="<%= (storeVO == null) ?  "" : storeVO.getStore_phone() %>">					
					<span class="glyphicon  form-control-feedback" style="margin-right:10px" aria-hidden="true" id="phonecheckstate"></span>					
				</div>
				<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_phone)?'hidden':'visible'};">
					<font color="red">${errorMsgs.store_phone }</font></p>
			</div>
			
			<div class="form-group">
					<label for="store_owner" class="control-label col-sm-2">店家負責人*</label>
					<div class="col-sm-10">
					<input type="text" class="form-control" id="store_owner" placeholder="Owner" name="store_owner" id="store_owner" value="<%= (storeVO == null) ?  "" : storeVO.getStore_owner() %>">
					<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_owner)?'hidden':'visible'};">
					<font color="red">${errorMsgs.store_owner }</font></p>
					</div>
			</div>
			
			<div class="form-group">
				<label for="InputAddr" class="control-label col-sm-2">店家地址*</label>
				<div class="form-inline col-sm-10">				
					郵遞區號(免填)<input type="text" name="zip" id="zipcode" readonly="readonly" size="3" class="form-control" value="${zip }">
					<span>縣市</span> 
					<select class="form-control" name="county" id="zone1" value=""></select> 
					<input type="hidden" id="addr1" value="${county }"> 
					<span>鄉鎮[市]區</span> 
					<select class="form-control" name="city" id="zone2"></select>
					
					<input type="hidden" id="addr2" value="${city }"> 
				</div>
			</div>
			<div class="form-group">
				<label for="zone3" class="control-label col-sm-2"></label>	
				<div class="col-sm-10">		
					<input type="text" class="form-control" id="zone3" name="address" style="margin-top: 2px" value="${address }" size="64" onblur="getLatlng()">
				<!-- 這邊送經緯度 -->
				<input type="hidden" id="latlng" name="store_latlng" value="${store_latlng }" id="store_latlng"> 
				<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_address)?'hidden':'visible'};">
					<font color="red">${errorMsgs.store_address }</font></p>
				</div>
			</div>
			
			<div class="form-group">
			<label for="store_type_no" class="control-label col-sm-2">請選擇店家種類*</label>	
				<div class="col-sm-10">		
				<select size="1" class="form-control" name="store_type_no" id="store_type_no">
					<option value="">請選擇</option>
					<c:forEach var="store_typeVO" items="${store_typeSvc.all }" >
						<option value="${store_typeVO.store_type_no}" ${(store_typeVO.store_type_no==storeVO.store_type_no)? 'selected':'' } >${store_typeVO.store_type_name} 
					</c:forEach>	
					</select>
					<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_type_no)?'hidden':'visible'};">
					<font color="red">${errorMsgs.store_type_no }</font></p>
					</div>
			</div>
			
			<div class="form-group">
				<label for="InputStoreOpen">請選擇店家營業時段*</label>
				<details id="midnightDetails">
					<summary id="midnight">深夜時段 (00:00~05:00) </summary>
						<input type="checkbox" name="time" class="time" value="00" ${(not empty time[0] and time[0] != 0) ? 'checked': ''}>00:00~01:00
							   <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot0" value="1" ${(bookOrNot0==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot0" value="0" ${(bookOrNot0==0)?'checked':'' } >否<br>
							  開放訂位人數：<input type="text" name="bookAmt0" size="2" value="${bookAmt0 }" maxlength="2">
							  <font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot0)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot0}</font>
							</p>
							  </div><br>
						<input type="checkbox" name="time" class="time" value="01" ${(not empty time[1] and time[1] != 0) ? 'checked': ''}>01:00~02:00
							   <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot1"value="1" ${(bookOrNot1==1)?'checked':'' } >是
							  <input type="radio" name="bookOrNot1"value="0" ${(bookOrNot1==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt1" size="2" value="${bookAmt1 }" maxlength="2">
							  <font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot1)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot1}</font>
							</p>
							  </div><br>
							  <input type="checkbox" name="time" class="time" value="02" ${(not empty time[2] and time[2] != 0) ? 'checked': ''}>02:00~03:00
							   <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot2"value="1" ${(bookOrNot2==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot2"value="0" ${(bookOrNot2==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt2" size="2" value="${bookAmt2 }" maxlength="2">
							  <font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot2)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot2}</font>
							</p>
							  </div><br>
							  <input type="checkbox" name="time" class="time" value="03" ${(not empty time[3] and time[3] != 0) ? 'checked': ''}>03:00~04:00
							   <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot3"value="1" ${(bookOrNot3==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot3"value="0" ${(bookOrNot3==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt3" size="2" value="${bookAmt3 }" maxlength="2">
							  <font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot3)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot3}</font>
							</p>
							  </div><br>
							  <input type="checkbox" name="time" class="time" value="04" ${(not empty time[4] and time[4] != 0) ? 'checked': ''}>04:00~05:00
							   <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot4"value="1" ${(bookOrNot4==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot4"value="0" ${(bookOrNot4==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt4" size="2" value="${bookAmt4 }" maxlength="2">
							  <font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot4)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot4}</font>
							</p>
							  </div><br>
						
				</details>
				
				<details id="breakfastDetails">
						 <summary id="breakfast">早餐時段 (05:00~10:00)</summary>
							  <input type="checkbox" name="time" class="time" value="05" ${(not empty time[5] and time[5] != 0) ? 'checked': ''}>05:00~06:00
							   <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot5"value="1" ${(bookOrNot5==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot5"value="0" ${(bookOrNot5==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt5" size="2" value="${bookAmt5 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot5)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot5}</font>
							</p>
							  </div><br>
							 <input type="checkbox" name="time" class="time" value="06" ${(not empty time[6] and time[6] != 0) ? 'checked': ''}>06:00~07:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot6"value="1" ${(bookOrNot6==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot6"value="0" ${(bookOrNot6==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt6" size="2" value="${bookAmt6 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot6)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot6}</font></p>
							  </div><br>
							 <input type="checkbox" name="time" class="time" value="07" ${(not empty time[7] and time[7] != 0) ? 'checked': ''}>07:00~08:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot7"value="1" ${(bookOrNot7==1)?'checked':'' } >是
							  <input type="radio" name="bookOrNot7"value="0" ${(bookOrNot7==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt7" size="2" value="${bookAmt7 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot7)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot7}</font>
							</p>
							  </div><br>
							  <input type="checkbox" name="time" class="time" value="08" ${(not empty time[8] and time[8] != 0) ? 'checked': ''}>08:00~09:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot8"value="1" ${(bookOrNot8==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot8"value="0" ${(bookOrNot8==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt8" size="2" value="${bookAmt8 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot8)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot8}</font>
							</p>
							  </div><br>
							  <input type="checkbox" name="time" class="time" value="09" ${(not empty time[9] and time[9] != 0) ? 'checked': ''}>09:00~10:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot9"value="1" ${(bookOrNot9==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot9"value="0" ${(bookOrNot9==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt9" size="2" value="${bookAmt9 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot9)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot9}</font>
							</p>
							  </div><br>					
					</details>
				<details id="lunchDetails">
						 <summary id="lunch">午餐、下午茶時段 (10:00~17:00)</summary>
							  <input type="checkbox" name="time" class="time" value="10" ${(not empty time[10] and time[10] != 0) ? 'checked': ''}>10:00~11:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot10"value="1" ${(bookOrNot10==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot10"value="0" ${(bookOrNot10==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt10" size="2" value="${bookAmt10 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot10)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot10}</font>
							</p>
							  </div><br>					
							  <input type="checkbox" id="check11" name="time" class="time" value="11" ${(not empty time[11] and time[11] != 0) ? 'checked': ''}>11:00~12:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot11"value="1" ${(bookOrNot11==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot11"value="0" ${(bookOrNot11==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt11" size="2" value="${bookAmt11 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot11)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot11}</font>
							</p>
							  </div><br>					
							  <input type="checkbox" id="check12" name="time" class="time" value="12" ${(not empty time[12] and time[12] != 0) ? 'checked': ''}>12:00~13:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot12"value="1" ${(bookOrNot12==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot12"value="0" ${(bookOrNot12==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt12" size="2" value="${bookAmt12 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot12)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot12}</font>
							</p>
							  </div><br>					
							  <input type="checkbox" id="check13" name="time" class="time" value="13" ${(not empty time[13] and time[13] != 0) ? 'checked': ''}>13:00~14:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot13"value="1" ${(bookOrNot13==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot13"value="0" ${(bookOrNot13==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt13" size="2" value="${bookAmt13 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot13)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot13}</font>
							</p>
							  </div><br>					
							 <input type="checkbox" id="check14" name="time" class="time" value="14" ${(not empty time[14] and time[14] != 0) ? 'checked': ''}>14:00~15:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot14"value="1" ${(bookOrNot14==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot14"value="0" ${(bookOrNot14==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt14" size="2" value="${bookAmt14 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot14)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot14}</font>
							</p>
							  </div><br>					
							  <input type="checkbox" name="time" class="time" value="15" ${(not empty time[15] and time[15] != 0) ? 'checked': ''}>15:00~16:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot15"value="1" ${(bookOrNot15==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot15"value="0" ${(bookOrNot15==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt15" size="2" value="${bookAmt15 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot15)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot15}</font>
							</p>
							  </div><br>					
							  <input type="checkbox" name="time" class="time" value="16" ${(not empty time[16] and time[16] != 0) ? 'checked': ''}>16:00~17:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot16"value="1" ${(bookOrNot16==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot16"value="0" ${(bookOrNot16==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt16" size="2" value="${bookAmt16 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot16)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot16}</font>
							</p>
							  </div><br>										
					</details>
				<details id="nightDetails">
					  <summary id="night">晚餐時段 (17:00~21:00)</summary>
					  <input type="checkbox"  id="check17" name="time" class="time" value="17"  ${(not empty time[17] and time[17] != 0) ? 'checked': ''}>17:00~18:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot17"value="1" ${(bookOrNot17==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot17"value="0" ${(bookOrNot17==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt17" size="2" value="${bookAmt17 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot17)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot17}</font>
							</p>
							  </div></br>					
					  <input type="checkbox" id="check18" name="time" class="time" value="18" ${(not empty time[18] and time[18] != 0) ? 'checked': ''}>18:00~19:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot18"value="1" ${(bookOrNot18==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot18"value="0" ${(bookOrNot18==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt18" size="2" value="${bookAmt18 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot18)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot18}</font>
							</p>
							  </div></br>					
					  <input type="checkbox" id="check19" name="time" class="time" value="19" ${(not empty time[19] and time[19] != 0) ? 'checked': ''}>19:00~20:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot19"value="1" ${(bookOrNot19==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot19"value="0" ${(bookOrNot19==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt19" size="2" value="${bookAmt19 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot19)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot19}</font>
							</p>
							  </div><br>					
					  <input type="checkbox"  id="check20" name="time" class="time" value="20" ${(not empty time[20] and time[20] != 0) ? 'checked': ''}>20:00~21:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot20"value="1"  ${(bookOrNot20==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot20"value="0" ${(bookOrNot20==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt20" size="2" value="${bookAmt20}" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot20)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot20}</font>
							</p>
							  </div><br>					
				</details>
				<details id="latenightDetails">
					  <summary id="latenight">消夜時段 (21:00~24:00)</summary>
					  <input type="checkbox" name="time" class="time" value="21" ${(not empty time[21] and time[21] != 0) ? 'checked': ''}>21:00~22:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot21"value="1" ${(bookOrNot21==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot21"value="0" ${(bookOrNot21==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt21" size="2" value="${bookAmt21}" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot21)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot21}</font>
							</p>
							  </div></br>								
					 <input type="checkbox" name="time" class="time" value="22" ${(not empty time[22] and time[22] != 0) ? 'checked': ''}>22:00~23:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot22"value="1" ${(bookOrNot22==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot22"value="0"  ${(bookOrNot22==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt22" size="2" value="${bookAmt22}" maxlength="2"><font color="red"><i>上限99</i></font>
							 <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot22)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot22}</font>
							</p>
							  </div></br>					
					  <input type="checkbox" name="time" class="time" value="23" ${(not empty time[23] and time[23] != 0) ? 'checked': ''}>23:00~24:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot23"value="1" ${(bookOrNot23==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot23"value="0" ${(bookOrNot23==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt23" size="2" value="${bookAmt23}" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot23)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot23}</font>
							</p>
							  </div><br>										   	
				</details>			
				<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_open)?'hidden':'visible'};">
					<font color="red">${errorMsgs.store_open}</font>
				</p>
			</div>
						
			<div class="form-group">
					<label for="InputIntro">店家介紹</label>
					<textarea id="store_intro" name="store_intro" rows="6" cols="50" class="form-control" id="InputName" placeholder="Enter Store Introduction here"></textarea>
			</div>
					
			<br><br>
			<input type="submit" class="btn btn-success" id="RegisterBtn" onclick="getLatlng()" value="註冊"></input>
			<font size="2" color="red"><span>*為必填</span></font> 
			<input type="hidden" name="action" value="storeRegister">
			<input type="button" id="magical" class="btn btn-success" value="神奇小按鈕" style="margin-left:5em" >			
			</form>
			
			</div>
			</div>
			</div>
		</div>
		<div class="col-md-1"></div>
	</div>
</div>
<!-- register -->
<!-- footer-->
	<jsp:include page="/front-end/footer.html" />
<!-- footer-->

</body>
</html>
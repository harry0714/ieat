<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.store.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/previewimage.js"></script>
<script src="<%=request.getContextPath()%>/js/address.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/sweetalert.css">
<link href="<%=request.getContextPath()%>/css/memberprefecture.css" rel="stylesheet" type="text/css" />
<%
	StoreVO storeVO = (StoreVO) request.getAttribute("storeVO"); 
%>
<jsp:useBean id="store_typeSvc" scope="page" class="com.store_type.model.Store_typeService"></jsp:useBean>
<script>	
// 店家開放時間  開放訂位 及訂位人數的顯示
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
function getLatlng() {
// 取得由地址轉換成的經緯度
	var geocoder = new google.maps.Geocoder();
	var zone1 = $('#zone1').val(); 
	var zone2 = $('#zone2').val();	
	var addr = zone1 + zone2 + $("#zone3").val();
	console.log(addr); 
	
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
}
</script>
<title>iEat - 店家資料更新</title>
</head>
<body>
	<!-- header -->
	<jsp:include page="/front-end/head.jsp" />
	<!-- header -->
	<div class="main-1">
	<div class="container">
		<div class="col-md-3">
		<jsp:include page="/front-end/store/storeMenu.jsp" />
		</div>
		<div class="col-md-9">
				<c:if test="${not empty errorMsgs.elseError }">
					<div>
						<font color='red' size="5">
							<p>${errorMsgs.elseError}</p>
						</font>
					</div>
				</c:if>
					<div class="panel panel-default">
					<div class="panel-heading" style="color:#d15a15;font-size:20px">基本資料</div>
					<div class="panel-body" style="padding: 10%">
				<form action="<%=request.getContextPath() %>/store/store.do" method="post" class="form-horizontal" role="form">
					<div class="form-group">
						<label for="InputName" class="control-label col-sm-2">店家名稱*</label> 
						<div class="col-sm-10">
						<input type="text" class="form-control" id="InputName" placeholder="Name" name="store_name" value="${storeVO.store_name}">
						<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_name)?'hidden':'visible'};">
							<font color="red">${errorMsgs.store_name }</font>
						</p>
						</div>
					</div>
					
					<div class="form-group">
						<label for="InputPhone" class="control-label col-sm-2">店家電話*</label> 
						<div class="col-sm-10">
						<input type="text" class="form-control" id="InputPhone" placeholder="Phone" name="store_phone" value="${storeVO.store_phone}">
						<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_phone)?'hidden':'visible'};">
							<font color="red">${errorMsgs.store_phone}</font>
						</p>
						</div>
					</div>
					
					<div class="form-group">
						<label for="InputEmail" class="control-label col-sm-2">店家信箱*</label> 
						<div class="col-sm-10">
						<input type="text" class="form-control" id="InputEmail" placeholder="Email" name="store_phone" value="${storeVO.store_email}" readonly>
						<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_email)?'hidden':'visible'};">
							<font color="red">${errorMsgs.store_email}</font>
						</p>
						</div>
					</div>

					<div class="form-group">
						<label for="InputOwner" class="control-label col-sm-2">店家負責人*</label>
						<div class="col-sm-10">
						<input type="text" class="form-control" id="InputOwner" placeholder="Owner" name="store_owner" value="${storeVO.store_owner }">
						<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_owner)?'hidden':'visible'};">
						<font color="red">${errorMsgs.store_owner }</font></p>
						</div>
					</div>	
					
					<div class="form-group">
						<label for="InputAddr" class="control-label col-sm-2">店家地址*</label>
						<div class="form-inline col-sm-10">				
							<span>郵遞區號(免填)</span>
								<input type="text" name="zip" id="zipcode" readonly="readonly" size="3" class="form-control" value="${zip }">
							<span>縣市</span> 
								<select class="form-control" name="county" id="zone1" value=""></select> 
								<input type="hidden" id="addr1" value="${county }"> 
							<span>鄉鎮[市]區</span> 
								<select class="form-control" name="city" id="zone2">
									<option value="">請選擇</option> </select>
						<input type="hidden" id="addr2" value="${city }"> 
						<input type="text" class="form-control" name="address" style="margin-top: 2px" value="${address }" size="36" onblur="getLatlng()">
						<input type="hidden" name="store_latlng" value="${storeVO.store_latlng }">
						</div>
						<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_address)?'hidden':'visible'};">
						<font color="red">${errorMsgs.store_address }</font></p>
					</div>				
					
					<div class="form-group">
						<label for="InputStoreType" class="control-label col-sm-2">請選擇店家種類*</label>
							<div class="col-sm-10">
							<select size="1" class="form-control" name="store_type_no" >
								<option value="">請選擇</option>
								<c:forEach var="store_typeVO" items="${store_typeSvc.all }">
									<option value="${store_typeVO.store_type_no}" ${(store_typeVO.store_type_no==storeVO.store_type_no)? 'selected':'' } >${store_typeVO.store_type_name} 
								</c:forEach>	
							</select>
						<p class="bg-danger show" style="visibility:${(empty errorMsgs.store_type_no)?'hidden':'visible'};">
						<font color="red">${errorMsgs.store_type_no }</font></p>
						</div>
					</div>
					
			<div class="form-group">
				<label for="InputStoreOpen">請選擇店家營業時段*</label>
				<details>
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
				
				<details>
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
				<details>
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
							  <input type="checkbox" name="time" class="time" value="11" ${(not empty time[11] and time[11] != 0) ? 'checked': ''}>11:00~12:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot11"value="1" ${(bookOrNot11==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot11"value="0" ${(bookOrNot11==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt11" size="2" value="${bookAmt11 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot11)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot11}</font>
							</p>
							  </div><br>					
							  <input type="checkbox" name="time" class="time" value="12" ${(not empty time[12] and time[12] != 0) ? 'checked': ''}>12:00~13:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot12"value="1" ${(bookOrNot12==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot12"value="0" ${(bookOrNot12==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt12" size="2" value="${bookAmt12 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot12)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot12}</font>
							</p>
							  </div><br>					
							  <input type="checkbox" name="time" class="time" value="13" ${(not empty time[13] and time[13] != 0) ? 'checked': ''}>13:00~14:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot13"value="1" ${(bookOrNot13==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot13"value="0" ${(bookOrNot13==0)?'checked':'' }>否<br>
							  開放訂位人數：<input type="text" name="bookAmt13" size="2" value="${bookAmt13 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot13)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot13}</font>
							</p>
							  </div><br>					
							 <input type="checkbox" name="time" class="time" value="14" ${(not empty time[14] and time[14] != 0) ? 'checked': ''}>14:00~15:00
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
				<details>
					  <summary id="night">晚餐時段 (17:00~21:00)</summary>
					  <input type="checkbox" name="time" class="time" value="17"  ${(not empty time[17] and time[17] != 0) ? 'checked': ''}>17:00~18:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot17"value="1" ${(bookOrNot17==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot17"value="0" ${(bookOrNot17==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt17" size="2" value="${bookAmt17 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot17)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot17}</font>
							</p>
							  </div></br>					
					  <input type="checkbox" name="time" class="time" value="18" ${(not empty time[18] and time[18] != 0) ? 'checked': ''}>18:00~19:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot18"value="1" ${(bookOrNot18==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot18"value="0" ${(bookOrNot18==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt18" size="2" value="${bookAmt18 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot18)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot18}</font>
							</p>
							  </div></br>					
					  <input type="checkbox" name="time" class="time" value="19" ${(not empty time[19] and time[19] != 0) ? 'checked': ''}>19:00~20:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot19"value="1" ${(bookOrNot19==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot19"value="0" ${(bookOrNot19==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt19" size="2" value="${bookAmt19 }" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot19)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot19}</font>
							</p>
							  </div><br>					
					  <input type="checkbox" name="time" class="time" value="20" ${(not empty time[20] and time[20] != 0) ? 'checked': ''}>20:00~21:00
							  <div class="bookable">是否開放訂位？
							  <input type="radio" name="bookOrNot20"value="1"  ${(bookOrNot20==1)?'checked':'' }>是
							  <input type="radio" name="bookOrNot20"value="0" ${(bookOrNot20==0)?'checked':'' }>否</br>
							  開放訂位人數：<input type="text" name="bookAmt20" size="2" value="${bookAmt20}" maxlength="2"><font color="red"><i>上限99</i></font>
							  <p class="bg-danger show" style="visibility:${(empty errorMsgs.bookOrNot20)?'hidden':'visible'};">
									<font color="red">${errorMsgs.bookOrNot20}</font>
							</p>
							  </div><br>					
				</details>
				<details>
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
						<textarea name="store_intro" rows="6" cols="50" class="form-control" id="InputName" placeholder="Enter Store Introduction here">${storeVO.store_intro }</textarea>
					</div>
					
						<input type="hidden" name="action" value="store_update">					
						<input type="hidden" name="store_no" value="${storeVO.store_no }">
						<input type="hidden" name="store_id" value="${storeVO.store_id }">
						<input type="hidden" name="store_psw" value="${storeVO.store_psw }">						
						<input type="hidden" name="store_status" value="${storeVO.store_status }">
						<input type="hidden" name="store_star" value="${storeVO.store_star }">
						<input type="hidden" name="store_validate" value="${storeVO.store_validate }">
						<input type="hidden" name="store_email" value="${storeVO.store_email }">
						<button type="submit" class="btn btn-success">確認修改</button>
						<font size="2" color="red"><span>*為必填</span></font>						
					</form>
				</div>
			</div>
		</div>
	</div><!-- container -->
</div>
<jsp:include page="/front-end/footer.html"/>
<!-- /footer-->			
</body>
</html>
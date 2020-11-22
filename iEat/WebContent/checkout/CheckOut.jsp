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

	function pay1() {
		document.getElementById("card").style.display = "none";
		document.getElementById("cardNum").value="";
	}
	function card() {
		document.getElementById("card").style.display = "";
	}
	function ValidateNumber(e, pnumber)
	{
	    if (!/^\d+$/.test(pnumber))
	    {
	        e.value = /^\d+/.exec(e.value);
	    }
	    return false;
	}
	
	function now() {
		document.getElementById("time1").style.display = "none";
		document.getElementById("time1").value="";
	}
	function after() {
		document.getElementById("time1").style.display = "";
	}
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
					<div class="step active">
						2付款方式確認
					</div>
					&#10151;
					<div class="step">
						3訂單完成
					</div>
				</div>		
	<!-- 表單 -->	
	<!-- 表單 -->	
		<form name="form1" method="post" action="<%=request.getContextPath()%>/ord/ord.do">
<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>	
			<div class="panel panel-success"><!--pay-->
				<div class="panel-heading">選擇付款方式</div>
				<div class="panel-body" style="font-size:1.2em;">
						
							<table class="table01">
								<tr>
									<th><input type="radio" name="ord_paid" onclick="pay1()" value="1" checked></th>
									<td>現場付款</td>
								</tr>
								<tr>
									<th><input type="radio" name="ord_paid" onclick="card()" value="0"></th>
									<td>
										線上刷卡
										<span id="card" style = "display:none;">輸入卡號:
										 <input type="text" name="A" size="4" onkeyup="if(this.value.length==4)document.form1.B.focus(); return ValidateNumber(this,value);">-
										 <input type="text" name="B" size="4" onkeyup="if(this.value.length==4)document.form1.C.focus(); return ValidateNumber(this,value);">-
										 <input type="text" name="C" size="4" onkeyup="if(this.value.length==4)document.form1.D.focus(); return ValidateNumber(this,value);">-
										 <input type="text" name="D" maxlength="4" size="4" onkeyup="return ValidateNumber(this,value);">
										 <font size="3" color="red">請輸入16碼卡號*</font>
										</span>
									</td>
								</tr>
							</table>
					</div>
			</div><!--pay-->
			<%
				SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date current = new Date();
				
				String[] s = new String[7];
				
				for(int i=0; i<s.length; i++) {
					current.setTime(current.getTime()+30*60*1000);//暫時改成兩分鐘
					s[i] = sdFormat.format(current);
				}
				
				String str0[] = s[0].split(" ");
				String str1[] = s[1].split(" ");
				String str2[] = s[2].split(" ");
				String str3[] = s[3].split(" ");
				String str4[] = s[4].split(" ");
				String str5[] = s[5].split(" ");
				String str6[] = s[6].split(" ");
				
				String hms0[] = str0[1].split(":");
				String hms1[] = str1[1].split(":");
				String hms2[] = str2[1].split(":");
				String hms3[] = str3[1].split(":");
				String hms4[] = str4[1].split(":");
				String hms5[] = str5[1].split(":");
				String hms6[] = str6[1].split(":");
 				
				
			%>
			<div class="panel panel-success"><!--pay-->
				<div class="panel-heading">選擇取餐時段</div>
				<div class="panel-body" style="font-size:1.2em;">
						
							<table class="table01">
								<tr>
									<th><input type="radio" name="pickup" onclick="now()" value="<%=s[0]%>" checked></th>
									<td>系統預定:  <span id="nowDay"><%=str0[0]+" "+hms0[0]+":"+hms0[1]%></span></td>
								</tr>
								<tr>
									<th><input type="radio" name="pickup" onclick="after()"></th>
									<td>顧客指定: <span id="nowDay"><%= str0[0]%>
										<select name="time"  id="time1" style = "display:none;">
											<option value="">請選擇時段</option>
											<option value="<%= s[1]%>"><%= hms1[0]+":"+hms1[1]%></option>
											<option value="<%= s[2]%>"><%= hms2[0]+":"+hms2[1]%></option>
											<option value="<%= s[3]%>"><%= hms3[0]+":"+hms3[1]%></option>
											<option value="<%= s[4]%>"><%= hms4[0]+":"+hms4[1]%></option>
											<option value="<%= s[5]%>"><%= hms5[0]+":"+hms5[1]%></option>
											<option value="<%= s[6]%>"><%= hms6[0]+":"+hms6[1]%></option>
										</select>
									</span></td>
								</tr>
							</table>
					</div>
			</div><!--pay-->
				<div class="cur-right">
					<input type="submit" value="送出">
<%-- 					<a class="morebtn hvr-rectangle-in" href="<%=request.getContextPath()%>/checkout/shopping.do" style="text-align:center;">送出</a> --%>
				</div>
		<%	
			Date date = new Date();
			String sampleDate = sdFormat.format(date);
		%>
		<input type="hidden" name="action" value="insert">
<!-- 		<input type="hidden" name="ord_qrcode_status" value="1"> -->
		<input type="hidden" name="mem_no" value="${user.no}">
		<input type="hidden" name="ord_date" value="<%= java.sql.Timestamp.valueOf(sampleDate)%>">	
		</form>
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
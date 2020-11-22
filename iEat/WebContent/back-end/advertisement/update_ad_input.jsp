<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.advertisement.model.*"%>
<%
	AdvertisementVO advertisementVO = (AdvertisementVO) request.getAttribute("advertisementVO");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>廣告資料修改 - update_ad_input.jsp</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/jquery-ui.min.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js"></script>
  <script>
	$(function() {
		$("#ad_startdate").datepicker({
			minDate: 0,
			dateFormat : "yy-mm-dd",
			onClose: function(selectedDate) {
				$("#ad_enddate").datepicker("option", "minDate", selectedDate);
			}
		});
		
		$("#ad_enddate").datepicker({
			defaultDate: "+1w",
			dateFormat : "yy-mm-dd",
			onClose: function(selectedDate) {
				$("#ad_startdate").datepicker("option", "maxDate", selectedDate);
			}
		});
		
		$("#ad_image").on('change',function(e){
			$("#pic_preview").attr({src:URL.createObjectURL(e.target.files[0])});
		});

	});

  </script>
</head>
<body>
<div class="container">
<c:if test="${not empty errorMsgs}">
	<font color="red">請修正以下錯誤:
		<ul>
			<c:forEach var="message" items="${errorMsgs}">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</font>
</c:if>
	<div class="row">
		<h3>修改廣告表單</h3>
		<div class="col-md-4">
			<img id="pic_preview" src="<%=request.getContextPath()%>/back-end/advertisement/ImageAd?ad_no=${updateAdvertisementVO.ad_no}" class="img-responsive"/>
		</div>		
		<div class="col-md-6">
			<form class="form-horizontal" method="post" action="advertisement.do" enctype="multipart/form-data">
			<div class="form-group">
				<label class="control-label col-sm-3" for="ad_imagetitle">廣告標題</label>
				<div class = "col-sm-6"> 
				<input type="text" class="form-control" id="ad_imagetitle" name="ad_imagetitle" value="${updateAdvertisementVO.ad_imagetitle}">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3" for="ad_startdate">開始日期</label>
				<div class = "col-sm-4"> 
				<input type="text" class="form-control" id="ad_startdate" name="ad_startdate" value="${updateAdvertisementVO.ad_startdate}">
				</div>
				</div>
				<div class="form-group">
				<label class="control-label col-sm-3" for="ad_enddate">結束日期</label>
				<div class = "col-sm-4"> 
				<input type="text" class="form-control" id="ad_enddate" name="ad_enddate" value="${updateAdvertisementVO.ad_enddate}">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3" for="ad_imagetitle">連結店家</label>
				<div class = "col-sm-6"> 
				<jsp:useBean id="storeSvc" scope="page" class="com.store.model.StoreService" />
					<select class="form-control" name="store_no">
						<c:forEach var="storeVO" items="${storeSvc.all}">
							<option value="${storeVO.store_no}" ${(storeVO.store_no==updateAdvertisementVO.store_no)?'selected':''}>${storeVO.store_name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3" for="ad_image">廣告圖</label>
				<div class = "col-sm-9"> 
				<input type="file" id="ad_image" name="ad_image">
				</div>
			</div>
			<div class="form-group"> 
			  <div class="col-sm-offset-3 col-sm-10"> 
			  	<input type="hidden" name="action" value="update">
			  	<input type="hidden" name="ad_no" value="${updateAdvertisementVO.ad_no}">
			  	<input type="hidden" name="whichPage" value="<%=request.getParameter("whichPage")%>">
			  	<input type="hidden" name="ad_filter" value="${ad_filter}">
			    <input type="submit" class="btn btn-success" value="修改"> 
			  </div> 
			</div> 
			</form>
		</div>
	</div><!-- end row -->
</div>
</body>
</html>
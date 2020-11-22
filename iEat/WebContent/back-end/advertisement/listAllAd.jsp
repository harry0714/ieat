<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.*"%>
<%@page import="com.advertisement.model.*"%>
<%@page import="com.advertisement.tools.AdStatusType"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js"></script>
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>
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
<title>iEat - 後端管理廣告</title>
<%
response.setHeader("Cache-Control", "no-store"); 
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0);
%> 
<script>
$(document).ready(function(){
	
	<c:if test="${not empty success}">
		swal("Good job!", "${success}", "success")
	</c:if>

	<c:choose>
	  <c:when test="${not empty advertisementVO && not empty advertisementVO.ad_no}">
		$("#form_add").hide();
	  	$("#form_update").show();
	  	activeBtn();
	  </c:when>
	  <c:when test="${not empty advertisementVO}">
		$("#form_add").show();	  
		$("#form_update").hide();
		activeBtn();
	  </c:when>
	  <c:otherwise>
	  	$("#form_add").hide();
	  	$("#form_update").hide();
	  	resetBtn();
	  </c:otherwise>
	</c:choose>

	
    $("#filter").on("change", "input:radio", function(){
        $("#filter").submit();
    });
    
    $(".glyphicon-pencil").click(function() {
		resetBtn();
    	$("#errorMsgs").empty();
    	$("#form_add").hide();
    	$("#ad_list_table").find("tr").removeClass("success");
    	$(this).closest("tr").addClass("success");
    	$.ajax({
            type: "POST",
            url: "advertisement.do",
            data: {action:"get_update_form",ad_no:createQueryString(this)},
            dataType: "json",
            
           	success: function(data){
                   drawForm(data);
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
    
    $("#btn_get_add_form").click(function(){
    	$("#errorMsgs").empty();
    	$("#ad_list_table").find("tr").removeClass("success");
    	$("#form_update").hide();
    	$("#form_add").toggle().find('input:text').val('');
    	$("#store_no").val($("#store_no option:first").val());
    });
    

	$("#ad_startdate_update,#ad_startdate_add").datepicker({
		minDate: 0,
		dateFormat : "yy-mm-dd",
		onClose: function(selectedDate) {
			$("#ad_enddate_update").datepicker("option", "minDate", selectedDate);
		}
	}).css({'background':'#fff','cursor':'auto'});
	
	$("#ad_enddate_update,#ad_enddate_add").datepicker({
		defaultDate: "+1w",
		dateFormat : "yy-mm-dd",
		onClose: function(selectedDate) {
			$("#ad_startdate_update").datepicker("option", "maxDate", selectedDate);
		}
	}).css({'background':'#fff','cursor':'auto'});
    
	$("#ad_image_add").on('change',function(e){
		$("#pic_preview_add").attr({src:URL.createObjectURL(e.target.files[0])});
	});
    
    function drawForm(data){
    	var context = '<%=request.getContextPath()%>';
    	$("#form_update").show();
    	$("#pic_preview_update").attr({src:context+"/back-end/advertisement/ImageAd?ad_no="+data.ad_no});
    	$("#ad_imagetitle_update").val(data.ad_imagetitle);
    	$("#ad_startdate_update").val(data.ad_startdate);
    	$("#ad_enddate_update").val(data.ad_enddate);
    	$("#store_no_update").find('option[value='+data.store_no+']').attr('selected','selected');
    	$("#ad_no_update").val(data.ad_no);
		change_register();
    } 
	
	function change_register(){
		$("#form_update :input").off('change').one('change', activeBtn);
		$("#ad_image_update").on('change',function(e){
			$("#pic_preview_update").attr({src:URL.createObjectURL(e.target.files[0])});
		}); 
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

});
</script>

</head>
<body>
<%@ include file="/back-end/page/head.jsp" %>
<%@ include file="/back-end/page/body.jsp" %> 
<div class="col-sm-9 col-sm-offset-3 col-md-10 col-sm-offset-2 main">

<a href="<%=request.getContextPath()%>/back-end/index.jsp">回首頁</a>
<form id="filter" method="get" action="advertisement.do">
<% 
	List<AdStatusType> types = Arrays.asList(AdStatusType.values());
	request.setAttribute("types", types);
%>
	<label class="radio-inline">
	  	<input type="radio" id="inlineradio" name="ad_filter"
				<c:if test="${param.ad_filter=='on'}">
					checked
				</c:if>
		>全部
	</label>
<c:forEach varStatus="s" var="type" items="${types}">
	<label class="radio-inline">
	  	<input type="radio" id="inlineradio${s.index}" name="ad_filter" value="${s.index}"
	  			<c:catch>
				${(param.ad_filter==s.index)?"checked":""}
				</c:catch>
		>${type.values()[s.index]}
	</label>	
</c:forEach>
	<input type="hidden" name="action" value="filter_ads">
</form>

<c:if test="${list.size()>0}">
<jsp:useBean id="list" scope="request" type="java.util.List" />
<%@ include file="pages/page1.file" %>

<table id="ad_list_table" class="table table-striped" width='800'>
	<tr>
		<th>廣告編號</th>
		<th>廣告標題</th>
		<th>店家名稱</th>
		<th>廣告期間</th>
		<th>狀態</th>
		<th>修改</th>
		<th>刪除</th>		
	</tr>
		<c:forEach var="advertisementVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
			<tr>
				<td>${advertisementVO.ad_no}</td>
				<td>${advertisementVO.ad_imagetitle}</td>
				<td>
				<jsp:useBean id="storeSvc" scope="page" class="com.store.model.StoreService"/>
				${storeSvc.getOneStore(advertisementVO.store_no).store_name}
				</td>		
				<td>${advertisementVO.ad_startdate} ~ ${advertisementVO.ad_enddate}</td>
				<c:set var="status" value="${AdStatusType.getCode(advertisementVO.ad_startdate,advertisementVO.ad_enddate)}"/>				
				<td><span class="label ${AdStatusType.getCssClass(status)}">${AdStatusType.values()[AdStatusType.getCode(advertisementVO.ad_startdate,advertisementVO.ad_enddate)]}</span></td>								
				<td><a href="#"><i class="glyphicon glyphicon-pencil"></i></a></td>
				<td><a class="btn_delete" href="advertisement.do?action=delete&ad_no=${advertisementVO.ad_no}&whichPage=<%=whichPage%>&ad_filter=${ad_filter}"><i class="glyphicon glyphicon-trash"></i></a></td>
			</tr>
		</c:forEach>
</table>
<%@ include file="pages/page2.file" %>

</c:if>

<br>
<a id="btn_get_add_form" href="#"><i class="glyphicon glyphicon-edit"></i>新增廣告</a>

<div  id="errorMsgs">
<c:if test="${not empty errorMsgs}">
	<font color = "red">請修正以下錯誤
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</ul>
	</font>
</c:if>
</div>

<div id="form_update" class="row">
	<h3>修改廣告表單</h3>
	<div class="col-md-4">
		<img id="pic_preview_update" src="<%=request.getContextPath()%>/back-end/advertisement/ImageAd?ad_no=${advertisementVO.ad_no}" class="img-responsive"/>
	</div>		
	<div class="col-md-6">
		<form class="form-horizontal" method="post" action="advertisement.do" enctype="multipart/form-data">
		<div class="form-group">
			<label class="control-label col-sm-3" for="ad_imagetitle_update">廣告標題</label>
			<div class = "col-sm-6"> 
			<input type="text" class="form-control" id="ad_imagetitle_update" name="ad_imagetitle" value="${advertisementVO.ad_imagetitle}">
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="ad_startdate_update">開始日期</label>
			<div class = "col-sm-4"> 
				<div class="input-group">
					<span class="input-group-addon icon_calendar"><i class="glyphicon glyphicon-calendar"></i></span>
					<input type="text" class="form-control" id="ad_startdate_update" name="ad_startdate" value="${advertisementVO.ad_startdate}" readonly>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="ad_enddate_update">結束日期</label>
			<div class = "col-sm-4">
				<div class="input-group">
					<span class="input-group-addon icon_calendar"><i class="glyphicon glyphicon-calendar"></i></span>
					<input type="text" class="form-control" id="ad_enddate_update" name="ad_enddate" value="${advertisementVO.ad_enddate}" readonly>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="store_no_update">連結店家</label>
			<div class = "col-sm-6"> 
			<select class="form-control" id="store_no_update" name="store_no">
					<c:forEach var="storeVO" items="${storeSvc.all}" >
						<option value="${storeVO.store_no}" ${(storeVO.store_no==advertisementVO.store_no)?'selected':''}>${storeVO.store_name}</option>
					</c:forEach>
			</select>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="ad_image">廣告圖</label>
			<div class = "col-sm-9"> 
			<input type="file" id="ad_image_update" name="ad_image">
			</div>
		</div>
		<div class="form-group"> 
		  <div class="col-sm-offset-3 col-sm-10"> 
		  	<input type="hidden" name="action" value="update">
		  	<input type="hidden" id="ad_no_update" name="ad_no">
		  	<input type="hidden" name="whichPage" value="<c:out value="${param.whichPage}" default="1"/>">
		  	<input type="hidden" name="ad_filter" value="${ad_filter}">
		    <input id="btn_update" type="submit" class="btn btn-default" value="修改" disabled> 
		  </div> 
		</div> 
		</form>
	</div>
</div><!-- end row -->

<div id="form_add" class="row">
	<h3>新增廣告表單</h3>
	<div class="col-md-4">
		<img id="pic_preview_add" src="<%=request.getContextPath()%>/back-end/advertisement/ImageAd" class="img-responsive"/>
	</div>		
	<div class="col-md-6">
		<form class="form-horizontal" method="post" action="advertisement.do" enctype="multipart/form-data">
		<div class="form-group">
			<label class="control-label col-sm-3" for="ad_imagetitle_add">廣告標題</label>
			<div class = "col-sm-6"> 
			<input type="text" class="form-control" id="ad_imagetitle_add" name="ad_imagetitle" value="${advertisementVO.ad_imagetitle}">
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="ad_startdate_add">開始日期</label>
			<div class = "col-sm-4">
				<div class="input-group">
					<span class="input-group-addon icon_calendar"><i class="glyphicon glyphicon-calendar"></i></span>
					<input type="text" class="form-control" id="ad_startdate_add" name="ad_startdate" value="${advertisementVO.ad_startdate}" readonly>
				</div>
			</div>
			</div>
			<div class="form-group">
			<label class="control-label col-sm-3" for="ad_enddate_add">結束日期</label>
			<div class = "col-sm-4">
				<div class="input-group">
					<span class="input-group-addon icon_calendar"><i class="glyphicon glyphicon-calendar"></i></span>
					<input type="text" class="form-control" id="ad_enddate_add" name="ad_enddate" value="${advertisementVO.ad_enddate}" readonly>					
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="ad_imagetitle_add">連結店家</label>
			<div class = "col-sm-6"> 
				<select class="form-control" id="store_no" name="store_no">
					<c:forEach var="storeVO" items="${storeSvc.all}" >
						<option value="${storeVO.store_no}" ${(storeVO.store_no==advertisementVO.store_no)?'selected':''}>${storeVO.store_name}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-3" for="ad_image_add">廣告圖</label>
			<div class = "col-sm-9"> 
			<input type="file" id="ad_image_add" name="ad_image">
			</div>
		</div>
		<div class="form-group"> 
		  <div class="col-sm-offset-3 col-sm-10"> 
		  	<input type="hidden" name="whichPage" value="<c:out value="${whichPage}" default="1"/>">
		  	<input type="hidden" name="ad_filter" value="${ad_filter}">		  
		  	<input type="hidden" name="action" value="insert">
		    <input type="submit" class="btn btn-success" value="新增廣告"> 
		  </div> 
		</div> 
		</form>
	</div>
</div><!-- end row -->

<br>
</div>
</body>
</html>
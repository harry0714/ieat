<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.store_photo.model.*"%>
<%@ page import="com.store.model.*"%>
<%
	Map<String, String> store = (Map<String, String>) session.getAttribute("store"); 
%>

<%-- 錯誤表列 --%>
<div class="container">
<c:if test="${not empty errorMsgs}">
	<font color='red'>請修正以下錯誤:
		<ul>
			<c:forEach var="message" items="${errorMsgs}">
				<li>${message}</li>
			</c:forEach>
		</ul>
	</font>
</c:if>

	<FORM METHOD="post" ACTION="<%=request.getContextPath() %>/store_photo/store_photo.do"
		name="form1" enctype="multipart/form-data" class="form-horizontal" >
		<div class="form-group">
			<label class="control-label col-sm-1" ></label>
			<div class="col-sm-4" id="storeresult"></div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-sm-1">照片:</label>
			<div class="col-sm-10">
				<input  type="file" name="photo" id="fileinput1">
			</div>
		</div>
		
		<div class="form-group">
			<div class="col-sm-4">
				<input type="hidden" name="store_no" class="form-control" value="${store.store_no}"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-sm-1">照片名稱：</label>
			<div class="col-sm-4">
				<input type="TEXT" name="photo_name" class="form-control" value="${store_photoVO.photo_name}"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-sm-1" >照片描述：</label>
			<div class="col-sm-4">
				<input type="TEXT" name="photo_des" class="form-control" value="${store_photoVO.photo_des}" />
			</div>
		</div>
		
		<div class="form-group">
			<div class="col-sm-10">
				<input type="hidden" name="action" value="insert"> 
				<input type="submit" value="送出新增" class="btn btn-info">
			</div>
		</div>
		
	</FORM>
</div>


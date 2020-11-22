<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.store_photo.model.*"%>
<%@ page import="com.store.model.*"%>
<%
	Store_photoVO store_photoVO = (Store_photoVO) request.getAttribute("store_photoVO");
	System.out.println("store_photoVO++++++++++++++++++++++"+store_photoVO); 
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
	<h3>修改店家照片：</h3>
	<FORM METHOD="post" ACTION="<%=request.getContextPath() %>/store_photo/store_photo.do" name="form1" enctype="multipart/form-data" class="form-horizontal" >
		<div class="form-group">
			<label class="control-label col-sm-1" ></label>
			<div class="col-sm-4"  >
				<img src="<%=request.getContextPath()%>/photo_read/photo_read.do?photo_no=${store_photoVO.photo_no}" width=200 height=200 id="storephoto" >
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-sm-2">照片:</label>
			<div class="col-sm-10">
				<input  type="file" name="photo" id="fileinput2">
			</div>
		</div>
		
		<div class="form-group">
			<div class="col-sm-4">
				<input id="photo_no" type="hidden" name="photo_no" class="form-control" value="${store_photoVO.photo_no}"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-sm-2" >照片名稱:</label>
			<div class="col-sm-4">
				<input id="photo_name" type="TEXT" name="photo_name" class="form-control"
					value="${store_photoVO.photo_name}"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-sm-2" >照片描述:</label>
			<div class="col-sm-4">
				<input id="photo_des" type="TEXT" name="photo_des" class="form-control" value="${store_photoVO.photo_des}" />
			</div>
		</div>
		<div><font color="red">${store_photoVO.store_no}</font></div>
				<input type="hidden" name="store_no" value="${store_photoVO.store_no}">
				<input type="hidden" name="action" value="update"> 
				<input type="submit" value="修改" class="btn btn-info">
		</FORM>
	</div>

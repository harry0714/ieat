<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.meal.model.*"%>
<%@ page import="com.store.model.*"%>

<%
MealVO mealVO = (MealVO) request.getAttribute("mealVO"); //EmpServlet.java(Concroller), 存入req的empVO物件
%>

<div class="container">
<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>請修正以下錯誤:
		<c:forEach var="message" items="${errorMsgs}">
			<li>${message}</li>
		</c:forEach>
	</font>
</c:if>
</div>

<div class="container" >

<form METHOD="post" ACTION="<%=request.getContextPath() %>/meal/meal.do" enctype="multipart/form-data" class="form-horizontal">
	<div class="form-group" >
	    <label class="control-label col-sm-2">照片:</label>
		<div class="col-sm-10">
	    <img src="<%=request.getContextPath()%>/mealPicRead/mealPicRead.do?meal_no=${mealVO.meal_no}" width=100 height=100 id="mealphoto" >
				<input type="file" name="meal_photo" id="fileinput2">
	    </div>
	 </div>  
	<div class="form-group" >
	    <label class="control-label col-sm-2">餐點名稱:</label>
		<div class="col-sm-10">
	  	<input id="meal_name" type="TEXT" name="meal_name" size="45" value="${mealVO.meal_name}" />
	  	<input id="meal_no" type="hidden" name="meal_no" value="${mealVO.meal_no}">
	  	<input id="meal_status" type="hidden" name="meal_status" value="${mealVO.meal_status}">
		</div>
	</div>
	<div class="form-group" >
	    <label class="control-label col-sm-2">餐點描述:</label>
		<div class="col-sm-10">
	   	<input id="meal_descr" type="TEXT" name="meal_descr" size="45" value="${mealVO.meal_descr}" />
		</div>
	</div>
	<div class="form-group" >
	    <label class="control-label col-sm-2">餐點價格:</label>
		<div class="col-sm-10">
	   	<input id="meal_price" type="TEXT" name="meal_price" size="45" value="${mealVO.meal_price}" />
		</div>
	</div>
	
			<div class="form-group">
				<div class="col-sm-10"> 	
					<input type="hidden" name="action" value="update">
					<input id="meal_no" type="hidden" name="meal_no" value="${mealVO.meal_no}">
					<input type="submit" value="送出修改" class="btn btn-info">
				</div>
			</div>
	</form>
</div>


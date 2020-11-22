<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.meal.model.*"%>
<%@ page import="com.store.model.*"%>


<%
	MealVO mealVO = (MealVO) request.getAttribute("mealVO");
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

	<h3>新增餐點：</h3>
	<FORM METHOD="post" ACTION="<%=request.getContextPath() %>/meal/meal.do" name="form1" enctype="multipart/form-data" class="form-horizontal">
		
		<div class="form-group">
			<div class="col-sm-4" id="photoResult" ></div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-1">照片：</label>
			<div class="col-sm-10">
				<input type="file" name="meal_photo" id="fileinput">
			</div>
		</div>
		<div class="form-group">
		
			<div class="col-sm-4">
				<input type="hidden" name="store_no" class="form-control" value="${storeVO.store_no}"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-1" for="meal_name">餐點名稱：</label>
			<div class="col-sm-4">
				<input type="TEXT" name="meal_name" class="form-control"
					value="${mealVO.meal_name}"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-1" for="meal_descr">餐點描述：</label>
			<div class="col-sm-4">
				<input type="TEXT" name="meal_descr" class="form-control" value="${mealVO.meal_descr}" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-1" for="meal_price">餐點價格：</label>
			<div class="col-sm-4">
				<input type="TEXT" name="meal_price" class="form-control"
					value="${mealVO.meal_price}" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-1" for="meal_status">餐點狀態：</label>
			<div class="col-sm-4">
				<select name="meal_status">
				  <option  value="0">上架</option>
				  <option  value="1">下架</option>
				</select>
			</div>
		</div>
		
		<jsp:useBean id="mealSvc" scope="page" class="com.meal.model.MealService" />
		<div class="form-group">
			<div class="col-sm-10">
				<input type="hidden" name="action" value="insert">
				<input type="submit" value="送出新增" class="btn btn-info">
			</div>
		</div>
	</FORM>
	</div>


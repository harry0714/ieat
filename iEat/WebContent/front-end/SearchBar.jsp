<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- searchBar -->
<div id="searchBar"> 
	<form method="post" action="<%= request.getContextPath() %>/store/store.do">
		<div class="input-group">		
			<input type="hidden" name="action" value="listStores_ByCompositeQuery">				
			<input type="text" class="form-control" placeholder="Search" name="store_name" value="${param.store_name }">
			<span class="input-group-btn">
				<button class="btn btn-success" type="submit">
					<i class="glyphicon glyphicon-search"></i>
				</button>
			</span>							
		</div><br>
		<div class="form-inline">
			<select class="form-control" id="zone1"></select>
			<select class="form-control" id="zone2"></select>
			<input type="hidden" id="store_ads" name="store_ads" value="${param.store_ads }">
			<input type="hidden" name="store_status" value="1">
			<select class="form-control" name="store_type_no">
				<option value="">餐廳種類</option>
					<c:forEach var="stVO" items="${stList}">
						<option value="${stVO.store_type_no }" ${(stVO.store_type_no==param.store_type_no)?"selected":"" }>${stVO.store_type_name}</option>
					</c:forEach>
			</select>
		</div>
	</form>
</div> 
<!-- SearchBar -->
    
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.adm.model.*"%>

<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font color='red'>請修正以下錯誤:
		<ul>
			<c:forEach var="message" items="${errorMsgs}">
				<li>${message}</li>
			</c:forEach>
		</ul>
		
	</font>
</c:if>

<div class="container">
 <input type="button" value="magic" class="btn btn-info" id="magic">
<%-- 	<a href="<%=request.getContextPath()%>/back-end/adm/adm.jsp">回首頁</a> --%>

	<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/adm/adm.do" name="form1" enctype="multipart/form-data" class="form-horizontal">
		<div class="form-group">
			<label class="control-label col-sm-1"></label>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-1">照片:</label>
			<div class="col-sm-10">
				<input type="file" name="adm_photo" id="fileinput">
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-sm-1" >預覽:</label>
			<div class="col-sm-4" id="photoResult" >
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-sm-1" for="adm_name">姓名:</label>
			<div class="col-sm-4">
				<input type="TEXT" name="adm_name" class="form-control" value="${admVO.adm_name}" id="name"/>
			</div>
		</div>

		<div class="form-group">
			<label class="control-label col-sm-1" for="adm_user">帳號:</label>
			<div class="col-sm-4">
				<input type="TEXT" name="adm_user" class="form-control" value="${admVO.adm_user}" id="user"/>
			</div>
		</div>


		<div class="form-group">

			<div class="col-sm-1">
				<input type="hidden" name="adm_psd" class="form-control" value="${admVO.adm_psd}" />
			</div>
		</div>

		<div class="form-group">
			<div class="col-sm-offset-1 col-sm-10">
				<div class="radio">
					<label><input type="radio" name="adm_sex" value="1" id="sex1">男</label>
					 <label><input type="radio" name="adm_sex" value="0" id="sex0">女</label>
				</div>
			</div>
		</div>

		<div class="form-group">
			<label class="control-label col-sm-1">生日:</label>
			<div class="col-sm-4">
				<input type="date" name="adm_bd" class="form-control" value="${admVO.adm_bd}" id="bd"/>
			</div>
		</div>

		<div class="form-group">
			<label class="control-label col-sm-1">信箱:</label>
			<div class="col-sm-4">
				<input type="email" name="adm_email" class="form-control" value="${admVO.adm_email}" id="email"/>
			</div>
		</div>

		<div class="form-group">
			<label class="control-label col-sm-1">電話:</label>
			<div class="col-sm-4">
				<input type="tel" name="adm_phone" class="form-control" value="${admVO.adm_phone}" id="phone"/>
			</div>
		</div>

		<div class="form-group">
			<div class="col-sm-offset-1 col-sm-10">
				<div class="radio">
					<label><input type="radio" name="adm_level" value="1" id="level1">最高權限</label>
					<label><input type="radio" name="adm_level" value="0" id="level0">一般權限</label>
				</div>
			</div>
		</div>

		<div class="form-group">
			<label class="control-label col-sm-1">地址:</label>
			<div class="col-sm-4">
				<input type="text" name="adm_addr" class="form-control" value="${admVO.adm_addr}" id="addr"/>
			</div>
		</div>


		<jsp:useBean id="admSvc" scope="page" class="com.adm.model.AdmService" />
		<div class="form-group">
			<div class="col-sm-10">
				<input type="hidden" name="action" value="insert">
				<input type="submit" value="送出新增" class="btn btn-info">
			</div>
		</div>
	</FORM>

</div>


<script>
$("#magic").click(function(){
 	$("#name").val("梁曉明");
	$("#user").val("herry");
 	
 	$("#level1").attr("checked",true);
 	$("#email").val("herry0000714@Gmail.com");
 	$("#phone").val("0982215704");
 	$("#sex1").attr("checked",true); //設定打勾
 	$("#bd").val("1989-06-19");
 	$("#addr").val("桃園縣好大路178號");

	
 });
</script>


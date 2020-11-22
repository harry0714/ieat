<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.adm.model.*"%>

<%
	AdmVO admVO = (AdmVO) request.getAttribute("admVO");
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

<%--  <a href="<%=request.getContextPath()%>/back-end/adm/adm.jsp">回首頁</a> --%>
<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/adm/adm.do" name="form1" enctype="multipart/form-data" class="form-horizontal">

	<div class="form-group" >
	    <label class="control-label col-sm-1">照片:</label>
		<div class="col-sm-10">
	    <img  src="<%=request.getContextPath()%>/PhotoReader?adm_no=${admVO.adm_no}" width=200 height=200 id="xxx" >
	    <input type="file"   name="adm_photo" id="fileinput2">
	    </div>
	</div>
	
	<div class="form-group">
      <label class="control-label col-sm-1" for="adm_name">姓名:</label>
      <div class="col-sm-4">          
		<input id="adm_name" type="TEXT" name="adm_name"  class="form-control" value="${admVO.adm_name }" />
      </div>
    </div>
	
	<div class="form-group">
      <label class="control-label col-sm-1" for="adm_user">帳號:</label>
      <div class="col-sm-4">          
		<input id="adm_user" type="TEXT" name="adm_user"  class="form-control" value="${admVO.adm_user}" />
      </div>
    </div>
	
	<div class="form-group">
      <label class="control-label col-sm-1" for="adm_psd">密碼:</label>
      <div class="col-sm-4">          
		<input id="adm_psd" type="password" name="adm_psd" class="form-control" value="${ admVO.adm_psd}" />
      </div>
    </div>
	
	<div class="form-group">   
		<label class="control-label col-sm-1" >性別:</label>     
      <div class="control-label col-sm-3">
        <div class="radio">
          <label class="control-label col-sm-3"><input id="adm_sex1" type="radio" name="adm_sex" value="1"> 男</label>
          <label class="control-label col-sm-4"><input id="adm_sex2" type="radio" name="adm_sex" value="0" > 女</label>
        </div>
      </div>
    </div>
	
	<div class="form-group">
      <label class="control-label col-sm-1">生日:</label>
      <div class="col-sm-4">          
		<input id="adm_bd" type="date" name="adm_bd"  class="form-control" value="${ admVO.adm_bd}" />
      </div>
    </div>
	
	<div class="form-group">
      <label class="control-label col-sm-1">信箱:</label>
      <div class="col-sm-4">          
		<input id="adm_email" type="email" name="adm_email"  class="form-control" value="${ admVO.adm_email}" />
      </div>
    </div>
	
	<div class="form-group">
      <label class="control-label col-sm-1">電話:</label>
      <div class="col-sm-4">          
		<input id="adm_phone" type="tel" name="adm_phone"  class="form-control" value="${ admVO.adm_phone}" />
      </div>
    </div>
	
	<div class="form-group">
	 <label class="control-label col-sm-1">權限:</label>        
      <div class=" col-sm-5">
        <div class="radio">
       	<c:if test="${admuser.adm_level==1 }">
          <label  class="col-sm-3"><input id="adm_level1" type="radio" name="adm_level" value="1">最高權限</label>
          </c:if>
          <c:if test="${admuser.adm_level==0 ||admuser.adm_level==1}">
          <label  class="col-sm-3"><input id="adm_level2" type="radio" name="adm_level" value="0" >一般權限</label>
          </c:if>
        </div>
      </div>
    </div>
    
	<div class="form-group">
      <label class="control-label col-sm-1">地址:</label>
      <div class="col-sm-4">          
		<input id="adm_addr" type="text" name="adm_addr"  class="form-control" value="${ admVO.adm_addr}" />
      </div>
    </div>
	
	<jsp:useBean id="admSvc" scope="page" class="com.adm.model.AdmService" />
			<div class="form-group">
				<div class="col-sm-10"> 	
					<input type="hidden" name="action" value="update">
					<input id="adm_no" type="hidden" name="adm_no" value="${ admVO.adm_no}">
					<input type="submit" value="送出修改" class="btn btn-info">
				</div>
			</div>
		</FORM>
	</div>


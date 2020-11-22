<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ include file="/back-end/page/head.jsp"%>
<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1 class="page-header"></h1>


	<h3>資料查詢:</h3>
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
	<ul>
		<li><a href='listAllQa.jsp'>List</a> all Emps.</li>
		<br>
		<br>

		<li>
			<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/qa/qa.do">
				<b>問題編號 :</b> <input type="text" name="qa_no"> <input
					type="submit" value="送出"> <input type="hidden"
					name="action" value="getOne_For_Display">
			</FORM>
		</li>

		<jsp:useBean id="qatypeSvc" scope="page"
			class="com.qa_type.model.Qa_typeService" />
		<jsp:useBean id="qaSvc" scope="page" class="com.qa.model.QaService" />
		<li>
			<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/qa/qa.do">
				<b>問題編號:</b> <select size="1" name="qa_no">
					<c:forEach var="qaVO" items="${qaSvc.all}">
						<option value="${qaVO.qa_no}">${qaVO.qa_no}
					</c:forEach>
				</select> <input type="submit" value="送出"> <input type="hidden"
					name="action" value="getOne_For_Display">
			</FORM>
		</li>

		<li>
			<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/qa/qa.do">
				<b>問題種類編號:</b> <select size="1" name="qa_type">
					<c:forEach var="qatypeVO" items="${qatypeSvc.all}">
						<option value="${qatypeVO.qa_type_no}">${qatypeVO.qa_type_name}
					</c:forEach>
				</select> <input type="submit" value="送出"> <input type="hidden"
					name="action" value="getOne_For_Display">
			</FORM>
		</li>
	</ul>


	<h3>員工管理</h3>

	<ul>
		<li><a href='addQa.jsp'>Add</a> a new Emp.</li>
	</ul>
</div>
</div>
</div>
<%@ include file="/back-end/page/body.jsp"%>
</body>

</html>

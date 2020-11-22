<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.store.model.*"%>

<%
	StoreService storeSvc = new StoreService();
	List<StoreVO> list = storeSvc.getAll();
	pageContext.setAttribute("list", list);
%>
<%@ include file="/back-end/page/head.jsp"%>
<%@ include file="/back-end/page/body.jsp"%>
<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1 class="page-header"></h1>
	<table class="table table-striped">
		<thead>
			<tr>
				<th>店家編號</th>
				<th>店名</th>
				<th>電話</th>
				<th>店家負責人</th>
				<th>店家狀態</th>
			</tr>
		</thead>
		<tbody>
		<tbody>
			<%@ include file="/back-end/page/page1.file"%>
			<c:forEach var="storeVO" items="${list}" begin="<%=pageIndex%>"
				end="<%=pageIndex+rowsPerPage-1%>">
				<tr>
					<td>${storeVO.store_no}</td>
					<td>${storeVO.store_name}</td>
					<td>${storeVO.store_phone}</td>
					<td>${storeVO.store_owner}</td>
					<td>
					<c:if test="${storeVO.store_status=='0'}">已停權</c:if>
					<c:if test="${storeVO.store_status=='1'}">正常營業</c:if>
					<c:if test="${storeVO.store_status=='2'}">待審核</c:if>
					<c:if test="${storeVO.store_status=='3'}">停止營業</c:if>
				</div>
					</td>


					<td>
						<FORM METHOD="post"
							ACTION="<%=request.getContextPath()%>/store/store.do">

							<button type="submit" class="btn btn-info" data-toggle="modal"
								data-target="#myModal">詳細資料</button>

							<input type="hidden" name="store_no" value="${storeVO.store_no}">
							<input type="hidden" name="action" value="getOne_For_display">
						</FORM>
					</td>


					<td>
						<FORM METHOD="post"
							ACTION="<%=request.getContextPath()%>/store/store.do">
							<input type="submit" value="啟用"> <input type="hidden"
								name="store_no" value="${storeVO.store_no}"> <input
								type="hidden" name="action" value="getOne_For_disable">
						</FORM>
					</td>
					<td>
						<FORM METHOD="post"
							ACTION="<%=request.getContextPath()%>/store/store.do">
							<input type="submit" value="停用"> <input type="hidden"
								name=store_no value="${storeVO.store_no}"> <input
								type="hidden" name="action" value="getOne_For_enable">
						</FORM>
					</td>
				</tr>
			</c:forEach>
		</tbody>
		<%@ include file="/back-end/page/page2.file"%>

	</table>

</div>



<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.store.model.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="<%=request.getContextPath()%>/back-end/index.jsp">
<title>iEat - ��ݩ��a�f��</title>

<!-- CSS -->
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
<link href="<%=request.getContextPath()%>/css/c3.css" rel="stylesheet" type="text/css">
<!-- CSS -->

<!-- JS -->  
<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>
<script src="<%=request.getContextPath()%>/js/d3.min.js" charset="utf-8"></script>
<script src="<%=request.getContextPath()%>/js/c3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script src="<%=request.getContextPath()%>/js/indexsocket.js"></script>
<script src="<%=request.getContextPath()%>/js/ie-emulation-modes-warning.js"></script>
<script type="text/javascript" src="https://maps.google.com/maps/api/js?key=AIzaSyANhvv_DDadWfAzIGurcl8fZixWAdfrgQk"></script>
<!-- JS -->
<%-- �����m�߱ĥ� EL ���g�k���� --%>
<script>
								$(document).ready(function(){
									$(".glyphicon-ok").click(function(){
										$(this).closest("form").submit();
									})
								})
							

								$(document).ready(function(){
									$(".glyphicon-remove").click(function(){
										$(this).closest("form").submit();
									})
								})
								$(document).ready(function(){
									$(".glyphicon-exclamation-sign").click(function(){
										$(this).closest("form").submit();
									})
								})
								$(document).ready(function(){
									$(".glyphicon-check").click(function(){
										$(this).closest("form").submit();
									})
								})
								</script>		
<%
	StoreService storeSvc = new StoreService();
	List<StoreVO> list = storeSvc.findBy_reviewed();
	pageContext.setAttribute("list", list);
%>
</head>
<body>
<%@ include file="/back-end/page/head.jsp"%>
<%@ include file="/back-end/page/body.jsp"%>

<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1 class="page-header">���a�˵�</h1>
	<table class="table table-striped">
		<thead>
			<tr>
				<th>���a�s��</th>
				<th>���a�b��</th>
				<th>���a�W��</th>
				<th>���a�q��</th>
				<th>���a�t�d�H</th>
				<th>���a�H�c</th>
				<th>���a�ҥΤ��</th>
				<th>���a���A</th>
				<th>���v/�ҥ�</th>
				<th>����</th>
			</tr>
		</thead>
		<tbody>
			<%@ include file="/back-end/page/page1.file"%>
			<c:forEach var="storeVO" items="${list}" begin="<%=pageIndex%>"
				end="<%=pageIndex+rowsPerPage-1%>">
				<tr>
					<td>${storeVO.store_no}</td>
					<td>${storeVO.store_id}</td>
					<td>${storeVO.store_name}</td>
					<td>${storeVO.store_phone}</td>
					<td>${storeVO.store_owner}</td>
					<td>${storeVO.store_email}</td>
					
					<c:if test="${!(storeVO.store_validate==null)}">
					  <td>${storeVO.store_validate}</td>
					</c:if>
					<c:if test="${(storeVO.store_validate==null)}">
					 <td>�w����</td>
					</c:if>
					
					<td><c:if test="${storeVO.store_status=='0'}"><button type="button" class="btn btn-danger btn-xs">�w���v</button> </c:if>
						<c:if test="${storeVO.store_status=='1'}"><button type="button" class="btn btn-Success btn-xs">���`��~</button> </c:if> 
						<c:if test="${storeVO.store_status=='2'}"><button type="button" class="btn btn-danger btn-xs">�ݼf��</button></c:if> 
						<c:if test="${storeVO.store_status=='3'}"><button type="button" class="btn btn-Warning btn-xs">������~</button></c:if>
					</td>


						
						<td>
						<c:if test="${(storeVO.store_status=='1')}">
							<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/store/store.do">
								<a href="#"><i class="glyphicon glyphicon-exclamation-sign"></i></a>	
<!-- 								<input type="submit" value="���v">  -->
								<input type="hidden" name=store_no value="${storeVO.store_no}"> 
								<input type="hidden" name="action" value="getOne_For_disable">
							</FORM>
						</c:if>	
						<c:if test="${(storeVO.store_status=='0')}">
							<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/store/store.do">
								<a href="#"><i class="glyphicon glyphicon-check"></i></a>	

<!-- 								<input type="submit" value="��_�v��">  -->
								<input type="hidden" name=store_no value="${storeVO.store_no}"> 
								<input type="hidden" name="action" value="getOne_For_return">
							</FORM>
						</c:if>	
						</td>

						
						<td>
							<c:if test="${(storeVO.store_status=='3')}">
							<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/store/store.do">

								<a href="#"><i class="glyphicon glyphicon-ok"></i></a>	
<!-- 								<input type="submit" value="�}��">  -->
								<input type="hidden" name="store_no" value="${storeVO.store_no}"> 
								<input type="hidden" name="action" value="getOne_For_enable">
							</FORM>	
							</c:if>
							<c:if test="${storeVO.store_status!='3'}">					
							<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/store/store.do">
								<a href="#"><i class="glyphicon glyphicon-remove"></i></a>
<!-- 								<input type="submit" value="����">  -->
								<input type="hidden" name=store_no value="${storeVO.store_no}"> 
								<input type="hidden" name="action" value="getOne_For_stop">
							</FORM>
							</c:if>
						</td>
				</tr>
			</c:forEach>
		</tbody>
		<%@ include file="/back-end/page/page2.file"%>
	</table>
</div>
<!-- Bootstrap core JavaScript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script>
	window.jQuery
			|| document
					.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')
</script>
<script src="js/bootstrap.min.js"></script>
<script src="js/holder.min.js"></script>
<script src="js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>




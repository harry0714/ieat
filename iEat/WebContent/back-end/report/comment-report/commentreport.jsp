<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.comment_report.model.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="com.store_comment.model.*"%>


<jsp:useBean id="memberSvc" scope="page"
	class="com.member.model.MemberService" />
<jsp:useBean id="scSvc" scope="page"
	class="com.store_comment.model.StoreCommentService" />

<%
	List<MemberVO> memberList = memberSvc.getAll();
	List<StoreCommentVO> scList = scSvc.getAll();
	pageContext.setAttribute("memberList", memberList);
	pageContext.setAttribute("scList", scList);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
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

<!-- Logo --><link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/eat.png" /><!-- Logo -->

<title>iEat - 後端餐廳評論檢舉</title>
<script>
$(document).ready(function() {
	
	$("#status_selected").change(function(){
		$("#statusform").submit();
	});
});
</script>
</head>
<body>
	<!-- header -->
		<%@ include file="/back-end/page/head.jsp" %> 
	<!-- header -->
	<!-- SideBar -->
		<%@ include file="/back-end/page/body.jsp" %> 
	<!-- SideBar -->
	<div class="container-fluid">
		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<c:if test="${not empty errorMessage}">
				<font color='red'>請修正以下錯誤:
					<ul>
						<c:forEach var="message" items="${errorMessage}">
							<li>${message}</li>
						</c:forEach>
					</ul>
				</font>
			</c:if>
			<div class="panel panel-default">
				<div class="panel-heading">
					
					<h2>餐廳評論檢舉</h2>
				</div>
				<table class="table">
					<thead>
						<form action="<%=request.getContextPath()%>/comment_report/comment_report.do" id="statusform" method="post">
							<input type="hidden" name=action value="entercommentreport">
							<tr>
								<th>篩選:
								
									<select name="status_selected" id="status_selected">
										<option value="" ${(param.status_selected == "")?"selected": ""}>全部</option>
										<option value="0" ${(param.status_selected == "0")?"selected": ""}>未審核</option>
										<option value="1" ${(param.status_selected == "1")?"selected": ""}>通過</option>
										<option value="2" ${(param.status_selected == "2")?"selected": ""}>不通過</option>
									</select>
								</th>
							</tr>
						</form>
						<tr>
							<th>#</th>
							<th>店家評論檢舉編號</th>
							<th>評論編號</th>
							<th>評論會員編號/名稱</th>
							<th>評論內容</th>
							<th>檢舉人編號/姓名</th>
							<th>檢舉內容</th>
							<th>檢舉日期</th>
							<th>審核狀態</th>
							<th colspan="2">審核</th>

						</tr>
					</thead>
					<tbody>
						<c:forEach var="crVO" items="${crList}" varStatus="s">
							<c:set var="crVO" value="${crVO}" />
							<%
								CommentReportVO crVO = (CommentReportVO) pageContext.getAttribute("crVO");
									String comment_report_status = null;
									switch (crVO.getComment_report_status().charAt(0)) {
									case '0':
										comment_report_status = "未審核";
										break;
									case '1':
										comment_report_status = "通過";
										break;
									case '2':
										comment_report_status = "不通過";
										break;
									}
									pageContext.setAttribute("comment_report_status", comment_report_status);
							%>
							<tr
								class="${((crVO.comment_report_no==param.comment_report_no))?'info':((s.index%2)==0?'success':'') }">
								<td style="vertical-align: middle">${s.count}</td>
								<td style="vertical-align: middle">${crVO.comment_report_no }</td>
								<td style="vertical-align: middle">${crVO.comment_no }</td>
								
								<c:forEach var="scVO" items="${scList }">
									<c:if test="${crVO.comment_no == scVO.comment_no}">
										<c:forEach var="memberVO" items="${memberList }">
											<c:if test="${scVO.mem_no == memberVO.mem_no}">
												<c:set var="spottedMember" value="${scVO.mem_no}"></c:set>
												<td style="vertical-align: middle">${scVO.mem_no }<br>${memberVO.mem_name}</td>
											</c:if>
										</c:forEach>
									</c:if>
								</c:forEach>
								
								<c:forEach var="scVO" items="${scList }">
									<c:if test="${crVO.comment_no == scVO.comment_no}">	
										<td style="vertical-align: middle">${scVO.comment_conect }</td>
									</c:if>
								</c:forEach>
								
								<c:forEach var="memberVO" items="${memberList }">
									<c:if test="${crVO.mem_no == memberVO.mem_no}">
										<td style="vertical-align: middle">${crVO.mem_no }<br>${memberVO.mem_name}</td>
									</c:if>
								</c:forEach>
								
								<td style="vertical-align: middle">${crVO.comment_report_context }</td>
								<td style="vertical-align: middle">${crVO.comment_report_date }</td>
								<td style="vertical-align: middle">${comment_report_status }</td>

								<td style="vertical-align: middle"><c:if
										test="${crVO.comment_report_status == '0'}">
										<form action="<%=request.getContextPath()%>/comment_report/comment_report.do" method="post">
											<input type="hidden" name="action" value="checkpass">
											<input type="hidden" name="comment_report_no" value="${crVO.comment_report_no}">
											<input type="hidden" name="spotted_member" value="${spottedMember}"> <!-- 被檢舉的會員 -->
											<input type="hidden" name="mem_no" value="${crVO.mem_no}"> <!-- 檢舉人 -->
											<input type="hidden" name="status_selected" value="${param.status_selected }">
											<button type="submit" class="btn btn-success">通過</button>
										</form>
									</c:if></td>
								<td style="vertical-align: middle"><c:if
										test="${crVO.comment_report_status == '0'}">
										<form
											action="<%=request.getContextPath()%>/comment_report/comment_report.do" method="post">
											<input type="hidden" name="action" value="checkfail">
											<input type="hidden" name="comment_report_no"
												value="${crVO.comment_report_no}">
											<input type="hidden" name="status_selected" value="${param.status_selected }">
											<button type="submit" class="btn btn-success">不通過</button>
										</form>
									</c:if></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>

<%
	memberList = null;
	scList = null;
%>
</html>
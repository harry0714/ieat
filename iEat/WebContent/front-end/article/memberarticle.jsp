<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.article.model.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/sweetalert.css">
<link href="<%=request.getContextPath()%>/css/memberprefecture.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/memberprefecture.css" rel="stylesheet" type="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/tinymce/tinymce.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script>
	tinymce.init({
		selector : $('#editor')
	});
</script>
</head>
<body>
	<!-- header -->
	<jsp:include page="/front-end/head.jsp" />
	<!-- header -->
	<!-- register -->
	<div class="main-1">
		<div class="container">
			<div class="col-md-3">
				<jsp:include page="/front-end/member/membermenu.jsp" />
			</div>

			<div class="col-md-9">
				<c:if test="${! empty errorMessage.elseError }">
					<div>
						<font color='red' size="5">
							<p>${errorMessage.elseError}</p>
						</font>
					</div>
				</c:if>
				<div class="panel panel-default">
					<div class="panel-heading" style="color:#d15a15;font-size:20px">食記列表</div>
				<table class="table">
					<thead>
						<tr>
							<th>#</th>
							<th>日期</th>
							<th>文章標題</th>
							<th>編輯</th>
							<th>刪除</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach var="artVO" items="${artList}" varStatus="s">
							<c:set var="artVO" value="${artVO }" />
							<%
								ArticleVO artVO = (ArticleVO) pageContext.getAttribute("artVO");
									String art_date = new SimpleDateFormat("yyyy-MM-dd hh:mm")
											.format((java.sql.Timestamp) artVO.getArt_date());
									pageContext.setAttribute("art_date", art_date);
							%>
							<tr
								class="${((artVO.art_no==param.art_no))?'info':((s.index%2)==0?'success':'') }">
								<td scope="row" style="vertical-align: middle;">${s.count}</td>
								<td style="vertical-align: middle;">${art_date}</td>
								<td style="vertical-align: middle;">${artVO.art_name}</td>
								<td>
									<form action="<%=request.getContextPath()%>/article/article.do">
										<input type="hidden" name="action" value="entereditarticle">
										<input type="hidden" name="art_no" value="${artVO.art_no}">
										<button
											class="btn  ${((s.index%2)==0)?'bg-success':'btn-default' }">
											<span class="glyphicon glyphicon-pencil"></span>
										</button>
									</form>
								</td>
								<td>
									<form action="<%=request.getContextPath()%>/article/article.do">
										<input type="hidden" name="action" value="deletearticle">
										<input type="hidden" name="art_no" value="${artVO.art_no }">
										<button
											class="btn  ${((s.index%2)==0)?'bg-success':'btn-default' }">
											<span class="glyphicon glyphicon-trash"></span>
										</button>
									</form>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<!-- register -->
	<!-- footer-->
	<jsp:include page="/front-end/footer.html" />
	<!-- footer-->
</body>
</html>
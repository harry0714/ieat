<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	
	String today = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm").format(new java.sql.Timestamp(System.currentTimeMillis()));
	pageContext.setAttribute("today", today);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/sweetalert.css">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script src="<%=request.getContextPath()%>/js/tinymce/tinymce.min.js"></script>
	<link href="<%=request.getContextPath()%>/css/memberprefecture.css"
	rel="stylesheet" type="text/css" />
<script>
	tinymce.init({
		selector : $('#editor')
	});	
</script>
<script>
	$(document).ready(function() {
		$('#art_date').val('${today}');
		$('#btn_submit').click(function() {
			$('#editor').tinymce().save();
			$('#editor_form').submit();
		})
		$('#btn_preview').click(function(){
			var content = tinyMCE.activeEditor.getContent({format : 'html'});
			var art_name = $('#art_name').val();
			var art_date = $('#art_date').val();
			$.ajax({
				type:"POST",
				url:"<%=request.getContextPath()%>/article/article.do",
				data:{action:"store",art_context:content,art_name:art_name,art_date:art_date},
				success : function(data) {
					window.open("<%=request.getContextPath()%>/article/article.do?action=preview","_blank",config="toolbar=no,menubar=no");
				},
				error : function() {
					alert("error");
				}
			});
		});
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
					<div class="panel-heading" style="color:#d15a15;font-size:20px">撰寫食記</div>
					<div class="panel-body">
				<form id="editor_form"
					action="<%=request.getContextPath()%>/article/article.do"
					method="post">
					<div class="form-group">
							<label></label>
							<input type="text" class="form-control" name="art_name"  placeholder="請輸入食記標題   不要大於30字" id="art_name">
							<c:if test="${! empty errorMessage.art_name}">
							<font color=red><p>${errorMessage.art_name}</p></font>
							</c:if>
					</div>
					<div class="form-inline ">
							<label></label>
							<input type="datetime-local" class="form-control" name="art_date" id="art_date" >
					</div>
					<hr/>
					<c:if test="${! empty errorMessage.art_context}">
							<font color=red><p>${errorMessage.art_context}</p></font>
					</c:if>
					<textarea id="editor" name="art_context"></textarea>
					<input type="hidden" name="action" value="submit">
					<br/>
					<button class="btn btn-success" id="btn_submit">發表文章</button>
				</form>
				<button style="margin-left:100px;margin-top:-34px" class="btn btn-success" id="btn_preview">預覽</button>

				</div>
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
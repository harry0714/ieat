<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.article.model.*"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>




<head>
<link rel="shortcut icon" type="image/x-icon"
	href="<%=request.getContextPath()%>/images/iEat_logo.png" />
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<link href="<%=request.getContextPath()%>/css/meal.css" rel="stylesheet"
	type="text/css" media="all" />
<link href="<%=request.getContextPath()%>/css/sweetalert.css"
	rel="stylesheet" type="text/css" media="all" />
	<title>Article: Home</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>

</head>
<jsp:useBean id="artSvc" class="com.article.model.ArticleService" /> 
<%
	List<ArticleVO> list = artSvc.getAll();
	pageContext.setAttribute("list", list);
%>
<jsp:useBean id="memberSvc" scope="page"
	class="com.member.model.MemberService" />

<!-- header -->
<jsp:include page="/front-end/head.jsp" />
<!-- header -->
<!-- register -->


<%-- 萬用複合查詢-以下欄位-可隨意增減 --%>

<div class="main-1">
	<div class="container">

		<c:if test="${not empty errorMsgs}">
			<font color='red'>請修正以下錯誤: <c:forEach var="message"
					items="${errorMsgs}">
					<li>${message}</li>
				</c:forEach>
			</font>
		</c:if>
			<div class="col-md-3">			
					<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/article/article.do" name="form1" id="search">
			<div class="input-group">		
			<input type="hidden" name="action" value="listArticle_ByCompositeQuery">				
			<input type="text" name="art_name" class="form-control" required
				placeholder="哈囉! 請輸入食記關鍵字!">
			<span class="input-group-btn">
				<button class="btn btn-success" type="submit">
					<i class="glyphicon glyphicon-search"></i>
				</button>
			</span>							
		</div>
		</FORM>
			</div>
			<div class="col-md-9">
			<%@ include file="file/Afile01"%>
			<div class="well">
					<c:forEach var="articleVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
				<!-- 	食記的名稱 -->
				<div class="single_art" style="margin-bottom:10px">
					<div class="art_header">
					<a href="<%=request.getContextPath()%>/article/article.do?action=getFor_Display&art_no=${articleVO.art_no}">${articleVO.art_name}</a>
					<span style="float:right">【<font color="#1e675a">${memberSvc.getOneMember(articleVO.mem_no).mem_name}</font>】${articleVO.art_date}</span>
					</div>
					<div class="art_content">
						<div class="col-md-6">
							<div class="pic">
							<img class="img-responsive"
							src="
							<c:out value="${artSvc.getArticelImageByArt_no(articleVO.art_no)}" default="${pageContext.request.contextPath}/images/default_image.jpeg"/>
							" />
							</div>
						</div>
							<div class="col-md-6">
							<!-- 		食記的內容		 -->

									<p style="overflow: hidden;line-height:1.4em;height:11.2em;">${artSvc.getContext(articleVO.art_no,200)}</p>
									<a class="see" href="<%=request.getContextPath()%>/article/article.do?action=getFor_Display&art_no=${articleVO.art_no}">....繼續閱讀</a>

							</div>
						<div class="clearfix"></div>
					</div>
				</div>
		</c:forEach>
		</div>
		<div style="text-align:center"><%@ include file="file/Afile02"%></div>
			</div>
	</div>
</div>


<!-- /register -->
<!-- footer-->
<jsp:include page="/front-end/footer.html" />
<!-- /footer-->


</body>
</html>
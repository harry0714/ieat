<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- 萬用複合查詢-可由客戶端select_page.jsp隨意增減任何想查詢的欄位 --%>
<%-- 此頁只作為複合查詢時之結果練習，可視需要再增加分頁、送出修改、刪除之功能--%>

<jsp:useBean id="listArticle_ByCompositeQuery" scope="request" type="java.util.List" />
<jsp:useBean id="artSvc" scope="page"
	class="com.article.model.ArticleService" />
<jsp:useBean id="memberSvc" scope="page"
	class="com.member.model.MemberService" />
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all">
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
</head>

<body bgcolor='white'>
	<jsp:include page="/front-end/head.jsp" />

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
				<FORM METHOD="post"
					ACTION="<%=request.getContextPath()%>/article/article.do"
					name="form1" id="search">
					<div class="input-group">
						<input type="hidden" name="action"
							value="listArticle_ByCompositeQuery"> <input type="text"
							name="art_name" class="form-control" required
							placeholder="哈囉! 請輸入食記關鍵字!"> <span
							class="input-group-btn">
							<button class="btn btn-success" type="submit">
								<i class="glyphicon glyphicon-search"></i>
							</button>
						</span>
					</div>
				</FORM>
			</div>
			<div class="col-md-9">
				<c:forEach var="articleVO" items="${listArticle_ByCompositeQuery}">
					<!-- 	食記的名稱 -->
					<div class="single_art" style="margin-bottom: 10px">
						<div class="art_header">
							${articleVO.art_name}<span style="float: right">【<font
								color=purple>${memberSvc.getOneMember(articleVO.mem_no).mem_name}</font>】${articleVO.art_date}
							</span>
						</div>
						<div class="art_content"
							style="border: 1px solid #ccc; padding: 15px; background: white">
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
								<a class="see"
									href="<%=request.getContextPath()%>/article/article.do?action=getFor_Display&art_no=${articleVO.art_no}">....繼續閱讀</a>
							</div>
							<div class="clearfix"></div>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>

	<jsp:include page="/front-end/footer.html" />
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="javax.mail.*" %>
<%@ page import="javax.mail.internet.*" %>
<%@ page import="javax.activation.*" %>    
<%@ page import="com.email.model.*" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Date"%>
<%@ page import="com.qa.model.*"%>
<%@ page import="com.qa_type.model.*" %>
<%@ page import="java.util.*"%>

<%QaVO qaVO =  (QaVO) request.getAttribute("qaVO"); %>

<%
	QaService qaSvc = new QaService();
	List<QaVO> list = qaSvc.getAll();
	pageContext.setAttribute("list",list);
%>

<jsp:useBean id="QaSvc" scope="page" class="com.qa.model.QaService"/>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath()%>/images/iEat_logo.png" />
<link href="<%= request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all"/>
<link href="<%= request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath()%>/css/meal.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath()%>/css/Q&A.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%= request.getContextPath()%>/js/sweetalert.js"></script>
<title>Insert title here</title>
</head>


<script>
$(function(){
	// 幫 #qaContent 的 ul 子元素加上 .accordionPart
	// 接著再找出 li 中的第一個 div 子元素加上 .qa_title
	// 並幫其加上 hover 及 click 事件
	// 同時把兄弟元素加上 .qa_content 並隱藏起來
	$('#qaContent ul').addClass('accordionPart').find('li div:nth-child(1)').addClass('qa_title').hover(function(){
		$(this).addClass('qa_title_on');
	}, function(){
		$(this).removeClass('qa_title_on');
	}).click(function(){
		// 當點到標題時，若答案是隱藏時則顯示它，同時隱藏其它已經展開的項目
		// 反之則隱藏
		var $qa_content = $(this).next('div.qa_content');
		if(!$qa_content.is(':visible')){
			$('#qaContent ul li div.qa_content:visible').slideUp();
		}
		$qa_content.slideToggle();
	}).siblings().addClass('qa_content').hide();
});

</script>
<body>
<jsp:include page="/front-end/head.jsp" />
<div class="main-1">
	<div class="container">
		<div class="col-md-7">
		<h2>Q&A</h2>
	<%@ include file="88888file/Afile01" %> 
	<c:forEach var="qaVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">

		<div id="qaContent">
		 <ul class="accordionPart">
				<li>
					<div class="qa_title">${qaVO.q_context}</div>
					<div class="qa_content" style="display: none;">
					${qaVO.a_context}
					</div>
				</li>
		 </ul>
		</div>
		
	</c:forEach>
	
	</div>

		<div class="col-md-5">
				<div class="panel panel-default">
				<div class="panel-heading" style="color:#d15a15;font-size:20px">聯絡客服</div>
				<div class="panel-body" style="padding:5%;margin:10px">
			<form class="form-horizontal" role="form" method="post" Action="<%=request.getContextPath()%>/Tousweb/tousweb.do">
				<div class="form-group">					
					<label for="name" class="control-label">title</label>
					<input type="text" class="form-control" id="subject" name="subject" placeholder="Enter your title" >
				</div>
				<div class="form-group">
					<label for="email" class="control-label">Email</label>
					<input type="email" class="form-control" id="yourmail" name="yourmail"
					 placeholder="example@domain.com" >
					
				</div>
				<div class="form-group">
					<label for="message" class="control-label">Message</label>
					<textarea class="form-control" rows="4" name="messageText"  id="messageText"></textarea>
				</div>
				<div class="form-group">
					<input id="submit" name="send" type="submit" value="送出" class="btn btn-success">
					<input type="hidden" name="action" value="Send_Mail">
					
				 <input type="reset" class="btn btn-success" value="重寫輸入">	
				 
				 <input type="hidden" id="to" name="to" value="aa101ieat@gmail.com" >
				</div>
			</form>
			</div>
			</div>
		</div>
	</div>
</div>	
<%@ include file="88888file/Afile02" %>



<!-- /register -->	
<!-- footer-->
<jsp:include page="/front-end/footer.html"/>
<!-- /footer-->
</footer>

</body>
</html>
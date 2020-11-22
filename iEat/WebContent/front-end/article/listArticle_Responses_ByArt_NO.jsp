<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.Set"%>
<%@ page import="com.article.model.*"%>
<%@ page import="com.article_response.model.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="com.article_response_report.model.*"%>
<jsp:useBean id="memberSvc" scope="page"
	class="com.member.model.MemberService" />
<jsp:useBean id="artrsSvc" scope="page"
	class="com.article_response.model.Article_ResponseService" />

<%
	Article_rs_reVO article_rs_reVO = (Article_rs_reVO) request.getAttribute("article_rs_reVO");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="<%=request.getContextPath()%>/css/sweetalert.css"
	rel="stylesheet" type="text/css" media="all" />
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script>
	$(document).ready(
			function() {
				$(".btn_open").click(
						function() {
							$("#form1").find("input[name='art_rs_no']").val(
									$(this).prev().val());
							console.log($(this).prev().val());
							$("#form1").show();
							$("#form2").hide();
							$("#press").show();
							$("#re_rs_press").show();
						})
				$(".deleBtn").click(function() {
					var obj = $(this);
					swal({
						title : "您確定要刪除嗎?",
						text : "您將要刪除此筆回覆!",
						type : "warning",
						showCancelButton : true,
						confirmButtonColor : "#DD6B55",
						confirmButtonText : "Yes, delete it!",
						cancelButtonText : "No, cancel plx!",
						closeOnConfirm : false,
						closeOnCancel : false
					}, function(isConfirm) {
						if (isConfirm) {
							swal({
								title : "刪除成功!",
								text : "您已經刪除回復.",
								timer : 2000,
								showConfirmButton : false,
								type : "success"
							});
							setTimeout(function() {
								obj.closest("form").submit(); //送出最近的form表單 刪除
							})
						} else {
							swal("取消", "您的回覆已保留 :)", "error");
						}
					});
				})
			});
</script>

</head>
<body bgcolor='white'>
	<%
		Article_ResponseVO article_responseVO = new Article_ResponseVO();
		ArticleVO articleVO = (ArticleVO) request.getAttribute("articleVO"); //EmpServlet.java(Concroller), 存入req的empVO物件
		ArticleService artSvc = new ArticleService();
		Set<Article_ResponseVO> responseSet = artSvc.getArticle_ResponsesByArt_no(articleVO.getArt_no());
		pageContext.setAttribute("responseSet", responseSet);
	%>
	<%-- 錯誤表列 --%>
	<c:if test="${not empty responseSet}"> <!-- 回覆內表內不是空的  就列出 -->
		<c:forEach var="article_responseVO" items="${responseSet}"> <!-- 列出所有食記回覆 -->
			<div>
				<c:forEach var="memberVO" items="${memberSvc.all}"> <!-- memberVO -->
					<c:if test="${article_responseVO.mem_no==memberVO.mem_no}">
						<font size="+1"><b> ${memberVO.mem_name} </b> 於 </font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<font color="darkgray" size="-2">
							${article_responseVO.art_rs_date} </font>
						<input type="hidden" name="art_rs_no"
							value="${article_responseVO.art_rs_no}">
						<button class="btn btn-default btn-md btn_open" style="float:right" data-toggle="modal" data-target="#myModal">
					<font size="3px">回覆檢舉</font>
				</button>
						<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Response/article_response.do">
							<input type="button" value="刪除" class="btn btn-default deleBtn" id="delete_btn"
								${(user.no==memberVO.mem_no) ?'style=background:#337ab7;margin-left:110%;height:38px;width:100px;font-size:16px;font-color:orange':'disabled style=display:none;'}>
							<input type="hidden" name="art_rs_no" value="${article_responseVO.art_rs_no}">
							<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"> <!--送出本網頁的路徑給Controller-->
							<input type="hidden" name="action" value="delete"> 
							<input type="hidden" name="art_no" value=<%=articleVO.getArt_no()%>>
							<HR color="#00FF00" size="10">
						</FORM>
					</c:if>
				</c:forEach> <!-- memberVO -->
			</div>

			<div> ${article_responseVO.art_rs_context}</div>		
			<hr style="border: 1px dashed darkgray; margin:20px;" size="1" width="90%" align="center">
			<br><br>
		</c:forEach> <!-- 列出所有食記回覆 -->
		<br><br><br><br>
	</c:if> <!-- 回覆內表內不是空的  就列出 -->

	<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/article_response_report/article_response_report.do" id="form1" name="form1">
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">回覆檢舉</h4>
					</div>
					<div class="modal-body" id="re_rs_press" style="display: none">
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
						回覆檢舉內容
						<textarea name="art_rs_re_con" cols="30" rows="5"></textarea>
						<input type="hidden" name="action" value="insert">
					</div>
					<div class="modal-footer" style="display: none" id="press">
						<button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
						<input type="submit" value="檢舉"> <input type="hidden"
							name="action" value="insert"> <input type="hidden"
							name="art_no" value="<%=articleVO.getArt_no()%>"> <input
							type="hidden" name="mem_no" value="${user.no}"> <input
							type="hidden" name="art_rs_no"
							value="${article_responseVO.art_rs_no}"> <input
							type="hidden" name="art_rs_re_sta" value=0
							class="btn btn-primary">

					</div>

				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->


	</FORM>


</body>
</html>
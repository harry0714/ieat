<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.article.model.*"%>
<%@ page import="com.article_response.model.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="com.article_report.model.*"%>
<%@ page import="com.article_response_report.model.*"%>
<jsp:useBean id="artrsSvc" scope="page" class="com.article_response.model.Article_ResponseService" />
<jsp:useBean id="artSvc" scope="page" class="com.article.model.ArticleService" />
<jsp:useBean id="memberSvc" scope="page" class="com.member.model.MemberService" />
<%
	ArticleVO articleVO = (ArticleVO) request.getAttribute("articleVO"); //EmpServlet.java(Concroller), 存入req的empVO物件

	Article_reportVO article_reportVO = (Article_reportVO) request.getAttribute("article_report");

	Article_rs_reVO article_rs_reVO = (Article_rs_reVO) request.getAttribute("article_rs_reVO");

	Set<Article_ResponseVO> responseSet = artSvc.getArticle_ResponsesByArt_no(articleVO.getArt_no());
	pageContext.setAttribute("responseSet", responseSet);

%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/iEat_logo.png" />
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%=request.getContextPath()%>/css/sweetalert.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script>
	$(document).ready(function() {
		$(".btn_open2").click(function() {
			$("#form1").hide();
			$("#form2").show();
		})

		<c:if test="${not empty reportSuccess}">
		swal("檢舉成功", "檢舉成功\n請等候後端審核通知", "success");
		</c:if>
		
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
				confirmButtonText : "確定",
				cancelButtonText : "取消",
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
	<!-- header -->
	<jsp:include page="/front-end/head.jsp" />
	<!-- header -->
	<div class="main-1">
		<div class="container">
			<div class="col-md-3">
				<div class="article_border">
					<div style="margin:10px">
					<img class="img-responsive"  src="<%=request.getContextPath()%>/member/memberImageReader.do?mem_no=${articleVO.mem_no}">
					</div>
					<h4><b>${memberSvc.getOneMember(articleVO.mem_no).mem_name}</b></h4>
					<p>${articleVO.art_date} </p>
				</div>
			</div>
			<div class="col-md-9">
				<div class="article_border">
				<h2> <%=articleVO.getArt_name()%></h2>
					<div class="well">
					<font size="+1"> <%=articleVO.getArt_context()%>
					</font>
					</div>

						<button class="btn btn-default btn-md btn_open2" data-toggle="modal" data-target="#myModal2">食記檢舉</button>
				
				</div>

				<!-- 顯示所有回覆 -->
				<div class="article_border">
				<div class="well">

	
		<c:if test="${not empty responseSet}"> <!-- 回覆內表內不是空的  就列出 -->
			<c:forEach var="article_responseVO" items="${responseSet}"> <!-- 列出所有食記回覆 -->
			<div>
				<font size="+1"><b> ${memberSvc.getOneMember(article_responseVO.mem_no).mem_name} </b> 於 </font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<font color="darkgray" size="-2">
							${article_responseVO.art_rs_date} </font>
				
				<input type="hidden" name="art_rs_no" value="${article_responseVO.art_rs_no}">
				<button class="btn btn-default btn-md btn_open" style="float:right" data-toggle="modal" data-target="#myModal">
					回覆檢舉
				</button>
				<c:if test="${user.no==article_responseVO.mem_no}">
						<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Response/article_response.do" style="float:right;margin-right:10px">	
							<input type="button" value="刪除" class="btn btn-danger deleBtn" id="delete_btn">
							<input type="hidden" name="art_rs_no" value="${article_responseVO.art_rs_no}">
							<input type="hidden" name="requestURL" value="<%=request.getServletPath()%>"> <!--送出本網頁的路徑給Controller-->
							<input type="hidden" name="action" value="delete"> 
							<input type="hidden" name="art_no" value=<%=articleVO.getArt_no()%>>
						</FORM>
				</c:if>
				<div class="clearfix"></div>
			</div>

			<div> ${article_responseVO.art_rs_context}</div>		
			<hr style="border: 1px dashed darkgray;" size="1" width="90%" align="center">
		</c:forEach> <!-- 列出所有食記回覆 -->

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
						<textarea name="art_rs_re_con" cols="50" rows="5"></textarea>
						<input type="hidden" name="action" value="insert">
					</div>
					<div class="modal-footer" style="display: none" id="press">
						<button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
						<input type="submit" value="檢舉">
						<input type="hidden" name="action" value="insert">
						<input type="hidden" name="art_no" value="<%=articleVO.getArt_no()%>">
						<input type="hidden" name="mem_no" value="${user.no}">
						<input type="hidden" name="art_rs_no" value="${article_responseVO.art_rs_no}">
						<input type="hidden" name="art_rs_re_sta" value=0 class="btn btn-primary">
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->


	</FORM>
					<!-- 新增回覆 -->
					
						<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Response/article_response.do" name="form1" >
						<textarea  name="art_rs_context"  style="width:100%" rows=5 ></textarea>						
						<input type="hidden" name="action" value="insert">
						<input type="hidden" name="art_no" value="<%=articleVO.getArt_no()%>">
						<input type="hidden" name="mem_no" value="${user.no}">						
						<input type="submit" value="送出新增" > 
					   </FORM> 
					</div>
					</div>
					<div>
						<FORM METHOD="post"
							ACTION="<%=request.getContextPath()%>/article_report/article_report.do"
							id="form2" name="form2">
							<div class="modal fade" id="myModal2" tabindex="-1" role="dialog"
								aria-labelledby="myModalLabel" aria-hidden="true">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-hidden="true">&times;</button>
											<h4 class="modal-title" id="myModalLabel">食記檢舉</h4>
										</div>
										<div class="modal-body">
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
											<textarea name="art_re_context" cols="50" rows="5"></textarea>
											<input type="hidden" name="action" value="insert">
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default"
												data-dismiss="modal">關閉</button>
											<input type="submit" value="檢舉食記"> <input
												type="hidden" name="action" value="insert"> <input
												type="hidden" name="mem_no" value="${user.no}"> <input
												type="hidden" name="art_no"
												value="<%=articleVO.getArt_no()%>"> <input
												type="hidden" name="art_re_status" value=0
												class="btn btn-primary">
										</div>

									</div>
									<!-- /.modal-content -->
								</div>
								<!-- /.modal -->
							</div>
						</FORM>
						<hr color="#00FF00" size="10">
				</div>
			</div>
		</div>
		<!-- container -->
	</div>
	<jsp:include page="/front-end/footer.html" />
</body>
</html>
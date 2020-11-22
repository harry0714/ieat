<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>

<script src="<%=request.getContextPath()%>/js/socket.js"></script>
<script>
		$(document).ready(function() {
			$("li").click(function() {
				$(this).children("div").toggle("slow");
			});
		});
</script>
<style>
.submenu{
	display:none;
	list-style-type:none;
}
.nav-sidebar>li:hover .submenu{
	display:block;
}
</style>
<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar">
				<ul class="nav nav-sidebar">
					<li class=""><a href="#">檢舉管理<span class="badge" id="totalUntreated"></span></a>
					<div class="list-group" hidden>
						<a href="<%=request.getContextPath()%>/reservation_report/reservation_report.do?action=enterreservationreport&status_selected=0" class="list-group-item">訂位檢舉<span class="badge" id="rrUntreated"></span></a> 
						<a href="<%=request.getContextPath()%>/article_report/article_report.do2?action=backEndenterarticlereport&status_selected=0" class="list-group-item">食記檢舉<span class="badge" id="arUntreated"></span></a> 
						<a href="<%=request.getContextPath()%>/store_report/store_report.do?action=enterstorereport&status_selected=0" class="list-group-item">店家檢舉<span class="badge" id="srUntreated"></span></a> 
						<a href="<%=request.getContextPath()%>/comment_report/comment_report.do?action=entercommentreport&status_selected=0" class="list-group-item">餐廳評論檢舉<span class="badge" id="crUntreated"></span></a> 
						<a href="<%=request.getContextPath()%>/ord_report/ord_report.do?action=enterorderreport&status_selected=0" class="list-group-item">訂餐檢舉<span class="badge" id="orUntreated"></span></a> 
						<a href="<%=request.getContextPath()%>/article_response_report/article_response_report.do2?action=enterarticleresponsereport&status_selected=0" class="list-group-item">食記回覆檢舉<span class="badge" id="arrUntreated"></span></a> 
					</div>
					</li>
					
					<li><a href="<%=request.getContextPath()%>/back-end/advertisement/advertisement.do?ad_filter=on&action=filter_ads">廣告管理</a></li>
					<li><a href="<%=request.getContextPath()%>/back-end/qa/listAllQa.jsp">Q&A管理</a></li>
					<li class=""><a href="#">店家管理<span class="badge" id="storetotalUntreated"></span></a>
					<div class="list-group" hidden>
						<a href="<%=request.getContextPath()%>/back-end/store/notreviewed.jsp" class="list-group-item">店家審核<span class="badge" id="storeUntreated"></span></a>
						<a href="<%=request.getContextPath()%>/back-end/store/reviewed.jsp" class="list-group-item">店家檢視</a>
					</div></li>
					<li><a href="<%=request.getContextPath()%>/back-end/member/member.jsp">會員管理</a></li>
					<li><a href="<%=request.getContextPath()%>/back-end/adm/adm.jsp">管理員管理</a></li>
				</ul>
			</div>
			</div>
			</div>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.store_type.model.*"%>
<%@ page import="com.store.model.*" %>
<%
	Store_typeService stSvc = new Store_typeService();
	List<Store_typeVO> stList =  stSvc.getAll();
	request.setAttribute("stList", stList);
%>

<script src="<%=request.getContextPath()%>/js/addressforindex.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/alertify.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/alertify.rtl.min.css">
<script src="<%=request.getContextPath()%>/js/alertify.js"></script>

<script >
var MyPoint = "/ReportScoket";
var host = window.location.host;
var path = window.location.pathname;
var webCtx = path.substring(0, path.indexOf('/', 1));
var endPointURL = "ws://" + window.location.host + webCtx + MyPoint;
var websocket = new WebSocket(endPointURL);
$(document).ready(function() {

	$("#store_report").click(function(){
		swal({   
			title: "餐廳檢舉",   
			text: "檢舉內容:",   
			type: "input",   
			showCancelButton: true,   
			closeOnConfirm: false,
			confirmButtonText: "確認",   
			cancelButtonText: "取消",
			inputPlaceholder: "請輸入小於100字的內容" 
			}, 
			function(inputValue){   
				if (inputValue === false) return false;      
				if (inputValue === "") {     
					swal.showInputError("請填寫內容");     
					return false   
				}
				$.ajax({
					type:"POST",
					dataType:"text",
					url:"<%=request.getContextPath()%>/store_report/store_report.do",
					data:{action:"addAjax",store_no:"${param.store_no}",store_report_content:inputValue},
					success : function(data) {
						if("add" == data){
							swal("", "你的檢舉已收到 ，會盡速處理", "success");
							websocket.send(JSON.stringify({
								"message" : "前端送出"
							}));
							return;
						}
						alert(data);
					},
					error : function() {
						alert("error");
					}
				});
				 
			}
		);
	});	
	
	$("#bookmark").click(function(){
		
		var action;
		if($(this).text()=="加入最愛"){
			action="addAjax";
		}
		if($(this).text()=="移除最愛"){
			action="deleteAjax";
		}
		$.ajax({
			type:"post",
			url: "<%=request.getContextPath()%>/bookmarkstore/bookmarkstore.do",
			data : {action :action,store_no:"${param.store_no}"},
			dataType:"text",
			success : function(data){
				if(data == "add"){
					
					$("#bookmark").text("移除最愛");
					alertify.success('成功將此餐廳加入最愛');
					
					return;
				}
				if(data == "delete"){
					$("#bookmark").text("加入最愛");
					alertify.error('已從最愛中移除');
					
					return;
				}
				if(data == "error"){
					alert(data);
				}
			},
			error:function(){
				alert("error");
			}
		});
	});
});
</script>
<style>
  /*讓超連結disable掉 */
        a.disabled {
  			pointer-events: none;
   			cursor: default;
   			style:color="grey"; 
		}
</style>

<div class="col-md-2">
			<div class="sub_nav">
				<ul>
					<% 
								String url = request.getServletPath(); 
								request.setAttribute("url",url);
					%>
					<li ${(url eq "/front-end/store/display_oneStore.jsp")?"class='active'":''}><a href="<%= request.getContextPath() %>/store/store.do?action=getOne_For_Display&store_no=${param.store_no}">店家資訊</a></li>
					<li ${(url eq "/checkout/Orders.jsp")?"class='active'":''}><a href="<%= request.getContextPath()%>/checkout/Orders.jsp?store_no=${param.store_no}">線上餐點</a></li>
					<li ${param.action.equals("get_reservation_info")?"class='active'":''}>
						<c:if test="${empty store }"> <!-- 若店家不登入 則顯示ok -->
							<a href="<%=request.getContextPath()%>/store/store.do?action=get_reservation_info&store_no=${param.store_no}">線上訂位</a>
						</c:if>
						<!-- 若為店家登入狀態  則無法訂位 取消訂位超連結 -->
						<c:if test="${not empty store }">
							<a href="<%=request.getContextPath()%>/store/store.do?action=get_reservation_info&store_no=${param.store_no}" class="disabled" style="color:grey;">線上訂位</a>
						</c:if>
					</li>
					<c:if test="${! empty user }">
						<c:if test="${! bookmark_StoreNo.contains(param.store_no)}">
							<li><a href="#" id="bookmark">加入最愛</a></li>
						</c:if>
						<c:if test="${bookmark_StoreNo.contains(param.store_no)}">
							<li><a href="#" id="bookmark">移除最愛</a></li>
						</c:if>
					
						<li><a href="#" id="store_report">餐廳檢舉</a></li>
					</c:if>
				</ul>
			</div>
			<div class="sub_nav">
				<div id="searchBar">
					<form method="post" action="<%= request.getContextPath() %>/store/store.do">
						<div class="input-group">		
							<input type="hidden" name="action" value="listStores_ByCompositeQuery">				
							<input type="text" class="form-control" placeholder="Search" name="store_name" value="${param.store_name }">
							<span class="input-group-btn">
								<button class="btn btn-success" type="submit">
									<i class="glyphicon glyphicon-search"></i>
								</button>
							</span>							
						</div>
						<br>
						<div class="form-inline">
							<select class="form-control" id="zone1" >
							  
							</select>
							
							<select class="form-control" id="zone2" >
							 
							</select>
							<input type="hidden" id="store_ads" name="store_ads" value="${param.store_ads }">
							<input type="hidden" name="store_status" value="1">
							<select class="form-control" name="store_type_no">
							  <option value="">餐廳種類</option>
							  
							  <c:forEach var="stVO" items="${stList}">
							  	<option value="${stVO.store_type_no }" ${(stVO.store_type_no==param.store_type_no)?"selected":"" }>${stVO.store_type_name}</option>
							  </c:forEach>
							</select>
						</div>
					</form>
				</div>
			</div>
		</div>

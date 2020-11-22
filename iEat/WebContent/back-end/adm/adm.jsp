<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.adm.model.*"%>
<%
    AdmService admSvc = new AdmService();
    List<AdmVO> list = admSvc.getAll();
    pageContext.setAttribute("list",list);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="<%=request.getContextPath()%>/back-end/index.jsp">
<title>iEat - 後端管理</title>

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
								<script>
								$(document).ready(function(){
									$(".glyphicon-trash").click(function(){
										$(this).closest("form").submit();
									})
									$(".glyphicon-pencil").click(function(){
										$.ajax({
								            type: "POST",
								            url: "<%=request.getContextPath()%>/adm/adm.do",
								            data: {action:"getOne_For_Update",adm_no:createQueryString(this)},
								            dataType: "json",
								            
								           	success: function(json){
								                   drawForm(json);
								               },

								            error:function (xhr, ajaxOptions, thrownError) {
								                console.log(xhr);
								                alert(xhr.status);
								                alert(thrownError)
								               }
								        })     
									
									})
									function createQueryString(e){
										console.log($(e).parent().next().val());
										return $(e).parent().next().val();
								    }
									function drawForm(json){
										$("#adm_no").val(json.adm_no);
										$("#adm_name").val(json.adm_name);
										$("#adm_phone").val(json.adm_phone);
										$("#adm_bd").val(json.adm_bd);
										$("#adm_addr").val(json.adm_addr);
										$("#adm_user").val(json.adm_user);
										$("#xxx").val(json.adm_photo);
										$("#adm_email").val(json.adm_email);
										$("#adm_psd").val(json.adm_psd);
										$('input[name="adm_sex"][value="'+json.adm_sex+'"]').attr('checked', 'checked');
										$('input[name="adm_level"][value="'+json.adm_level+'"]').attr('checked', 'checked');
										$("#xxx").attr("src",'<%=request.getContextPath()%>/PhotoReader?adm_no='+json.adm_no);
									}
									<c:if test="${not empty errorMsgs}">
								
										$("#myModal").modal('show');
									</c:if>
								})
							

								</script>
</head>
<body>																
<%@ include file="/back-end/page/head.jsp"%>
<%@ include file="/back-end/page/body.jsp"%>							
<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h1 class="page-header">管理員帳號管理</h1>
	
				<c:if test="${admuser.adm_level=='1'}">
				<a style="float:right" id="addBtn" class="glyphicon glyphicon-plus" data-toggle="modal" data-target="#myModal">新增管理員</a>
<%-- 				<a style="float:right" class="glyphicon glyphicon-plus" href="<%=request.getContextPath()%>/back-end/adm/addAdm.jsp" role="button">新增管理員</a> --%>
				</c:if>
		<table class="table table-striped">
			<thead>
				<tr>
					<th>員工編號</th>
					<th>員工姓名</th>
					<th>帳號</th>
					<th>性別</th>
					<th>生日</th>
					<th>信箱</th>
					<th>電話</th>
					<th>權限</th>
					<th>地址</th>
					<th>照片</th>
					<th>修改</th>
					<th>刪除</th>
				</tr>

			</thead>
			<tbody>

				<%@ include file="/back-end/adm/pages/page1.file"%>
				<c:forEach var="admVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
					<tr align='center' valign='middle' ${(admVO.adm_no==param.adm_no) ? 'bgcolor=#CCCCFF':''}>
						<td>${admVO.adm_no}</td>
						<td>${admVO.adm_name}</td>
						<td>${admVO.adm_user}</td>
						<td>${admVO.adm_sex %2== 1 ? '男': '女'}</td>
						<td>${admVO.adm_bd}</td>
						<td>${admVO.adm_email}</td>
						<td>${admVO.adm_phone}</td>
						<td>${admVO.adm_level %2== 1 ? "最高權限": "一般權限"}</td>
						<td>${admVO.adm_addr}</td>
						<td><img src="<%=request.getContextPath()%>/PhotoReader?adm_no=${admVO.adm_no}" width=100 height=100></td>
						<td>
						<c:if test="${admuser.adm_level==1||admVO.adm_user==admuser.adm_user}">
							<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/adm/adm.do">
								<a href="#" data-toggle="modal" data-target="#myModal1"><i class="glyphicon glyphicon-pencil"></i></a>
<!-- 								<input type="submit" value="修改" class="btn btn-info btn-xm"> -->
								<input type="hidden" name="adm_no" value="${admVO.adm_no}">
								<input type="hidden" name="requestURL"	value="<%=request.getServletPath()%>">
								 <input type="hidden" name="whichPage"	value="<%=whichPage%>"> 
								<input type="hidden" name="action" value="getOne_For_Update">
								
							</FORM>
						</c:if>
						</td>
						
						<c:if test="${admuser.adm_level=='1'}">
						<td>
							<FORM METHOD="post"
								ACTION="<%=request.getContextPath()%>/adm/adm.do">
								<a href="#"><i class="glyphicon glyphicon-trash"></i></a>
<!-- 								<input type="submit" value="刪除" class="btn btn-danger btn-xm"> -->
								 <input type="hidden" name="adm_no" value="${admVO.adm_no}">
								 <input type="hidden" name="requestURL"	value="<%=request.getServletPath()%>">
								  <input type="hidden" name="whichPage"	value="<%=whichPage%>"> 
								 <input type="hidden" name="action" value="delete">
							</FORM>
						</td>
						</c:if>
					</tr>
					
				</c:forEach>
			<%@ include file="/back-end/adm/pages/page2.file"%>
			</tbody>

		</table>
</div>
<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
       
        <h4 class="modal-title">員工資料新增</h4>
      </div>
      <div class="modal-body">
<jsp:include page="addAdm.jsp"/>
      </div>
      <div class="modal-footer">
        <a href="<%=request.getContextPath()%>/back-end/adm/adm.jsp"><button type="button" class="btn btn-default">Close</button></a>
      </div>
    </div>

  </div>
</div>

<div id="myModal1" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title">管理員資料修改</h4>
      </div>
      <div class="modal-body">
		 <jsp:include page="update_adm_input.jsp"/>
      </div>
      <div class="modal-footer">
        <a href="<%=request.getContextPath()%>/back-end/adm/adm.jsp"><button type="button" class="btn btn-default">Close</button></a>
      </div>
    </div>

  </div>
</div>
</body>
<script>
function readFile(){ 
    var file = this.files[0]; 
    if(!/image\/\w+/.test(file.type)){ 
        alert("Error!!!"); 
        return false; 
    } 
   
    var reader = new FileReader(); 
    reader.readAsDataURL(file); 
    reader.onload = function(e){ 
    	photoResult.innerHTML ='<img src="'+this.result+'" alt="" width=100 height=100 />';
    	$("#xxx").attr("src",this.result);
    } 
}

function doFirst(){
	var photoResult = document.getElementById("photoResult"); 
	var input = document.getElementById("fileinput");
	var input2 = document.getElementById("fileinput2"); 
	
	if(typeof FileReader==='undefined'){ 
	    result.innerHTML = "no suppor FileReader"; 
	    input.setAttribute('disabled','disabled'); 
	    input2.setAttribute('disabled','disabled');
	}else{ 
	    input.addEventListener('change',readFile,false); 
	    input2.addEventListener('change',readFile,false); 
	} 
}

window.addEventListener('load',doFirst,false);

</script>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.store_photo.model.*"%>
<%@ page import="com.store.model.*"%>
<html>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet" type="text/css" media="all" />
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<link href="<%=request.getContextPath()%>/css/sweetalert.css"
	rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/memberprefecture.css"
	rel="stylesheet" type="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<%
	Store_photoService store_photoSvc = new Store_photoService();
	Map<String, String> store = (Map<String, String>) session.getAttribute("store");
	String store_no = store.get("store_no");
	List<Store_photoVO> store_photoVO = store_photoSvc.getOneStorePhoto(store_no);
	pageContext.setAttribute("store_photoVO", store_photoVO);
%>

</head>
<body>
	<jsp:include page="/front-end/head.jsp" />
	<div class="main-1">
		<div class="container">
			<!-- head -->
			<div class="col-md-3">
				<jsp:include page="/front-end/store/storeMenu.jsp"></jsp:include>
			</div>
			<!-- head -->
			<div class="col-md-9">
			<div class="panel panel-default">
				<div class="panel-heading" style="color:#d15a15;font-size:20px">店家照片管理<a style="float: right" class="glyphicon glyphicon-plus"
					data-toggle="modal" data-target="#myModal">新增店家照片</a></div>		
				<table class="table table-bordered">
					<tr>
						<th>店家照片</th>
						<th>店家照片名稱</th>
						<th>店家照片描述</th>
						<!-- 						<th>修改</th> -->
						<th>刪除</th>
					</tr>
					<c:forEach var="Store_photoVO" items="${store_photoVO}">
						<tr>
							<td><img
								src="<%=request.getContextPath()%>/photo_read/photo_read.do?photo_no=${Store_photoVO.photo_no}"
								class="img-rounded" width=150 height=150></td>
							<td>${Store_photoVO.photo_name}</td>
							<td>${Store_photoVO.photo_des}</td>
							<!-- 修改怪怪的  所以拿掉 -->
							<!-- 						<td> -->
							<%-- 			 				<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/store_photo/store_photo.do"> --%>
							<!-- 			   	 				<a href="#" data-toggle="modal" data-target="#myModal1"> -->
							<!-- 			   	 					<i class="glyphicon glyphicon-pencil"></i> -->
							<!-- 			   	 				</a>修改 -->
							<%-- 			     				<input type="hidden" name="photo_no" value="${Store_photoVO.photo_no}"> --%>
							<!-- 			     				<input type="hidden" name="action"	value="getOne_For_Update"> -->
							<!-- 			   				</FORM> -->
							<!-- 						</td> -->
							<td>
								<FORM METHOD="post"
									ACTION="<%=request.getContextPath()%>/store_photo/store_photo.do">
									<a href="#"><i class="glyphicon glyphicon-trash"></i></a>
									<!-- 刪除 -->
									<input type="hidden" name="photo_no"
										value="${Store_photoVO.photo_no}"> <input
										type="hidden" name="action" value="delete">
								</FORM>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<!-- Container -->
		<c:if test="${! empty errorMessage1 }">
			<div class="container">
				<font color='red'>請修正以下錯誤:
					<p>${errorMessage1.elseError}</p>
				</font>
			</div>
		</c:if>
	</div>

	<div id="myModal" class="modal fade" role="dialog">
		<div class="modal-dialog">
			Modal content
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">新增照片</h4>
				</div>
				<!-- 新增照片 跳出的視窗 -->
				<div class="modal-body">
					<jsp:include page="/front-end/store/addstorephoto.jsp" />
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>

		</div>
	</div>


	<div id="myModal1" class="modal fade" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<!-- 修改照片資訊 -->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				<div class="modal-body">
					<jsp:include page="/front-end/store/update_store_photo_input.jsp"></jsp:include>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>

	<script>
$(".glyphicon-pencil").click(function(){
	$.ajax({
        type: "POST",
        url: "<%=request.getContextPath()%>/store_photo/store_photo.do",
        data: {action:"getOne_For_Update",photo_no:createQueryString(this)},
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
    function createQueryString(e){
		console.log($(e).parent().next().val());
		return $(e).parent().next().val();
    }
	function drawForm(json){
		$("#photo_no").val(json.photo_no);
		$("#photo_name").val(json.photo_name);
		$("#photo_des").val(json.photo_des);
		$("#store_no").val(json.store_no);
		$("#storephoto").attr("src",'<%=request.getContextPath()%>/photo_read/photo_read.do?photo_no='+ json.photo_no);
				}
	});

		$(".glyphicon-trash").click(function() {
			$(this).closest("form").submit();
		});

		function readFile() {
			var file = this.files[0];
			if (!/image\/\w+/.test(file.type)) {
				alert("Error!!!");
				return false;
			}

			var reader = new FileReader();
			reader.readAsDataURL(file);
			reader.onload = function(e) {
				storeresult.innerHTML = '<img src="'+this.result+'" alt="" width=100 height=100 />';
				storephoto.innerHTML = '<img src="'+this.result+'" alt="" width=100 height=100 />';
				$("#storephoto").attr("src", this.result);
			}
		}

		function doFirst() {
			var storeresult = document.getElementById("storeresult");
			var storephoto = document.getElementById("storephoto");
			var input = document.getElementById("fileinput1");
			var input2 = document.getElementById("fileinput2");

			if (typeof FileReader === 'undefined') {
				result.innerHTML = "no suppor FileReader";
				input.setAttribute('disabled', 'disabled');
				input2.setAttribute('disabled', 'disabled');
			} else {
				input.addEventListener('change', readFile, false);
				input2.addEventListener('change', readFile, false);
			}
		}

		window.addEventListener('load', doFirst, false);
	</script>
	<jsp:include page="/front-end/footer.html" />
</body>
</html>
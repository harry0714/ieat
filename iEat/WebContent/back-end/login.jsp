<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">



<script src="<%=request.getContextPath()%>/js/jquery-2.2.3.min.js"></script>
<!-- Bootstrap Core CSS -->
<link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/metisMenu.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/sb-admin-2.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<script>

$(document).ready(function(){
	
	$("#magic").click(function(){
		$("#user1").val("brendan9012");
		$("#pass").val("111111");
	});
});
</script>
<body>

	<div class="container">
		<div class="row">
			<div class="col-md-8 col-md-offset-2">
				<div class="login-panel panel panel-default">
					<div class="panel-heading">
						<img  src="<%=request.getContextPath()%>/images/321.png">
					</div>
					<div class="panel-body">
						<form method="post" action="<%=request.getContextPath()%>/adm/adm.do">
							<fieldset>
								<div class="form-group">
									<input class="form-control" placeholder="user" name="adm_user" type="text" autofocus id="user1" value="">
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="Password" name="adm_psd" type="password" id="pass" value="">
								</div>

								<!-- Change this to a button or input when using this as a form -->
								<input type="hidden" name="action" value="login">
								 <input type="submit" class="btn btn-lg btn-danger btn-block" value="登入" >
							</fieldset>
						</form>
					</div>
						 <input type="button" class="btn btn-info btn-xm" value="magic" id="magic">
				</div>
			</div>
		</div>
	</div>
	
	<!-- jQuery -->
	<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="<%=request.getContextPath()%>/js/bootstrap.js"></script>

	<!-- Metis Menu Plugin JavaScript -->
	<script src="<%=request.getContextPath()%>/js/metisMenu.min.js"></script>

	<!-- Custom Theme JavaScript -->
	<script src="<%=request.getContextPath()%>/js/sb-admin-2.js"></script>

</body>

</html>

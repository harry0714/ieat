<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/tinymce/tinymce.min.js"></script>
<script>tinymce.init({ selector: $('#editor')});</script>
<script>
$(function() {
	$('#btn_submit').click(
		function(){
			$('#editor').tinymce().save();
			$('#editor_form').submit();
		}		
	)
	console.log($("#editor").prop('nodeName'));	
})

</script>
</head>
<body>
<div class="container">
		<form id="editor_form" action="<%=request.getContextPath()%>/article/article.do" method="post">
        <textarea id="editor" name="editor"></textarea>
        <input type="hidden" name="action" value="submit">
        <button id="btn_submit">送出文章</button>
        </form>
</div>
</body>
</html>
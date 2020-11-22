<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Discount</title>
</head>
<body>
<h3>優惠查詢:</h3>
<ul>
	<li><a href="listAllDiscount.jsp">所有優惠</a> all Discounts.</li><br>
	<li>個別店家優惠 :store Discounts
		<form method="post" action="discount.do">
			<label for="store_no">
				<input type="text" id="store_no" name="store_no" value="S000000001">
				<input type="hidden" name="action" value="get_store">
			</label>
			<input type="submit" value="送出">
		</form>
		</li>
</ul>
<h3>新增S000000001店家優惠:</h3>
<ul>
	<li><a href="addDiscount.jsp?store_no=S000000001">Add</a> a new Discount.</li>
</ul>
</body>
</html>
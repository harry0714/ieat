
<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.article.model.*"%>
<%@ page import="com.article_response.model.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="com.article_report.model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="shortcut icon" type="image/x-icon"
	href="<%=request.getContextPath()%>/images/iEat_logo.png" />
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


</head>
<body bgcolor='white'>

	  <div class="container">
	  
	  <div style="border:1px solid darkgrey;border-radius:10px;padding:20px;margin:10px;background:white">
			<p >

				<%
									String art_date = (String)request.getAttribute("art_date");
									String art_date1  = art_date.replace("T"," ").replace("-", "/");
									pageContext.setAttribute("art_date1", art_date1);
				%>
				<font color="darkgray"> ${art_date1} </font>
			</p>
			<p  style="font-size: 20;">
				<font size="+2"> ${art_name} 
			</font>
			</p>
			
		
				<font size="+1"> ${art_context}
				</font>
				
				<br><br><br><br><br><br>
				</div>
		</div>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.article_report.model.*"%>
<%@ page import="com.article.model.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%
Article_reportVO article_reportVO = (Article_reportVO) request.getAttribute("article_reportVO");
%>
<%
	ArticleVO articleVO = (ArticleVO) request.getAttribute("articleVO"); //EmpServlet.java (Concroller), 存入req的empVO物件 (包括幫忙取出的empVO, 也包括輸入資料錯誤時的empVO物件)
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath()%>/images/iEat_logo.png" />
<link href="<%= request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all"/>
<link href="<%= request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath()%>/css/meal.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%-- <script src="<%=request.getContextPath()%>/js/tinymce/tinymce.min.js"></script> --%>
<script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
<script src="http://code.jquery.com/jquery-1.12.3.min.js"></script>
<script src="<%= request.getContextPath()%>/js/bootstrap.min.js"></script>
</head>
<body bgcolor='white'>


<!-- 按钮触发模态框 -->



<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/Article_Report/article_report.do2" name="form1">
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" 
               data-dismiss="modal" aria-hidden="true">
                  &times;
            </button>
            <h4 class="modal-title" id="myModalLabel">
            	食記檢舉
            			</h4>
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
			
				<table border="0">
					<tr>
						<td>食記檢舉內容</td>
						<td>
						<input type="text" name="art_re_context" size="45"
						value="<%= (article_reportVO==null)? "" : article_reportVO.getArt_re_context()%>" />
						</td>
					</tr>
						<%
						SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						Date current = new Date();
						String sampleDate = sdFormat.format(current);
						%>
				</table>

		</div>      
        
         
         
     <div class="modal-footer">
            <button type="button" class="btn btn-default" 
               data-dismiss="modal">關閉
       </button>          

		<input type="submit" value="檢舉食記" >
      	<input type="hidden" name="action" value="insert">
		<input type="hidden" name="art_re_date" value="<%= java.sql.Timestamp.valueOf(sampleDate)%>">
		<input type="hidden" name="mem_no" value="${user.no}">
		<input type="hidden" name="art_no" value="<%=articleVO.getArt_no()%>">
		<input type="hidden" name="art_re_status" value=0 class="btn btn-primary">
		
 	</div>
 
 		   </div><!-- /.modal-content -->
	</div><!-- /.modal -->
 </div>

</FORM> 
</body>
</html>
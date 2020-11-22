<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!-- header -->
<div class="head-top">
	<div class="container">
		<div class="header-right1">
			<div class="cart box_1">
				<!-- 非登入狀態 -->
				<c:if test="${empty user and empty store}">	
				<a href="<%= request.getContextPath()%>/checkout/Cart.jsp"><img src="<%= request.getContextPath() %>/images/cart.png" alt=""> 結帳</a>
				<a href="<%= request.getContextPath() %>/front-end/login.jsp">登入</a> 
				<a href="<%= request.getContextPath() %>/front-end/member/memberregister.jsp">加入會員</a>
				<a href="<%=request.getContextPath() %>/front-end/store/storeRegister.jsp">申請成為店家</a>
				</c:if>
				<!-- 會員登入 -->
				<c:if test="${not empty user }">
				<a href="<%= request.getContextPath()%>/checkout/Cart.jsp"><img src="<%= request.getContextPath() %>/images/cart.png" alt=""> 結帳</a>
				<a style="text-decoration: none">${user.name} 你好</a>
				<a href="<%= request.getContextPath() %>/front-end/member/personal.jsp">會員專區</a>
				<a href="<%= request.getContextPath() %>/member/member.do?action=memberlogout">登出</a>	
				</c:if>
				<!--  店家登入 -->				
				<c:if test="${not empty store }">
				<a style="text-decoration: none">${store.store_name} </a>
				<a href="<%= request.getContextPath() %>/front-end/store/personal.jsp">店家專區</a>
				<a href="<%= request.getContextPath() %>/store/store.do?action=storeLogout">登出</a>	
				</c:if>
				<div class="clearfix"> </div>
			</div>
		</div>
	
	</div>
</div>
	<div class="header">
		
		<div class="container">
			<div class="logo">
				<a href="<%= request.getContextPath() %>/front-end/index.jsp"><img src="<%= request.getContextPath() %>/images/logo.png" class="img-responsive" alt=""></a>
			</div>
			<div class="header-left">
				<div class="head-nav">
					<span class="menu"> </span>
						<ul>
							<!-- 判斷現在在哪個網頁 -->
							<% 
								String url = request.getServletPath(); 
								request.setAttribute("url",url);
							%>
							
							<li ${(url eq "/front-end/index.jsp")?"class='active'":''}><a href="<%= request.getContextPath() %>/front-end/index.jsp">Home</a></li> <!-- 回首頁 -->
							<li ${(url eq "/front-end/restaurants/Restaurants.jsp" || url eq "/front-end/restaurants/listStores_ByCompositeQuery.jsp" )?"class='active'":''}><a href="<%= request.getContextPath() %>/front-end/restaurants/Restaurants.jsp">Restaurants</a></li> <!-- 餐廳 -->
							<li ${(url eq "/front-end/article/SelectArticlePage.jsp")?"class='active'":''}><a href="<%= request.getContextPath() %>/front-end/article/SelectArticlePage.jsp">Articles</a></li> <!-- 食記 -->
							<li ${(url eq "/front-end/contact/iEatcontact.jsp")?"class='active'":''}><a href="<%=request.getContextPath() %>/front-end/contact/iEatcontact.jsp">Contact</a></li> <!-- 聯絡我們 -->
								<div class="clearfix"> </div>		
						</ul>
								<!-- script-for-nav -->
							<script>
								$( "span.menu" ).click(function() {
								  $( ".head-nav ul" ).slideToggle(300, function() {
									// Animation complete.
								  });
								});
								
								$( window ).resize(function() {
									if($( window ).width() >= 768) {
										$(".head-nav ul").show();
									}
									else {
										$(".head-nav ul").hide();
								   }
								});								
							</script>
						<!-- script-for-nav --> 
				</div>
					<div class="clearfix"> </div>	
				</div>
					<div class="clearfix"> </div>
			</div>
	</div>
<!-- header -->
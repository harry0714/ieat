<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ page import="com.store_type.model.*"%>
    <%@ page import="com.discount.model.*"%>
    <%@ page import="com.ord_meal.model.*"%>
	<%@ page import="com.ord.model.*"%>
	<%@ page import="com.meal.model.*"%>
	<%@ page import="com.advertisement.model.*"%>
	<%@ page import="com.article.model.*" %>
    <%@ page import="java.util.*"%> 
<% 
	Store_typeService stSvc = new Store_typeService();
	List<Store_typeVO> stList =  stSvc.getAll();
	request.setAttribute("stList", stList);
	
	DiscountService disSvc = new DiscountService();
	request.setAttribute("disSvc",disSvc);
	
	Ord_mealService ord_mealSvc = new Ord_mealService();
    List<Ord_mealVO> list = ord_mealSvc.getTopFiveMeal();
    pageContext.setAttribute("list",list);
    
    OrdService ordSvc = new OrdService();
    List<OrdVO> list1 = ordSvc.getTopFiveStore();
    pageContext.setAttribute("list1",list1);
    
    MealService mealSvc1 = new MealService();
    List<MealVO> mealrandomlist = mealSvc1.getRandomMeal();
    pageContext.setAttribute("mealrandomlist",mealrandomlist);
    
    AdvertisementService  adSvc = new AdvertisementService();
	List<AdvertisementVO> adList = adSvc.getRandom(3);
	request.setAttribute("adList",adList);
	MealService mealSvc = new MealService();
	List<MealVO> mealList = mealSvc.getRandom(16);
	request.setAttribute("mealList",mealList);
	request.setAttribute("mealSvc",mealSvc);

%>	
<jsp:useBean id="articleSvc" class="com.article.model.ArticleService" /> 
<%
    List<ArticleVO> artlist = articleSvc.getRandom();
    pageContext.setAttribute("artlist",artlist);
%>   


 
<!DOCTYPE html>
<html>
<head>
<link href="<%= request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath() %>/css/sweetalert.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>

<script src="<%= request.getContextPath() %>/js/jquery-2.2.3.min.js"></script>
<script src="<%= request.getContextPath() %>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script src="<%=request.getContextPath()%>/js/addressforindex.js"></script>

<!-- Logo --><link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/eat.png" /><!-- Logo -->
<title>iEat - 官方首頁</title>
<script type="text/javascript">
// 店家註冊 重導回首頁的alert
$(document).ready(function() {
	<c:if test="${not empty success}">
		swal("Success", "感謝你註冊成為iEat店家\n店家審核需要一至兩個工作天\n結果將以E-mail通知\n請您耐心等候\n謝謝您的體諒\n請繼續瀏覽iEat官方首頁\n如欲新增店家照片 請至店家專區新增", "success");
	</c:if>
	
	$(".ad").click(function(){
		var store_no = $(this).attr("alt");
		window.location.href = "<%=request.getContextPath()%>/store/store.do?action=getOne_For_Display&store_no=" +store_no;
	});
});
</script>	
<script>
// 新增至購物車
$(document).ready(function(){
    $(".add_to_cart").click(function() {
    	$.ajax({
            type: "POST",
            url: "<%= request.getContextPath()%>/checkout/shopping.do",
            data: { action:"ADD",
            		meal_no:createQueryString(this)},
           	success: function(){
                   announce();
               },
            error:function (xhr, ajaxOptions, thrownError) {
                console.log(xhr);
                alert(xhr.status);
                alert(thrownError)
               }
        })     
    });
    
    function createQueryString(e){
		return $(e).find("input[name='meal_no']").val();
    } 
    function announce(){
    	$("#cart").show().delay(1000).fadeOut();;
    }	
})
</script>

</head>
<body>
<div id="cart"><img src="<%= request.getContextPath()%>/images/add_cart.png">SUCCESS</div>
<jsp:useBean id="storeSvc" scope="page" class="com.store.model.StoreService"/>
<!-- header -->
<jsp:include page="/front-end/head.jsp"/>
<!-- header -->

<!-- banner -->
	<div class="banner"></div>
<!-- banner -->	
<!-- Carousel-->
	  <div id="myCarousel" class="carousel slide" data-ride="carousel">
		<!-- Indicators -->
		<ol class="carousel-indicators">
		  <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
		  <li data-target="#myCarousel" data-slide-to="1"></li>
		  <li data-target="#myCarousel" data-slide-to="2"></li>
		</ol>

		<!-- Wrapper for slides -->
		<div class="carousel-inner" role="listbox">
			<c:forEach var="adVO" items="${adList}" varStatus="s">
		  		<div class="item ${s.index==0?'active':'' }">
					<img class="ad" style="cursor: pointer;" src="<%=request.getContextPath()%>/advertisement/advertisementImageReader.do?ad_no=${adVO.ad_no}" title="${adVO.ad_imagetitle }" alt="${adVO.store_no }"/></a>
		  		</div>
		 	 </c:forEach>
		</div>

		<!-- Left and right controls -->
		<a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
		  <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
		  <span class="sr-only">Previous</span>
		</a>
		<a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
		  <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
		  <span class="sr-only">Next</span>
		</a>
	  </div>
	<!-- Carousel-->
	<div style="margin:20px">
		<div class="container">
			<div class="col-md-7">
				<div class="news">
					<h3>最新優惠</h3>
					<hr style="border:none;background-color:#5A9522;:red;height:5px;">
						<ul>
							<c:forEach var="disVO" items="${disSvc.all }">
							<li><span>${disVO.discount_startdate }~${disVO.discount_enddate }</span><span><a href="<%= request.getContextPath() %>/store/store.do?action=getOne_For_Display&store_no=${disVO.store_no}">${disVO.discount_title }</a></span></li>
							</c:forEach>
						</ul>
				</div><!--news-->
			</div>
			<div class="col-md-5">
			<!-- SearchBar -->
				<jsp:include page="/front-end/SearchBar.jsp"></jsp:include> 
			<!-- SearchBar -->	
			</div>
		</div><!--container-->
	</div>

<!-- latis -->	
	<div class="latis">
		<div class="container">
			<div class="col-md-9">
			<div class="index_row">
				<h3>推薦餐點</h3>
				<c:forEach var="mealVO" items="${mealrandomlist}" varStatus="s">
				<div class="col-md-4 latis-left">
					<h4>${mealVO.meal_name}</h4>
					<div style="height:150px;width:100%;overflow:hidden">			
						<img  src="<%= request.getContextPath()%>/mealphoto_read/mealphoto_read.do?photo_no=${mealVO.meal_no}" class="img-responsive" alt="">
					</div>
					<div class="simpleCart_shelfItem">
						<p style="width:45%;height:25px;overflow:hidden">${mealVO.meal_descr} </p>
						<div class="cur">
							<div class="cur-left add_to_cart">
								<input type="hidden" name="meal_no" value="${mealVO.meal_no}">
								<a class="morebtn hvr-rectangle-in" style="cursor: pointer;">加入餐車</a>
							</div>
							<div class="cur-right">
								<c:if test="${(mealVO.meal_discount==0)}">
									<h6>價格$${mealVO.meal_price}</h6>
								</c:if>
								<c:if test="${(mealVO.meal_discount>0)}">
									<h6>優惠價$ ${mealVO.meal_discount}</h6>
								</c:if>
							</div>
								<div class="clearfix"> </div>
						</div>
					</div>
				</div>
				</c:forEach>
					<div class="clearfix"> </div>
			</div><!--row-->
			
			
<!-- 			首頁食記改正 -->
			<div class="index_row">
				<h3>食記精選</h3>
				<c:forEach var="artVO" items="${artlist}" varStatus="s" begin="0" end="0">
					<div class="col-md-5">
						<div id="hot_article">
							<div style="width:100%;height:200px;overflow:hidden;margin-bottom:10px">
								<img class="img-responsive" src="
								<c:out value="${articleSvc.getArticelImageByArt_no(artVO.art_no)}" default="${pageContext.request.contextPath}/images/default_image.jpeg"/>
								"/>				
							</div>
							<h4><a href="<%=request.getContextPath()%>/article/article.do?action=getFor_Display&art_no=${artVO.art_no}">${artVO.art_name}</a></h4>
							<p style="overflow: hidden;line-height:1.4em;height:8.4em;">	${articleSvc.getContext(artVO.art_no,100)}</p>
							<a class="see" href="<%=request.getContextPath()%>/article/article.do?action=getFor_Display&art_no=${artVO.art_no}">See More</a>
							<div class="clearfix"> </div>
					</div>
				</div>	
  				</c:forEach>
					<div class="col-md-7">
						<div id="articles">
  							<c:forEach var="artVO" items="${artlist}" varStatus="s" begin="1">  
							<div class="article">
								<div class="col-md-4">
										<img class="img-responsive"
											src="
									<c:out value="${articleSvc.getArticelImageByArt_no(artVO.art_no)}" default="${pageContext.request.contextPath}/images/default_image.jpeg"/>
									" />

									</div>
								<div class="col-md-8">
									<h4><a href="<%=request.getContextPath()%>/article/article.do?action=getFor_Display&art_no=${artVO.art_no}">${artVO.art_name}</a></h4>
								<p style="overflow: hidden;line-height:1.4em;height:4.2em;">${articleSvc.getContext(artVO.art_no,45)}</p>
									<a class="see" href="<%=request.getContextPath()%>/article/article.do?action=getFor_Display&art_no=${artVO.art_no}">See More</a>
								</div>
								<div class="clearfix"> </div>
							</div>	
							</c:forEach>
						</div>
					</div>
				<div class="clearfix"> </div>
			</div><!--row-->
			</div><!--mark-->
			
			
			<div class="col-md-3">
				<div class="top">			
					<h3>排行榜</h3>
					<div class="top_half">
						<h4>人氣美食TOP5</h4>
						<div class="top_pic">
							<c:forEach var="ord_mealVO" items="${list}" varStatus="s">
							<c:if test="${s.count==1}">
								<a href="<%= request.getContextPath()%>/checkout/Orders.jsp?store_no=${mealSvc.getOneMeal(ord_mealVO.meal_no).store_no }">
									<img src="<%= request.getContextPath()%>/mealphoto_read/mealphoto_read.do?photo_no=${ord_mealVO.meal_no}">
								</a>
							</c:if>
							</c:forEach>
						</div>
						<ul>
							<c:forEach var="ord_mealVO" items="${list}" varStatus="s">
								<li><a href="<%= request.getContextPath()%>/checkout/Orders.jsp?store_no=${mealSvc.getOneMeal(ord_mealVO.meal_no).store_no }" id="b${s.count}">${mealSvc.getOneMeal(ord_mealVO.meal_no).meal_name } </a></li>
							</c:forEach>
						</ul>
					</div><!--top1-->
					<div class="top_half">
						<h4>人氣餐廳TOP5</h4>
						<div class="top_pic">
							<c:forEach var="ordVO" items="${list1}" varStatus="t">
								<c:if test="${t.count==1}">
									<a href="<%= request.getContextPath()%>/store/store.do?action=getOne_For_Display&store_no=${ordVO.store_no}">
										<img src="<%= request.getContextPath()%>/StorePhotoRead/StorePhotoRead.do?store_no=${ordVO.store_no}">
									</a>
								</c:if>
							</c:forEach>
						</div>
						<ul>
							<c:forEach var="ordVO" items="${list1}" varStatus="t">
								<li><a href="<%= request.getContextPath()%>/store/store.do?action=getOne_For_Display&store_no=${ordVO.store_no }" id="a${t.count}">${storeSvc.getOneStore(ordVO.store_no).store_name } </a></li>
							</c:forEach>
						</ul>
						<a style="float:right" href="<%= request.getContextPath() %>/front-end/restaurants/Restaurants.jsp">more>></a>
					</div><!--top2-->		
				</div>
			</div>
		</div><!--container-->

	</div>
<!-- latis -->

<!-- feature -->
<div class="feature">
		<div class="container">
			<div class="fle-xsel">
				<ul id="flexiselDemo3">
					<c:forEach var="mealVO" items="${mealList }">
					<li>
						<a href="<%= request.getContextPath() %>/checkout/Orders.jsp?store_no=${mealVO.store_no}"><img style="width: 7em;height: 7em" src="<%=request.getContextPath()%>/mealphoto_read/mealphoto_read.do?photo_no=${mealVO.meal_no}" class="img-responsive" title="${mealVO.meal_name }"></a>							
					</li>
					</c:forEach>
									
				</ul>
							
							 <script type="text/javascript">
								$(window).load(function() {
									
									$("#flexiselDemo3").flexisel({
										visibleItems: 8,
										animationSpeed: 1000,
										autoPlay: true,
										autoPlaySpeed: 3000,    		
										pauseOnHover: true,
										enableResponsiveBreakpoints: true,
										responsiveBreakpoints: { 
											portrait: { 
												changePoint:480,
												visibleItems: 2
											}, 
											landscape: { 
												changePoint:640,
												visibleItems: 3
											},
											tablet: { 
												changePoint:768,
												visibleItems: 3
											}
										}
									});
									
								});
								</script>
								<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.flexisel.js"></script>
					<div class="clearfix"> </div>
				
			</div>
		</div>
	</div>
<!-- feature -->

<!-- footer-->
	<jsp:include page="/front-end/footer.html"/>
<!-- footer-->	
</body>
</html>
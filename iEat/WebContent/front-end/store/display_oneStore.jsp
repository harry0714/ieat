<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.store.model.*"%>
<%@ page import="com.store_photo.model.*"%>
<%@ page import="com.member.model.*"%>
<%@ page import="com.discount.model.*" %>
<%@ page import="java.util.*"%>
<%
	StoreVO storeVO = (StoreVO) request.getAttribute("storeVO");
	Store_photoVO store_photoVO = (Store_photoVO) request.getAttribute("store_photoVO");
	String store_no = storeVO.getStore_no(); 
	MemberService memberSvc = new MemberService();
	List<MemberVO> memberList = memberSvc.getAll();
	pageContext.setAttribute("memberList", memberList);
	// 取得優惠
	DiscountService discountSvc = new DiscountService(); 
	List<DiscountVO> discountList = discountSvc.getStoreDiscount(store_no);	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath()%>/images/iEat_logo.png" />
<link href="<%= request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
<link href="<%= request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath() %>/css/sweetalert.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath() %>/css/font-awesome.min.css" rel="stylesheet" type="text/css" media="all" />
<link href="<%= request.getContextPath() %>/css/star-rating.css" rel="stylesheet" type="text/css" media="all" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<%= request.getContextPath() %>/js/jquery-2.2.3.min.js"></script>
<script src="<%= request.getContextPath() %>/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/js/sweetalert.js"></script>
<script src="<%=request.getContextPath()%>/js/star-rating.js"></script>	
<script src="http://maps.googleapis.com/maps/api/js"></script>
<jsp:useBean id="listStore_photoByStore_no" scope="request" type="java.util.Set"></jsp:useBean>
<jsp:useBean id="store_typeSvc" scope="page" class="com.store_type.model.Store_typeService" />

   <script type="text/javascript" src="<%= request.getContextPath()%>/js/jssor.slider.min.js"></script>
    <!-- use jssor.slider.debug.js instead for debug -->
    <script>
        jssor_1_slider_init = function() {
            
            var jssor_1_options = {
              $AutoPlay: true,
              $ArrowNavigatorOptions: {
                $Class: $JssorArrowNavigator$
              },
              $ThumbnailNavigatorOptions: {
                $Class: $JssorThumbnailNavigator$,
                $Cols: 9,
                $SpacingX: 3,
                $SpacingY: 3,
                $Align: 260
              }
            };
            
            var jssor_1_slider = new $JssorSlider$("jssor_1", jssor_1_options);
            
            //responsive code begin
            //you can remove responsive code if you don't want the slider scales while window resizing
            function ScaleSlider() {
                var refSize = jssor_1_slider.$Elmt.parentNode.clientWidth;
                if (refSize) {
                    refSize = Math.min(refSize, 600);
                    jssor_1_slider.$ScaleWidth(refSize);
                }
                else {
                    window.setTimeout(ScaleSlider, 30);
                }
            }
            ScaleSlider();
            $Jssor$.$AddEvent(window, "load", ScaleSlider);
            $Jssor$.$AddEvent(window, "resize", ScaleSlider);
            $Jssor$.$AddEvent(window, "orientationchange", ScaleSlider);
            //responsive code end
        };
    </script>

    <style>
        
        /* jssor slider arrow navigator skin 02 css */
        /*
        .jssora02l                  (normal)
        .jssora02r                  (normal)
        .jssora02l:hover            (normal mouseover)
        .jssora02r:hover            (normal mouseover)
        .jssora02l.jssora02ldn      (mousedown)
        .jssora02r.jssora02rdn      (mousedown)
        */
        .jssora02l, .jssora02r {
            display: block;
            position: absolute;
            /* size of arrow element */
            width: 55px;
            height: 55px;
            cursor: pointer;
            background: url('<%=request.getContextPath()%>/images/a02.png') no-repeat;
            overflow: hidden;
        }
        .jssora02l { background-position: -3px -33px; }
        .jssora02r { background-position: -63px -33px; }
        .jssora02l:hover { background-position: -123px -33px; }
        .jssora02r:hover { background-position: -183px -33px; }
        .jssora02l.jssora02ldn { background-position: -3px -33px; }
        .jssora02r.jssora02rdn { background-position: -63px -33px; }

        /* jssor slider thumbnail navigator skin 03 css */
        /*
        .jssort03 .p            (normal)
        .jssort03 .p:hover      (normal mouseover)
        .jssort03 .pav          (active)
        .jssort03 .pdn          (mousedown)
        */
        
        .jssort03 .p {
            position: absolute;
            top: 0;
            left: 0;
            width: 62px;
            height: 32px;
        }
        
        .jssort03 .t {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            border: none;
        }
        
        .jssort03 .w, .jssort03 .pav:hover .w {
            position: absolute;
            width: 60px;
            height: 30px;

            box-sizing: content-box;
        }
        
        .jssort03 .pdn .w, .jssort03 .pav .w {
            border-style: solid;
        }
        
        .jssort03 .c {
            position: absolute;
            top: 0;
            left: 0;
            width: 62px;
            height: 32px;
            background-color: #000;
        
            filter: alpha(opacity=45);
            opacity: .45;
            transition: opacity .6s;
            -moz-transition: opacity .6s;
            -webkit-transition: opacity .6s;
            -o-transition: opacity .6s;
        }
        .jssort03 .p:hover .c, .jssort03 .pav .c {
            filter: alpha(opacity=0);
            opacity: 0;
        }
        .jssort03 .p:hover .c {
            transition: none;
            -moz-transition: none;
            -webkit-transition: none;
            -o-transition: none;
        }
        
        * html .jssort03 .w {
            width /**/: 62px;
            height /**/: 32px;
        }      
    </style>
    
<script>
// 取得地址 
function doFirst(){
		
var latlng= new google.maps.LatLng(<c:out value="${storeVO.store_latlng}"/>);
var map= new google.maps.Map(document.getElementById('position'), {
	zoom: 16,
	center: latlng,
	mapTypeId: google.maps.MapTypeId.ROADMAP
	});
	
var marker = new google.maps.Marker({
	position:latlng,
	map:map,
	/* icon:'dgtp.gif' */
	});	
}
window.addEventListener('load',doFirst,false);

</script>
<script type="text/javascript">
$(document).ready(function() {
	<c:if test="${not empty noStore}">
		swal("此功能僅提供給會員\n請重新登入為會員\n再執行此動作"); 
	</c:if>
	
});

function handler(comment_no){
	swal({   
		title: "評論檢舉",   
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
				url:"<%=request.getContextPath()%>/comment_report/comment_report.do",
				data:{action:"addAjax",comment_no:comment_no,comment_report_context:inputValue},
				success : function(data) {
					if("add" == data){
						swal("", "你的檢舉已收到 ，會盡速處理", "success");
						websocket.send(JSON.stringify({
							"message" : "前端送出"
						}));
						return;
					}
					if("login" == data){
						swal("請登入會員");
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
}
</script>	
<title>iEat - 餐廳資料</title>
</head>
<body>
<!-- header -->
<jsp:include page="/front-end/head.jsp"/>
<!-- header -->

<!-- about -->
<div class="main-1">
	<div class="container">
		<jsp:include page="/front-end/store/store_left_bar.jsp"/> <!-- 店家左邊列表 -->		
		<div class="col-md-10">
			<div class="panel panel-success">
				<div class="panel-heading">店家資訊
				</div>
				<div class="panel-body">
					<div class="row">
						<div id="jssor_1" style="position: relative; margin: 0 auto; top: 0px; left: 0px; width: 600px; height: 400px; overflow: hidden; visibility: hidden;">
						<!-- Loading Screen -->
						<div data-u="loading" style="position: absolute; top: 0px; left: 0px;">
							<div style="filter: alpha(opacity=70); opacity: 0.7; position: absolute; display: block; top: 0px; left: 0px; width: 100%; height: 100%;"></div>
							<div style="position:absolute;display:block;background:url('<%=request.getContextPath() %>/images/loading.gif') no-repeat center center;top:0px;left:0px;width:100%;height:100%;"></div>
						</div>
						<!--slider-->		
						<div data-u="slides" style="cursor: default; position: relative; top: 0px; left: 0px; width: 600px; height: 400px; overflow: hidden;">
						<c:forEach var="store_photoVO" items="${listStore_photoByStore_no }">
							<div data-p="112.50" style="display: none;">
								<img data-u="image" src="<%=request.getContextPath()%>/photo_read/photo_read.do?photo_no=${store_photoVO.photo_no}" >
								<img data-u="thumb" src="<%=request.getContextPath()%>/photo_read/photo_read.do?photo_no=${store_photoVO.photo_no}" >
							</div>
						</c:forEach>
							<a data-u="ad" href="http://www.jssor.com" style="display:none">jQuery Slider</a>
						
						</div>
						<!-- Thumbnail Navigator -->
						<div u="thumbnavigator" class="jssort03" style="position:absolute;left:0px;bottom:0px;width:600px;height:60px;" data-autocenter="1">
							<div style="position: absolute; top: 0; left: 0; width: 100%; height:100%; background-color: #000; filter:alpha(opacity=30.0); opacity:0.3;"></div>
							<!-- Thumbnail Item Skin Begin -->
							<div u="slides" style="cursor: default;">
								<div u="prototype" class="p">
									<div class="w">
										<div u="thumbnailtemplate" class="t"></div>
									</div>
									<div class="c"></div>
								</div>
							</div>
							<!-- Thumbnail Item Skin End -->
						</div>
						<!-- Arrow Navigator -->
						<span data-u="arrowleft" class="jssora02l" style="top:0px;left:8px;width:55px;height:55px;" data-autocenter="2"></span>
						<span data-u="arrowright" class="jssora02r" style="top:0px;right:8px;width:55px;height:55px;" data-autocenter="2"></span>
						</div>
							<script>
								jssor_1_slider_init();
							</script>
						<!--slider-->
					</div>
					<div class="row">
						<div class="col-md-7">
							<div class="cell">
								<div class="bs-callout bs-callout-success news">
								 	<h3>優惠消息</h3>
									<ul>
									<c:forEach var="disVO" items="${disList }">
										<li><span>${disVO.discount_startdate }~${disVO.discount_enddate }</span><span>${disVO.discount_title }</span></li>
									</c:forEach>
									</ul>
								</div>	
								<h3>${storeVO.store_name }</h3>
								<p>
								<c:if test="${empty storeVO.store_intro }">
									<font color="red">無該店家的介紹</font>
								</c:if>
								<c:if test="${not empty storeVO.store_intro }">
									${storeVO.store_intro }
								</c:if>								
								</p>
							</div>	
						</div>
						<div class="col-md-5">
							<div class="cell"  style="border-left:1px solid #ccc">
								<ul>
									<li><i class="glyphicon glyphicon-bookmark"></i> 
										<c:forEach var="store_typeVO" items="${store_typeSvc.all }">
											<c:if test="${storeVO.store_type_no==store_typeVO.store_type_no}">
											${store_typeVO.store_type_name} 
											</c:if>
										</c:forEach>餐廳</li>
									<li><i class="glyphicon glyphicon-home"></i> ${storeVO.store_ads }</li>
									<li><i class="glyphicon glyphicon-phone-alt"></i> ${storeVO.store_phone }</li>
									<li><i class="glyphicon glyphicon-time"></i>	
<!-- 									為避免forTokens跳出錯誤訊息 改用forEach取代 -->
<!-- 									字串在Servlet那邊先切好  再重設attribute回來																 -->
<%-- 										<c:forTokens items="${(storeVO.store_open)}" delims="-" var="open" varStatus="varStatus">			 --%>
<%-- 											<c:if test="${open==1}"> --%>
<%-- 												${varStatus.index}:00~${varStatus.index+1}:00<br> --%>
<%--                  							</c:if>		 --%>
<%-- 										</c:forTokens> --%>
										<c:forEach items="${store_open}" var="open" varStatus="varStatus">
											<c:if test="${open==0}">	<%-- 抓起始時段 如果0後面為1 表示開始--%>							
												<c:forEach items="${store_open}" var="open2" varStatus="varStatus2" begin="${varStatus.index+1}" end="${varStatus.index+1}">
													<c:if test="${open2==1 }">
														${varStatus2.index}:00 ～
													</c:if>
												</c:forEach>	
											</c:if>
											<c:if test="${open==1 }">  <%-- 抓結束時段 如果1後面為0 表示結束 --%>
												<c:forEach items="${store_open}" var="open3" varStatus="varStatus3" begin="${varStatus.index+1}" end="${varStatus.index+1}">
													<c:if test="${open3==0 }">
														${varStatus3.index}:00<br>
													</c:if>
												</c:forEach>
											</c:if>															
										</c:forEach>
												
										<c:if test="${not (storeVO.store_open).contains('1')}">
                 							<font color="red">該店家無營業時段資訊</font>
             							</c:if></li>
									<li><i class="glyphicon glyphicon-star"></i> <fmt:formatNumber value="${storeVO.store_star}" pattern="#.##" type="number"/></li>
								</ul>				
								<div class="contact-details">
								  <div class="contact-map">
									 <h4>FIND US HERE</h4>
									 <div id="position" style="width:300px; height:300px; border:0;"></div>
								  </div>
								</div>  
							</div>
						</div>
					</div>
					<div class="well">
					<c:if test="${! empty user }">
                    <form role="form" action="<%=request.getContextPath()%>/store_comment/store_comment.do" method=post>
                        <div class="form-group">
                        	<input id="input-21c" value="0" type="number" class="rating" name="comment_level" min=0 max=5 step=0.5 data-size="xs" data-stars="5">
                            <textarea class="form-control" name="comment_conect" rows="3"></textarea>
                        </div>
                        <input type="hidden" name="store_no" value="${param.store_no }">
                        <input type="hidden" name="action" value="insert">
                        <button type="submit" class="btn btn-primary">評論</button>
                    </form>
                    <hr>
                    </c:if>
                     <c:if test="${empty user }">
                    		<h3 style="text-align:center;color:gray">要評論請先登入會員</h3>
                    <hr>
                    </c:if>
                    <h4> ${scList.size() }則 餐廳評論:</h4>
                    <c:forEach var="scVO" items="${scList}">
                    <div class="media">
                    <a class="pull-left">
                        <img class="media-object img-circle" src="<%= request.getContextPath() %>/member/memberImageReader.do?mem_no=${scVO.mem_no}" style="width:64px;height:64px" alt="">
                    </a>
                    <div class="media-body">
                        <h4 class="media-heading"><c:forEach var="memberVO" items="${memberList}"><c:if test="${memberVO.mem_no == scVO.mem_no }">${memberVO.mem_acct }</c:if></c:forEach>
                            <small>
                            	<fmt:formatDate var="comment_time" value="${scVO.comment_time}" pattern="MMM dd yyyy aaa hh:ss"/>${comment_time } 
                            	<div class="dropdown" style="float:right;">
  									<button class="btn btn-default dropdown-toggle btn-xs" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="border:none;">
  										<span class="glyphicon glyphicon-option-vertical"></span>
  									</button>
  									<ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenu2">
    									<li><a href="javascript:handler('${scVO.comment_no}');">檢舉</a></li>
  									</ul>
								</div>	
                            	<small>
                            		<input id="input-3" name="input-3" value="${scVO.comment_level }" class="rating-loading form-control" data-size="xs" >
                            	</small>
                            </small>
                        </h4>
                        ${scVO.comment_conect }
                        
                    </div>
                    	
                	</div>
                	<hr>
                	</c:forEach>
                	<script>
						$(document).on('ready', function(){
    						$('.rating-loading').rating({displayOnly: true, step: 0.5});
						});
					</script>
                </div>
				</div>
			</div>			  
		</div>		
	<div class="clearfix"></div>
	</div>
</div>

<!-- footer-->
<jsp:include page="/front-end/footer.html"/>
<!-- footer-->	
</body>
</html>
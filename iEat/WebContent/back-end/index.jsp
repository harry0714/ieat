<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="com.adm.model.*"%>
<%@ page import="com.store.model.*"%>

<%
    AdmService admSvc = new AdmService();
    List<AdmVO> list = admSvc.getAll();
    pageContext.setAttribute("list",list);
    StoreService storeSvc = new StoreService();
	List<StoreVO> storeList = storeSvc.getAll();
	pageContext.setAttribute("storeList", storeList);
    AdmVO admVO =(AdmVO) session.getAttribute("admVO");
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
<!-- Logo --><link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/images/eat.png" /><!-- Logo -->
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
$(function () {
	$.ajax({
		type:"POST",
		// url 抓到 IndexBackEnd.java這隻工具程式 
		url:"<%=request.getContextPath()%>/back_end/index.html",
			data : {action : "getAllStore"},
			dataType : "json",
			success : function(data) {
				drawMap(data);
			},
			error : function() {
				alert("error");
			}
		});
		function drawMap(oJson) {
			var latlng1 = new google.maps.LatLng(23.763828, 121.825829);
			//設定地圖參數
			var mapOptions = {
				zoom : 8, //初始放大倍數
				center :latlng1 , //中心點所在位置
				mapTypeId : google.maps.MapTypeId.ROADMAP
			//正常2D道路模式
			};
			var map = new google.maps.Map(document.getElementById("map"),mapOptions);
			
			var i = 0;
			var currentInfowindow = '';
			$.each(oJson, function() {
				var s = oJson[i].store_latlng;

				var latlng = s.split(",");

				var latlngMap = new google.maps.LatLng(latlng[0], latlng[1]);
				var marker = new google.maps.Marker({
					position : latlngMap, //經緯度
					title : oJson[i].store_name, //顯示文字
					map : map
				//指定要放置的地圖對象
				});
				
			  var contentString = '<h2>'+oJson[i].store_name+'</h2><p>'+oJson[i].store_phone+'</p>';

			  var infowindow = new google.maps.InfoWindow({
			    content: contentString
			  });
			  marker.addListener('click', function() {
				 	if(currentInfowindow != ''){
				 		currentInfowindow.close();
				 	}
				 	currentInfowindow = infowindow;
				    infowindow.open(map, marker);
			  });
				i++;
			});
		}
	});
</script>
</head>
<body>
	<!-- header --><jsp:include page="/back-end/page/head.jsp"/><!-- header -->
	<!-- sideBar --><jsp:include page="/back-end/page/body.jsp"/><!-- sideBar -->
        <div id="page-wrapper">
           	<div class="col-sm-9 col-sm-offset-3 col-md-12 col-md-offset-2 main"> 
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-2 col-md-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-users fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <span class="huge" id="totalMember"></span>
                                   
                                    <div>New members!</div>
                                </div>
                            </div>
                        </div>
                       
                           
                      
                    </div>
                </div>
                <div class="col-lg-2 col-md-6">
                    <div class="panel panel-green">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-edit fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                   <span class="huge" id="art"></span>
                                    <div>New Articles!</div>
                                </div>
                            </div>
                        </div>
                       
                    </div>
                </div>
                <div class="col-lg-2 col-md-6">
                    <div class="panel panel-yellow">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-shopping-cart fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                    <span class="huge" id="ord"></span>
                                    <div>New Orders!</div>
                                </div>
                            </div>
                        </div>
                       
                    </div>
                </div>
                <div class="col-lg-2 col-md-6">
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-table fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                     <span class="huge" id="allreservation"></span>
                                    <div>New Reservation!</div>
                                </div>
                            </div>
                        </div>
                       
                    </div>
                </div>
                <div class="col-lg-2 col-md-6">
                    <div class="panel panel-red">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-home fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
                                  <span class="huge" id="allstore"></span>
                                    <div>New Stores!</div>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
            </div>
            <!-- /.row -->
           <div class="row">
				<div id="map" style="width: 100%; height: 600px"></div>
			</div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/metisMenu.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/raphael-min.js"></script>
    <script src="<%=request.getContextPath()%>/js/morris.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/morris-data.js"></script>
    <script src="<%=request.getContextPath()%>/js/sb-admin-2.js"></script>

</body>

</html>

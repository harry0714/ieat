$(function () {
	$.ajax({
		type:"POST",
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
			var latlng1 = new google.maps.LatLng(24.985505, 121.57531);
			//設定地圖參數
			var mapOptions = {
				zoom : 10, //初始放大倍數
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
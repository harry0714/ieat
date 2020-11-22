<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
		
			
			<div class="membermenu">
				<ul class="list-group">
					<!--<li class="list-group-item">首頁</li>-->
					<li class="list-group-item">店家資料
						<div class="list-group" hidden>
							<a href="<%= request.getContextPath() %>/store/store.do?action=getOne_For_Update" class="list-group-item">基本資料</a>
							<a href="<%= request.getContextPath() %>/front-end/store/passwordupdate.jsp" class="list-group-item">修改密碼</a>
							<a href="<%= request.getContextPath() %>/front-end/store/listOneStorePhoto.jsp" class="list-group-item">修改店家照片</a>
						</div>
					</li>					
					<li class="list-group-item"><a href="<%=request.getContextPath()%>/front-end/store/listAllMeal.jsp">餐點管理</a></li>
					<li class="list-group-item"><a href="<%=request.getContextPath()%>/front-end/discount/discount.do?action=get_store_discount">優惠管理</a></li>				
					<li class="list-group-item">管理訂單紀錄
						<div class="list-group" hidden>
							<a href="<%= request.getContextPath() %>/ord/ord.do?action=storeord" class="list-group-item">訂餐紀錄列表</a>
							<a href="<%= request.getContextPath() %>/reservation/reservation.do?action=storereservation" class="list-group-item">訂位紀錄列表</a>
						</div>
					</li>
				</ul>
			</div>
				<!--店家專區動畫 -->
				<script>
					$(document).ready(function() {
						$("div.membermenu > ul > li").click(function() {
							$(this).children("div").toggle("slow");
						});
					});
				</script>


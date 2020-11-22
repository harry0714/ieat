<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


			<div class="membermenu">
				<ul class="list-group">
					<!--<li class="list-group-item">首頁</li>-->
					<li class="list-group-item">個人資料
						<div class="list-group" hidden>
							<a href="<%= request.getContextPath() %>/member/member.do?action=member_for_update" class="list-group-item">基本資料</a>
							<a href="<%= request.getContextPath() %>/front-end/member/passwordupdate.jsp" class="list-group-item">修改密碼</a>
							<a href="<%= request.getContextPath() %>/front-end/member/emailupdate.jsp" class="list-group-item">修改驗證信箱</a>
						</div>
					</li>
					<li class="list-group-item">個人食記
						<div class="list-group" hidden>
							<a href="<%= request.getContextPath() %>/front-end/article/writearticle.jsp" class="list-group-item">撰寫食記</a>
							<a href="<%= request.getContextPath() %>/article/article.do?action=memberarticle" class="list-group-item">食記列表</a>
						</div>
					</li>
					<li class="list-group-item">我的最愛
						<div class="list-group" hidden>
							<a href="<%= request.getContextPath() %>/bookmarkstore/bookmarkstore.do?action=enterbookmarkstore" class="list-group-item">最愛餐廳</a>
							<a href="<%= request.getContextPath() %>/bookmarkmeal/bookmarkmeal.do?action=enterbookmarkmeal" class="list-group-item">最愛美食</a>
						</div>
					</li>
					<li class="list-group-item">訂單紀錄
						<div class="list-group" hidden>
							<a href="<%= request.getContextPath() %>/ord/ord.do?action=memberord" class="list-group-item">訂餐紀錄列表</a>
							<a href="<%= request.getContextPath() %>/reservation/reservation.do?action=memberreservation" class="list-group-item">訂位紀錄列表</a>
						</div>
					</li>
				</ul>
				<!--會員專區動畫 -->
				<script>
					$(document).ready(function() {
						$("div.membermenu > ul > li").click(function() {
							$(this).children("div").toggle("slow");
						});
					});
				</script>
			</div>


<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- GNB 높이값: height:60px; --%>
<div style="position: relative; height:60px; width: 100%; background: #133b52;">
	<div style="width: 100%; position: absolute;">
		<!-- 로고 -->
		<!-- 메뉴 -->
		<div id="jqxmenu" class="netismenu">
			<ul id="mega-menu" class="mega-menu" style="visibility: hidden">${sessionScope.menu}</ul>
		</div>
		<%-- 로그아웃 --%>
		<div id="btnLogout" class="userLogout" style="z-index: 99999"></div>	
		<div class="userId"><a href="" style="color: #fff;">${sessionScope.User.userName}</a></div>
	</div>
</div>



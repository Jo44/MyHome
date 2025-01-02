<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="snake.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="<%=path%>/css/snake/snake.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div id="wrapper">
		<!-- Navigation Bar -->
		<%@include file="../navbar.jspf"%>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-12 col-md-offset-1 col-md-10">
						<div class="panel panel-large center-div">
							<div class="panel-heading">
								<i class="fa fa-dragon fa-fw"></i> <fmt:message key="snake.header" />
							</div>
							<div class="panel-body">
								<div id="focusCatcher" tabindex="-1"></div>
								<noscript class="col-xs-offset-1 col-xs-10 center big-spaced-vertical"><fmt:message key="javascript.snake" /></noscript>
								<div id="scoreDisplay">
									<span> High score   : </span><span id="highscore">0</span><br />
									<span>Current score : </span><span id="score">0</span>
								</div>
								<canvas id="snakeCanvas" width="600" height="600"></canvas>
								<div class="row">
									<div id="gameControls" class="col-xs-6">
										<select id="level">
									        <option value="0" selected>Level 1</option>
									        <option value="1">Level 2</option>
									        <option value="2">Level 3</option>
									    </select>
									    <select id="speed">
									        <option value="100" selected>Speed x1</option>
									        <option value="75">Speed x1.5</option>
									        <option value="50">Speed x2</option>
									    </select>
									</div>
									<div id="audioControls" class="col-xs-6">
									    <button id="muteMusic" class="btn btn-classic btn-xs"></button>
									    <div id="volumeControl">
									        <span>Volume</span>
									        <input id="volume" type="range" name="volume" min="0.0" max="1.0" step="0.1" value="0.3">
									    </div>
									    <button id="muteFx" class="btn btn-classic btn-xs"></button>
									</div>
								</div>
							</div>
							<div class="panel-footer"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- jQuery -->
	<script src="<%=path%>/js/plugins/jquery.js"></script>
	<!-- Bootstrap JavaScript -->
	<script src="<%=path%>/js/plugins/bootstrap.js"></script>
	<!-- Font Awesome JavaScript -->
	<script src="https://kit.fontawesome.com/3010c2773a.js" crossorigin="anonymous"></script>
	<!-- Snake JavaScript -->
	<script>
		var path = "<%=path%>";
	</script>
	<script src="<%=path%>/js/snake/snake.js"></script>
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
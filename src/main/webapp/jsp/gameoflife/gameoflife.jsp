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
<title><fmt:message key="game.life.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="<%=path%>/css/gameoflife/gameoflife.css" rel="stylesheet" type="text/css" />
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
								<i class="fa fa-bug fa-fw"></i> <fmt:message key="game.life.header" />
								<div class="pull-right">
									<i id="game-controls-arrow" class="fas fa-arrow-circle-up fa-fw" onclick="displayGameControls()"></i>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<div>
									<button id="refresh" type="button" class="btn btn-classic btn-sm btn-fixed" onclick="refresh()"><fmt:message key="game.life.refresh" /></button>
								</div>
								<div id="grid-container" class="spaced-vertical"></div>
								<div id="game-controls">
									<div>
										<label for="speed" title="50 <> 1000">Vitesse :</label> <input type="range" id="speed" name="speed" value="250" min="50" max="1000" step="10" oninput="updateValue('speed')" required /> <span id="speedValue">250</span> ms</br>
										<label for="rows" title="10 <> 150">Lignes :</label> <input type="range" id="rows" name="rows" value="100" min="10" max="150" step="1" oninput="updateValue('rows')" required /> <span id="rowsValue">100</span></br>
										<label for="cols" title="10 <> 150">Colonnes :</label> <input type="range" id="cols" name="cols" value="100" min="10" max="150" step="1" oninput="updateValue('cols')" required /> <span id="colsValue">100</span></br>
									</div>
								</div>
								<noscript class="col-xs-offset-1 col-xs-10 center big-spaced-vertical"><fmt:message key="javascript.game.life" /></noscript>
							</div>
							<div class="panel-footer">
								<span id="lifeCopyright">Conway's Game of Life - 1970 <i class="far fa-copyright fa-fw"></i></span>
							</div>
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
	<!-- Game of Life JavaScript -->
	<script src="<%=path%>/js/gameoflife/gameoflife.js"></script>
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
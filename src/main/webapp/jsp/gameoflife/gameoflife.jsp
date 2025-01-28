<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="game.life.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/gameoflife/gameoflife.css" rel="stylesheet" type="text/css" />
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
									<button id="generate" type="button" class="btn btn-classic btn-sm btn-fixed" onclick="generate()"><fmt:message key="game.life.refresh" /></button>
								</div>
								<!-- Life appears here -->
								<div id="life" class="spaced-vertical"></div>
								<!-- Life ends there -->
								<div id="game-controls">
									<div>
										<label for="speed" title="0 <> 100"><fmt:message key="game.life.speed" /></label> <input type="range" id="speed" name="speed" value="75" min="0" max="100" step="1" oninput="updateValue('speed')" required /> <span id="speedText">75</span> %</br>
										<label for="rows" title="10 <> 150"><fmt:message key="game.life.rows" /></label> <input type="range" id="rows" name="rows" value="100" min="10" max="150" step="10" oninput="updateValue('rows')" required /> <span id="rowsText">100</span></br>
										<label for="cols" title="10 <> 150"><fmt:message key="game.life.cols" /></label> <input type="range" id="cols" name="cols" value="100" min="10" max="150" step="10" oninput="updateValue('cols')" required /> <span id="colsText">100</span></br>
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
	<script src="${pageContext.request.contextPath}/js/plugins/jquery.js"></script>
	<!-- Bootstrap JavaScript -->
	<script src="${pageContext.request.contextPath}/js/plugins/bootstrap.js"></script>
	<!-- Font Awesome JavaScript -->
	<script src="https://kit.fontawesome.com/3010c2773a.js" crossorigin="anonymous"></script>
	<!-- Game of Life JavaScript -->
	<script src="${pageContext.request.contextPath}/js/gameoflife/gameoflife.js"></script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
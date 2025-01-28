<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="health.food.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/health/health.css" rel="stylesheet" type="text/css" />
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
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="far fa-clock"></i> <fmt:message key="health.food.timer.header" />
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/health"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body unselectable">
								<!-- Timer -->
								<div class="row big-marged-top marged-bottom">
									<div id="chronoPanel">
										<div id="chronoBckgnd">
											<div id="periodLeft">00:00</div>
											<div id="state"><fmt:message key="health.food.timer.state.stop" /></div>
										</div>
									</div>
								</div>
								<noscript class="col-xs-12 spaced-vertical"><fmt:message key="javascript.timer" /></noscript>
								<!-- Volume -->
								<div class="row spaced-vertical">
									<div>
										<span><fmt:message key="health.food.timer.volume" /></span>
									</div>
									<div>
										<input id="volume" type="range" name="volume" min="0.0" max="1.0" step="0.1" value="0.5">
									</div>
								</div>
								<!-- Presets -->
								<div class="row spaced-vertical">
									<fmt:message key="health.food.timer.presets" />
									<div class="spaced-vertical">
										<div>
											<button id="btn5Min" class="btn btn-classic btn-sm btn-fixed"><fmt:message key="health.food.timer.btn.5min" /></button>
											<button id="btn3Min" class="btn btn-classic btn-sm btn-fixed"><fmt:message key="health.food.timer.btn.3min" /></button>
										</div>
										<div class="marged-top">
											<button id="btn2Min" class="btn btn-classic btn-sm btn-fixed"><fmt:message key="health.food.timer.btn.2min" /></button>
											<button id="btn1Min30" class="btn btn-classic btn-sm btn-fixed"><fmt:message key="health.food.timer.btn.1min30" /></button>
											<button id="btn1Min" class="btn btn-classic btn-sm btn-fixed"><fmt:message key="health.food.timer.btn.1min" /></button>
										</div>
									</div>
								</div>
								<!-- Custom -->
								<div class="row marged-top big-marged-bottom">
									<fmt:message key="health.food.timer.custom" /><br />
									<div class="spaced-vertical">
										<button id="btnStop" class="btn btn-classic btn-sm">Stop</button>
										<input id="customTime" type="time" name="customTime" min="00:00:00" max="23:59:59" value="00:02:30" step="1" />
										<button id="btnGo" class="btn btn-classic btn-sm">Go</button>
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
	<script src="${pageContext.request.contextPath}/js/plugins/jquery.js"></script>
	<!-- Bootstrap JavaScript -->
	<script src="${pageContext.request.contextPath}/js/plugins/bootstrap.js"></script>
	<!-- Font Awesome JavaScript -->
	<script src="https://kit.fontawesome.com/3010c2773a.js" crossorigin="anonymous"></script>
	<!-- Timer Init JavaScript -->
	<script>
	    const stateRunning = '<fmt:message key="health.food.timer.state.running" />';
		const stateStop = '<fmt:message key="health.sport.timer.state.stop" />';
	</script>
	<!-- Timer Food JavaScript -->
	<script src="${pageContext.request.contextPath}/js/health/food/timer.js"></script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
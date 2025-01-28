<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="health.main.page.title" /></title>
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
								<i class="fas fa-heartbeat fa-fw"></i> <fmt:message key="health.main.header" />
							</div>
							<div class="panel-body big-marged-top marged-bottom">
								<!-- Food -->
								<div class="col-xs-4 info-block unselectable">
									<div class="marged-bottom">
										<a href="${pageContext.request.contextPath}/health/food"><img class="info-img" src="${pageContext.request.contextPath}/img/health/food.png" alt="<fmt:message key="health.food" />" /></a>
									</div>
									<div>
										<a href="${pageContext.request.contextPath}/health/food"><span class="info-label"><fmt:message key="health.food" /></span></a>
									</div>
								</div>
								<!-- Sport -->
								<div class="col-xs-4 info-block unselectable">
									<div class="marged-bottom">
										<a href="${pageContext.request.contextPath}/health/sports"><img class="info-img" src="${pageContext.request.contextPath}/img/health/sport.png" alt="<fmt:message key="health.sport" />" /></a>
									</div>
									<div>
										<a href="${pageContext.request.contextPath}/health/sports"><span class="info-label"><fmt:message key="health.sport" /></span></a>
									</div>
								</div>
								<!-- Weight -->
								<div class="col-xs-4 info-block unselectable">
									<div class="marged-bottom">
										<a href="${pageContext.request.contextPath}/health/weights"><img class="info-img" src="${pageContext.request.contextPath}/img/health/weight.png" alt="<fmt:message key="health.weight" />" /></a>
									</div>
									<div>
										<a href="${pageContext.request.contextPath}/health/weights"><span class="info-label"><fmt:message key="health.weight" /></span></a>
									</div>
								</div>
								<!-- End -->
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
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
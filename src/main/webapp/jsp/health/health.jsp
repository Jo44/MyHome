<%@ page import="fr.my.home.bean.User"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="health.main.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="<%=path%>/css/health/health.css" rel="stylesheet" type="text/css" />
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
								<div class="col-xs-4 info-block unselectable">
									<div class="marged-bottom">
										<a href="<%=path%>/health/food"><img class="info-img" src="<%=path%>/img/health/food.png" alt="<fmt:message key="health.food" />" /></a>
									</div>
									<div>
										<a href="<%=path%>/health/food"><span class="info-label"><fmt:message key="health.food" /></span></a>
									</div>
								</div>
								<div class="col-xs-4 info-block unselectable">
									<div class="marged-bottom">
										<a href="<%=path%>/health/sports"><img class="info-img" src="<%=path%>/img/health/sport.png" alt="<fmt:message key="health.sport" />" /></a>
									</div>
									<div>
										<a href="<%=path%>/health/sports"><span class="info-label"><fmt:message key="health.sport" /></span></a>
									</div>
								</div>
								<div class="col-xs-4 info-block unselectable">
									<div class="marged-bottom">
										<a href="<%=path%>/health/weights"><img class="info-img" src="<%=path%>/img/health/weight.png" alt="<fmt:message key="health.weight" />" /></a>
									</div>
									<div>
										<a href="<%=path%>/health/weights"><span class="info-label"><fmt:message key="health.weight" /></span></a>
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
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="status.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div id="wrapper">
		<!-- Navigation Bar -->
		<%@include file="../navbar.jspf"%>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-12">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fas fa-wrench fa-fw"></i> <fmt:message key="status.header" />
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/status_inside"><fmt:message key="global.refresh" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<!-- Session Start -->
								<div class="col-xs-offset-1 col-xs-10 big-marged-top marged-bottom center">
									<p><i class="fas fa-clock fa-fw"></i> <fmt:message key="status.session" />
										<c:choose>
											<c:when test="${not empty requestScope.sessionStart}">
												<span class="green bold">${requestScope.sessionStart}</span>
											</c:when>
											<c:otherwise>
												<span class="red bold">???</span>
											</c:otherwise>
										</c:choose>
									</p>
								</div>
								<!-- Database Status -->
								<div class="col-xs-offset-1 col-xs-10 marged-top big-marged-bottom center">
									<p><i class="fas fa-database fa-fw"></i> <fmt:message key="status.database" />
										<c:choose>
											<c:when test="${requestScope.databaseOnline}">
												<span class="green bold"><fmt:message key="global.online" /></span>
											</c:when>
											<c:otherwise>
												<span class="red bold"><fmt:message key="global.offline" /></span>
											</c:otherwise>
										</c:choose>
									</p>
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
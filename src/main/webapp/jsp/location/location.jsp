<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="loc.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/location/location.css" rel="stylesheet" type="text/css" />
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
								<i class="fas fa-sitemap fa-fw"></i> <fmt:message key="loc.search.header" />
								<div class="pull-right">
									<button type="submit" form="locationForm" class="btn btn-primary btn-xs btn-fixed"><fmt:message key="global.search" /></button>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<!-- Error -->
								<c:if test="${not empty requestScope.error}">
									<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification center" role="alert">
										<p><strong>Oops ! </strong>${requestScope.error}<i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
									</div>
								</c:if>
								<!-- Form Search -->
								<form id="locationForm" action="location" method="post">
									<div class="col-xs-offset-1 col-xs-10 center big-marged-top">
										<span class="bold"><fmt:message key="loc.search.website.ip" /></span>
									</div>
									<div class="col-xs-offset-1 col-xs-10 center marged-top big-marged-bottom">
										<input id="inputSearch" type="text" name="websiteIP" maxlength="100" placeholder="<fmt:message key="loc.search.example" />" title="<fmt:message key="help.location.website.ip" />" required autofocus />
										<i id="clearSearch" class="fas fa-times-circle fa-fw red"></i>
									</div>
								</form>
								<!-- End Form Search -->
							</div>
							<div class="panel-footer"></div>
						</div>
						<!-- Result -->
						<c:if test="${not empty requestScope.objectIPAPI}">
							<div class="panel panel-default center-div">
								<div class="panel-heading">
									<!-- Header -->
									<c:choose>
										<c:when test="${requestScope.objectIPAPI.lon >= -180 && requestScope.objectIPAPI.lon < -30}">
											<i class="fas fa-globe-americas fa-fw"></i> <fmt:message key="loc.map.header" />
										</c:when>
										<c:when test="${requestScope.objectIPAPI.lon >= -30 && requestScope.objectIPAPI.lon < 80}">
											<i class="fas fa-globe-africa fa-fw"></i> <fmt:message key="loc.map.header" />
										</c:when>
										<c:otherwise>
											<i class="fas fa-globe-asia fa-fw"></i> <fmt:message key="loc.map.header" />
										</c:otherwise>
									</c:choose>
									<div class="pull-right">
										<a class="btn btn-danger btn-xs btn-fixed" href="${pageContext.request.contextPath}/location"><fmt:message key="global.clear" /></a>
									</div>
									<div class="fixHeight"></div>
								</div>
								<div class="panel-body no-padding">
									<!-- Informations -->
									<p class="center spaced">
										<span class="bold"><fmt:message key="loc.map.search" /> </span>${requestScope.search}<br />
										<span class="bold"><fmt:message key="loc.map.ip" /> </span>${requestScope.objectIPAPI.query}<br />
										<span class="bold"><fmt:message key="loc.map.organization" /> </span>${requestScope.objectIPAPI.org}<br />
										<span class="bold"><fmt:message key="loc.map.country" /> </span>${requestScope.objectIPAPI.country}<br />
									</p>
									<noscript class="col-xs-offset-1 col-xs-10 big-spaced-vertical"><fmt:message key="javascript.location" /></noscript>
									<!-- Google Map -->
									<div id="map"></div>
								</div>
								<div class="panel-footer"></div>
							</div>
						</c:if>
						<!-- End Result -->
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
	<!-- Location JavaScript -->
	<c:if test="${not empty requestScope.objectIPAPI}">
		<script>
			var latParam = ${requestScope.objectIPAPI.lat};
			var lngParam = ${requestScope.objectIPAPI.lon};
		</script>
		<script src="${pageContext.request.contextPath}/js/location/location.js"></script>
		<!-- Google Maps API -->
		<script src="https://maps.googleapis.com/maps/api/js?key=<fmt:message key="google.api.key" bundle="${settings}" />&callback=initMap" async defer></script>
	</c:if>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
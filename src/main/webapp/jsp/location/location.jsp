<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.api.ObjectIPAPI"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
    String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	ObjectIPAPI objectIPAPI = (ObjectIPAPI) view.getValueForKey("objectIPAPI");
	String search = (String) view.getValueForKey("search");
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="loc.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="<%=path%>/css/location/location.css" rel="stylesheet" type="text/css" />
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
								<%
								    if (error != null) {
								%>
								<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification center" role="alert">
									<p><strong>Oops ! </strong><%=error%><i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
								</div>
								<%
								    }
								%>
								<form id="locationForm" action="location" method="post">
									<div class="col-xs-offset-1 col-xs-10 center big-marged-top">
										<span class="bold"><fmt:message key="loc.search.website.ip" /></span>
									</div>
									<div class="col-xs-offset-1 col-xs-10 center marged-top big-marged-bottom">
										<input id="inputSearch" type="text" name="websiteIP" maxlength="100" placeholder="<fmt:message key="loc.search.example" />" title="<fmt:message key="help.location.website.ip" />" required autofocus>
										<i id="clearSearch" class="fas fa-times-circle fa-fw red"></i>
									</div>
								</form>
							</div>
							<div class="panel-footer"></div>
						</div>
						<%
						    if (objectIPAPI != null) {
						%>
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<%
									if (objectIPAPI.getLon() >= -180 && objectIPAPI.getLon() < -30) {
								%>
										<i class="fas fa-globe-americas fa-fw"></i> <fmt:message key="loc.map.header" />
								<%
									} else if (objectIPAPI.getLon() >= -30 && objectIPAPI.getLon() < 80) {
								%>
										<i class="fas fa-globe-africa fa-fw"></i> <fmt:message key="loc.map.header" />
								<%
									} else {
								%>
										<i class="fas fa-globe-asia fa-fw"></i> <fmt:message key="loc.map.header" />
								<%
									}
								%>
								<div class="pull-right">
									<a class="btn btn-danger btn-xs btn-fixed" href="<%=path%>/location"><fmt:message key="global.clear" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body no-padding">
								<p class="center spaced">
									<span class="bold"><fmt:message key="loc.map.search" /> </span><%=search%><br />
									<span class="bold"><fmt:message key="loc.map.ip" /> </span><%=objectIPAPI.getQuery()%><br />
									<span class="bold"><fmt:message key="loc.map.organization" /> </span><%=objectIPAPI.getOrg()%><br />
									<span class="bold"><fmt:message key="loc.map.country" /> </span><%=objectIPAPI.getCountry()%><br />
								</p>
								<noscript class="col-xs-offset-1 col-xs-10 big-spaced-vertical"><fmt:message key="javascript.location" /></noscript>
								<div id="map"></div>
							</div>
							<div class="panel-footer"></div>
						</div>
						<%
						    }
						%>
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
	<%
		if (objectIPAPI != null) {
	%>
	<!-- Location JavaScript -->
	<script>
		var latParam = <%=objectIPAPI.getLat()%>;
		var lngParam = <%=objectIPAPI.getLon()%>;
	</script>
	<script src="<%=path%>/js/location/location.js"></script>
	<!-- Google Maps API -->
	<script src="https://maps.googleapis.com/maps/api/js?key=<fmt:message key="google.api.key" bundle="${settings}" />&callback=initMap" async defer></script>
	<%
		}
	%>
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
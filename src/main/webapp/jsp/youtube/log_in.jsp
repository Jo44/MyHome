<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="yt.login.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- Font Awesome CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/font-awesome.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/youtube/youtube.css" rel="stylesheet" type="text/css" />
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
								<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.login.header" />
							</div>
							<div id="infoBox" class="panel-body no-padding">
								<h4><fmt:message key="yt.global.infos.header" /></h4>
								<p><fmt:message key="yt.global.infos.msg" /></p>
								<a class="btn btn-primary btn-xs btn-large big-marged-top marged-bottom" href="https://accounts.google.com/o/oauth2/v2/auth?client_id=${requestScope.clientID}&redirect_uri=${requestScope.redirectURI}&response_type=code&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fyoutube%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fyoutube.force-ssl&access_type=offline&prompt=consent&state=${sessionScope.state}"><fmt:message key="yt.global.login" /></a>
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
    <!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
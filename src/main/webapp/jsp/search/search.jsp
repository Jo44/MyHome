<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="search.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/search/search.css" rel="stylesheet" type="text/css" />
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
								<i class="fas fa-search fa-fw"></i> <fmt:message key="search.header" />
								<div class="pull-right">
									<a id="searchBtn" class="btn btn-primary btn-xs btn-fixed" href="" target="_self"><fmt:message key="global.search" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<!-- Search -->
								<div class="col-xs-offset-1 col-xs-10 center big-marged-top">
									<div class="rd-search-group">
										<input id="rdSearchGoogle" type="radio" name="searchFilter" value="google" checked="checked">
										<label for="rdSearchGoogle" class="rd-search-google"><img src="${pageContext.request.contextPath}/img/search/logo-google.png" alt="Google" /></label>
									</div>
									<div class="rd-search-group">
										<input id="rdSearchYouTube" type="radio" name="searchFilter" value="youtube">
										<label for="rdSearchYouTube" class="rd-search-youtube"><img src="${pageContext.request.contextPath}/img/search/logo-youtube.png" alt="YouTube" /></label>
									</div>
									<div class="rd-search-group">
										<input id="rdSearchAmazon" type="radio" name="searchFilter" value="amazon">
										<label for="rdSearchAmazon" class="rd-search-amazon"><img src="${pageContext.request.contextPath}/img/search/logo-amazon.png" alt="Amazon" /></label>
									</div>
								</div>
								<div class="col-xs-offset-1 col-xs-10 center marged-top big-marged-bottom">
									<input id="inputSearch" type="text" name="search" maxlength="200" placeholder="<fmt:message key="search.url.example" />" title="<fmt:message key="help.search" />" autofocus />
									<i id="clearSearch" class="fas fa-times-circle fa-fw red"></i>
								</div>
								<noscript class="col-xs-offset-1 col-xs-10 center big-marged-bottom"><fmt:message key="javascript.search" /></noscript>
								<!-- End Search -->
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
	<!-- Search JavaScript -->
	<script src="${pageContext.request.contextPath}/js/search/search.js"></script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
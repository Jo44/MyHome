<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="recovery.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/users/users.css" rel="stylesheet" type="text/css" />
<!-- reCAPTCHA JavaScript -->
<script src="https://www.google.com/recaptcha/api.js" async defer></script>
</head>
<body>
	<div class="lang-box">
	<!-- Language Box -->
	<%@include file="../../langbox.jspf"%>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-xs-12 col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4">
				<div class="panel panel-default center-div">
					<div class="panel-heading">
						<h3 class="panel-title in-line"><fmt:message key="recovery.header" /></h3>
						<div class="pull-right">
							<c:choose>
							    <c:when test="${databaseOnline}">
							    	<img src="${pageContext.request.contextPath}/img/trafficlight-green.png" alt="ON" />
							    </c:when>
							    <c:otherwise>
							    	<img src="${pageContext.request.contextPath}/img/trafficlight-red.png" alt="OFF" />
							    </c:otherwise>
							</c:choose>
						</div>
						<div class="fixHeight"></div>
					</div>
					<div class="panel-body">
						<c:if test="${not empty requestScope.error}">
							<div class="col-xs-12">
								<div id="alert-danger" class="alert alert-danger center" role="alert">
									<p><strong>${requestScope.error}</strong><i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
								</div>
							</div>
						</c:if>
						<!-- Form Recovery -->
						<form action="recovery" method="post">
							<div class="col-xs-12 marged-top">
								<input class="btn-block" placeholder="<fmt:message key="global.email" />" name="user_email" type="email" maxlength="50" title="<fmt:message key="help.6.50" />" required autofocus />
							</div>
							<noscript class="col-xs-12 marged-top center"><fmt:message key="javascript.recaptcha" /></noscript>
							<div id="recaptchaBox" class="col-xs-12 marged-top center">
								<div class="g-recaptcha" data-theme="dark" data-sitekey="<fmt:message key="recaptcha.public" bundle="${settings}" />"></div>
							</div>
							<div class="col-xs-12 marged-top">
								<button type="submit" class="btn btn-md btn-info btn-block"><fmt:message key="global.continue" /></button>
							</div>
							<div class="col-xs-12 spaced-vertical">
								<a class="btn btn-md btn-info btn-block" href="${pageContext.request.contextPath}/check"><fmt:message key="recovery.back" /></a>
							</div>
						</form>
						<!-- End Form Recovery -->
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
		</div>
	</div>
	<!-- jQuery -->
	<script src="${pageContext.request.contextPath}/js/plugins/jquery.js"></script>
	<!-- Font Awesome JavaScript -->
	<script src="https://kit.fontawesome.com/3010c2773a.js" crossorigin="anonymous"></script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>

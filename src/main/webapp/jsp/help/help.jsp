<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="help.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/help/help.css" rel="stylesheet" type="text/css" />
<!-- reCAPTCHA JavaScript -->
<script src="https://www.google.com/recaptcha/api.js" async defer></script>
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
								<i class="fas fa-question-circle fa-fw"></i> <fmt:message key="help.header" />
								<div class="pull-right">
									<button type="submit" form="newHelpForm" class="btn btn-success btn-xs btn-fixed"><fmt:message key="global.send" /></button>
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/home"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body row justify-content-around">
								<!-- Error -->
								<c:if test="${not empty requestScope.error}">
									<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification center" role="alert">
										<p><strong>Oops ! </strong>${requestScope.error}<i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
									</div>
								</c:if>
								<!-- Form Contact -->
								<form id="newHelpForm" action="help" method="post">
									<div class="col-xs-offset-1 col-xs-10 big-marged-top info-contact">
										<p><fmt:message key="help.description" /></p>
									</div>
									<div class="col-xs-offset-1 col-xs-10 big-marged-top">
										<label for="areaContact"><fmt:message key="help.message" /></label><br />
										<textarea id="areaContact" name="message" maxlength="5000" cols="25" rows="10" title="<fmt:message key="help.1.5000" />" autofocus></textarea>
									</div>
									<noscript class="col-xs-offset-1 col-xs-10 big-spaced-vertical"><fmt:message key="javascript.recaptcha" /></noscript>
									<div id="recaptchaBox" class="col-xs-12 big-spaced-vertical center">
										<div class="g-recaptcha" data-theme="dark" data-sitekey="<fmt:message key="recaptcha.public" bundle="${settings}" />"></div>
									</div>
								</form>
								<!-- End Form Contact -->
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
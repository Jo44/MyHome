<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="sign.up.check.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap Core CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- CSS My Home -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- CSS Perso -->
<link href="${pageContext.request.contextPath}/css/users/users.css" rel="stylesheet" type="text/css" />
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
						<h3 class="panel-title in-line"><fmt:message key="sign.up.check.header" /></h3>
						<div class="pull-right">
							<a class="btn btn-classic btn-xs btn-fixed" href="${pageContext.request.contextPath}/check"><fmt:message key="global.back" /></a>
						</div>
						<div class="fixHeight"></div>
					</div>
					<div class="padding-body center">
						<!-- Message -->
						<p><fmt:message key="sign.up.check.msg1" /></p>
						<br />
						<p><fmt:message key="sign.up.check.msg2" /></p>
						<br />
						<p><fmt:message key="sign.up.check.msg3" /> <a href="mailto:<fmt:message key="admin.email" bundle="${settings}" />"><fmt:message key="global.lower.email" /></a>.</p>
						<!-- End Message -->
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
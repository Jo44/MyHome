<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="password.page.title" /></title>
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
		<%@include file="../../navbar.jspf"%>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-12 col-md-offset-1 col-md-10">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fas fa-user fa-fw"></i> <fmt:message key="password.header" />
								<div class="pull-right">
									<button type="submit" form="updatePasswordForm" class="btn btn-success btn-xs btn-fixed"><fmt:message key="global.update" /></button>
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/settings"><fmt:message key="global.cancel" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<c:if test="${not empty requestScope.error}">
									<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification center" role="alert">
										<p><strong>Oops ! </strong>${requestScope.error}<i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
									</div>
								</c:if>
								<!-- Form Update -->
								<form id="updatePasswordForm" action="change" method="post">
									<div class="col-xs-12 big-marged-top">
										<label for="inputOldPassword"><fmt:message key="global.old.password" /></label><br />
										<input id="inputOldPassword" type="password" name="oldpassword" pattern=".{6,30}" title="<fmt:message key="help.6.30" />" required autofocus />
									</div>
									<div class="col-xs-12 big-marged-top">
										<label for="inputNewPassword1"><fmt:message key="global.new.password" /></label><br />
										<input id="inputNewPassword1" type="password" name="newpassword" pattern=".{6,30}" title="<fmt:message key="help.6.30" />" required />
									</div>
									<div class="col-xs-12 big-spaced-vertical">
										<label for="inputNewPassword2"><fmt:message key="global.confirm.password" /></label><br />
										<input id="inputNewPassword2" type="password" name="newpassword-confirm" pattern=".{6,30}" title="<fmt:message key="help.6.30" />" required />
									</div>
								</form>
								<!-- End Form Update -->
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
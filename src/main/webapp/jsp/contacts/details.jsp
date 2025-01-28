<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="contacts.details.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- International Telephone Input CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/intlTelInput.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/contacts/contacts.css" rel="stylesheet" type="text/css" />
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
								<i class="fas fa-edit fa-fw"></i> <fmt:message key="contacts.details.header" />
								<div class="pull-right">
									<button type="submit" form="contactForm" class="btn btn-warning btn-xs btn-fixed"><fmt:message key="global.update" /></button>
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/contacts"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification center disable" role="alert">
									<p><strong>Oops ! </strong><fmt:message key="error.contact.phone" /><i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
								</div>
								<!-- Form Update -->
								<form id="contactForm" action="contacts" method="post">
									<input type="hidden" name="action" value="update">
									<input type="hidden" name="idContact" value="${requestScope.contact.id}">
									<input type="hidden" id="intlPhoneNumber" name="intlPhoneNumber" value="">
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 big-marged-top">
										<div class="inputWithIcon">
											<input id="textFirstname" name="firstname" type="text" value="${requestScope.contact.firstname}" placeholder="<fmt:message key="global.firstname" />" pattern=".{1,30}" title="<fmt:message key="help.1.30" />" autofocus required>
											<i class="fas fa-user fa-fw" aria-hidden="true"></i>
										</div>
									</div>
									<div class="col-xs-12 col-sm-5 big-marged-top">
										<div class="inputWithIcon">
											<input id="textLastname" name="lastname" type="text" value="${requestScope.contact.lastname}" placeholder="<fmt:message key="global.lastname" />" pattern=".{1,30}" title="<fmt:message key="help.1.30" />">
											<i class="fas fa-user fa-fw" aria-hidden="true"></i>
										</div>
									</div>
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 big-marged-top">
										<div class="inputWithIcon">
											<input id="textEmail" name="email" type="email" value="${requestScope.contact.email}" placeholder="<fmt:message key="global.email.example" />" maxlength="50" pattern="[^@]+@[^@]+\.[a-zA-Z]{2,6}" title="<fmt:message key="help.6.50" />">
											<i class="fas fa-envelope fa-fw" aria-hidden="true"></i>
										</div>
									</div>
									<div class="col-xs-12 col-sm-5 big-marged-top">
										<div class="inputWithIcon">
											<input id="textTwitter" name="twitter" type="text" value="${requestScope.contact.twitter}" placeholder="<fmt:message key="global.twitter" />" pattern=".{1,30}" title="<fmt:message key="help.1.30" />">
											<i class="fas fa-at fa-fw" aria-hidden="true"></i>
										</div>
									</div>
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 big-spaced-vertical">
										<input id="textPhone" name="phone" type="tel" value="${requestScope.contact.phone}" pattern=".{1,16}" title="<fmt:message key="help.1.16" />">
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
	<!-- International Telephone Input JavaScript -->
	<script src="${pageContext.request.contextPath}/js/plugins/intlTelInput/intlTelInput.js"></script>
	<!-- Form Contacts JavaScript -->
	<script>
		var path = "${pageContext.request.contextPath}";
	</script>
	<script src="${pageContext.request.contextPath}/js/contacts/contacts.js"></script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="settings.page.title" /></title>
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
								<i class="fas fa-cogs fa-fw"></i> <fmt:message key="settings.header" />
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/home"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<c:choose>
								    <c:when test="${not empty requestScope.error}">
										<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification center" role="alert">
											<p><strong>Oops ! </strong>${requestScope.error}<i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
										</div>
									</c:when>
									<c:when test="${not empty requestScope.success}">
										<div id="alert-success" class="col-xs-offset-1 col-xs-10 alert alert-success panel-notification center" role="alert">
											<p>${requestScope.success}<i id="close-alert-success" class="fas fa-times-circle fa-fw close-button light-grey"></i></p>
										</div>
									</c:when>
								    <c:otherwise></c:otherwise>
								</c:choose>
								<div class="col-xs-offset-1 col-xs-10">
									<!-- Credentials -->
									<div class="big-marged-top">
										<label for="showLogin"><fmt:message key="global.login" /></label>
									</div>
									<div>
										<input id="showLogin" type="text" value="${sessionScope.user.name}" disabled />
									</div>
									<div class="big-marged-top">
										<label for="showPassword"><fmt:message key="global.password" /></label>
									</div>
									<div>
										<input id="showPassword" type="password" value="<fmt:message key="settings.display.pass" />" disabled />
									</div>
									<div class="big-marged-top">
										<label for="showEmail"><fmt:message key="global.email" /></label>
									</div>
									<div>
										<input id="showEmail" class="marged-bottom" type="text" value="${sessionScope.user.email}" disabled />
									</div>
									<div class="big-marged-top">
										<a class="btn btn-classic btn-sm btn-large" href="${pageContext.request.contextPath}/change"><fmt:message key="settings.update.pass" /></a>
									</div>
									<!-- YouTube Infos Modal -->
									<div class="big-spaced-vertical">
										<a id="ytInfos" class="btn btn-classic btn-sm btn-large" data-toggle="modal" data-target=".modal-infos" data-backdrop="static"><fmt:message key="yt.global.infos.header" /> <i class="fas fa-info-circle fa-fw"></i></a>
										<div class="modal fade modal-infos" tabindex="-1" role="dialog" aria-labelledby="modal_label_infos">
											<div class="modal-dialog" role="document">
												<div class="modal-content center">
													<div class="modal-header">
														<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
														<h4 id="modal_label_infos" class="modal-title"><fmt:message key="yt.global.infos.header" /></h4>
													</div>
													<div class="modal-body">
														<p><fmt:message key="yt.global.infos.msg" /></p>
													</div>
													<div class="modal-footer center">
														<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.close" /></button>
													</div>
												</div>
											</div>
										</div>
									</div>
									<!-- End YouTube Infos Modal -->
								</div>
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
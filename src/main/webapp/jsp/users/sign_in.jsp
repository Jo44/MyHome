<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="sign.in.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/users/users.css" rel="stylesheet" type="text/css" />
<!-- Cookie Consent CSS (cookieconsent.insites.com) -->
<link href="${pageContext.request.contextPath}/css/plugins/cookieconsent.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="lang-box">
	<!-- Language Box -->
	<%@include file="../langbox.jspf"%>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-xs-12 col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4">
				<div class="panel panel-default center-div">
					<div class="panel-heading">
						<h3 class="panel-title in-line"><fmt:message key="sign.in.header" /></h3>
						<div class="pull-right">
							<c:choose>
							    <c:when test="${requestScope.databaseOnline}">
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
						<!-- Error -->
						<c:if test="${not empty requestScope.error}">
							<div class="col-xs-12">
								<div id="alert-danger" class="alert alert-danger center" role="alert">
									<p><strong>${requestScope.error}</strong><i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
								</div>
							</div>
						</c:if>
						<!-- Form Sign In -->
						<form action="check" method="post">
							<!-- Login -->
							<div class="col-xs-12 marged-top"> 
								<c:choose>
									<c:when test="${not empty requestScope.userName}">
										<input class="btn-block" name="user_name" type="text" pattern=".{3,30}" placeholder="<fmt:message key="global.login" />" title="<fmt:message key="help.3.30" />" required value='${requestScope.userName}' />
									</c:when>
									<c:otherwise>
										<input class="btn-block" name="user_name" type="text" pattern=".{3,30}" placeholder="<fmt:message key="global.login" />" title="<fmt:message key="help.3.30" />" required autofocus />
									</c:otherwise>
								</c:choose>
							</div>
							<!-- Password -->
							<div class="col-xs-12 marged-top">
								<c:choose>
									<c:when test="${not empty requestScope.userName}">
									 	<input class="btn-block" name="user_pass" type="password" pattern=".{6,30}" placeholder="<fmt:message key="global.password" />" title="<fmt:message key="help.6.30" />" required autofocus />
									</c:when>
									<c:otherwise>
										<input class="btn-block" name="user_pass" type="password" pattern=".{6,30}" placeholder="<fmt:message key="global.password" />" title="<fmt:message key="help.6.30" />" required />
									</c:otherwise>
								</c:choose>
							</div>
							<!-- Remember Me -->
							<div class="col-xs-12 checkbox center">
								<label>
									<c:choose>
										<c:when test="${not empty requestScope.userName}">
										 	<input name="remember" type="checkbox" value="true" checked />
										</c:when>
										<c:otherwise>
											<input name="remember" type="checkbox" value="true" />
										</c:otherwise>
									</c:choose>
									<span><fmt:message key="sign.in.remember" /></span>
								</label>
							</div>
							<!-- Connection -->
							<div class="col-xs-12">
								<button type="submit" class="btn btn-md btn-info btn-block"><fmt:message key="global.connection" /></button>
							</div>
						</form>
						<!-- Register -->
						<div class="col-xs-12 center marged-top">
							<a class="login-link" href="${pageContext.request.contextPath}/register"><fmt:message key="sign.in.sign.up" /></a>
						</div>
						<!-- Recovery -->
						<div class="col-xs-12 center">
							<a class="login-link" href="${pageContext.request.contextPath}/recovery"><fmt:message key="sign.in.recovery" /></a>
						</div>
						<!-- End -->
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
	<!-- Cookie Consent JavaScript (cookieconsent.insites.com) -->
	<script src="${pageContext.request.contextPath}/js/plugins/cookieconsent.js"></script>
	<!-- Cookie JavaScript -->
	<script src="${pageContext.request.contextPath}/js/users/cookie.js"></script>
	<script>
		const cookieMsg = '<fmt:message key="sign.in.cookie.msg" />';
		const cookieLink = '<fmt:message key="sign.in.cookie.link" />';
	</script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>

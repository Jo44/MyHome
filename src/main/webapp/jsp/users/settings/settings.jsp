<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	String success = (String) view.getValueForKey("success");
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="settings.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
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
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/home"><fmt:message key="global.back" /></a>
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
									} else if (success != null) {
								%>
								<div id="alert-success" class="col-xs-offset-1 col-xs-10 alert alert-success panel-notification center" role="alert">
									<p><%=success%><i id="close-alert-success" class="fas fa-times-circle fa-fw close-button light-grey"></i></p>
								</div>
								<%
									}
								%>
								<div class="col-xs-offset-1 col-xs-10">
									<div class="big-marged-top">
										<label for="showLogin"><fmt:message key="global.login" /></label>
									</div>
									<div>
										<input id="showLogin" type="text" value="<%=user.getName()%>" disabled />
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
										<input id="showEmail" class="marged-bottom" type="text" value="<%=user.getEmail()%>" disabled />
									</div>
									<div class="big-spaced-vertical">
										<a class="btn btn-warning btn-xs btn-fixed" href="<%=path%>/change"><fmt:message key="settings.update.pass" /></a>
									</div>
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
	<script src="<%=path%>/js/plugins/jquery.js"></script>
	<!-- Bootstrap JavaScript -->
	<script src="<%=path%>/js/plugins/bootstrap.js"></script>
	<!-- Font Awesome JavaScript -->
	<script src="https://kit.fontawesome.com/3010c2773a.js" crossorigin="anonymous"></script>
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
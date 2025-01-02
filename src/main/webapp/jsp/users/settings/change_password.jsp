<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="password.page.title" /></title>
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
								<i class="fas fa-user fa-fw"></i> <fmt:message key="password.header" />
								<div class="pull-right">
									<button type="submit" form="updatePasswordForm" class="btn btn-success btn-xs btn-fixed"><fmt:message key="global.update" /></button>
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/settings"><fmt:message key="global.cancel" /></a>
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
									}
								%>
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
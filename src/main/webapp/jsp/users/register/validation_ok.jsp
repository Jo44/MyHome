<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
    String path = getServletContext().getContextPath();
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<!-- Redirection automatique après 5 secondes -->
<meta http-equiv="refresh" content="5; url=<%=path%>/check" />
<title><fmt:message key="sign.up.valid.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap Core CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- CSS My Home -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- CSS Perso -->
<link href="<%=path%>/css/users/users.css" rel="stylesheet" type="text/css" />
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
						<h3 class="panel-title in-line"><fmt:message key="sign.up.valid.header" /></h3>
					</div>
					<div class="padding-body center">
						<p><fmt:message key="sign.up.valid.msg1" /></p>
						<br />
						<p><fmt:message key="sign.up.valid.msg2" /></p>
						<br />
						<p><a class="login-link" href="<%=path%>/check"><fmt:message key="sign.up.valid.msg3" /></a></p>
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
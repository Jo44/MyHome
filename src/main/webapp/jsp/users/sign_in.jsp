<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
    String path = getServletContext().getContextPath();
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	boolean databaseOnline = (boolean) view.getValueForKey("databaseOnline");
	String userName = (String) view.getValueForKey("userName");
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="sign.in.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="<%=path%>/css/users/users.css" rel="stylesheet" type="text/css" />
<!-- Cookie Consent CSS (cookieconsent.insites.com) -->
<link href="<%=path%>/css/plugins/cookieconsent.css" rel="stylesheet" type="text/css" />
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
							<%
							    if (databaseOnline) {
							%>
							<img src="<%=path%>/img/trafficlight-green.png" alt="ON" />
							<%
							    } else {
							%>
							<img src="<%=path%>/img/trafficlight-red.png" alt="OFF" />
							<%
							    }
							%>
						</div>
						<div class="fixHeight"></div>
					</div>
					<div class="panel-body">
						<%
						    if (error != null) {
						%>
						<div class="col-xs-12">
							<div id="alert-danger" class="alert alert-danger center" role="alert">
								<p><strong><%=error%></strong><i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
							</div>
						</div>
						<%
						    }
						%>
						<form action="check" method="post">
							<div class="col-xs-12 marged-top">
								<input class="btn-block" name="user_name" type="text" pattern=".{3,30}" placeholder="<fmt:message key="global.login" />" title="<fmt:message key="help.3.30" />" required
								<%
									if (userName != null) {
										out.print("value='" + userName + "'");
								    } else {
										out.print(" autofocus");
								    }
								%>>
							</div>
							<div class="col-xs-12 marged-top">
								<input class="btn-block" name="user_pass" type="password" pattern=".{6,30}" placeholder="<fmt:message key="global.password" />" title="<fmt:message key="help.6.30" />" required
								<%
									if (userName != null) {
										out.print(" autofocus");
								    }
								%>>
							</div>
							<div class="col-xs-12 checkbox center">
								<label>
									<input name="remember" type="checkbox" value="true"
									<%
										if (userName != null) {
											out.print(" checked");
									    }
									%>>
									<span><fmt:message key="sign.in.remember" /></span>
								</label>
							</div>
							<div class="col-xs-12">
								<button type="submit" class="btn btn-md btn-info btn-block"><fmt:message key="global.connection" /></button>
							</div>
						</form>
						<div class="col-xs-12 center marged-top">
							<a class="login-link" href="<%=path%>/register"><fmt:message key="sign.in.sign.up" /></a>
						</div>
						<div class="col-xs-12 center">
							<a class="login-link" href="<%=path%>/recovery"><fmt:message key="sign.in.recovery" /></a>
						</div>
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
		</div>
	</div>
	<!-- jQuery -->
	<script src="<%=path%>/js/plugins/jquery.js"></script>
	<!-- Font Awesome JavaScript -->
	<script src="https://kit.fontawesome.com/3010c2773a.js" crossorigin="anonymous"></script>
	<!-- Cookie Consent JavaScript (cookieconsent.insites.com) -->
	<script src="<%=path%>/js/plugins/cookieconsent.js"></script>
	<!-- Cookie JavaScript -->
	<script src="<%=path%>/js/users/cookie.js"></script>
	<script>
		var cookieMsg = '<fmt:message key="sign.in.cookie.msg" />';
		var cookieLink = '<fmt:message key="sign.in.cookie.link" />';
	</script>
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
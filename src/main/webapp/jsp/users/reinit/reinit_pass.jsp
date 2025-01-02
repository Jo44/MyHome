<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
    String path = getServletContext().getContextPath();
    ViewJSP view = (ViewJSP) request.getAttribute("view");
    String token = (String) view.getValueForKey("token");
    String error = (String) view.getValueForKey("error");
    boolean databaseOnline = (boolean) view.getValueForKey("databaseOnline");
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="reinit.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
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
						<h3 class="panel-title in-line"><fmt:message key="reinit.header" /></h3>
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
						<form action="reinit" method="post">
							<input type='hidden' name='token' value='<% if (token != null && token.trim().length() > 0) { out.print(token); } %>'>
							<div class="col-xs-12 marged-top">
								<input id="inputNewPassword1" class="btn-block" type="password" name="newpassword" pattern=".{6,30}"
									placeholder="<fmt:message key="global.new.password" />" title="<fmt:message key="help.6.30" />" required autofocus>
							</div>
							<div class="col-xs-12 marged-top">
								<input id="inputNewPassword2" class="btn-block" type="password" name="newpassword-confirm" pattern=".{6,30}"
									placeholder="<fmt:message key="global.confirm.password" />" title="<fmt:message key="help.6.30" />" required>
							</div>
							<div class="col-xs-12 marged-top">
								<button type="submit" class="btn btn-md btn-info btn-block"><fmt:message key="global.update" /></button>
							</div>
							<div class="col-xs-12 spaced-vertical">
								<a class="btn btn-md btn-info btn-block" href="<%=path%>/check"><fmt:message key="recovery.back" /></a>
							</div>
						</form>
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
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
	Timestamp sessionStart = (Timestamp) view.getValueForKey("sessionStart");
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
<title><fmt:message key="status.page.title" /></title>
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
		<%@include file="../navbar.jspf"%>
		<!-- Page Content -->
		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-12">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fas fa-wrench fa-fw"></i> <fmt:message key="status.header" />
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/status_inside"><fmt:message key="global.refresh" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<div class="col-xs-offset-1 col-xs-10 big-marged-top marged-bottom center">
									<p><i class="fas fa-clock fa-fw"></i> <fmt:message key="status.session" />
										<%
											if (sessionStart != null) {
										%>
											<span class="green bold"><%=formatter.format(sessionStart)%></span>
										<%
											} else {
										%>
											<span class="red bold">null</span>
										<%
											}
										%>
									</p>
								</div>
								<div class="col-xs-offset-1 col-xs-10 marged-top big-marged-bottom center">
									<p><i class="fas fa-database fa-fw"></i> <fmt:message key="status.database" />
										<%
											if (databaseOnline) {
										%>
											<span class="green bold"><fmt:message key="global.online" /></span>
										<%
											} else {
										%>
											<span class="red bold"><fmt:message key="global.offline" /></span>
										<%
											}
										%>
									</p>
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
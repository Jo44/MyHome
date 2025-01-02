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
<title><fmt:message key="yt.pl.add.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- Font Awesome CSS -->
<link href="<%=path%>/css/plugins/font-awesome.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="<%=path%>/css/youtube/youtube.css" rel="stylesheet" type="text/css" />
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
								<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.pl.add.header" />
								<div class="pull-right">
									<button type="submit" form="newPlaylistForm" class="btn btn-success btn-xs btn-fixed"><fmt:message key="global.save" /></button>
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/youtube_playlists?action=list"><fmt:message key="global.back" /></a>
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
								<form id="newPlaylistForm" action="youtube_playlists" method="post">
									<input type="hidden" name="action" value="add">
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 marged-top">
										<label for="textTitle"><fmt:message key="yt.pl.title" /></label>
										<br />
										<input id="textTitle" name="titlePlaylist" type="text" pattern=".{1,150}" title="<fmt:message key="help.1.150" />" autofocus required>
									</div>
									<div class="col-xs-12 col-sm-5 marged-top">
										<label for="textDescription"><fmt:message key="yt.pl.description" /></label>
										<br />
										<input id="textDescription" name="descriptionPlaylist" type="text" pattern=".{0,5000}" title="<fmt:message key="help.0.5000" />">
									</div>
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 marged-top">
										<label><fmt:message key="global.privacy" /></label>
										<br />
										<label id="lblRdPublic" class="radioButton marged-top">
											<input name="radioPrivacy" type="radio" value="public" checked>
											<span class="checkmarkRdBtn"></span>
											<span class="checkbox-item"><fmt:message key="global.public" /> <i class="fas fa-globe-americas fa-fw img-radio"></i></span>
										</label>
										<label id="lblRdPrivate" class="radioButton">
											<input name="radioPrivacy" type="radio" value="private">
											<span class="checkmarkRdBtn"></span>
											<span class="checkbox-item"><fmt:message key="global.private" /> <i class="fas fa-lock fa-fw img-radio"></i></span>
										</label>
										<label id="lblRdUnlisted" class="radioButton">
											<input name="radioPrivacy" type="radio" value="unlisted">
											<span class="checkmarkRdBtn"></span>
											<span class="checkbox-item"><fmt:message key="global.unlisted" /> <i class="fas fa-user-secret fa-fw img-radio"></i></span>
										</label>
									</div>
									<div class="col-xs-12 col-sm-5 spaced-vertical">
										<label id="cbActive" for="checkboxActive"><fmt:message key="global.active" /></label>
										<br />
										<span class="checkbox-item"><fmt:message key="global.off" /></span>
										<label class="switch switch-update">
											<input id="checkboxActive" name="checkboxActive" type="checkbox" checked>
											<span class="slider"></span>
										</label>
										<span class="checkbox-item"><fmt:message key="global.on" /></span>
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
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
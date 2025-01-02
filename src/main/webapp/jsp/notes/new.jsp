<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String today = (String) view.getValueForKey("today");
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="notes.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="<%=path%>/css/notes/notes.css" rel="stylesheet" type="text/css" />
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
								<i class="fas fa-edit fa-fw"></i> <fmt:message key="notes.add.header" />
								<div class="pull-right">
									<button type="submit" form="newNoteForm" class="btn btn-success btn-xs btn-fixed"><fmt:message key="global.save" /></button>
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/notes"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<form id="newNoteForm" action="notes" method="post">
									<div class="col-xs-12 marged-top">
										<label for="selectDateTime"><fmt:message key="global.date" /></label><br />
										<input id="selectDateTime" type="datetime-local" name="dateTime" value="<%=today%>">
									</div>
									<div class="col-xs-12 marged-top">
										<label for="textTitle"><fmt:message key="global.title" /></label><br />
										<input id="textTitle" name="title" type="text" pattern=".{1,100}" title="<fmt:message key="help.1.100" />" autofocus required>
									</div>
									<div class="col-xs-12 spaced-vertical">
										<label for="areaComment"><fmt:message key="global.message" /></label>
										<div id="areaNicEdit" class="scroll">
											<textarea id="areaComment" name="message" maxlength="5000" rows="10"></textarea>
										</div>
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
	<!-- NicEdit (RichTextBox) JavaScript -->
	<script src="<%=path%>/js/plugins/nicEdit.js"></script>
	<!-- Form Notes JavaScript -->
	<script src="<%=path%>/js/notes/notes.js"></script>
    <!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
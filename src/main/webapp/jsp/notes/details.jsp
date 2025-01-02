<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="fr.my.home.bean.Note"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	Note note = (Note) view.getValueForKey("note");
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
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
								<i class="fas fa-edit fa-fw"></i> <fmt:message key="notes.details.header" />
								<div class="pull-right">
									<button type="button" class="btn btn-danger btn-xs btn-fixed" data-toggle="modal" data-target=".modal-confirm" data-backdrop="static"><fmt:message key="global.delete" /></button>
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/notes"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body no-padding">
								<table class="table table-striped">
									<tr>
										<th class="col-xs-3"><fmt:message key="global.date" /> - <fmt:message key="global.time" /></th>
										<td class="col-xs-9"><%=formatter.format(note.getSaveDate())%></td>
									</tr>
									<tr>
										<th><fmt:message key="global.title" /></th>
										<td class="note-title"><%=note.getTitle()%></td>
									</tr>
									<tr>
										<th><fmt:message key="global.message" /></th>
										<td></td>
									</tr>
								</table>
								<div class="message-box center scroll"><%=note.getMessage()%></div>
								<!-- modal : confirmation suppression -->
								<div class="modal fade modal-confirm" tabindex="-1" role="dialog" aria-labelledby="modal_label">
									<div class="modal-dialog modal-sm" role="document">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal" aria-label="Close">
													<span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span>
												</button>
												<h4 class="modal-title" id="modal_label"><fmt:message key="global.confirm" /></h4>
											</div>
											<div class="modal-body">
												<p><fmt:message key="notes.delete" /></p>
											</div>
											<div class="modal-footer center">
												<a href="<%=path%>/notes?action=delete&id=<%=note.getId()%>" id="delModalBtn" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
												<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.cancel" /></button>
											</div>
										</div>
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
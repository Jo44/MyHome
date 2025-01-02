<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="fr.my.home.bean.Note"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	String success = (String) view.getValueForKey("success");
	int maxRows = (int) view.getValueForKey("maxRows");
	List<Note> listNote = (List<Note>) view.getValueForKey("listNote");
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
								<i class="fas fa-edit fa-fw"></i> <fmt:message key="notes.list.header" />
								<div class="pull-right">
									<select id="maxRows" onchange="setNewMaxRows('<%=path%>')">
										<option value="10"<% if (maxRows == 10) { out.print(" selected"); } %>>10</option>
										<option value="25"<% if (maxRows == 25) { out.print(" selected"); } %>>25</option>
										<option value="50"<% if (maxRows == 50) { out.print(" selected"); } %>>50</option>
										<option value="-1"<% if (maxRows == -1) { out.print(" selected"); } %>><fmt:message key="global.all" /></option>
									</select>
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/notes?action=add"><fmt:message key="global.add" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body no-padding">
								<%
									if (error != null) {
								%>
								<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification marged-top center" role="alert">
									<p><strong>Oops ! </strong><%=error%><i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
								</div>
								<%
									} else if (success != null) {
								%>
								<div id="alert-success" class="col-xs-offset-1 col-xs-10 alert alert-success panel-notification marged-top center" role="alert">
									<p><%=success%><i id="close-alert-success" class="fas fa-times-circle fa-fw close-button light-grey"></i></p>
								</div>
								<%
									}
									if (listNote == null || listNote.size() < 1) {
								%>
								<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
									<p><fmt:message key="notes.list.empty" /></p>
								</div>
								<%
									} else {
								%>
								<!-- Results Table -->
								<div class="col-xs-12 scroll no-padding">
									<table id="tablePagination" class="table table-striped">
										<thead>
											<tr>
												<th class="col-xs-3">
													<table class="margin-div">
														<tr>
															<td>
																<fmt:message key="global.date" />
															</td>
															<td>
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/notes?action=list&order-by=date&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/notes?action=list&order-by=date&dir=desc"></a>
															</td>
														</tr>
													</table>
												</th>
												<th class="col-xs-4">
													<table class="margin-div">
														<tr>
															<td>
																<fmt:message key="lbl.note.title" />
															</td>
															<td>
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/notes?action=list&order-by=title&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/notes?action=list&order-by=title&dir=desc"></a>
															</td>
														</tr>
													</table>
												</th>
												<th class="col-xs-5">
													<table class="margin-div">
														<tr>
															<td>
																<fmt:message key="lbl.note.message" />
															</td>
															<td>
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/notes?action=list&order-by=message&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/notes?action=list&order-by=message&dir=desc"></a>
															</td>
														</tr>
													</table>
												</th>
											</tr>
										</thead>
										<tbody>
										<%
											for (Note note : listNote) {
										%>
											<tr class="link-tab-list" onclick="window.location='<%=path%>/notes?action=details&id=<%=note.getId()%>';">
												<td>
													<a href="<%=path%>/notes?action=details&id=<%=note.getId()%>"><%=formatter.format(note.getSaveDate())%></a>
												</td>
												<td>
													<a href="<%=path%>/notes?action=details&id=<%=note.getId()%>"><%=note.getTitle()%></a>
												</td>
												<td>
													<%
														if (note.getMessage().isEmpty()) {
															out.print("<i class='fas fa-times fa-fw red'></i>");
														} else {
															out.print("<a href='" + path + "/notes?action=details&id=" + note.getId() + "'>" + note.getMessage() + "</a>");
														}
													%>
												</td>
											</tr>
										<%
											}
										%>
										</tbody>
									</table>
								</div>
								<!-- End Results Table -->
								<%
									}
								%>
							</div>
							<div class="panel-footer">
								<div id="navPagination" class="unselectable"></div>
								<img id="loadingImg" class="unselectable" src="<%=path%>/img/loading.svg" alt="loading .." />
							</div>
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
	<!-- Pagination JavaScript -->
	<script src="<%=path%>/js/plugins/pagination.js"></script>
	<!-- Max Rows {AJAX} JavaScript -->
    <script src="<%=path%>/js/users/max_rows.js"></script>
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
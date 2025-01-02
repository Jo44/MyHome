<%@ page import="java.util.*"%>
<%@ page import="fr.my.home.bean.Contact"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	String success = (String) view.getValueForKey("success");
	int maxRows = (int) view.getValueForKey("maxRows");
	List<Contact> listContact = (List<Contact>) view.getValueForKey("listContact");
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="contacts.list.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="<%=path%>/css/contacts/contacts.css" rel="stylesheet" type="text/css" />
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
						<div class="panel panel-large center-div">
							<div class="panel-heading">
								<i class="fas fa-address-book fa-fw"></i> <fmt:message key="contacts.list.header" />
								<div class="pull-right">
									<select id="maxRows" onchange="setNewMaxRows('<%=path%>')">
										<option value="10"<% if (maxRows == 10) { out.print(" selected"); } %>>10</option>
										<option value="25"<% if (maxRows == 25) { out.print(" selected"); } %>>25</option>
										<option value="50"<% if (maxRows == 50) { out.print(" selected"); } %>>50</option>
										<option value="-1"<% if (maxRows == -1) { out.print(" selected"); } %>><fmt:message key="global.all" /></option>
									</select>
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/contacts?action=add"><fmt:message key="global.add" /></a>
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
									if (listContact == null || listContact.size() < 1) {
								%>
								<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
									<p><fmt:message key="contacts.list.empty" /></p>
								</div>
								<%
									} else {
								%>
								<!-- Results Table -->
								<div class="col-xs-12 scroll no-padding">
									<table id="tablePagination" class="table table-striped">
										<thead>
											<tr>
												<th class="col-xs-2">
													<table class="margin-div">
														<tr>
															<td>
																<fmt:message key="lbl.contact.firstname" />
															</td>
															<td>
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/contacts?action=list&order-by=firstname&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/contacts?action=list&order-by=firstname&dir=desc"></a>
															</td>
														</tr>
													</table>
												</th>
												<th class="col-xs-2">
													<table class="margin-div">
														<tr>
															<td>
																<fmt:message key="lbl.contact.lastname" />
															</td>
															<td>
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/contacts?action=list&order-by=lastname&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/contacts?action=list&order-by=lastname&dir=desc"></a>
															</td>
														</tr>
													</table>
												</th>
												<th class="col-xs-2">
													<table class="margin-div">
														<tr>
															<td>
																<fmt:message key="lbl.contact.email" />
															</td>
															<td>
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/contacts?action=list&order-by=email&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/contacts?action=list&order-by=email&dir=desc"></a>
															</td>
														</tr>
													</table>
												</th>
												<th class="col-xs-2">
													<table class="margin-div">
														<tr>
															<td>
																<fmt:message key="lbl.contact.phone" />
															</td>
															<td>
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/contacts?action=list&order-by=phone&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/contacts?action=list&order-by=phone&dir=desc"></a>
															</td>
														</tr>
													</table>
												</th>
												<th class="col-xs-2">
												<table class="margin-div">
														<tr>
															<td>
																<fmt:message key="lbl.contact.twitter" />
															</td>
															<td>
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/contacts?action=list&order-by=twitter&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/contacts?action=list&order-by=twitter&dir=desc"></a>
															</td>
														</tr>
													</table>
												</th>
												<th class="col-xs-2">
													<span class="label-table"><fmt:message key="lbl.actions" /></span>
												</th>
											</tr>
										</thead>
										<tbody>
											<%
												for (Contact contact : listContact) {
											%>
											<tr class="link-tab-list">
												<td>
													<%=contact.getFirstname()%>
												</td>
												<td>
													<%
														if (contact.getLastname().isEmpty()) {
															out.print("<i class='fas fa-times fa-fw red'></i>");
														} else {
															out.print(contact.getLastname());
														}
													%>
												</td>
												<td>
													<%
														if (contact.getEmail().isEmpty()) {
															out.print("<i class='fas fa-times fa-fw red'></i>");
														} else {
															out.print("<a href='mailto:" + contact.getEmail() + "' target='_blank'>" + contact.getEmail() + "</a>");
														}
													%>
												</td>
												<td>
													<%
														if (contact.getPhone().isEmpty()) {
															out.print("<i class='fas fa-times fa-fw red'></i>");
														} else {
															out.print("<table class='margin-div'><tr><td><i class='fas fa-mobile-alt fa-fw green'></i></td><td><a href='callto://" + contact.getPhone() + "' target='_blank'>" + contact.getPhone() + "</a></td></tr></table>");
														}
													%>
												</td>
												<td>
													<%
														if (contact.getTwitter().isEmpty()) {
															out.print("<i class='fas fa-times fa-fw red'></i>");
														} else {
															out.print("<table class='margin-div'><tr><td><i class='fab fa-twitter fa-fw blue twitter-marged'></i></td><td><a href='https://twitter.com/" + contact.getTwitter() + "' target='_blank'>" + contact.getTwitter() + "</a></td></tr></table>");
														}
													%>
												</td>
												<td>
													<table class="margin-div">
														<tr>
															<td>
																<a href="<%=path%>/contacts?action=details&id=<%=contact.getId()%>" class="btn btn-warning btn-xs actions-button small-marged"><i class="fas fa-edit fa-fw white"></i></a>
															</td>
															<td>
																<a class="btn btn-danger btn-xs actions-button small-marged" data-toggle="modal" data-target=".modal-confirm-<%=contact.getId()%>" data-backdrop="static"><i class="fas fa-trash-alt fa-fw white"></i></a>
															</td>
														</tr>
													</table>
													<div class="modal fade modal-confirm-<%=contact.getId()%>" tabindex="-1" role="dialog" aria-labelledby="modal_label_<%=contact.getId()%>">
														<div class="modal-dialog modal-sm" role="document">
															<div class="modal-content">
																<div class="modal-header">
																	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
																	<h4 id="modal_label_<%=contact.getId()%>" class="modal-title"><fmt:message key="global.confirm" /></h4>
																</div>
																<div class="modal-body">
																	<p><fmt:message key="contacts.delete" /></p>
																</div>
																<div class="modal-footer center">
																	<a href="<%=path%>/contacts?action=delete&id=<%=contact.getId()%>" id="delModalBtn_<%=contact.getId()%>" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
																	<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.cancel" /></button>
																</div>
															</div>
														</div>
													</div>
												</td>
											</tr>
											<%
												}
											%>
										</tbody>
									</table>
								</div>
								<!-- End Results Table -->
								<% } %>
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
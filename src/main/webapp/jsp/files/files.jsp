<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.CustomFile"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	String success = (String) view.getValueForKey("success");
	int maxRows = (int) view.getValueForKey("maxRows");
	List<CustomFile> listFile = (List<CustomFile>) view.getValueForKey("listFile");
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
	DecimalFormat formatDecimal = new DecimalFormat("#.00");
	long usedSpace = (long) view.getValueForKey("usedSpace");

	long progressValue;
	if (usedSpace <= 0) {
		progressValue = 0;
	} else if (usedSpace >= 5368709120L) {
		progressValue = 100;
	} else {
		progressValue = (usedSpace * 100) / 5368709120L;
	}

	String formatUsedSpace;
	if (usedSpace <= 0) {
		formatUsedSpace = "0 octet";
	} else if (usedSpace == 1) {
		formatUsedSpace = "1 octet";
	} else if (usedSpace < 1024) {
		formatUsedSpace = String.valueOf(usedSpace) + " octets";
	} else if (usedSpace < (1024 * 1024)) {
		formatUsedSpace = String.valueOf(formatDecimal.format((double) usedSpace / 1024)) + " ko";
	} else if (usedSpace < (1024 * 1024 * 1024)) {
		formatUsedSpace = String.valueOf(formatDecimal.format((double) usedSpace / (1024 * 1024))) + " Mo";
	} else {
		formatUsedSpace = String.valueOf(formatDecimal.format((double) usedSpace / (1024 * 1024 * 1024))) + " Go";
	}
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="files.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- Tooltips CSS -->
<link href="<%=path%>/css/plugins/tooltip.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="<%=path%>/css/files/files.css" rel="stylesheet" type="text/css" />
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
						<div class="panel panel-default panel-top center-div">
							<div class="panel-heading">
								<i class="fas fa-file-import fa-fw"></i> <fmt:message key="files.add.header" />
								<div class="pull-right">
									<button type="submit" form="newFileForm" id="sendFilesButton" class="btn btn-success btn-xs btn-fixed" disabled><fmt:message key="global.add" /></button>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body unselectable">
								<div class="col-xs-12 col-sm-offset-1 col-sm-10 big-spaced-vertical">
									<div class="center-div">
										<p><fmt:message key="files.add.used.space" /><br /><span class="bold"><%=formatUsedSpace%></span> <fmt:message key="files.add.on" /> <span class="bold">5 Go</span></p>
									</div>
									<div class="progress marged-top" title="<%=formatUsedSpace%> sur 5 Go">
										<div class="progress-bar
										<%
											if (progressValue >= 70 && progressValue < 90) {
												out.print(" progress-bar-warning");
											} else if (progressValue >= 90) {
												out.print(" progress-bar-alert");
											}
										%>
										" role="progressbar" aria-valuenow="<%=progressValue%>" aria-valuemin="0" aria-valuemax="100" style="width: <%=progressValue%>%;">
										<%
											if (progressValue > 20) {
										%>
											<span class="progress-bar-value"><%=progressValue%> %</span>
										<%
											}
										%>
										</div>
										<%
											if (progressValue <= 20) {
										%>
										<span class="progress-bar-value-inverted"><%=progressValue%> %</span>										
										<%
											}
										%>
									</div>
									<hr>
									<form id="newFileForm" action="files" method="post" enctype="multipart/form-data">
										<label for="fileInput" class="label-file"><fmt:message key="files.add.choose.files" /></label>
										<p class="center bold"><fmt:message key="files.add.limit" /></p>
										<p id="selectedFiles"></p>
										<input id="fileInput" type="file" name="file" multiple />
										<noscript class="col-xs-12 big-marged-top"><fmt:message key="javascript.files.add" /></noscript>
									</form>
									<%
										if (error != null) {
									%>
									<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification big-marged-top center" role="alert">
										<p><strong>Oops ! </strong><%=error%><i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
									</div>
									<%
										} else if (success != null) {
									%>
									<div id="alert-success" class="col-xs-offset-1 col-xs-10 alert alert-success panel-notification big-marged-top center" role="alert">
										<p><%=success%><i id="close-alert-success" class="fas fa-times-circle fa-fw close-button light-grey"></i></p>
									</div>
									<%
										}
									%>
								</div>
							</div>
							<div class="panel-footer"></div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-md-offset-1 col-md-10">
						<div class="panel panel-default panel-top center-div">
							<div class="panel-heading">
								<i class="fas fa-copy fa-fw"></i> <fmt:message key="files.list.header" />
								<div class="pull-right">
									<select id="maxRows" onchange="setNewMaxRows('<%=path%>')">
										<option value="10"<% if (maxRows == 10) { out.print(" selected"); } %>>10</option>
										<option value="25"<% if (maxRows == 25) { out.print(" selected"); } %>>25</option>
										<option value="50"<% if (maxRows == 50) { out.print(" selected"); } %>>50</option>
										<option value="-1"<% if (maxRows == -1) { out.print(" selected"); } %>><fmt:message key="global.all" /></option>
									</select>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body no-padding">
								<%
									if (listFile == null || listFile.size() < 1) {
								%>
								<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
									<p><fmt:message key="files.list.empty" /></p>
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
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/files?action=list&order-by=date&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/files?action=list&order-by=date&dir=desc"></a>
															</td>
														</tr>
													</table>
												</th>
												<th class="col-xs-2">
													<table class="margin-div">
														<tr>
															<td>
																<fmt:message key="lbl.file.weight" />
															</td>
															<td>
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/files?action=list&order-by=weight&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/files?action=list&order-by=weight&dir=desc"></a>
															</td>
														</tr>
													</table>
												</th>
												<th class="col-xs-5">
													<table class="margin-div">
														<tr>
															<td>
																<fmt:message key="lbl.file.name" />
															</td>
															<td>
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/files?action=list&order-by=name&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/files?action=list&order-by=name&dir=desc"></a>
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
												for (CustomFile file : listFile) {
											%>
											<tr class="link-tab-list">
												<td>
													<%=formatter.format(file.getUploadDate())%>
												</td>
												<td>
													<%
														long weight = file.getWeight();
														if (weight <= 1) {
															out.print(String.valueOf(weight) + " octet");
														} else if (weight < 1024) {
															out.print(String.valueOf(weight) + " octets");
														} else if (weight < (1024 * 1024)) {
															out.print(String.valueOf(formatDecimal.format((double) weight / 1024)) + " ko");
														} else if (weight < (1024 * 1024 * 1024)) {
															out.print(String.valueOf(formatDecimal.format((double) weight / (1024 * 1024))) + " Mo");
														} else {
															out.print(String.valueOf(formatDecimal.format((double) weight / (1024 * 1024 * 1024))) + " Go");
														}
													%>
												</td>
												<td>
													<%=file.getName()%>
												</td>
												<td>
													<table class="margin-div">
														<tr>
															<td>
																<a href="<%=path%>/files?action=get&id=<%=file.getId()%>" class="btn btn-primary btn-xs actions-button small-marged"><i class="fas fa-download fa-fw white"></i></a>
															</td>
															<td>
																<a class="btn btn-danger btn-xs actions-button small-marged" data-toggle="modal" data-target=".modal-confirm-<%=file.getId()%>" data-backdrop="static"><i class="fas fa-trash-alt fa-fw white"></i></a>
															</td>
														</tr>
													</table>
													<div class="modal fade modal-confirm-<%=file.getId()%>" tabindex="-1" role="dialog" aria-labelledby="modal_label_<%=file.getId()%>">
														<div class="modal-dialog modal-sm" role="document">
															<div class="modal-content">
																<div class="modal-header">
																	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
																	<h4 id="modal_label_<%=file.getId()%>" class="modal-title"><fmt:message key="global.confirm" /></h4>
																</div>
																<div class="modal-body">
																	<p><fmt:message key="files.delete" /></p>
																</div>
																<div class="modal-footer center">
																	<a href="<%=path%>/files?action=delete&id=<%=file.getId()%>" id="delModalBtn_<%=file.getId()%>" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
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
	<!-- Form Files JavaScript -->
	<script src="<%=path%>/js/files/files.js"></script>
	<!-- Max Rows {AJAX} JavaScript -->
    <script src="<%=path%>/js/users/max_rows.js"></script>
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="contacts.list.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/contacts/contacts.css" rel="stylesheet" type="text/css" />
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
									<!-- Max Rows -->
									<select id="maxRows" onchange="setNewMaxRows('${pageContext.request.contextPath}')">
										<c:choose>
											<c:when test="${requestScope.maxRows == 10}">
												<option value="10" selected>10</option>
												<option value="25">25</option>
												<option value="50">50</option>
												<option value="-1"><fmt:message key="global.all" /></option>
											</c:when>
											<c:when test="${requestScope.maxRows == 25}">
												<option value="10">10</option>
												<option value="25" selected>25</option>
												<option value="50">50</option>
												<option value="-1"><fmt:message key="global.all" /></option>
											</c:when>
											<c:when test="${requestScope.maxRows == 50}">
												<option value="10">10</option>
												<option value="25">25</option>
												<option value="50" selected>50</option>
												<option value="-1"><fmt:message key="global.all" /></option>
											</c:when>
											<c:when test="${requestScope.maxRows == -1}">
												<option value="10">10</option>
												<option value="25">25</option>
												<option value="50">50</option>
												<option value="-1" selected><fmt:message key="global.all" /></option>
											</c:when>
											<c:otherwise>
												<option value="10">10</option>
												<option value="25">25</option>
												<option value="50">50</option>
												<option value="-1"><fmt:message key="global.all" /></option>
											</c:otherwise>
										</c:choose>
									</select>
									<!-- End Max Rows -->
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/contacts?action=add"><fmt:message key="global.add" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body no-padding">
								<!-- Error / Success -->
								<c:choose>
									<c:when test="${not empty requestScope.error}">
										<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification marged-top center" role="alert">
											<p><strong>Oops ! </strong>${requestScope.error}<i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
										</div>
									</c:when>
									<c:when test="${not empty requestScope.success}">
										<div id="alert-success" class="col-xs-offset-1 col-xs-10 alert alert-success panel-notification marged-top center" role="alert">
											<p>${requestScope.success}<i id="close-alert-success" class="fas fa-times-circle fa-fw close-button light-grey"></i></p>
										</div>
									</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
								<!-- End Error / Success -->
								<c:choose>
									<c:when test="${empty requestScope.listContact}">
										<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="contacts.list.empty" /></p>
										</div>
									</c:when>
									<c:otherwise>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/contacts?action=list&order-by=firstname&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/contacts?action=list&order-by=firstname&dir=desc"></a>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/contacts?action=list&order-by=lastname&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/contacts?action=list&order-by=lastname&dir=desc"></a>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/contacts?action=list&order-by=email&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/contacts?action=list&order-by=email&dir=desc"></a>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/contacts?action=list&order-by=phone&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/contacts?action=list&order-by=phone&dir=desc"></a>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/contacts?action=list&order-by=twitter&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/contacts?action=list&order-by=twitter&dir=desc"></a>
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
													<!-- List Contacts -->
													<c:forEach items="${requestScope.listContact}" var="contact">
														<tr class="link-tab-list">
															<td>
																${contact.firstname}
															</td>
															<td>
																<c:choose>
																	<c:when test="${empty contact.lastname}">
																		<i class='fas fa-times fa-fw red'></i>
																	</c:when>
																	<c:otherwise>
																		${contact.lastname}
																	</c:otherwise>
																</c:choose>
															</td>
															<td>
																<c:choose>
																	<c:when test="${empty contact.email}">
																		<i class='fas fa-times fa-fw red'></i>
																	</c:when>
																	<c:otherwise>
																		<a href='mailto:${contact.email}' target='_blank'>${contact.email}</a>
																	</c:otherwise>
																</c:choose>
															</td>
															<td>
																<c:choose>
																	<c:when test="${empty contact.phone}">
																		<i class='fas fa-times fa-fw red'></i>
																	</c:when>
																	<c:otherwise>
																		<table class='margin-div'>
																			<tr>
																				<td>
																					<i class='fas fa-mobile-alt fa-fw green'></i>
																				</td>
																				<td>
																					<a href='callto://${contact.phone}' target='_blank'>${contact.phone}</a>
																				</td>
																			</tr>
																		</table>
																	</c:otherwise>
																</c:choose>
															</td>
															<td>
																<c:choose>
																	<c:when test="${empty contact.twitter}">
																		<i class='fas fa-times fa-fw red'></i>
																	</c:when>
																	<c:otherwise>
																		<table class='margin-div'>
																			<tr>
																				<td>
																					<i class='fab fa-twitter fa-fw blue twitter-marged'></i>
																				</td>
																				<td>
																					<a href='https://x.com/${contact.twitter}' target='_blank'>${contact.twitter}</a>
																				</td>
																			</tr>
																		</table>
																	</c:otherwise>
																</c:choose>
															</td>
															<td>
																<table class="margin-div">
																	<tr>
																		<td>
																			<a href="${pageContext.request.contextPath}/contacts?action=details&id=${contact.id}" class="btn btn-warning btn-xs actions-button small-marged"><i class="fas fa-edit fa-fw white"></i></a>
																		</td>
																		<td>
																			<a class="btn btn-danger btn-xs actions-button small-marged" data-toggle="modal" data-target=".modal-confirm-${contact.id}" data-backdrop="static"><i class="fas fa-trash-alt fa-fw white"></i></a>
																		</td>
																	</tr>
																</table>
																<!-- Confirm Delete Modal -->
																<div class="modal fade modal-confirm-${contact.id}" tabindex="-1" role="dialog" aria-labelledby="modal_label_${contact.id}">
																	<div class="modal-dialog modal-sm" role="document">
																		<div class="modal-content">
																			<div class="modal-header">
																				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
																				<h4 id="modal_label_${contact.id}" class="modal-title"><fmt:message key="global.confirm" /></h4>
																			</div>
																			<div class="modal-body">
																				<p><fmt:message key="contacts.delete" /></p>
																			</div>
																			<div class="modal-footer center">
																				<a href="${pageContext.request.contextPath}/contacts?action=delete&id=${contact.id}" id="delModalBtn_${contact.id}" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
																				<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.cancel" /></button>
																			</div>
																		</div>
																	</div>
																</div>
																<!-- End Confirm Delete Modal -->
															</td>
														</tr>
													</c:forEach>
													<!-- End List Contacts -->
												</tbody>
											</table>
										</div>
									<!-- End Results Table -->
									</c:otherwise>
								</c:choose>
							</div>
							<div class="panel-footer">
								<div id="navPagination" class="unselectable"></div>
								<img id="loadingImg" class="unselectable" src="${pageContext.request.contextPath}/img/loading.svg" alt="loading .." />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- jQuery -->
	<script src="${pageContext.request.contextPath}/js/plugins/jquery.js"></script>
	<!-- Bootstrap JavaScript -->
	<script src="${pageContext.request.contextPath}/js/plugins/bootstrap.js"></script>
	<!-- Font Awesome JavaScript -->
	<script src="https://kit.fontawesome.com/3010c2773a.js" crossorigin="anonymous"></script>
	<!-- Pagination JavaScript -->
	<script src="${pageContext.request.contextPath}/js/plugins/pagination.js"></script>
	<!-- Max Rows {AJAX} JavaScript -->
    <script src="${pageContext.request.contextPath}/js/users/max_rows.js"></script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
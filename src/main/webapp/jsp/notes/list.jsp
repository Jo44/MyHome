<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="notes.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/notes/notes.css" rel="stylesheet" type="text/css" />
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
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/notes?action=add"><fmt:message key="global.add" /></a>
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
									<c:when test="${empty requestScope.listNote}">
										<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="notes.list.empty" /></p>
										</div>
									</c:when>
									<c:otherwise>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/notes?action=list&order-by=date&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/notes?action=list&order-by=date&dir=desc"></a>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/notes?action=list&order-by=title&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/notes?action=list&order-by=title&dir=desc"></a>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/notes?action=list&order-by=message&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/notes?action=list&order-by=message&dir=desc"></a>
																	</td>
																</tr>
															</table>
														</th>
													</tr>
												</thead>
												<tbody>
													<!-- List Notes -->
													<c:forEach items="${requestScope.listNote}" var="note">
														<tr class="link-tab-list" onclick="window.location='${pageContext.request.contextPath}/notes?action=details&id=${note.id}';">
															<td>
																<a href="${pageContext.request.contextPath}/notes?action=details&id=${note.id}"><fmt:formatDate value="${note.saveDate}" pattern="${requestScope.formatterDate.toPattern()}" /></a>
															</td>
															<td>
																<a href="${pageContext.request.contextPath}/notes?action=details&id=${note.id}">${note.title}</a>
															</td>
															<td>
																<c:choose>
																	<c:when test="${empty note.message}">
																		<i class='fas fa-times fa-fw red'></i>
																	</c:when>
																	<c:otherwise>
																		<a href="${pageContext.request.contextPath}/notes?action=details&id=${note.id}">${note.message}</a>
																	</c:otherwise>
																</c:choose>
															</td>
														</tr>
													</c:forEach>
													<!-- End List Notes -->
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
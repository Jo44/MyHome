<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="files.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- Tooltips CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/tooltip.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/files/files.css" rel="stylesheet" type="text/css" />
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
										<p><fmt:message key="files.add.used.space" /></p>
										<p class="marged-top"><span class="bold">${requestScope.formattedUsedSpace}</span> <fmt:message key="files.add.on" /> <span class="bold">5 Go</span></p>
									</div>
									<!-- Progress Bar -->
									<div class="progress marged-top" title="${requestScope.formattedUsedSpace} sur 5 Go">
										<c:choose>
											<c:when test="${requestScope.progressValue >= 90}">
												<div class="progress-bar progress-bar-alert" role="progressbar" aria-valuenow="${requestScope.progressValue}" aria-valuemin="0" aria-valuemax="100" style="width: ${requestScope.progressValue}%;">
													<span class="progress-bar-value">${requestScope.progressValue} %</span>
												</div>
											</c:when>
											<c:when test="${requestScope.progressValue >= 70 && requestScope.progressValue < 90}">
												<div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="${requestScope.progressValue}" aria-valuemin="0" aria-valuemax="100" style="width: ${requestScope.progressValue}%;">
													<span class="progress-bar-value">${requestScope.progressValue} %</span>
												</div>
											</c:when>
											<c:when test="${requestScope.progressValue >= 50 && requestScope.progressValue < 70}">
												<div class="progress-bar" role="progressbar" aria-valuenow="${requestScope.progressValue}" aria-valuemin="0" aria-valuemax="100" style="width: ${requestScope.progressValue}%;">
													<span class="progress-bar-value">${requestScope.progressValue} %</span>
												</div>
											</c:when>
											<c:otherwise>
												<div class="progress-bar" role="progressbar" aria-valuenow="${requestScope.progressValue}" aria-valuemin="0" aria-valuemax="100" style="width: ${requestScope.progressValue}%;"></div>
												<span class="progress-bar-value-inverted">${requestScope.progressValue} %</span>
											</c:otherwise>
										</c:choose>
									</div>
									<!-- End Progress Bar -->
									<hr>
									<form id="newFileForm" action="files" method="post" enctype="multipart/form-data">
										<label for="fileInput" class="label-file"><fmt:message key="files.add.choose.files" /></label>
										<p class="marged-top center bold"><fmt:message key="files.add.limit" /></p>
										<p id="selectedFiles"></p>
										<input id="fileInput" type="file" name="file" multiple />
										<noscript class="col-xs-12 big-marged-top"><fmt:message key="javascript.files.add" /></noscript>
									</form>
									<!-- Error / Success -->
									<c:choose>
										<c:when test="${not empty requestScope.error}">
											<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification big-marged-top center" role="alert">
												<p><strong>Oops ! </strong>${requestScope.error}<i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
											</div>
										</c:when>
										<c:when test="${not empty requestScope.success}">
											<div id="alert-success" class="col-xs-offset-1 col-xs-10 alert alert-success panel-notification big-marged-top center" role="alert">
												<p>${requestScope.success}<i id="close-alert-success" class="fas fa-times-circle fa-fw close-button light-grey"></i></p>
											</div>
										</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
									<!-- End Error / Success -->
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
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body no-padding">
								<c:choose>
									<c:when test="${empty requestScope.listFile}">
										<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="files.list.empty" /></p>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/files?action=list&order-by=date&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/files?action=list&order-by=date&dir=desc"></a>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/files?action=list&order-by=weight&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/files?action=list&order-by=weight&dir=desc"></a>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/files?action=list&order-by=name&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/files?action=list&order-by=name&dir=desc"></a>
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
													<!-- List Files -->
													<c:forEach items="${requestScope.listFile}" var="file">
														<tr class="link-tab-list">
															<td>
																<fmt:formatDate value="${file.uploadDate}" pattern="${requestScope.formatterDate.toPattern()}" />
															</td>
															<td>
																${file.formattedWeight}
															</td>
															<td>
																${file.name}
															</td>
															<td>
																<table class="margin-div">
																	<tr>
																		<td>
																			<a href="${pageContext.request.contextPath}/files?action=get&id=${file.id}" class="btn btn-primary btn-xs actions-button small-marged"><i class="fas fa-download fa-fw white"></i></a>
																		</td>
																		<td>
																			<a class="btn btn-danger btn-xs actions-button small-marged" data-toggle="modal" data-target=".modal-confirm-${file.id}" data-backdrop="static"><i class="fas fa-trash-alt fa-fw white"></i></a>
																		</td>
																	</tr>
																</table>
																<!-- Confirm Delete Modal -->
																<div class="modal fade modal-confirm-${file.id}" tabindex="-1" role="dialog" aria-labelledby="modal_label_${file.id}">
																	<div class="modal-dialog modal-sm" role="document">
																		<div class="modal-content">
																			<div class="modal-header">
																				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
																				<h4 id="modal_label_${file.id}" class="modal-title"><fmt:message key="global.confirm" /></h4>
																			</div>
																			<div class="modal-body">
																				<p><fmt:message key="files.delete" /></p>
																			</div>
																			<div class="modal-footer center">
																				<a href="${pageContext.request.contextPath}/files?action=delete&id=${file.id}" id="delModalBtn_${file.id}" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
																				<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.cancel" /></button>
																			</div>
																		</div>
																	</div>
																</div>
																<!-- End Confirm Delete Modal -->
															</td>
														</tr>
													</c:forEach>
													<!-- End List Files -->
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
	<!-- Form Files JavaScript -->
	<script src="${pageContext.request.contextPath}/js/files/files.js"></script>
	<!-- Max Rows {AJAX} JavaScript -->
    <script src="${pageContext.request.contextPath}/js/users/max_rows.js"></script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
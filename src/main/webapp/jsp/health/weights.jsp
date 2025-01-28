<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="health.weight.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/health/health.css" rel="stylesheet" type="text/css" />
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
								<i class="fas fa-plus-circle fa-fw"></i> <fmt:message key="health.weight.add.header" />
								<div class="pull-right">
									<button type="submit" form="newWeightForm" class="btn btn-success btn-xs btn-fixed"><fmt:message key="global.add" /></button>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<!-- Form Add -->
								<form id="newWeightForm" action="weights" method="post">
									<div class="col-xs-12 col-sm-6 big-marged-top">
										<label for="selectDateTime"><fmt:message key="global.date" /></label><br />
										<input id="selectDateTime" type="datetime-local" name="dateTime" value="${requestScope.today}">
									</div>
									<div class="col-xs-12 col-sm-6 big-spaced-vertical">
										<label for="numberWeight"><fmt:message key="health.weight.add.title" /></label><br />
										<input id="numberWeight" name="weightValue" type="number" min="1" max="500" step="0.1" title="<fmt:message key="help.1.500" />" autofocus required>
										<fmt:message key="health.weight.add.unit" />
									</div>
								</form>
								<!-- End Form Add -->
							</div>
							<div class="panel-footer"></div>
						</div>
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fas fa-chart-area fa-fw"></i> <fmt:message key="health.weight.chart.header" />
								<div class="pull-right">
									<button id="applyBtn" class="btn btn-primary btn-xs btn-fixed" type="button" onclick="updatePeriod()"><fmt:message key="global.apply" /></button>
								</div>
								<div class="fixHeight"></div>
								<div id="fromToBloc" class="pull-right">
									<!-- Period Filter -->
									<span><fmt:message key="health.weight.chart.from" /></span>
									<input id="selectFromDateTime" type="datetime-local" name="fromDateTime" value="${requestScope.from}">
									<span id="toDateTimeLabel"><fmt:message key="health.weight.chart.to" /></span>
									<input id="selectToDateTime" type="datetime-local" name="toDateTime" value="${requestScope.to}">
									<!-- End Period Filter -->
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<!-- Weight Chart -->
								<c:choose>
									<c:when test="${empty requestScope.listWeight}">
										<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="health.weight.list.empty" /></p>
										</div>
									</c:when>
									<c:otherwise>
										<canvas id="weightChart"></canvas>
									</c:otherwise>
								</c:choose>
								<!-- End Weight Chart -->
							</div>
							<div class="panel-footer"></div>
						</div>
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fas fa-weight fa-fw"></i> <fmt:message key="health.weight.header" />
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
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/health"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
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
									<c:when test="${empty requestScope.listWeight}">
										<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="health.weight.list.empty" /></p>
										</div>
									</c:when>
									<c:otherwise>
										<!-- Results Table -->
										<div class="col-xs-12 scroll no-padding">
											<table id="tablePagination" class="table table-striped">
												<thead>
													<tr>
														<th class="col-xs-5">
															<table class="margin-div">
																<tr>
																	<td>
																		<fmt:message key="global.date" />
																	</td>
																	<td>
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/health/weights?action=list&order-by=date&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/health/weights?action=list&order-by=date&dir=desc"></a>
																	</td>
																</tr>
															</table>
														</th>
														<th class="col-xs-5">
															<table class="margin-div">
																<tr>
																	<td>
																		<fmt:message key="lbl.health.weight" />
																	</td>
																	<td>
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/health/weights?action=list&order-by=value&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/health/weights?action=list&order-by=value&dir=desc"></a>
																	</td>
																</tr>
															</table>
														</th>
														<th class="col-xs-2">
															<span><fmt:message key="lbl.actions" /></span>
														</th>
													</tr>
												</thead>
												<tbody>
													<!-- List Weights -->
													<c:forEach items="${requestScope.listWeight}" var="weight">
														<tr class="link-tab-list">
															<td>
																<fmt:formatDate value="${weight.saveDate}" pattern="${requestScope.formatterDate.toPattern()}" />
															</td>
															<td>
																${weight.value} kg
															</td>
															<td>
																<a class="btn btn-danger btn-xs actions-button small-marged" data-toggle="modal" data-target=".modal-confirm-${weight.id}" data-backdrop="static"><i class="fas fa-trash-alt fa-fw white"></i></a>
																<!-- Confirm Delete Modal -->
																<div class="modal fade modal-confirm-${weight.id}" tabindex="-1" role="dialog" aria-labelledby="modal_label_${weight.id}">
																	<div class="modal-dialog modal-sm" role="document">
																		<div class="modal-content">
																			<div class="modal-header">
																				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
																				<h4 id="modal_label_${weight.id}" class="modal-title"><fmt:message key="global.confirm" /></h4>
																			</div>
																			<div class="modal-body">
																				<p><fmt:message key="health.weight.delete" /></p>
																			</div>
																			<div class="modal-footer center">
																				<a href="${pageContext.request.contextPath}/health/weights?action=delete&id=${weight.id}" id="delModalBtn_${weight.id}" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
																				<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.cancel" /></button>
																			</div>
																		</div>
																	</div>
																</div>
																<!-- End Confirm Delete Modal -->
															</td>
														</tr>
													</c:forEach>
													<!-- End List Weights -->
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
	<!-- Chart JavaScript -->
	<script type="text/javascript">
		var path = "${pageContext.request.contextPath}";
		var data_date = [
		    <c:if test="${not empty requestScope.jsListWeight}">
		    	<c:forEach items="${requestScope.jsListWeight}" var="weight">
		    		'${weight.saveDateJS}',
		    	</c:forEach>
		    </c:if>
		    ];
		var data_weight = [
		    <c:if test="${not empty requestScope.jsListWeight}">
		    	<c:forEach items="${requestScope.jsListWeight}" var="weight">
		    		${weight.value},
		    	</c:forEach>
		    </c:if>
		    ];
	</script>
	<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.7/dist/chart.umd.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/moment@2.29.4/moment.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-moment@1.0.0"></script>
	<script src="${pageContext.request.contextPath}/js/health/weight/chart.js"></script>
	<!-- Pagination JavaScript -->
	<script src="${pageContext.request.contextPath}/js/plugins/pagination.js"></script>
	<!-- Max Rows {AJAX} JavaScript -->
    <script src="${pageContext.request.contextPath}/js/users/max_rows.js"></script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
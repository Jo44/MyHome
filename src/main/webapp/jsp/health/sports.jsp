<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="health.sport.page.title" /></title>
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
								<i class="fas fa-plus-circle fa-fw"></i>
								<fmt:message key="health.sport.activity.header" />
								<div class="pull-right">
									<button type="submit" form="newSportForm" class="btn btn-success btn-xs btn-fixed">
										<fmt:message key="global.add" />
									</button>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<!-- Form Add -->
								<form id="newSportForm" action="sports" method="post">
									<div class="col-xs-12 col-sm-6 big-marged-top">
										<label for="selectDateTime"><fmt:message key="global.date" /></label><br />
										<input id="selectDateTime" type="datetime-local" name="dateTime" value="${requestScope.today}">
									</div>
									<div class="col-xs-12 col-sm-6 big-spaced-vertical">
										<label for="textActivity"><fmt:message key="health.sport.activity" /></label><br />
										<input id="textActivity" name="activity" type="text" pattern=".{1,200}" title="<fmt:message key="help.1.200" />" autofocus required	/>
									</div>
								</form>
								<!-- End Form Add -->
							</div>
							<div class="panel-footer"></div>
						</div>
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="far fa-clock"></i>
								<fmt:message key="health.sport.timer.header" />
								<div class="pull-right">
									<i id="chronoDetailsArrow" class="fas fa-arrow-circle-down fa-fw" onclick="displayChronoDetails()"></i>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body unselectable">
								<!-- Chrono : Start / Stop buttons -->
								<div class="row">
									<div id="btnPanel">
										<button id="btnAction" class="btn btn-classic btn-sm btn-fixed" onclick="actionChrono()"><fmt:message key="health.sport.timer.btn.start" /></button>
										<button id="btnStop" class="btn btn-classic btn-sm btn-fixed" onclick="stopChrono()"><fmt:message key="health.sport.timer.btn.stop" /></button>
									</div>
								</div>
								<!-- Chrono : Display -->
								<div class="row">
									<div id="chronoPanel">
										<div id="chronoBckgnd">
											<div id="periodLeft">00:00:20</div>
											<div id="state"><fmt:message key="health.sport.timer.state.stop" /></div>
											<div class="row">
												<div class="col-xs-offset-1 col-xs-5">
													<div id="totalLeft">00:10:00</div>
													<div><fmt:message key="health.sport.timer.total.left" /></div>
												</div>
												<div class="col-xs-5">
													<div id="totalPast">00:00:00</div>
													<div><fmt:message key="health.sport.timer.total.past" /></div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- Chrono : Détails -->
								<div id="chronoDetails" class="row">
									<!-- Chrono : Configuration -->
									<div id="configPanel" class="col-xs-12 col-sm-4">
										<div id="configBckgnd">
											<div>
												<strong><fmt:message key="health.sport.timer.config" /></strong>
											</div>
											<div id="configForm">
												<div>
													<span><fmt:message key="health.sport.timer.config.volume" /></span>
												</div>
												<div>
													<input id="volume" type="range" name="volume" min="0.0" max="1.0" step="0.1" value="0.5">
												</div>
												<div>
													<span><fmt:message key="health.sport.timer.config.delay" /></span><input id="delay" type="number" name="delay" min="0" max="60" value="10" required /><span>sec</span>
												</div>
											</div>
										</div>
									</div>
									<!-- Chrono : Preset -->
									<div id="presetPanel" class="col-xs-12 col-sm-4">
										<div id="presetBckgnd">
											<div>
												<strong><fmt:message key="health.sport.timer.preset" /></strong>
											</div>
											<div id="presetForm">
												<div>
													<input id="presetTraining" type="radio" name="preset" onchange="presetTraining()" checked> <label for="presetTraining"><fmt:message key="health.sport.timer.preset.training" /></label>
												</div>
												<div>
													<input id="presetEndurance" type="radio" name="preset" onchange="presetEndurance()"> <label for="presetEndurance"><fmt:message key="health.sport.timer.preset.endurance" /></label>
												</div>
												<div>
													<input id="presetCustom" type="radio" name="preset" onchange="presetCustom()"> <label for="presetCustom"><fmt:message key="health.sport.timer.preset.custom" /></label>
												</div>
											</div>
										</div>
									</div>
									<!-- Chrono : Custom -->
									<div id="customPanel" class="col-xs-12 col-sm-4">
										<div id="customBckgnd">
											<div>
												<strong><fmt:message key="health.sport.timer.custom" /></strong>
											</div>
											<div id="customForm">
												<div>
													<span><fmt:message key="health.sport.timer.custom.effort" /></span><input id="effortPeriod" type="time" name="effortPeriod" min="00:00:00" max="23:59:59" value="00:00:20" step="1" onchange="changeCustom()" required disabled />
												</div>
												<div>
													<span><fmt:message key="health.sport.timer.custom.rest" /></span><input id="restPeriod" type="time" name="restPeriod" min="00:00:00" max="23:59:59" value="00:00:10" step="1" onchange="changeCustom()" required disabled />
												</div>
												<div>
													<span><fmt:message key="health.sport.timer.custom.repeat" /></span><input id="repeats" type="number" name="repeats" min="0" max="99" value="20" onchange="changeCustom()" required disabled /><span><fmt:message key="health.sport.timer.custom.times" /></span>
												</div>
											</div>
										</div>
									</div>
								</div>
								<noscript class="col-xs-12 spaced-vertical"><fmt:message key="javascript.chrono" /></noscript>
								<!-- End Chrono -->
							</div>
							<div class="panel-footer"></div>
						</div>
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fas fa-chart-area fa-fw"></i> <fmt:message key="health.sport.chart.header" />
								<div class="pull-right">
									<button id="applyBtn" class="btn btn-primary btn-xs btn-fixed" type="button" onclick="updatePeriod()"><fmt:message key="global.apply" /></button>
								</div>
								<div class="fixHeight"></div>
								<div id="fromToBloc" class="pull-right">
									<!-- Period Filter -->
									<span><fmt:message key="health.sport.chart.from" /></span>
									<input id="selectFromDateTime" type="datetime-local" name="fromDateTime" value="${requestScope.from}">
									<span id="toDateTimeLabel"><fmt:message key="health.sport.chart.to" /></span>
									<input id="selectToDateTime" type="datetime-local" name="toDateTime" value="${requestScope.to}">
									<!-- End Period Filter -->
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<!-- Sport Chart -->
								<c:choose>
									<c:when test="${empty requestScope.listSport}">
										<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="health.sport.list.empty" /></p>
										</div>
									</c:when>
									<c:otherwise>
										<canvas id="sportChart"></canvas>
									</c:otherwise>
								</c:choose>
								<!-- End Sport Chart -->
							</div>
							<div class="panel-footer"></div>
						</div>
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fas fa-running fa-fw"></i>
								<fmt:message key="health.sport.header" />
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
									<c:when test="${empty requestScope.listSport}">
										<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="health.sport.list.empty" /></p>
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
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/health/sports?action=list&order-by=date&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/health/sports?action=list&order-by=date&dir=desc"></a>
																	</td>
																</tr>
															</table>
														</th>
														<th class="col-xs-7">
															<table class="margin-div">
																<tr>
																	<td>
																		<fmt:message key="lbl.health.activity" />
																	</td>
																	<td>
																		<a class="fas fa-angle-up fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/health/sports?action=list&order-by=activity&dir=asc"></a><br />
																		<a class="fas fa-angle-down fa-sm order-arrow small-marged in-block" href="${pageContext.request.contextPath}/health/sports?action=list&order-by=activity&dir=desc"></a>
																	</td>
																</tr>
															</table>
														</th>
														<th class="col-xs-2"><span class="label-table"><fmt:message key="lbl.actions" /></span></th>
													</tr>
												</thead>
												<tbody>
													<!-- List Sports -->
													<c:forEach items="${requestScope.listSport}" var="sport">
														<tr class="link-tab-list">
															<td>
																<fmt:formatDate value="${sport.saveDate}" pattern="${requestScope.formatterDate.toPattern()}" />
															</td>
															<td>
																${sport.activity}
															</td>
															<td>
																<a class="btn btn-danger btn-xs actions-button small-marged" data-toggle="modal" data-target=".modal-confirm-${sport.id}" data-backdrop="static"><i class="fas fa-trash-alt fa-fw white"></i></a>
																<!-- Confirm Delete Modal -->
																<div class="modal fade modal-confirm-${sport.id}" tabindex="-1" role="dialog" aria-labelledby="modal_label_${sport.id}">
																	<div class="modal-dialog modal-sm" role="document">
																		<div class="modal-content">
																			<div class="modal-header">
																				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
																					<span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span>
																				</button>
																				<h4 id="modal_label_${sport.id}" class="modal-title">
																					<fmt:message key="global.confirm" />
																				</h4>
																			</div>
																			<div class="modal-body">
																				<p><fmt:message key="health.sport.delete" /></p>
																			</div>
																			<div class="modal-footer center">
																				<a href="${pageContext.request.contextPath}/health/sports?action=delete&id=${sport.id}" id="delModalBtn_${sport.id}" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
																				<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal">
																					<fmt:message key="global.cancel" />
																				</button>
																			</div>
																		</div>
																	</div>
																</div>
																<!-- End Confirm Delete Modal -->
															</td>
														</tr>
													</c:forEach>
													<!-- End List Sports -->
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
	<!-- Chrono Init JavaScript -->
	<script>
		const btnStart = '<fmt:message key="health.sport.timer.btn.start" />';
		const btnContinue = '<fmt:message key="health.sport.timer.btn.continue" />';
		const btnPause = '<fmt:message key="health.sport.timer.btn.pause" />';
		const stateWaiting = '<fmt:message key="health.sport.timer.state.waiting" />';
	    const stateRunning = '<fmt:message key="health.sport.timer.state.running" />';
		const stateRest = '<fmt:message key="health.sport.timer.state.rest" />';
	    const statePause = '<fmt:message key="health.sport.timer.state.pause" />';
		const stateStop = '<fmt:message key="health.sport.timer.state.stop" />';
	    const stateDone = '<fmt:message key="health.sport.timer.state.done" />';
	</script>
	<!-- Chrono Sport JavaScript -->
	<script src="${pageContext.request.contextPath}/js/health/sport/chrono.js"></script>
	<!-- Chart JavaScript -->
	<script src="${pageContext.request.contextPath}/js/health/sport/setup.js"></script>
	<script type="text/javascript">
		var path = "${pageContext.request.contextPath}";
		var raw_data_date = [
		    <c:if test="${not empty requestScope.jsListSport}">
		    	<c:forEach items="${requestScope.jsListSport}" var="sport">
		    		'${sport.saveDateJS}',
		    	</c:forEach>
		    </c:if>
		    ];
		const chartDatas = generateDailyDatas(raw_data_date);
		var data_date = chartDatas.dates;
		var data_sport = chartDatas.sport;
	</script>
	<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.7/dist/chart.umd.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/moment@2.29.4/moment.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-moment@1.0.0"></script>
	<script src="${pageContext.request.contextPath}/js/health/sport/chart.js"></script>
	<!-- Pagination JavaScript -->
	<script src="${pageContext.request.contextPath}/js/plugins/pagination.js"></script>
	<!-- Max Rows {AJAX} JavaScript -->
	<script src="${pageContext.request.contextPath}/js/users/max_rows.js"></script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
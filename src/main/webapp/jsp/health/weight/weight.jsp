<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.util.*"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%@ page import="fr.my.home.bean.Weight"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	String success = (String) view.getValueForKey("success");
	String today = (String) view.getValueForKey("today");
	String from = (String) view.getValueForKey("from");
	String to = (String) view.getValueForKey("to");
	int maxRows = (int) view.getValueForKey("maxRows");
	List<Weight> listWeight = (List<Weight>) view.getValueForKey("listWeight");
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
<title><fmt:message key="health.weight.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="<%=path%>/css/health/health.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div id="wrapper">
		<!-- Navigation Bar -->
		<%@include file="../../navbar.jspf"%>
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
								<form id="newWeightForm" action="weights" method="post">
									<div class="col-xs-12 col-sm-6 big-marged-top">
										<label for="selectDateTime"><fmt:message key="global.date" /></label><br />
										<input id="selectDateTime" type="datetime-local" name="dateTime" value="<%=today%>">
									</div>
									<div class="col-xs-12 col-sm-6 big-spaced-vertical">
										<label for="numberWeight"><fmt:message key="health.weight.add.title" /></label><br />
										<input id="numberWeight" name="weightValue" type="number" min="1" max="500" step="0.1" title="<fmt:message key="help.1.500" />" autofocus required>
										<fmt:message key="health.weight.add.unit" />
									</div>
								</form>
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
									<span><fmt:message key="health.weight.chart.from" /></span>
									<input id="selectFromDateTime" type="datetime-local" name="fromDateTime" value="<%=from%>">
									<span id="toDateTimeLabel"><fmt:message key="health.weight.chart.to" /></span>
									<input id="selectToDateTime" type="datetime-local" name="toDateTime" value="<%=to%>">
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<%
									if (listWeight != null && listWeight.size() > 0) {
								%>
								<canvas id="weightChart"></canvas>
								<%
									}
								%>
							</div>
							<div class="panel-footer"></div>
						</div>
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fas fa-weight fa-fw"></i> <fmt:message key="health.weight.header" />
								<div class="pull-right">
									<select id="maxRows" onchange="setNewMaxRows('<%=path%>')">
										<option value="10"<% if (maxRows == 10) { out.print(" selected"); } %>>10</option>
										<option value="25"<% if (maxRows == 25) { out.print(" selected"); } %>>25</option>
										<option value="50"<% if (maxRows == 50) { out.print(" selected"); } %>>50</option>
										<option value="-1"<% if (maxRows == -1) { out.print(" selected"); } %>><fmt:message key="global.all" /></option>
									</select>
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/health"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
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
									if (listWeight == null || listWeight.size() < 1) {
								%>
								<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
									<p><fmt:message key="health.weight.list.empty" /></p>
								</div>
								<%
									} else {
								%>
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
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/health/weights?action=list&order-by=date&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/health/weights?action=list&order-by=date&dir=desc"></a>
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
																<a class="fas fa-angle-up fa-fw order-arrow small-marged in-block" href="<%=path%>/health/weights?action=list&order-by=value&dir=asc"></a>
																<a class="fas fa-angle-down fa-fw order-arrow small-marged in-block" href="<%=path%>/health/weights?action=list&order-by=value&dir=desc"></a>
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
										<%
											for (Weight weight : listWeight) {
										%>
											<tr class="link-tab-list">
												<td>
													<a href=""><%=formatter.format(weight.getSaveDate())%></a>
												</td>
												<td>
													<a href=""><%=weight.getValue()%> kg</a>
												</td>
												<td>
													<a class="btn btn-danger btn-xs actions-button small-marged" data-toggle="modal" data-target=".modal-confirm-<%=weight.getId()%>" data-backdrop="static"><i class="fas fa-trash-alt fa-fw white"></i></a>
													<div class="modal fade modal-confirm-<%=weight.getId()%>" tabindex="-1" role="dialog" aria-labelledby="modal_label_<%=weight.getId()%>">
														<div class="modal-dialog modal-sm" role="document">
															<div class="modal-content">
																<div class="modal-header">
																	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
																	<h4 id="modal_label_<%=weight.getId()%>" class="modal-title"><fmt:message key="global.confirm" /></h4>
																</div>
																<div class="modal-body">
																	<p><fmt:message key="health.weight.delete" /></p>
																</div>
																<div class="modal-footer center">
																	<a href="<%=path%>/health/weights?action=delete&id=<%=weight.getId()%>" id="delModalBtn_<%=weight.getId()%>" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
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
	<!-- Chart JavaScript -->
	<script type="text/javascript">
		var data_date = [
		    <%
		    	if (listWeight != null && listWeight.size() > 0) {
		    		Collections.reverse(listWeight);
		    		for (Weight weight : listWeight) {
		    			out.print("'" + weight.getSaveDate().toLocalDateTime().toString() + "',");
			    	}
		    	}
		    %>
		    ];
		var data_weight = [
		    <%
		    	if (listWeight != null && listWeight.size() > 0) {
			    	for (Weight weight : listWeight) {
			    		out.print(weight.getValue() + ",");
			    	}
		    	}
		    %>
		    ];
	</script>
	<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.7/dist/chart.umd.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/moment@2.29.4/moment.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-moment@1.0.0"></script>
	<script src="<%=path%>/js/health/weight/chart.js"></script>
	<!-- Pagination JavaScript -->
	<script src="<%=path%>/js/plugins/pagination.js"></script>
	<!-- Max Rows {AJAX} JavaScript -->
    <script src="<%=path%>/js/users/max_rows.js"></script>
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
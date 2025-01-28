<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="yt.pl.list.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- Font Awesome CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/font-awesome.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/youtube/youtube.css" rel="stylesheet" type="text/css" />
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
								<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.pl.list.header" />
								<div class="pull-right">
									<!-- Result Count -->
									<span id="totalSearch"><fmt:message key="yt.pl.result.total" /> <span id="totalPlaylistCount">
										<c:choose>
											<c:when test="${not empty requestScope.listPlaylist}">
												<c:choose>
													<c:when test="${requestScope.listPlaylist.get(0).totalResults >= 10000}">
														<fmt:message key='yt.pl.result.more' /> <span>10000</span>
													</c:when>
													<c:otherwise>
														${requestScope.listPlaylist.get(0).totalResults}
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												0
											</c:otherwise>
										</c:choose>
									</span></span>
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/youtube_playlists?action=add"><fmt:message key="global.add" /></a>
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/youtube_player"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-heading">
								<div class="pull-right">
									<!-- YouTube Infos Modal -->
									<a id="ytInfos" class="btn btn-classic btn-xs" data-toggle="modal" data-target=".modal-infos" data-backdrop="static"><i class="fas fa-info-circle fa-fw"></i></a>
									<div class="modal fade modal-infos" tabindex="-1" role="dialog" aria-labelledby="modal_label_infos">
										<div class="modal-dialog" role="document">
											<div class="modal-content center">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
													<h4 id="modal_label_infos" class="modal-title"><fmt:message key="yt.global.infos.header" /></h4>
												</div>
												<div class="modal-body">
													<p><fmt:message key="yt.global.infos.msg" /></p>
												</div>
												<div class="modal-footer center">
													<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.close" /></button>
												</div>
											</div>
										</div>
									</div>
									<!-- Confirm Disconnect Modal -->
									<a class="btn btn-primary btn-xs spaced-pad-horizontal" data-toggle="modal" data-target=".modal-confirm-disconnect" data-backdrop="static">${requestScope.channelName}</a>
									<div class="modal fade modal-confirm-disconnect" tabindex="-1" role="dialog" aria-labelledby="modal_label_disconnect">
										<div class="modal-dialog modal-sm" role="document">
											<div class="modal-content">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
													<h4 id="modal_label_disconnect" class="modal-title"><fmt:message key="global.confirm" /></h4>
												</div>
												<div class="modal-body">
													<p><fmt:message key="yt.global.disconnect" /> ${requestScope.channelName} ?</p>
												</div>
												<div class="modal-footer center">
													<a href="${pageContext.request.contextPath}/youtube_logout" id="modal_btn_disconnect" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="yt.global.logout" /></a>
													<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.cancel" /></button>
												</div>
											</div>
										</div>
									</div>
									<!-- End Confirm Disconnect Modal -->
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
									<c:when test="${empty requestScope.listPlaylist}">
										<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="yt.pl.list.empty" /></p>
										</div>
									</c:when>
									<c:otherwise>
										<!-- Results Table -->
										<div class="col-xs-12 scroll no-padding">
											<table id="tablePagination" class="table table-striped">
												<thead>
													<tr>
														<th class="col-xs-2">
															<span><fmt:message key="lbl.playlist.thumbnail" /></span>
														</th>
														<th class="col-xs-3">
															<span><fmt:message key="lbl.playlist.title" /></span>
														</th>
														<th class="col-xs-1">
															<span><fmt:message key="lbl.playlist.active" /></span>
														</th>
														<th class="col-xs-3">
															<span><fmt:message key="global.date" /></span>
														</th>
														<th class="col-xs-1">
															<span><fmt:message key="lbl.playlist.privacy" /></span>
														</th>
														<th class="col-xs-2">
															<span><fmt:message key="lbl.actions" /></span>
														</th>
													</tr>
												</thead>
												<tbody>
													<!-- List Playlists -->
													<c:forEach items="${requestScope.listPlaylist}" var="playlist" varStatus="loop">
														<tr class="link-tab-list">
															<td>
																<a href="${pageContext.request.contextPath}/youtube_playlists?action=update&idPlaylist=${playlist.id}">
																	<img class="img-thumbnail-list unselectable" src="${playlist.urlImage}" alt="<fmt:message key="lbl.playlist.thumbnail" />" />
																</a>
															</td>
															<td>
																<div class="yt-table-cell">
																	${playlist.title}
																</div>
															</td>
															<td>
																<!-- Active -->
																<label class="switch">
																	<c:choose>
																		<c:when test="${playlist.active}">
																			<input id="checkboxIndex${loop.index}" class="checkbox-active" name="checkboxActive" type="checkbox" disabled="disabled"
																		onchange="setActive('${pageContext.request.contextPath}', ${loop.index}, '${playlist.id}', this)" checked />
																		</c:when>
																		<c:otherwise>
																			<input id="checkboxIndex${loop.index}" class="checkbox-active" name="checkboxActive" type="checkbox" disabled="disabled"
																		onchange="setActive('${pageContext.request.contextPath}', ${loop.index}, '${playlist.id}', this)" />
																		</c:otherwise>
																	</c:choose>
																	<span class="slider"></span>
																</label>
																<!-- End Active -->
															</td>
															<td>
																<fmt:formatDate value="${playlist.publishedAt}" pattern="${requestScope.formatterDate.toPattern()}" />
															</td>
															<td>
																<!-- Privacy -->
																<c:choose>
																	<c:when test="${playlist.privacy == 'public'}">
																		<i title="<fmt:message key="global.public" />" class="fas fa-globe-americas fa-fw fa-lg"></i>
																	</c:when>
																	<c:when test="${playlist.privacy == 'private'}">
																		<i title="<fmt:message key="global.private" />" class="fas fa-lock fa-fw fa-lg"></i>
																	</c:when>
																	<c:when test="${playlist.privacy == 'unlisted'}">
																		<i title="<fmt:message key="global.unlisted" />" class="fas fa-user-secret fa-fw fa-lg"></i>
																	</c:when>
																	<c:otherwise></c:otherwise>
																</c:choose>
																<!-- End Privacy -->
															</td>
															<td>
																<table class="margin-div">
																	<tr>
																		<td>
																			<a href="${pageContext.request.contextPath}/youtube_playlists?action=update&idPlaylist=${playlist.id}" class="btn btn-warning btn-xs actions-button small-marged"><i class="fas fa-edit fa-fw white"></i></a>
																		</td>
																		<td>
																			<a class="btn btn-danger btn-xs actions-button small-marged" data-toggle="modal" data-target=".modal-confirm-${playlist.id}" data-backdrop="static"><i class="fas fa-trash-alt fa-fw white"></i></a>
																		</td>
																	</tr>
																</table>
																<!-- Confirm Delete Modal -->
																<div class="modal fade modal-confirm-${playlist.id}" tabindex="-1" role="dialog" aria-labelledby="modal_label_${playlist.id}">
																	<div class="modal-dialog modal-sm" role="document">
																		<div class="modal-content">
																			<div class="modal-header">
																				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
																				<h4 id="modal_label_${playlist.id}" class="modal-title"><fmt:message key="global.confirm" /></h4>
																			</div>
																			<div class="modal-body">
																				<p><fmt:message key="yt.pl.delete" /></p>
																			</div>
																			<div class="modal-footer center">
																				<a href="${pageContext.request.contextPath}/youtube_playlists?action=delete&idPlaylist=${playlist.id}" id="delModalBtn_${playlist.id}" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
																				<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.cancel" /></button>
																			</div>
																		</div>
																	</div>
																</div>
																<!-- End Confirm Delete Modal -->
															</td>
														</tr>														
													</c:forEach>
													<!-- End List Playlists -->
												</tbody>
											</table>
										</div>
										<!-- End Results Table -->
									</c:otherwise>
								</c:choose>
							</div>
							<div class="panel-footer">
								<!-- Pagination -->
								<c:set var="queryStringInit" value="${request.getAttribute('jakarta.servlet.forward.query_string')}" />
								<c:if test="${not empty queryStringInit}">
									<c:set var="index" value="${fn:indexOf(queryStringInit, '&current=')}" />
									<c:if test="${index != -1}">
									    <c:set var="queryStringInit" value="${fn:substring(queryStringInit, 0, index)}" />
									</c:if>
									<c:if test="${not empty requestScope.prevPageToken}">
									    <c:set var="prevPagePath" value="${pageContext.request.contextPath}/youtube_playlists?${queryStringInit}&current=${requestScope.currentPage - 1}&pageToken=${requestScope.prevPageToken}" />
									</c:if>
									<c:if test="${not empty requestScope.nextPageToken}">
									    <c:set var="nextPagePath" value="${pageContext.request.contextPath}/youtube_playlists?${queryStringInit}&current=${requestScope.currentPage + 1}&pageToken=${requestScope.nextPageToken}" />
									</c:if>
								</c:if>
								<div class="pager">
									<div class="pull-left">
										<c:choose>
											<c:when test="${not empty prevPagePath}">
												<a id="prevResults" class="pager-item" href="${prevPagePath}"><i class="fas fa-backward fa-fw"></i></a>
											</c:when>
											<c:otherwise>
												<a id="prevResults" class="pager-item" href=""><i class="fas fa-backward fa-fw"></i></a>
											</c:otherwise>
										</c:choose>
									</div>
									<span id="displayPagination">
										<c:if test="${not empty requestScope.displayPagination}">
											${requestScope.displayPagination}
										</c:if>
									</span>
									<div class="pull-right">
										<c:choose>
											<c:when test="${not empty nextPagePath}">
												<a id="nextResults" class="pager-item" href="${nextPagePath}"><i class="fas fa-forward fa-fw"></i></a>
											</c:when>
											<c:otherwise>
												<a id="nextResults" class="pager-item" href=""><i class="fas fa-forward fa-fw"></i></a>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								<div>
									<img id="loadingImg" class="unselectable" src="${pageContext.request.contextPath}/img/loading.svg" alt="loading .." />
								</div>
								<!-- End Pagination -->
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
	<!-- Activation playlist JavaScript  -->
	<script src="${pageContext.request.contextPath}/js/youtube/playlist.js"></script>
    <!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="yt.pl.update.page.title" /></title>
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
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.pl.update.pl.header" />
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/youtube_playlists?action=list"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<!-- Error / Success -->
								<c:choose>
									<c:when test="${not empty requestScope.error}">
										<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification center" role="alert">
											<p><strong>Oops ! </strong>${requestScope.error}<i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
										</div>
									</c:when>
									<c:when test="${not empty requestScope.success}">
										<div id="alert-success" class="col-xs-offset-1 col-xs-10 alert alert-success panel-notification center" role="alert">
											<p>${requestScope.success}<i id="close-alert-success" class="fas fa-times-circle fa-fw close-button light-grey"></i></p>
										</div>
									</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
								<!-- Form Update -->
								<form id="updatePlaylistForm" action="youtube_playlists" method="post">
									<input type="hidden" name="action" value="update">
									<input type="hidden" name="idPlaylist" value="${requestScope.playlist.id}">
									<!-- Title -->
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 marged-top">
										<label for="textTitle"><fmt:message key="yt.pl.title" /></label>
										<br />
										<input id="textTitle" name="titlePlaylist" type="text" pattern=".{1,150}"
											title="<fmt:message key="help.1.150" />" value="${requestScope.playlist.title}" required>
									</div>
									<!-- Description -->
									<div class="col-xs-12 col-sm-5 marged-top">
										<label for="textDescription"><fmt:message key="yt.pl.description" /></label>
										<br />
										<input id="textDescription" name="descriptionPlaylist" type="text" pattern=".{0,5000}"
											title="<fmt:message key="help.0.5000" />" value="${requestScope.playlist.description}">
									</div>
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 marged-top">
										<label><fmt:message key="global.privacy" /></label>
										<br />
										<!-- Radio Public -->
										<label id="lblRdPublic" class="radioButton marged-top">
											<c:choose>
												<c:when test="${requestScope.playlist.privacy == 'public'}">
													<input name="radioPrivacy" type="radio" value="public" checked />
												</c:when>
												<c:otherwise>
													<input name="radioPrivacy" type="radio" value="public" />
												</c:otherwise>
											</c:choose>
											<span class="checkmarkRdBtn"></span>
											<span class="checkbox-item"><fmt:message key="global.public" /> <i class="fas fa-globe-americas fa-fw img-radio"></i></span>
										</label>
										<!-- Radio Private -->
										<label id="lblRdPrivate" class="radioButton">
											<c:choose>
												<c:when test="${requestScope.playlist.privacy == 'private'}">
													<input name="radioPrivacy" type="radio" value="private" checked />
												</c:when>
												<c:otherwise>
													<input name="radioPrivacy" type="radio" value="private" />
												</c:otherwise>
											</c:choose>
											<span class="checkmarkRdBtn"></span>
											<span class="checkbox-item"><fmt:message key="global.private" /> <i class="fas fa-lock fa-fw img-radio"></i></span>
										</label>
										<!-- Radio Unlisted -->
										<label id="lblRdUnlisted" class="radioButton">
											<c:choose>
												<c:when test="${requestScope.playlist.privacy == 'unlisted'}">
													<input name="radioPrivacy" type="radio" value="unlisted" checked />
												</c:when>
												<c:otherwise>
													<input name="radioPrivacy" type="radio" value="unlisted" />
												</c:otherwise>
											</c:choose>
											<span class="checkmarkRdBtn"></span>
											<span class="checkbox-item"><fmt:message key="global.unlisted" /> <i class="fas fa-user-secret fa-fw img-radio"></i></span>
										</label>
									</div>
									<!-- Check Active -->
									<div class="col-xs-12 col-sm-5 marged-top">
										<label for="checkboxActive"><fmt:message key="global.active" /></label>
										<br />
										<span class="checkbox-item"><fmt:message key="global.off" /></span>
										<label class="switch switch-update">
											<c:choose>
												<c:when test="${requestScope.playlist.active}">
													<input id="checkboxActive" name="checkboxActive" type="checkbox" checked />
												</c:when>
												<c:otherwise>
													<input id="checkboxActive" name="checkboxActive" type="checkbox" />
												</c:otherwise>
											</c:choose>
											<span class="slider"></span>
										</label>
										<span class="checkbox-item"><fmt:message key="global.on" /></span>
									</div>
									<div class="col-xs-12 spaced-vertical">
										<button type="submit" class="btn btn-success btn-xs btn-fixed"><fmt:message key="global.save.changes" /></button>
									</div>
								</form>
								<!-- End Form Update -->
							</div>
							<div class="panel-footer"></div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-md-offset-1 col-md-10">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.pl.update.video.header" />
								<div class="pull-right">
									<!-- Result Count -->
									<span id="totalSearch"><fmt:message key="yt.video.result.total" /> <span id="totalVideoCount">
										<c:choose>
											<c:when test="${not empty requestScope.listVideo}">
												<c:choose>
													<c:when test="${requestScope.listVideo.get(0).totalResults >= 10000}">
														<fmt:message key="yt.video.result.more" /> <span>10000</span>
													</c:when>
													<c:otherwise>
														${requestScope.listVideo.get(0).totalResults}
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												0
											</c:otherwise>
										</c:choose>
									</span></span>
									<!-- End Result Count -->
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/youtube_videos?action=add&idPlaylist=${requestScope.playlist.id}"><fmt:message key="global.add" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body no-padding">
								<c:choose>
									<c:when test="${empty requestScope.listVideo}">
										<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="yt.pl.update.video.empty" /></p>
										</div>
									</c:when>
									<c:otherwise>
										<!-- Results Table -->
										<div class="col-xs-12 scroll no-padding">
											<table id="tablePagination" class="table table-striped">
												<thead>
													<tr>
														<th class="col-xs-3">
															<span><fmt:message key="lbl.video.thumbnail" /></span>
														</th>
														<th class="col-xs-7">
															<span><fmt:message key="lbl.video.title" /></span>
														</th>
														<th class="col-xs-2">
															<span><fmt:message key="lbl.actions" /></span>
														</th>
													</tr>
												</thead>
												<tbody>
													<!-- List Videos -->
													<c:forEach items="${requestScope.listVideo}" var="video">
														<tr class="link-tab-list">
															<td>
																<a href="https://www.youtube.com/watch?v=${video.id}" target="_blank">
																	<img class="img-thumbnail-list unselectable" src="${video.urlImage}" alt="<fmt:message key="lbl.playlist.thumbnail" />" />
																</a>
															</td>
															<td>
																<div class="yt-table-cell">
																	${video.title}
																</div>
															</td>
															<td>
																<!-- Confirm Delete Modal -->
																<a class="btn btn-danger btn-xs actions-button small-marged" data-toggle="modal" data-target=".modal-confirm-${video.itemPlaylistId}" data-backdrop="static"><i class="fas fa-trash-alt fa-fw white"></i></a>
																<div class="modal fade modal-confirm-${video.itemPlaylistId}" tabindex="-1" role="dialog" aria-labelledby="modal_label_${video.itemPlaylistId}">
																	<div class="modal-dialog modal-sm" role="document">
																		<div class="modal-content">
																			<div class="modal-header">
																				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
																				<h4 id="modal_label_${video.itemPlaylistId}" class="modal-title"><fmt:message key="global.confirm" /></h4>
																			</div>
																			<div class="modal-body">
																				<p><fmt:message key="yt.video.delete" /></p>
																			</div>
																			<div class="modal-footer center">
																				<a href="${pageContext.request.contextPath}/youtube_videos?action=delete&idPlaylist=${requestScope.playlist.id}&idPlaylistItem=${video.itemPlaylistId}" id="delModalBtn_${video.itemPlaylistId}" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
																				<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.cancel" /></button>
																			</div>
																		</div>
																	</div>
																</div>
																<!-- End Confirm Delete Modal -->
															</td>
														</tr>
													</c:forEach>
													<!-- End List Videos -->
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
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
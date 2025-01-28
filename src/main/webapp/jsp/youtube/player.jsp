<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="yt.player.page.title" /></title>
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
						<div class="panel panel-default panel-player center-div">
							<div class="panel-heading">
								<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.player.header" />
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-large" href="${pageContext.request.contextPath}/youtube_playlists?action=list"><fmt:message key="yt.player.management" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-heading">
								<div id="selectPlayer">
									<div>
										<!-- Select Video -->
										<select id="selectVideo">
											<option value="-1" selected><fmt:message key="yt.player.all.videos" /></option>
											<c:if test="${not empty requestScope.listVideo}">
												<c:forEach items="${requestScope.listVideo}" var="video">
													<option value="${video.id}">${video.shortTitle}</option>
												</c:forEach>
											</c:if>
										</select>
										<!-- Select Playlist -->
										<select id="selectPlaylist">
											<option value="-1" selected><fmt:message key="yt.player.all.playlists" /></option>
											<c:if test="${not empty requestScope.listPlaylist}">
												<c:forEach items="${requestScope.listPlaylist}" var="playlist">
													<option value="${playlist.id}">${playlist.shortTitle}</option>
												</c:forEach>
											</c:if>
										</select>
									</div>
									<div>
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
								</div>
								<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger alert-player panel-notification center marged-top" role="alert">
									<p><strong>Oops ! </strong><fmt:message key="error.yt.player" /> <span id="errorId"></span> )<i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body no-padding">
								<c:choose>
									<c:when test="${not empty requestScope.listVideo}">
										<!-- Player Box -->
										<noscript class="col-xs-offset-1 col-xs-10 big-spaced-vertical"><fmt:message key="javascript.yt.player" /></noscript>
										<div id="noValid" class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="yt.player.no.valid.msg1" /><br /><fmt:message key="yt.player.no.valid.msg2" /></p>
										</div>
										<div id="playerBox">
											<div id="player"></div>
										</div>
										<!-- End Player Box -->
									</c:when>
									<c:otherwise>
										<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="yt.player.empty.msg1" /><br /><fmt:message key="yt.player.empty.msg2" /></p>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="panel-footer">
								<!-- Player Controls -->
								<div id="playerControls" class="pager unselectable">
									<div id="ytVideoArtistTitle" class="col-xs-8"></div>
									<div id="ytVideoTimes" class="col-xs-4"><span id="ytCurrentTime"></span> / <span id="ytDuration"></span></div>
									<div class="col-xs-12 spaced-vertical">
										<input id="ytProgressBar" type="range" min="0" max="100" step="any" value="0" title="<fmt:message key="yt.player.title.progress" />" />
									</div>
									<div class="col-xs-12 marged-top">
										<div class="pull-left">
											<div id="volDownVid" class="pager-item" title="<fmt:message key="yt.player.title.volume.down" />"><i class="fas fa-volume-down fa-fw"></i></div>
											<div id="volValueVid" class="pager-item-disable" title="<fmt:message key="yt.player.title.volume" />"></div>
											<div id="volUpVid" class="pager-item" title="<fmt:message key="yt.player.title.volume.up" />"><i class="fas fa-volume-up fa-fw"></i></div>
										</div>
										<div id="prevVid" class="pager-item" title="<fmt:message key="yt.player.title.previous" />"><i class="fas fa-backward fa-fw"></i></div>
										<div id="playVid" class="pager-item" title="<fmt:message key="yt.player.title.play" />"><i id="playVidIcon" class="fas fa-play fa-fw fa-sm"></i></div>
										<div id="pauseVid" class="pager-item" title="<fmt:message key="yt.player.title.pause" />"><i id="pauseVidIcon" class="fas fa-pause fa-fw fa-sm"></i></div>
										<div id="nextVid" class="pager-item" title="<fmt:message key="yt.player.title.next" />"><i class="fas fa-forward fa-fw"></i></div>
										<div class="pull-right">
											<div id="volMuteVid" class="pager-item" title="<fmt:message key="yt.player.title.mute" />"><i class="fas fa-volume-mute fa-fw red"></i></div>
											<div id="volUnmuteVid" class="pager-item" title="<fmt:message key="yt.player.title.unmute" />"><i class="fas fa-volume-off fa-fw"></i></div>
											<div id="fullscreenVid" class="pager-item" title="<fmt:message key="yt.player.title.fullscreen" />"><i class="fas fa-expand fa-fw"></i></div>
										</div>
										<div class="fixHeight"></div>
									</div>
								</div>
								<!-- End Player Controls -->
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
	<!-- YouTube IFrame -->
	<script src="//www.youtube.com/iframe_api"></script>
	<!-- YouTube Player JavaScript -->
	<script>
		const allVideo = '<fmt:message key="yt.player.all.videos" />';
		var videos = [
		    <c:if test="${not empty requestScope.listVideo}">
		    	<c:forEach items="${requestScope.listVideo}" var="video" varStatus="loop">
		    		<c:if test="${not loop.first}">, </c:if>
		    		["${video.id}", "${video.playlistId}", "${video.youtubeTitle}"]
		    	</c:forEach>
		    </c:if>
			];
    </script>
    <script src="${pageContext.request.contextPath}/js/youtube/player.js"></script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
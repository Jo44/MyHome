<%@ page import="java.util.*"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%@ page import="fr.my.home.bean.YouTubePlaylist"%>
<%@ page import="fr.my.home.bean.YouTubeVideo"%>
<%@ page import="fr.my.home.tool.GlobalTools"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	List<YouTubePlaylist> listPlaylist = (List<YouTubePlaylist>) view.getValueForKey("listPlaylist");
	List<YouTubeVideo> listVideo = (List<YouTubeVideo>) view.getValueForKey("listVideo");
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="yt.player.page.title" /></title>
<link href="<%=path%>/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="<%=path%>/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="<%=path%>/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- Font Awesome CSS -->
<link href="<%=path%>/css/plugins/font-awesome.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="<%=path%>/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="<%=path%>/css/youtube/youtube.css" rel="stylesheet" type="text/css" />
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
								<div id="headerTitle" class="in-line">
									<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.player.header" />
								</div>
								<div class="pull-right">
									<select id="selectVideo">
										<option value="-1" selected><fmt:message key="yt.player.all.videos" /></option>
										<%
											for (YouTubeVideo video : listVideo) {
												String title = video.getTitle().length() > 30 ? video.getTitle().substring(0, 30) + ".." : video.getTitle();
												out.print("<option value='" + video.getId() + "'>" + title + "</option>");
											}
										%>
									</select>
									<select id="selectPlaylist">
										<option value="-1" selected><fmt:message key="yt.player.all.playlists" /></option>
										<%
											for (YouTubePlaylist playlist : listPlaylist) {
												String title = playlist.getTitle().length() > 30 ? playlist.getTitle().substring(0, 30) + ".." : playlist.getTitle();
												out.print("<option value='" + playlist.getId() + "'>" + title + "</option>");
											}
										%>
									</select>
									<a id="headerButton" class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/youtube_playlists?action=list"><fmt:message key="yt.player.management" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body no-padding">
								<%
									if (listVideo != null && listVideo.size() > 0) {
								%>
								<noscript class="col-xs-offset-1 col-xs-10 big-spaced-vertical"><fmt:message key="javascript.yt.player" /></noscript>
								<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger alert-player panel-notification center" role="alert">
									<p><strong>Oops ! </strong><fmt:message key="error.yt.player" /> <span id="errorId"></span> )<i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
								</div>
								<div id="noValid" class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
									<p><fmt:message key="yt.player.no.valid.msg1" /><br /><fmt:message key="yt.player.no.valid.msg2" /></p>
								</div>
								<div id="playerBox">
									<div id="player"></div>
								</div>
								<%
									} else {
								%>
								<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
									<p><fmt:message key="yt.player.empty.msg1" /><br /><fmt:message key="yt.player.empty.msg2" /></p>
								</div>
								<%
									}
								%>
							</div>
							<div class="panel-footer">
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
	<!-- YouTube IFrame -->
	<script src="//www.youtube.com/iframe_api"></script>
	<!-- YouTube Player JavaScript -->
	<script>
		// Recupere le message
		var allVideo = "<fmt:message key='yt.player.all.videos' />";
		// Liste des videos
		var videos = [
		<%
			if (listVideo != null && listVideo.size() > 0) {
			    int first = 0;
		    	for (YouTubeVideo video : listVideo) {
		    	    if (first != 0) {
		    			out.print(", ");
		    	    }
		    	    out.print("['" + video.getId() + "', '" + video.getPlaylistId() + "', '" + GlobalTools.formattedYTTitle(video.getTitle()) + "']");
		    	    first++;
		    	}
			}
		%>
		];
    </script>
    <script src="<%=path%>/js/youtube/player.js"></script>
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
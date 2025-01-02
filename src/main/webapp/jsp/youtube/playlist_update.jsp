<%@ page import="java.util.*"%>
<%@ page import="fr.my.home.bean.YouTubePlaylist"%>
<%@ page import="fr.my.home.bean.YouTubeVideo"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
    User user = (User) request.getSession().getAttribute("user");
    ViewJSP view = (ViewJSP) request.getAttribute("view");
    String error = (String) view.getValueForKey("error");
    String success = (String) view.getValueForKey("success");
    String currentPageStr = (String) view.getValueForKey("current");
    YouTubePlaylist playlist = (YouTubePlaylist) view.getValueForKey("playlist");
    List<YouTubeVideo> listVideo = (List<YouTubeVideo>) view.getValueForKey("listVideo");
	String queryStringInit = (String) request.getAttribute("javax.servlet.forward.query_string");
	String prevPageToken = "";
	String nextPageToken = "";
	String prevPagePath = "";
	String nextPagePath = "";
	String displayPagination = "";
    int currentPage = (currentPageStr == null || currentPageStr.isEmpty()) ? 1 : Integer.parseInt(currentPageStr);
	int pageResults = 0;
	
	if (listVideo != null && listVideo.size() > 0) {
		if (listVideo.get(0).getPrevPageToken() != null && !listVideo.get(0).getPrevPageToken().isEmpty()) {
			prevPageToken = listVideo.get(0).getPrevPageToken();
		}
		if (listVideo.get(0).getNextPageToken() != null && !listVideo.get(0).getNextPageToken().isEmpty()) {
			nextPageToken = listVideo.get(0).getNextPageToken();
		}
		pageResults = listVideo.size();
		if (listVideo.size() > 1) {
			displayPagination = String.valueOf(((currentPage - 1) * 10) + 1) + " - " + String.valueOf(((currentPage - 1) * 10) + pageResults);
		}
	}
	if (queryStringInit != null) {
		int index = queryStringInit.lastIndexOf("&current=");
		if (index != -1) {
			queryStringInit = queryStringInit.substring(0, index);
		}
		if (prevPageToken != null && !prevPageToken.trim().isEmpty()) {
			prevPagePath = path + "/youtube_playlists?" + queryStringInit + "&current=" + String.valueOf(currentPage - 1) + "&pageToken=" + prevPageToken;
		}
		if (nextPageToken != null && !nextPageToken.trim().isEmpty()) {
			nextPagePath = path + "/youtube_playlists?" + queryStringInit + "&current=" + String.valueOf(currentPage + 1) + "&pageToken=" + nextPageToken;
		}
	}
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="yt.pl.update.page.title" /></title>
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
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.pl.update.pl.header" />
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/youtube_playlists?action=list"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<%
								    if (error != null) {
								%>
								<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification center" role="alert">
									<p><strong>Oops ! </strong><%=error%><i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
								</div>
								<%
								    } else if (success != null) {
								%>
								<div id="alert-success" class="col-xs-offset-1 col-xs-10 alert alert-success panel-notification center" role="alert">
									<p><%=success%><i id="close-alert-success" class="fas fa-times-circle fa-fw close-button light-grey"></i></p>
								</div>
								<%
								    }
								%>
								<form id="updatePlaylistForm" action="youtube_playlists" method="post">
									<input type="hidden" name="action" value="update">
									<input type="hidden" name="idPlaylist" value="<%=playlist.getId()%>">
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 marged-top">
										<label for="textTitle"><fmt:message key="yt.pl.title" /></label>
										<br />
										<input id="textTitle" name="titlePlaylist" type="text" pattern=".{1,150}"
											title="<fmt:message key="help.1.150" />" value="<%=playlist.getTitle()%>" required>
									</div>
									<div class="col-xs-12 col-sm-5 marged-top">
										<label for="textDescription"><fmt:message key="yt.pl.description" /></label>
										<br />
										<input id="textDescription" name="descriptionPlaylist" type="text" pattern=".{0,5000}"
											title="<fmt:message key="help.0.5000" />" value="<%=playlist.getDescription()%>">
									</div>
									<div class="col-xs-12 col-sm-offset-1 col-sm-5 marged-top">
										<label><fmt:message key="global.privacy" /></label>
										<br />
										<label id="lblRdPublic" class="radioButton marged-top">
											<input name="radioPrivacy" type="radio" value="public"
										  	<%
										  		if (playlist.getPrivacy().equals("public")) {
										  			out.print(" checked");
										  		}
											%>>
											<span class="checkmarkRdBtn"></span>
											<span class="checkbox-item"><fmt:message key="global.public" /> <i class="fas fa-globe-americas fa-fw img-radio"></i></span>
										</label>
										<label id="lblRdPrivate" class="radioButton">
											<input name="radioPrivacy" type="radio" value="private"
											<%
										  		if (playlist.getPrivacy().equals("private")) {
										  			out.print(" checked");
										  		}
											%>>
											<span class="checkmarkRdBtn"></span>
											<span class="checkbox-item"><fmt:message key="global.private" /> <i class="fas fa-lock fa-fw img-radio"></i></span>
										</label>
										<label id="lblRdUnlisted" class="radioButton">
											<input name="radioPrivacy" type="radio" value="unlisted"
											<%
										  		if (playlist.getPrivacy().equals("unlisted")) {
										  			out.print(" checked");
										  		}
											%>>
											<span class="checkmarkRdBtn"></span>
											<span class="checkbox-item"><fmt:message key="global.unlisted" /> <i class="fas fa-user-secret fa-fw img-radio"></i></span>
										</label>
									</div>
									<div class="col-xs-12 col-sm-5 marged-top">
										<label for="checkboxActive"><fmt:message key="global.active" /></label>
										<br />
										<span class="checkbox-item"><fmt:message key="global.off" /></span>
										<label class="switch switch-update">
											<input id="checkboxActive" name="checkboxActive" type="checkbox"
											<%
												if (playlist.isActive()) {
													out.print(" checked");
											    }
											%>>
											<span class="slider"></span>
										</label>
										<span class="checkbox-item"><fmt:message key="global.on" /></span>
									</div>
									<div class="col-xs-12 spaced-vertical">
										<button type="submit" class="btn btn-success btn-xs btn-fixed"><fmt:message key="global.save.changes" /></button>
									</div>
								</form>
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
									<span id="totalSearch"><fmt:message key="yt.video.result.total" /> <span id="totalVideoCount">
									<%
										if (listVideo != null && listVideo.size() > 0) {
											if (listVideo.get(0).getTotalResults() >= 10000) {
									%>
												<fmt:message key='yt.video.result.more' /> <span>10000</span>
									<%
											} else {
												out.print(String.valueOf(listVideo.get(0).getTotalResults()));
											}
										} else {
											out.print("0");
										}
									%>
									</span></span>
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/youtube_videos?action=add&idPlaylist=<%=playlist.getId()%>"><fmt:message key="global.add" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body no-padding">
								<%
								    if (listVideo == null || listVideo.size() < 1) {
								%>
								<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
									<p><fmt:message key="yt.pl.update.video.empty" /></p>
								</div>
								<%
								    } else {
								%>
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
											<%
												for (YouTubeVideo video : listVideo) {
											%>
											<tr class="link-tab-list">
												<td>
													<a href="https://www.youtube.com/watch?v=<%=video.getId()%>" target="_blank">
														<img class="img-thumbnail-list unselectable" src="<%=video.getUrlImage()%>" alt="<fmt:message key="lbl.playlist.thumbnail" />" />
													</a>
												</td>
												<td>
													<div class="yt-table-cell">
														<%=video.getTitle()%>
													</div>
												</td>
												<td>
													<a class="btn btn-danger btn-xs actions-button small-marged" data-toggle="modal" data-target=".modal-confirm-<%=video.getItemPlaylistId()%>" data-backdrop="static"><i class="fas fa-trash-alt fa-fw white"></i></a>
													<div class="modal fade modal-confirm-<%=video.getItemPlaylistId()%>" tabindex="-1" role="dialog" aria-labelledby="modal_label_<%=video.getItemPlaylistId()%>">
														<div class="modal-dialog modal-sm" role="document">
															<div class="modal-content">
																<div class="modal-header">
																	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
																	<h4 id="modal_label_<%=video.getItemPlaylistId()%>" class="modal-title"><fmt:message key="global.confirm" /></h4>
																</div>
																<div class="modal-body">
																	<p><fmt:message key="yt.video.delete" /></p>
																</div>
																<div class="modal-footer center">
																	<a href="<%=path%>/youtube_videos?action=delete&idPlaylist=<%=playlist.getId()%>&idPlaylistItem=<%=video.getItemPlaylistId()%>" id="delModalBtn_<%=video.getItemPlaylistId()%>" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
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
								<div class="pager">
									<div class="pull-left">
										<a id="prevResults" class="pager-item" href="<%=prevPagePath%>"><i class="fas fa-backward fa-fw"></i></a>
									</div>
									<span id="displayPagination"><%=displayPagination%></span>
									<div class="pull-right">
										<a id="nextResults" class="pager-item" href="<%=nextPagePath%>"><i class="fas fa-forward fa-fw"></i></a>
									</div>
								</div>
								<div>
									<img id="loadingImg" class="unselectable" src="<%=path%>/img/loading.svg" alt="loading .." />
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
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
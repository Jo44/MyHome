<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="fr.my.home.bean.YouTubePlaylist"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
    User user = (User) request.getSession().getAttribute("user");
    ViewJSP view = (ViewJSP) request.getAttribute("view");
    String error = (String) view.getValueForKey("error");
    String success = (String) view.getValueForKey("success");
    String currentPageStr = (String) view.getValueForKey("current");
    List<YouTubePlaylist> listPlaylist = (List<YouTubePlaylist>) view.getValueForKey("listPlaylist");
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
    String queryStringInit = (String) request.getAttribute("javax.servlet.forward.query_string");
    String prevPageToken = "";
	String nextPageToken = "";
	String prevPagePath = "";
	String nextPagePath = "";
    String displayPagination = "";
    int currentPage = (currentPageStr == null || currentPageStr.isEmpty()) ? 1 : Integer.parseInt(currentPageStr);
	int pageResults = 0;
	
	if (listPlaylist != null && listPlaylist.size() > 0) {
		if (listPlaylist.get(0).getPrevPageToken() != null && !listPlaylist.get(0).getPrevPageToken().isEmpty()) {
			prevPageToken = listPlaylist.get(0).getPrevPageToken();
		}
		if (listPlaylist.get(0).getNextPageToken() != null && !listPlaylist.get(0).getNextPageToken().isEmpty()) {
			nextPageToken = listPlaylist.get(0).getNextPageToken();
		}
		pageResults = listPlaylist.size();
		if (listPlaylist.size() > 1) {
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
<title><fmt:message key="yt.pl.list.page.title" /></title>
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
						<div class="panel panel-large center-div">
							<div class="panel-heading">
								<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.pl.list.header" />
								<div class="pull-right">
									<span id="totalSearch"><fmt:message key="yt.pl.result.total" /> <span id="totalPlaylistCount">
									<%
										if (listPlaylist != null && listPlaylist.size() > 0) {
											if (listPlaylist.get(0).getTotalResults() >= 10000) {
									%>
												<fmt:message key='yt.pl.result.more' /> <span>10000</span>
									<%
											} else {
												out.print(String.valueOf(listPlaylist.get(0).getTotalResults()));
											}
										} else {
											out.print("0");
										}
									%>
									</span></span>
									<a id="ytInfos" class="btn btn-classic btn-xs" data-toggle="modal" data-target=".modal-infos" data-backdrop="static"><i class="fas fa-info-circle fa-fw"></i></a>
									<div class="modal fade modal-infos" tabindex="-1" role="dialog" aria-labelledby="modal_label_infos">
										<div class="modal-dialog" role="document">
											<div class="modal-content center">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
													<h4 id="modal_label_infos" class="modal-title"><fmt:message key="yt.pl.infos.header" /></h4>
												</div>
												<div class="modal-body">
													<p><fmt:message key="yt.pl.infos.msg1" /> <span class="bold">00:00</span> <fmt:message key="yt.pl.infos.msg2" /></p>
												</div>
												<div class="modal-footer center">
													<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.close" /></button>
												</div>
											</div>
										</div>
									</div>
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/youtube_playlists?action=add"><fmt:message key="global.add" /></a>
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/youtube_player"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body no-padding">
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
									if (listPlaylist == null || listPlaylist.size() < 1) {
								%>
								<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
									<p><fmt:message key="yt.pl.list.empty" /></p>
								</div>
								<%
									} else {
								%>
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
											<%
												int index = 0;
												for (YouTubePlaylist playlist : listPlaylist) {
											%>
											<tr class="link-tab-list">
												<td>
													<a href="<%=path%>/youtube_playlists?action=update&idPlaylist=<%=playlist.getId()%>">
														<img class="img-thumbnail-list unselectable" src="<%=playlist.getUrlImage()%>" alt="<fmt:message key="lbl.playlist.thumbnail" />" />
													</a>
												</td>
												<td>
													<div class="yt-table-cell">
														<%=playlist.getTitle()%>
													</div>
												</td>
												<td>
													<label class="switch">
														<input id="checkboxIndex<%=index%>" class="checkbox-active" name="checkboxActive" type="checkbox" disabled="disabled"
															onchange="setActive('<%=path%>', <%=index%>, '<%=playlist.getId()%>', this)"
														<%
															if (playlist.isActive()) {
																	out.print(" checked");
															}
														%>>
														<span class="slider"></span>
													</label>
												</td>
												<td>
													<%=formatter.format(playlist.getPublishedAt())%>
												</td>
												<td>
													<%
														if (playlist.getPrivacy().equals("public")) {
													%>
														<i class="fas fa-globe-americas fa-fw fa-lg"></i>
													<%
														} else if (playlist.getPrivacy().equals("private")) {
													%>
														<i class="fas fa-lock fa-fw fa-lg"></i>
													<%	
														} else if (playlist.getPrivacy().equals("unlisted")){
													%>
														<i class="fas fa-user-secret fa-fw fa-lg"></i>
													<%
														}
													%>
												</td>
												<td>
													<table class="margin-div">
														<tr>
															<td>
																<a href="<%=path%>/youtube_playlists?action=update&idPlaylist=<%=playlist.getId()%>" class="btn btn-warning btn-xs actions-button small-marged"><i class="fas fa-edit fa-fw white"></i></a>
															</td>
															<td>
																<a class="btn btn-danger btn-xs actions-button small-marged" data-toggle="modal" data-target=".modal-confirm-<%=playlist.getId()%>" data-backdrop="static"><i class="fas fa-trash-alt fa-fw white"></i></a>
															</td>
														</tr>
													</table>
													<div class="modal fade modal-confirm-<%=playlist.getId()%>" tabindex="-1" role="dialog" aria-labelledby="modal_label_<%=playlist.getId()%>">
														<div class="modal-dialog modal-sm" role="document">
															<div class="modal-content">
																<div class="modal-header">
																	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-times fa-fw white"></i></span></button>
																	<h4 id="modal_label_<%=playlist.getId()%>" class="modal-title"><fmt:message key="global.confirm" /></h4>
																</div>
																<div class="modal-body">
																	<p><fmt:message key="yt.pl.delete" /></p>
																</div>
																<div class="modal-footer center">
																	<a href="<%=path%>/youtube_playlists?action=delete&idPlaylist=<%=playlist.getId()%>" id="delModalBtn_<%=playlist.getId()%>" class="btn btn-danger btn-sm btn-fixed"><fmt:message key="global.delete" /></a>
																	<button type="button" class="btn btn-primary btn-sm btn-fixed" data-dismiss="modal"><fmt:message key="global.cancel" /></button>
																</div>
															</div>
														</div>
													</div>
												</td>
											</tr>
											<%
												index++;
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
	<!-- Activation playlist JavaScript  -->
	<script src="<%=path%>/js/youtube/playlist.js"></script>
    <!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
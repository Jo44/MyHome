<%@ page import="java.util.*"%>
<%@ page import="fr.my.home.bean.YouTubeVideo"%>
<%@ page import="fr.my.home.bean.User"%>
<%@ page import="fr.my.home.bean.jsp.ViewJSP"%>
<%
	String path = getServletContext().getContextPath();
	User user = (User) request.getSession().getAttribute("user");
	ViewJSP view = (ViewJSP) request.getAttribute("view");
	String error = (String) view.getValueForKey("error");
	String idPlaylist = (String) view.getValueForKey("idPlaylist");
	String searchMethod = (String) view.getValueForKey("searchMethod");
	String searchInput = (String) view.getValueForKey("searchInput");
	String currentPageStr = (String) view.getValueForKey("currentPage");
	List<YouTubeVideo> listVideo = (List<YouTubeVideo>) view.getValueForKey("listVideo");
	int currentPage = (currentPageStr == null || currentPageStr.isEmpty()) ? 1 : Integer.parseInt(currentPageStr);
	int pageResults = 0;
	String prevPageToken = "";
	String nextPageToken = "";
	String displayPagination = "";
	
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
%>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="yt.video.page.title" /></title>
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
								<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.video.search.header" />
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-fixed" href="<%=path%>/youtube_playlists?action=update&idPlaylist=<%=idPlaylist%>"><fmt:message key="global.back" /></a>
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
								    }
								%>
								<div id="searchBloc">
									<form id="searchVideoForm" action="youtube_videos" method="post">
										<div class="col-xs-offset-1 col-xs-10 marged-top">
											<label id="lblRdKeyword" class="radioButton marged-top">
												<input name="searchMethod" type="radio" value="keyword"
												<%
													if (searchMethod == null || !searchMethod.equals("url")) {
														out.print(" checked");
													}
												%>>
												<span class="checkmarkRdBtn"></span>
												<span class="checkbox-item"><fmt:message key="yt.video.search.by.keyword" /></span>
											</label>
											<label id="lblRdUrl" class="radioButton">
												<input name="searchMethod" type="radio" value="url"
												<%
													if (searchMethod != null && searchMethod.equals("url")) {
														out.print(" checked");
													}
												%>>
												<span class="checkmarkRdBtn"></span>
												<span class="checkbox-item"><fmt:message key="yt.video.search.by.url" /></span>
											</label>
										</div>
										<div class="col-xs-offset-1 col-xs-10 marged-top">
											<input type="hidden" name="action" value="search">
											<input type="hidden" name="idPlaylist" value="<%=idPlaylist%>">
											<input type="hidden" name="currentPage" value="<%=currentPage%>">
											<input type="hidden" name="prevPageToken" value="<%=prevPageToken%>">
											<input type="hidden" name="nextPageToken" value="<%=nextPageToken%>">
											<input id="textSearch" name="searchInput" type="text" pattern=".{1,100}" title="<fmt:message key="help.search" />"
												placeholder="<fmt:message key="yt.video.search.keyword.example" />" 
												<%
													if (searchInput != null && !searchInput.isEmpty()) {
														out.print(" value='" + searchInput + "' ");
													} else {
														out.print(" ");
													}
												%>
												required autofocus>
										</div>
									</form>
									<div class="col-xs-offset-1 col-xs-10 big-marged-top marged-bottom">
										<button type="submit" name="submitMethod" form="searchVideoForm" class="btn btn-primary btn-xs btn-fixed" value="initPage"><fmt:message key="global.search" /></button>
									</div>
								</div>
								<noscript class="col-xs-offset-1 col-xs-10 big-spaced-vertical"><fmt:message key="javascript.yt.video.search" /></noscript>
							</div>
							<div class="panel-footer"></div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12 col-md-offset-1 col-md-10">
						<div class="panel panel-default center-div">
							<div class="panel-heading">
								<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.video.result.header" />
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
									<span id="selectSearch"><fmt:message key="yt.video.result.select" /> <span id="selectedVideoCount">0</span></span>
									<button id="addVideoBtn" type="submit" form="addVideosForm" class="btn btn-success btn-xs btn-fixed" disabled><fmt:message key="yt.video.result.add" /></button>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body scroll no-padding">
								<%
									if (listVideo == null || listVideo.size() < 1) {
								%>
								<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
									<p><fmt:message key="yt.video.result.empty" /></p>
								</div>
								<%
									} else {
								%>
								<form id="addVideosForm" action="youtube_videos" method="post">
									<input type="hidden" name="action" value="add">
									<input type="hidden" name="idPlaylist" value="<%=idPlaylist%>">
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
													<span><fmt:message key="lbl.video.select" /></span>
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
														<img class="img-thumbnail-list unselectable" src="<%=video.getUrlImage()%>" alt="<fmt:message key="lbl.video.thumbnail" />" />
													</a>
												</td>
												<td>
													<div class="yt-table-cell">
														<%=video.getTitle()%>
													</div>
												</td>
												<td>
													<label class="cbButton">
														<input type="checkbox" name="cbUrlId" value="<%=video.getId()%>">
														<span class="checkmarkCheckbox"></span>
													</label>
												</td>
											</tr>
											<%
												}
											%>
										</tbody>
									</table>
								</form>
								<%
									}
								%>
							</div>
							<div class="panel-footer">
								<div class="pager">
									<div class="pull-left">
										<button id="prevResults" type="submit" name="submitMethod" form="searchVideoForm" class="pager-item" value="prevPage" disabled>
											<i class="fas fa-backward fa-fw"></i>
										</button>
									</div>
									<span id="displayPagination"><%=displayPagination%></span>
									<div class="pull-right">
										<button id="nextResults" type="submit" name="submitMethod" form="searchVideoForm" class="pager-item" value="nextPage" disabled>
											<i class="fas fa-forward fa-fw"></i>
										</button>
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
	<!-- Search Video JavaScript -->
	<script src="<%=path%>/js/youtube/search.js"></script>
	<script>
		// Messages pour localisation
		var keywordExample = '<fmt:message key="yt.video.search.keyword.example" />';
		var urlExample = '<fmt:message key="yt.video.search.url.example" />';
		var addVideo = '<fmt:message key="yt.video.result.add" />';
		var addVideos = '<fmt:message key="yt.video.result.adds" />';
		// Token de pagination
		var prevPageToken = '<%=prevPageToken%>';
		var nextPageToken = '<%=nextPageToken%>';
	</script>
	<!-- My Home JavaScript -->
	<script src="<%=path%>/js/myhome.js"></script>
</body>
</html>
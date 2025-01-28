<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="yt.video.page.title" /></title>
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
								<i class="fab fa-youtube fa-fw"></i> <fmt:message key="yt.video.search.header" />
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/youtube_playlists?action=update&idPlaylist=${requestScope.idPlaylist}"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<!-- Error -->
								<c:if test="${not empty requestScope.error}">
									<div id="alert-danger" class="col-xs-offset-1 col-xs-10 alert alert-danger panel-notification center" role="alert">
										<p><strong>Oops ! </strong>${requestScope.error}<i id="close-alert-danger" class="fas fa-times-circle fa-fw close-button black"></i></p>
									</div>
								</c:if>
								<div id="searchBloc">
									<!-- Form Search -->
									<form id="searchVideoForm" action="youtube_videos" method="post">
										<div class="col-xs-offset-1 col-xs-10 marged-top">
											<label id="lblRdKeyword" class="radioButton marged-top">
												<!-- Radio Keyword -->
												<c:choose>
													<c:when test="${empty requestScope.searchMethod || requestScope.searchMethod != 'url'}">
														<input name="searchMethod" type="radio" value="keyword" checked />
													</c:when>
													<c:otherwise>
														<input name="searchMethod" type="radio" value="keyword" />
													</c:otherwise>
												</c:choose>
												<span class="checkmarkRdBtn"></span>
												<span class="checkbox-item"><fmt:message key="yt.video.search.by.keyword" /></span>
											</label>
											<label id="lblRdUrl" class="radioButton">
												<!-- Radio URL -->
												<c:choose>
													<c:when test="${not empty requestScope.searchMethod && requestScope.searchMethod == 'url'}">
														<input name="searchMethod" type="radio" value="url" checked />
													</c:when>
													<c:otherwise>
														<input name="searchMethod" type="radio" value="url" />
													</c:otherwise>
												</c:choose>
												<span class="checkmarkRdBtn"></span>
												<span class="checkbox-item"><fmt:message key="yt.video.search.by.url" /></span>
											</label>
										</div>
										<div class="col-xs-offset-1 col-xs-10 marged-top">
											<input type="hidden" name="action" value="search">
											<input type="hidden" name="idPlaylist" value="${requestScope.idPlaylist}">
											<input type="hidden" name="currentPage" value="${requestScope.currentPage}">
											<input type="hidden" name="prevPageToken" value="${requestScope.prevPageToken}">
											<input type="hidden" name="nextPageToken" value="${requestScope.nextPageToken}">
											<!-- Search Input -->
											<c:choose>
												<c:when test="${not empty requestScope.searchInput}">
													<input id="textSearch" name="searchInput" type="text" pattern=".{1,100}" title="<fmt:message key="help.search" />" 
														placeholder="<fmt:message key="yt.video.search.keyword.example" />" value="${requestScope.searchInput}" required autofocus />
												</c:when>
												<c:otherwise>
													<input id="textSearch" name="searchInput" type="text" pattern=".{1,100}" title="<fmt:message key="help.search" />" 
														placeholder="<fmt:message key="yt.video.search.keyword.example" />" required autofocus />
												</c:otherwise>
											</c:choose>
										</div>
									</form>
									<!-- End Form Search -->
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
									<!-- Result Count -->
									<span id="totalSearch"><fmt:message key="yt.pl.result.total" /> <span id="totalPlaylistCount">
										<c:choose>
											<c:when test="${not empty requestScope.listVideo}">
												<c:choose>
													<c:when test="${requestScope.listVideo.get(0).totalResults >= 10000}">
														<fmt:message key='yt.video.result.more' /> <span>10000</span>
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
									<span id="selectSearch"><fmt:message key="yt.video.result.select" /> <span id="selectedVideoCount">0</span></span>
									<button id="addVideoBtn" type="submit" form="addVideosForm" class="btn btn-success btn-xs btn-fixed" disabled><fmt:message key="yt.video.result.add" /></button>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body scroll no-padding">
								<c:choose>
									<c:when test="${empty requestScope.listVideo}">
										<div class="col-xs-offset-1 col-xs-10 big-spaced-vertical center">
											<p><fmt:message key="yt.video.result.empty" /></p>
										</div>
									</c:when>
									<c:otherwise>
										<!-- Form Add -->
										<form id="addVideosForm" action="youtube_videos" method="post">
											<input type="hidden" name="action" value="add">
											<input type="hidden" name="idPlaylist" value="${requestScope.idPlaylist}">
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
													<!-- List Videos -->
													<c:forEach items="${requestScope.listVideo}" var="video">
														<tr class="link-tab-list">
															<td>
																<a href="https://www.youtube.com/watch?v=${video.id}" target="_blank">
																	<img class="img-thumbnail-list unselectable" src="${video.urlImage}" alt="<fmt:message key="lbl.video.thumbnail" />" />
																</a>
															</td>
															<td>
																<div class="yt-table-cell">
																	${video.title}
																</div>
															</td>
															<td>
																<label class="cbButton">
																	<input type="checkbox" name="cbUrlId" value="${video.id}">
																	<span class="checkmarkCheckbox"></span>
																</label>
															</td>
														</tr>
													</c:forEach>
													<!-- End List Videos -->
												</tbody>
											</table>
										</form>
										<!-- End Form Add -->
									</c:otherwise>
								</c:choose>
							</div>
							<div class="panel-footer">
								<!-- Pagination -->							
								<div class="pager">
									<div class="pull-left">
										<button id="prevResults" type="submit" name="submitMethod" form="searchVideoForm" class="pager-item" value="prevPage" disabled>
											<i class="fas fa-backward fa-fw"></i>
										</button>
									</div>
									<span id="displayPagination">
										<c:if test="${not empty requestScope.displayPagination}">
											${requestScope.displayPagination}
										</c:if>
									</span>
									<div class="pull-right">
										<button id="nextResults" type="submit" name="submitMethod" form="searchVideoForm" class="pager-item" value="nextPage" disabled>
											<i class="fas fa-forward fa-fw"></i>
										</button>
									</div>
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
	<!-- Search Video JavaScript -->
	<script src="${pageContext.request.contextPath}/js/youtube/search.js"></script>
	<script>
		const keywordExample = '<fmt:message key="yt.video.search.keyword.example" />';
		const urlExample = '<fmt:message key="yt.video.search.url.example" />';
		const addVideo = '<fmt:message key="yt.video.result.add" />';
		const addVideos = '<fmt:message key="yt.video.result.adds" />';
		// Token de pagination
		<c:choose>
			<c:when test="${not empty requestScope.prevPageToken}">
				var prevPageToken = '${requestScope.prevPageToken}';
			</c:when>
			<c:otherwise>
				var prevPageToken = '';
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${not empty requestScope.nextPageToken}">
				var nextPageToken = '${requestScope.nextPageToken}';
			</c:when>
			<c:otherwise>
				var nextPageToken = '';
			</c:otherwise>
		</c:choose>
	</script>
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
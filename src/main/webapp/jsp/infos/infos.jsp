<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="infos.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
<!-- Bootstrap CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/bootstrap.css" rel="stylesheet" type="text/css" />
<!-- SB Admin CSS -->
<link href="${pageContext.request.contextPath}/css/plugins/sb-admin.css" rel="stylesheet" type="text/css" />
<!-- My Home CSS -->
<link href="${pageContext.request.contextPath}/css/myhome.css" rel="stylesheet" type="text/css" />
<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/css/infos/infos.css" rel="stylesheet" type="text/css" />
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
								<i class="fas fa-info-circle fa-fw"></i> <fmt:message key="infos.header" />
								<div class="pull-right">
									<a class="btn btn-primary btn-xs btn-fixed" href="${pageContext.request.contextPath}/home"><fmt:message key="global.back" /></a>
								</div>
								<div class="fixHeight"></div>
							</div>
							<div class="panel-body">
								<!-- About me -->
								<div class="col-xs-offset-2 col-xs-8 marged-bottom">
									<h1><fmt:message key="infos.about.title" /> :</h1>
								</div>
								<div class="col-xs-12 col-sm-offset-1 col-sm-10">
									<div id="aboutContent">
										<p><fmt:message key="infos.about.content" /></p>
									</div>
								</div>
								<!-- Technologies -->
								<div class="col-xs-offset-2 col-xs-8 spaced-vertical">
									<h1><fmt:message key="infos.tech.used.title" /> :</h1>
								</div>
								<!-- Raspberry Pi -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://www.raspberrypi.org/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/raspberry.png" alt="Raspberry Pi" /></a>
									</div>
									<div>
										<a href="https://www.raspberrypi.org/" target="_blank"><span class="info-label">Raspberry Pi</span></a>
									</div>
								</div>
								<!-- NGINX -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://www.nginx.com/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/nginx.png" alt="NGINX" /></a>
									</div>
									<div>
										<a href="https://www.nginx.com/" target="_blank"><span class="info-label">NGINX</span></a>
									</div>
								</div>
								<!-- Apache Tomcat -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="http://tomcat.apache.org/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/tomcat.png" alt="Apache Tomcat" /></a>
									</div>
									<div>
										<a href="http://tomcat.apache.org/" target="_blank"><span class="info-label">Apache Tomcat</span></a>
									</div>
								</div>
								<!-- Java -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://www.java.com/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/java.png" alt="Java 21" /></a>
									</div>
									<div>
										<a href="https://www.java.com/" target="_blank"><span class="info-label">Java 21</span></a>
									</div>
								</div>
								<!-- JSP / JSTL / EL -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://www.oracle.com/java/technologies/jspt.html" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/jsp.png" alt="JSP / JSTL / EL" /></a>
									</div>
									<div>
										<a href="https://www.oracle.com/java/technologies/jspt.html" target="_blank"><span class="info-label">JSP / JSTL / EL</span></a>
									</div>
								</div>
								<!-- HTML5 -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://www.w3schools.com/html/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/html.png" alt="HTML5" /></a>
									</div>
									<div>
										<a href="https://www.w3schools.com/html/" target="_blank"><span class="info-label">HTML5</span></a>
									</div>
								</div>
								<!-- CSS3 -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://www.w3schools.com/css/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/css.png" alt="CSS3" /></a>
									</div>
									<div>
										<a href="https://www.w3schools.com/css/" target="_blank"><span class="info-label">CSS3</span></a>
									</div>
								</div>
								<!-- JavaScript -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://www.w3schools.com/js/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/javascript.png" alt="JavaScript" /></a>
									</div>
									<div>
										<a href="https://www.w3schools.com/js/" target="_blank"><span class="info-label">JavaScript</span></a>
									</div>
								</div>
								<!-- jQuery -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://jquery.com/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/jquery.png" alt="jQuery" /></a>
									</div>
									<div>
										<a href="https://jquery.com/" target="_blank"><span class="info-label">jQuery</span></a>
									</div>
								</div>
								<!-- Hibernate -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="http://hibernate.org/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/hibernate.png" alt="Hibernate" /></a>
									</div>
									<div>
										<a href="http://hibernate.org/" target="_blank"><span class="info-label">Hibernate</span></a>
									</div>
								</div>
								<!-- MariaDB -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://mariadb.org/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/mariadb.png" alt="MariaDB" /></a>
									</div>
									<div>
										<a href="https://mariadb.org/" target="_blank"><span class="info-label">MariaDB</span></a>
									</div>
								</div
								<!-- Eclipse IDE -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://eclipseide.org/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/eclipse.png" alt="Eclipse IDE" /></a>
									</div>
									<div>
										<a href="https://eclipseide.org/" target="_blank"><span class="info-label">Eclipse IDE</span></a>
									</div>
								</div>
								<!-- Apache Maven -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://maven.apache.org/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/maven.png" alt="Apache Maven" /></a>
									</div>
									<div>
										<a href="https://maven.apache.org/" target="_blank"><span class="info-label">Apache Maven</span></a>
									</div>
								</div>
								<!-- Google Gemini -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://gemini.google.com/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/gemini.png" alt="Google Gemini" /></a>
									</div>
									<div>
										<a href="https://gemini.google.com/" target="_blank"><span class="info-label">Google Gemini</span></a>
									</div>
								</div>
								<!-- Chat GPT -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://chatgpt.com/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/chatgpt.png" alt="Chat GPT" /></a>
									</div>
									<div>
										<a href="https://chatgpt.com/" target="_blank"><span class="info-label">Chat GPT</span></a>
									</div>
								</div>
								<!-- Tabnine -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://www.tabnine.com/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/tabnine.png" alt="Tabnine" /></a>
									</div>
									<div>
										<a href="https://www.tabnine.com/" target="_blank"><span class="info-label">Tabnine</span></a>
									</div>
								</div>
								<!-- Google Cloud Platform -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://cloud.google.com/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/google.png" alt="Google Cloud Platform" /></a>
									</div>
									<div>
										<a href="https://cloud.google.com/" target="_blank"><span class="info-label">Google Cloud Platform</span></a>
									</div>
								</div>
								<!-- Google Maps -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://maps.googleapis.com/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/maps.png" alt="Google Maps" /></a>
									</div>
									<div>
										<a href="https://maps.googleapis.com/" target="_blank"><span class="info-label">Google Maps</span></a>
									</div>
								</div>
								<!-- Spotify -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://www.spotify.com/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/spotify.png" alt="Spotify" /></a>
									</div>
									<div>
										<a href="https://www.spotify.com/" target="_blank"><span class="info-label">Spotify</span></a>
									</div>
								</div>
								<!-- YouTube -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://www.youtube.com/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/youtube.png" alt="YouTube" /></a>
									</div>
									<div>
										<a href="https://www.youtube.com/" target="_blank"><span class="info-label">YouTube</span></a>
									</div>
								</div>
								<!-- reCAPTCHA -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://www.google.com/recaptcha/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/recaptcha.png" alt="reCAPTCHA" /></a>
									</div>
									<div>
										<a href="https://www.google.com/recaptcha/" target="_blank"><span class="info-label">reCAPTCHA</span></a>
									</div>
								</div>
								<!-- ip-api -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="http://ip-api.com/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/ipapi.png" alt="ip-api" /></a>
									</div>
									<div>
										<a href="http://ip-api.com/" target="_blank"><span class="info-label">ip-api</span></a>
									</div>
								</div>
								<!-- Apache Log4j -->
								<div class="col-xs-offset-2 col-xs-4 col-sm-offset-0 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://logging.apache.org/log4j/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/log4j.png" alt="Apache Log4j" /></a>
									</div>
									<div>
										<a href="https://logging.apache.org/log4j/" target="_blank"><span class="info-label">Apache Log4j</span></a>
									</div>
								</div>
								<!-- JUnit -->
								<div class="col-xs-4 col-sm-3 info-block unselectable">
									<div class="marged-bottom">
										<a href="https://junit.org/" target="_blank"><img class="info-img" src="${pageContext.request.contextPath}/img/infos/junit.png" alt="JUnit" /></a>
									</div>
									<div>
										<a href="https://junit.org/" target="_blank"><span class="info-label">JUnit</span></a>
									</div>
								</div>
							</div>
							<div class="panel-footer"></div>
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
	<!-- My Home JavaScript -->
	<script src="${pageContext.request.contextPath}/js/myhome.js"></script>
</body>
</html>
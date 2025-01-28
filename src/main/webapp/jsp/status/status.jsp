<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="My Home">
<meta name="author" content="Jonathan">
<title><fmt:message key="status.page.title" /></title>
<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon" type="image/x-icon" />
</head>
<body>
	<!-- CONTENT -->
	<br /> ***************************************
	<br /> **********&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MY&nbsp;&nbsp;HOME&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**********
	<br /> ***************************************
	<br /> ******&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;******
	<br /> ******&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tomcat&nbsp;Status&nbsp;:&nbsp;ONLINE&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;******
	<br /> ******&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;******
	<br /> ******&nbsp;&nbsp;&nbsp;&nbsp;Hibernate&nbsp;Status&nbsp;:&nbsp;
	<c:choose>
		<c:when test="${requestScope.databaseOnline}">
			ONLINE&nbsp;&nbsp;&nbsp;&nbsp;******
		</c:when>
		<c:otherwise>
			OFFLINE&nbsp;&nbsp;&nbsp;******
		</c:otherwise>
	</c:choose>
	<br /> ******&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;******
	<br /> ***************************************
	<br /> ***************************************
	<!-- END CONTENT -->
</body>
</html>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'FreedomFighterInfoForm.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
	/*window.onload = function() {
	alert("fuck!!");
		document.forms["fightersForm"].submit();
	};*/
	$(function() {
		document.forms["fightersForm"].submit();
	});
</script>
</head>

<body>

	<form method="post" action="freedomFighterInfo" name="fightersForm"
		style="padding: 100px">
		<button class="btn btn-info" type="submit">Generate Again</button>
	</form>


</body>

</html>

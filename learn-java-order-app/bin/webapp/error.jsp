<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/common.css">

<title>受注管理システム</title>
</head>
<body>
	<%@ include file="navbar.jsp"%>

	<main role="main" class="container">
		<h1>エラー</h1>
		<p>${message}</p>
		<button type="button" class="btn btn-secondary" onclick="history.back();">戻る</button>
	</main>

	<!-- jQueryとBootstrap -->
	<script src="js/jquery-3.5.1.slim.min.js"></script>
	<script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>
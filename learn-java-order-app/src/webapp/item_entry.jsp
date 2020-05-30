<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/common.css">

<title>商品マスタ登録</title>
</head>
<body>
	<script src="js/jquery-3.5.1.slim.min.js"></script>
	<script src="js/bootstrap.bundle.min.js"></script>

	<%@ include file="navbar.jsp"%>

	<main role="main" class="container">
		<h1>商品マスタ登録</h1>
		<%@ include file="error_message.jsp"%>
		<form action="item_entry" method="post">
			<input type="hidden" name="mode" id="mode" value="${mode}">
			<div class="form-group">
				<label for="item_id">商品ID : </label>
				<input type="text" class="form-control" name="item_id" id="item_id" value="${item_id}"
					<c:if test="${mode == 'UPDATE'}">readonly="readonly" style="background-color : lightgray;"</c:if>>
			</div>
			<div class="form-group">
				<label for="item_name">商品名 : </label>
				<input type="text" class="form-control" name="item_name" id="item_name" value="${item_name}">
			</div>
			<div class="form-group">
				<label for="item_price">単価(円) : </label>
				<input type="number" class="form-control" name="item_price" id="item_price" value="${item_price}">
			</div>
			<button type="submit" class="btn btn-primary">登録</button>
			<button type="button" class="btn btn-secondary" onclick="location.href='item_list'">戻る</button>
		</form>
	</main>
</body>
</html>
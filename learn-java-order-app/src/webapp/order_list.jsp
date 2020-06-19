<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/common.css">

<title>受注リスト</title>
</head>
<body>
	<%@ include file="navbar.jsp"%>

	<main role="main" class="container">
		<h1>受注リスト</h1>
		<div style="margin: 5px;">
			<a class="btn btn-primary" href="order_entry" role="button">
				<svg class="bi bi-plus-circle" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
 						<path fill-rule="evenodd" d="M8 3.5a.5.5 0 0 1 .5.5v4a.5.5 0 0 1-.5.5H4a.5.5 0 0 1 0-1h3.5V4a.5.5 0 0 1 .5-.5z"/>
 						<path fill-rule="evenodd" d="M7.5 8a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1H8.5V12a.5.5 0 0 1-1 0V8z"/>
						<path fill-rule="evenodd" d="M8 15A7 7 0 1 0 8 1a7 7 0 0 0 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
				</svg>
				<span style="vertical-align: middle;">新規登録</span>
			</a>
		</div>
		<table class="table table-striped">
			<thead>
				<tr>
					<th>受注ID</th>
					<th>ステータス</th>
					<th>受注日</th>
					<th>出荷日</th>
					<th>顧客名</th>
					<th>受注金額</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="order" items="${list}">
					<tr>
						<td><a href="order_entry?order_id=${order.orderId}"><c:out value="${order.orderId}" /></a></td>
						<td><c:out value="${order.orderStatus.name}" /></td>
						<td><c:out value="${order.orderDate}" /></td>
						<td><c:out value="${order.shipDate}" /></td>
						<td><c:out value="${order.customerName}" /></td>
						<td style="text-align: right;"><fmt:formatNumber value="${order.orderAmount}" pattern="#,##0 円"></fmt:formatNumber></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</main>

	<!-- jQueryとBootstrap -->
	<script src="js/jquery-3.5.1.slim.min.js"></script>
	<script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>
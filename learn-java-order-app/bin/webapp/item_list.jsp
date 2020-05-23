<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品マスタ</title>
</head>
<body>
<h1>商品マスタ</h1>
<form name="item_form" action="item_list" method="post">
<input type="hidden" id="command" name="command" value="">
<a href="item_entry">新規登録</a>
<table border="1">
<thead>
<tr>
<th>商品ID</th><th>商品名</th><th>単価</th><th>削除</th>
</tr>
</thead>
<tbody>
<c:forEach var="item" items="${list}">
<tr>
<td><a href="item_entry?item_id=${item.itemId}"><c:out value="${item.itemId}" /></a></td>
<td><c:out value="${item.itemName}" /></td>
<td><c:out value="${item.itemPrice}" />円</td>
<td>
<input type="submit" value="削除" onclick="item_form.command.value='delete ${item.itemId}';">
</td>
</tr>
</c:forEach>
</tbody>
</table>
</form>
</body>
</html>
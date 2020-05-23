<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品マスタ登録</title>
</head>
<body>
<h1>商品マスタ登録</h1>
<c:forEach var="message" items="${errors}">
<p style="color:red; margin: 0;">${message}</p>
</c:forEach>
<form action="item_entry" method="post">
<input type="hidden" name="mode" id="mode" value="${mode}">
商品ID : <input type="text" name="item_id" id="item_id" value="${item_id}"
           <c:if test="${mode == 'update'}">
             readonly="readonly"
             style="background-color : lightgray;"
           </c:if>
         ><br>
商品名 : <input type="text" name="item_name" id="item_name" value="${item_name}"><br>
単価　 : <input type="number" name="item_price" id="item_price" value="${item_price}">円<br>
<input type="submit" value="登録">
<input type="button" onclick="location.href='./item_list'" value="戻る">
</form>
</body>
</html>
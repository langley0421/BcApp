<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>名刺管理</title>
</head>
<body>
<h1>検索</h1>
<hr>
<form method="POST" action="/webapp/SearchServlet">
氏名<input type="text" name="name"><br>
住所<input type="text" name="address"><br>
<input type="submit" name="regist" value="検索"><br>
</form>
<a href="/webapp/MenuServlet">メニューへ戻る</a>
</body>
</html>

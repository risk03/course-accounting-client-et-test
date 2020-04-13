<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Учёт реализации товаров - Товары</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-3.4.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>
</head>
<body>
<div class="wrapper">
    <div class="left-trigger">
    </div>
    <div id="black"></div>
    <div id="menu">
        <input name="menubutton" type="button" value="Магазины" onclick="location.href='stores'">
        <input name="menubutton" type="button" value="Товары" onclick="location.href='products'">
        <input name="menubutton" type="button" value="Пользователи" onclick="location.href='users'">
    </div>
    <div class="welcome">
        <h1>Добро пожаловать!</h1>
        Система учёта реализации товаров
    </div>
</div>
</body>
</html>
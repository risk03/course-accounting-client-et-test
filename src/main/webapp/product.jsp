<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Учёт реализации товаров - Поиск товара</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-3.4.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>
    <script src="${pageContext.request.contextPath}/js/hollow.js"></script>
</head>
<body>
<div class="wrapper">
    <div class="left-trigger">
    </div>
    <div id="black"></div>
    <div id="menu">
        <input name="menubutton" type="button" value="Главная страница" onclick="location.href='index'">
        <input name="menubutton" type="button" value="Магазины" onclick="location.href='stores'">
        <input name="menubutton" type="button" value="Товары" onclick="location.href='products'">
        <input name="menubutton" type="button" value="Чеки" onclick="location.href='transactions'">
        <input name="menubutton" type="button" value="Пользователи" onclick="location.href='users'">
        <input name="menubutton" type="button" value="Отчёты" onclick="location.href='reports'">
    </div>
    <div class="up">
        <table class="main-table">
            <tr>
                <td>
                    <div class="wrapper-header">
                        <table class="table-header">
                            <tr>
                                <th>Магазин</th>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="wrapper-content">
                        <table class="table-content">
                            <jsp:useBean id="storeList" scope="request" type="java.util.ArrayList"/>
                            <c:forEach items="${storeList}" var="i">
                                <tr>
                                    <td><c:out value="${i[0]}"/></td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <div class="down">
        <form>
            <jsp:useBean id="productInfo" scope="request" type="java.util.ArrayList"/>
            <c:out value="${productInfo[1]}"/>
            <input type="hidden" name="id" value="${productInfo[0]}">
        </form>
    </div>
</div>
</body>
</html>
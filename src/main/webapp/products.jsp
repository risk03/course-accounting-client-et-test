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
    <script src="${pageContext.request.contextPath}/js/products.js"></script>
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
    <div class="up">
        <table class="main-table">
            <tr>
                <td>
                    <div class="wrapper-header">
                        <table class="table-header">
                            <tr>
                                <th>ID</th>
                                <th>Наименование</th>
                                <th>Цена продажи</th>
                                <th>Описание</th>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="wrapper-content">
                        <table class="table-content">
                            <jsp:useBean id="productList" scope="request" type="java.util.ArrayList"/>
                            <c:forEach items="${productList}" var="i">
                                <tr><td><c:out value="${i[0]}"/></td><td><c:out value="${i[1]}"/></td><td><c:out value="${i[2]}"/></td><td><c:out value="${i[3]}"/></td></tr>
                            </c:forEach>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <div class="down">
        <form>
            <label>Данные</label>
            <table>
                <tr>
                    <td>ID</td>
                    <td><label><input name="id" type="number" min="0"/></label></td>
                    <td><label><input type="submit" value="Добавить" name="operation"></label></td>
                </tr>
                <tr>
                    <td>Наименование</td>
                    <td><label><input name="name" type="text" maxlength="60"></label></td>
                    <td><label><input type="submit" value="Сохранить" name="operation"></label></td>
                </tr>
                <tr>
                    <td>Цена продажи</td>
                    <td><label><input name="sellingPrice" type="number" min="0" max="9999999999" step="0.01"></label></td>
                    <td><label><input type="submit" value="Удалить" name="operation"></label></td>
                </tr>
                <tr>
                    <td>Описание</td>
                    <td><label><input name="description" type="text" maxlength="255"></label></td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>
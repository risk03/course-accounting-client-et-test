<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Учёт реализации товаров - Отчёты</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-3.4.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>
    <script src="${pageContext.request.contextPath}/js/hollow.js"></script>
    <script>$(document).ready(function () {
        $("input[type='datetime-local']").each(function () {
            $(this).val(new Date().toJSON().slice(0, 19))
        });
    });
    </script>
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
                                <jsp:useBean id="headers" scope="request" type="java.util.ArrayList"/>
                                <c:forEach items="${headers}" var="i">
                                    <th><c:out value="${i}"/></th>
                                </c:forEach>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="wrapper-content">
                        <table class="table-content">
                            <jsp:useBean id="reportrow" scope="request" type="java.util.ArrayList"/>
                            <c:forEach items="${reportrow}" var="i">
                                <tr>
                                    <c:forEach items="${i}" var="j">
                                        <td><c:out value="${j}"/></td>
                                    </c:forEach>
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
            <table>
                <tr>
                    <td>Тип отчёта</td>
                    <td><label>
                        <select name="reportType">
                            <option>О магазинах</option>
                            <option>О товарах</option>
                            <option>О сотрудниках</option>
                        </select>
                    </label>
                    </td>
                </tr>
                <tr>
                    <td>Начало периода</td>
                    <td><label><input type="datetime-local" name="dateFrom" step="1"></label></td>
                </tr>
                <tr>
                    <td>Конец периода</td>
                    <td><label><input type="datetime-local" name="dateTo" step="1"></label></td>
                </tr>
                <tr>
                    <td>Валюта</td>
                    <td><label>
                        <select name="currency">
                            <option>BYN</option>
                            <option>USD</option>
                            <option>EUR</option>
                        </select>
                    </label></td>
                </tr>
                <tr>
                    <td colspan="2"><label>
                        <input name="operation" type="submit" value="Получить">
                    </label></td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>
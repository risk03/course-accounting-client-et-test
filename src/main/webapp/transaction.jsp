<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Учёт реализации товаров - Магазин</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/table.css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-3.4.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/table.js"></script>
    <script src="${pageContext.request.contextPath}/js/transaction.js"></script>
</head>
<body>
<div class="wrapper">
    <div class="left-trigger">
    </div>
    <div id="black"></div>
    <div id="menu">
        <input name="menubutton" type="button" value="Магазины" onclick="location.href='stores'">
        <input name="menubutton" type="button" value="Товары" onclick="location.href='products'">
        <input name="menubutton" type="button" value="Чеки" onclick="location.href='transactions'">
        <input name="menubutton" type="button" value="Пользователи" onclick="location.href='users'">
    </div>
    <div class="up">
        <table class="main-table">
            <tr>
                <td>
                    <div class="wrapper-header">
                        <table class="table-header">
                            <tr>
                                <th>Наименование</th>
                                <th>Количество</th>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="wrapper-content">
                        <table class="table-content">
                            <jsp:useBean id="entryList" scope="request" type="java.util.ArrayList"/>
                            <c:forEach items="${entryList}" var="i">
                                <tr>
                                    <td><c:out value="${i[0]}"/></td>
                                    <td><c:out value="${i[1]}"/></td>
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
            <jsp:useBean id="transactionInfo" scope="request" type="java.util.List"/>
            <label>Чек №${transactionInfo[0]}<br>
                ${transactionInfo[1]}<br>
                Кассир ${transactionInfo[2]}<br>
                от ${transactionInfo[3]}</label>
            <table>
                <tr>
                    <td>Товар</td>
                    <td><label><select name="product">
                        <jsp:useBean id="productList" scope="request" type="java.util.ArrayList"/>
                        <c:forEach items="${productList}" var="i">
                            <option><c:out value="${i}"/></option>
                        </c:forEach>
                    </select></label></td>
                    <td><label><input type="submit" value="Установить" name="operation"></label></td>
                </tr>
                <tr>
                    <td>Количество</td>
                    <td><label><input name="quantity" type="number" min="0" max="9999999999" step="0.001"></label></td>
                    <td></td>
                </tr>
            </table>
            <input type="hidden" name="id" value="${transactionInfo[0]}">
        </form>
    </div>
</div>
</body>
</html>
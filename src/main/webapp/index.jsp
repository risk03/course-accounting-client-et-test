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
        <input name="menubutton" type="button" value="Главная страница" onclick="location.href='index'">
        <jsp:useBean id="loginStatus" scope="request" type="java.lang.String"/>
        <c:if test="${loginStatus.equals(\"Заведующий\")}">
            <%="<input name=\"menubutton\" type=\"button\" value=\"Магазины\" onclick=\"location.href='stores'\"><input name=\"menubutton\" type=\"button\" value=\"Товары\" onclick=\"location.href='products'\"><input name=\"menubutton\" type=\"button\" value=\"Чеки\" onclick=\"location.href='transactions'\"><input name=\"menubutton\" type=\"button\" value=\"Пользователи\" onclick=\"location.href='users'\"><input name=\"menubutton\" type=\"button\" value=\"Отчёты\" onclick=\"location.href='reports'\">"%>
        </c:if>
        <c:if test="${loginStatus.equals(\"Кассир\")}">
            <%="<input name=\"menubutton\" type=\"button\" value=\"Магазины\" onclick=\"location.href='stores'\"><input name=\"menubutton\" type=\"button\" value=\"Товары\" onclick=\"location.href='products'\"><input name=\"menubutton\" type=\"button\" value=\"Чеки\" onclick=\"location.href='transactions'\">"%>
        </c:if>
    </div>
    <div class="welcome">
        <h1>Добро пожаловать!</h1>
        Система учёта реализации товаров<br>
        <c:if test="${!loginStatus.equals(\"notLogged\")}">
            <jsp:useBean id="loggedUser" scope="request" type="java.util.ArrayList"/>
            <c:out value="${loggedUser[4]} ${loggedUser[1]} ${loggedUser[2]} ${loggedUser[3]}"/>
            <form>
                <input type="submit" name="loginOperation" value="Выход">
            </form>
        </c:if>
        <c:if test="${loginStatus.equals(\"notLogged\")}">
            <form style="display: inline-block">
                <label></label>
                <table>
                    <tr>
                        <td>Логин</td>
                        <td><label><input name="loginLogin" type="text" maxlength="45"/></label></td>
                    </tr>
                    <tr>
                        <td>Пароль</td>
                        <td><label><input name="loginPassword" type="password" maxlength="45"></label></td>
                    </tr>
                    <tr>
                        <td colspan="2"><input type="submit" name="loginOperation" value="Вход"></td>
                    </tr>
                </table>
            </form>
        </c:if>
    </div>
</div>
</body>
</html>
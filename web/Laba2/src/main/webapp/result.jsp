<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en"/>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>lab2</title>
    <link rel="stylesheet" href="result.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Марьин Григорий</h1>
        <h1>Группа: P3212 Вариант: </h1>
        <a href="index.jsp">Назад</a>
    </div>
    <div class="content">
        <section class="results-section">
            <h2>Есть пробитие?</h2>
            <table class="results-table">
                <thead>
                <tr>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>result</th>
                    <th>Время запроса</th>
                    <th>Время работы</th>
                </tr>
                </thead>
                <tbody>
                <jsp:useBean id="results" scope="session" class="web.beans.ResultBean"/>
                <c:if test="${not empty results.points}">
                    <c:set var="lastResult" value="${results.points[results.points.size() - 1]}"/>
                    <tr>
                        <td><fmt:formatNumber value="${lastResult.x}" minFractionDigits="0" maxFractionDigits="15"/></td>
                        <td><fmt:formatNumber value="${lastResult.y}" minFractionDigits="0" maxFractionDigits="15"/></td>
                        <td><fmt:formatNumber value="${lastResult.r}" minFractionDigits="0"/></td>
                        <td>${lastResult.inside ? "Есть пробитие" : "Почти попал"}</td>
                        <td><fmt:formatDate value="${lastResult.timestamp}" pattern="dd.MM.yyyy, HH:mm:ss"/></td>
                        <td>${lastResult.executionTime}ms</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </section>
    </div>
</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="web.beans.ResultBean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    ResultBean resultBean = (ResultBean) session.getAttribute("results");
    if (resultBean == null) {
        resultBean = new ResultBean();
        session.setAttribute("results", resultBean);
    }
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Проверка попадания точки в область</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Проверка попадания точки в область</h1>
        <div class="header-info">
            <div>ФИО: Марьин Григорий Алексеевич</div>
            <div>Группа: P3212</div>
            <div>Вариант: 468259</div>
        </div>
    </div>

    <div class="content">
        <div class="form-section">
            <h2>Параметры точки и области</h2>
            <form id="pointForm" method="POST" action="controller">
                <div class="form-group">
                    <label for="x">Координата X:</label>
                    <select id="x" name="x">
                        <option value="">Выберите значение</option>
                        <option value="-5">-5</option>
                        <option value="-4">-4</option>
                        <option value="-3">-3</option>
                        <option value="-2">-2</option>
                        <option value="-1">-1</option>
                        <option value="0">0</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                    </select>
                    <div class="error-message" id="xError"></div>
                </div>

                <div class="form-group">
                    <label for="y">Координата Y:</label>
                    <input type="text" id="y" name="y" placeholder="Введите число от -5 до 5" value="">
                    <div class="error-message" id="yError"></div>
                </div>

                <div class="form-group">
                    <label for="r">Радиус R:</label>
                    <input type="text" id="r" name="r" placeholder="Введите число: 1, 2, 3, 4 или 5" value="3">
                    <div class="error-message" id="rError"></div>
                </div>

                <button type="submit" class="submit-btn">Проверить попадание</button>
            </form>
        </div>

        <div class="graph-section">
            <h2>Область на координатной плоскости</h2>
            <p>Mocha Mousse - Цвет 2025 года hex-код #A47864</p>
            <div class="graph-container" id="graphContainer">
                <canvas id="graphCanvas" width="500" height="400"></canvas>
            </div>
        </div>

        <div class="results-section">
            <h2>Результаты проверок</h2>
            <table class="results-table">
                <thead>
                <tr>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>Результат</th>
                    <th>Время</th>
                </tr>
                </thead>
                <tbody id="resultsBody">
                <c:forEach var="point" items="${results.points}">
                    <tr>
                        <td>${point.x}</td>
                        <td>${point.y}</td>
                        <td>${point.r}</td>
                        <td class="${point.inside ? 'result-hit' : 'result-miss'}">
                                ${point.inside ? 'Попадание' : 'Непопадание'}
                        </td>
                        <td>${point.timestamp}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="notification" id="notification"></div>

<script>
    const sessionPoints = [
        <c:forEach var="point" items="${results.points}" varStatus="status">
        {
            x: ${point.x},
            y: ${point.y},
            r: ${point.r},
            inside: ${point.inside}
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];
</script>

<script src="index.js"></script>
</body>
</html>
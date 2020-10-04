<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>meals</h2>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calorise</th>
        <th></th>
        <th></th>
    </tr>

    <c:forEach items="${meals}" var="mealsv">
        <c:if test="${mealsv.excess == true}">
                <tr style="color: red">
                    <td>${mealsv.dateTime}</td>
                    <td>${mealsv.description}</td>
                    <td>${mealsv.calories}</td>
                    <td></td>
                    <td></td>
                </tr>
        </c:if>
        <c:if test="${mealsv.excess == false}">
                <tr style="color: green">
                    <td>${mealsv.dateTime}</td>
                    <td>${mealsv.description}</td>
                    <td>${mealsv.calories}</td>
                    <td></td>
                    <td></td>
                </tr>
        </c:if>
    </c:forEach>

</table>
</body>
</html>
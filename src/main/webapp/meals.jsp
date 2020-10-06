<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html lang="ru">
<style type="text/css">
    /*BODY {*/
    /*    background: white; !* Цвет фона веб-страницы *!*/
    /*}*/
    TABLE {
        width: 600px; /* Ширина таблицы */
        border-collapse: collapse; /* Убираем двойные линии между ячейками */
        /*border: 2px solid white; !* Прячем рамку вокруг таблицы *!*/
    }
    TD, TH {
        padding: 3px; /* Поля вокруг содержимого таблицы */
        border: 2px solid black; /* Параметры рамки */
        text-align: left; /* Выравнивание по левому краю */
    }
</style>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<a href="?action=add">Add Meal</a>
<br/>
<%--https://stackoverflow.com/questions/35606551/jstl-localdatetime-format--%>
<table>
    <tr>
        <th>Id</th>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>

    <c:forEach items="${meals}" var="mealsv">
        <c:if test="${mealsv.excess == true}">
                <tr style="color: red">
                    <td>${mealsv.id}</td>
                    <td><javatime:format value="${mealsv.dateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
                    <td>${mealsv.description}</td>
                    <td>${mealsv.calories}</td>
                    <td>Update</td>
                    <td><a href="?action=delete&id=${mealsv.id}">Delete</a></td>
                </tr>
        </c:if>
        <c:if test="${mealsv.excess == false}">

                <tr style="color: green">
                    <td>${mealsv.id}</td>
                    <td><javatime:format value="${mealsv.dateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
                    <td>${mealsv.description}</td>
                    <td>${mealsv.calories}</td>
                    <td>Update</td>
                    <td><a href="?action=delete&id=${mealsv.id}">Delete</a></td>
                </tr>
        </c:if>
    </c:forEach>

</table>
</body>
</html>
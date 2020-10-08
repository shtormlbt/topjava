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
        border: 0px solid black; /* Параметры рамки */
        text-align: left; /* Выравнивание по левому краю */
    }
</style>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<%--<c:if test="${action==add}">--%>
<%--    <h2>Add meal</h2>--%>
<%--</c:if>--%>
<%--<c:if test="${action==update}">--%>
<%--    <h2>Edit meal</h2>--%>
<%--</c:if>--%>

<% if (request.getAttribute("action").equals("add")) { %>
<h3>Add meal</h3>
<% } %>
<% if (request.getAttribute("action").equals("update")) { %>
<h3>Edit meal</h3>
<% } %>

<form action="meals" method="post" name="frmAddMeal">

    <input type="hidden" name="id" value="<c:out value="${meal.id}"/>" >
    <table>
        <tr>
            <td>
    DateTime:
            </td>
            <td>
            <input type="datetime-local" name="dateTime" value="<c:out value="${meal.dateTime}"/>" /><br />
            </td>
        </tr>
        <tr>
            <td>
    Description:
            </td>
            <td>
                <input type="text"  name="description" value="<c:out value="${meal.description}"/>" /><br />
            </td>
        </tr>
        <tr>
            <td>
    Calories:
            </td>
            <td>
                <input type="text" name="calories" value="<c:out value="${meal.calories}"/>" /><br />
            </td>
        </tr>
    </table>
    <br/>
    <input  type="submit" value="Save" />
</form>

</body>
</html>
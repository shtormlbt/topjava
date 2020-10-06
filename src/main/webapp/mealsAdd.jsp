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
<h2>Meals</h2>

<form action="meals" method="post" name="frmAddMeal">

    <table>
        <tr>
            <td>
    DateTime:
            </td>
            <td>
            <input type="datetime-local" name="dateTime" value="<c:out value="${mealsv.dateTime}"/>" /><br />
            </td>
        </tr>
        <tr>
            <td>
    Description:
            </td>
            <td>
                <input type="text"  name="description" value="<c:out value="${mealsv.description}"/>" /><br />
            </td>
        </tr>
        <tr>
            <td>
    Calories:
            </td>
            <td>
                <input type="text" name="calories" value="<c:out value="${mealsv.calories}"/>" /><br />
            </td>
        </tr>
    </table>
    <br/>
    <input  type="submit" value="Save" />
</form>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="https://example.com/functions"%>
<html lang="ru">
<head>
    <title>Meal</title>
    <style>
        input {
            margin: 5px 10px 5px 0px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${meal !=null ? "Edit meal" : "Insert meals"}</h2>
<form method="POST" action='meals' name="frmInsertEdit">
    <input type="hidden" name="id" value="${meal.id}">
    DateTime : <input type="datetime-local" name="dateTime" value="${meal != null ? meal.dateTime : f:getNowDateTime()}"/> <br/>
    Description : <input type="text" name="description" value="${meal.description}"/><br/>
    Calories : <input type="number" name="calories" value="${meal.calories}"/><br/>
    <input type="submit" value="${meal !=null ? "Update" : "Insert"}"/>
    <input type="button" onclick="history.back();" value="Cancel"/>
</form>
</body>
</html>
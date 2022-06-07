<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table {
            border-collapse: collapse;
            border: 1px solid black;
        }

        tr, td {
            border: 1px solid black;
            padding: 5px 10px 5px 10px;
        }

        .tr-header {
            font-weight: bold;
            text-align: center;
        }

        .tr-font-red {
            color: red;
        }

        .tr-font-green {
            color: green;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h4><a href="?action=insert">Add Meal</a></h4>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table class="mealsList">
    <tr class="tr-header">
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
    </tr>
    <jsp:useBean id="mealsList" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${mealsList}">
        <tr class=${meal.isExcess() ? "tr-font-red" : "tr-font-green"}>
            <td>${timeUtil.formatDateTime(meal.getDateTime(), "yyyy-MM-dd HH:mm")}</td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>
            <td><a href="?action=update&id=${meal.getId()}">Update</a></td>
            <td><a href="?action=delete&id=${meal.getId()}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
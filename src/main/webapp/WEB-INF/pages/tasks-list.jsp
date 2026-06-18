<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" xml:lang="en">
<head>
    <title>List of Tasks</title>
    <style>
        <%@include file="../styles/main.css"%>
    </style>
</head>
<body>
    <%@include file="header.html"%>
    <%-- Повідомлення про успіх --%>
    <%
        String successMessage = (String) session.getAttribute("success");
        if (successMessage != null && !successMessage.isEmpty()) {
    %>
    <div class="alert alert-success" id="successAlert">
        <button type="button" class="close-alert" onclick="closeAlert('successAlert')">&times;</button>
        <strong>Success!</strong> <%= successMessage %>
    </div>
    <%
            session.removeAttribute("success");
        }
    %>
    <%-- Повідомлення про помилку--%>
    <%
        String errorMessage = (String) session.getAttribute("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
    %>
    <div class="alert alert-danger" id="errorAlert">
        <button type="button" class="close-alert" onclick="closeAlert('errorAlert')">&times;</button>
        <strong>Error!</strong> <%= errorMessage %>
    </div>
    <%
            session.removeAttribute("error");
        }
    %>
    <table>
        <tr>
            <th>No.</th>
            <th>Name</th>
            <th>Priority</th>
            <th colspan="3">Operation</th>
        </tr>
        <%
            List<Task> tasks = (List<Task>) request.getAttribute("tasks");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
        %>
        <tr>
            <td><%= i + 1 %>
            </td>
            <td><%= task.getTitle() %>
            </td>
            <td><%= task.getPriority() %>
            </td>
            <td>
                <a href="read-task?id=<%= task.getId() %>">Read</a>
            </td>
            <td>
                <a href="edit-task?id=<%= task.getId() %>">Edit</a>
            </td>
            <td>
                <a href="delete-task?id=<%= task.getId() %>">Delete</a>
            </td>
        </tr>
        <%
            }
        %>
    </table>
    <div style="margin-top: 30px;">
        <a href="${pageContext.request.contextPath}/create-task" class="btn btn-success">Create New Task</a>
    </div>
</body>
</html>

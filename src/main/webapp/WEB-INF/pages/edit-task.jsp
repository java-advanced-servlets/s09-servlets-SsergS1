<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page import="com.softserve.itacademy.model.Priority" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en" xml:lang="en">
<head>
    <title>Edit existing Task</title>
    <style>
        <%@include file="../styles/main.css"%>
    </style>
</head>
<body>
<%@include file="header.html" %>
<div class="form-container">
    <h2>Update Task</h2>

    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <%
        Task task = (Task) request.getAttribute("task");
        String title = (String) request.getAttribute("title");
        Priority priority = (Priority) request.getAttribute("priority");

        String displayTitle = (title != null) ? title : (task != null ? task.getTitle() : "");
        Priority displayPriority = (priority != null) ? priority : (task != null ? task.getPriority() : null);
    %>

    <form action="${pageContext.request.contextPath}/edit-task" method="post">
        <div class="form-group">
            <label for="id">Id:</label>
            <input type="hidden" id="id" name="id" value="<%= task != null ? task.getId() : "" %>">
            <input type="text" class="form-control" value="<%= task != null ? task.getId() : "" %>" disabled>

            <label for="title">Title:</label>
            <input type="text" id="title" name="title" class="form-control"
                   value="<%= displayTitle %>"
                   placeholder="Enter task title" required>
        </div>

        <div class="form-group">
            <label for="priority">Priority:</label>
            <select id="priority" name="priority" class="form-select" required>
                <option value="" disabled <%= displayPriority == null ? "selected" : "" %>>Select priority</option>
                <%
                    for (Priority p : Priority.values()) {
                        boolean isSelected = displayPriority != null && displayPriority.equals(p);
                %>
                <option value="<%= p %>" <%= isSelected ? "selected" : "" %>><%= p %></option>
                <% } %>
            </select>
        </div>

        <button type="submit" class="btn btn-success">Update Task</button>
        <a href="${pageContext.request.contextPath}/tasks-list" class="btn">Cancel</a>
    </form>
</div>
</body>
</html>
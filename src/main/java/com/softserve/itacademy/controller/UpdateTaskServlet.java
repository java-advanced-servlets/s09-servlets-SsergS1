package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// URL path for accessing this servlet
@WebServlet("/edit-task")
public class UpdateTaskServlet extends HttpServlet {

    private TaskRepository taskRepository;

    @Override
    public void init() {
        taskRepository = TaskRepository.getTaskRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            request.setAttribute("message", "Task ID is missing!");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Task task = taskRepository.read(id);

            if (task != null) {
                request.setAttribute("task", task);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/pages/edit-task.jsp");
                requestDispatcher.forward(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("message", "Task with ID '" + id + "' not found!");
                request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("message", "Invalid Task ID format!");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String title = request.getParameter("title");
        String priorityStr = request.getParameter("priority");

        if (title != null && !title.isEmpty() && priorityStr != null && !priorityStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Priority priority = Priority.valueOf(priorityStr);

                Task existingTask = taskRepository.read(id);
                if (existingTask == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found");
                    return;
                }

                existingTask.setTitle(title);
                existingTask.setPriority(priority);

                boolean updated = taskRepository.update(existingTask);

                if (updated) {
                    response.sendRedirect("/tasks-list");
                } else {
                    request.setAttribute("error", "Task with a given name already exists!");
                    request.setAttribute("title", title);
                    request.setAttribute("priority", priority);
                    request.setAttribute("task", existingTask);

                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/pages/edit-task.jsp");
                    requestDispatcher.forward(request, response);
                }

            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid task ID");
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/pages/edit-task.jsp");
                requestDispatcher.forward(request, response);
            }
        } else {
            request.setAttribute("error", "Title and priority must not be empty!");

            try {
                int id = Integer.parseInt(idStr);
                Task existingTask = taskRepository.read(id);
                request.setAttribute("task", existingTask);
            } catch (NumberFormatException e) {}

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/pages/edit-task.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
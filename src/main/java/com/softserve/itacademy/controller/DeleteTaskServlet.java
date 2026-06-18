package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


// Specifies the URL path for this servlet
@WebServlet("/delete-task")
public class DeleteTaskServlet extends HttpServlet {

    private TaskRepository taskRepository;

    @Override
    public void init() {
        taskRepository = TaskRepository.getTaskRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            request.getSession().setAttribute("error", "Task ID is required!");
            response.sendRedirect(request.getContextPath() + "/tasks-list");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            if (taskRepository.read(id) == null) {
                request.getSession().setAttribute("error", "Task with ID " + id + " not found!");
                response.sendRedirect(request.getContextPath() + "/tasks-list");
                return;
            }

            boolean deleted = taskRepository.delete(id);

            if (deleted) {
                request.getSession().setAttribute("success", "Task successfully deleted!");
            } else {
                request.getSession().setAttribute("error", "Failed to delete task!");
            }

            response.sendRedirect(request.getContextPath() + "/tasks-list");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid task ID format!");
            response.sendRedirect(request.getContextPath() + "/tasks-list");
        }
    }
}
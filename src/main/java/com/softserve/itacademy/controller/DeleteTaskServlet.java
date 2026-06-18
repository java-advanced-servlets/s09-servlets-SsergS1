package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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

        // Проверка наличия ID
        if (idParam == null || idParam.isEmpty()) {
            request.setAttribute("message", "Task ID is missing!");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }

        try {
            int id = Integer.parseInt(idParam);

            // Проверяем, существует ли задача
            if (taskRepository.read(id) == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                request.setAttribute("message", "Task with ID '" + id + "' not found!");
                request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
                return;
            }

            boolean deleted = taskRepository.delete(id);

            HttpSession session = request.getSession(false);
            if (session != null) {
                if (deleted) {
                    session.setAttribute("success", "Task successfully deleted!");
                } else {
                    session.setAttribute("error", "Failed to delete task!");
                }
            }

            response.sendRedirect(request.getContextPath() + "/tasks-list");

        } catch (NumberFormatException e) {
            request.setAttribute("message", "Invalid Task ID format!");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        }
    }
}
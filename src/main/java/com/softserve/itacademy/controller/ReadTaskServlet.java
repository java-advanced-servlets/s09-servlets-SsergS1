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

@WebServlet("/read-task")
public class ReadTaskServlet extends HttpServlet {

    private TaskRepository taskRepository;

    @Override
    public void init() {
        taskRepository = TaskRepository.getTaskRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Task task = taskRepository.read(id);
                if (task != null) {
                    request.setAttribute("task", task);
                    request.getRequestDispatcher("/WEB-INF/pages/read-task.jsp").forward(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    request.setAttribute("message", "Task with ID '" + id + "' not found!");
                    request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("message", "Invalid Task ID format!");
                request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("message", "Task ID is missing!");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        }
    }
}
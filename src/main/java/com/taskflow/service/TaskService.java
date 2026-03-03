package com.taskflow.service;

import com.taskflow.model.dto.request.CreateTaskRequest;
import com.taskflow.model.dto.request.UpdateTaskStatusRequest;
import com.taskflow.model.dto.response.TaskResponse;
import java.util.List;

public interface TaskService {
    TaskResponse createTask(CreateTaskRequest request);
    TaskResponse getTaskById(Long id);
    List<TaskResponse> getAllTasks();
    List<TaskResponse> getTasksByStatus(String status);
    TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest request);
    void deleteTask(Long id);
}
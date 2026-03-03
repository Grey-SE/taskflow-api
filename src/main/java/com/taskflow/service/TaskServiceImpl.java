package com.taskflow.service;

import com.taskflow.exception.BadRequestException;
import com.taskflow.exception.NotFoundException;
import com.taskflow.model.dto.request.CreateTaskRequest;
import com.taskflow.model.dto.request.UpdateTaskStatusRequest;
import com.taskflow.model.dto.response.TaskResponse;
import com.taskflow.model.entity.Task;
import com.taskflow.model.entity.User;
import com.taskflow.model.enums.TaskStatus;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {

        // Verify the assigned user actually exists
        User assignedTo = userRepository.findById(request.getAssignedToId())
                .orElseThrow(() -> new NotFoundException(
                        "User", request.getAssignedToId()
                ));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .assignedTo(assignedTo)
                .build();
        // Note: status and createdAt are set by @PrePersist

        Task saved = taskRepository.save(task);
        return TaskResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task", id));
        return TaskResponse.from(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByStatus(String statusString) {
        TaskStatus status;

        // Safely parse the string into enum
        try {
            status = TaskStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    "Invalid status. Valid values: TODO, IN_PROGRESS, DONE"
            );
        }

        return taskRepository.findByStatus(status)
                .stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Override
    public TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task", id));

        // Business rule — same as Phase 1
        if (task.getStatus() == TaskStatus.DONE &&
                request.getStatus() == TaskStatus.TODO) {
            throw new BadRequestException(
                    "Cannot move a completed task back to TODO"
            );
        }

        task.setStatus(request.getStatus());
        // No need to call save() — @Transactional detects the change
        // and flushes automatically. This is called "dirty checking."
        return TaskResponse.from(task);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task", id);
        }
        taskRepository.deleteById(id);
    }
}
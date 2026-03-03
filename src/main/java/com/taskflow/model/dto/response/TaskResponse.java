package com.taskflow.model.dto.response;

import com.taskflow.model.entity.Task;
import com.taskflow.model.enums.TaskStatus;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private String assignedToName;   // we show the name, not the whole User object
    private Long assignedToId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TaskResponse from(Task task) {
        TaskResponse response = new TaskResponse();
        response.id             = task.getId();
        response.title          = task.getTitle();
        response.description    = task.getDescription();
        response.status         = task.getStatus();
        response.createdAt      = task.getCreatedAt();
        response.updatedAt      = task.getUpdatedAt();

        if (task.getAssignedTo() != null) {
            response.assignedToName = task.getAssignedTo().getName();
            response.assignedToId   = task.getAssignedTo().getId();
        }

        return response;
    }
}
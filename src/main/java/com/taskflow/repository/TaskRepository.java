package com.taskflow.repository;

import com.taskflow.model.entity.Task;
import com.taskflow.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Spring generates the SQL for these automatically
    // just from the method name — no SQL needed
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByAssignedToIdAndStatus(Long userId, TaskStatus status);
}
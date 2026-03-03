package com.taskflow.model.dto.response;

import com.taskflow.model.entity.User;
import com.taskflow.model.enums.UserRole;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;

    // Notice: NO password field — never expose passwords in responses

    // Static factory method — clean way to convert Entity → DTO
    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.id        = user.getId();
        response.name      = user.getName();
        response.email     = user.getEmail();
        response.role      = user.getRole();
        response.createdAt = user.getCreatedAt();
        return response;
    }
}
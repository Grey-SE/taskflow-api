package com.taskflow.service;

import com.taskflow.model.dto.request.RegisterUserRequest;
import com.taskflow.model.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse registerUser(RegisterUserRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getAllUsers();
}
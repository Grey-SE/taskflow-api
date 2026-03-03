package com.taskflow.service;

import com.taskflow.model.dto.request.LoginRequest;
import com.taskflow.model.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
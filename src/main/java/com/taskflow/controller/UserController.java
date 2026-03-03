package com.taskflow.controller;

import com.taskflow.model.dto.request.RegisterUserRequest;
import com.taskflow.model.dto.response.UserResponse;
import com.taskflow.model.entity.User;
import com.taskflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController             // handles HTTP, returns JSON automatically
@RequestMapping("/api/users")  // base URL for all methods in this class
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/users/register
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterUserRequest request) {

        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // GET /api/users/email/alice@taskflow.com
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // GET /api/users/me
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal User currentUser) {
        // Spring injects the authenticated user from SecurityContext
        return ResponseEntity.ok(UserResponse.from(currentUser));
    }
}
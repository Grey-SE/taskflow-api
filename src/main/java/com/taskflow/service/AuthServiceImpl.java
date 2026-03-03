package com.taskflow.service;

import com.taskflow.exception.BadRequestException;
import com.taskflow.model.dto.request.LoginRequest;
import com.taskflow.model.dto.response.AuthResponse;
import com.taskflow.model.entity.User;
import com.taskflow.repository.UserRepository;
import com.taskflow.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(LoginRequest request) {

        // ── Step 1: Find user by email ────────────────────────
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        // IMPORTANT: don't say "email not found"
                        // Always use a vague message to prevent email enumeration
                        new BadRequestException("Invalid email or password")
                );

        // ── Step 2: Verify password against stored hash ───────
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Same vague message — don't reveal which field was wrong
            throw new BadRequestException("Invalid email or password");
        }

        // ── Step 3: Generate JWT ──────────────────────────────
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        // ── Step 4: Return token + user info ──────────────────
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name(),
                86400000L   // 24 hours in ms
        );
    }
}
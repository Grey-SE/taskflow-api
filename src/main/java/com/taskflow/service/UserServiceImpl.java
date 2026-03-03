package com.taskflow.service;

import com.taskflow.exception.BadRequestException;
import com.taskflow.exception.ConflictException;
import com.taskflow.exception.NotFoundException;
import com.taskflow.model.dto.request.RegisterUserRequest;
import com.taskflow.model.dto.response.UserResponse;
import com.taskflow.model.entity.User;
import com.taskflow.model.enums.UserRole;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service                    // Spring manages this class
@RequiredArgsConstructor    // Lombok generates constructor injection
@Transactional              // every method runs in a DB transaction
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse registerUser(RegisterUserRequest request) {

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Name cannot be empty");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(
                    "Email already registered: " + request.getEmail()
            );
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword()) // plain text for now
                .role(UserRole.USER)              // default role on register
                .build();

        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)   // optimization for read operations
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        return UserResponse.from(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User", email));
        return UserResponse.from(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)   // method reference — converts each entity
                .toList();
    }
}
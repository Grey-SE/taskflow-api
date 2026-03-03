package com.taskflow.model.entity;

import com.taskflow.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")                    // maps to "users" table in MySQL
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder                                  // enables builder pattern
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)   // unique = no duplicate emails
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)          // stores "ADMIN"/"USER" not 0/1
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Relationship — one user has many tasks
    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @PrePersist                           // runs automatically before saving
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
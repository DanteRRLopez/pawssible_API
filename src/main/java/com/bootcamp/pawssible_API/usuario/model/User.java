package com.bootcamp.pawssible_API.usuario.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name", nullable=false, length=80)
    private String firstName;

    @Column(name="last_name", nullable=false, length=80)
    private String lastName;

    @Column(nullable=false, unique=true, length=120)
    private String email;

    @Column(nullable=false, length=255)
    private String password; // almacenaremos hash

    @Column(nullable=false)
    private boolean enabled = true;

    @ManyToOne(optional = false)
    @JoinColumn(name="role_id", nullable=false)
    private Role role;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}

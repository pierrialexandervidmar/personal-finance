package com.system.personalfinance.dtos.user;

import java.time.Instant;

public class UserResponse{

    private Long id;
    private String name;
    private String email;
    private Instant createdAt;

    public UserResponse(Long id, String name, String email, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

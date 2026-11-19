package com.example.DeliveryService.dto;

import com.example.DeliveryService.model.User;

public class UserResponse {
    private Long id;
    private String username;
    private String role;
    private boolean available;
    private boolean blocked;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.available = user.isAvailable();
        this.blocked = user.isBlocked();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public boolean isAvailable() { return available; }
    public boolean isBlocked() { return blocked; }
}
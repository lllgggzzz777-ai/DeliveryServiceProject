package com.example.DeliveryService.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role; // CLIENT, COURIER, ADMIN

    // Поля для курьеров (онлайн/офлайн)
    private boolean available = false;

    // Поле для админки (блокировка пользователя)
    private boolean blocked = false;

    // Пустой конструктор для JPA 
    public User() {}

    // Конструктор для быстрого создания, используется в UserService.register
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.available = false;
        this.blocked = false;
    }

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getUsername() { 
        return username; 
    }
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }

    public String getRole() { 
        return role; 
    }
    public void setRole(String role) { 
        this.role = role; 
    }

    public boolean isAvailable() { 
        return available; 
    }
    public void setAvailable(boolean available) { 
        this.available = available; 
    }

    public boolean isBlocked() { 
        return blocked; 
    }
    public void setBlocked(boolean blocked) { 
        this.blocked = blocked; 
    }
}
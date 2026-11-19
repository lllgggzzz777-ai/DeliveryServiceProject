package com.example.DeliveryService.controller;

import com.example.DeliveryService.dto.UserResponse;
import com.example.DeliveryService.model.User;
import com.example.DeliveryService.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Регистрация пользователя
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        // Проверка: не занят ли уже такой логин
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Пользователь с таким именем уже существует"));
        }

        // По умолчанию, если роль не передана, делаем клиента
        String role = (request.getRole() != null) ? request.getRole() : "CLIENT";
        
        User savedUser = userService.register(request.getUsername(), request.getPassword(), role);
        
        return ResponseEntity.ok(new UserResponse(savedUser));
    }

    // Вход (имитация без токена)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        Optional<User> userOpt = userService.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Пользователь не найден"));
        }

        User user = userOpt.get();
        // Простая проверка пароля
        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Неверный пароль"));
        }

        return ResponseEntity.ok(new UserResponse(user));
    }

    // Выход (заглушка)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Выход выполнен успешно"));
    }

    // Обновление токена (заглушка)
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh() {
        return ResponseEntity.ok(Map.of("message", "В проекте без токенов обновление не требуется"));
    }

    // Запрос сброса пароля (заглушка)
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword() {
        return ResponseEntity.ok(Map.of("message", "Инструкция по сбросу пароля отправлена (заглушка)"));
    }

    // Сброс пароля по токену (заглушка)
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword() {
        return ResponseEntity.ok(Map.of("message", "Пароль успешно сброшен (заглушка)"));
    }
}
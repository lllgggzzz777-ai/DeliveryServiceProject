package com.example.DeliveryService.controller;

import com.example.DeliveryService.model.User;
import com.example.DeliveryService.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    // Внедряем сервис через конструктор (как в "Допустимом коде")
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 1. Регистрация пользователя
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        // Проверка: не занят ли уже такой логин
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
        }

        // По умолчанию, если роль не передана, делаем клиента
        String role = (request.getRole() != null) ? request.getRole() : "CLIENT";
        
        User savedUser = userService.register(request.getUsername(), request.getPassword(), role);
        return ResponseEntity.ok(savedUser);
    }

    // 2. Вход (имитация без токена)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        Optional<User> userOpt = userService.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }

        User user = userOpt.get();
        // Простая проверка пароля
        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.badRequest().body("Неверный пароль");
        }

        // В реальном проекте здесь возвращали бы JWT токен. 
        // Для локальной разработки просто возвращаем данные пользователя.
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Успешный вход");
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        
        return ResponseEntity.ok(response);
    }

    // 3. Выход (заглушка, так как нет сессий и токенов)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Выход выполнен успешно"));
    }

    // 4. Обновление токена (заглушка)
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh() {
        return ResponseEntity.ok(Map.of("message", "В проекте без токенов обновление не требуется"));
    }

    // 5. Запрос сброса пароля (заглушка)
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword() {
        return ResponseEntity.ok(Map.of("message", "Инструкция по сбросу пароля отправлена (заглушка)"));
    }

    // 6. Сброс пароля по токену (заглушка)
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword() {
        return ResponseEntity.ok(Map.of("message", "Пароль успешно сброшен (заглушка)"));
    }
}
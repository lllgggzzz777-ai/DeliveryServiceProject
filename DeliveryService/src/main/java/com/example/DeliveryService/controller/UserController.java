package com.example.DeliveryService.controller;

import com.example.DeliveryService.dto.UserResponse;
import com.example.DeliveryService.model.User;
import com.example.DeliveryService.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Получить профиль текущего пользователя
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            return ResponseEntity.ok(new UserResponse(user)); // Оборачиваем в DTO, чтобы скрыть пароль
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Обновить профиль 
    @PatchMapping("/me")
    public ResponseEntity<?> updateMyProfile(@RequestParam Long userId, @RequestBody User request) {
        try {
            User updatedUser = userService.updateProfile(userId, request);
            return ResponseEntity.ok(new UserResponse(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Смена пароля
    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(@RequestParam Long userId, @RequestBody Map<String, String> passwords) {
        try {
            String oldPassword = passwords.get("oldPassword");
            String newPassword = passwords.get("newPassword");

            if (oldPassword == null || newPassword == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Необходимо передать oldPassword и newPassword"));
            }

            userService.changePassword(userId, oldPassword, newPassword);
            return ResponseEntity.ok(Map.of("message", "Пароль успешно изменен"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

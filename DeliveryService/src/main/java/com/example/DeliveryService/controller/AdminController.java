package com.example.DeliveryService.controller;

import com.example.DeliveryService.model.DeliveryOrder;
import com.example.DeliveryService.model.User;
import com.example.DeliveryService.service.AdminService;
import com.example.DeliveryService.service.OrderService;
import com.example.DeliveryService.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final OrderService orderService;

    // Внедрение зависимостей через конструктор
    public AdminController(AdminService adminService, 
                           UserService userService, 
                           OrderService orderService) {
        this.adminService = adminService;
        this.userService = userService;
        this.orderService = orderService;
    }

    // Сводная статистика (Дашборд) - ЛОГИКА ТЕПЕРЬ В СЕРВИСЕ
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // Все заказы (с возможностью фильтрации по статусу)
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(@RequestParam(required = false) String status) {
        List<DeliveryOrder> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderService.getOrdersByStatus(status);
        } else {
            orders = orderService.getAllOrders();
        }
        return ResponseEntity.ok(orders);
    }

    // Изменение статуса любого заказа администратором
    @PatchMapping("/orders/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            DeliveryOrder updated = orderService.updateStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //  Все пользователи
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Блокировка/разблокировка пользователя
    @PatchMapping("/users/{id}/block")
    public ResponseEntity<?> blockUser(@PathVariable Long id, @RequestParam boolean blocked) {
        try {
            User updated = userService.blockUser(id, blocked);
            String action = blocked ? "заблокирован" : "разблокирован";
            return ResponseEntity.ok(Map.of(
                    "message", "Пользователь " + action,
                    "user", updated
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 6. Управление курьерами (получить всех, с их статусами)
    @GetMapping("/couriers")
    public List<User> getAllCouriers() {
        return userService.getCouriers();
    }
}
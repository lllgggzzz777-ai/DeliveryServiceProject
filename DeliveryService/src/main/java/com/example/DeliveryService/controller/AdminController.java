package com.example.DeliveryService.controller;

import com.example.DeliveryService.model.DeliveryOrder;
import com.example.DeliveryService.model.User;
import com.example.DeliveryService.service.OrderService;
import com.example.DeliveryService.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final OrderService orderService;

    // Внедрение зависимостей через конструктор
    public AdminController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    // 1. Сводная статистика (Дашборд)
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        Map<String, Object> stats = new HashMap<>();
        
        // Статистика по заказам
        stats.put("totalOrders", orderService.countAll());
        stats.put("createdOrders", orderService.countByStatus("CREATED"));
        stats.put("assignedOrders", orderService.countByStatus("ASSIGNED"));
        stats.put("inTransitOrders", orderService.countByStatus("IN_TRANSIT"));
        stats.put("deliveredOrders", orderService.countByStatus("DELIVERED"));
        stats.put("cancelledOrders", orderService.countByStatus("CANCELLED"));

        // Статистика по пользователям
        List<User> allUsers = userService.getAllUsers();
        stats.put("totalUsers", allUsers.size());
        
        long couriersCount = allUsers.stream().filter(u -> "COURIER".equals(u.getRole())).count();
        long clientsCount = allUsers.stream().filter(u -> "CLIENT".equals(u.getRole())).count();
        
        stats.put("totalCouriers", couriersCount);
        stats.put("totalClients", clientsCount);

        return ResponseEntity.ok(stats);
    }

    // 2. Все заказы (с возможностью фильтрации по статусу)
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

    // 3. Изменение статуса любого заказа администратором
    @PatchMapping("/orders/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            DeliveryOrder updated = orderService.updateStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 4. Все пользователи
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 5. Блокировка/разблокировка пользователя
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
package com.example.DeliveryService.controller;

import com.example.DeliveryService.model.DeliveryOrder;
import com.example.DeliveryService.model.User;
import com.example.DeliveryService.service.OrderService;
import com.example.DeliveryService.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/couriers")
public class CourierController {

    private final UserService userService;
    private final OrderService orderService;

    // Внедрение зависимостей через конструктор
    public CourierController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    // 1. Список всех курьеров
    @GetMapping
    public List<User> getAllCouriers() {
        return userService.getCouriers();
    }

    //  Профиль конкретного курьера
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourierProfile(@PathVariable Long id) {
        try {
            User courier = userService.findById(id);
            if (!"COURIER".equals(courier.getRole())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Пользователь с ID " + id + " не является курьером"));
            }
            return ResponseEntity.ok(courier);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //  Смена статуса курьера (онлайн/офлайн)
    @PatchMapping("/{id}/availability")
    public ResponseEntity<?> changeAvailability(@PathVariable Long id, @RequestParam boolean available) {
        try {
            User updatedCourier = userService.setAvailability(id, available);
            return ResponseEntity.ok(updatedCourier);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //  Активные заказы курьера
    @GetMapping("/{id}/orders")
    public ResponseEntity<?> getCourierOrders(@PathVariable Long id) {
        try {
            // Проверка, что это действительно курьер
            User courier = userService.findById(id);
            if (!"COURIER".equals(courier.getRole())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Пользователь не является курьером"));
            }

            List<DeliveryOrder> orders = orderService.getOrdersByCourier(id);
            
            // Фильтрация: оставляем только те, что еще не доставлены и не отменены
            List<DeliveryOrder> activeOrders = orders.stream()
                    .filter(o -> !"DELIVERED".equals(o.getStatus()) && !"CANCELLED".equals(o.getStatus()))
                    .toList();

            return ResponseEntity.ok(activeOrders);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //  Статистика заработка курьера
    @GetMapping("/{id}/earnings")
    public ResponseEntity<?> getCourierEarnings(@PathVariable Long id) {
        try {
            User courier = userService.findById(id);
            if (!"COURIER".equals(courier.getRole())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Пользователь не является курьером"));
            }

            double totalEarnings = orderService.getEarnings(id);
            
            return ResponseEntity.ok(Map.of(
                    "courierId", id,
                    "courierName", courier.getUsername(),
                    "totalEarnings", totalEarnings
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
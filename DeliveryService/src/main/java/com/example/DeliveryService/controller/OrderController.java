package com.example.DeliveryService.controller;

import com.example.DeliveryService.model.DeliveryOrder;
import com.example.DeliveryService.model.Review;
import com.example.DeliveryService.service.OrderService;
import com.example.DeliveryService.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ReviewService reviewService; // Добавили сервис отзывов

    // Внедрение зависимостей через конструктор
    public OrderController(OrderService orderService, ReviewService reviewService) {
        this.orderService = orderService;
        this.reviewService = reviewService;
    }

    // Создать заказ
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody DeliveryOrder order) {
        try {
            DeliveryOrder created = orderService.createOrder(order);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Список заказов (с возможностью фильтрации по клиенту или курьеру)
    @GetMapping
    public List<DeliveryOrder> getOrders(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long courierId) {
        
        if (clientId != null) {
            return orderService.getOrdersByClient(clientId);
        }
        if (courierId != null) {
            return orderService.getOrdersByCourier(courierId);
        }
        return orderService.getAllOrders();
    }

    // Детали заказа
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long id) {
        try {
            DeliveryOrder order = orderService.getOrderById(id);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Обновить заказ (до принятия курьером)
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody DeliveryOrder request) {
        try {
            DeliveryOrder updated = orderService.updateOrder(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Отменить заказ (удаление или пометка как отмененный)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.cancelOrder(id, "Удален клиентом");
            return ResponseEntity.ok(Map.of("message", "Заказ отменен"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Отмена с указанием причины
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrderWithReason(@PathVariable Long id, @RequestParam String reason) {
        try {
            DeliveryOrder cancelled = orderService.cancelOrder(id, reason);
            return ResponseEntity.ok(cancelled);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Текущий статус заказа
    @GetMapping("/{id}/status")
    public ResponseEntity<?> getOrderStatus(@PathVariable Long id) {
        try {
            DeliveryOrder order = orderService.getOrderById(id);
            return ResponseEntity.ok(Map.of("status", order.getStatus()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // История изменений статуса
    @GetMapping("/{id}/history")
    public ResponseEntity<?> getOrderHistory(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getHistory(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Повторить заказ (создать копию)
    @PostMapping("/{id}/repeat")
    public ResponseEntity<?> repeatOrder(@PathVariable Long id) {
        try {
            DeliveryOrder repeated = orderService.repeatOrder(id);
            return ResponseEntity.ok(repeated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Назначить курьера на заказ (вспомогательный эндпоинт)
    @PatchMapping("/{id}/assign/{courierId}")
    public ResponseEntity<?> assignCourier(@PathVariable Long id, @PathVariable Long courierId) {
        try {
            DeliveryOrder assigned = orderService.assignCourier(id, courierId);
            return ResponseEntity.ok(assigned);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Обновить статус (вспомогательный эндпоинт для курьера)
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            DeliveryOrder updated = orderService.updateStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Оставить отзыв о заказе
    @PostMapping("/{id}/review")
    public ResponseEntity<?> addReview(@PathVariable Long id, @RequestBody Review request) {
        try {
            Review review = reviewService.createReview(id, request.getRating(), request.getComment());
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Получить отзыв о заказе
    @GetMapping("/{id}/review")
    public ResponseEntity<?> getReview(@PathVariable Long id) {
        try {
            Review review = reviewService.getReviewByOrder(id);
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
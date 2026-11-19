package com.example.DeliveryService.controller;

import com.example.DeliveryService.dto.OrderRequest;
import com.example.DeliveryService.dto.OrderResponse;
import com.example.DeliveryService.model.DeliveryOrder;
import com.example.DeliveryService.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Создать заказ 
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        try {
            OrderResponse response = orderService.createOrder(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Список заказов 
    @GetMapping
    public List<OrderResponse> getOrders(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long courierId) {
        
        List<DeliveryOrder> orders;
        if (clientId != null) {
            orders = orderService.getOrdersByClient(clientId);
        } else if (courierId != null) {
            orders = orderService.getOrdersByCourier(courierId);
        } else {
            orders = orderService.getAllOrders();
        }
        
        // Превращаем список сущностей в список DTO
        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    // Детали заказа 
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long id) {
        try {
            DeliveryOrder order = orderService.getOrderById(id);
            return ResponseEntity.ok(new OrderResponse(order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Обновить заказ 
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderRequest request) {
        try {
            DeliveryOrder updated = orderService.updateOrder(id, request);
            return ResponseEntity.ok(new OrderResponse(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Отменить заказ (удаление)
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
            return ResponseEntity.ok(new OrderResponse(cancelled));
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
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
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

    // Повторить заказ
    @PostMapping("/{id}/repeat")
    public ResponseEntity<?> repeatOrder(@PathVariable Long id) {
        try {
            DeliveryOrder repeated = orderService.repeatOrder(id);
            return ResponseEntity.ok(new OrderResponse(repeated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Назначить курьера
    @PatchMapping("/{id}/assign/{courierId}")
    public ResponseEntity<?> assignCourier(@PathVariable Long id, @PathVariable Long courierId) {
        try {
            DeliveryOrder assigned = orderService.assignCourier(id, courierId);
            return ResponseEntity.ok(new OrderResponse(assigned));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Обновить статус 
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            DeliveryOrder updated = orderService.updateStatus(id, status);
            return ResponseEntity.ok(new OrderResponse(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
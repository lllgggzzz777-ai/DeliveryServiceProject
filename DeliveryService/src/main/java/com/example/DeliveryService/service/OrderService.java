package com.example.DeliveryService.service;

import com.example.DeliveryService.dto.OrderRequest;
import com.example.DeliveryService.dto.OrderResponse;
import com.example.DeliveryService.model.DeliveryOrder;
import com.example.DeliveryService.model.User;
import com.example.DeliveryService.repository.OrderRepository;
import com.example.DeliveryService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // Внедрение зависимостей через конструктор
    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // Создать заказ 
    public OrderResponse createOrder(OrderRequest request) {
        if (request.getPrice() <= 0) {
            throw new RuntimeException("Цена должна быть больше 0");
        }
        
        // Ищем клиента по ID
        User client = userRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Клиент с ID " + request.getClientId() + " не найден"));

     
        DeliveryOrder order = new DeliveryOrder();
        order.setPickupAddress(request.getPickupAddress());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setPrice(request.getPrice());
        order.setClient(client);
        order.setStatus("CREATED");

        DeliveryOrder savedOrder = orderRepository.save(order);
        
        // Возвращаем DTO 
        return new OrderResponse(savedOrder);
    }

    // Получить все заказы
    public List<DeliveryOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    // Получить заказы конкретного клиента
    public List<DeliveryOrder> getOrdersByClient(Long clientId) {
        return orderRepository.findByClientId(clientId);
    }

    // Получить заказы конкретного курьера
    public List<DeliveryOrder> getOrdersByCourier(Long courierId) {
        return orderRepository.findByCourierId(courierId);
    }

    // Получить заказ по ID
    public DeliveryOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ с ID " + id + " не найден"));
    }

    // Обновить заказ (Принимает DTO)
    public DeliveryOrder updateOrder(Long id, OrderRequest request) {
        DeliveryOrder order = getOrderById(id);
        
        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Нельзя редактировать заказ после его принятия курьером");
        }

        if (request.getPickupAddress() != null && !request.getPickupAddress().isEmpty()) {
            order.setPickupAddress(request.getPickupAddress());
        }
        if (request.getDeliveryAddress() != null && !request.getDeliveryAddress().isEmpty()) {
            order.setDeliveryAddress(request.getDeliveryAddress());
        }
        if (request.getPrice() > 0) {
            order.setPrice(request.getPrice());
        }

        return orderRepository.save(order);
    }

    // Отменить заказ
    public DeliveryOrder cancelOrder(Long id, String reason) {
        DeliveryOrder order = getOrderById(id);

        if ("DELIVERED".equals(order.getStatus())) {
            throw new RuntimeException("Нельзя отменить уже доставленный заказ");
        }
        if ("CANCELLED".equals(order.getStatus())) {
            throw new RuntimeException("Заказ уже отменен");
        }

        order.setStatus("CANCELLED");
        order.setCancelled(true);
        order.setCancelReason(reason);

        return orderRepository.save(order);
    }

    // Обновить статус
    public DeliveryOrder updateStatus(Long id, String status) {
        DeliveryOrder order = getOrderById(id);

        if ("CANCELLED".equals(order.getStatus())) {
            throw new RuntimeException("Нельзя изменить статус отмененного заказа");
        }

        order.setStatus(status);
        return orderRepository.save(order);
    }

    // Назначить курьера
    public DeliveryOrder assignCourier(Long orderId, Long courierId) {
        DeliveryOrder order = getOrderById(orderId);

        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Курьера можно назначить только на новый заказ (статус CREATED)");
        }

        User courier = userRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Курьер с ID " + courierId + " не найден"));

        if (!"COURIER".equals(courier.getRole())) {
            throw new RuntimeException("Пользователь с ID " + courierId + " не является курьером");
        }

        order.setCourier(courier);
        order.setStatus("ASSIGNED");
        return orderRepository.save(order);
    }

    // Повторить заказ
    public DeliveryOrder repeatOrder(Long id) {
        DeliveryOrder oldOrder = getOrderById(id);

        DeliveryOrder newOrder = new DeliveryOrder(
                oldOrder.getPickupAddress(),
                oldOrder.getDeliveryAddress(),
                oldOrder.getPrice(),
                oldOrder.getClient()
        );
        
        return orderRepository.save(newOrder);
    }

    // Получить историю (заглушка)
    public List<Object> getHistory(Long id) {
        getOrderById(id);
        return java.util.Collections.emptyList();
    }

    // Статистика
    public long countAll() {
        return orderRepository.count();
    }

    public long countByStatus(String status) {
        return orderRepository.findAll().stream()
                .filter(o -> status.equals(o.getStatus()))
                .count();
    }

    //  Заработок курьера
    public double getEarnings(Long courierId) {
        List<DeliveryOrder> orders = orderRepository.findByCourierId(courierId);
        double total = 0;
        for (DeliveryOrder order : orders) {
            if ("DELIVERED".equals(order.getStatus())) {
                total += order.getPrice();
            }
        }
        return total;
    }

    // Получить заказы по статусу
    public List<DeliveryOrder> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}
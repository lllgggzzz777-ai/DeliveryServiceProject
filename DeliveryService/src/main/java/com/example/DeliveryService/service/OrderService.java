package com.example.DeliveryService.service;

import com.example.DeliveryService.model.DeliveryOrder;
import com.example.DeliveryService.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    // Внедрение репозитория через конструктор
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Создать заказ
    public DeliveryOrder createOrder(DeliveryOrder order) {
        if (order.getPrice() <= 0) {
            throw new RuntimeException("Цена должна быть больше 0");
        }
        if (order.getClientId() == null) {
            throw new RuntimeException("Не указан ID клиента");
        }
        // По умолчанию статус CREATED, если не передан
        if (order.getStatus() == null) {
            order.setStatus("CREATED");
        }
        return orderRepository.save(order);
    }

    // Получить все заказы
    public List<DeliveryOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    // Получить заказы конкретного клиента
    public List<DeliveryOrder> getOrdersByClient(Long clientId) {
        return orderRepository.findByClientId(clientId);
    }

    //  Получить заказы конкретного курьера
    public List<DeliveryOrder> getOrdersByCourier(Long courierId) {
        return orderRepository.findByCourierId(courierId);
    }

    //  Получить заказ по ID (с проверкой на существование)
    public DeliveryOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ с ID " + id + " не найден"));
    }

    // Обновить заказ (разрешено только пока статус CREATED)
    public DeliveryOrder updateOrder(Long id, DeliveryOrder request) {
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

    // Отменить заказ с указанием причины
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

    // Обновить статус заказа 
    public DeliveryOrder updateStatus(Long id, String status) {
        DeliveryOrder order = getOrderById(id);

        if ("CANCELLED".equals(order.getStatus())) {
            throw new RuntimeException("Нельзя изменить статус отмененного заказа");
        }

        order.setStatus(status);
        return orderRepository.save(order);
    }

    // Назначить курьера на заказ
    public DeliveryOrder assignCourier(Long orderId, Long courierId) {
        DeliveryOrder order = getOrderById(orderId);

        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Курьера можно назначить только на новый заказ (статус CREATED)");
        }

        order.setCourierId(courierId);
        order.setStatus("ASSIGNED");
        return orderRepository.save(order);
    }

    //  Повторить заказ (создать новый на основе старого)
    public DeliveryOrder repeatOrder(Long id) {
        DeliveryOrder oldOrder = getOrderById(id);

        DeliveryOrder newOrder = new DeliveryOrder(
                oldOrder.getPickupAddress(),
                oldOrder.getDeliveryAddress(),
                oldOrder.getPrice(),
                oldOrder.getClientId()
        );
        
        return orderRepository.save(newOrder);
    }

    // Получить историю изменений статуса
    public List<Object> getHistory(Long id) {
        // Проверяем, что заказ существует
        getOrderById(id);
        
        return java.util.Collections.emptyList();
    }

    // Статистика (для админки)
    public long countAll() {
        return orderRepository.count();
    }

    public long countByStatus(String status) {
        return orderRepository.findAll().stream()
                .filter(o -> status.equals(o.getStatus()))
                .count();
    }

    //  Расчет заработка курьера
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

    // Получить заказы по статусу (для админки)
    public List<DeliveryOrder> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}

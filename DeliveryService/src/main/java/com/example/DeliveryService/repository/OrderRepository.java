package com.example.DeliveryService.repository;

import com.example.DeliveryService.model.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<DeliveryOrder, Long> {

    // Найти все заказы конкретного клиента
    List<DeliveryOrder> findByClientId(Long clientId);

    // Найти все заказы конкретного курьера
    List<DeliveryOrder> findByCourierId(Long courierId);

    // Найти заказы по статусу 
    List<DeliveryOrder> findByStatus(String status);

    // Найти заказы клиента с определенным статусом
    List<DeliveryOrder> findByClientIdAndStatus(Long clientId, String status);
}
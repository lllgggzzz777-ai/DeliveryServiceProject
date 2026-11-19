package com.example.DeliveryService.service;

import com.example.DeliveryService.repository.OrderRepository;
import com.example.DeliveryService.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AdminService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalOrders", orderRepository.count());
        
        // Пример подсчета по ролям
        long couriers = userRepository.findByRole("COURIER").size();
        stats.put("totalCouriers", couriers);
        
        // Вы можете добавить сюда и другие подсчеты
        return stats;
    }
}
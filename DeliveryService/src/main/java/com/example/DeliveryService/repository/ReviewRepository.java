package com.example.DeliveryService.repository;

import com.example.DeliveryService.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // Найти отзыв по ID заказа. 
    Optional<Review> findByOrderId(Long orderId);
}
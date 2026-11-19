package com.example.DeliveryService.service;

import com.example.DeliveryService.model.DeliveryOrder;
import com.example.DeliveryService.model.Review;
import com.example.DeliveryService.repository.OrderRepository;
import com.example.DeliveryService.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    // Внедрение зависимостей через конструктор
    public ReviewService(ReviewRepository reviewRepository, OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
    }

    // Создать отзыв
    public Review createReview(Long orderId, int rating, String comment) {
        // Проверка валидности оценки
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Оценка должна быть от 1 до 5");
        }

        if (reviewRepository.findByOrderId(orderId).isPresent()) {
            throw new RuntimeException("Отзыв на этот заказ уже оставлен");
        }

        DeliveryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ с ID " + orderId + " не найден"));

        Review review = new Review(order, rating, comment);
        return reviewRepository.save(review);
    }

    // Получить отзыв по ID заказа
    public Review getReviewByOrder(Long orderId) {
        return reviewRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Отзыв на заказ с ID " + orderId + " не найден"));
    }
}
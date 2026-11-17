package com.example.DeliveryService.service;

import com.example.DeliveryService.model.Review;
import com.example.DeliveryService.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Создать отзыв
    public Review createReview(Long orderId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Оценка должна быть от 1 до 5");
        }
        // Проверяем, нет ли уже отзыва на этот заказ
        if (reviewRepository.findByOrderId(orderId).isPresent()) {
            throw new RuntimeException("Отзыв на этот заказ уже оставлен");
        }
        Review review = new Review(orderId, rating, comment);
        return reviewRepository.save(review);
    }

    // Получить отзыв по заказу
    public Review getReviewByOrder(Long orderId) {
        return reviewRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Отзыв на заказ с ID " + orderId + " не найден"));
    }
}
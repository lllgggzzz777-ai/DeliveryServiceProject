package com.example.DeliveryService.controller;

import com.example.DeliveryService.model.Review;
import com.example.DeliveryService.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    // Внедрение сервиса через конструктор
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Оставить отзыв о заказе
    @PostMapping("/orders/{id}/review")
    public ResponseEntity<?> addReview(@PathVariable Long id, @RequestBody Review request) {
        try {

            Review review = reviewService.createReview(id, request.getRating(), request.getComment());
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //Получить отзыв о заказе
    @GetMapping("/orders/{id}/review")
    public ResponseEntity<?> getReview(@PathVariable Long id) {
        try {
            Review review = reviewService.getReviewByOrder(id);
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
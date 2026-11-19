package com.example.DeliveryService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // СВЯЗЬ С ЗАКАЗОМ (Один заказ - один отзыв)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @JsonIgnore // Чтобы при запросе отзыва не подгружался весь объект заказа (защита от рекурсии)
    private DeliveryOrder order;

    private int rating;       // Оценка от 1 до 5
    private String comment;   // Текст отзыва
    private LocalDateTime createdAt;

    public Review() {}

    // Конструктор теперь принимает объект DeliveryOrder
    public Review(DeliveryOrder order, int rating, String comment) {
        this.order = order;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public DeliveryOrder getOrder() { 
        return order; 
    }
    public void setOrder(DeliveryOrder order) { 
        this.order = order; 
    }

    public int getRating() { 
        return rating; 
    }
    public void setRating(int rating) { 
        this.rating = rating; 
    }

    public String getComment() { 
        return comment; 
    }
    public void setComment(String comment) { 
        this.comment = comment; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
}
package com.example.DeliveryService.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class DeliveryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pickupAddress;   // Адрес забора
    private String deliveryAddress; // Адрес доставки
    private String status;          // CREATED, ASSIGNED, IN_TRANSIT, DELIVERED, CANCELLED
    private double price;           // Стоимость доставки

    // Один клиент может иметь много заказов 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id") 
    private User client;

    // Один курьер может выполнять много заказов 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id") 
    private User courier;

    // ПОЛЯ ОТМЕНЫ
    private boolean cancelled = false;
    private String cancelReason;

    // Пустой конструктор 
    public DeliveryOrder() {}

    // Конструктор для быстрого создания заказа 
    public DeliveryOrder(String pickupAddress, String deliveryAddress, double price, User client) {
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.price = price;
        this.client = client;
        this.status = "CREATED"; // По умолчанию заказ создан
        this.cancelled = false;
    }

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getPickupAddress() { 
        return pickupAddress; 
    }
    public void setPickupAddress(String pickupAddress) { 
        this.pickupAddress = pickupAddress; 
    }

    public String getDeliveryAddress() { 
        return deliveryAddress; 
    }
    public void setDeliveryAddress(String deliveryAddress) { 
        this.deliveryAddress = deliveryAddress; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public double getPrice() { 
        return price; 
    }
    public void setPrice(double price) { 
        this.price = price; 
    }

    
    public User getClient() { 
        return client; 
    }
    public void setClient(User client) { 
        this.client = client; 
    }

    public User getCourier() { 
        return courier; 
    }
    public void setCourier(User courier) { 
        this.courier = courier; 
    }

    public boolean isCancelled() { 
        return cancelled; 
    }
    public void setCancelled(boolean cancelled) { 
        this.cancelled = cancelled; 
    }

    public String getCancelReason() { 
        return cancelReason; 
    }
    public void setCancelReason(String cancelReason) { 
        this.cancelReason = cancelReason; 
    }
}
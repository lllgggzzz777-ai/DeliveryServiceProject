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
    
    private Long clientId;          // ID клиента, создавшего заказ
    private Long courierId;         // ID курьера (может быть null, пока не назначен)

    // Поля для отмены заказа
    private boolean cancelled = false;
    private String cancelReason;

    // Пустой конструктор 
    public DeliveryOrder() {}

    // Конструктор для быстрого создания заказа
    public DeliveryOrder(String pickupAddress, String deliveryAddress, double price, Long clientId) {
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.price = price;
        this.clientId = clientId;
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

    public Long getClientId() { 
        return clientId; 
    }
    public void setClientId(Long clientId) { 
        this.clientId = clientId; 
    }

    public Long getCourierId() { 
        return courierId; 
    }
    public void setCourierId(Long courierId) { 
        this.courierId = courierId; 
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
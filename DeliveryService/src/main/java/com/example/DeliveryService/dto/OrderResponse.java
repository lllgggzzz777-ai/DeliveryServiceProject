package com.example.DeliveryService.dto;

import com.example.DeliveryService.model.DeliveryOrder;

public class OrderResponse {
    
    private Long id;
    private String pickupAddress;
    private String deliveryAddress;
    private String status;
    private double price;
    
    private Long clientId;
    private String clientName; 
    
    private Long courierId;
    private String courierName;

    // Конструктор, который превращает Entity
    public OrderResponse(DeliveryOrder order) {
        this.id = order.getId();
        this.pickupAddress = order.getPickupAddress();
        this.deliveryAddress = order.getDeliveryAddress();
        this.status = order.getStatus();
        this.price = order.getPrice();

        if (order.getClient() != null) {
            this.clientId = order.getClient().getId();
            this.clientName = order.getClient().getUsername();
        }
        
        if (order.getCourier() != null) {
            this.courierId = order.getCourier().getId();
            this.courierName = order.getCourier().getUsername();
        }
    }


    public Long getId() { return id; }
    public String getPickupAddress() { return pickupAddress; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getStatus() { return status; }
    public double getPrice() { return price; }
    public Long getClientId() { return clientId; }
    public String getClientName() { return clientName; }
    public Long getCourierId() { return courierId; }
    public String getCourierName() { return courierName; }
}
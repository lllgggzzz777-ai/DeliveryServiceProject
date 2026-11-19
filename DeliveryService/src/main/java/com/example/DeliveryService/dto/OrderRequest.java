package com.example.DeliveryService.dto;

public class OrderRequest {
    private String pickupAddress;
    private String deliveryAddress;
    private double price;
    private Long clientId; // Только ID

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
}
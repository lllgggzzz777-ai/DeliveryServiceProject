package com.example.DeliveryService.controller;

import com.example.DeliveryService.model.Address;
import com.example.DeliveryService.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // 1. Список адресов пользователя
    @GetMapping
    public List<Address> getAddresses(@RequestParam Long userId) {
        return addressService.getUserAddresses(userId);
    }

    // 2. Добавить адрес
    @PostMapping
    public ResponseEntity<?> addAddress(@RequestBody Address request) {
        try {
            Address saved = addressService.addAddress(request);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 3. Обновить адрес
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody Address request) {
        try {
            Address updated = addressService.updateAddress(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 4. Удалить адрес
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.ok(Map.of("message", "Адрес удален"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
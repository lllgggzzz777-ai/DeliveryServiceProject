package com.example.DeliveryService.repository;

import com.example.DeliveryService.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Все адреса конкретного пользователя
    List<Address> findByUserId(Long userId);
}
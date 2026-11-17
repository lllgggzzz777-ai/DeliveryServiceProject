package com.example.DeliveryService.service;

import com.example.DeliveryService.model.Address;
import com.example.DeliveryService.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    public Address addAddress(Address address) {
        if (address.getAddress() == null || address.getAddress().isEmpty()) {
            throw new RuntimeException("Текст адреса не может быть пустым");
        }
        return addressRepository.save(address);
    }

    public Address updateAddress(Long id, Address request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Адрес с ID " + id + " не найден"));
        
        if (request.getTitle() != null) address.setTitle(request.getTitle());
        if (request.getAddress() != null) address.setAddress(request.getAddress());
        
        return addressRepository.save(address);
    }

    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new RuntimeException("Адрес с ID " + id + " не найден");
        }
        addressRepository.deleteById(id);
    }
}
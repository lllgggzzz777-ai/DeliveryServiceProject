package com.example.DeliveryService.service;

import com.example.DeliveryService.model.Address;
import com.example.DeliveryService.model.User;
import com.example.DeliveryService.repository.AddressRepository;
import com.example.DeliveryService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    // Внедрение зависимостей через конструктор
    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    //  Получить все адреса конкретного пользователя
    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    //  Добавить новый адрес
    public Address addAddress(Address request) {
        if (request.getAddress() == null || request.getAddress().isEmpty()) {
            throw new RuntimeException("Текст адреса не может быть пустым");
        }
        
        if (request.getUser() == null || request.getUser().getId() == null) {
            throw new RuntimeException("Не указан ID пользователя");
        }

        // Ищем реального пользователя в базе
        User user = userRepository.findById(request.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + request.getUser().getId() + " не найден"));

        // Привязываем найденного пользователя к адресу
        request.setUser(user);
        
        return addressRepository.save(request);
    }

    // Обновить существующий адрес
    public Address updateAddress(Long id, Address request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Адрес с ID " + id + " не найден"));
        
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            address.setTitle(request.getTitle());
        }
        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            address.setAddress(request.getAddress());
        }
        
        return addressRepository.save(address);
    }

    // Удалить адрес
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new RuntimeException("Адрес с ID " + id + " не найден");
        }
        addressRepository.deleteById(id);
    }
}
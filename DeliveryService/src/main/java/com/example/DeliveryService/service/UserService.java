package com.example.DeliveryService.service;

import com.example.DeliveryService.model.User;
import com.example.DeliveryService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Внедрение репозитория через конструктор
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Создание нового пользователя (используется в AuthController)
    public User register(String username, String password, String role) {
        User user = new User(username, password, role);
        return userRepository.save(user);
    }

    // Поиск по логину 
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Поиск по ID с выбросом ошибки 
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден"));
    }

    // Обновление профиля (только имя)
    public User updateProfile(Long id, User request) {
        User user = findById(id);

        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }
        
        return userRepository.save(user);
    }

    // Смена пароля с проверкой старого
    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = findById(id);

        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Неверный старый пароль");
        }
        if (newPassword == null || newPassword.length() < 4) {
            throw new RuntimeException("Новый пароль слишком короткий (минимум 4 символа)");
        }

        user.setPassword(newPassword);
        userRepository.save(user);
    }

    // Получить список всех курьеров
    public List<User> getCouriers() {
        return userRepository.findByRole("COURIER");
    }

    // Смена статуса курьера (онлайн/офлайн)
    public User setAvailability(Long courierId, boolean available) {
        User courier = findById(courierId);
        
        if (!"COURIER".equals(courier.getRole())) {
            throw new RuntimeException("Пользователь с ID " + courierId + " не является курьером");
        }
        
        courier.setAvailable(available);
        return userRepository.save(courier);
    }

    // Получить всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Блокировка/разблокировка пользователя
    public User blockUser(Long id, boolean blocked) {
        User user = findById(id);
        user.setBlocked(blocked);
        return userRepository.save(user);
    }
}
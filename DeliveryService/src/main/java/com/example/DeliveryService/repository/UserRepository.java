package com.example.DeliveryService.repository;

import com.example.DeliveryService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Поиск пользователя по логину (используется при регистрации и входе)
    Optional<User> findByUsername(String username);

    // Поиск всех пользователей с определенной ролью (используется для получения списка курьеров)
    List<User> findByRole(String role);
    
    // Проверка, существует ли пользователь с таким логином
    boolean existsByUsername(String username);
}

package com.example.DeliveryService.config;

import com.example.DeliveryService.model.User;
import com.example.DeliveryService.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            // Проверяем, есть ли уже пользователи в базе.
            // Если база пустая (count == 0), создаем тестовых пользователей.
            if (userRepository.count() == 0) {
                
                // 1. Создаем Клиента
                User client = new User();
                client.setUsername("client1");
                client.setPassword("123");
                client.setRole("CLIENT");
                userRepository.save(client);

                // 2. Создаем Курьера (по умолчанию offline, так как available = false)
                User courier = new User();
                courier.setUsername("courier1");
                courier.setPassword("123");
                courier.setRole("COURIER");
                userRepository.save(courier);

                // 3. Создаем Администратора
                User admin = new User();
                admin.setUsername("admin1");
                admin.setPassword("123");
                admin.setRole("ADMIN");
                userRepository.save(admin);

                // Выводим красивое сообщение в консоль, чтобы вы видели, что всё сработало
                System.out.println("==================================================");
                System.out.println(" ТЕСТОВЫЕ ПОЛЬЗОВАТЕЛИ УСПЕШНО СОЗДАНЫ В H2:");
                System.out.println("  Клиент:   ID = " + client.getId() + ", логин = client1, пароль = 123");
                System.out.println("  Курьер:   ID = " + courier.getId() + ", логин = courier1, пароль = 123");
                System.out.println("  Админ:    ID = " + admin.getId() + ", логин = admin1, пароль = 123");
                System.out.println("==================================================");
            } else {
                System.out.println("==================================================");
                System.out.println("ℹ  Пользователи уже существуют в базе данных. Пропускаем инициализацию.");
                System.out.println("==================================================");
            }
        };
    }
}
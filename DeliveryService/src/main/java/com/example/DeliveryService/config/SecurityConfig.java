package com.example.DeliveryService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Отключаем CSRF (защиту от подделки межсайтовых запросов), 
            // так как мы делаем REST API и работаем без сессий
            .csrf(AbstractHttpConfigurer::disable)
            
            // Отключаем стандартную форму логина и Basic Auth, 
            // чтобы Spring не выбрасывал нас на страницу входа
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            
            // Разрешаем все запросы без авторизации
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            
            // Разрешаем открывать H2 Console во фрейме (иначе она не загрузится)
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
            );

        return http.build();
    }
}
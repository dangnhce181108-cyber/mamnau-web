package com.example.mamnau.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()   // Cho phép tất cả request, không yêu cầu login
                )
                .csrf(csrf -> csrf.disable()); // Tắt CSRF cho môi trường dev
        return http.build();
    }
}

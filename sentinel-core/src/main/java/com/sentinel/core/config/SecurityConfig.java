package com.sentinel.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Bean cung cấp thuật toán mã hóa mật khẩu (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Cấu hình tạm thời: Cho phép tất cả API đều truy cập được (để test đã)
    // Sau này làm xong Login ta sẽ chặn sau.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF để test Postman dễ dàng
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Tạm thời cho phép tất cả (Permit All)
                );
        return http.build();
    }
}
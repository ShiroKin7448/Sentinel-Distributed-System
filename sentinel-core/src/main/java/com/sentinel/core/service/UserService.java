package com.sentinel.core.service;

import com.sentinel.core.dto.request.UserRegisterRequest;
import com.sentinel.core.dto.response.UserResponse;
import com.sentinel.core.entity.User;
import com.sentinel.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Lấy cái Bean BCrypt vừa tạo ở bước 2

    public UserResponse register(UserRegisterRequest request) {
        // 1. Kiểm tra trùng username
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username đã tồn tại!");
        }

        // 2. Tạo Entity và Mã hóa mật khẩu
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole() != null ? request.getRole() : "OPERATOR");
        user.setIsActive(true);

        // --- QUAN TRỌNG: MÃ HÓA PASSWORD ---
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(encodedPassword);

        // 3. Lưu vào DB
        User savedUser = userRepository.save(user);

        // 4. Trả về DTO
        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());
        response.setIsActive(savedUser.getIsActive());

        return response;
    }
}
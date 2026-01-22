package com.sentinel.core.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String username;
    private String role;

    // Constructor cho gọn
    public LoginResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
}
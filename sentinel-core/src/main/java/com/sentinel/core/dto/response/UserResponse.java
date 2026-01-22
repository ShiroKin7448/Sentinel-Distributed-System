package com.sentinel.core.dto.response;

import lombok.Data;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private String role;
    private Boolean isActive;
}
package com.sentinel.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cameras")
@Data
public class Camera {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "rtsp_url", nullable = false, columnDefinition = "TEXT")
    private String rtspUrl;

    private String location;

    private String status; // ONLINE, OFFLINE

    // Tạm thời để String để tránh lỗi thư viện JSON phức tạp lúc đầu.
    // Sau này sẽ dùng thư viện để parse JSON sau.
    @Column(name = "ai_config", columnDefinition = "jsonb")
    private String aiConfig;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
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

    private String status;

    @Column(name = "ai_models", columnDefinition = "jsonb")
    private String aiModels;

    @Column(name = "confidence_threshold")
    private Double confidenceThreshold;

    @Column(name = "min_event_duration")
    private Integer minEventDuration;

    @Column(name = "zone_coordinates", columnDefinition = "jsonb")
    private String zoneCoordinates;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
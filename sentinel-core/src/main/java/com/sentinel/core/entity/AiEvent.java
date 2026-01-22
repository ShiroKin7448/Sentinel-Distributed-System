package com.sentinel.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_events")
@Data
public class AiEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "camera_id")
    private Camera camera;

    @OneToOne
    @JoinColumn(name = "snapshot_file_id")
    private StoredFile snapshotFile;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    private String severity;

    private Double confidence; // 0.0 - 1.0

    @Column(columnDefinition = "jsonb")
    private String bbox;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    private String message;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
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
    private String eventType; // FIRE, INTRUSION

    private String severity; // INFO, WARNING

    private Double confidence;

    private String message;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
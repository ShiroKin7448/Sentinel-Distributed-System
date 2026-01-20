package com.sentinel.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "file_chunks")
@Data
public class FileChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private StoredFile file;

    @ManyToOne
    @JoinColumn(name = "node_id")
    private StorageNode storageNode;

    @Column(name = "chunk_order", nullable = false)
    private Integer chunkOrder;

    @Column(name = "physical_name", nullable = false)
    private String physicalName;

    @Column(nullable = false)
    private Long size;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
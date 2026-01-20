package com.sentinel.core.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "storage_nodes")
@Data
public class StorageNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "base_path", nullable = false)
    private String basePath;

    @Column(name = "capacity_total")
    private Long capacityTotal;

    @Column(name = "capacity_used")
    private Long capacityUsed;

    private String status; // ACTIVE, FULL, DOWN

    @Column(name = "last_checked")
    private LocalDateTime lastChecked;
}
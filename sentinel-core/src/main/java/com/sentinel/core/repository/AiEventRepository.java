package com.sentinel.core.repository;

import com.sentinel.core.entity.AiEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AiEventRepository extends JpaRepository<AiEvent, UUID> {
    // Tìm sự kiện mới nhất của 1 camera
    List<AiEvent> findByCameraIdOrderByCreatedAtDesc(UUID cameraId);
}
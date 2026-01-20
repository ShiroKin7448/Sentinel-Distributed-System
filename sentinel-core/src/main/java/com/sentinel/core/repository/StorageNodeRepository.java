package com.sentinel.core.repository;

import com.sentinel.core.entity.StorageNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorageNodeRepository extends JpaRepository<StorageNode, Integer> {
    // Tìm node theo tên (để tránh tạo trùng)
    Optional<StorageNode> findByName(String name);

    // Tìm node còn hoạt động
    Optional<StorageNode> findFirstByStatus(String status);
}
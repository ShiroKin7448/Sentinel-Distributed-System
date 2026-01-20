package com.sentinel.core.repository;

import com.sentinel.core.entity.FileChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileChunkRepository extends JpaRepository<FileChunk, UUID> {
    // Lấy danh sách các mảnh của 1 file, sắp xếp theo thứ tự
    List<FileChunk> findByFileIdOrderByChunkOrderAsc(UUID fileId);
}
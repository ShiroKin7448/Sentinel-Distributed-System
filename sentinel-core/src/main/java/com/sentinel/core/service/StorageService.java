package com.sentinel.core.service;

import com.sentinel.core.entity.StorageNode;
import com.sentinel.core.repository.StorageNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageService {

    @Autowired
    private StorageNodeRepository storageNodeRepository;

    // Lấy danh sách tất cả node
    public List<StorageNode> getAllNodes() {
        return storageNodeRepository.findAll();
    }

    // Tạo node mới
    public StorageNode createNode(StorageNode node) {
        // Có thể thêm logic kiểm tra trùng tên ở đây nếu cần
        node.setStatus("ACTIVE"); // Mặc định là Active
        node.setCapacityUsed(0L); // Mới tạo chưa dùng gì
        return storageNodeRepository.save(node);
    }
}
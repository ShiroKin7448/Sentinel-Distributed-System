package com.sentinel.core.controller;

import com.sentinel.core.entity.StorageNode;
import com.sentinel.core.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storage-nodes") // Đường dẫn gốc
public class StorageNodeController {

    @Autowired
    private StorageService storageService;

    @GetMapping
    public List<StorageNode> getAllNodes() {
        return storageService.getAllNodes();
    }

    @PostMapping
    public ResponseEntity<StorageNode> createNode(@RequestBody StorageNode node) {
        StorageNode newNode = storageService.createNode(node);
        return ResponseEntity.ok(newNode);
    }
}
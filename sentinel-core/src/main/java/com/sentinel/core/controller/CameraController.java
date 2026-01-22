package com.sentinel.core.controller;

import com.sentinel.core.dto.request.CameraCreateRequest;
import com.sentinel.core.dto.response.CameraResponse;
import com.sentinel.core.service.CameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cameras")
public class CameraController {

    @Autowired
    private CameraService cameraService;

    @PostMapping
    public ResponseEntity<CameraResponse> createCamera(@RequestBody CameraCreateRequest request) {
        return ResponseEntity.ok(cameraService.createCamera(request));
    }

    @GetMapping
    public ResponseEntity<List<CameraResponse>> getAllCameras() {
        return ResponseEntity.ok(cameraService.getAllCameras());
    }
}
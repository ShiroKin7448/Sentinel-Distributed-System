package com.sentinel.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentinel.core.dto.request.CameraCreateRequest;
import com.sentinel.core.dto.response.CameraResponse;
import com.sentinel.core.entity.Camera;
import com.sentinel.core.repository.CameraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CameraService {

    @Autowired
    private CameraRepository cameraRepository;

    // Bộ công cụ của Jackson để chuyển đổi JSON <-> String
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 1. Tạo Camera mới
    public CameraResponse createCamera(CameraCreateRequest request) {
        Camera camera = new Camera();
        camera.setName(request.getName());
        camera.setRtspUrl(request.getRtspUrl());
        camera.setLocation(request.getLocation());
        camera.setStatus("OFFLINE");

        // Cập nhật các thông số thông minh
        updateSmartConfig(camera, request);

        Camera savedCamera = cameraRepository.save(camera);
        return mapToResponse(savedCamera);
    }

    // 2. Lấy danh sách Camera
    public List<CameraResponse> getAllCameras() {
        return cameraRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // --- Helper Methods (Hàm phụ trợ) ---

    // Chuyển từ DTO vào Entity (Object -> JSON String)
    private void updateSmartConfig(Camera camera, CameraCreateRequest request) {
        try {
            camera.setConfidenceThreshold(request.getConfidenceThreshold());
            camera.setMinEventDuration(request.getMinEventDuration());

            if (request.getAiModels() != null) {
                camera.setAiModels(objectMapper.writeValueAsString(request.getAiModels()));
            }
            if (request.getZoneCoordinates() != null) {
                camera.setZoneCoordinates(objectMapper.writeValueAsString(request.getZoneCoordinates()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xử lý JSON Config", e);
        }
    }

    // Chuyển từ Entity ra DTO (JSON String -> Object)
    private CameraResponse mapToResponse(Camera camera) {
        CameraResponse response = new CameraResponse();
        response.setId(camera.getId());
        response.setName(camera.getName());
        response.setRtspUrl(camera.getRtspUrl());
        response.setStatus(camera.getStatus());
        response.setLocation(camera.getLocation());
        response.setLastHeartbeat(camera.getLastHeartbeat());
        response.setConfidenceThreshold(camera.getConfidenceThreshold());
        response.setMinEventDuration(camera.getMinEventDuration());

        try {
            if (camera.getAiModels() != null) {
                response.setAiModels(objectMapper.readValue(camera.getAiModels(), Object.class));
            }
            if (camera.getZoneCoordinates() != null) {
                response.setZoneCoordinates(objectMapper.readValue(camera.getZoneCoordinates(), Object.class));
            }
        } catch (Exception e) {
            // Nếu lỗi parse thì trả về null hoặc log lại
            e.printStackTrace();
        }
        return response;
    }
}
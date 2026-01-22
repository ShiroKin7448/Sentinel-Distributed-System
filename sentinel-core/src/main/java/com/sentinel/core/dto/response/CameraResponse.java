package com.sentinel.core.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CameraResponse {
    private UUID id;
    private String name;
    private String rtspUrl;
    private String status;
    private String location;

    // Trả về dạng Object để Frontend dễ đọc (không trả String)
    private Object aiModels;
    private Double confidenceThreshold;
    private Integer minEventDuration;
    private Object zoneCoordinates;

    private LocalDateTime lastHeartbeat;
}
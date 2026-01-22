package com.sentinel.core.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CameraCreateRequest {
    private String name;
    private String rtspUrl;
    private String location;

    // Frontend gửi mảng JSON, ta hứng bằng List/Object
    private List<String> aiModels;
    private Double confidenceThreshold;
    private Integer minEventDuration;

    // Vùng cấm: Mảng các tọa độ [[x,y], [x,y]]
    // Object có thể hứng bất kỳ cấu trúc JSON nào
    private Object zoneCoordinates;
}
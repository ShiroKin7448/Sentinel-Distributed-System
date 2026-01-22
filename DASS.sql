-- ==========================================
-- BƯỚC 0: DỌN DẸP DỮ LIỆU CŨ (Clean Slate)
-- ==========================================
DROP TABLE IF EXISTS ai_events CASCADE;
DROP TABLE IF EXISTS cameras CASCADE;
DROP TABLE IF EXISTS file_chunks CASCADE;
DROP TABLE IF EXISTS files CASCADE;
DROP TABLE IF EXISTS storage_nodes CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ==========================================
-- PHẦN 1: KHỞI TẠO CẤU TRÚC (SCHEMA V2 - SMART CAM)
-- ==========================================

-- 1. Enable UUID Extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- GROUP 1: IAM (Identity & Access Management)
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'OPERATOR', 
    is_active BOOLEAN DEFAULT TRUE, -- [Mới] Khóa tài khoản mềm
    avatar_url TEXT,                -- [Mới] Ảnh đại diện
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- GROUP 2: STORAGE ENGINE (TinyS3 Core)
CREATE TABLE storage_nodes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    base_path VARCHAR(255) NOT NULL, 
    capacity_total BIGINT DEFAULT 0, 
    capacity_used BIGINT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE', 
    last_checked TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE files (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    original_name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(50), 
    bucket VARCHAR(50) DEFAULT 'default',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE file_chunks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    file_id UUID REFERENCES files(id) ON DELETE CASCADE,
    node_id INTEGER REFERENCES storage_nodes(id),
    chunk_order INTEGER NOT NULL, 
    physical_name VARCHAR(255) NOT NULL, 
    size BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_chunks_file_id ON file_chunks(file_id);

-- GROUP 3: SENTINEL CORE (Smart Surveillance)
CREATE TABLE cameras (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    rtsp_url TEXT NOT NULL,
    location VARCHAR(255),
    status VARCHAR(20) DEFAULT 'OFFLINE', 
    
    -- [Mới] Các trường cấu hình cho "Smart Camera"
    ai_models JSONB DEFAULT '["yolov8n"]',  -- Danh sách model sẽ chạy (VD: ["yolo", "fire_net"])
    confidence_threshold FLOAT DEFAULT 0.7, -- Ngưỡng tin cậy (Lớp lọc 1)
    min_event_duration INTEGER DEFAULT 2,   -- Thời gian tối thiểu (giây) để báo động (Lớp lọc 3)
    zone_coordinates JSONB,                 -- Tọa độ vùng cấm (Lớp lọc 2) - VD: [[x1,y1], [x2,y2]...]
    
    last_heartbeat TIMESTAMP,               -- Để kiểm tra camera còn sống hay chết
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ai_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    camera_id UUID REFERENCES cameras(id) ON DELETE CASCADE,
    snapshot_file_id UUID REFERENCES files(id), 
    
    event_type VARCHAR(50) NOT NULL, -- FIRE, INTRUSION, PERSON
    severity VARCHAR(20) DEFAULT 'INFO',
    confidence FLOAT, 
    
    -- [Mới] Dữ liệu chi tiết sự kiện
    bbox JSONB,     -- Tọa độ vật thể [x, y, w, h] để vẽ lại hình chữ nhật
    metadata JSONB, -- Các thông tin phụ (VD: {"age": 25, "gender": "male"})
    
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_events_camera_time ON ai_events(camera_id, created_at DESC);
CREATE INDEX idx_events_type ON ai_events(event_type);

-- ==========================================
-- PHẦN 2: INSERT DỮ LIỆU MẪU (Dữ liệu thật cho máy của bạn)
-- ==========================================

-- Lưu ý: Trong SQL chuỗi, dấu backslash \ cần được escape thành \\
-- Tạo Node 1 (10GB) tại thư mục máy bạn
INSERT INTO storage_nodes (name, base_path, capacity_total, status)
VALUES (
    'Local Node 01',
    'C:\\Users\\ducdu\\Downloads\\Distributed_AI_Surveillance_Storage\\sentinel_data\\node1',
    10737418240, -- 10GB
    'ACTIVE'
);

-- Tạo Node 2 (10GB) tại thư mục máy bạn
INSERT INTO storage_nodes (name, base_path, capacity_total, status)
VALUES (
    'Local Node 02',
    'C:\\Users\\ducdu\\Downloads\\Distributed_AI_Surveillance_Storage\\sentinel_data\\node2',
    10737418240, -- 10GB
    'ACTIVE'
);

-- Tạo User Admin mặc định (Pass: admin123 - Đã hash mẫu bằng BCrypt)
-- Hash này tương đương với 'admin123'
INSERT INTO users (username, password_hash, email, role, is_active)
VALUES (
    'admin',
    '$2a$10$wS9y/n.wzZl.y/hFv.m.UO./.x.y.z.ABC_HASH_EXAMPLE_FOR_DEV', 
    'admin@sentinel.com',
    'ADMIN',
    TRUE
);
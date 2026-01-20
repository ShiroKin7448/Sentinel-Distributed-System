-- ==========================================
-- BƯỚC 0: DỌN DẸP DỮ LIỆU CŨ (Để tránh lỗi "Already Exists")
-- ==========================================
DROP TABLE IF EXISTS ai_events CASCADE;
DROP TABLE IF EXISTS cameras CASCADE;
DROP TABLE IF EXISTS file_chunks CASCADE;
DROP TABLE IF EXISTS files CASCADE;
DROP TABLE IF EXISTS storage_nodes CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ==========================================
-- PHẦN 1: KHỞI TẠO CẤU TRÚC (SCHEMA)
-- ==========================================

-- 1. Enable UUID Extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- GROUP 1: IAM
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'OPERATOR', 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- GROUP 2: STORAGE ENGINE 
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

-- GROUP 3: SENTINEL CORE
CREATE TABLE cameras (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    rtsp_url TEXT NOT NULL,
    location VARCHAR(255),
    status VARCHAR(20) DEFAULT 'OFFLINE', 
    ai_config JSONB DEFAULT '{}', 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ai_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    camera_id UUID REFERENCES cameras(id) ON DELETE CASCADE,
    snapshot_file_id UUID REFERENCES files(id), 
    event_type VARCHAR(50) NOT NULL, 
    severity VARCHAR(20) DEFAULT 'INFO',
    confidence FLOAT, 
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_events_camera_time ON ai_events(camera_id, created_at DESC);
CREATE INDEX idx_events_type ON ai_events(event_type);

-- ==========================================
-- PHẦN 2: INSERT DỮ LIỆU MẪU (10GB / Node)
-- ==========================================

-- Tạo Node 1 (10GB)
INSERT INTO storage_nodes (name, base_path, capacity_total, status)
VALUES (
    'Local Node 01',
    'C:\Users\ducdu\Downloads\Distributed_AI_Surveillance_Storage\sentinel_data\node1',
    10737418240, -- 10GB
    'ACTIVE'
);

-- Tạo Node 2 (10GB)
INSERT INTO storage_nodes (name, base_path, capacity_total, status)
VALUES (
    'Local Node 02',
    'C:\Users\ducdu\Downloads\Distributed_AI_Surveillance_Storage\sentinel_data\node2',
    10737418240, -- 10GB
    'ACTIVE'
);
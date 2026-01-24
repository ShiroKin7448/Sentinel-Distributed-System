import cv2
import pika
import time
import json
import base64

# --- CẤU HÌNH ---
RABBITMQ_HOST = 'localhost'
RABBITMQ_PORT = 5672
RABBITMQ_USER = 'admin'
RABBITMQ_PASS = 'admin123'
QUEUE_NAME = 'camera_frames'
CAMERA_SOURCE = 1  # Số 0 là Webcam. Nếu muốn dùng file video, thay bằng đường dẫn file (ví dụ: "video.mp4")

def connect_rabbitmq():
    """Hàm kết nối đến RabbitMQ, tự động thử lại nếu lỗi"""
    credentials = pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS)
    parameters = pika.ConnectionParameters(RABBITMQ_HOST, RABBITMQ_PORT, '/', credentials)
    
    while True:
        try:
            connection = pika.BlockingConnection(parameters)
            channel = connection.channel()
            # Tạo hàng đợi (Queue) nếu chưa có
            channel.queue_declare(queue=QUEUE_NAME, durable=True)
            print("✅ Đã kết nối RabbitMQ thành công!")
            return connection, channel
        except Exception as e:
            print(f"❌ Lỗi kết nối RabbitMQ: {e}. Thử lại sau 5s...")
            time.sleep(5)

def main():
    # 1. Kết nối RabbitMQ
    connection, channel = connect_rabbitmq()

    # 2. Mở Camera (hoặc Video)
    cap = cv2.VideoCapture(CAMERA_SOURCE)
    
    if not cap.isOpened():
        print("❌ Không thể mở Camera!")
        return

    print("🎥 Đang bắt đầu gửi hình ảnh từ Camera...")
    
    frame_count = 0
    
    try:
        while True:
            # Đọc 1 khung hình
            ret, frame = cap.read()
            if not ret:
                print("Hết video hoặc lỗi Camera.")
                break

            frame_count += 1
            
            # --- TỐI ƯU HÓA ---
            # Chỉ gửi 1 frame mỗi 5 frame (giảm tải cho hệ thống, khoảng 6 FPS)
            if frame_count % 5 != 0:
                continue

            # Resize ảnh nhỏ lại (cho nhẹ mạng) -> 640x480
            frame = cv2.resize(frame, (640, 480))

            # Mã hóa ảnh sang JPEG
            _, buffer = cv2.imencode('.jpg', frame)
            
            # Chuyển sang chuỗi Base64 để gửi qua mạng
            jpg_as_text = base64.b64encode(buffer).decode('utf-8')

            # Tạo gói tin JSON
            message = {
                "camera_id": "cam_cong_chinh", # Giả lập ID
                "timestamp": time.time(),
                "image_data": jpg_as_text
            }

            # Gửi vào RabbitMQ
            channel.basic_publish(
                exchange='',
                routing_key=QUEUE_NAME,
                body=json.dumps(message),
                properties=pika.BasicProperties(
                    delivery_mode=2,  # Tin nhắn bền vững (lưu đĩa)
                )
            )
            
            print(f"📤 Đã gửi frame #{frame_count}")

            # Hiển thị cửa sổ xem trước (Preview)
            cv2.imshow('Ingestion Service - Preview', frame)

            # Bấm nút 'q' để thoát
            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

    except KeyboardInterrupt:
        print("Dừng chương trình...")
    finally:
        cap.release()
        cv2.destroyAllWindows()
        connection.close()

if __name__ == '__main__':
    main()
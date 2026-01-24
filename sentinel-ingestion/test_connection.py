import pika

try:
    # Thông tin kết nối (giống trong docker-compose)
    credentials = pika.PlainCredentials('admin', 'admin123')
    parameters = pika.ConnectionParameters('localhost', 5672, '/', credentials)
    
    # Thử kết nối
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()
    
    print("✅ KẾT NỐI RABBITMQ THÀNH CÔNG!")
    print("RabbitMQ đang chờ nhận video frame...")
    
    connection.close()
except Exception as e:
    print("❌ KẾT NỐI THẤT BẠI:", e)
use smarthome;
CREATE TABLE members (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     VARCHAR(50) NOT NULL UNIQUE,
    pass    VARCHAR(255) NOT NULL,
    name   VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    address     VARCHAR(255) NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ;
CREATE TABLE sensor_data (
    log_id INT AUTO_INCREMENT PRIMARY KEY, -- 데이터 고유 번호
    id varchar(20),                        --  사용자id
    location VARCHAR(50),                  -- 'livingroom', 'kitchen' 등 위치
    sensor_type VARCHAR(50),               -- 'temp', 'humidity', 'light' 등 센서 종류
    sensor_value VARCHAR(100),             -- 센서 값
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 수신 시간
);
select * from members;
select * from sensor_data;
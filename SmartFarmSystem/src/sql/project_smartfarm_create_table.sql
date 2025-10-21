-- ==============================
--  USERS: 사용자 정보
-- ==============================
CREATE TABLE project_smartfarm.users (
    user_uid           INT AUTO_INCREMENT PRIMARY KEY,
    user_id            VARCHAR(50)  NOT NULL,
    password           VARCHAR(225) NOT NULL,
    email              VARCHAR(100) NOT NULL,
    name               VARCHAR(50)  NOT NULL,
    security_question  VARCHAR(100),
    security_answer    VARCHAR(10),
    UNIQUE INDEX user_id_UNIQUE (user_id ASC),
    UNIQUE INDEX email_UNIQUE (email ASC)
);

-- ==============================
--  DEVICE_SPECS: 기기 스펙 정보
-- ==============================
CREATE TABLE project_smartfarm.device_specs (
    spec_uid    INT PRIMARY KEY,
    farm_slots  INT NOT NULL,
    features    JSON NOT NULL
);

-- ==============================
--  DEVICES: 사용자 보유 기기
-- ==============================
CREATE TABLE project_smartfarm.devices (
    device_uid     INT AUTO_INCREMENT PRIMARY KEY,
    user_uid       INT NOT NULL,
    spec_uid       INT,
    serial_number  VARCHAR(100),
    UNIQUE INDEX serial_number_UNIQUE (serial_number ASC),
    CONSTRAINT fk_devices_user
        FOREIGN KEY (user_uid)
        REFERENCES project_smartfarm.users (user_uid)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_devices_spec
        FOREIGN KEY (spec_uid)
        REFERENCES project_smartfarm.device_specs (spec_uid)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- ==============================
--  PLANT_PRESETS: 작물별 최적 환경 설정
-- ==============================
CREATE TABLE project_smartfarm.plant_presets (
    preset_uid           INT AUTO_INCREMENT PRIMARY KEY,
    plant_name           VARCHAR(50) NOT NULL,
    optimal_temp         FLOAT NOT NULL,
    optimal_humidity     FLOAT NOT NULL,
    light_intensity      FLOAT NOT NULL,
    co2_level            FLOAT NOT NULL,
    soil_moisture        FLOAT NOT NULL,
    growth_period_days   INT NOT NULL,
    UNIQUE INDEX plant_name_UNIQUE (plant_name ASC)
);

-- ==============================
--  FARMS: 사용자별 농장 관리
-- ==============================
CREATE TABLE project_smartfarm.farms (
    farm_uid       INT AUTO_INCREMENT PRIMARY KEY,
    user_uid       INT NOT NULL,
    device_uid     INT NOT NULL,
    preset_uid     INT,
    farm_number    INT NOT NULL,
    planting_date  DATE NOT NULL,
    CONSTRAINT fk_farms_user
        FOREIGN KEY (user_uid)
        REFERENCES project_smartfarm.users (user_uid)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_farms_device
        FOREIGN KEY (device_uid)
        REFERENCES project_smartfarm.devices (device_uid)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_farms_preset
        FOREIGN KEY (preset_uid)
        REFERENCES project_smartfarm.plant_presets (preset_uid)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- ==============================
--  SENSOR_LOGS: 센서 측정 로그
-- ==============================
CREATE TABLE project_smartfarm.sensor_logs (
    log_uid                BIGINT AUTO_INCREMENT PRIMARY KEY,
    farm_uid               INT NOT NULL,
    recorded_at            DATETIME NOT NULL,
    measured_temp          FLOAT NOT NULL,
    measured_humidity      FLOAT NOT NULL,
    measured_co2           FLOAT NOT NULL,
    measured_soil_moisture FLOAT NOT NULL,
    CONSTRAINT fk_sensor_logs_farm
        FOREIGN KEY (farm_uid)
        REFERENCES project_smartfarm.farms (farm_uid)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
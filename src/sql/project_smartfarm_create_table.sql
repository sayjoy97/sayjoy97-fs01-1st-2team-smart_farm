use project_smartfarm;

-- ==============================
--  USERS: 사용자 정보
-- ==============================
CREATE TABLE project_smartfarm.users (
	user_uid                INT AUTO_INCREMENT PRIMARY KEY,
    user_id                 VARCHAR(50)  NOT NULL,
    password                VARCHAR(225) NOT NULL,
    email                   VARCHAR(100) NOT NULL,
    name                    VARCHAR(50)  NOT NULL,
    security_question       VARCHAR(100) NOT NULL,
    security_answer         VARCHAR(100) NOT NULL,
    device_serial_number    VARCHAR(20)  NOT NULL,
    UNIQUE INDEX user_id_UNIQUE (user_id ASC),
    UNIQUE INDEX email_UNIQUE (email ASC),
    UNIQUE INDEX device_serial_number_UNIQUE (device_serial_number ASC)
);

-- ==============================
--  DEVICE_SPECS: 기기 스펙 정보
-- ==============================
CREATE TABLE project_smartfarm.device_specs (
    spec_uid      INT  PRIMARY KEY,
    farm_slots    INT  NOT NULL,
    features      JSON NOT NULL
);

-- ==============================
--  DEVICES: 사용자 보유 기기
-- ==============================
CREATE TABLE project_smartfarm.devices (
    device_serial_number    varchar(20) PRIMARY KEY,
    user_uid                INT,
    spec_uid                INT NOT NULL,
    CONSTRAINT fk_devices_user
        FOREIGN KEY (user_uid)
        REFERENCES project_smartfarm.users (user_uid)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_devices_spec
        FOREIGN KEY (spec_uid)
        REFERENCES project_smartfarm.device_specs (spec_uid)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ==============================
--  PLANT_PRESETS: 작물별 최적 환경 설정
-- ==============================
CREATE TABLE project_smartfarm.plant_presets (
    preset_uid            INT AUTO_INCREMENT PRIMARY KEY,
    plant_name            VARCHAR(50) NOT NULL,
    optimal_temp          FLOAT       NOT NULL,
    optimal_humidity      FLOAT       NOT NULL,
    light_intensity       FLOAT       NOT NULL,
    co2_level             FLOAT       NOT NULL,
    soil_moisture         FLOAT       NOT NULL,
    growth_period_days    INT         NOT NULL
);

-- ==============================
--  FARMS: 사용자별 농장 관리
-- ==============================
CREATE TABLE project_smartfarm.farms (
    farm_uid                varchar(20) PRIMARY KEY,
    user_uid                INT NOT NULL,
    preset_uid              INT,
    planting_date           DATE,
    CONSTRAINT fk_farms_user
        FOREIGN KEY (user_uid)
        REFERENCES project_smartfarm.users (user_uid)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_farms_preset
        FOREIGN KEY (preset_uid)
        REFERENCES project_smartfarm.plant_presets (preset_uid)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ==============================
--  SENSOR_LOGS: 센서 측정 로그
-- ==============================
CREATE TABLE project_smartfarm.sensor_logs (
    log_uid                BIGINT AUTO_INCREMENT PRIMARY KEY,
    farm_uid               varchar(20) NOT NULL,
    recorded_at            DATETIME    NOT NULL,
    measured_temp          FLOAT       NOT NULL,
    measured_humidity      FLOAT       NOT NULL,
    measured_co2           FLOAT,
    measured_soil_moisture FLOAT       NOT NULL,
    CONSTRAINT fk_sensor_logs_farm
        FOREIGN KEY (farm_uid)
        REFERENCES project_smartfarm.farms (farm_uid)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ==============================
--  NOTIFICATION_LOGS: 알림 로그
-- ============================== 
CREATE TABLE project_smartfarm.notification_logs (
    notification_log_uid BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_serial_number VARCHAR(20) NOT NULL,
    log_message          VARCHAR(100) NOT NULL,
    recorded_at          DATETIME     NOT NULL,
    CONSTRAINT fk_notification_logs_device
        FOREIGN KEY (device_serial_number)
        REFERENCES project_smartfarm.devices (device_serial_number)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

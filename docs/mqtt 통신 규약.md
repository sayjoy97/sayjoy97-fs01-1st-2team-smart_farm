# MQTT 통신 규약

## 📋 토픽 구조

### 기본 형식
```
smartfarm/{farmUid}/[sensor|preset]/[data|request|response]
```

### farmUid 구조
```
farmUid = {deviceSerial}:{slotNumber}
예: A1001:1, A1001:2, B2002:1
```

- **deviceSerial**: DB에 사전 등록된 디바이스 시리얼 넘버
- **slotNumber**: 같은 디바이스 내 팜 슬롯 번호 (1, 2, 3, ...)

---

## 🔄 통신 규약 테이블

| 구분                | 역할                | Publish(발행)                                  | Subscribe(구독)                     | 토픽 예시                                | 페이로드 예시                                                        | 비고                                  |
| ----------------- | ----------------- | -------------------------------------------- | --------------------------------- | ------------------------------------ | -------------------------------------------------------------- | ----------------------------------- |
| **라즈베리파이(기기)**    | 프리셋 요청         | `smartfarm/{farmUid}/preset/request`   | `smartfarm/{farmUid}/preset` | `smartfarm/A1001:1/preset/request` | `A1001:1` (farmUid)   | 시작 시 각 슬롯마다. QoS 1           |
| 〃                 | 프리셋 응답 수신         | —   | `smartfarm/{farmUid}/preset/response` | `smartfarm/A1001:1/preset/response` | `OptimalTemp=22;OptimalHumidity=65;...`   | DB 조회 결과. QoS 1           |
| 〃                 | 센서 데이터 전송         | `smartfarm/{farmUid}/sensor/data`   | — | `smartfarm/A1001:1/sensor/data` | `temp=23.5;humidity=60;measuredLight=1024;soil=2048;co2=450`   | 10초 주기. QoS 0. ADC 원시값           |
| 〃                 | 알림 로그 전송          | `smartfarm/{deviceSerial}/sensor/nl` | —                                 | `smartfarm/A1001/sensor/nl`     | `물탱크 부족` (평문)                                               | QoS 1. 디바이스 전체 알림                               |
| 〃                 | 프리셋 업데이트 수신            | —                                            | `smartfarm/{farmUid}/preset`                                 | `smartfarm/A1001:1/preset`         | `OptimalTemp=25;OptimalHumidity=70;...`     | 유저가 설정 변경 시. QoS 1                  |
| **DB 서버**         | 센서 데이터 수신·저장      | —                                            | `smartfarm/+/sensor/#`          | `smartfarm/A1001:1/sensor/data` | 위와 동일                                                          | `sensor_logs` 테이블에 INSERT           |
| 〃                 | 알림 로그 수신·저장·중계       | `{userId}/smartfarm/{deviceSerial}/sensor/nl`                                            | `smartfarm/+/sensor/#`                                 | `smartfarm/A1001/sensor/nl`     | 평문                                                             | `notification_logs` INSERT + 유저 중계    |
| 〃                 | 프리셋 요청 수신            | —           | `smartfarm/+/preset/request`                                 | `smartfarm/A1001:1/preset/request`         | `A1001:1`     | DB 조회 트리거               |
| 〃                 | 프리셋 응답 전송            | `smartfarm/{farmUid}/preset/response`           | —                                 | `smartfarm/A1001:1/preset/response`         | `OptimalTemp=22;...` 또는 `none`     | DB 조회 결과. QoS 1               |
| 〃                 | 프리셋 업데이트 전송            | `smartfarm/{farmUid}/preset`           | —                                 | `smartfarm/A1001:1/preset`         | `OptimalTemp=25;...`     | 유저가 설정 변경 시. QoS 1               |
| **사용자 앱(콘솔)**     | 센서 데이터 조회         | —                                            | —                                 | —                                    | —                                                              | DB 조회 (JDBC)                       |
| 〃                 | 알림 수신         | —                                            | `{userId}/smartfarm/+/sensor/nl`                                 | `user123/smartfarm/A1001/sensor/nl`                                    | `물탱크 부족`                                                              | DB 서버가 중계한 알림. QoS 1                       |
| 〃                 | 프리셋 설정 요청         | —                                            | —                                 | —                                    | —                                                              | DB 서버에 저장 → DB 서버가 MQTT 발행       |

---

## 📝 주요 페이로드 형식

### 1. 프리셋 요청 (라즈베리파이 → DB 서버)
```
토픽: smartfarm/A1001:1/preset/request
페이로드: A1001:1
```

**목적:** 라즈베리파이 시작 시 각 슬롯별로 DB에 저장된 프리셋 요청

**예시:**
- 슬롯 1: `smartfarm/A1001:1/preset/request`
- 슬롯 2: `smartfarm/A1001:2/preset/request`

### 2. 프리셋 응답 (DB 서버 → 라즈베리파이)
```
토픽: smartfarm/A1001:1/preset/response
페이로드: OptimalTemp=22;OptimalHumidity=65;LightIntensity=3000;SoilMoisture=1800;Co2Level=800
```

**페이로드가 "none"인 경우:** DB에 프리셋 없음 → 기본값 사용

**필드:**
- `OptimalTemp`: 적정 온도 (°C)
- `OptimalHumidity`: 적정 습도 (%)
- `LightIntensity`: 적정 조도 (lux)
- `SoilMoisture`: 적정 토양 수분 (ADC 값)
- `Co2Level`: 적정 CO2 농도 (ppm)

### 3. 센서 데이터 (라즈베리파이 → DB 서버)
```
토픽: smartfarm/A1001:1/sensor/data
페이로드: temp=23.5;humidity=60;measuredLight=1024;soil=2048;co2=450
```

**참고:** 각 슬롯마다 독립적으로 센서 데이터 전송

**필드:**
- `temp`: 온도 (°C)
- `humidity`: 습도 (%)
- `measuredLight`: 조도 (ADC 원시값, 0~1023)
- `soil`: 토양 수분 (ADC 원시값, 0~4095)
- `co2`: CO2 농도 (ppm, 선택)

> 모델 구분: 디바이스 시리얼 규칙에 따라 CO2 포함 여부가 달라짐
> - 고급형 4슬롯: `A4xxx` → CO2 포함, 슬롯 [1,2,3,4]
> - 고급형 1슬롯: `A1xxx` → CO2 포함, 슬롯 [1]
> - 일반형 4슬롯: `B4xxx` → CO2 미포함, 슬롯 [1,2,3,4]
> - 일반형 1슬롯: `B1xxx` → CO2 미포함, 슬롯 [1]

**참고:**
- 센서 일부 누락 시 해당 필드만 제외하고 전송
- 예: `temp=23.5;humidity=60` (조도/토양 센서 고장 시)

### 4. 프리셋 업데이트 (DB 서버 → 라즈베리파이)
```
토픽: smartfarm/A1001:1/preset
페이로드: OptimalTemp=25;OptimalHumidity=70;LightIntensity=3500;SoilMoisture=2000;Co2Level=900
```

**목적:** 유저가 콘솔에서 특정 슬롯의 설정 변경 시 실시간 업데이트

### 5. 알림 로그 (라즈베리파이 → DB 서버 → 유저)
```
1) 라즈베리파이 → DB 서버
토픽: smartfarm/A1001/sensor/nl
페이로드: 물탱크 부족

2) DB 서버 → 유저 콘솔
토픽: user123/smartfarm/A1001/sensor/nl
페이로드: 물탱크 부족
```

**형식:** 평문 메시지

**흐름:**
1. 라즈베리파이가 알림 발행
2. DB 서버가 수신 → `notification_logs` 테이블에 저장
3. DB 조회: `device_serial`로 `user_id` 찾기
4. 해당 유저에게 알림 중계 발행

---

## 🔧 구독 패턴

### 라즈베리파이
```
smartfarm/{farmUid}/preset          # 자신의 슬롯별 프리셋 업데이트
smartfarm/{farmUid}/preset/response # 자신의 슬롯별 프리셋 응답
```

**예시 (디바이스 A1001, 슬롯 1):**
- `smartfarm/A1001:1/preset` 
- `smartfarm/A1001:1/preset/response`

**예시 (디바이스 A4001, 슬롯 1~4):**
- 슬롯 1: `smartfarm/A4001:1/preset`, `smartfarm/A4001:1/preset/response`
- 슬롯 2: `smartfarm/A4001:2/preset`, `smartfarm/A4001:2/preset/response`
- 슬롯 3: `smartfarm/A4001:3/preset`, `smartfarm/A4001:3/preset/response`
- 슬롯 4: `smartfarm/A4001:4/preset`, `smartfarm/A4001:4/preset/response`

### DB 서버
```
smartfarm/+/sensor/#          # 모든 farmUid의 센서 데이터/알림
smartfarm/+/preset/request    # 모든 farmUid의 프리셋 요청
```

**수신 예시:**
- `smartfarm/A1001:1/sensor/data`
- `smartfarm/A1001:2/sensor/data`
- `smartfarm/A1001/sensor/nl`
- `smartfarm/A1001:1/preset/request`
- `smartfarm/B2002:1/preset/request`

### 유저 콘솔
```
{userId}/smartfarm/+/sensor/nl    # 자신이 소유한 모든 디바이스의 알림
```

**예시 (user123이 A1001, B4001 소유):**
- 구독: `user123/smartfarm/+/sensor/nl`
- 수신:
  - `user123/smartfarm/A1001/sensor/nl`
  - `user123/smartfarm/B4001/sensor/nl`

---

## 🚀 사용 방법

### 1. DB에 디바이스 사전 등록
```sql
INSERT INTO devices (device_serial, device_name, status) 
VALUES ('A1001', '스마트팜 디바이스 1', 'active');
```

### 2. 유저 회원가입 및 디바이스 등록
- Java 콘솔에서 회원가입
- 라즈베리파이 시리얼 넘버 입력
- DB의 `devices` 테이블에 `user_id` 업데이트

### 3. 식물 설정 (프리셋 저장)
- Java 콘솔에서 식물 추가
- 목표 환경(온도, 습도 등) 설정
- DB의 `farms` 테이블에 저장

### 4. 라즈베리파이 실행

**설정 (`main.py`):**
```python
device_serial = "A1001"      # 디바이스 시리얼 넘버
broker = "localhost"         # MQTT 브로커 주소
interval = 10                # 센서 읽기 주기 (초)
```

**실행:**
```bash
python smartfarm/main.py
```

**실행 흐름:**
1. MQTT 연결
2. 각 슬롯별로 DB에 프리셋 요청
   - 슬롯 1: `smartfarm/A1001:1/preset/request`
   - 슬롯 2: `smartfarm/A1001:2/preset/request` (있을 경우)
3. DB 응답 대기 (최대 10초)
4. 각 슬롯별 프리셋 적용 (없으면 기본값)
5. 각 슬롯별 센서 데이터 전송 시작 (10초마다)
6. 각 슬롯별 프리셋 기반 자동 제어

**참고:** 한 디바이스에 여러 슬롯(팜)이 있을 수 있음. 디바이스 시리얼 접두어로 모델·슬롯 구성이 결정됨 (A4/A1/B4/B1).

---

## 📌 참고사항

### MQTT 통신 주체
- **라즈베리파이 ↔ DB 서버**: MQTT로 실시간 데이터 전송 (센서, 프리셋)
- **DB 서버 ↔ 유저 콘솔**: MQTT로 알림 중계 (실시간)
- **유저 콘솔 ↔ DB**: JDBC로 DB 조회/명령 (센서 데이터, 프리셋 설정)

### QoS 레벨
- 센서 데이터: QoS 0 (빠른 전송, 손실 허용)
- 프리셋 요청/응답: QoS 1 (최소 1회 전달 보장)
- 프리셋 업데이트: QoS 1 (최소 1회 전달 보장)
- 알림: QoS 1 (최소 1회 전달 보장)

### 데이터 흐름

**센서 데이터 조회:**
```
라즈베리파이 → (MQTT) → DB 서버 → (저장) → (JDBC) → 유저 콘솔
```

**프리셋 설정:**
```
유저 콘솔 → (JDBC) → DB 서버 → (저장 & MQTT 발행) → 라즈베리파이
```

**시작 시 프리셋 로드:**
```
라즈베리파이 → (MQTT 요청) → DB 서버 → (DB 조회) → (MQTT 응답) → 라즈베리파이
```

**알림 중계:**
```
라즈베리파이 → (MQTT) → DB 서버 → (DB 저장 & 조회 user_id) → (MQTT 중계) → 유저 콘솔
```

### 액추에이터 자동 제어

라즈베리파이가 **자체적으로 판단**하여 제어:

**히터 (온도 기반):**
- 온도 < 목표 - 2°C → ON
- 온도 > 목표 + 1°C → OFF

**물펌프 (토양 수분 기반):**
- 토양 ADC > 목표 + 500 → ON (건조)
- 토양 ADC < 목표 - 200 → OFF (충분)

**환기팬 (습도 기반):**
- 습도 > 목표 + 10% → ON
- 습도 < 목표 - 5% → OFF

### 기본 프리셋 값
DB에 프리셋이 없을 경우:
```python
OptimalTemp = 25°C
OptimalHumidity = 60%
LightIntensity = 3000 lux
SoilMoisture = 2000 ADC
Co2Level = 800 ppm
```

### DB 서버 구현 요구사항

**1. 프리셋 요청 처리:**
```java
// 구독: smartfarm/+/preset/request
client.subscribe("smartfarm/+/preset/request");

// 메시지 수신 시
String farmUid = payload;  // 예: "A1001:1"
String[] parts = farmUid.split(":");
String deviceSerial = parts[0];  // "A1001"
int slotNumber = Integer.parseInt(parts[1]);  // 1

PresetDTO preset = db.getPresetByFarmUid(farmUid);

if (preset != null) {
    String response = "OptimalTemp=" + preset.getTemp() + ";...";
    client.publish("smartfarm/" + farmUid + "/preset/response", response);
} else {
    client.publish("smartfarm/" + farmUid + "/preset/response", "none");
}
```

**2. 프리셋 변경 시:**
```java
// 유저가 특정 슬롯의 설정 변경 시
String farmUid = "A1001:1";  // 디바이스 + 슬롯
String presetData = "OptimalTemp=25;OptimalHumidity=70;...";
client.publish("smartfarm/" + farmUid + "/preset", presetData);
```

**3. 센서 데이터 저장:**
```java
// 구독: smartfarm/+/sensor/#
client.subscribe("smartfarm/+/sensor/#");

// sensor/data 수신 시 → sensor_logs 테이블 저장
// sensor/nl 수신 시 → notification_logs 테이블 저장
```

**4. 알림 중계 (DB 서버 → 유저):**
```java
// messageArrived 처리
if (topic.endsWith("/sensor/nl")) {
    if (DBServerMode) {
        // 1. 알림 저장
        notificationService.saveNotification(topic, payload);
        
        // 2. deviceSerial 추출
        String deviceSerial = extractDeviceSerial(topic);  // "A1001"
        
        // 3. user_id 조회
        String userId = deviceService.getUserIdByDeviceSerial(deviceSerial);
        
        // 4. 유저에게 중계
        if (userId != null) {
            publishNotificationToUser(userId, deviceSerial, payload);
            // 토픽: user123/smartfarm/A1001/sensor/nl
        }
    }
}
```

**5. 유저 콘솔에서 알림 수신:**
```java
// 유저 모드 생성자
MqttManager userClient = new MqttManager("user123");

// 자동 구독: user123/smartfarm/+/sensor/nl

// messageArrived에서 알림 수신
if (topic.endsWith("/sensor/nl")) {
    if (!DBServerMode) {
        System.out.println("🔔 알림 수신: " + payload);
        // UI에 알림 표시
    }
}
```

| 구분                | 역할                | Publish(발행)                                       | Subscribe(구독)                                          | 토픽 예시                                            | 페이로드 예시(JSON)                                                             | 비고                                  |
| ----------------- | ----------------- | ------------------------------------------------- | ------------------------------------------------------ | ------------------------------------------------ | ------------------------------------------------------------------------- | ----------------------------------- |
| **라즈베리파이(기기)**    | 센서 데이터 전송 / 명령 수신 | `{userId}/smartfarm/{farmUid}/sensor/data`        | `{userId}/smartfarm/+/cmd/#`                   | `user1/smartfarm/A101:1/sensor/data`             | `{"temp":23.5,"hum":60,"co2":800,"soil":420,"ts":"2025-10-23T02:10:00Z"}` | 10~30초 주기. QoS 0~1                  |
| 〃                 | 명령 수행 결과 회신(ACK)  | `{userId}/smartfarm/{farmUid}/ack/{actuatorType}` | —                                                      | `user1/smartfarm/A101:1/ack/pump`                | `{"status":"ok","action":"on","duration":3000,"corrId":"abc..."}`         | 실패 시 `status:"nack"`                |
| **DB 서버**         | 센서 데이터 수신·저장      | —                                                 | `+/smartfarm/+/sensor/data`<br>`+/smartfarm/+/query/#` | `user1/smartfarm/A101:1/sensor/data`             | 위와 동일                                                                     | 수신 후 `sensor_logs` INSERT. 쿼리 요청 처리 |
| **사용자 앱(콘솔/GUI)** | 명령 발행 / 실시간 모니터링  | `{userId}/smartfarm/{farmUid}/cmd/{actuatorType}` | `{userId}/smartfarm/+/sensor/data`                     | `user1/smartfarm/A101:1/cmd/led/1`               | `{"action":"on","brightness":80,"corrId":"abc..."}`                       | 본인 소유 농장만 구독                        |

명령 예시: user1/smartfarm/A101:1/cmd/led/1 페이로드 {"action":"on","brightness":80,"corrId":"abc..."}

farmUid = {deviceUid}:{farmOrder}. farmOrder ∈ {1,2,3,4,...}.

actuatorType는 가급적 논리명 사용(예: led/1, pump, fan). 핀 번호는 DB 매핑 테이블로 분리 권장

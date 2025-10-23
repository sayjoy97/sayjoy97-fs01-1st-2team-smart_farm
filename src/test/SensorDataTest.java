package test;

import service.SensorDataService;
import service.SensorDataServiceImpl;

public class SensorDataTest {

    public static void main(String[] args) {
        System.out.println("📥 센서 데이터 저장 테스트 시작...");

        try {
            // 서비스 객체 생성
            SensorDataService service = new SensorDataServiceImpl();

            // 1️⃣ 테스트용 토픽 (농장 ID 포함)
            // farm/F001/sensor → F001번 농장에 해당하는 센서 데이터
            String topic = "farm/F001/sensor";

            // 2️⃣ 테스트용 payload
            // 실제 라즈베리파이에서 보낼 데이터와 동일한 형식
            String payload = "temp=24.5;humidity=55;co2=700;soil=390;";

            // 3️⃣ 데이터 저장 테스트
            service.saveData(topic, payload);
            System.out.println("✅ DB 저장 완료 확인\n");

            // 4️⃣ 최근 로그 조회 (DAO를 직접 호출하거나, service에 추가 가능)
            System.out.println("📊 최근 로그 조회 테스트...");
            // 필요하다면 여기서 dao.getLogsByFarm("F001", null, 10) 호출도 가능

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

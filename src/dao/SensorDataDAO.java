package dao;

import java.util.List;

import dto.SensorDataDTO;

public interface SensorDataDAO {
	//MQTT에서 센서값 받았을 때 자동 저장
	int insertSensorData(SensorDataDTO data);
	// 팜의 최신 로그 조회 : “현재 환경 상태” 표시할 때 사용
	SensorDataDTO getLatestLog(int farmUid);
	// 팜의 특정 로그 조회 : “그래프나 통계 화면”에서 사용
	List<SensorDataDTO> getLogsByFarm(int farmUid);
}

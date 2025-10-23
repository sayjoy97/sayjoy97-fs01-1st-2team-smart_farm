package dao;

import java.util.List;

import dto.SensorDataDTO;

public interface SensorDataDAO {
	//MQTT에서 센서값 받았을 때 자동 저장
	int insertSensorData(SensorDataDTO data);
	// 특정 팜의 로그를 전체 읽거나, 시간에 따라 읽거나, 갯수에 따라 읽기
	List<SensorDataDTO> getLogsByFarm(int farmUid, Integer hours, Integer limit);
	// 사용자별 로그를 전체 읽거나, 시간에 따라 읽거나, 갯수에 따라 읽기 
	List<SensorDataDTO> getLogsByUser(int userUid, Integer hours, Integer limit);
}

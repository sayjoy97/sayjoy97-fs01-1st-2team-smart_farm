package service;

import java.util.ArrayList;

import dto.SensorDataDTO;

public interface SensorDataService {
	 int insertSensorData(SensorDataDTO data);
	 SensorDataDTO getLatestLog(int farmUid);
	 ArrayList<SensorDataDTO> getLogsByFarm(int farmUid);
	 void saveData(String topic, String payload);
}

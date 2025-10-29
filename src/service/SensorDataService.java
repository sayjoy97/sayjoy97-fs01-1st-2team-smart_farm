package service;

import java.util.List;

import dto.SensorDataDTO;

public interface SensorDataService {
	 int insertSensorData (SensorDataDTO data);
	 List<SensorDataDTO> getLogsByFarm (String farmUid, Integer hours, Integer limit);
	 List<SensorDataDTO> getLogsByUser (int userUid, Integer hours, Integer limit);
	 List<SensorDataDTO> getLogsForGraph (String farmUid);
	 void makeGraph (int[] values, int[] hours, double referenceTickValue, double scale, String unit);
	 void saveData (String topic, String payload);
}

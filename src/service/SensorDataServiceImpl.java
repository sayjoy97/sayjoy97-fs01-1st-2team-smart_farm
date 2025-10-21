package service;

import dao.SensorDataDAO;
import dao.SensorDataDAOImpl;
import dto.SensorDataDTO;

public class SensorDataServiceImpl implements SensorDataService{
	private SensorDataDAO dao = new SensorDataDAOImpl();
	@Override
	public void saveData(String topic, String payload) {
		// 1. 토픽 파싱
        String[] topicdata = topic.split("/");
        String[] payloadData = payload.split(":");
        SensorDataDTO data = 
        		new SensorDataDTO(0, topicdata[0],
        				"livingroom", topicdata[topicdata.length-1],
        				payload, null);
        dao.insertSensorData(data);
	}
}

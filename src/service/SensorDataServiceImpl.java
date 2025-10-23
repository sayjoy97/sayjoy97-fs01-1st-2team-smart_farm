package service;

import java.sql.Timestamp;
import java.util.ArrayList;

import dao.SensorDataDAO;
import dao.SensorDataDAOImpl;
import dto.SensorDataDTO;

public class SensorDataServiceImpl implements SensorDataService{
	private SensorDataDAO dao = new SensorDataDAOImpl();
	@Override
	public void saveData(String topic, String payload) {
		
		// 1. 토픽 파싱
        String[] parts = topic.split("/");
        int farmUid = Integer.parseInt(parts[1]); // farm 번호추출
        
        // 2. payload 구조 
        String[] payloadData = payload.split(":");
        float temp = Float.parseFloat(payloadData[0]);
        float humidity = Float.parseFloat(payloadData[1]);
        float co2 = Float.parseFloat(payloadData[2]);
        float soil = Float.parseFloat(payloadData[3]);
        
        SensorDataDTO data = 
        		new SensorDataDTO(
        						  0,
        						  farmUid,
        						  new Timestamp(System.currentTimeMillis()),
        						  temp, humidity, co2, soil
        						  );
        dao.insertSensorData(data);
	}
	@Override
	public int insertSensorData(SensorDataDTO data) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public SensorDataDTO getLatestLog(int farmUid) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<SensorDataDTO> getLogsByFarm(int farmUid) {
		// TODO Auto-generated method stub
		return null;
	}
}

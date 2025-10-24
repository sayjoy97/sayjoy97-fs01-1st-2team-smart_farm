package service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dao.SensorDataDAO;
import dao.SensorDataDAOImpl;
import dto.SensorDataDTO;

public class SensorDataServiceImpl implements SensorDataService{
	private SensorDataDAO dao = new SensorDataDAOImpl();
	@Override
	public void saveData(String topic, String payload) {
		try {
		// 1. 토픽 파싱
        String[] parts = topic.split("/");
        String farmUid = parts[2]; //토픽 구조 예시: "{userId}/smartfarm/{farmUid}/sensor/data" -> 세번째 자리에 들어감 
        float temp = 0, humidity = 0, co2 = 0, soil = 0;
        
        // 2. payload 구조 (예: "temp=23.5;hum=60;co2=800;soil=420;")
        // key : value 형식으로 데이터 구분
        // ; -> 센서 항목 나누고 = -> key/value값으로 나눔 
        String[] pairs = payload.split(";");
        for(String pair : pairs) {
        	if(pair.contains("=")) {
        		String[] keyvalue = pair.split("=");
        		String key = keyvalue[0].trim();
        		String value = keyvalue[1].trim();
        		
        		switch(key) {
        		case "temp":
        			temp = Float.parseFloat(value);
        			break;
        		case "humidity":
        			humidity = Float.parseFloat(value);
        			break;
        		case "co2":
        			co2 = Float.parseFloat(value);
        			break;
        		case "soil":
        			soil = Float.parseFloat(value);
        			break;
        		}
        	}
        }
        
        // DTO에 담아서 DB에 저장 
        SensorDataDTO data = new SensorDataDTO(
        		  0,
				  farmUid,
				  new Timestamp(System.currentTimeMillis()),
				  temp, humidity, co2, soil
        );
        
        dao.insertSensorData(data);
        System.out.println("DB 저장 완료!" + data);
		}catch(Exception e) {
			e.printStackTrace();
		}
        
	}
	@Override
	public int insertSensorData(SensorDataDTO data) {
		return dao.insertSensorData(data);
	}
	@Override
	public List<SensorDataDTO> getLogsByFarm(String farmUid, Integer hours, Integer limit) {
		return dao.getLogsByFarm(farmUid, hours, limit);
	}
	@Override
	public List<SensorDataDTO> getLogsByUser(int userUid, Integer hours, Integer limit) {
		return dao.getLogsByUser(userUid, hours, limit);
	}


	
}

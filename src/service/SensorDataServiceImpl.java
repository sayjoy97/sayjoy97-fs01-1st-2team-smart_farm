package service;

import java.sql.Timestamp;
import java.time.LocalTime;
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
		// 1. 토픽 파싱: {userId}/smartfarm/{farmUid}/sensor/data
        String[] parts = topic.split("/");
        String farmUid = parts[2]; // farmUid 추출 (예: A101:1)
        float temp = 0, humidity = 0, measuredLight = 0, co2 = 0, soil = 0;
        
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
        		case "measuredLight":
        			measuredLight = Float.parseFloat(value);
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
				  temp, humidity, measuredLight ,co2, soil
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
	
	public List<SensorDataDTO> getLogsForGraph(String farmUid) {
		return dao.getLogsForGraph(farmUid);
	}

	public void makeGraph(int[] values, int[] hours, double referenceTickValue, double scale, String unit) {
		int maxValue = 0;
		int minValue = 10000;
		double level = 0;
		for (int value : values) {
			if (value >= maxValue) {
				maxValue = value;
			}
			if (value < minValue) {
				minValue = value;
			}
		}
		if (maxValue <= (int)referenceTickValue) {
			maxValue = (int)referenceTickValue;
			minValue -= minValue % scale;
		} else {
			double d = referenceTickValue;
			while (maxValue <= d) {
				d += scale;
			}
			maxValue = (int)d;
			if (minValue >= (int)referenceTickValue) {
				minValue = (int)referenceTickValue;
			} else {
				minValue -= minValue % scale;
			}
		}
		System.out.println("          |                                                   (단위 : " + unit + ")");
		for (level = maxValue; level >= minValue; level -= scale) {
			if (level == referenceTickValue) {
				System.out.printf("\u001B[31m%9.1f\u001B[0m", level);
				System.out.print(" |");
				for (int i = values.length - 1; i >= 0; i--) {
					if (values[i] >= level) {
						System.out.print("\u001B[31m ■■\u001B[0m");
					} else {
						System.out.print("   ");
					}
					System.out.print("  ");
				}
			} else {
				System.out.printf("%9.1f", level);
				System.out.print(" |");
				for (int i = 9; i >= 0; i--) {
					if (values[i] >= level) {
						System.out.print(" ■■");
					} else {
						System.out.print("   ");
					}
					System.out.print("  ");
				}
			}
			System.out.println("");
		}
		System.out.printf("%9.1f", level);
		System.out.println(" | ■■   ■■   ■■   ■■   ■■   ■■   ■■   ■■   ■■   ■■  ");
		System.out.println("          +----+----+----+----+----+----+----+----+----+----+");
		System.out.print("          |");
		for (int i = hours.length - 1; i >= 0; i--) {
			
			if (((hours[i] + 24) % 24) == 0) {
				System.out.print(" 24");
			} else if (((hours[i] + 24) % 24) < 10) {
				System.out.print(" 0" + ((hours[i] + 24) % 24));
			} else {
				System.out.print(" " + ((hours[i] + 24) % 24));
			}
			System.out.print(" |");
		}
		System.out.println("");
	}
}

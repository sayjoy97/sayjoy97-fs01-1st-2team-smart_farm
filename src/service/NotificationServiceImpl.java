package service;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.NotificationDAO;
import dao.NotificationDAOImpl;
import dto.MemberDTO;

public class NotificationServiceImpl implements NotificationService {
	private NotificationDAO notificationDAO = new NotificationDAOImpl();
	public ArrayList<String> showNotification(MemberDTO user) {
		ArrayList<String> notifications = notificationDAO.showNotification(user);
		return notifications;
	}

	
	public void deleteAllNotification(MemberDTO user) {
		notificationDAO.deleteAllNotification(user);
	}
	
	public void deleteNotification(ArrayList<Integer> deleteNLUs) {
		notificationDAO.deleteNotification(deleteNLUs);
	}


	@Override
	public void saveData(String topic, String payload, LocalDate recordedAt) {
		try {
		// 1. 토픽 파싱
        String[] parts = topic.split("/");
        String farmUid = parts[2]; //토픽 구조 예시: "{userId}/smartfarm/{farmUid}/sensor/data" -> 세번째 자리에 들어감 
        //farmUid에서deviceSerialNumber 추출
        String[] parsedevice = farmUid.split(":");
        String deviceSerialNumber = parsedevice[0];
        // 2. payload 평문으로 저장
        
        notificationDAO.addNL(deviceSerialNumber, payload, recordedAt);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}

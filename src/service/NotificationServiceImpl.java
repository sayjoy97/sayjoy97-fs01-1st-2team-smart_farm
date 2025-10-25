package service;

import java.sql.Timestamp;
import java.util.ArrayList;

import dao.NotificationDAO;
import dao.NotificationDAOImpl;
import dto.MemberDTO;
import dto.NotificationLogsDTO;

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
	public void saveNotification(String topic, String payload) {
		try {
			// 1. 토픽 파싱: {userId}/smartfarm/{farmUid}/sensor/nl
			String[] parts = topic.split("/");
			String farmUid = parts[2]; // farmUid 추출 (예: A101:1)
			
			// 2. farmUid에서 device_serial_number 추출
			String deviceSerialNumber = farmUid.split(":")[0]; // 예: A101:1 -> A101
			
			// 3. payload를 log_message로 사용
			String logMessage = payload.trim();
			
			// 4. DTO 생성
			NotificationLogsDTO notification = new NotificationLogsDTO(
				0, // auto increment
				deviceSerialNumber,
				logMessage,
				new Timestamp(System.currentTimeMillis())
			);
			
			// 5. DB에 저장
			notificationDAO.insertNotification(notification);
			System.out.println("알림 DB 저장 완료! " + notification);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package service;

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
}

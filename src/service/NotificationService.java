package service;

import java.time.LocalDate;
import java.util.ArrayList;

import dto.MemberDTO;

public interface NotificationService {
	ArrayList<String> showNotification(MemberDTO user);
	void deleteAllNotification(MemberDTO user);
	void deleteNotification(ArrayList<Integer> deleteNLUs);
	void saveData(String topic, String payload, LocalDate recordedAt);
}

package service;

import java.util.ArrayList;

import dto.MemberDTO;

public interface NotificationService {
	ArrayList<String> showNotification(MemberDTO user);
	void deleteAllNotification(MemberDTO user);
	void deleteNotification(ArrayList<Integer> deleteNLUs);
}

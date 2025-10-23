package dao;

import java.util.ArrayList;

import dto.MemberDTO;

public interface NotificationDAO {
	ArrayList<String> showNotification(MemberDTO user);
	int deleteAllNotification(MemberDTO user);
	int deleteNotification(ArrayList<Integer> deleteNLUs);
}

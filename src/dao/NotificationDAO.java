package dao;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import dto.MemberDTO;

public interface NotificationDAO {
	ArrayList<String> showNotification(MemberDTO user);
	int deleteAllNotification(MemberDTO user);
	int deleteNotification(ArrayList<Integer> deleteNLUs);
	int addNL(String deviceSerialNumber, String payload, LocalDate recordedAt);
}

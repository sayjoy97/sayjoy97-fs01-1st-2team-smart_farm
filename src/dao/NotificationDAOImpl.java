package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dto.MemberDTO;
import dto.NotificationLogsDTO;
import util.DBUtil;

public class NotificationDAOImpl implements NotificationDAO {
	public ArrayList<String> showNotification(MemberDTO user) {
		String sql = "SELECT nl.*\n"
				   + "FROM notification_logs nl\n"
				   + "JOIN devices d ON nl.device_serial_number = d.device_serial_number\n"
				   + "JOIN users u ON d.user_uid = u.user_uid\n"
				   + "WHERE u.user_id = ?\n"
				   + "ORDER BY recorded_at;";
	    Connection con = null;
	    PreparedStatement ptmt = null;
	    ResultSet rs = null;
	    ArrayList<String> notifications = new ArrayList<>();
	    SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 HH시 mm분");
	    try {
	        con = DBUtil.getConnect();
	        ptmt = con.prepareStatement(sql);
	        ptmt.setString(1, user.getUserId());
	        rs = ptmt.executeQuery();
	        while (rs.next()) {
	        	String str = "기기 시리얼 넘버: " + rs.getString("device_serial_number")
	        	           + "/알림 발생 시각: " + sdf.format(rs.getTimestamp("recorded_at"))
	        	           + "/메시지: " + rs.getString("log_message")
	        	           + "/" + rs.getInt("notification_log_uid");
	        	notifications.add(str);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs, ptmt, con);
	    }
	    return notifications;
	}

	public int deleteAllNotification(MemberDTO user) {
		String sql = "DELETE nl\n"
				   + "FROM notification_logs nl\n"
				   + "JOIN devices d ON nl.device_serial_number = d.device_serial_number\n"
				   + "JOIN users u ON d.user_uid = u.user_uid\n"
				   + "WHERE u.user_id = ?;";
		Connection con = null;
	    PreparedStatement ptmt = null;
	    int result = 0;
	    try {
	        con = DBUtil.getConnect();
	        ptmt = con.prepareStatement(sql);
	        ptmt.setString(1, user.getUserId());
	        result = ptmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(null, ptmt, con);
	    }
		return result;
	}
	
	public int deleteNotification(ArrayList<Integer> deleteNLUs) {
		String sql = "DELETE FROM notification_logs\n"
				   + "WHERE notification_log_uid = ?";
		Connection con = null;
	    PreparedStatement ptmt = null;
	    int result = 0;
	    int returnValue = 0;
	    try {
	        con = DBUtil.getConnect();
	        ptmt = con.prepareStatement(sql);
	        for (int deletNLU : deleteNLUs) {
	        	ptmt.setInt(1, deletNLU);
		        result = ptmt.executeUpdate();
		        returnValue = returnValue + result;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(null, ptmt, con);
	    }
	    return returnValue;
	}
	
	@Override
	public int addNotification(NotificationLogsDTO notification) {
		String sql = "INSERT INTO notification_logs VALUES(null, ?, ?, now())";
		Connection con = null;
		PreparedStatement ptmt = null;
		int result = 0;
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, notification.getDeviceSerialNumber());
			ptmt.setString(2, notification.getLogMessage());
			
			result = ptmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, ptmt, con);
		}
		return result;
	}
}

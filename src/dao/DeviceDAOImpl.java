package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dto.MemberDTO;
import util.DBUtil;

public class DeviceDAOImpl implements DeviceDAO {
	public int addDevice(MemberDTO user) {
		String sql1 = "UPDATE devices\n"
				    + "JOIN users ON devices.device_serial_number = users.device_serial_number\n"
				    + "SET devices.user_uid = users.user_uid\n"
				    + "WHERE devices.device_serial_number = ?;";
		Connection con = null;
		PreparedStatement ptmt1 = null;
		int result = 0;
		try {
			con = DBUtil.getConnect();
			ptmt1 = con.prepareStatement(sql1);
			ptmt1.setString(1, user.getDeviceSerialNumber());
			result = ptmt1.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, ptmt1, con);
		}
		return result;
	}
}

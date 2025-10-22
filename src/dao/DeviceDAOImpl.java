package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.MemberDTO;
import util.DBUtil;

public class DeviceDAOImpl implements DeviceDAO {
	public int addDevice(MemberDTO user) {
		String sql1 = "select user_uid, device_serial_number from users where user_id = ?";
		String sql2 = "insert into devices values(null,?,?)";
		Connection con = null;
		PreparedStatement ptmt1 = null;
		PreparedStatement ptmt2 = null;
		ResultSet rs = null;
		int userUid = 0;
		String deviceSerialNumber = "";
		int specUid = 0;
		int result = 0;
		try {
			con = DBUtil.getConnect();
			ptmt1 = con.prepareStatement(sql1);
			ptmt1.setString(1, user.getUserId());
			rs = ptmt1.executeQuery();
			if (rs.next()) {
				userUid = rs.getInt("user_uid");
				deviceSerialNumber = rs.getString("device_serial_number");
			}
			
			if (deviceSerialNumber.contains("A1")) {
				specUid = 1;
			} else if (deviceSerialNumber.contains("A4")) {
				specUid = 2;
			} else if (deviceSerialNumber.contains("B1")) {
				specUid = 3;
			} else if (deviceSerialNumber.contains("B4")) {
				specUid = 4;
			}
			ptmt2 = con.prepareStatement(sql2);
			ptmt2.setInt(1, userUid);
			ptmt2.setInt(2, specUid);
			result = ptmt2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ptmt1, con);
			DBUtil.close(null, ptmt2, null);
		}
		return result;
	}
}

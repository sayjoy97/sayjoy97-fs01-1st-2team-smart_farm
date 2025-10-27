package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dto.DeviceDTO;
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
			return 0;
		} finally {
			DBUtil.close(null, ptmt1, con);
		}
		return result;
	}
	
	
	public ArrayList<DeviceDTO> selectUserDevices(MemberDTO user) {
		String sql1 = "select * from devices where user_uid = ?";
		Connection con = null;
		PreparedStatement ptmt =null;
		ResultSet rs = null;
		ArrayList<DeviceDTO> devices = new ArrayList<DeviceDTO>();
		try {
			con = DBUtil.getConnect();
			ptmt =  con.prepareStatement(sql1);
			ptmt.setInt(1, user.getUserUid());
			rs =  ptmt.executeQuery();
			DeviceDTO userDevice = null;
			while(rs.next()) {
				userDevice = new DeviceDTO(
					    rs.getString("device_serial_number"),
					    rs.getInt("user_uid"),
					    rs.getInt("spec_uid")
					);
				devices.add(userDevice);
			}
			
		}  catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ptmt, con);
		}
		return devices;
		
	}
	
	public int addNewDevice(MemberDTO user, String dsn) {
		String sql = "UPDATE devices\n"
	               + "JOIN users ON users.user_id = ?\n"
			   	   + "SET devices.user_uid = users.user_uid\n"
	               + "WHERE devices.device_serial_number = ?";
		Connection con = null;
		PreparedStatement ptmt = null;
		int result = 0;

		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, user.getUserId());
			ptmt.setString(2, dsn);
			result = ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, ptmt, con);
		}
		return result;
	}
	
	public int deleteDevice(String deleteDSN) {
		String sql = "UPDATE devices\n"
			   	   + "SET user_uid = null\n"
	               + "WHERE device_serial_number = ?";
		Connection con = null;
		PreparedStatement ptmt = null;
		int result = 0;
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, deleteDSN);
			result = ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, ptmt, con);
		}
		return result;
	}
	
	@Override
	public String getUserIdByDeviceSerial(String deviceSerial) {
		String sql = "SELECT u.user_id " +
		             "FROM devices d " +
		             "JOIN users u ON d.user_uid = u.user_uid " +
		             "WHERE d.device_serial_number = ?";
		Connection con = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		String userId = null;
		
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, deviceSerial);
			rs = ptmt.executeQuery();
			
			if (rs.next()) {
				userId = rs.getString("user_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ptmt, con);
		}
		
		return userId;
	}
}
	

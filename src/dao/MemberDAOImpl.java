package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.MemberDTO;

import util.DBUtil;

public class MemberDAOImpl implements MemberDAO {

	@Override
	public int register(MemberDTO user) {
		String sql = "insert into users values(null, ?, ?, ?, ?, ?, ?, ?)";
		Connection con = null;
		PreparedStatement ptmt = null;
		int result = 0;
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, user.getUserId());
			ptmt.setString(2, user.getPassword());
			ptmt.setString(3, user.getEmail());
			ptmt.setString(4, user.getName());
			ptmt.setString(5, user.getSecurityQuestion());
			ptmt.setString(6, user.getSecurityAnswer());
			ptmt.setString(7, user.getDeviceSerialNumber());
			result = ptmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, ptmt, con);
		}
		return result;
	}

	@Override
	public MemberDTO login(String userId, String password) {
		Connection con = null;
		PreparedStatement ptmt =null;
		ResultSet rs = null;
		MemberDTO loginSuccessUser = null;
		String sql = "select * from users where user_id=? and password=?";
		try {
			con = DBUtil.getConnect();
			ptmt =  con.prepareStatement(sql);
			ptmt.setString(1, userId);
			ptmt.setString(2, password);
			rs =  ptmt.executeQuery();
			if(rs.next()) {
				loginSuccessUser = new MemberDTO(rs.getInt("user_uid"), 
						rs.getString("user_id"), rs.getString("password"),
						rs.getString("email"), rs.getString("name"),
						rs.getString("security_question"), rs.getString("security_answer"),
						rs.getString("device_serial_number"));
			}
		}  catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, ptmt, con);
		}
		return loginSuccessUser;
	}
	@Override
	public void updateUserInfo(int userUid, String column, String newValue) {
	    String sql = "UPDATE users SET " + column + " = ? WHERE user_uid = ?";
	    Connection con = null;
	    PreparedStatement ptmt = null;

	    try {
	        con = DBUtil.getConnect();
	        ptmt = con.prepareStatement(sql);
	        ptmt.setString(1, newValue);
	        ptmt.setInt(2, userUid);

	        int result = ptmt.executeUpdate();

	        if (result > 0) {
	            System.out.println("✅ 사용자 정보가 성공적으로 수정되었습니다.");
	        } else {
	            System.out.println("⚠️ 해당 사용자를 찾을 수 없습니다.");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(null, ptmt, con);
	    }
	}
}

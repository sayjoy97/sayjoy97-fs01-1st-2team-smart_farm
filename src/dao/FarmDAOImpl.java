package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dto.MemberDTO;
import util.DBUtil;

public class FarmDAOImpl implements FarmDAO {
	public int createFarm(MemberDTO user) {
		String sql = "INSERT INTO farms\n"
				   + "(farm_uid, user_uid, preset_uid, planting_date)\n"
				   + "VALUES\n"
				   + "(?, (select user_uid from users where user_id = ?), NULL, NULL);\n";
	    Connection con = null;
	    PreparedStatement ptmt = null;
	    int result = 0;
	    try {
	        int row = Integer.parseInt(user.getDeviceSerialNumber().substring(1, 2));
	        con = DBUtil.getConnect();
	        con.setAutoCommit(false);
	        ptmt = con.prepareStatement(sql);
	        for (int num = 1; num <= row; num++) {
	            ptmt.setString(1, user.getDeviceSerialNumber() + ":" + num);
	            ptmt.setString(2, user.getUserId());
	            ptmt.addBatch();
	        }
	        int[] results = ptmt.executeBatch();
	        for (int r : results) {
	        	result = result + r;
	        }
	        con.commit();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        try { if (con != null) con.rollback(); } catch (SQLException ignore) {}
	    } finally {
	        DBUtil.close(null, ptmt, con);
	    }
	    return result;
	}
	
	public int createFarm(MemberDTO user, String dsn) {
		String sql = "INSERT INTO farms\n"
				   + "(farm_uid, user_uid, preset_uid, planting_date)\n"
				   + "VALUES\n"
				   + "(?, (select user_uid from users where user_id = ?), NULL, NULL);\n";
	    Connection con = null;
	    PreparedStatement ptmt = null;
	    int result = 0;
	    try {
	        int row = Integer.parseInt(dsn.substring(1, 2));
	        con = DBUtil.getConnect();
	        con.setAutoCommit(false);
	        ptmt = con.prepareStatement(sql);
	        for (int num = 1; num <= row; num++) {
	            ptmt.setString(1, dsn + ":" + num);
	            ptmt.setString(2, user.getUserId());
	            ptmt.addBatch();
	        }
	        int[] results = ptmt.executeBatch();
	        for (int r : results) {
	        	result = result + r;
	        }
	        con.commit();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        try { if (con != null) con.rollback(); } catch (SQLException ignore) {}
	    } finally {
	        DBUtil.close(null, ptmt, con);
	    }
	    return result;
	}
	
	public int addFarm(String plantName, String farmUid) {
		String sql = "UPDATE farms\n"
				   + "SET preset_uid = (select preset_uid from plant_presets where plant_name = ?),\n"
				   + "    planting_date = CURDATE()\n"
				   + "WHERE farm_uid = ?";
	    Connection con = null;
	    PreparedStatement ptmt = null;
	    int result = 0;
	    try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, plantName);
			ptmt.setString(2, farmUid);
			result = ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, ptmt, con);
		}
	    return result;
	}
}

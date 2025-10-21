package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dto.SensorDataDTO;
import util.DBUtil;

public class SensorDataDAOImpl implements SensorDataDAO {

	@Override
	public int insertSensorData(SensorDataDTO data) {
		String sql = "insert into sensor_data  values(null,?,?,?,?,now())";
		Connection con = null;
		PreparedStatement ptmt = null;
		int result = 0;
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, data.getId());
			ptmt.setString(2, data.getLocation());
			ptmt.setString(3, data.getSensorType());
			ptmt.setString(4, data.getSensorValue());
			result = ptmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(null, ptmt, con);
		}
		return result;
	}

}

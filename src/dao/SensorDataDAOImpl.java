package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.protocol.Resultset;

import dto.SensorDataDTO;
import util.DBUtil;

public class SensorDataDAOImpl implements SensorDataDAO {

	@Override
	public int insertSensorData(SensorDataDTO data) {
		String sql = "insert into sensor_logs values(null,?,now(),?,?,?,?)";
		Connection con = null;
		PreparedStatement ptmt = null;
		int result = 0;
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setLong(1, data.getLogId());
			ptmt.setInt(2, data.getFarmUid());
			ptmt.setTimestamp(3, data.getRecordedAt());
			ptmt.setFloat(4, data.getMeasuredTemp());
			ptmt.setFloat(5, data.getMeasuredHumidity());
			ptmt.setFloat(6, data.getMeasuredCo2());
			ptmt.setFloat(7, data.getMeasuredSoilMoisture());
			
			result = ptmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(null, ptmt, con);
		}
		return result;
	}

	@Override
	public SensorDataDTO getLatestLog(int farmUid) {
		String sql = "select * from sensor_logs where farm_uid = ? order by recorded_at desc limit 1";
		Connection con = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		SensorDataDTO sensor = null;
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, farmUid);
			rs = ptmt.executeQuery();
			if(rs.next()) {
		 		sensor = new SensorDataDTO(
						rs.getLong("log_uid"),
						rs.getInt("farm_uid"),
						rs.getTimestamp("recorded_at"),
						rs.getFloat("measured_temp"),
						rs.getFloat("measured_humidity"),
						rs.getFloat("measured_co2"),
						rs.getFloat("measured_soil_moisture")
						);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs ,ptmt, con);
		}
		return sensor;
	}

	@Override
	public ArrayList<SensorDataDTO> getLogsByFarm(int farmUid) {
		String sql = "select * from sensor_logs where farm_uid = ? order by recorded_at desc";
		Connection con = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		ArrayList<SensorDataDTO> sensor_list = new ArrayList<SensorDataDTO>();
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, farmUid);
			rs = ptmt.executeQuery();
			while(rs.next()) {
				SensorDataDTO s = new SensorDataDTO(
						rs.getLong("log_uid"),
						rs.getInt("farm_uid"),
						rs.getTimestamp("recorded_at"),
						rs.getFloat("measured_temp"),
						rs.getFloat("measured_humidity"),
						rs.getFloat("measured_co2"),
						rs.getFloat("measured_soil_moisture")
						);
				sensor_list.add(s);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs ,ptmt, con);
		}
		return sensor_list;
	}

}

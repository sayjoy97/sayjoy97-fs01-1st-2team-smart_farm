package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.SensorDataDTO;
import util.DBUtil;

public class SensorDataDAOImpl implements SensorDataDAO {

	 
	
	@Override
	public int insertSensorData(SensorDataDTO data) {
		String sql = "insert into sensor_logs values(null,?,now(),?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ptmt = null;	
		int result = 0;
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, data.getFarmUid());
			ptmt.setFloat(2, data.getMeasuredTemp());
			ptmt.setFloat(3, data.getMeasuredHumidity());
			ptmt.setFloat(4, data.getMeasuredLight());
			ptmt.setFloat(5, data.getMeasuredCo2());
			ptmt.setFloat(6, data.getMeasuredSoilMoisture());
			
			result = ptmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(null, ptmt, con);
		}
		return result;
	}

	@Override
	public ArrayList<SensorDataDTO> getLogsByFarm(String farmUid, Integer hours, Integer limit) {
	    String sql = "select * from sensor_logs where farm_uid = ?";

	    // 선택적으로 시간 조건 추가
	    if (hours != null) {
	        sql += " and recorded_at >= now() - interval ? hour"; //INTERVAL : 얼마만큼의 시간 차이 
	    }

	    // 최신순 정렬
	    sql += " order by recorded_at desc";

	    // 선택적으로 개수 제한 추가
	    if (limit != null) {
	        sql += " limit ?";
	    }

	    Connection con = null;
	    PreparedStatement ptmt = null;
	    ResultSet rs = null;
	    ArrayList<SensorDataDTO> list = new ArrayList<>();

	    try {
	        con = DBUtil.getConnect();
	        ptmt = con.prepareStatement(sql);
	        
	        //조건에 맞으면 인덱스를 올려서 ?에 값을 받게끔 
	        int idx = 1;
	        ptmt.setString(idx++, farmUid);
	        if (hours != null) ptmt.setInt(idx++, hours);
	        if (limit != null) ptmt.setInt(idx++, limit);

	        rs = ptmt.executeQuery();
	        while (rs.next()) {
	            SensorDataDTO s = new SensorDataDTO(
	                rs.getLong("log_uid"),
	                rs.getString("farm_uid"),
	                rs.getTimestamp("recorded_at"),
	                rs.getFloat("measured_temp"),
	                rs.getFloat("measured_humidity"),
	                rs.getFloat("measured_Light"),
	                rs.getFloat("measured_co2"),
	                rs.getFloat("measured_soil_moisture")
	            );
	            list.add(s);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs, ptmt, con);
	    }

	    return list;
	}

	@Override
	public List<SensorDataDTO> getLogsByUser(int userUid, Integer hours, Integer limit) {
		String sql = "select sl.* "
				+ "from sensor_logs sl "
				+ "join farms f on sl.farm_uid = f.farm_uid " 
				+ "where f.user_id = ? ";
		// 선택적으로 시간 조건 추가
		if(hours != null) {
			sql += "and recorded_at >= now() - interval ? hour";
			// ex)'2025-10-23 16:00:00' - '2025-10-23 ?:00:00'
		}
		
		sql += "order by recorded_at desc";
		
		if(limit != null) {
			sql += "limit ?";
		}
		Connection con = null;
	    PreparedStatement ptmt = null;
	    ResultSet rs = null;
	    ArrayList<SensorDataDTO> list = new ArrayList<SensorDataDTO>();
		
	    try {
	    	con = DBUtil.getConnect();
	        ptmt = con.prepareStatement(sql);
	        //조건에 맞으면 인덱스를 올려서 ?에 값을 받게끔 
	        int index = 1;
	        ptmt.setInt(index++, userUid);
	        if( hours != null) ptmt.setInt(index++, hours);
	        if (limit != null) ptmt.setInt(index++, limit);

	        rs = ptmt.executeQuery();
	        while (rs.next()) {
	            SensorDataDTO s = new SensorDataDTO(
	                rs.getLong("log_uid"),
	                rs.getString("farm_uid"),
	                rs.getTimestamp("recorded_at"),
	                rs.getFloat("measured_temp"),
	                rs.getFloat("measured_humidity"),
	                rs.getFloat("measuredLight"),
	                rs.getFloat("measured_co2"),
	                rs.getFloat("measured_soil_moisture")
	            );
	            list.add(s);
	        }

		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
		
	}
}

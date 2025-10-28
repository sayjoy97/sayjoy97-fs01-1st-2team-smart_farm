package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.PresetDTO;
import util.DBUtil;

public class PlantDAOImpl implements PlantDAO {
	@Override
	public int addCustomPreset(PresetDTO presetDTO) {
		String sql = "insert into plant_presets values(null,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ptmt = null;
		int result = 0;
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, presetDTO.getPlantName());
			ptmt.setFloat(2, presetDTO.getOptimalTemp());
			ptmt.setFloat(3, presetDTO.getOptimalHumidity());
			ptmt.setFloat(4, presetDTO.getLightIntensity());
			ptmt.setFloat(5, presetDTO.getCo2Level());
			ptmt.setFloat(6, presetDTO.getSoilMoisture());
			ptmt.setFloat(7, presetDTO.getGrowthPeriodDays());
			result = ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(null, ptmt, con);
		}
		return result;
	}

	@Override
	public PresetDTO selectPreset(int presetUid) {
		String sql = "select * from plant_presets where preset_uid = ?";
		Connection con = null;
		PreparedStatement ptmt = null;
		PresetDTO presetDTO = new PresetDTO();
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, presetUid);
			ResultSet rs = ptmt.executeQuery();
			while (rs.next()) {
				presetDTO.setPresetUid(rs.getInt(1));
				presetDTO.setPlantName(rs.getString(2));
				presetDTO.setOptimalTemp(rs.getFloat(3));
				presetDTO.setOptimalHumidity(rs.getFloat(4));
				presetDTO.setLightIntensity(rs.getFloat(5));
				presetDTO.setCo2Level(rs.getFloat(6));
				presetDTO.setSoilMoisture(rs.getFloat(7));
				presetDTO.setGrowthPeriodDays(rs.getInt(8));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(null, ptmt, con);
		}
		return presetDTO;
	}


}

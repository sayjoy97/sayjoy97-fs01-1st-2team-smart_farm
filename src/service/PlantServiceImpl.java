package service;

import dao.PlantDAO;
import dao.PlantDAOImpl;
import dto.PresetDTO;

public class PlantServiceImpl implements PlantService {
	private PlantDAO plantDAO = new PlantDAOImpl();
	@Override
	public int addCustomPreset(PresetDTO presetDTO) {
		int result = plantDAO.addCustomPreset(presetDTO);
		return result;
	}
	public PresetDTO selectPreset(int presetUid) {
		PresetDTO presetDTO = plantDAO.selectPreset(presetUid);
		return presetDTO;
	}
	
	public int updatePreset(String farmUid, PresetDTO presetDTO) {
		return plantDAO.updatePreset(farmUid, presetDTO);
	}
}

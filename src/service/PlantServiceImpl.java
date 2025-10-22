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
}

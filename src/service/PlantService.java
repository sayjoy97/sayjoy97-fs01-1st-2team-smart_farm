package service;

import dto.PresetDTO;

public interface PlantService {
	int addCustomPreset(PresetDTO presetDTO);
	PresetDTO selectPreset(int presetUid);
	int updatePreset(String farmUid, PresetDTO presetDTO);
}

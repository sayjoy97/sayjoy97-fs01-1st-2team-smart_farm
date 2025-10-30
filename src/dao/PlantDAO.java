package dao;

import dto.PresetDTO;

public interface PlantDAO {
	int addCustomPreset(PresetDTO presetDTO);
	PresetDTO selectPreset(int presetUid);
	int updatePreset(String farmUid, PresetDTO presetDTO);
}

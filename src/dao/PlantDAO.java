package dao;

import dto.PresetDTO;

public interface PlantDAO {
	int addCustomPreset(PresetDTO presetDTO);
	PresetDTO selectPreset(int presetUid);
}

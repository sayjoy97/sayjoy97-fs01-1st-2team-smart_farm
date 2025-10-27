package service;

import java.util.ArrayList;

import dto.DeviceDTO;
import dto.FarmDTO;
import dto.MemberDTO;
import dto.PresetDTO;

public interface FarmService {
	ArrayList<FarmDTO> selectDevicesFarm(MemberDTO user);
	void createFarm(MemberDTO user);
	void createFarm(MemberDTO user, String dsn);
	int addFarm(String plantName, String farmUid);
	void deleteFarm(String deleteDSN);
	String getPlantName(String farmUid);
	PresetDTO selectPresetByFarmUid(String farmUid);
}

package dao;

import java.util.ArrayList;

import dto.DeviceDTO;
import dto.FarmDTO;
import dto.MemberDTO;
import dto.PresetDTO;

public interface FarmDAO {
	int createFarm(MemberDTO user);
	int createFarm(MemberDTO user, String dsn);
	int addFarm(String plantName, String farmUid);
	ArrayList<FarmDTO> selectDevicesFarm(MemberDTO user);
	int deleteFarm(String deleteDSN);
	String getPlantName(String farmUid);
	PresetDTO selectPresetByFarmUid(String farmUid);
}

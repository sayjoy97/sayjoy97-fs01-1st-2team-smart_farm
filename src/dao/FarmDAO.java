package dao;

import java.util.ArrayList;

import dto.DeviceDTO;
import dto.FarmDTO;
import dto.MemberDTO;

public interface FarmDAO {
	int createFarm(MemberDTO user);
	int createFarm(MemberDTO user, String dsn);
	int addFarm(String plantName, String farmUid);
	ArrayList<FarmDTO> selectDevicesFarm(MemberDTO user);
	int deleteFarm(String deleteDSN);
}

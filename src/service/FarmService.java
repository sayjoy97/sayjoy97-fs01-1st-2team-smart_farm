package service;

import java.util.ArrayList;

import dto.DeviceDTO;
import dto.FarmDTO;
import dto.MemberDTO;

public interface FarmService {
	ArrayList<FarmDTO> selectDevicesFarm(MemberDTO user);
	void createFarm(MemberDTO user);
	void createFarm(MemberDTO user, String dsn);
	void addFarm(String plantName, String farmUid);
}

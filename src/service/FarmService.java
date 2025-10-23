package service;

import java.util.ArrayList;

import dto.DeviceDTO;
import dto.FarmDTO;
import dto.MemberDTO;

public interface FarmService {
	public void createFarm(MemberDTO user);
	public void createFarm(MemberDTO user, String dsn);
	public void addFarm(String plantName, String farmUid);
	ArrayList<FarmDTO> selectDevicesFarm(MemberDTO user);
}

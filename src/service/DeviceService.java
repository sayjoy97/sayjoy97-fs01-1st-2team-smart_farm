package service;

import java.util.ArrayList;

import dto.DeviceDTO;
import dto.MemberDTO;

public interface DeviceService {
	int addDevice(MemberDTO user);
	int addNewDevice(MemberDTO user, String dsn);
	ArrayList<DeviceDTO> selectUserDevices(MemberDTO user);
	void deleteDevice(String deleteDSN);
}

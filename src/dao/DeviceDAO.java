package dao;

import java.util.ArrayList;

import dto.DeviceDTO;
import dto.MemberDTO;

public interface DeviceDAO {
	int addDevice(MemberDTO user);
	ArrayList<DeviceDTO> selectUserDevices(MemberDTO user);
	int addNewDevice(MemberDTO user, String dsn);
	int deleteDevice(String deleteDSN);
}

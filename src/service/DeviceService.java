package service;

import dto.MemberDTO;

public interface DeviceService {
	int addDevice(MemberDTO user);
	int addNewDevice(MemberDTO user, String dsn);
}

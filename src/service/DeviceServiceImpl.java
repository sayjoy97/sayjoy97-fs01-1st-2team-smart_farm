package service;

import java.util.ArrayList;

import dao.DeviceDAO;
import dao.DeviceDAOImpl;
import dto.DeviceDTO;
import dto.MemberDTO;

public class DeviceServiceImpl implements DeviceService {
	private DeviceDAO deviceDAO = new DeviceDAOImpl();
	
	public int addDevice(MemberDTO user) {
		int result = deviceDAO.addDevice(user);
		return result;
	}
	
	public int addNewDevice(MemberDTO user, String dsn) {
		int result = deviceDAO.addNewDevice(user, dsn);
		return result;
	}
	
	public ArrayList<DeviceDTO> selectUserDevices(MemberDTO user){
		return deviceDAO.selectUserDevices(user);
	}
	
	
}

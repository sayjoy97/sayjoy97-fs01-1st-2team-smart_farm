package service;

import java.util.ArrayList;

import dao.FarmDAO;
import dao.FarmDAOImpl;
import dto.FarmDTO;
import dto.MemberDTO;
import dto.PresetDTO;
import mqtt.MqttManager;

public class FarmServiceImpl implements FarmService {
	FarmDAO farmDAO = new FarmDAOImpl();
	public void createFarm(MemberDTO user) {
		farmDAO.createFarm(user);
	}
	
	public void createFarm(MemberDTO user, String dsn) {
		farmDAO.createFarm(user, dsn);
	}
	
	public int addFarm(String plantName, String farmUid) {
		return farmDAO.addFarm(plantName, farmUid);
	}

	@Override
	public ArrayList<FarmDTO> selectDevicesFarm(MemberDTO user) {
		// TODO Auto-generated method stub
		return farmDAO.selectDevicesFarm(user);
	}
	
	public void deleteFarm(String deleteDSN) {
		farmDAO.deleteFarm(deleteDSN);
	}
	
	public String getPlantName(String farmUid) {
		return farmDAO.getPlantName(farmUid);
	}
	
	public PresetDTO selectPresetByFarmUid(String farmUid) {
		return farmDAO.selectPresetByFarmUid(farmUid);
	}
}

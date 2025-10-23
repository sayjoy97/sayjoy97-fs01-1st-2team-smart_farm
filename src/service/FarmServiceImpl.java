package service;

import dao.FarmDAO;
import dao.FarmDAOImpl;
import dto.MemberDTO;

public class FarmServiceImpl implements FarmService {
	FarmDAO farmDAO = new FarmDAOImpl();
	public void createFarm(MemberDTO user) {
		farmDAO.createFarm(user);
	}
	
	public void createFarm(MemberDTO user, String dsn) {
		farmDAO.createFarm(user, dsn);
	}
	
	public void addFarm(String plantName, String farmUid) {
		farmDAO.addFarm(plantName, farmUid);
	}
}

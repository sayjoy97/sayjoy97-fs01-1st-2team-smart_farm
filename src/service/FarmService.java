package service;

import dto.MemberDTO;

public interface FarmService {
	public void createFarm(MemberDTO user);
	public void createFarm(MemberDTO user, String dsn);
	public void addFarm(String plantName, String farmUid);
}

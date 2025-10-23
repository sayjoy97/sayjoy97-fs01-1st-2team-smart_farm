package service;

import dto.MemberDTO;

public interface FarmService {
	void createFarm(MemberDTO user);
	void createFarm(MemberDTO user, String dsn);
	void addFarm(String plantName, String farmUid);
}

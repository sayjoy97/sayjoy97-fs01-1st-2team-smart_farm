package dao;

import dto.MemberDTO;

public interface FarmDAO {
	int createFarm(MemberDTO user);
	int createFarm(MemberDTO user, String dsn);
	int addFarm(String plantName, String farmUid);
}

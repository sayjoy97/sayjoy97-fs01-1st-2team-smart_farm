package service;

import dto.MemberDTO;

public interface MemberService {
	int register(MemberDTO user);
	MemberDTO login(String userId, String password);
	int addDevice(MemberDTO user);
}

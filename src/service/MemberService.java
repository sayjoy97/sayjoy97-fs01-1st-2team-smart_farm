package service;

import dto.MemberDTO;

public interface MemberService {
	int register(MemberDTO user);
	MemberDTO login(String userId, String password);
	MemberDTO findQA(String email);
	void updateUserInfo(int userUid, String string, String newPw);
}

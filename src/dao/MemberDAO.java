package dao;

import dto.MemberDTO;

public interface MemberDAO {
	int register(MemberDTO user);
	MemberDTO login(String userId, String password);
	MemberDTO findQA(String email);
}

package service;

import dao.MemberDAO;
import dao.MemberDAOImpl;
import dto.MemberDTO;

public class MemberServiceImpl implements MemberService {
	private final MemberDAO memberDAO = new MemberDAOImpl();
	@Override
	public int register(MemberDTO user) {
		int result = memberDAO.register(user);

        return result;
	}

	@Override
	public MemberDTO login(String id, String pass) {
		MemberDTO user = memberDAO.login(id, pass);
		System.out.println(user);
		return user;
	}

}

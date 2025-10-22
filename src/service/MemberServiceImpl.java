package service;

import dao.DeviceDAO;
import dao.DeviceDAOImpl;
import dao.MemberDAO;
import dao.MemberDAOImpl;
import dto.MemberDTO;

public class MemberServiceImpl implements MemberService {
	private final MemberDAO memberDAO = new MemberDAOImpl();
	private DeviceDAO deviceDAO = new DeviceDAOImpl();
	@Override
	public int register(MemberDTO user) {
		int result = memberDAO.register(user);

        return result;
	}

	@Override
	public MemberDTO login(String userId, String password) {
		MemberDTO user = memberDAO.login(userId, password);
		System.out.println(user);
		return user;
	}
	
	public int addDevice(MemberDTO user) {
		int result = deviceDAO.addDevice(user);
		return result;
	}
}

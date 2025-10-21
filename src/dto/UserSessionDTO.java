package dto;

public class UserSessionDTO {
	MemberDTO loginUser;
	public UserSessionDTO() {
		
	}
	public UserSessionDTO(MemberDTO loginUser) {
		super();
		this.loginUser = loginUser;
	}

	@Override
	public String toString() {
		return "UserSessionDTO [loginUser=" + loginUser + "]";
	}

	public MemberDTO getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(MemberDTO loginUser) {
		this.loginUser = loginUser;
	}
	
}

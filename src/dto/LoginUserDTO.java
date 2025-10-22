package dto;

public class LoginUserDTO {
    private String userId;      // 아이디
    private String password;
    public LoginUserDTO() {
    }
	public LoginUserDTO(String userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
	}
	@Override
	public String toString() {
		return "LoginUserDTO [userId=" + userId + ", password=" + password + "]";
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	} 
}

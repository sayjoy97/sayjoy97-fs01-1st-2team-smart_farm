package dto;

public class LoginUserDTO {
    private String userId;      // 아이디
    private String pass;
    public LoginUserDTO() {
    	
    }
	public LoginUserDTO(String userId, String pass) {
		super();
		this.userId = userId;
		this.pass = pass;
	}
	@Override
	public String toString() {
		return "LoginUserDTO [userId=" + userId + ", pass=" + pass + "]";
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	} 
    
}

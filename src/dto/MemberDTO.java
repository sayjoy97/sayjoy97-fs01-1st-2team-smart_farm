package dto;

import java.sql.Timestamp;

public class MemberDTO {
    private int userUid;
    private String userId;
    private String password;
    private String email;
    private String name;
    private String securityQuestion;
    private String securityAnswer;
    private String deviceSerialNumber;

    public MemberDTO() {
    }
    public MemberDTO(int userUid, String userId, String password, String email, String name, String securityQuestion,
    		String securityAnswer, String deviceSerialNumber) {
		super();
		this.userUid = userUid;
		this.userId = userId;
		this.password = password;
		this.email = email;
		this.name = name;
		this.securityQuestion = securityQuestion;
		this.securityAnswer = securityAnswer;
		this.deviceSerialNumber = deviceSerialNumber;
	}
    
	public int getUserUid() {
		return userUid;
	}
	public void setUserUid(int userUid) {
		this.userUid = userUid;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecurityQuestion() {
		return securityQuestion;
	}
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}
	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}
	@Override
	public String toString() {
		return "MemberDTO [userUid=" + userUid + ", userId=" + userId + ", password=" + password + ", email=" + email
				+ ", name=" + name + ", securityQuestion=" + securityQuestion + ", securityAnswer=" + securityAnswer
				+ ", deviceSerialNumber=" + deviceSerialNumber + "]";
	}
}
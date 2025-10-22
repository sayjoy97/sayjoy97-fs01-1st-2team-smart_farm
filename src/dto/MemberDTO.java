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

    // 기본 생성자
    public MemberDTO() {
    }
    public MemberDTO(int userUid, String userId, String password, String email, String name, String securityQuestion,
    		String securityAnswer) {
		super();
		this.userUid = userUid;
		this.userId = userId;
		this.password = password;
		this.email = email;
		this.name = name;
		this.securityQuestion = securityQuestion;
		this.securityAnswer = securityAnswer;
	}
    
    // --- Getter and Setter ---
    
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
	@Override
	public String toString() {
		return "MemberDTO [userUid=" + userUid + ", userId=" + userId + ", password=" + password + ", email=" + email
				+ ", name=" + name + ", securityQuestion=" + securityQuestion + ", securityAnswer=" + securityAnswer
				+ "]";
	}
}
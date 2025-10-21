package dto;

import java.sql.Timestamp;

public class MemberDTO {

    private int id;             // 회원 고유 번호 (PK)
    private String userId;      // 아이디
    private String pass;    // 비밀번호
    private String name;    // 성명
    private String email;       // 이메일
    private String address;     // 주소
    private Timestamp createdAt;  // 가입일

    // 기본 생성자
    public MemberDTO() {
    }
    public MemberDTO(int id, String userId, String pass, String name, String email, String address,
			Timestamp createdAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.pass = pass;
		this.name = name;
		this.email = email;
		this.address = address;
		this.createdAt = createdAt;
	}

   
    // --- Getter and Setter ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
	@Override
	public String toString() {
		return "MemberDTO [id=" + id + ", userId=" + userId + ", pass=" + pass + ", name=" + name + ", email=" + email
				+ ", address=" + address + ", createdAt=" + createdAt + "]";
	}

}
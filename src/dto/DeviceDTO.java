package dto;

public class DeviceDTO {
	private int devuceUid;
	private int userUid;
	private int specUid;
	
	public DeviceDTO() {
	}
	public DeviceDTO(int devuceUid, int userUid, int specUid) {
		super();
		this.devuceUid = devuceUid;
		this.userUid = userUid;
		this.specUid = specUid;
	}
	public int getDevuceUid() {
		return devuceUid;
	}
	public void setDevuceUid(int devuceUid) {
		this.devuceUid = devuceUid;
	}
	public int getUserUid() {
		return userUid;
	}
	public void setUserUid(int userUid) {
		this.userUid = userUid;
	}
	public int getSpecUid() {
		return specUid;
	}
	public void setSpecUid(int specUid) {
		this.specUid = specUid;
	}
	@Override
	public String toString() {
		return "DeviceDTO [devuceUid=" + devuceUid + ", userUid=" + userUid + ", specUid=" + specUid + "]";
	}
}

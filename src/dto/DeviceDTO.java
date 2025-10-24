package dto;

public class DeviceDTO {
	private String deviceSerialNumber;
	private int userUid;
	private int specUid;
	
	public DeviceDTO() {
	}
	public DeviceDTO(String deviceSerialNumber, int userUid, int specUid) {
		super();
		this.deviceSerialNumber = deviceSerialNumber;
		this.userUid = userUid;
		this.specUid = specUid;
	}
	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}
	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
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
		return "DeviceDTO [deviceSerialNumber=" + deviceSerialNumber + ", userUid=" + userUid + ", specUid=" + specUid + "]";
	}
}

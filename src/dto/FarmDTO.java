package dto;

import java.util.Date;

public class FarmDTO {
	private String farmUid;
	private int userUid;
	private int presetUid;
	private Date planting_date;
	
	public FarmDTO() {
	}
	public FarmDTO(String farmUid, int userUid, int presetUid, Date planting_date) {
		super();
		this.farmUid = farmUid;
		this.userUid = userUid;
		this.presetUid = presetUid;
		this.planting_date = planting_date;
	}
	public String getFarmUid() {
		return farmUid;
	}
	public void setFarmUid(String farmUid) {
		this.farmUid = farmUid;
	}
	public int getUserUid() {
		return userUid;
	}
	public void setUserUid(int userUid) {
		this.userUid = userUid;
	}
	public int getPresetUid() {
		return presetUid;
	}
	public void setPresetUid(int presetUid) {
		this.presetUid = presetUid;
	}
	public Date getPlanting_date() {
		return planting_date;
	}
	public void setPlanting_date(Date planting_date) {
		this.planting_date = planting_date;
	}
	@Override
	public String toString() {
		return "FarmDTO [farmUid=" + farmUid + ", userUid=" + userUid + ", presetUid=" + presetUid + ", planting_date="
				+ planting_date + "]";
	}
}

package dto;

public class PresetDTO {
	private int presetUid;
	private String plantName;
	private float optimalTemp;
	private float optimalHumidity;
	private float lightIntensity;
	private float co2Level;
	private float soilMoisture;
	private int growthPeriodDays;
	
	public PresetDTO() {
	}
	public PresetDTO(int presetUid, String plantName, float optimalTemp, float optimalHumidity,
			float lightIntensity, float co2Level, float soilMoisture, int growthPeriodDays) {
		super();
		this.presetUid = presetUid;
		this.plantName = plantName;
		this.optimalTemp = optimalTemp;
		this.optimalHumidity = optimalHumidity;
		this.lightIntensity = lightIntensity;
		this.co2Level = co2Level;
		this.soilMoisture = soilMoisture;
		this.growthPeriodDays = growthPeriodDays;
	}
	public int getPresetUid() {
		return presetUid;
	}
	public void setPresetUid(int presetUid) {
		this.presetUid = presetUid;
	}
	public String getPlantName() {
		return plantName;
	}
	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}
	public float getOptimalTemp() {
		return optimalTemp;
	}
	public void setOptimalTemp(float optimalTemp) {
		this.optimalTemp = optimalTemp;
	}
	public float getOptimalHumidity() {
		return optimalHumidity;
	}
	public void setOptimalHumidity(float optimalHumidity) {
		this.optimalHumidity = optimalHumidity;
	}
	public float getLightIntensity() {
		return lightIntensity;
	}
	public void setLightIntensity(float lightIntensity) {
		this.lightIntensity = lightIntensity;
	}
	public float getCo2Level() {
		return co2Level;
	}
	public void setCo2Level(float co2Level) {
		this.co2Level = co2Level;
	}
	public float getSoilMoisture() {
		return soilMoisture;
	}
	public void setSoilMoisture(float soilMoisture) {
		this.soilMoisture = soilMoisture;
	}
	public int getGrowthPeriodDays() {
		return growthPeriodDays;
	}
	public void setGrowthPeriodDays(int growthPeriodDays) {
		this.growthPeriodDays = growthPeriodDays;
	}
	@Override
	public String toString() {
		return "AddPlantServiceDTO [presetUid=" + presetUid + ", plantName=" + plantName + ", optimalTemp="
				+ optimalTemp + ", optimalHumidity=" + optimalHumidity + ", lightIntensity=" + lightIntensity
				+ ", co2Level=" + co2Level + ", soilMoisture=" + soilMoisture + ", growthPeriodDays=" + growthPeriodDays
				+ "]";
	}
}

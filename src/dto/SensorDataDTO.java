package dto;

import java.sql.Timestamp;

public class SensorDataDTO {
	    private long logId;
	    private String farmUid;
	    private Timestamp recordedAt; // 센서 데이터가 기록된 시각 (MySQL DATETIME과 매핑)
	    private Float measuredTemp;
	    private Float measuredHumidity;
	    private Float measuredLight;
	    private Float measuredCo2;
	    private Float measuredSoilMoisture;
	    public SensorDataDTO() {
	    	
	    }
	    
		public SensorDataDTO(long logId, String farmUid, Timestamp recordedAt, Float measuredTemp, Float measuredHumidity,
				Float measuredLight, Float measuredCo2, Float measuredSoilMoisture) {
			super();
			this.logId = logId;
			this.farmUid = farmUid;
			this.recordedAt = recordedAt;
			this.measuredTemp = measuredTemp;
			this.measuredHumidity = measuredHumidity;
			this.measuredLight= measuredLight;
			this.measuredCo2 = measuredCo2;
			this.measuredSoilMoisture = measuredSoilMoisture;
		}

		public long getLogId() {
			return logId;
		}

		public void setLogId(long logId) {
			this.logId = logId;
		}

		public String getFarmUid() {
			return farmUid;
		}

		public void setFarmUid(String farmUid) {
			this.farmUid = farmUid;
		}

		public Timestamp getRecordedAt() {
			return recordedAt;
		}

		public void setRecordedAt(Timestamp recordedAt) {
			this.recordedAt = recordedAt;
		}

		public Float getMeasuredTemp() {
			return measuredTemp;
		}

		public void setMeasuredTemp(Float measuredTemp) {
			this.measuredTemp = measuredTemp;
		}

		public Float getMeasuredHumidity() {
			return measuredHumidity;
		}

		public void setMeasuredHumidity(Float measuredHumidity) {
			this.measuredHumidity = measuredHumidity;
		}

		public Float getMeasuredLight() {
			return measuredLight;
		}

		public void setMeasuredLight(Float measuredLight) {
			this.measuredLight = measuredLight;
		}

		public Float getMeasuredCo2() {
			return measuredCo2;
		}

		public void setMeasuredCo2(Float measuredCo2) {
			this.measuredCo2 = measuredCo2;
		}

		public Float getMeasuredSoilMoisture() {
			return measuredSoilMoisture;
		}

		public void setMeasuredSoilMoisture(Float measuredSoilMoisture) {
			this.measuredSoilMoisture = measuredSoilMoisture;
		}
		@Override
		public String toString() {
			return "SensorDataDTO [logId=" + logId + ", farmUid=" + farmUid + ", recordedAt=" + recordedAt
					+ ", measuredTemp=" + measuredTemp + ", measuredHumidity=" + measuredHumidity + ", measuredLight="
					+ measuredLight + ", measuredCo2=" + measuredCo2 + ", measuredSoilMoisture=" + measuredSoilMoisture
					+ "]";
		}
}

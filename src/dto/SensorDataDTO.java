package dto;

import java.sql.Timestamp;

public class SensorDataDTO {
	    private long logId;
	    private int farmUid;
	    private Timestamp recordedAt; // 센서 데이터가 기록된 시각 (MySQL DATETIME과 매핑)
	    private Float measuredTemp;
	    private Float measuredHumidity;
	    private Float measuredCo2;
	    private Float measuredSoilMoisture;
	    public SensorDataDTO() {
	    	
	    }
	    
		public SensorDataDTO(long logId, int farmUid, Timestamp recordedAt, Float measuredTemp, Float measuredHumidity,
				Float measuredCo2, Float measuredSoilMoisture) {
			super();
			this.logId = logId;
			this.farmUid = farmUid;
			this.recordedAt = recordedAt;
			this.measuredTemp = measuredTemp;
			this.measuredHumidity = measuredHumidity;
			this.measuredCo2 = measuredCo2;
			this.measuredSoilMoisture = measuredSoilMoisture;
		}

		@Override
		public String toString() {
			return "SensorDataDTO [logId=" + logId + ", farmUid=" + farmUid + ", recordedAt=" + recordedAt
					+ ", measuredTemp=" + measuredTemp + ", measuredHumidity=" + measuredHumidity + ", measuredCo2="
					+ measuredCo2 + ", measuredSoilMoisture=" + measuredSoilMoisture + "]";
		}

		public long getLogId() {
			return logId;
		}

		public void setLogId(long logId) {
			this.logId = logId;
		}

		public int getFarmUid() {
			return farmUid;
		}

		public void setFarmUid(int farmUid) {
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
		

}

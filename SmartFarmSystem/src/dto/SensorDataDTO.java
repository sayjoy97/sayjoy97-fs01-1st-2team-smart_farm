package dto;

import java.sql.Timestamp;

public class SensorDataDTO {
	    private int logId;
	    private String id;
	    private String location;
	    private String sensorType;
	    private String sensorValue;
	    private Timestamp createdAt;
	    public SensorDataDTO() {
	    	
	    }
		public SensorDataDTO(int logId, String id, String location, String sensorType, String sensorValue,
				Timestamp createdAt) {
			super();
			this.logId = logId;
			this.id = id;
			this.location = location;
			this.sensorType = sensorType;
			this.sensorValue = sensorValue;
			this.createdAt = createdAt;
		}
		@Override
		public String toString() {
			return "SensorDataDTO [logId=" + logId + ", id=" + id + ", location=" + location + ", sensorType="
					+ sensorType + ", sensorValue=" + sensorValue + ", createdAt=" + createdAt + "]";
		}
		public int getLogId() {
			return logId;
		}
		public void setLogId(int logId) {
			this.logId = logId;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getSensorType() {
			return sensorType;
		}
		public void setSensorType(String sensorType) {
			this.sensorType = sensorType;
		}
		public String getSensorValue() {
			return sensorValue;
		}
		public void setSensorValue(String sensorValue) {
			this.sensorValue = sensorValue;
		}
		public Timestamp getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(Timestamp createdAt) {
			this.createdAt = createdAt;
		}

}

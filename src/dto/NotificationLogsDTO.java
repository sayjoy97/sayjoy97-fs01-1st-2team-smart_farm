package dto;

import java.sql.Timestamp;

public class NotificationLogsDTO {
	private int notificationLogUid;
	private String deviceSerialNumber;
	private String logMessage;
	private Timestamp recordedAt;
	
	public NotificationLogsDTO() {
	}
	public NotificationLogsDTO(int notificationLogUid, String deviceSerialNumber, String logMessage,
			Timestamp recordedAt) {
		super();
		this.notificationLogUid = notificationLogUid;
		this.deviceSerialNumber = deviceSerialNumber;
		this.logMessage = logMessage;
		this.recordedAt = recordedAt;
	}
	public int getNotificationLogUid() {
		return notificationLogUid;
	}
	public void setNotificationLogUid(int notificationLogUid) {
		this.notificationLogUid = notificationLogUid;
	}
	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}
	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}
	public String getLogMessage() {
		return logMessage;
	}
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	public Timestamp getRecordedAt() {
		return recordedAt;
	}
	public void setRecordedAt(Timestamp recordedAt) {
		this.recordedAt = recordedAt;
	}
	@Override
	public String toString() {
		return "NotificationLogsDTO [notificationLogUid=" + notificationLogUid + ", deviceSerialNumber="
				+ deviceSerialNumber + ", logMessage=" + logMessage + ", recordedAt=" + recordedAt + "]";
	}
}

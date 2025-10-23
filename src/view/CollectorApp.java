package view;

import mqtt.MqttManager;

public class CollectorApp {
	public static void main(String[] args) {
		new MqttManager(true); // 수집기 모드
		while (true) {
			try { Thread.sleep(10000); } catch (InterruptedException e) {}
		}
	}
}

package mqtt;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import dto.PresetDTO;
import service.DeviceService;
import service.DeviceServiceImpl;
import service.FarmService;
import service.FarmServiceImpl;
import service.NotificationService;
import service.NotificationServiceImpl;
import service.SensorDataService;
import service.SensorDataServiceImpl;

public class MqttManager implements MqttCallback { // MqttCallback을 직접 구현
	private String id;
    private MqttClient client;
    private boolean DBServerMode;
    private final String broker = "tcp://localhost:1883";
    private String pubTopic; // 유저, 기계 식별 앞에 붙이기
    private String subTopic; // {유저}/smartfarm 하위의 모든 토픽을 구독
    private SensorDataService sensorService = new SensorDataServiceImpl();
    private NotificationService notificationService = new NotificationServiceImpl();
    private DeviceService deviceService = new DeviceServiceImpl();
    private FarmService farmService = new FarmServiceImpl();
    public String getPubTopic() {
		return pubTopic;
	}
    
    //라즈베리파이 명령어 보내기 명령 보낼 대상은 액추에어터 뿐이므로
	public void setPubTopic(String farmUid, String actuatorType) {
		this.pubTopic = id+"/smartfarm/"+farmUid+"/cmd/"+actuatorType;
	}
	
	
	public String getSubTopic() {
		return subTopic;
	}
	
	//id에 등록된 모든 기기들의 알림을 받아옴
	public void setSubTopic() {
		this.subTopic = id+"/smartfarm/+/sensor/nl";  // 알림만 구독
	}
	
	public MqttManager(String id) {//사용자 모드 생성자
    	this.id = id;
        try {
        	
            // 고유한 클라이언트 ID 생성 (충돌 방지)
            String clientId = "combined_client_" + UUID.randomUUID().toString();
            client = new MqttClient(broker, clientId);

            // 연결 옵션 설정
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // MqttCallback 인터페이스를 현재 클래스가 구현했으므로 this로 설정
            client.setCallback(this);

            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected.");

            // 연결 후 바로 구독 시작
            this.subscribe();

        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
    public MqttManager(boolean DBServerMode) {//수집기 모드 생성자
        try {
        	this.DBServerMode = DBServerMode;
            // 고유한 클라이언트 ID 생성 (충돌 방지)
            String clientId = "DBServer"+ UUID.randomUUID().toString();
            client = new MqttClient(broker, clientId);

            // 연결 옵션 설정
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true); // 자동 재연결 활성화

            // MqttCallback 인터페이스를 현재 클래스가 구현했으므로 this로 설정
            client.setCallback(this);

            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected.");

            // 연결 후 바로 구독 시작
            this.subscribe();

        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
    // 구독을 처리하는 메소드
    private void subscribe() {
        try {
            if (DBServerMode) {
                // 센서 데이터 및 알림 수신
                this.client.subscribe("smartfarm/+/sensor/#", 1);
                System.out.println("Subscribed to topic: smartfarm/+/sensor/#");
                
                // 프리셋 요청 수신
                this.client.subscribe("smartfarm/+/preset/request", 1);
                System.out.println("Subscribed to topic: smartfarm/+/preset/request");
            } else {
            	setSubTopic();
                this.client.subscribe(subTopic);
                System.out.println("Subscribed to topic: " + subTopic);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    
    // 발행을 처리하는 메소드 {userId}/smartfarm/{farmUid}/cmd/{actuatorType} << 액추에이터 작동 요청
    public void publish(String farmUid, String actuatorType, String cmd) {
        try {
            System.out.println(farmUid+"에" + actuatorType+" : "+cmd+" 요청을 보냅니다.");
            MqttMessage message = new MqttMessage(cmd.getBytes());
            message.setQos(0); // QoS Level 0
            this.client.publish(id+"/smartfarm/"+farmUid+"/cmd/"+actuatorType, message);
            System.out.println("Message published.");
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
    //라즈베리파이에 프리셋 업데이트 발행 (유저가 설정 변경 시)
    public void publishPresetUpdate(String farmUid, PresetDTO preset) {
        try {
            System.out.println(farmUid+"에 프리셋 업데이트를 전송합니다.");
            
            String presetmsg = "Co2Level="+String.valueOf(preset.getCo2Level())
            		+";LightIntensity="+ String.valueOf(preset.getLightIntensity())
            		+ ";OptimalHumidity="+String.valueOf(preset.getOptimalHumidity())
            		+ ";OptimalTemp="+String.valueOf(preset.getOptimalTemp())
            		+ ";SoilMoisture="+String.valueOf(preset.getSoilMoisture());

            MqttMessage message = new MqttMessage(presetmsg.getBytes());
            message.setQos(1); // QoS Level 1
            this.client.publish("smartfarm/"+farmUid+"/preset", message);
            System.out.println("프리셋 업데이트 발행 완료: smartfarm/"+farmUid+"/preset");
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
    
    //프리셋 요청에 대한 응답 발행 (DB 조회 후)
    public void publishPresetResponse(String farmUid, PresetDTO preset) {
        try {
            String presetmsg;
            if (preset != null) {
                presetmsg = "OptimalTemp="+String.valueOf(preset.getOptimalTemp())
                        + ";OptimalHumidity="+String.valueOf(preset.getOptimalHumidity())
                        + ";LightIntensity="+ String.valueOf(preset.getLightIntensity())
                        + ";SoilMoisture="+String.valueOf(preset.getSoilMoisture())
                        + ";Co2Level="+String.valueOf(preset.getCo2Level());
                System.out.println(farmUid+"에 대한 프리셋 응답: " + presetmsg);
            } else {
                presetmsg = "none";
                System.out.println(farmUid+"에 대한 프리셋 없음 (기본값 사용)");
            }

            MqttMessage message = new MqttMessage(presetmsg.getBytes());
            message.setQos(1); // QoS Level 1
            this.client.publish("smartfarm/"+farmUid+"/preset/response", message);
            System.out.println("프리셋 응답 발행 완료: smartfarm/"+farmUid+"/preset/response");
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
    //실시간 센서 정보 요청용 퍼블리시 메서드 오버로딩 >> 라즈베리파이에서 info 메세지를 받으면 센서 정보를 읽어서 보내도록 수행해야 하도록 구현해야 함.
    public void publish() {
        try {
        	String cmd = "info";
            System.out.println("내 농장의 현재 환경 정보 요청을 보냅니다.");
            MqttMessage message = new MqttMessage(cmd.getBytes());
            message.setQos(0); // QoS Level 0
            this.client.publish(id+"/smartfarm/+/cmd/", message);
            System.out.println("Message published.");
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
    
    // 연결을 종료하는 메소드
    public void close() {
        try {
            this.client.disconnect();
            this.client.close();
            System.out.println("Disconnected.");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // ---- MqttCallback 인터페이스의 메소드들 ---- //

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.getMessage());
        
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // 이 메소드는 메시지가 도착할 때마다 Paho 라이브러리에 의해 자동으로 호출됩니다.
        String payload = new String(message.getPayload());
        
        // 토픽별 분기 처리
        if(topic.endsWith("/sensor/data")) {
        	// 센서 데이터 저장 (DB 서버 모드)
        	sensorService.saveData(topic, payload);
        	
        } else if(topic.endsWith("/sensor/nl")) {
        	if (DBServerMode) {
        		// DB 서버 모드: 알림 저장 + 해당 유저에게 중계
        		notificationService.saveNotification(topic, payload);
        		
        		// deviceSerial 추출 (예: smartfarm/A1001/sensor/nl → A1001)
        		String deviceSerial = extractDeviceSerial(topic);
        		
        		// DB에서 device_serial로 user_id 조회
        		String userId = deviceService.getUserIdByDeviceSerial(deviceSerial);
        		
        		if (userId != null) {
        			// 해당 유저에게 알림 중계
        			publishNotificationToUser(userId, deviceSerial, payload);
        		} else {
        			System.out.println("⚠️  deviceSerial=" + deviceSerial + "에 연결된 유저 없음");
        		}
        		
        	} else {
        		// 유저 모드: 알림 수신 처리 (UI 표시 등)
        		System.out.println("🔔 알림 수신: " + payload);
        		// TODO: UI에 알림 표시
        	}
        	
        } else if(topic.endsWith("/preset/request")) {
        	// 프리셋 요청 처리 (DB 서버 모드)
        	String farmUid = payload; // 예: "A1001:1"
        	System.out.println("프리셋 요청 수신: " + farmUid);
        	
        	PresetDTO preset = farmService.selectPresetByFarmUid(farmUid);
        	publishPresetResponse(farmUid, preset);
        	
        	// 임시: 프리셋이 없다고 가정
        	publishPresetResponse(farmUid, null);
        	
        	System.out.println("⚠️  TODO: DB에서 프리셋 조회 후 publishPresetResponse() 호출 필요");
        }
    }
    
    // deviceSerial 추출 (smartfarm/A1001/sensor/nl → A1001)
    private String extractDeviceSerial(String topic) {
        String[] parts = topic.split("/");
        if (parts.length >= 2) {
            return parts[1];  // smartfarm/A1001/sensor/nl에서 A1001
        }
        return null;
    }
    
    // 알림을 특정 유저에게 중계 발행
    public void publishNotificationToUser(String userId, String deviceSerial, String notification) {
        try {
            String userTopic = userId + "/smartfarm/" + deviceSerial + "/sensor/nl";
            MqttMessage msg = new MqttMessage(notification.getBytes());
            msg.setQos(1);
            this.client.publish(userTopic, msg);
            System.out.println("알림 발행: " + userTopic + " → " + notification);
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // System.out.println("Delivery complete.");
    }


    
}

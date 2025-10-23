package mqtt;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import service.SensorDataService;
import service.SensorDataServiceImpl;

public class MqttManager implements MqttCallback { // MqttCallback을 직접 구현
	private String id;
    private MqttClient client;
    private boolean DBServerMode;
    private final String broker = "tcp://localhost:1883";
    private final String pubTopic = "/smartfarm/sensor/"; // 유저, 기계 식별 앞에 붙이기
    private final String subTopic = "/smartfarm/#"; // {유저}/smartfarm 하위의 모든 토픽을 구독
    private SensorDataService service = new SensorDataServiceImpl();

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
                this.client.subscribe("+/smartfarm/+/sensor/data", 1); //라즈베리파이로부터 센서 정보 수신
                this.client.subscribe("+/smartfarm/+/query/#", 1); //유저로부터 쿼리 요청 수신
                System.out.println("Subscribed to topic: +/smartfarm/+/sensor/data");
            } else {
                this.client.subscribe(id + subTopic);
                System.out.println("Subscribed to topic: " + id + subTopic);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    
    // 발행을 처리하는 메소드
    public void publish(String userId, String device, String content) {
        try {
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(0); // QoS Level 0
            this.client.publish(userId+device+pubTopic, message); //device 보낼때 앞에 '/'를 붙일 것
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
//        System.out.println("\n=============== MESSAGE ARRIVED ===============");
//        System.out.println(" Topic: " + topic);
//        System.out.println(" Message: " + new String(message.getPayload()));
//        System.out.println("=============================================");
        if(topic.contains("sensor")) {
        	if(topic.endsWith("dht11")) {
        		service.saveData(topic, new String(message.getPayload()));
        	}
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // System.out.println("Delivery complete.");
    }


    
}

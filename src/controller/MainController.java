package controller;

import javax.swing.JOptionPane;

import dto.LoginUserDTO;
import dto.MemberDTO;
import dto.PresetDTO;
import dto.UserSessionDTO;
import mqtt.MqttManager;
import service.DeviceService;
import service.DeviceServiceImpl;
import service.FarmService;
import service.FarmServiceImpl;
import service.MemberService;
import service.MemberServiceImpl;
import service.PlantService;
import service.PlantServiceImpl;
import util.ConsoleUtils;
import view.MainView;

public class MainController {
	private UserSessionDTO currentUser = null; // 현재 로그인한 사용자 정보
    private final MainView view = new MainView(); // 화면을 담당할 View 객체
    private final MemberService service = new MemberServiceImpl();
    private MqttManager mqttManager;
    public void run() {
        while (true) {
            if (currentUser == null) {
                // 로그인되지 않았을 때의 로직 처리
                handleInitialMenu();
            } else {
                // 로그인된 후의 로직 처리
                handleMainMenu();
            }
        }
    }
    private void handleInitialMenu() {
    	ConsoleUtils.clearConsole();
        String choice = view.showInitialMenu(); // View는 메뉴를 보여주고 입력만 받아서 전달
        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                register();
                break;
            case "9":
                exitProgram();
                break;
            default:
                view.showMessage("(!) 잘못된 입력입니다.");
        }
    }

    private void login() {
    	ConsoleUtils.clearConsole();
    	
    	LoginUserDTO loginUser = view.handleLogin();
    	MemberDTO loginSuccessUser = service.login(loginUser.getUserId(),
    									loginUser.getPassword());
    	//로그인 성공하면 세션에 로그인사용자정보를 담고 Mqtt Subscriber를 실행함
		if(loginSuccessUser!=null) {
			currentUser = new UserSessionDTO(loginSuccessUser);
			 System.out.println("\nMQTT 서비스에 연결을 시작합니다...");
	         mqttManager = new MqttManager(currentUser.getLoginUser().getUserId());
			handleMainMenu();
		} else {
			JOptionPane.showMessageDialog(null, "로그인실패");
			view.handleLogin();
		}
	}
	private void register() {
		ConsoleUtils.clearConsole();
        // View에 현재 사용자 이름을 넘겨주어 메뉴를 보여주게 함
        MemberDTO user = view.showRegistrationForm();
        FarmService farmService = new FarmServiceImpl();
        DeviceService deviceService = new DeviceServiceImpl();
        int result = service.register(user);
        new Thread(() -> {
            if (result >= 1) {
                JOptionPane.showMessageDialog(null, "회원가입이 완료됐습니다.");
                deviceService.addDevice(user);
                farmService.createFarm(user);
            } else {
                JOptionPane.showMessageDialog(null, "회원가입에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }).start(); // 스레드 시작!
	}
	private void handleMainMenu() {
		ConsoleUtils.clearConsole();
        // View에 현재 사용자 이름을 넘겨주어 메뉴를 보여주게 함
        String choice = view.showMainMenu(currentUser.getLoginUser().getName());
        switch (choice) {
            case "1":
                view.showMessage("💡 식물 추가 메뉴입니다.");
                handleAddPlantMenu();
                break;
            case "2":
                // analyzeSensorData();
                view.showMessage("📊 식물 관리 메뉴입니다.");
                break;
            case "3":
                // configureSettings();
                view.showMessage("⚙️ 마이페이지 메뉴입니다.");
                handleMyPageMenu();
                break;
            case "4":
                // 
                view.showMessage("⚙️ 일림 관리 메뉴입니다.");
                break;
            case "8":
                logout();
                break;
            case "9":
                exitProgram();
                break;
            default:
                view.showMessage("(!) 잘못된 입력입니다.");
        }
    }
	
	private void handleAddPlantMenu() {
        String choice = view.showAddPlantMenu();
        PlantService plantService = new PlantServiceImpl();
        FarmService farmService = new FarmServiceImpl();
        String[] value = new String[4];
        switch (choice) {
            case "1":
                view.showMessage("추천 식물 1입니다.");
                value = view.showPresetMenu(plantService.selectPreset(Integer.parseInt(choice)));
                if (value[3].equals("1")) {
                	farmService.addFarm(value[0], value[1] + ":" + value[2]);
                }
                break;
            case "2":
                view.showMessage("추천 식물 2입니다.");
                break;
            case "3":
                view.showMessage("추천 식물 3입니다.");
                break;
            case "4":
                view.showInsertMessage("신규 식물의 프리셋을 설정해 주세요.");
                PresetDTO presetDTO = view.showAddNewPlantMenu();
                plantService.addCustomPreset(presetDTO);
                view.showInsertMessage("기기 시리얼 넘버와 슬롯 번호를 설정해 주세요.");
                value = view.showPresetMenu(presetDTO);
                if (value[3].equals("1")) {
                	farmService.addFarm(value[0], value[1] + ":" + value[2]);
                }
                break;
            case "8":
            	handleMainMenu();
                break;
            default:
                view.showMessage("(!) 잘못된 입력입니다.");
        }
    }
	
	private void handleMyPageMenu() {
		String choice = view.showMyPageMenu();
		switch (choice) {
        case "1":
            view.showMessage("기기 추가입니다.");
            String dsn = view.showAddDevice();
            DeviceService deviceService = new DeviceServiceImpl();
            FarmService farmService = new FarmServiceImpl();
            deviceService.addNewDevice(currentUser.getLoginUser(), dsn);
            farmService.createFarm(currentUser.getLoginUser(), dsn);
            break;
        case "8":
        	handleMainMenu();
            break;
        case "9":
            break;
        default:
            view.showMessage("(!) 잘못된 입력입니다.");
		}
	}
	
	private void logout() {
		if(mqttManager != null) mqttManager.close();
		mqttManager = null;
		currentUser = null;
	}
	private void exitProgram() {
		logout();
		MainView.exitProgram();
	}
}
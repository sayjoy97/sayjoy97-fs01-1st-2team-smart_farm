package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import dto.DeviceDTO;
import dto.FarmDTO;
import dto.LoginUserDTO;
import dto.MemberDTO;
import dto.PresetDTO;
import dto.SensorDataDTO;
import dto.UserSessionDTO;
import mqtt.MqttManager;
import service.DeviceService;
import service.DeviceServiceImpl;
import service.FarmService;
import service.FarmServiceImpl;
import service.MemberService;
import service.MemberServiceImpl;
import service.NotificationService;
import service.NotificationServiceImpl;
import service.PlantService;
import service.PlantServiceImpl;
import service.SensorDataService;
import service.SensorDataServiceImpl;
import util.ConsoleUtils;
import view.MainView;

public class MainController {
	private UserSessionDTO currentUser = null; // 현재 로그인한 사용자 정보
    private final MainView view = new MainView(); // 화면을 담당할 View 객체
    private final MemberService service = new MemberServiceImpl();
    private final DeviceService deviceService = new DeviceServiceImpl();
    private final FarmService farmService = new FarmServiceImpl();
    MemberDTO loginSuccessUser = null;
    private MqttManager mqttManager;
    private final Scanner scanner = new Scanner(System.in);
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
    	loginSuccessUser = service.login(loginUser.getUserId(),
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
                //analyzeSensorData();
                view.showMessage("📊 식물 관리 메뉴입니다.");
                handleManagePlantMenu();
                break;
            case "3":
                // configureSettings();
                view.showMessage("⚙️ 마이페이지 메뉴입니다.");
                handleMyPageMenu();
                break;
            case "4":
                // 
                view.showMessage("⚙️ 알림 관리 메뉴입니다.");
                NotificationService notificationService = new NotificationServiceImpl();
                ArrayList<String> notifications = handleNotificationManagementMenu();
                String deleteIndexStr = view.showNotificationManagementMenu(notifications.size() == 0);
                ArrayList<Integer> deleteNLUs = new ArrayList<>();
                if (deleteIndexStr.equals("0")) {
                	
                } else if (deleteIndexStr.equalsIgnoreCase("ALL")) {
                	notificationService.deleteAllNotification(currentUser.getLoginUser());
                } else {
                	String[] deleteIndexs = deleteIndexStr.split(",");
                    for (int i = 0; i < deleteIndexs.length; i++) {
                    	String deleteNLU = notifications.get(Integer.parseInt(deleteIndexs[i].trim()) - 1).split("/")[3];
                    	deleteNLUs.add(Integer.parseInt(deleteNLU));
                    }
                    notificationService.deleteNotification(deleteNLUs);
                }
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
                view.showMessage("상추의 프리셋입니다.");
                value = view.showPresetMenu(plantService.selectPreset(Integer.parseInt(choice)));
                if (value[3].equals("1")) {
                	farmService.addFarm(value[0], value[1] + ":" + value[2]);
                }
                break;
            case "2":
                view.showMessage("딸기의 프리셋입니다.");
                value = view.showPresetMenu(plantService.selectPreset(Integer.parseInt(choice)));
                if (value[3].equals("1")) {
                	farmService.addFarm(value[0], value[1] + ":" + value[2]);
                }
                break;
            case "3":
                view.showMessage("바질의 프리셋입니다.");
                value = view.showPresetMenu(plantService.selectPreset(Integer.parseInt(choice)));
                if (value[3].equals("1")) {
                	farmService.addFarm(value[0], value[1] + ":" + value[2]);
                }
                break;
            case "4":
                view.showMessage("와사비의 프리셋입니다.");
                value = view.showPresetMenu(plantService.selectPreset(Integer.parseInt(choice)));
                if (value[3].equals("1")) {
                	farmService.addFarm(value[0], value[1] + ":" + value[2]);
                }
                break;
            case "5":
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
            case "9":
            	exitProgram();
                break;
            default:
                view.showMessage("(!) 잘못된 입력입니다.");
        }
    }
	
	private void handleManagePlantMenu() {
		ArrayList<FarmDTO> farms = farmService.selectDevicesFarm(loginSuccessUser);
	    String choice = view.showMyFarmsMenu(farms);

	    try {
	    	int choiceNum = Integer.parseInt(choice);
	    	
	    	if (choiceNum == 8) {
	    		handleMainMenu();
	    	} else if (choiceNum == 9) {
	    		exitProgram();
	    	} else if (choiceNum >= 1 && choiceNum <= farms.size()) {
	    		// 선택한 Farm으로 상세 페이지 이동
	    		FarmDTO selectedFarm = farms.get(choiceNum - 1);
	    		handleFarmDetailMenu(selectedFarm);
	    	} else {
	    		view.showMessage("(!) 잘못된 입력입니다.");
	    		handleManagePlantMenu();
	    	}
	    } catch (NumberFormatException e) {
	    	view.showMessage("(!) 숫자를 입력해주세요.");
	    	handleManagePlantMenu();
	    }
	}

	
	private void handleMyPageMenu() {
		String choice = view.showMyPageMenu();
		String dsn = "";
		switch (choice) {
        case "1":
            // 여기에 정보 조회하는 메서드 호출하면 될 듯
            break;
        case "2":
            break;
        case "3":
        	view.showMessage("기기 추가입니다.");
            dsn = view.showAddDevice();
            deviceService.addNewDevice(currentUser.getLoginUser(), dsn);
            farmService.createFarm(currentUser.getLoginUser(), dsn);
            break;
        case "4":
        	view.showMessage("기기 삭제입니다.");
        	ArrayList<DeviceDTO> deviceList = deviceService.selectUserDevices(currentUser.getLoginUser());
        	for (int i = 0; i < deviceList.size(); i++) {
        		dsn = deviceList.get(i).getDeviceSerialNumber();
        		System.out.println("[" + (i + 1) + "] " + dsn);
        	}
        	int deleteNum = view.showDeleteDevice();
        	String DeleteDSN = deviceList.get(deleteNum - 1).getDeviceSerialNumber();
        	farmService.deleteFarm(DeleteDSN);
        	deviceService.deleteDevice(DeleteDSN);
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

	private void handleFarmDetailMenu(FarmDTO farm) {
		ConsoleUtils.clearConsole();
		
		// Preset 정보 조회
		PlantService plantService = new PlantServiceImpl();
		PresetDTO preset = plantService.selectPreset(farm.getPresetUid());
		
		// 최근 센서 데이터 조회
		SensorDataService sensorDataService = new SensorDataServiceImpl();
		List<SensorDataDTO> latestDataList = sensorDataService.getLogsByFarm(farm.getFarmUid(), null, 1);
		SensorDataDTO latestData = null;
		if(latestDataList != null && !latestDataList.isEmpty()) {
			latestData = latestDataList.get(0);
		}
		
		// Farm 상세 정보 표시
		view.showFarmDetail(farm, preset, latestData);
		
		// 메뉴 선택
		String choice = view.showFarmDetailMenu();
		
		switch (choice) {
		case "1":
			ConsoleUtils.clearConsole();
			view.showSensorDataList(farm.getFarmUid());
			System.out.print("\n계속하려면 Enter를 누르세요...");
			scanner.nextLine();
			handleFarmDetailMenu(farm);
			break;
		case "8":
			handleManagePlantMenu();
			break;
		case "9":
			exitProgram();
			break;
		default:
			view.showMessage("(!) 잘못된 입력입니다.");
			handleFarmDetailMenu(farm);
		}
	}
	
	private ArrayList<String> handleNotificationManagementMenu() {
		NotificationService notificationService = new NotificationServiceImpl();
		ArrayList<String> notifications = notificationService.showNotification(currentUser.getLoginUser());
		int repeat = 35;
		for (int i = 0; i < notifications.size(); i++) {
			String[] notification = notifications.get(i).split("/");
			System.out.println("\n[" + (i + 1) + "]" + "-".repeat(repeat));
			System.out.println(notification[0]);
			System.out.println(notification[1]);
			System.out.println(notification[2]);
			if (i >= 9) {
				System.out.println("-".repeat(repeat + 4));
			} else {
				System.out.println("-".repeat(repeat + 3));
			}
		}
		return notifications;
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

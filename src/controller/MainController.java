package controller;

import java.time.LocalTime;
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
			 System.out.println("MQTT 서비스에 연결을 시작합니다...");
	         mqttManager = new MqttManager(currentUser.getLoginUser().getUserId());
			handleMainMenu();
		} else {
			JOptionPane.showMessageDialog(null, "로그인실패");
			System.out.println("--------------------------------------------------");
			view.showMessage("로그인에 실패했습니다.");
        	while (true) {
        		String choice = view.showFailLoginMenu();
            	switch (choice) {
            		case "1":
            			handleInitialMenu();
            			break;
            		case "2":
            			view.showMessage("아이디/비밀번호 찾기");
            			String email = view.showInputEmail();
            			// 입력 받은 이메일을 바탕으로 users테이블에서 본인 확인 질문과 본인 확인 답변 가져오기
            			MemberDTO findUser = service.findQA(email);
            			if (findUser == null) {
            				view.showMessage("잘못된 이메일을 입력하셨습니다.");
            			} else {
            				String securityQuetion = findUser.getSecurityQuestion();
                			view.showMessage("아이디/비밀번호 찾기");
                			String securityAnswer = view.showCompareQA(securityQuetion);
                			if (findUser.getSecurityAnswer().equals(securityAnswer)) {
                				view.showMessage("확인 성공");
                				System.out.println("\n아이디: " + findUser.getUserId());
                				System.out.println("비밀번호: " + findUser.getPassword());
                				System.out.println("\n엔터를 누르면 로그인 화면으로 이동합니다.");
                				System.out.println("--------------------------------------------------");
                				scanner.nextLine();
                				handleInitialMenu();
                				break;
                			} else {
                				view.showMessage("틀린 답변을 입력하셨습니다.");
                			}
            			}
            			break;
            		default:
            			view.showMessage("잘못된 값을 입력");
            	}
        	}
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
                handleAddPlantMenu();
                break;
            case "2":
                //analyzeSensorData();
                handleManagePlantMenu();
                break;
            case "3":
                // configureSettings();
                handleMyPageMenu();
                break;
            case "4":
                // 
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
		view.showMessage("💡 식물 추가 메뉴입니다.");
        String choice = view.showAddPlantMenu();
        PlantService plantService = new PlantServiceImpl();
        FarmService farmService = new FarmServiceImpl();
        String[] value = new String[4];
        PresetDTO preset = null;
        switch (choice) {
            case "1":
                view.showMessage("상추의 프리셋입니다.");
                preset = plantService.selectPreset(Integer.parseInt(choice));
                value = view.showPresetMenu(preset);
                if (value[3].equals("1")) {
                	farmService.addFarm(value[0], value[1] + ":" + value[2]);
                	mqttManager.publishPresetUpdate(value[0],preset);
                }
                break;
            case "2":
                view.showMessage("딸기의 프리셋입니다.");
                preset = plantService.selectPreset(Integer.parseInt(choice));
                value = view.showPresetMenu(plantService.selectPreset(Integer.parseInt(choice)));
                if (value[3].equals("1")) {
                	farmService.addFarm(value[0], value[1] + ":" + value[2]);
                	mqttManager.publishPresetUpdate(value[1] + ":" + value[2], preset);
                }
                break;
            case "3":
                view.showMessage("바질의 프리셋입니다.");
                preset = plantService.selectPreset(Integer.parseInt(choice));
                value = view.showPresetMenu(plantService.selectPreset(Integer.parseInt(choice)));
                if (value[3].equals("1")) {
                	farmService.addFarm(value[0], value[1] + ":" + value[2]);
                	mqttManager.publishPresetUpdate(value[1] + ":" + value[2], preset);
                }
                break;
            case "4":
                view.showMessage("와사비의 프리셋입니다.");
                preset = plantService.selectPreset(Integer.parseInt(choice));
                value = view.showPresetMenu(plantService.selectPreset(Integer.parseInt(choice)));
                if (value[3].equals("1")) {
                	farmService.addFarm(value[0], value[1] + ":" + value[2]);
                	mqttManager.publishPresetUpdate(value[1] + ":" + value[2], preset);
                }
                break;
            case "5":
                view.showMessage("신규 식물의 프리셋을 설정해 주세요.");
                PresetDTO presetDTO = view.showAddNewPlantMenu();
                plantService.addCustomPreset(presetDTO);
                view.showMessage("기기 시리얼 넘버와 슬롯 번호를 설정해 주세요.");
                value = view.showPresetMenu(presetDTO);
                if (value[3].equals("1")) {
                	farmService.addFarm(value[0], value[1] + ":" + value[2]);
                	mqttManager.publishPresetUpdate(value[1] + ":" + value[2], preset);
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
		view.showMessage("📊 식물 관리 메뉴입니다.");
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
		while(true) {
			view.showMessage("⚙️ 마이페이지 메뉴입니다.");
			String choice = view.showMyPageMenu();
			String dsn = "";
			switch (choice) {
				case "1":
					view.showMessage("정보 조회 메뉴입니다.");
					//현재 로그인 중인 사용자 불러오기
					MemberDTO user = currentUser.getLoginUser();
			
					// 사용자의 등록된 기기 리스트 가져오기 
					ArrayList<DeviceDTO> devices = deviceService.selectUserDevices(user);
					view.showInfo(user, devices);
					System.out.print("\n뒤로가려면 Enter를 누르세요...");
					scanner.nextLine(); // 입력 대기 (없으면 바로 다음 메뉴로 넘어감)
					break;
				case "2":
					view.showMessage("정보 수정 메뉴입니다.");

					String updateChoice = view.showUpdateMenu(); // 1. 비밀번호 / 2. 이름 / 3. 이메일
					MemberDTO userUpdate = currentUser.getLoginUser();

					switch (updateChoice) {
		        		case "1": // 비밀번호 변경
		        			String currentPw = view.getNewValue("현재 비밀번호");
		        			if (!userUpdate.getPassword().equals(currentPw)) {
		        				view.showMessage("❌ 현재 비밀번호가 일치하지 않습니다.");
		        				System.out.print("\n뒤로가려면 Enter를 누르세요...");
		        				scanner.nextLine();
		        				break;
		        			}

		        			String newPw = view.getNewValue("새 비밀번호");
		        			service.updateUserInfo(userUpdate.getUserUid(), "password", newPw);
		        			userUpdate.setPassword(newPw);
		        			view.showMessage("비밀번호가 성공적으로 변경되었습니다!");
		        			System.out.print("\n뒤로가려면 Enter를 누르세요...");
		        			scanner.nextLine();
		        			break;

		        		case "2": // 이름 변경
		        			String oldName = userUpdate.getName(); // 현재 이름 저장
		        			String newName = view.getNewValue("새 이름");

		        			service.updateUserInfo(userUpdate.getUserUid(), "name", newName);
		        			userUpdate.setName(newName); // 세션 반영

		        			// 변경 내역 표시
		        			view.showMessage("이름이 '" + oldName + "' → '" + newName + "' 로 변경되었습니다!");
		        			System.out.print("\n뒤로가려면 Enter를 누르세요...");
		        			scanner.nextLine();
		        			break;

		        		case "3": //이메일 변경
		        			String oldEmail = userUpdate.getEmail(); // 기존 이메일
		        			String newEmail = view.getNewValue("새 이메일");

		        			service.updateUserInfo(userUpdate.getUserUid(), "email", newEmail);
		        			userUpdate.setEmail(newEmail); // 세션 반영

		        			// 변경 내역 표시
		        			view.showMessage("이메일이 '" + oldEmail + "' → '" + newEmail + "' 로 변경되었습니다!");
		        			System.out.print("\n뒤로가려면 Enter를 누르세요...");
		        			scanner.nextLine();
		        			break;

		        		case "8": // 뒤로가기
		        			return;

		        		default:
		        			view.showMessage("(!) 잘못된 입력입니다.");
					}
					break;
				case "3": 
					view.showMessage("기기 추가입니다.");
        	
					System.out.print("  [1] 기기 시리얼 넘버 입력 ");
					System.out.println("\n  [8] 뒤로가기");
					dsn = scanner.nextLine().trim();

					if (dsn.equals("1")) {
						view.showMessage("기기 추가입니다.");
						dsn = view.showAddDevice();
						deviceService.addNewDevice(currentUser.getLoginUser(), dsn);
						farmService.createFarm(currentUser.getLoginUser(), dsn);
						break;
					}
					if (dsn.equals("8")) {
						return;
					}
				case "4":
					view.showMessage("기기 삭제입니다.");
					//사용자 기기 목록을 조회
					ArrayList<DeviceDTO> deviceList = deviceService.selectUserDevices(currentUser.getLoginUser());
					// 메뉴 표시
					System.out.println("\n  [1] 삭제할 기기 선택");
					System.out.println("  [8] 뒤로가기");
					System.out.println("\n--------------------------------------------------");
					System.out.print("> 입력: ");
					String input = scanner.nextLine().trim();
					
					view.showMessage("기기 삭제입니다.");
					if (input.equals("1")) {
						System.out.println("\n  [0] 뒤로가기");
						for (int i = 0; i < deviceList.size(); i++) {
							dsn = deviceList.get(i).getDeviceSerialNumber();
							System.out.println("  [" + (i + 1) + "] " + dsn);
						}
						int deleteNum = view.showDeleteDevice();
						if (deleteNum == 0) {
							return;
						} else {
							String DeleteDSN = deviceList.get(deleteNum - 1).getDeviceSerialNumber();
							farmService.deleteFarm(DeleteDSN);
							deviceService.deleteDevice(DeleteDSN);
							break;
						}
					}
					if (input.equals("8")) {
						return;
					}
				case "8":
					return;
				case "9":
					exitProgram();
					break;
				default:
					view.showMessage("(!) 잘못된 입력입니다.");
			}
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
			view.showMessage("센서 데이터 기록 (최근 10개)");
			view.showSensorDataList(farm.getFarmUid());
			System.out.print("\n계속하려면 Enter를 누르세요...");
			scanner.nextLine();
			handleFarmDetailMenu(farm);
			break;
		case "2":
			view.showMessage("10시간 센서 데이터");
			List<SensorDataDTO> dailyDataList = sensorDataService.getLogsByFarm(farm.getFarmUid(), 10, null);
			String plantName = farmService.getPlantName(farm.getFarmUid());
			List<SensorDataDTO> hoursDataList = new ArrayList<SensorDataDTO>();
			SensorDataDTO dailyData = new SensorDataDTO();
			for (SensorDataDTO sensorData : dailyDataList) {
				float measuredTemp = 0;
				float measuredHumidity = 0;
				float measuredLight = 0;
				float measuredCo2 = 0;
				float measuredSoilMoisture = 0;
				for (int i = 0; i <= 360; i+=90) {
					measuredTemp += sensorData.getMeasuredTemp();
					measuredHumidity += sensorData.getMeasuredHumidity();
					measuredLight += sensorData.getMeasuredLight();
					measuredCo2 += sensorData.getMeasuredCo2();
					measuredSoilMoisture += sensorData.getMeasuredSoilMoisture();
				}
				hoursDataList.add(new SensorDataDTO(0, null, null, measuredTemp / 4, measuredHumidity / 4, measuredLight / 4, measuredCo2 / 4, measuredSoilMoisture / 4));
			}
			PresetDTO presetDTO = farmService.selectPresetByFarmUid(farm.getFarmUid());
			int[] values = new int[10];
			int i = 0;
			double scale = 0;
			System.out.println("\n  [" + plantName + " 온도 변화 그래프]\n");
			for (SensorDataDTO dto : hoursDataList) {
				values[i] = Math.round(dto.getMeasuredTemp());
			}
			double referenceTickValue = presetDTO.getOptimalTemp();
			scale = 1;
			
			makeGraph(values, referenceTickValue, scale);
			
			
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
	
	private void makeGraph(int[] values, double referenceTickValue, double scale) {
		int maxValue = 0;
		int minValue = 10000;
		double level = 0;
		// 더미 데이터 삽입
		values[0] = 20;
		values[1] = 19;
		values[2] = 20;
		values[3] = 21;
		values[4] = 18;
		values[5] = 19;
		values[6] = 20;
		values[7] = 18;
		values[8] = 17;
		values[9] = 19;
		for (int value : values) {
			if (value > maxValue) {
				maxValue = value;
			}
			if (value < minValue) {
				minValue = value;
			}
		}
		if (minValue >= (int)referenceTickValue) {
			minValue = (int)referenceTickValue;
		}
		int blank = Double.toString(referenceTickValue).length();
		System.out.println(" ".repeat(blank) + " |                                                  ");
		for (level = maxValue; level > minValue; level -= scale) {
			System.out.print(level + " |");
			for (int i = 9; i >= 0; i--) {
				if (values[i] >= level) {
					System.out.print(" ■■");
				} else {
					System.out.print("   ");
				}
				System.out.print("  ");
			}
			System.out.println("");
		}
		System.out.println((level - scale) + " | ■■   ■■   ■■   ■■   ■■   ■■   ■■   ■■   ■■   ■■  ");
		System.out.println(" ".repeat(blank) + " +----+----+----+----+----+----+----+----+----+----+");
		System.out.print(" ".repeat(blank) + " |");
		for (int i = 9; i >= 0; i--) {
			if (((LocalTime.now().getHour() + 24 - i) % 24) == 0) {
				System.out.print(" 24");
			} else if (((LocalTime.now().getHour() + 24 - i) % 24) < 10) {
				System.out.print(" 0" + ((LocalTime.now().getHour() + 24 - i) % 24));
			} else {
				System.out.print(" " + ((LocalTime.now().getHour() + 24 - i) % 24));
			}
			System.out.print(" |");
		}
		System.out.println(" (단위 : ℃)");
	}
	
//	private int[] insertValue(List<SensorDataDTO> hoursDataList, String str) {
//		int[] values = new int[10];
//		int i = 0;
//		switch (str) {
//			case "measuredTemp":
//				for (SensorDataDTO dto : hoursDataList) {
//					values[i] = Math.round(dto.getMeasuredTemp());
//				}
//				break;
//			case "measuredHumidity":
//				for (SensorDataDTO dto : hoursDataList) {
//					values[i] = Math.round(dto.getMeasuredTemp());
//				}
//				break;
//			case "measuredLight":
//				for (SensorDataDTO dto : hoursDataList) {
//					values[i] = Math.round(dto.getMeasuredTemp());
//				}
//				break;
//			case "measuredCo2":
//				for (SensorDataDTO dto : hoursDataList) {
//					values[i] = Math.round(dto.getMeasuredTemp());
//				}
//				break;
//			case "measuredSoilMoisture":
//				for (SensorDataDTO dto : hoursDataList) {
//					values[i] = Math.round(dto.getMeasuredTemp());
//				}
//				break;
//			default:
//				break;
//		}
//		return values;
//	}
	
	private ArrayList<String> handleNotificationManagementMenu() {
		view.showMessage("⚙️ 알림 관리 메뉴입니다.");
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

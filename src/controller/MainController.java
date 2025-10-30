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
            case "E":
                exitProgram();
                break;
            case "e":
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
            case "B":
                logout();
                break;
            case "b":
                logout();
                break;
            case "E":
                exitProgram();
                break;
            case "e":
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
        String[] values = new String[4];
        PresetDTO preset = null;
        switch (choice) {
            case "1":
                view.showMessage("상추의 프리셋입니다.");
                preset = plantService.selectPreset(Integer.parseInt(choice));
                values = view.showPresetMenu(preset);
                if (values[3].equals("1")) {
                	int num = farmService.addFarm(values[0], values[1] + ":" + values[2]);
                	if (num == 0) {
                		view.showMessage("잘못된 값을 입력하셨습니다.");
                		System.out.println("\n  엔터를 입력해주세요.");
                		scanner.nextLine();
                	} else {
                		mqttManager.publishPresetUpdate(values[1] + ":" + values[2], preset);
                	}
                }
                break;
            case "2":
                view.showMessage("딸기의 프리셋입니다.");
                preset = plantService.selectPreset(Integer.parseInt(choice));
                values = view.showPresetMenu(plantService.selectPreset(Integer.parseInt(choice)));
                if (values[3].equals("1")) {
                	int num = farmService.addFarm(values[0], values[1] + ":" + values[2]);
                	if (num == 0) {
                		view.showMessage("잘못된 값을 입력하셨습니다.");
                		System.out.println("\n  엔터를 입력해주세요.");
                		scanner.nextLine();
                	} else {
                		mqttManager.publishPresetUpdate(values[1] + ":" + values[2], preset);
                	}
                }
                break;
            case "3":
                view.showMessage("바질의 프리셋입니다.");
                preset = plantService.selectPreset(Integer.parseInt(choice));
                values = view.showPresetMenu(plantService.selectPreset(Integer.parseInt(choice)));
                if (values[3].equals("1")) {
                	int num = farmService.addFarm(values[0], values[1] + ":" + values[2]);
                	if (num == 0) {
                		view.showMessage("잘못된 값을 입력하셨습니다.");
                		System.out.println("\n  엔터를 입력해주세요.");
                		scanner.nextLine();
                	} else {
                		mqttManager.publishPresetUpdate(values[1] + ":" + values[2], preset);
                	}
                }
                break;
            case "4":
                view.showMessage("와사비의 프리셋입니다.");
                preset = plantService.selectPreset(Integer.parseInt(choice));
                values = view.showPresetMenu(plantService.selectPreset(Integer.parseInt(choice)));
                if (values[3].equals("1")) {
                	int num = farmService.addFarm(values[0], values[1] + ":" + values[2]);
                	if (num == 0) {
                		view.showMessage("잘못된 값을 입력하셨습니다.");
                		System.out.println("\n  엔터를 입력해주세요.");
                		scanner.nextLine();
                	} else {
                		mqttManager.publishPresetUpdate(values[1] + ":" + values[2], preset);
                	}
                }
                break;
            case "5":
                view.showMessage("신규 식물의 프리셋을 설정해 주세요.");
                PresetDTO presetDTO = view.showAddNewPlantMenu();
                if (presetDTO == null) {
                	view.showMessage("잘못된 값을 입력하셨습니다.");
        			System.out.println("\n  엔터를 입력해주세요.");
        			scanner.nextLine();
                } else {
                	plantService.addCustomPreset(presetDTO);
                    view.showMessage("기기 시리얼 넘버와 슬롯 번호를 설정해 주세요.");
                    values = view.showPresetMenu(presetDTO);
                    if (values[3].equals("1")) {
                    	farmService.addFarm(values[0], values[1] + ":" + values[2]);
                    	mqttManager.publishPresetUpdate(values[1] + ":" + values[2], preset);
                    }
                }
                break;
            case "B":
            	handleMainMenu();
                break;
            case "b":
            	handleMainMenu();
                break;
            case "E":
            	exitProgram();
                break;
            case "e":
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
	    if (choice.equalsIgnoreCase("B")) {
	    	return;
	    } else if (choice.equalsIgnoreCase("E")) {
	    	exitProgram();
	    } else {
	    	try {
	    		int choiceNum = Integer.parseInt(choice);
	    		if (choiceNum >= 1 && choiceNum <= farms.size()) {
		    		// 선택한 Farm으로 상세 페이지 이동
		    		FarmDTO selectedFarm = farms.get(choiceNum - 1);
		    		handleFarmDetailMenu(selectedFarm);
		    	} else {
		    		view.showMessage("(!) 잘못된 입력입니다.");
		    		System.out.print("\n  Enter를 누르세요...");
        			scanner.nextLine();
		    		handleManagePlantMenu();
		    	}
	    	} catch (NumberFormatException e) {
	    		view.showMessage("(!) 잘못된 입력입니다.");
	    		System.out.print("\n  Enter를 누르세요...");
    			scanner.nextLine();
                handleManagePlantMenu();
	    	}
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
					System.out.print("\n  뒤로가려면 Enter를 누르세요...");
					scanner.nextLine(); // 입력 대기 (없으면 바로 다음 메뉴로 넘어감)
					break;
				case "2":
					view.showMessage("정보 수정 메뉴입니다.");

					String updateChoice = view.showUpdateMenu(); // 1. 비밀번호 / 2. 이름 / 3. 이메일
					MemberDTO userUpdate = currentUser.getLoginUser();

					switch (updateChoice) {
		        		case "1": // 비밀번호 변경
		        			view.showMessage("비밀번호 수정");
		        			String currentPw = view.getNewValue("현재 비밀번호");
		        			if (!userUpdate.getPassword().equals(currentPw)) {
		        				view.showMessage("  ❌ 현재 비밀번호가 일치하지 않습니다.");
		        				System.out.print("\n  뒤로가려면 Enter를 누르세요...");
		        				scanner.nextLine();
		        				break;
		        			}

		        			String newPw = view.getNewValue("새 비밀번호");
		        			if (newPw == "") {
		        				view.showMessage("값이 입력되지 않았습니다.");
		        				System.out.print("\n  Enter를 누르세요...");
			        			scanner.nextLine();
		        			} else {
		        				service.updateUserInfo(userUpdate.getUserUid(), "password", newPw);
			        			userUpdate.setPassword(newPw);
			        			view.showMessage("비밀번호가 성공적으로 변경되었습니다!");
			        			System.out.print("\n  뒤로가려면 Enter를 누르세요...");
			        			scanner.nextLine();
		        			}
		        			break;

		        		case "2": // 이름 변경
		        			view.showMessage("이름 수정");
		        			String oldName = userUpdate.getName(); // 현재 이름 저장
		        			String newName = view.getNewValue("새 이름");
		        			if (newName == "") {
		        				view.showMessage("값이 입력되지 않았습니다.");
		        				System.out.print("\n  Enter를 누르세요...");
			        			scanner.nextLine();
		        			} else {
		        				service.updateUserInfo(userUpdate.getUserUid(), "name", newName);
			        			userUpdate.setName(newName); // 세션 반영

			        			// 변경 내역 표시
			        			view.showMessage("이름이 '" + oldName + "' → '" + newName + "' 로 변경되었습니다!");
			        			System.out.print("\n  뒤로가려면 Enter를 누르세요...");
			        			scanner.nextLine();
		        			}
		        			break;

		        		case "3": //이메일 변경
		        			view.showMessage("이메일 수정");
		        			String oldEmail = userUpdate.getEmail(); // 기존 이메일
		        			String newEmail = view.getNewValue("새 이메일");
		        			if (newEmail == "") {
		        				view.showMessage("값이 입력되지 않았습니다.");
		        				System.out.print("\n  Enter를 누르세요...");
			        			scanner.nextLine();
		        			} else {
		        				service.updateUserInfo(userUpdate.getUserUid(), "email", newEmail);
			        			userUpdate.setEmail(newEmail); // 세션 반영

			        			// 변경 내역 표시
			        			view.showMessage("이메일이 '" + oldEmail + "' → '" + newEmail + "' 로 변경되었습니다!");
			        			System.out.print("\n  뒤로가려면 Enter를 누르세요...");
			        			scanner.nextLine();
		        			}
		        			break;

		        		case "B": // 뒤로가기
		        			break;
		        			
		        		case "b": // 뒤로가기
		        			break;

		        		default:
		        			view.showMessage("(!) 잘못된 입력입니다.");
		        			System.out.print("\n  Enter를 누르세요...");
		        			scanner.nextLine();
					}
					break;
				case "3": 
					view.showMessage("기기 추가입니다.");
        	
					System.out.print("  [1] 기기 시리얼 넘버 입력 ");
					System.out.println("\n  [B] 뒤로가기");
					System.out.println("\n--------------------------------------------------");
					System.out.print("  > 입력: ");
					dsn = scanner.nextLine().trim();

					if (dsn.equals("1")) {
						view.showMessage("기기 추가입니다.");
						dsn = view.showAddDevice();
						int num = deviceService.addNewDevice(currentUser.getLoginUser(), dsn);
						if (num == 0) {
							view.showMessage("잘못된 값을 입력하셨습니다.");
							System.out.println("\n  엔터를 눌러주세요.");
							scanner.nextLine();
						} else {
							farmService.createFarm(currentUser.getLoginUser(), dsn);
						}
						break;
					}
					if (dsn.equalsIgnoreCase("B")) {
						break;
					}
				case "4":
					view.showMessage("기기 삭제입니다.");
					//사용자 기기 목록을 조회
					ArrayList<DeviceDTO> deviceList = deviceService.selectUserDevices(currentUser.getLoginUser());
					// 메뉴 표시
					System.out.println("\n  [1] 삭제할 기기 선택");
					System.out.println("  [B] 뒤로가기");
					System.out.println("\n--------------------------------------------------");
					System.out.print("> 입력: ");
					String input = scanner.nextLine().trim();
					
					if (input.equals("1")) {
						view.showMessage("기기 삭제입니다.");
						System.out.println("\n  [B] 뒤로가기");
						for (int i = 0; i < deviceList.size(); i++) {
							dsn = deviceList.get(i).getDeviceSerialNumber();
							System.out.println("  [" + (i + 1) + "] " + dsn);
						}
						String deleteNum = view.showDeleteDevice();
						if (deleteNum.equalsIgnoreCase("B")) {
							break;
						} else {
							try {
								String DeleteDSN = deviceList.get(Integer.parseInt(deleteNum) - 1).getDeviceSerialNumber();
								farmService.deleteFarm(DeleteDSN);
								deviceService.deleteDevice(DeleteDSN);
								break;
							} catch (Exception e) {
								view.showMessage("기기 번호를 잘못 입력하셨습니다.");
								System.out.println("\n  엔터를 눌러주세요.");
								scanner.nextLine();
								break;
							}
						}
					} else if (input.equalsIgnoreCase("B")) {
						break;
					} else {
						view.showMessage("잘못된 값을 입력하셨습니다.");
						System.out.println("\n  엔터를 눌러주세요.");
						scanner.nextLine();
						break;
					}
				case "B":
					return;
				case "b":
					return;
				case "E":
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
			System.out.print("\n  계속하려면 Enter를 누르세요...");
			scanner.nextLine();
			handleFarmDetailMenu(farm);
			break;
		case "2":
			view.showMessage("10시간 센서 데이터");
			List<SensorDataDTO> hoursDataList = sensorDataService.getLogsForGraph(farm.getFarmUid());
			if(hoursDataList != null && !hoursDataList.isEmpty()) {
				String plantName = farmService.getPlantName(farm.getFarmUid());
				
				int[] avgTemp = new int[hoursDataList.size()];
				int[] avgHumidity = new int[hoursDataList.size()];
				int[] avgLight = new int[hoursDataList.size()];
				int[] avgCo2 = new int[hoursDataList.size()];
				int[] avgSoilMoisture = new int[hoursDataList.size()];
				int[] hours = new int[hoursDataList.size()];
				
				PresetDTO presetDTO = farmService.selectPresetByFarmUid(farm.getFarmUid());
				for (int i = 0; i < hoursDataList.size(); i++) {
					avgTemp[i] = (int)Math.round(hoursDataList.get(i).getMeasuredTemp());
					avgHumidity[i] = (int)Math.round(hoursDataList.get(i).getMeasuredHumidity());
					avgLight[i] = (int)Math.round(hoursDataList.get(i).getMeasuredLight());
					avgCo2[i] = (int)Math.round(hoursDataList.get(i).getMeasuredCo2());
					avgSoilMoisture[i] = (int)Math.round(hoursDataList.get(i).getMeasuredSoilMoisture());
					hours[i] = hoursDataList.get(i).getRecordedAt().toLocalDateTime().getHour();
				}
				
				System.out.println("\n  [" + plantName + " 온도 변화 그래프]\n");
				
//				더미데이터
//				avgTemp[0] = 23;
//				avgTemp[1] = 22;
//				avgTemp[2] = 21;
//				avgTemp[3] = 20;
//				avgTemp[4] = 19;
//				avgTemp[5] = 20;
//				avgTemp[6] = 21;
//				avgTemp[7] = 23;
//				avgTemp[8] = 24;
//				avgTemp[9] = 24;
				
				sensorDataService.makeGraph(avgTemp, hours, presetDTO.getOptimalTemp(), 0.5, "℃");
				
				System.out.println("\n  [" + plantName + " 습도 변화 그래프]\n");
				sensorDataService.makeGraph(avgHumidity, hours, presetDTO.getOptimalHumidity(), 5, "%");
				
				System.out.println("\n  [" + plantName + " 광량 변화 그래프]\n");
				for (int i = 0; i < avgLight.length; i++) {
					if (avgLight[i] >= 1200) {
						avgLight[i] = 1200;
					}
				}
				sensorDataService.makeGraph(avgLight, hours, presetDTO.getLightIntensity(), 10, "아날로그");
				
				if (farm.getFarmUid().charAt(0) == 'A') {
					System.out.println("\n  [" + plantName + " 이산화탄소 농도 변화 그래프]\n");
					sensorDataService.makeGraph(avgCo2, hours, presetDTO.getCo2Level(), 50, "ppm");
				}
				
				System.out.println("\n  [" + plantName + " 토양 수분 변화 그래프]\n");
				
				for (int i = 0; i < avgSoilMoisture.length; i++) {
					if (avgSoilMoisture[i] >= 100555) {
						avgSoilMoisture[i] = 100;
					} else if (avgSoilMoisture[i] >= 95000) {
						avgSoilMoisture[i] = 90;
					} else if (avgSoilMoisture[i] >= 84444) {
						avgSoilMoisture[i] = 80;
					} else if (avgSoilMoisture[i] >= 73888) {
						avgSoilMoisture[i] = 70;
					} else if (avgSoilMoisture[i] >= 63333) {
						avgSoilMoisture[i] = 60;
					} else if (avgSoilMoisture[i] >= 52777) {
						avgSoilMoisture[i] = 50;
					} else if (avgSoilMoisture[i] >= 42222) {
						avgSoilMoisture[i] = 40;
					} else if (avgSoilMoisture[i] >= 31666) {
						avgSoilMoisture[i] = 30;
					} else if (avgSoilMoisture[i] >= 21111) {
						avgSoilMoisture[i] = 20;
					} else if (avgSoilMoisture[i] >= 10555) {
						avgSoilMoisture[i] = 10;
					} else {
						avgSoilMoisture[i] = 0;
					}
				}
				if (presetDTO.getSoilMoisture() >= 100555) {
					presetDTO.setSoilMoisture(0);
				} else if (presetDTO.getSoilMoisture() >= 95000) {
					presetDTO.setSoilMoisture(10);
				} else if (presetDTO.getSoilMoisture() >= 84444) {
					presetDTO.setSoilMoisture(20);
				} else if (presetDTO.getSoilMoisture() >= 73888) {
					presetDTO.setSoilMoisture(30);
				} else if (presetDTO.getSoilMoisture() >= 63333) {
					presetDTO.setSoilMoisture(40);
				} else if (presetDTO.getSoilMoisture() >= 52777) {
					presetDTO.setSoilMoisture(50);
				} else if (presetDTO.getSoilMoisture() >= 42222) {
					presetDTO.setSoilMoisture(60);
				} else if (presetDTO.getSoilMoisture() >= 31666) {
					presetDTO.setSoilMoisture(70);
				} else if (presetDTO.getSoilMoisture() >= 21111) {
					presetDTO.setSoilMoisture(80);
				} else if (presetDTO.getSoilMoisture() >= 10555) {
					presetDTO.setSoilMoisture(90);
				} else {
					presetDTO.setSoilMoisture(100);
				}
				sensorDataService.makeGraph(avgSoilMoisture, hours, presetDTO.getSoilMoisture(), 10, "%");
				
				System.out.print("\n  계속하려면 Enter를 누르세요...");
				scanner.nextLine();
				handleFarmDetailMenu(farm);
				break;
			} else {
				System.out.println("  (!) 센서 데이터가 없습니다.\n");
				System.out.print("\n  계속하려면 Enter를 누르세요...");
				scanner.nextLine();
				//break;
			}
		case "3":
			view.showMessage("프리셋을 수정해주세요");
			//String[] values = new String[4];
			PresetDTO presetDTO = view.showUpdatePlantMenu(preset);
			System.out.println(farm.getFarmUid());
			System.out.println(presetDTO);
            if (presetDTO == null) {
            	view.showMessage("잘못된 값을 입력하셨습니다.");
    			System.out.println("\n  엔터를 입력해주세요.");
    			scanner.nextLine();
            } else {
            	plantService.updatePreset(farm.getFarmUid(), presetDTO);
            	System.out.println("");
            	plantService.selectPreset(presetDTO.getPresetUid());
//                view.showMessage("기기 시리얼 넘버와 슬롯 번호를 설정해 주세요.");
//                values = view.showPresetMenu(presetDTO);
//                if (values[3].equals("1")) {
//                	farmService.addFarm(values[0], values[1] + ":" + values[2]);
//                	mqttManager.publishPresetUpdate(values[1] + ":" + values[2], preset);
//                }
            }
//			mqttManager.publishPresetResponse(farm.getFarmUid(), presetDTO);
			break;
		case "B":
			handleManagePlantMenu();
			break;
		case "b":
			handleManagePlantMenu();
			break;
		case "E":
			exitProgram();
			break;
		case "e":
			exitProgram();
			break;
		default:
			view.showMessage("(!) 잘못된 입력입니다.");
			handleFarmDetailMenu(farm);
		}
	}
	
	private ArrayList<String> handleNotificationManagementMenu() {
		view.showMessage("⚙️ 알림 관리 메뉴입니다.");
		NotificationService notificationService = new NotificationServiceImpl();
		ArrayList<String> notifications = notificationService.showNotification(currentUser.getLoginUser());
		int repeat = 47;
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

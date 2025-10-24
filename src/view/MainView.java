package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dto.DeviceDTO;
import dto.FarmDTO;
import dto.LoginUserDTO;
import dto.MemberDTO;
import dto.PresetDTO;
import dto.SensorDataDTO;
import service.SensorDataService;
import service.SensorDataServiceImpl;
import util.ConsoleUtils;

public class MainView {
	private static final Scanner scanner = new Scanner(System.in);
	
	public String showInitialMenu() {
		System.out.println("\n==================================================");
		System.out.println("      🌿 라즈베리파이 스마트팜 제어 시스템 🌿");
		System.out.println("==================================================");
		System.out.println("\n환영합니다! 원하시는 메뉴를 선택해주세요.\n");
		System.out.println("  [1] 로그인");
		System.out.println("  [2] 회원가입");
		System.out.println("  [9] 프로그램 종료");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		return scanner.nextLine();
	}

	public String showMainMenu(String name) {
		System.out.println("\n==================================================");
		System.out.println("      🌿 라즈베리파이 스마트팜 제어 시스템 🌿");
		System.out.println("==================================================");
		System.out.printf("%s님, 환영합니다!         현재 상태: 양호 ✅\n\n", name);
		System.out.println("  [1] 식물 추가 💡");
		System.out.println("  [2] 식물 관리 📊");
		System.out.println("  [3] 마이 페이지 ⚙️");
		System.out.println("  [4] 알림 관리 ⚙️");
		System.out.println("  [8] 로그아웃");
		System.out.println("  [9] 프로그램 종료");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		return scanner.nextLine();
	}
	// 회원가입 정보를 입력받아 DTO 객체로 반환하는 메서드
    public MemberDTO showRegistrationForm() {
        System.out.println("\n==================================================");
        System.out.println("                   🌿 회원가입 🌿");
        System.out.println("--------------------------------------------------");
        MemberDTO newMember = new MemberDTO();
        
        System.out.print("\n  [필수] 아이디: ");
        newMember.setUserId(scanner.nextLine());
        
        while (true) {
            System.out.print("  [필수] 비밀번호: ");
            String password = scanner.nextLine();
            System.out.print("  [필수] 비밀번호 확인: ");
            String passwordConfirm = scanner.nextLine();
            if (password.equals(passwordConfirm)) {
                newMember.setPassword(passwordConfirm);
                break;
            } else {
                System.out.println("\n  (!) 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
            }
        }
        
        System.out.print("  [필수] 이름: ");
        newMember.setName(scanner.nextLine());
        System.out.print("  [필수] 이메일: ");
        newMember.setEmail(scanner.nextLine());
        System.out.print("  [필수] 본인 확인 질문: ");
        newMember.setSecurityQuestion(scanner.nextLine());
        System.out.print("  [필수] 본인 확인 답변: ");
        newMember.setSecurityAnswer(scanner.nextLine());
        System.out.print("  [필수] 기기 시리얼 넘버: ");
        newMember.setDeviceSerialNumber(scanner.nextLine());
        
        System.out.println("\n--------------------------------------------------");
        return newMember;
    }
	
	public LoginUserDTO handleLogin() {
		System.out.println("\n==================================================");
        System.out.println("                   🌿 로그인 🌿");
        System.out.println("--------------------------------------------------");
		System.out.print("아이디: ");
		String username = scanner.nextLine();
		System.out.print("비밀번호: ");
		String password = scanner.nextLine();
		return new LoginUserDTO(username, password);
	}
	public static void exitProgram() {
	        System.out.println("프로그램을 종료합니다. 안녕히 가세요!");
	        System.exit(0);
	    }
	public void showMessage(String string) {
		ConsoleUtils.clearConsole();
		System.out.println("\n==================================================");
		System.out.println("      " + string);
		System.out.println("==================================================");
	}
	
	public void showInsertMessage(String string) {
		ConsoleUtils.clearConsole();
		System.out.println("\n==================================================");
		System.out.println("      " + string);
		System.out.println("--------------------------------------------------");
	}
	
	public String showAddPlantMenu() {
		System.out.println("\n원하시는 메뉴를 선택해주세요.\n");
		System.out.println("  [1] 추천 식물: 상추");
		System.out.println("  [2] 추천 식물: 딸기");
		System.out.println("  [3] 추천 식물: 바질");
		System.out.println("  [4] 추천 식물: 와사비");
		System.out.println("  [5] 신규 식물 추가");
		System.out.println("  [8] 뒤로가기");
		System.out.println("  [9] 프로그램 종료");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		return scanner.nextLine();
	}
	
	public String showMyDevicesMenu(ArrayList<DeviceDTO> devices) {
		System.out.println("\n농장 정보.\n");
		int i = 1;
		for(DeviceDTO device:devices) {
			System.out.println("  ["+i+"] "+device.getDeviceSerialNumber());
		}
		System.out.println("  [8] 뒤로가기");
		System.out.println("  [9] 프로그램 종료");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		return scanner.nextLine();
	}
	
	public String showMyFarmsMenu(ArrayList<FarmDTO> farms) {
		System.out.println("\n내 농장 목록을 선택해주세요.\n");
		int i = 1;
		for(FarmDTO farm:farms) {
			System.out.println("  ["+i+"] Farm ID: " + farm.getFarmUid());
			i++;
		}
		System.out.println("  [8] 뒤로가기");
		System.out.println("  [9] 프로그램 종료");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		return scanner.nextLine();
	}
	public PresetDTO showAddNewPlantMenu() {
		PresetDTO presetDTO = new PresetDTO();
		System.out.print("식물 이름: ");
		presetDTO.setPlantName(scanner.nextLine());
		System.out.print("희망 온도 설정: ");
		presetDTO.setOptimalTemp(scanner.nextFloat());
		System.out.print("희망 습도 설정: ");
		presetDTO.setOptimalHumidity(scanner.nextFloat());
		System.out.print("희망 조도 설정: ");
		presetDTO.setLightIntensity(scanner.nextFloat());
		System.out.print("희망 CO2농도 설정: ");
		presetDTO.setCo2Level(scanner.nextFloat());
		System.out.print("희망 토양 습도 설정: ");
		presetDTO.setSoilMoisture(scanner.nextFloat());
		System.out.print("예상 생장 기간(일): ");
		presetDTO.setGrowthPeriodDays(scanner.nextInt());
		
		return presetDTO;
	}
	
	public String[] showPresetMenu(PresetDTO presetDTO) {
		String[] value = new String[4];  // value[0]: (식물 이름) / value[1]: (기기 시리얼 넘버) / value[2]: (슬롯번호) / value[3]: 1(확인), 2(취소)
		System.out.println("식물 이름: " + presetDTO.getPlantName());
		System.out.println("적정 온도: " + presetDTO.getOptimalTemp());
		System.out.println("적정 습도: " + presetDTO.getOptimalHumidity());
		System.out.println("적정 조도: " + presetDTO.getLightIntensity());
		System.out.println("적정 CO2농도: " + presetDTO.getCo2Level());
		System.out.println("적정 토양 습도: " + presetDTO.getSoilMoisture());
		System.out.println("예상 생장 기간(일): " + presetDTO.getGrowthPeriodDays());
		value[0] = presetDTO.getPlantName();
		scanner.nextLine();
		System.out.print("예상 생장 기간(일): ");
		value[1] = scanner.nextLine();
		System.out.print("슬롯 번호를 입력하세요: ");
		value[2] = scanner.nextLine();
		
		System.out.println("\n--------------------------------------------------");
		System.out.println("  [1] 확인");
		System.out.println("  [2] 취소");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		value[3] = scanner.nextLine();
		return value;
	}
	
	public String showMyPageMenu() {
        System.out.println("\n원하시는 메뉴를 선택해주세요.\n");
        System.out.println("  [1] 정보 조회");
        System.out.println("  [2] 정보 수정");
        System.out.println("  [3] 기기 추가");
        System.out.println("  [4] 기기 삭제");
        System.out.println("  [8] 뒤로가기");
        System.out.println("  [9] 프로그램 종료");
        System.out.println("\n--------------------------------------------------");
        System.out.print("> 입력: ");
        return scanner.nextLine();
    }
	
	public void showInfo(MemberDTO user, ArrayList<DeviceDTO> devices) {
		System.out.println("\n아이디: " + user.getUserId());
		System.out.println("이름: " + user.getName());
		System.out.println("이메일: " + user.getEmail());
		System.out.println("등록된 기기: ");

		System.out.println("\n원하시는 메뉴를 선택해주세요.\n");
		System.out.println("  [1] 정보 조회");
		System.out.println("  [2] 정보 수정");
		System.out.println("  [3] 기기 추가");
		System.out.println("  [4] 기기 삭제");
		System.out.println("  [8] 뒤로가기");
		System.out.println("  [9] 프로그램 종료");
		System.out.println("\n--------------------------------------------------");
		if (devices.isEmpty()) {
	        System.out.println("  (등록된 기기가 없습니다)");
	    } else {
	        int i = 1;
	        for (DeviceDTO d : devices) {
	            System.out.println("  " + (i++) + ". " + d.getDeviceSerialNumber());
	        }
	    }
	    System.out.println("\n--------------------------------------------------");
	    
	}
	public String showUpdateMenu() {
	
		System.out.println("  [1] 비밀번호 수정");
	    System.out.println("  [2] 이름 수정");
	    System.out.println("  [3] 이메일 수정");
	    System.out.println("  [8] 뒤로가기");
	    System.out.println("\n--------------------------------------------------");
	    System.out.print("> 입력: ");
	    return scanner.nextLine();
	}
	public String getNewValue(String label) {
	    System.out.print("\n" + label + "을(를) 입력하세요: ");
	    return scanner.nextLine();
	}
	
	
	public String showAddDevice() {
		System.out.println("\n추가할 기기의 시리얼 넘버를 입력해주세요.");
		System.out.print("  기기 시리얼 넘버: ");
		String dsn =  scanner.nextLine();
		System.out.println("\n--------------------------------------------------");
		return dsn;
	}
	
	public int showDeleteDevice() {
		System.out.println("\n삭제할 기기의 번호를 입력해주세요.");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		return scanner.nextInt();
	}
	
	public void showFarmDetail(FarmDTO farm, PresetDTO preset, SensorDataDTO latestData) {
		System.out.println("\n==================================================");
		System.out.println("농장 상세 정보");
		System.out.println("==================================================");
		System.out.println("\nFarm ID: " + farm.getFarmUid());
		
		if(preset != null && preset.getPlantName() != null) {
			System.out.println("식물: " + preset.getPlantName());
		} else {
			System.out.println("식물: 정보 없음");
		}
		
		if(farm.getPlanting_date() != null) {
			System.out.println(" 심은 날짜: " + farm.getPlanting_date());
			long diff = System.currentTimeMillis() - farm.getPlanting_date().getTime();
			long days = diff / (1000 * 60 * 60 * 24);
			System.out.println("  경과 일수: " + days + "일");
		}
		
		System.out.println(" 현재 센서 측정값 (최근 기록)");
		System.out.println("--------------------------------------------------");
		
		if(latestData != null) {
			System.out.println(" 측정 시간: " + latestData.getRecordedAt());
			
			// 온도
			if(latestData.getMeasuredTemp() != null) {
				System.out.print("  온도: " + latestData.getMeasuredTemp() + "°C");
				if(preset != null && preset.getOptimalTemp() > 0) {
					System.out.print("    [적정: " + preset.getOptimalTemp() + "°C]");
					float diff = Math.abs(latestData.getMeasuredTemp() - preset.getOptimalTemp());
					if(diff <= 2.0) {
						System.out.println(" 정상");
					} else if(diff <= 5.0) {
						System.out.println(" 주의");
					} else {
						System.out.println(" 위험");
					}
				} else {
					System.out.println();
				}
			}
			
			// 습도
			if(latestData.getMeasuredHumidity() != null) {
				System.out.print(" 습도: " + latestData.getMeasuredHumidity() + "%");
				if(preset != null && preset.getOptimalHumidity() > 0) {
					System.out.print("        [적정: " + preset.getOptimalHumidity() + "%]");
					float diff = Math.abs(latestData.getMeasuredHumidity() - preset.getOptimalHumidity());
					if(diff <= 5.0) {
						System.out.println(" 정상");
					} else if(diff <= 10.0) {
						System.out.println(" 주의");
					} else {
						System.out.println(" 위험");
					}
				} else {
					System.out.println();
				}
			}
			
			// CO2
			if(latestData.getMeasuredCo2() != null) {
				System.out.print(" CO2: " + latestData.getMeasuredCo2() + "ppm");
				if(preset != null && preset.getCo2Level() > 0) {
					System.out.print("    [적정: " + preset.getCo2Level() + "ppm]");
					float diff = Math.abs(latestData.getMeasuredCo2() - preset.getCo2Level());
					if(diff <= 50.0) {
						System.out.println("정상");
					} else if(diff <= 100.0) {
						System.out.println("주의");
					} else {
						System.out.println("위험");
					}
				} else {
					System.out.println();
				}
			}
			
			// 토양 습도
			if(latestData.getMeasuredSoilMoisture() != null) {
				System.out.print(" 토양습도: " + latestData.getMeasuredSoilMoisture());
				if(preset != null && preset.getSoilMoisture() > 0) {
					System.out.print("    [적정: " + preset.getSoilMoisture() + "]");
					float diff = Math.abs(latestData.getMeasuredSoilMoisture() - preset.getSoilMoisture());
					if(diff <= 20.0) {
						System.out.println("정상");
					} else if(diff <= 50.0) {
						System.out.println("주의");
					} else {
						System.out.println("위험");
					}
				} else {
					System.out.println();
				}
			}
		} else {
			System.out.println("  (!) 센서 데이터가 없습니다.");
		}
		
		System.out.println();
	}
	
	public String showFarmDetailMenu() {
		System.out.println("\n원하시는 메뉴를 선택해주세요.\n");
		System.out.println("  [1] 센서 데이터 목록 보기 (최근 10개)");
		System.out.println("  [8] 뒤로가기");
		System.out.println("  [9] 프로그램 종료");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		return scanner.nextLine();
	}
	
	public void showSensorDataList(String farmUid) {
		SensorDataService sensorDataService = new SensorDataServiceImpl();
		System.out.println("\n==================================================");
		System.out.println("센서 데이터 기록 (최근 10개)");
		System.out.println("==================================================\n");
		
		List<SensorDataDTO> dataList = sensorDataService.getLogsByFarm(farmUid, 24, 10);
		
		if(dataList != null && !dataList.isEmpty()) {
			for(SensorDataDTO data : dataList) {
				System.out.println("기록시각각 " + data.getRecordedAt());
				System.out.println(" 온도: " + data.getMeasuredTemp() + "°C" +
						         "  습도: " + data.getMeasuredHumidity() + "%" +
						         "  CO2: " + data.getMeasuredCo2() + "ppm" +
						         "   토양: " + data.getMeasuredSoilMoisture());
				System.out.println();
			}
		} else {
			System.out.println("  (!) 센서 데이터가 없습니다.\n");
		}
	}
	public String showNotificationManagementMenu(boolean bool) {
		if (bool) {
			System.out.println("\n새로운 메시지가 없습니다.");
			System.out.println("엔터를 눌러주세요.");
			scanner.nextLine();
			return "0";
		} else {
			System.out.println("\n확인한 메시지의 번호를 입력해주세요.");
			System.out.println("입력한 번호의 메시지는 삭제됩니다. (예시: 1,3,5)");
			System.out.println("전체 삭제를 원할 경우 ALL을 입력해주세요.\n");
			System.out.print("> 입력: ");
			return scanner.nextLine();
		}
	}
}





















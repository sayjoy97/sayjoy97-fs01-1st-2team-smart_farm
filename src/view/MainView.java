package view;

import java.util.Scanner;

import dto.LoginUserDTO;
import dto.MemberDTO;
import dto.PresetDTO;
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
		System.out.println("  [5] 뒤로 가기");
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
		System.out.println("  [1] 추천 식물 1: 이름");
		System.out.println("  [2] 추천 식물 2: 이름");
		System.out.println("  [3] 추천 식물 3: 이름");
		System.out.println("  [4] 신규 식물 추가");
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
	
	public String showPresetMenu(PresetDTO presetDTO) {
		System.out.println("식물 이름: " + presetDTO.getPlantName());
		System.out.println("적정 온도: " + presetDTO.getOptimalTemp());
		System.out.println("적정 습도: " + presetDTO.getOptimalHumidity());
		System.out.println("적정 조도: " + presetDTO.getLightIntensity());
		System.out.println("적정 CO2농도: " + presetDTO.getCo2Level());
		System.out.println("적정 토양 습도: " + presetDTO.getSoilMoisture());
		System.out.println("예상 생장 기간(일): " + presetDTO.getGrowthPeriodDays());
		
		System.out.println("\n--------------------------------------------------");
		System.out.println("  [1] 확인");
		System.out.println("  [2] 취소");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		return scanner.nextLine();
	}
	
	public String showMyPageMenu() {
		System.out.println("=============================");
	    System.out.println("⚙️  마이페이지 메뉴");
	    System.out.println("=============================");
		System.out.println("  [1] 내 정보 조회 ");
		System.out.println("  [2] 내 정보 수정 ");
		System.out.println("  [3] 뒤로가기 ");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		return scanner.nextLine();
	}
	public String mypageUpdateMenu() {
		System.out.println("\n 수정할 메뉴를 선택해주세요.\n");
		System.out.println("  [1] 비밀번호 ");
		System.out.println("  [2] 이름 ");
		System.out.println("  [3] 이메일 ");
		System.out.println("  [4] 뒤로가기 ");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		return scanner.nextLine();
	}
	public String mypageSelectMenu() {
		System.out.println("\n 수정할 메뉴를 선택해주세요.\n");
		System.out.println("  [1] 비밀번호 ");
		System.out.println("  [2] 이름 ");
		System.out.println("  [3] 이메일 ");
		System.out.println("  [4] 뒤로가기 ");
		System.out.println("\n--------------------------------------------------");
		System.out.print("> 입력: ");
		return scanner.nextLine();
	}
	
	
	
}





















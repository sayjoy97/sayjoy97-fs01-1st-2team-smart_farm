package view;

import java.util.Scanner;

import dto.LoginUserDTO;
import dto.MemberDTO;

public class MainView {
	private static final Scanner scanner = new Scanner(System.in);

	public String showInitialMenu() {
		System.out.println("\n==================================================");
		System.out.println("      🌿 라즈베리파이 스마트홈 제어 시스템 🌿");
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
		System.out.println("      🌿 라즈베리파이 스마트홈 제어 시스템 🌿");
		System.out.println("==================================================");
		System.out.printf("%s님, 환영합니다!         현재 상태: 양호 ✅\n\n", "김서연");
		System.out.println("  [1] 장치 제어 💡");
		System.out.println("  [2] 센서 데이터 분석 📊");
		System.out.println("  [3] 환경 설정 ⚙️");
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
                newMember.setPass(passwordConfirm);
                break;
            } else {
                System.out.println("\n  (!) 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
            }
        }
        
        System.out.print("  [필수] 이름: ");
        newMember.setName(scanner.nextLine());
        System.out.print("  [필수] 이메일: ");
        newMember.setEmail(scanner.nextLine());
        System.out.print("  [선택] 주소: ");
        newMember.setAddress(scanner.nextLine());
        
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
		// TODO Auto-generated method stub
		
	}
}

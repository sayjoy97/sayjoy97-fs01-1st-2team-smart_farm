package util;

	public final class ConsoleUtils {
	    public static void clearConsole() {
	        // 50~80줄 정도의 빈 줄을 출력해서 이전 내용이 안 보이게 합니다.
	        for (int i = 0; i < 60; i++) {
	            System.out.println("");
	        }
	    }
	}

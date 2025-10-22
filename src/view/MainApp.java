package view;

import javax.swing.JFrame;

import controller.MainController;

public class MainApp {
	public static void main(String[] args) {
	
        JFrame frame = new JFrame();
        frame.setUndecorated(true); // 창 장식(제목표시줄 등)을 없앰
        frame.setVisible(true);   // 잠깐 보이게 해서 시스템을 활성화
        frame.setVisible(false);  // 바로 다시 안보이게 함
        frame.dispose();          // 메모리에서 완전히 제거
		MainController mainController = new MainController();
	    mainController.run(); // 컨트롤러 실행	
	}
}

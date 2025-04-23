package org.minhquan.app;

import javax.swing.*;
import java.awt.event.WindowFocusListener;

public class GameWindow {

    private JFrame jFrame;

    public GameWindow(GamePanel gamePanel) {
        jFrame = new JFrame();

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(gamePanel);
        jFrame.pack(); // lấy tỉ lệ cửa sổ theo panel
        jFrame.setLocationRelativeTo(null); // đặt giữa màn hình
        jFrame.setResizable(false); // không cho chỉnh kích thước
        jFrame.setVisible(true);
        jFrame.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }

            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                gamePanel.requestFocus();
            }
        });
    }
}

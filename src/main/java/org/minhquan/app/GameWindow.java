package org.minhquan.app;

import javax.swing.*;

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
    }
}

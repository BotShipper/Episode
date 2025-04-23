package org.minhquan.app;

import lombok.Getter;
import org.minhquan.input.KeyboardInput;
import org.minhquan.input.MouseInput;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private MouseInput mouseInput;

    @Getter
    private Game game;

    public GamePanel(Game game) {
        mouseInput = new MouseInput(this);
        this.game = game;

        setPanelSize(); // thiết lập kích thước panel
        addKeyListener(new KeyboardInput(this)); // xử lý phím
        addMouseListener(mouseInput);                      // xử lý click chuột
        addMouseMotionListener(mouseInput);                // xử lý di chuyển chuột
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void updateGame() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        game.render(g);
    }
}

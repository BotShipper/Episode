package org.minhquan.app;

import lombok.Getter;
import org.minhquan.input.KeyboardInput;
import org.minhquan.input.MouseInput;

import javax.swing.*;
import java.awt.*;

import static org.minhquan.app.Game.GAME_HEIGHT;
import static org.minhquan.app.Game.GAME_WIDTH;

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
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
        System.out.println("Size:" + GAME_WIDTH + "x" + GAME_HEIGHT);
    }

    public void updateGame() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        game.render(g);
    }
}

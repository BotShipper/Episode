package org.minhquan.input;


import lombok.extern.slf4j.Slf4j;
import org.minhquan.app.GamePanel;
import org.minhquan.gamestate.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@Slf4j
public class KeyboardInput implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().keyReleased(e);
            case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);
            default -> log.error("No state found -> keyReleased");
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (GameState.state) {
            case MENU -> gamePanel.getGame().getMenu().keyPressed(e);
            case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);
            default -> log.error("No state found -> keyPressed");
        }
    }
}

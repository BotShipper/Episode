package org.minhquan.gamestate;

import lombok.Getter;
import org.minhquan.app.Game;
import org.minhquan.entity.Player;
import org.minhquan.level.LevelManager;
import org.minhquan.ui.PauseOverlay;
import org.minhquan.util.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Playing extends State implements StateMethod {
    private LevelManager levelManager;
    private PauseOverlay pauseOverlay;
    @Getter
    private Player player;
    private boolean paused;

    private int xLvOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int lvlTilesWide = Objects.requireNonNull(LoadSave.GetLevelData())[0].length; // Chiều rộng toàn bản đồ
    private int maxTilesOffet = lvlTilesWide - Game.TILES_IN_WIDTH; // Số ô camera có thể di chuyển - số ô không nằm trong bản đồ
    private int maxLvOffsetX = maxTilesOffet * Game.TILES_SIZE; // Tính theo pixel

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            player.update();
            checkCloseToBorder();
        } else {
            pauseOverlay.update();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvOffset;

        if (diff > rightBorder) {
            xLvOffset += diff - rightBorder;
        } else if (diff < leftBorder) {
            xLvOffset += diff - leftBorder;
        }

        if (xLvOffset > maxLvOffsetX) {
            xLvOffset = maxLvOffsetX;
        } else if (xLvOffset < 0) {
            xLvOffset = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g, xLvOffset);
        player.render(g, xLvOffset);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused) {
            pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(true);
            case KeyEvent.VK_D -> player.setRight(true);
            case KeyEvent.VK_SPACE -> player.setJump(true);
            case KeyEvent.VK_BACK_SPACE -> paused = !paused;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(false);
            case KeyEvent.VK_D -> player.setRight(false);
            case KeyEvent.VK_SPACE -> player.setJump(false);
        }
    }

    public void unpauseGame() {
        paused = false;
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE));
        player.loadLvData(levelManager.getCurrentLevel().getLvData());
        pauseOverlay = new PauseOverlay(this);
    }

    public void windowFocusLost() {
        player.resetDirBoolean();
    }
}

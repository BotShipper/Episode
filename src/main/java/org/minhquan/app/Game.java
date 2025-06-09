package org.minhquan.app;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.minhquan.gamestate.GameState;
import org.minhquan.gamestate.Menu;
import org.minhquan.gamestate.Playing;

import java.awt.*;

@Slf4j
public class Game implements Runnable {

    /**
     * <h5>Các hằng số cấu hình cho trò chơi.</h5>
     * - <b>TILES_DEFAULT_SIZE</b>: Kích thước mặc định của mỗi ô (tile) trong trò chơi, được đặt là 32 pixel.<br>
     * - <b>SCALE</b>: Hệ số tỉ lệ dùng để thay đổi kích thước của các ô trong trò chơi, giá trị là 1.5f (phóng to 1.5 lần).<br>
     * - <b>TILES_IN_WIDTH</b>: Số lượng ô theo chiều rộng của màn hình game. Được đặt là 26 ô.<br>
     * - <b>TILES_IN_HEIGHT</b>: Số lượng ô theo chiều cao của màn hình game. Được đặt là 14 ô.<br>
     * - <b>TILES_SIZE</b>: Kích thước thực tế của mỗi ô trong trò chơi, tính theo tỉ lệ SCALE.<br>
     * - <b>GAME_WIDTH</b>: Chiều rộng tổng thể của cửa sổ game, tính theo pixel, bằng số lượng ô theo chiều rộng nhân với kích thước của mỗi ô.<br>
     * - <b>GAME_HEIGHT</b>: Chiều cao tổng thể của cửa sổ game, tính theo pixel, bằng số lượng ô theo chiều cao nhân với kích thước của mỗi ô.<br>
     */
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.0f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_IN_WIDTH * TILES_SIZE;
    public final static int GAME_HEIGHT = TILES_IN_HEIGHT * TILES_SIZE;

    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    @Getter
    private Playing playing;
    @Getter
    private Menu menu;

    public Game() {

        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true); // Cho phép panel nhận focus
        gamePanel.requestFocus(); // nhận sự kiện bàn phím
        startGameLoop();
    }

    private void initClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (GameState.state) {
            case MENU -> menu.update();
            case PLAYING -> playing.update();
            default -> {
                log.error("No state found -> update");
                System.exit(0);
            }
        }
    }

    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU -> menu.draw(g);
            case PLAYING -> playing.draw(g);
            default -> log.error("No state found -> render");
        }
    }

    @Override
    public void run() {

        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint(); // Chạy lại paintComponent()
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            playing.getPlayer().resetDirBoolean();
        }
    }
}

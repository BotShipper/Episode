package org.minhquan.level;

import lombok.extern.slf4j.Slf4j;
import org.minhquan.app.Game;
import org.minhquan.gamestate.GameState;
import org.minhquan.util.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@Slf4j
public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;


    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("No more levels! Game completed!");
            GameState.state = GameState.MENU;
        }

        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvData(newLevel.getLvData());
        game.getPlaying().setMaxLvOffsetX(newLevel.getLvlOffset());
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        if (allLevels == null) {
            log.error("allLevels == null -> buildAllLevels");
            return;
        }

        for (BufferedImage img : allLevels) {
            levels.add(new Level(img));
        }
    }

    private void importOutsideSprites() {

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        if (img == null) {
            log.error("img == null -> importOutsideSprites");
            return;
        }

        levelSprite = new BufferedImage[48];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 12; i++) {
                int index = i + j * 12;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }

    public void draw(Graphics g, int lvlOffset) {
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLvData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                if (index >= 0) {
                    g.drawImage(levelSprite[index], i * Game.TILES_SIZE - lvlOffset, j * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
                }
            }
        }
    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }
}

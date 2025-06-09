package org.minhquan.level;

import lombok.Getter;
import org.minhquan.app.Game;
import org.minhquan.entity.Crabby;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.minhquan.util.HelpMethod.*;

@Getter
public class Level {

    private BufferedImage img;
    private int[][] lvData;
    private ArrayList<Crabby> crabs;
    private int lvlTilesWide; // Chiều rộng toàn bản đồ
    private int maxTilesOffset; // Số ô camera có thể di chuyển - số ô không nằm trong bản đồ
    private int maxLvOffsetX; // Tính theo pixel
    private Point lvlSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        calcLvlOffset();
        calcPlayerSpawn();
    }

    private void calcPlayerSpawn() {
        lvlSpawn = GetPlayerSpawn(img);
    }

    private void calcLvlOffset() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        lvData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvData[y][x];
    }

    public int getLvlOffset() {
        return maxLvOffsetX;
    }
}

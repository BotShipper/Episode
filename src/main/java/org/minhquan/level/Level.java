package org.minhquan.level;

import lombok.Getter;

public class Level {

    @Getter
    private int[][] lvData;

    public Level(int[][] lvData) {
        this.lvData = lvData;
    }

    public int getSpriteIndex(int x, int y) {
        return lvData[y][x];
    }
}

package org.minhquan.util;

import org.minhquan.app.Game;

public class HelpMethod {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvData) {

        if (!IsSolid(x, y, lvData))
            if (!IsSolid(x + width, y + height, lvData))
                if (!IsSolid(x + width, y, lvData))
                    return !IsSolid(x, y + height, lvData);

        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvData) {
        if (x < 0 || x >= Game.GAME_WIDTH)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        int value = lvData[(int) yIndex][(int) xIndex];

        return value >= 48 || value < 0 || value != 11;
    }
}

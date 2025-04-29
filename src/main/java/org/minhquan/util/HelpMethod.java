package org.minhquan.util;

import org.minhquan.app.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethod {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvData) {

        if (!IsSolid(x, y, lvData))
            if (!IsSolid(x + width, y + height, lvData))
                if (!IsSolid(x + width, y, lvData))
                    return !IsSolid(x, y + height, lvData);

        return false;
    }

    // Kiểm tra xem có phải ô chắn không
    private static boolean IsSolid(float x, float y, int[][] lvData) {
        if (x < 0 || x >= Game.GAME_WIDTH)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        int value = lvData[(int) yIndex][(int) xIndex];

        return value != 11;
    }

    // Đứng mép tránh đi xuyên tường
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) hitbox.x / Game.TILES_SIZE;
        if (xSpeed > 0) {
            // Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            // Left
            return currentTile * Game.TILES_SIZE;
        }
    }

    // Chạm mép tránh xuyên sàn hoặc tường
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            // Falling
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            // Jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    // Kiểm tra có đang đứng trên sàn
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvData) {
        // Check the pixel below bottomLeft and bottomRight
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height, lvData)) {
            return IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvData);
        }
        return true;
    }
}

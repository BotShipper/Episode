package org.minhquan.util;

import lombok.extern.slf4j.Slf4j;
import org.minhquan.app.Game;
import org.minhquan.entity.Crabby;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.minhquan.util.Constant.EnemyConstants.CRABBY;

@Slf4j
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
        int maxWidth = lvData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, lvData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvData) {
        int value = lvData[yTile][xTile];

        return value != 11; // 11 là ô không khí
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

    /**
     * Ở đây, chúng ta chỉ kiểm tra điểm ở dưới bên trái của kẻ địch, cộng hoặc trừ xSpeed.
     * Chúng ta không kiểm tra điểm dưới bên phải trong trường hợp kẻ địch đang di chuyển sang phải.
     * Cách kiểm tra đúng hơn là kiểm tra điểm dưới bên trái khi đi sang trái
     * và điểm dưới bên phải khi đi sang phải.
     * Tuy nhiên, điều này sẽ không ảnh hưởng nhiều đến trò chơi.
     * Kẻ địch đơn giản sẽ đổi hướng sớm hơn khi gặp mép bên phải,
     * nếu nó đang di chuyển sang phải
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvData) {
        if (xSpeed > 0) {
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvData);
        } else {
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvData);
        }
    }

    public static boolean IsAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData)) {
                return false;
            }
            if (!IsTileSolid(xStart + i, y + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox,
                                       Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile) {
            return IsAllTileWalkable(secondXTile, firstXTile, yTile, lvlData);
        } else {
            return IsAllTileWalkable(firstXTile, secondXTile, yTile, lvlData);
        }
    }

    // Lấy dải màu để ghép map (đất, nước, không khí)
    public static int[][] GetLevelData(BufferedImage img) {
        if (img == null) {
            log.error("img == null -> GetLevelData");
            return null;
        }

        int[][] lvData = new int[img.getHeight()][img.getWidth()];
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48) {
                    value = 0;
                }
                lvData[j][i] = value;
            }
        }
        return lvData;
    }

    public static ArrayList<Crabby> GetCrabs(BufferedImage img) {
        if (img == null) {
            log.error("img == null -> GetCrabs");
            return null;
        }
        ArrayList<Crabby> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == CRABBY) {
                    list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
                }
            }
        }
        return list;
    }

    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == 100) {
                    return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
                }
            }
        }
        return new Point(Game.TILES_SIZE, Game.TILES_SIZE);
    }
}

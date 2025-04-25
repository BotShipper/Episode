package org.minhquan.util;

import org.minhquan.app.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        String path = "/" + fileName;
        InputStream is = LoadSave.class.getResourceAsStream(path);

        try (is) {
            try {
                if (is == null) {
                    System.err.println("Không tìm thấy file: " + path);
                    return null;
                }
                img = ImageIO.read(is);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    public static int[][] GetLevelData() {
        int[][] lvData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
        if (img == null) {
            System.err.println("img == null -> GetLevelData");
            return null;
        }
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
}

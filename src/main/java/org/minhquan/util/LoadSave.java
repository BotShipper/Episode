package org.minhquan.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png"; // Hành động đi, đứng nhảy
    public static final String LEVEL_ATLAS = "outside_sprites.png"; // Nền đất, tường, không khí
    // public static final String LEVEL_ONE_DATA = "level_one_data.png"; // Dùng màu để kiểm tra tường
    public static final String LEVEL_ONE_DATA = "level_one_data_long.png"; // Dùng màu để kiểm tra tường
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        String path = "/" + fileName;
        InputStream is = LoadSave.class.getResourceAsStream(path);

        try (is) {
            try {
                if (is == null) {
                    log.error("Không tìm thấy file: {}", path);
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

    // Lấy dải màu để ghép map (đất, nước, không khí)
    public static int[][] GetLevelData() {
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
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
}

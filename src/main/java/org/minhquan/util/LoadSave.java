package org.minhquan.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png"; // Hành động đi, đứng nhảy
    public static final String LEVEL_ATLAS = "outside_sprites.png"; // Nền đất, tường, không khí
    // public static final String LEVEL_ONE_DATA = "level_one_data.png"; // Dùng màu để kiểm tra tường
    // public static final String LEVEL_ONE_DATA = "level_one_data_long.png"; // Dùng màu để kiểm tra tường
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";
    public static final String PLAYING_BG_IMG = "playing_bg_img.png";
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String CRABBY_SPITE = "crabby_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";

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

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/lvls");
        if (url == null) {
            log.error("url == null -> GetAllLevels");
            return null;
        }

        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            log.error("URISyntaxException -> GetAllLevels");
            return null;
        }

        File[] files = file.listFiles();
        if (files == null) {
            log.error("files == null -> GetAllLevels");
            return null;
        }
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png")) {
                    filesSorted[i] = files[j];
                }
            }
        }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];
        for (int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imgs;
    }
}

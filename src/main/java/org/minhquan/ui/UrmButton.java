package org.minhquan.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.minhquan.util.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.minhquan.util.Constant.UI.URMButtons.URM_DEFAULT_SIZE;
import static org.minhquan.util.Constant.UI.URMButtons.URM_SIZE;

@Slf4j
@Getter
@Setter
public class UrmButton extends PauseButton {

    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        if (temp == null) {
            log.error("temp == null -> loadImgs");
            return;
        }
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
        }
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    public void resetBooleans() {
        mouseOver = false;
        mousePressed = false;
    }
}

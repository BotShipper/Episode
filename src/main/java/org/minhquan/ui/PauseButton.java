package org.minhquan.ui;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class PauseButton {

    protected int x, y, width, height;
    protected Rectangle bounds;

    public PauseButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    private void createBounds() {
        bounds = new Rectangle(x, y, width, height);
    }
}

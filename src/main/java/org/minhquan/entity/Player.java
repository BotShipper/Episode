package org.minhquan.entity;

import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.minhquan.util.Constant.PlayerConstant.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    @Setter
    private boolean isMoving = false, isAttacking = false;
    @Setter
    private boolean isLeft, isUp, isRight, isDown;
    private float playerSpeed = 2.0f;


    public Player(float x, float y) {
        super(x, y);
        loadAnimation();
    }

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimationTick();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][aniIndex], (int) x, (int) y, 256, 160, null);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                isAttacking = false;
            }
        }
    }

    private void setAnimationTick() {

        int startAni = playerAction;

        if (isMoving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if (isAttacking) {
            playerAction = ATTACK_1;
        }

        if (playerAction != startAni) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {

        isMoving = false;

        if (isLeft && !isRight) {
            x -= playerSpeed;
            isMoving = true;
        } else if (isRight && !isLeft) {
            x += playerSpeed;
            isMoving = true;
        }

        if (isUp && !isDown) {
            y -= playerSpeed;
            isMoving = true;
        } else if (isDown && !isUp) {
            y += playerSpeed;
            isMoving = true;
        }
    }

    private void loadAnimation() {
        String path = "/player_sprites.png";
        InputStream is = getClass().getResourceAsStream(path);

        try (is) {
            try {
                if (is == null) {
                    System.err.println("Không tìm thấy file: " + path);
                    return;
                }
                BufferedImage img = ImageIO.read(is);

                animations = new BufferedImage[9][6];
                for (int j = 0; j < animations.length; j++) {
                    for (int i = 0; i < animations[j].length; i++) {
                        animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetDirBooleans() {
        isLeft = false;
        isRight = false;
        isUp = false;
        isDown = false;
    }
}

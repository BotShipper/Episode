package org.minhquan.entity;

import lombok.Setter;
import org.minhquan.util.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

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

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        if (img == null) {
            System.err.println("img == null -> loadAnimation");
            return;
        }

        animations = new BufferedImage[9][6];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }

    }

    public void resetDirBooleans() {
        isLeft = false;
        isRight = false;
        isUp = false;
        isDown = false;
    }
}

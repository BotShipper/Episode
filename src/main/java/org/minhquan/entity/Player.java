package org.minhquan.entity;

import lombok.Setter;
import org.minhquan.app.Game;
import org.minhquan.util.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.minhquan.util.Constant.PlayerConstant.*;
import static org.minhquan.util.HelpMethod.CanMoveHere;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    @Setter
    private boolean isMoving = false, isAttacking = false;
    @Setter
    private boolean isLeft, isUp, isRight, isDown;
    private float playerSpeed = 2.0f;
    @Setter
    private int[][] lvData;
    private float xDrawOffset = 21 * Game.SCALE, yDrawOffset = 4 * Game.SCALE;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimation();
        initHitbox(x, y, 20 * Game.SCALE, 28 * Game.SCALE);
    }

    public void update() {
        updatePos();              // Cập nhật vị trí
        updateAnimationTick();    // Cập nhật frame animation
        setAnimationTick();       // Xác định trạng thái animation hiện tại
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null);
        drawHitbox(g); // Vẽ hitbox để debug (nếu cần)
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
        if (!isLeft && !isRight && !isUp && !isDown)
            return;

        float xSpeed = 0, ySpeed = 0;

        if (isLeft && !isRight)
            xSpeed = -playerSpeed;
        else if (isRight && !isLeft)
            xSpeed = playerSpeed;


        if (isUp && !isDown)
            ySpeed = -playerSpeed;
        else if (isDown && !isUp)
            ySpeed = playerSpeed;

//        if (CanMoveHere(x + xSpeed, y + ySpeed, width, height, lvData)) {
//            this.x += xSpeed;
//            this.y += ySpeed;
//            isMoving = true;
//        }

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvData)) {
            hitbox.x += xSpeed;
            hitbox.y += ySpeed;
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

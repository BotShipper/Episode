package org.minhquan.entity;

import lombok.Setter;
import org.minhquan.app.Game;
import org.minhquan.util.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.minhquan.util.Constant.PlayerConstant.*;
import static org.minhquan.util.HelpMethod.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    @Setter
    private boolean isMoving = false, isAttacking = false;
    @Setter
    private boolean isLeft, isUp, isRight, isDown, isJump;
    private float playerSpeed = 2.0f;
    private int[][] lvData;
    private float xDrawOffset = 21 * Game.SCALE, yDrawOffset = 4 * Game.SCALE;

    // Jumping / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimation();
        initHitbox(x, y, 20 * Game.SCALE, 27 * Game.SCALE);

    }

    public void update() {
        updatePos();              // Cập nhật vị trí
        updateAnimationTick();    // Cập nhật frame animation
        setAnimation();       // Xác định trạng thái animation hiện tại
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null);
        // drawHitbox(g); // Vẽ hitbox để debug (nếu cần)
    }

    // Hiển thị hành động
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

    private void setAnimation() {
        int startAni = playerAction;

        if (isMoving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0) {
                playerAction = JUMP;
            } else {
                playerAction = FALLING;
            }
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

        if (isJump) {
            jump();
        }
        if (!isLeft && !isRight && !inAir)
            return;

        float xSpeed = 0;

        if (isLeft)
            xSpeed -= playerSpeed;
        if (isRight)
            xSpeed += playerSpeed;

        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, lvData)) {
                inAir = true;
            }
        }

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
        }
        isMoving = true;
    }

    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
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

    public void loadLvData(int[][] lvData) {
        this.lvData = lvData;
        if (!IsEntityOnFloor(hitbox, lvData)) {
            inAir = true;
        }
    }

    public void resetDirBooleans() {
        isLeft = false;
        isRight = false;
        isUp = false;
        isDown = false;
    }
}

package org.minhquan.entity;

import lombok.Getter;
import lombok.Setter;
import org.minhquan.app.Game;

import static org.minhquan.util.Constant.Direction.LEFT;
import static org.minhquan.util.Constant.Direction.RIGHT;
import static org.minhquan.util.Constant.EnemyConstants.*;
import static org.minhquan.util.HelpMethod.*;

@Getter
@Setter
public abstract class Enemy extends Entity {
    private int aniIndex, enemyState, enemyType;
    private int aniTick, aniSpeed = 25;
    private boolean firstUpdate = true;
    private boolean inAir;
    private float fallSpeed;
    private float gravity = 0.04f * Game.SCALE;
    private float walkSpeed = 0.35f * Game.SCALE;
    private int walkDir = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
            }
        }
    }

    public void update(int[][] lvlData) {
        updateMove(lvlData);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData) {
        if (firstUpdate) {
            if (!IsEntityOnFloor(hitbox, lvlData)) { // Kiểm tra xem có trên không không
                inAir = true;
            }
            firstUpdate = false;
        }
        if (inAir) {
            // Kiểm tra có thể rơi hay không
            // Nếu không cập nhật vị trí y
            if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir = false;
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            }
        } else {
            switch (enemyState) {
                case IDLE -> enemyState = RUNNING;
                case RUNNING -> {
                    float xSpeed = 0;
                    if (walkDir == LEFT) {
                        xSpeed -= walkSpeed;
                    } else {
                        xSpeed = walkSpeed;
                    }

                    if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
                        if (IsFloor(hitbox, xSpeed, lvlData)) { // Kiểm tra có mặt đất không
                            hitbox.x += xSpeed;
                            return;
                        }
                    }
                    changeWalkDir(); // Chuyển hướng di chuyển
                }
            }
        }
    }

    private void changeWalkDir() {
        if (walkDir == LEFT) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }
}

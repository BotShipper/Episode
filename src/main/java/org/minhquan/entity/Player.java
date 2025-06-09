package org.minhquan.entity;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.minhquan.app.Game;
import org.minhquan.gamestate.Playing;
import org.minhquan.util.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.minhquan.util.Constant.PlayerConstant.*;
import static org.minhquan.util.HelpMethod.*;

@Slf4j
public class Player extends Entity {

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    @Setter
    private boolean isMoving = false, isAttacking = false;
    @Setter
    private boolean isLeft, isUp, isRight, isDown, isJump;
    private float playerSpeed = Game.SCALE;
    private int[][] lvData;
    private float xDrawOffset = 21 * Game.SCALE, yDrawOffset = 4 * Game.SCALE;

    // Jumping / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    // StatusBarUI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    // AttackBox
    private Rectangle2D.Float attackBox;

    private int flipX = 0;
    private int flixW = 1;

    private boolean attackChecked;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimation();
        initHitbox(x, y, (int) (20 * Game.SCALE), (int) (27 * Game.SCALE));
        initAttackBox();
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, 20 * Game.SCALE, 20 * Game.SCALE);
    }

    public void update() {
        updateHealthBar();

        if (currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }

        updateAttackBox();

        updatePos();              // Cập nhật vị trí
        if (isAttacking) {
            checkAttack();
        }
        updateAnimationTick();    // Cập nhật frame animation
        setAnimation();       // Xác định trạng thái animation hiện tại
    }

    private void checkAttack() {
        if (attackChecked || aniIndex != 1) {
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if (isRight) {
            attackBox.x = hitbox.x + hitbox.width + 10 * Game.SCALE;
        } else if (isLeft) {
            attackBox.x = hitbox.x - hitbox.width - 10 * Game.SCALE;
        }
        attackBox.y = hitbox.y + 10 * Game.SCALE;
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex],
                (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
                (int) (hitbox.y - yDrawOffset),
                width * flixW, height, null);
        drawHitbox(g, lvlOffset); // Vẽ hitbox để debug (nếu cần)
        drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvlOffset) {
        g.setColor(Color.RED);
        g.drawRect((int) attackBox.x - lvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
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
                attackChecked = false;
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
            playerAction = ATTACK;
            if (startAni != ATTACK) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
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

        if (!inAir) {
            if ((!isLeft && !isRight) || (isLeft && isRight))
                return;
        }

        float xSpeed = 0;

        if (isLeft) {
            xSpeed -= playerSpeed;
            flipX = width;
            flixW = -1;
        }
        if (isRight) {
            xSpeed += playerSpeed;
            flipX = 0;
            flixW = 1;
        }

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

    public void changeHealth(int value) {
        currentHealth += value;

        if (currentHealth <= 0) {
            currentHealth = 0;
            // gameOver();
        } else if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    private void loadAnimation() {

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        if (img == null) {
            log.error("img == null -> loadAnimation");
            return;
        }

        animations = new BufferedImage[7][8];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);

    }

    public void loadLvData(int[][] lvData) {
        this.lvData = lvData;
        if (!IsEntityOnFloor(hitbox, lvData)) {
            inAir = true;
        }
    }

    public void resetDirBoolean() {
        isLeft = false;
        isRight = false;
        isUp = false;
        isDown = false;
    }

    public void resetAll() {
        resetDirBoolean();
        inAir = false;
        isAttacking = false;
        isMoving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if (!IsEntityOnFloor(hitbox, lvData)) {
            inAir = true;
        }
    }
}

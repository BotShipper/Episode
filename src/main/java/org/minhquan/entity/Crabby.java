package org.minhquan.entity;

import org.minhquan.app.Game;

import static org.minhquan.util.Constant.EnemyConstants.*;

public class Crabby extends Enemy {

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
    }

    public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (enemyState) {
                case IDLE -> newState(RUNNING);
                case RUNNING -> {
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                    }
                    if (isPlayerCloseForAttack(player)) {
                        newState(ATTACK);
                    }

                    move(lvlData);
                }
            }
        }
    }
}

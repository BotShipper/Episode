package org.minhquan.entity;

import lombok.extern.slf4j.Slf4j;
import org.minhquan.gamestate.Playing;
import org.minhquan.util.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.minhquan.util.Constant.EnemyConstants.*;

@Slf4j
public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        crabbies = LoadSave.GetCrabs();
        if (crabbies == null) {
            log.error("crabbies == null -> addEnemies");
            return;
        }
        System.out.println("Size of crabs: " + crabbies.size());
    }

    public void update(int[][] lvlData) {
        for (Crabby c : crabbies) {
            c.update(lvlData);
        }
    }

    public void draw(Graphics g, int xLvOffset) {
        drawCrabs(g, xLvOffset);
    }

    private void drawCrabs(Graphics g, int xLvOffset) {
        for (Crabby c : crabbies) {
            g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()],
                    (int) c.getHitbox().x - xLvOffset - CRABBY_DRAW_OFFSET_X,
                    (int) c.getHitbox().y - CRABBY_DRAW_OFFSET_Y,
                    CRABBY_WIDTH, CRABBY_HEIGHT, null);
        }
    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPITE);
        if (temp == null) {
            log.error("temp == null -> loadEnemyImgs");
            return;
        }
        for (int j = 0; j < crabbyArr.length; j++) {
            for (int i = 0; i < crabbyArr[j].length; i++) {
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }
}

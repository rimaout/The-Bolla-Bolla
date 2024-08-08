package entities;

import Utillz.LoadSave;
import gameStates.Playing;
import static Utillz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] zenChanSprites;
    private ArrayList<ZenChan> zenChans = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;

        loadEnemiesSprites();
        addEnemies();
    }

    private void addEnemies() {
        zenChans = LoadSave.getZenChans();
        System.out.println("Number of crabs: " + zenChans.size());
    }

    public void update(int [][] lvlData, Player player) {
        for (ZenChan z : zenChans) {
            z.update(lvlData, player);
        }
    }

    public void draw(Graphics g) {
        drawZenChans(g);
    }

    private void drawZenChans(Graphics g) {
        for (ZenChan z : zenChans) {
            g.drawImage(zenChanSprites[z.getEnemyState()][z.getAniIndex()], (int) (z.getHitbox().x - ZEN_CHAN_DRAWOFFSET_X), (int) (z.getHitbox().y - ZEN_CHAN_DRAWOFFSET_Y), ENEMY_WIDTH, ENEMY_HEIGHT, null);
            z.drawHitbox(g);
        }
    }

    private void loadEnemiesSprites() {
        zenChanSprites = new BufferedImage[8][4];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.ZEN_CHAN_ENEMY_SPRITE);

        for (int j = 0; j < zenChanSprites.length; j++) {
            for (int i = 0; i < zenChanSprites[j].length; i++) {
                zenChanSprites[j][i] = temp.getSubimage(i * ENEMY_WIDTH_DEFAULT, j * ENEMY_HEIGHT_DEFAULT, ENEMY_WIDTH_DEFAULT, ENEMY_HEIGHT_DEFAULT);
            }
        }

    }
}

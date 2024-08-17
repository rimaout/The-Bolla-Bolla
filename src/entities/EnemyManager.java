package entities;

import levels.Level;
import utilz.LoadSave;
import gameStates.Playing;
import static utilz.Constants.EnemyConstants.*;

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
    }

    public void loadEnemies(Level level) {
        zenChans = level.getZenChans();
    }

    public void update(int [][] lvlData, Player player) {
        boolean allDead = true;

        for (ZenChan z : zenChans) {
            z.update(lvlData, player);
            allDead = false;
        }

        if(allDead)
            playing.setLevelCompleted(true);

        checkEnemyHit(player);
    }

    public void draw(Graphics g) {
        drawZenChans(g);
    }

    private void drawZenChans(Graphics g) {
        for (ZenChan z : zenChans)
            if(z.isActive())
                g.drawImage(zenChanSprites[z.getEnemyState()][z.getAnimationIndex()], (int) (z.getHitbox().x - ZEN_CHAN_DRAWOFFSET_X) + z.flipX(), (int) (z.getHitbox().y - ZEN_CHAN_DRAWOFFSET_Y), ENEMY_W * z.flipW(), ENEMY_H, null);
    }

    public void checkEnemyHit(Player player) {
        for (ZenChan z : zenChans)
            if (z.getHitbox().intersects(player.getHitbox()) && z.isActive())
                    player.death();
    }

    private void loadEnemiesSprites() {
        zenChanSprites = new BufferedImage[8][4];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.ZEN_CHAN_ENEMY_SPRITE);

        for (int j = 0; j < zenChanSprites.length; j++)
            for (int i = 0; i < zenChanSprites[j].length; i++)
                zenChanSprites[j][i] = temp.getSubimage(i * ENEMY_DEFAULT_W, j * ENEMY_DEFAULT_H, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);
    }

    public void resetAll() {
        for (ZenChan z : zenChans)
            z.resetEnemy();
    }
}

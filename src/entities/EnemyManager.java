package entities;

import levels.LevelManager;
import utilz.LoadSave;
import gameStates.Playing;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private static EnemyManager instance;

    private Playing playing;
    private Player player;
    private int[][] levelData;
    
    private BufferedImage[][] zenChanSprites;
    private ArrayList<Enemy> enemies;

    private EnemyManager(Playing playing, Player player) {
        this.playing = playing;
        this.player = player;
        loadEnemiesSprites();
        loadEnemies();
        loadLevelData();
    }

    public static EnemyManager getInstance(Playing playing, Player player) {
        if (instance == null) {
            instance = new EnemyManager(playing, player);
        }
        return instance;
    }

    public static EnemyManager getInstance() {
        return instance;
    }


    public void update() {
        boolean allDead = true;

        for (Enemy e : enemies) {
            if (e.isActive()) {
                e.update(player);
                checkEnemyHit(player, e);
            }
            if (e.isAlive())
                allDead = false;
        }

        if(allDead)
            playing.setLevelCompleted(true);
    }

    public void draw(Graphics g) {

        for (Enemy e : enemies)
            if(e.isActive())
                g.drawImage(getEnemySprite(e.getEnemyType())[e.getEnemyState()][e.getAnimationIndex()], (int) (e.getHitbox().x - ZEN_CHAN_OFFSET_X) + e.flipX(), (int) (e.getHitbox().y - ZEN_CHAN_OFFSET_Y), ENEMY_W * e.flipW(), ENEMY_H, null);
    }

    public void checkEnemyHit(Player player, Enemy enemy) {
            if (enemy.getHitbox().intersects(player.getHitbox()) && enemy.isActive())
                    player.death();
    }

    public void loadLevelData() {
        levelData = LevelManager.getInstance().getCurrentLevel().getLevelData();
    }

    public void loadEnemies() {
        enemies = LevelManager.getInstance().getCurrentLevel().getEnemies();
    }

    private void loadEnemiesSprites() {
        zenChanSprites = new BufferedImage[8][4];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.ZEN_CHAN_ENEMY_SPRITE);

        for (int j = 0; j < zenChanSprites.length; j++)
            for (int i = 0; i < zenChanSprites[j].length; i++)
                zenChanSprites[j][i] = temp.getSubimage(i * ENEMY_DEFAULT_W, j * ENEMY_DEFAULT_H, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);
    }

    public void resetAll() {
        for (Enemy e : enemies)
            e.resetEnemy();
    }

    public BufferedImage[][] getEnemySprite(EnemyType enemyType) {
        switch (enemyType) {
            case ZEN_CHAN:
                return zenChanSprites;
            default:
                return null;
        }
    }
}

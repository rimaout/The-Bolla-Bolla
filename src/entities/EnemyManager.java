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
    private BufferedImage[][] maitaSprites;
    private ArrayList<Enemy> enemies;

    private boolean allEnemiesReachedSpawn = false;

    private boolean allEnemiesDead = false;
    private int allEnemiesDeadChronometer = 0;

    private boolean enemiesFreeze = false;
    private int freezeTimer;

    private boolean playerInvincibleMode = false;
    private int invincibleTimer;

    private boolean firstUpdate = true;
    private long lastTimerUpdate;

    private EnemyManager(Playing playing, Player player) {
        this.playing = playing;
        this.player = player;
        loadSprites();
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
        timersUpdate();
        enemiesUpdate();
    }

    private void timersUpdate() {
        if (firstUpdate) {
            firstUpdate = false;
            lastTimerUpdate = System.currentTimeMillis();
        }

        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();

        freezeTimer -= timeDelta;
        invincibleTimer -= timeDelta;

        if (freezeTimer <= 0)
            enemiesFreeze = false;

        if (invincibleTimer <= 0)
            playerInvincibleMode = false;

        if (allEnemiesDead)
            allEnemiesDeadChronometer += timeDelta;
    }

    private void enemiesUpdate() {
        int deadCounter = 0;
        int reachedSpawnCounter = 0;

        for (Enemy e : enemies) {
            if (e.isActive()) {

                if (!enemiesFreeze)
                    e.update(player);

                checkEnemyHit(player, e);
            }

            if (e.getReachedSpawn())
                reachedSpawnCounter++;

            if (!e.isAlive())
                deadCounter++;
        }

        switch (enemies.size() - deadCounter) {
            case 0 -> allEnemiesDead = true; // All enemies are dead
            case 1 -> setAllHungry();        // Only one enemy left: set to hungry mode
        }

        if (reachedSpawnCounter == enemies.size())
            allEnemiesReachedSpawn = true;
    }

    public void draw(Graphics g) {

        for (Enemy e : enemies)
            if(e.isActive())
                g.drawImage(getEnemySprite(e.getEnemyType())[e.getEnemyState()][e.getAnimationIndex()], (int) (e.getHitbox().x - ENEMY_HITBOX_OFFSET_X) + e.flipX(), (int) (e.getHitbox().y - ENEMY_HITBOX_OFFSET_Y), ENEMY_W * e.flipW(), ENEMY_H, null);
    }

    public void checkEnemyHit(Player player, Enemy enemy) {
            if (enemy.getHitbox().intersects(player.getHitbox()) && enemy.isActive())

                if (playerInvincibleMode)
                    enemy.death(player);
                else
                    player.death();
    }

    private void setAllHungry() {
        for (Enemy e : enemies)
            if (e.isActive())
                e.setEnemyState(HUNGRY_STATE);
    }

    public void loadLevelData() {
        levelData = LevelManager.getInstance().getCurrentLevel().getLevelData();
    }

    public void loadEnemies() {
        enemies = LevelManager.getInstance().getCurrentLevel().getEnemies();
    }

    private void loadSprites() {
        zenChanSprites = new BufferedImage[9][4];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.ZEN_CHAN_ENEMY_SPRITE);

        for (int j = 0; j < zenChanSprites.length; j++)
            for (int i = 0; i < zenChanSprites[j].length; i++)
                zenChanSprites[j][i] = temp.getSubimage(i * ENEMY_DEFAULT_W, j * ENEMY_DEFAULT_H, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);

        maitaSprites = new BufferedImage[9][4];
        temp = LoadSave.GetSprite(LoadSave.MAITA_ENEMY_SPRITE);
        for (int j = 0; j < maitaSprites.length; j++)
            for (int i = 0; i < maitaSprites[j].length; i++)
                maitaSprites[j][i] = temp.getSubimage(i * ENEMY_DEFAULT_W, j * ENEMY_DEFAULT_H, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);
    }

    public void resetAll() {
        for (Enemy e : enemies)
            e.resetEnemy();

        allEnemiesReachedSpawn = false;
        allEnemiesDead = false;
        allEnemiesDeadChronometer = 0;

        freezeTimer = 0;
        enemiesFreeze = false;
        invincibleTimer = 0;
        playerInvincibleMode = false;

        firstUpdate = true;
    }

    public BufferedImage[][] getEnemySprite(EnemyType enemyType) {
        return switch (enemyType) {
            case ZEN_CHAN -> zenChanSprites;
            case MAITA -> maitaSprites;
        };
    }

    public int getEnemyCount() {
        return enemies.size();
    }

    public boolean getAllEnemiesReachedSpawn() {
        return allEnemiesReachedSpawn;
    }

    public void setChacknHeartfreeze(int timer) {
        freezeTimer = timer;
        invincibleTimer = timer;

        enemiesFreeze = true;
        playerInvincibleMode = true;
    }

    public int getAllEnemiesDeadChronometer() {
        return allEnemiesDeadChronometer;
    }
}

package entities;

import model.entities.PlayerModel;
import model.levels.LevelManagerModel;
import model.utilz.LoadSave;
import model.utilz.PlayingTimer;

import static model.utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private static EnemyManager instance;

    private final PlayingTimer timer = PlayingTimer.getInstance();
    private final PlayerModel playerModel;

    private BufferedImage[][] zenChanSprites;
    private BufferedImage[][] maitaSprites;
    private BufferedImage[][] skelMonstaSprites;

    private ArrayList<Enemy> enemies;

    private boolean allEnemiesReachedSpawn = false;

    private boolean allEnemiesDead = false;
    private int allEnemiesDeadChronometer = 0;

    private boolean enemiesFreeze = false;
    private int freezeTimer;

    private boolean playerInvincibleMode = false;
    private int invincibleTimer;

    private EnemyManager(PlayerModel playerModel) {
        this.playerModel = playerModel;
        loadSprites();
    }

    public static EnemyManager getInstance(PlayerModel playerModel) {
        if (instance == null) {
            instance = new EnemyManager(playerModel);
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

        freezeTimer -= (int) timer.getTimeDelta();
        invincibleTimer -= (int) timer.getTimeDelta();

        if (freezeTimer <= 0)
            enemiesFreeze = false;

        if (invincibleTimer <= 0)
            playerInvincibleMode = false;

        if (allEnemiesDead)
            allEnemiesDeadChronometer += (int) timer.getTimeDelta();
    }

    private void enemiesUpdate() {
        int deadCounter = 0;
        int reachedSpawnCounter = 0;

        for (Enemy e : enemies) {
            if (e.isActive()) {

                if (!enemiesFreeze)
                    e.update(playerModel);

                checkEnemyHit(playerModel, e);
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

    public void checkEnemyHit(PlayerModel playerModel, Enemy enemy) {
        if (!playerModel.isActive())
            return;

        if (enemy.getHitbox().intersects(playerModel.getHitbox()) && enemy.isActive()) {
            if (playerInvincibleMode)
                enemy.instantKill(playerModel);
            else {
                playerModel.death();
                setAllNormal();
            }
        }
    }

    public void setAllHungry() {
        for (Enemy e : enemies)
            if (e.isActive())
                e.setEnemyState(HUNGRY_STATE);
    }

    private void setAllNormal() {
        for (Enemy e : enemies)
            if (e.isActive())
                e.setEnemyState(NORMAL_STATE);

        HurryUpManager.getInstance().restart();
    }

    public void loadEnemies() {
        enemies = LevelManagerModel.getInstance().getCurrentLevel().getEnemies();
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

        skelMonstaSprites = new BufferedImage[3][2];
        temp = LoadSave.GetSprite(LoadSave.SKEL_MONSTA_ENEMY_SPRITE);
        for (int j = 0; j < skelMonstaSprites.length; j++)
            for (int i = 0; i < skelMonstaSprites[j].length; i++)
                skelMonstaSprites[j][i] = temp.getSubimage(i * ENEMY_DEFAULT_W, j * ENEMY_DEFAULT_H, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);
    }

    public void newLevelReset() {
        enemies.clear();

        allEnemiesReachedSpawn = false;
        allEnemiesDead = false;
        allEnemiesDeadChronometer = 0;
        enemiesFreeze = false;
        freezeTimer = 0;
        playerInvincibleMode = false;
        invincibleTimer = 0;
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public BufferedImage[][] getEnemySprite(EnemyType enemyType) {
        return switch (enemyType) {
            case ZEN_CHAN -> zenChanSprites;
            case MAITA -> maitaSprites;
            case SKEL_MONSTA -> skelMonstaSprites;
        };
    }

    public int getActiveEnemiesCount() {
        int count = 0;
        for (Enemy e : enemies)
            if (e.isActive())
                count++;
        return count;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public boolean didAllEnemiesReachedSpawn() {
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

    public boolean areAllEnemiesDead() {
        return allEnemiesDead;
    }
}

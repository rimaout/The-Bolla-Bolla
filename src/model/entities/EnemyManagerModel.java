package model.entities;

import entities.HurryUpManager;
import model.levels.LevelManagerModel;
import model.utilz.PlayingTimer;

import static model.utilz.Constants.EnemyConstants.*;

import java.util.ArrayList;

public class EnemyManagerModel {
    private static EnemyManagerModel instance;

    private final PlayingTimer timer = PlayingTimer.getInstance();
    private final PlayerModel playerModel;

    private ArrayList<EnemyModel> enemies;

    private boolean allEnemiesReachedSpawn = false;

    private boolean allEnemiesDead = false;
    private int allEnemiesDeadChronometer = 0;

    private boolean enemiesFreeze = false;
    private int freezeTimer;

    private boolean playerInvincibleMode = false;
    private int invincibleTimer;

    private EnemyManagerModel(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    public static EnemyManagerModel getInstance(PlayerModel playerModel) {
        if (instance == null) {
            instance = new EnemyManagerModel(playerModel);
        }
        return instance;
    }

    public static EnemyManagerModel getInstance() {
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

        for (EnemyModel e : enemies) {
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

    public void checkEnemyHit(PlayerModel playerModel, EnemyModel enemyModel) {
        if (!playerModel.isActive())
            return;

        if (enemyModel.getHitbox().intersects(playerModel.getHitbox()) && enemyModel.isActive()) {
            if (playerInvincibleMode)
                enemyModel.instantKill(playerModel);
            else {
                playerModel.death();
                setAllNormal();
            }
        }
    }

    public void setAllHungry() {
        for (EnemyModel e : enemies)
            if (e.isActive())
                e.setEnemyState(HUNGRY_STATE);
    }

    private void setAllNormal() {
        for (EnemyModel e : enemies)
            if (e.isActive())
                e.setEnemyState(NORMAL_STATE);

        HurryUpManager.getInstance().restart();
    }

    public void loadEnemies() {
        enemies = LevelManagerModel.getInstance().getCurrentLevel().getEnemies();
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

    public int getActiveEnemiesCount() {
        int count = 0;
        for (EnemyModel e : enemies)
            if (e.isActive())
                count++;
        return count;
    }

    public ArrayList<EnemyModel> getEnemiesModels() {
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

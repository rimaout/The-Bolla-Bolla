package com.rima.model.entities;

import com.rima.model.levels.LevelManagerModel;
import com.rima.model.utilz.PlayingTimer;

import static com.rima.model.utilz.Constants.EnemyConstants.*;

import java.util.ArrayList;

/**
 * Manages the enemies in the game, including their states, updates, and interactions with the player.
 */
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

    /**
     * Constructs the EnemyManagerModel (private due to Singleton design patter implementation).
     */
    private EnemyManagerModel() {
        this.playerModel = PlayerModel.getInstance();
    }

    /**
     * Returns the singleton instance of the EnemyManagerModel.
     *
     * @return the singleton instance
     */
    public static EnemyManagerModel getInstance() {
        if (instance == null) {
            instance = new EnemyManagerModel();
        }
        return instance;
    }

    /**
     * Updates the state of all enemies and relevant timers.
     */
    public void update() {
        timersUpdate();
        enemiesUpdate();
    }

    /**
     * Updates the timers for freezing and invincibility effects.
     */
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

    /**
     * Updates the state of all enemies, checking for collisions and updating their states.
     */
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

    /**
     * Checks if the player has hit an enemy and handles the interaction.
     *
     * @param playerModel the player model
     * @param enemyModel the enemy model
     */
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

    /**
     * Sets all active enemies to the hungry state.
     */
    public void setAllHungry() {
        for (EnemyModel e : enemies)
            if (e.isActive())
                e.setEnemyState(HUNGRY_STATE);
    }

    /**
     * Sets all active enemies to the normal state and restarts the HurryUpManager ({@link HurryUpManagerModel}).
     */
    private void setAllNormal() {
        for (EnemyModel e : enemies)
            if (e.isActive())
                e.setEnemyState(NORMAL_STATE);

        HurryUpManagerModel.getInstance().restart();
    }

    /**
     * Loads the enemies for the current level.
     *
     * <p>This method gets the list of enemies from the current level by the LevelManagerModel and saves them in the enemies list.
     */
    public void loadEnemies() {
        enemies = LevelManagerModel.getInstance().getCurrentLevel().getEnemies();
    }


    /**
     * Resets the state of the enemies for a new level.
     *
     * <p>This method calls the newLevelReset method to clear the list of enemies and reset various flags and timers related to enemy states,
     * such as whether all enemies have reached their spawn points, whether all enemies are dead, the chronometer for the time all enemies have been dead,
     * the freeze state of enemies, and the invincibility mode of the player.
     */
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

    /**
     * Resets the state of the class to be ready for a new play.
     *
     * <p>This method calls the newLevelReset method to clear the list of enemies and reset various flags and timers related to enemy states,
     * such as whether all enemies have reached their spawn points, whether all enemies are dead, the chronometer for the time all enemies have been dead,
     * the freeze state of enemies, and the invincibility mode of the player.
     */
    public void newPlayReset() {
        newLevelReset();
    }

    /**
     * Returns the count of active enemies.
     *
     * @return the count of active enemies
     */
    public int getActiveEnemiesCount() {
        int count = 0;
        for (EnemyModel e : enemies)
            if (e.isActive())
                count++;
        return count;
    }

    /**
     * Returns the list of enemy models.
     *
     * @return the list of enemy models
     */
    public ArrayList<EnemyModel> getEnemiesModels() {
        return enemies;
    }


    /**
     * returns if all enemies have reached their spawn points.
     *
     * @return true if all enemies have reached their spawn points, false otherwise
     */
    public boolean didAllEnemiesReachedSpawn() {
        return allEnemiesReachedSpawn;
    }

    /**
     * This method sets the freeze and invincibility timers to the specified value, enabling the freeze state for enemies
     * and the invincibility mode for the player.
     *
     *  <p> This method is used when the player picks up the Chack'n Heart power-up
     *
     * @param timer the timer value to set for both freeze and invincibility effects
     */
    public void setChacknHeartfreeze(int timer) {
        freezeTimer = timer;
        invincibleTimer = timer;

        enemiesFreeze = true;
        playerInvincibleMode = true;
    }

    /**
     * Returns the chronometer value for the time all enemies have been dead.
     *
     * @return the chronometer value
     */
    public int getAllEnemiesDeadChronometer() {
        return allEnemiesDeadChronometer;
    }

    /**
     * returns if all enemies are dead.
     *
     * @return true if all enemies are dead, false otherwise
     */
    public boolean areAllEnemiesDead() {
        return allEnemiesDead;
    }
}
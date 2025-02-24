package com.rima.model.projectiles;

import java.util.ArrayList;

import com.rima.model.entities.EnemyModel;
import com.rima.model.entities.PlayerModel;
import com.rima.model.entities.EnemyManagerModel;

/**
 * Game logic for managing all projectiles.
 */
public class ProjectileManagerModel {
    private static ProjectileManagerModel instance;
    private final PlayerModel playerModel;

    private final ArrayList<ProjectileModel> projectileModels = new ArrayList<>();

    // PowerUp Multipliers
    private float playerProjectileSpeedMultiplier = 1;
    private float playerProjectileDistanceMultiplier = 1;
    private float playerProjectileActiveTimeMultiplier = 1;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private ProjectileManagerModel() {
        this.playerModel = PlayerModel.getInstance();
    }

    /**
     * Returns the singleton instance of the ProjectileManagerModel.
     *
     * @return the singleton instance
     */
    public static ProjectileManagerModel getInstance() {
        if (instance == null) {
            instance = new ProjectileManagerModel();
        }
        return instance;
    }

    /**
     * Updates all active projectiles and checks for collisions.
     */
    public void update() {

        for (ProjectileModel p : projectileModels) {
            if (!p.isActive())
                continue;

            p.update();
            updateEntityCollisions(p);
        }
    }

    /**
     * Checks for collisions between the given projectile and other entities.
     *
     * @param p the projectile to check for collisions
     */
    private void updateEntityCollisions(ProjectileModel p) {
        p.checkPlayerHit(playerModel);

        for (EnemyModel enemyModel : EnemyManagerModel.getInstance().getEnemiesModels())
            p.checkEnemyHit(enemyModel, playerModel);
    }

    /**
     * Adds a new projectile to the manager.
     *
     * @param projectileModel the projectile to add
     */
    public void addProjectile(ProjectileModel projectileModel) {
        projectileModels.add(projectileModel);
    }

    /**
     * Resets the projectiles for a new level.
     */
    public void newLevelReset() {
        projectileModels.clear();
    }

    /**
     * Resets the projectiles and multipliers for a new play session.
     */
    public void newPlayReset() {
        projectileModels.clear();
        playerProjectileSpeedMultiplier = 1;
        playerProjectileDistanceMultiplier = 1;
        playerProjectileActiveTimeMultiplier = 1;
    }

    /**
     * Returns the current speed multiplier for player projectiles.
     *
     * @return the speed multiplier
     */
    public float getPlayerProjectileSpeedMultiplier() {
        return playerProjectileSpeedMultiplier;
    }

    /**
     * Sets the speed multiplier for player projectiles.
     *
     * @param playerProjectileSpeedMultiplier the new speed multiplier
     */
    public void setPlayerProjectileSpeedMultiplier(float playerProjectileSpeedMultiplier) {
        this.playerProjectileSpeedMultiplier = playerProjectileSpeedMultiplier;
    }

    /**
     * Returns the current distance multiplier for player projectiles.
     *
     * @return the distance multiplier
     */
    public float getPlayerProjectileDistanceMultiplier() {
        return playerProjectileDistanceMultiplier;
    }

    /**
     * Sets the distance multiplier for player projectiles.
     *
     * @param playerProjectileDistanceMultiplier the new distance multiplier
     */
    public void setPlayerProjectileDistanceMultiplier(float playerProjectileDistanceMultiplier) {
        this.playerProjectileDistanceMultiplier = playerProjectileDistanceMultiplier;
    }

    /**
     * Returns the list of all projectiles managed by this manager.
     *
     * @return the list of projectiles
     */
    public ArrayList<ProjectileModel> getProjectileModels() {
        return projectileModels;
    }

    /**
     * Returns the current active time multiplier for player projectiles.
     *
     * @return the active time multiplier
     */
    public float getPlayerProjectileActiveMultiplier(){
        return playerProjectileActiveTimeMultiplier;
    }

    /**
     * Sets the active time multiplier for player projectiles.
     *
     * @param playerProjectileActiveTimeMultiplier the new active time multiplier
     */
    public void setPlayerProjectileActiveMultiplier(float playerProjectileActiveTimeMultiplier) {
        this.playerProjectileActiveTimeMultiplier = playerProjectileActiveTimeMultiplier;
    }
}
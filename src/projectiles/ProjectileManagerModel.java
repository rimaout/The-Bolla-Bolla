package projectiles;

import java.util.ArrayList;

import entities.Enemy;
import model.entities.PlayerModel;
import entities.EnemyManager;

public class ProjectileManagerModel {
    private static ProjectileManagerModel instance;
    private final PlayerModel playerModel;

    private final ArrayList<ProjectileModel> projectileModels = new ArrayList<>();

    // PowerUp Multipliers
    private float playerProjectileSpeedMultiplier = 1;
    private float playerProjectileDistanceMultiplier = 1;

    private ProjectileManagerModel(PlayerModel playerModel) {

        this.playerModel = playerModel;
    }

    public static ProjectileManagerModel getInstance(PlayerModel playerModel) {
        if (instance == null) {
            instance = new ProjectileManagerModel(playerModel);
        }
        return instance;
    }

    public static ProjectileManagerModel getInstance() {
        return instance;
    }

    public void update() {

        for (ProjectileModel p : projectileModels) {
            if (!p.isActive())
                continue;

            p.update();
            updateEntityCollisions(p);
        }
    }

    private void updateEntityCollisions(ProjectileModel p) {
        p.checkPlayerHit(playerModel);

        for (Enemy enemy : EnemyManager.getInstance().getEnemies())
            p.checkEnemyHit(enemy, playerModel);
    }

    public void addProjectile(ProjectileModel projectileModel) {
        projectileModels.add(projectileModel);
    }

    public void newLevelReset() {
        projectileModels.clear();
    }

    public void newPlayReset() {
        projectileModels.clear();
        playerProjectileSpeedMultiplier = 1;
        playerProjectileDistanceMultiplier = 1;
    }

    public float getPlayerProjectileSpeedMultiplier() {
        return playerProjectileSpeedMultiplier;
    }

    public void setPlayerProjectileSpeedMultiplier(float playerProjectileSpeedMultiplier) {
        this.playerProjectileSpeedMultiplier = playerProjectileSpeedMultiplier;
    }

    public float getPlayerProjectileDistanceMultiplier() {
        return playerProjectileDistanceMultiplier;
    }

    public void setPlayerProjectileDistanceMultiplier(float playerProjectileDistanceMultiplier) {
        this.playerProjectileDistanceMultiplier = playerProjectileDistanceMultiplier;
    }

    public ArrayList<ProjectileModel> getProjectileModels() {
        return projectileModels;
    }
}

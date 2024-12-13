package model.projectiles;

import java.util.ArrayList;

import model.entities.EnemyModel;
import model.entities.PlayerModel;
import model.entities.EnemyManagerModel;

public class ProjectileManagerModel {
    private static ProjectileManagerModel instance;
    private final PlayerModel playerModel;

    private final ArrayList<ProjectileModel> projectileModels = new ArrayList<>();

    // PowerUp Multipliers
    private float playerProjectileSpeedMultiplier = 1;
    private float playerProjectileDistanceMultiplier = 1;
    private float playerProjectileActiveTimeMultiplier = 1;

    private ProjectileManagerModel() {
        this.playerModel = PlayerModel.getInstance();
    }

    public static ProjectileManagerModel getInstance() {
        if (instance == null) {
            instance = new ProjectileManagerModel();
        }
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

        for (EnemyModel enemyModel : EnemyManagerModel.getInstance().getEnemiesModels())
            p.checkEnemyHit(enemyModel, playerModel);
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
        playerProjectileActiveTimeMultiplier = 1;
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

    public float getPlayerProjectileActiveMultiplier(){
        return playerProjectileActiveTimeMultiplier;
    }

    public void setPlayerProjectileActiveMultiplier(float playerProjectileActiveTimeMultiplier) {
        this.playerProjectileActiveTimeMultiplier = playerProjectileActiveTimeMultiplier;
    }
}
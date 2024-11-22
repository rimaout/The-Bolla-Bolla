package model.projectiles;


import model.entities.EnemyModel;
import model.entities.PlayerModel;
import model.utilz.Constants;
import model.utilz.Constants.Direction;
import model.utilz.Constants.Projectiles.ProjectileType;

import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.Projectiles.*;
import static model.utilz.HelpMethods.IsPerimeterWallTile;

public class LightingProjectileModel extends ProjectileModel {

    public LightingProjectileModel(float x, float y, ProjectileType type) {
        super(x, y, CalculateLightingDirection(x), type);
    }

    @Override
    protected void updatePos() {

        float xSpeed;
        if (direction == LEFT)
            xSpeed = -LIGHTNING_SPEED;
        else
            xSpeed = LIGHTNING_SPEED;

        if (!IsPerimeterWallTile(hitbox.x))
            hitbox.x += xSpeed;
        else
            active = false;
    }

    @Override
    protected void checkEnemyHit(EnemyModel enemyModel, PlayerModel playerModel) {
        //Parameters: enemy  = enemy that is being checked for collision with projectile
        //            player = player to add score to if the enemy is killed

        if (!enemyModel.isActive() || enemyModel.isImmune())
            return;

        if (hitbox.intersects(enemyModel.getHitbox())) {
            enemyModel.instantKill(playerModel);
            active = false;
        }
    }

    @Override
    protected void checkPlayerHit(PlayerModel playerModel) {
        // not used, lighting can't hit players
    }

    private static Direction CalculateLightingDirection(float x) {
        // if lighting is on the left side of the screen, go right
        if (x < Constants.GAME_WIDTH / 2f)  return Direction.RIGHT;

        // if lighting is on the right side of the screen, go left
        else return Direction.LEFT;
    }
}

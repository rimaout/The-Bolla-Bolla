package model.projectiles;

import model.utilz.Constants;
import model.utilz.Constants.Direction;
import model.utilz.Constants.Projectiles.ProjectileType;
import model.entities.EnemyModel;
import model.entities.PlayerModel;

import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.Projectiles.*;
import static model.utilz.HelpMethods.IsPerimeterWallTile;

/**
 * Represents a logic of a lighting projectile in the game.
 */
public class LightingProjectileModel extends ProjectileModel {

    /**
     * Constructs a LightingProjectileModel with the specified position and type.
     *
     * @param x the x-coordinate of the projectile
     * @param y the y-coordinate of the projectile
     * @param type the type of the projectile
     */
    public LightingProjectileModel(float x, float y, ProjectileType type) {
        super(x, y, CalculateLightingDirection(x), type);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Moves the projectile left or right based on its direction and deactivates it if it hits a perimeter wall tile.
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * If the projectile hits an active enemy, it instantly kills the enemy and deactivates the projectile.
     *
     * @param enemyModel the enemy model to check against
     * @param playerModel the player model to add score to if the enemy is killed
     */
    @Override
    protected void checkEnemyHit(EnemyModel enemyModel, PlayerModel playerModel) {
        if (!enemyModel.isActive() || enemyModel.isImmune())
            return;

        if (hitbox.intersects(enemyModel.getHitbox())) {
            enemyModel.instantKill(playerModel);
            active = false;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method is not used as lighting projectiles cannot hit players.
     *
     * @param playerModel the player model to check against
     */
    @Override
    protected void checkPlayerHit(PlayerModel playerModel) {
        // not used, lighting can't hit players
    }

    /**
     * Calculates the direction of the lighting projectile based on its initial x-coordinate.
     * <p>
     * If the projectile is on the left side of the screen, it goes right. If it is on the right side, it goes left.
     *
     * @param x the x-coordinate of the projectile
     * @return the direction of the projectile
     */
    private static Direction CalculateLightingDirection(float x) {
        if (x < Constants.GAME_WIDTH / 2f)
            return Direction.RIGHT;
        else
            return Direction.LEFT;
    }
}
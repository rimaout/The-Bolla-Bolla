package model.projectiles;

import model.entities.EnemyModel;
import model.entities.PlayerModel;
import model.levels.LevelManagerModel;
import model.utilz.Constants.Direction;

import static model.utilz.Constants.Direction.*;
import static model.utilz.Constants.Projectiles.*;
import static model.utilz.HelpMethods.CanMoveHere;
import static model.utilz.Constants.Projectiles.ProjectileState.IMPACT;
import static model.utilz.Constants.Projectiles.ProjectileState.MOVING;
import static model.utilz.Constants.Projectiles.ProjectileType.MAITA_FIREBALL;

/**
 * Represents the logic of a Maita fire projectile in the game.
 */
public class MaitaFireProjectileModel extends ProjectileModel {

    /**
     * Constructs a MaitaFireProjectileModel with the specified position and direction.
     *
     * @param x the x-coordinate of the projectile
     * @param y the y-coordinate of the projectile
     * @param direction the direction of the projectile
     */
    public MaitaFireProjectileModel(float x, float y, Direction direction) {
        super(x, y, direction, MAITA_FIREBALL);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Moves the projectile left or right based on its direction and changes its state to IMPACT if collide with an obstacle.
     */
    @Override
    protected void updatePos() {

        if (state == MOVING) {

            float xSpeed;
            if (direction == LEFT)
                xSpeed = -MAITA_FIREBALL_SPEED;
            else
                xSpeed = MAITA_FIREBALL_SPEED;

            if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, LevelManagerModel.getInstance().getLevelTileData()))
                hitbox.x += xSpeed;
            else {
                state = IMPACT;
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method is not used as Maita fire projectiles cannot hit enemies.
     *
     * @param enemyModel the enemy model to check against
     * @param playerModel the player model to check against
     */
    @Override
    public void checkEnemyHit(EnemyModel enemyModel, PlayerModel playerModel) {
        // not used, MaitaFireProjectiles can't hit enemies
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the projectile hits an active and non-immune player, it kills the player and changes its state to IMPACT.
     *
     * @param playerModel the player model to check against
     */
    @Override
    public void checkPlayerHit(PlayerModel playerModel) {
        // Parameters: player = player that is being checked for collision with projectile

        if (!playerModel.isActive() || playerModel.isImmune())
            return;

        if (hitbox.intersects(playerModel.getHitbox()) && !playerModel.isRespawning()) {
            playerModel.death();
            state = IMPACT;
        }
    }
}
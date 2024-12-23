package model.projectiles;

import model.entities.EnemyModel;
import model.entities.PlayerModel;
import model.levels.LevelManagerModel;
import model.utilz.Constants.Direction;
import model.bubbles.playerBubbles.PlayerBubblesManagerModel;

import static model.utilz.Constants.Projectiles.*;
import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.HelpMethods.CanMoveHere;
import static model.utilz.Constants.Projectiles.ProjectileType.PLAYER_BUBBLE;

/**
 * Represents the logic of a player bubble projectile in the game.
 */
public class PlayerBubbleProjectileModel extends ProjectileModel {
    private float activeTimer = 290 * ProjectileManagerModel.getInstance().getPlayerProjectileActiveMultiplier();    // time until the projectile transforms in a "floating" bubble

    /**
     * Constructs a PlayerBubbleProjectileModel with the specified position and direction.
     *
     * @param x the x-coordinate of the projectile
     * @param y the y-coordinate of the projectile
     * @param direction the direction of the projectile
     */
    public PlayerBubbleProjectileModel(float x, float y, Direction direction) {
        super(x, y, direction, PLAYER_BUBBLE);
        this.direction = direction;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Moves the projectile left or right based on its direction.
     */
    @Override
    protected void updatePos() {

        float projectileSpeed = PLAYER_BUBBLE_SPEED * ProjectileManagerModel.getInstance().getPlayerProjectileSpeedMultiplier();

        float xSpeed;
        if (direction == LEFT)
            xSpeed = -projectileSpeed;
        else
            xSpeed = projectileSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, LevelManagerModel.getInstance().getLevelTileData()))
            hitbox.x += xSpeed;
    }

    /**
     * Updates the active timer and transforms the projectile into a floating bubble if the timer reaches zero.
     */
    @Override
    protected void updateTimer() {
        activeTimer -= timer.getTimeDelta();

        if (activeTimer <= 0) {
            active = false;
            PlayerBubblesManagerModel.getInstance().createEmptyBubble(hitbox.x, hitbox.y, direction);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the projectile hits an active and enemy, it captures the enemy in a bubble and deactivates the projectile.
     *
     * @param enemyModel the enemy model to check against
     * @param playerModel the player model to add score to if the enemy is killed (not used in this case)
     */
    @Override
    protected void checkEnemyHit(EnemyModel enemyModel, PlayerModel playerModel) {
        if (!enemyModel.isActive() || enemyModel.isImmune())
            return;

        if (hitbox.intersects(enemyModel.getHitbox())) {
            if (hitbox.intersects(enemyModel.getHitbox())) {
                enemyModel.bubbleCapture(direction);
                active = false;
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method is not used as player bubbles cannot hit players.
     *
     * @param playerModel the player model to check against
     */
    @Override
    protected void checkPlayerHit(PlayerModel playerModel) {
        // not used, playerBubbles can't hit players
    }
}
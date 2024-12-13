package model.projectiles;

import model.entities.EnemyModel;
import model.entities.PlayerModel;
import model.utilz.Constants.Direction;
import model.bubbles.playerBubbles.PlayerBubblesManagerModel;


import static model.utilz.Constants.Projectiles.*;
import static model.utilz.Constants.Direction.LEFT;
import static model.entities.HelpMethods.CanMoveHere;
import static model.utilz.Constants.Projectiles.ProjectileType.PLAYER_BUBBLE;

public class PlayerBubbleProjectileModel extends ProjectileModel {
    private float activeTimer = 290;    // time until the projectile transforms in a "floating" bubble

    public PlayerBubbleProjectileModel(float x, float y, Direction direction) {
        super(x, y, direction, PLAYER_BUBBLE);
        this.direction = direction;
    }

    @Override
    protected void updatePos() {

        float projectileSpeed = PLAYER_BUBBLE_SPEED * ProjectileManagerModel.getInstance().getPlayerProjectileSpeedMultiplier();

        float xSpeed;
        if (direction == LEFT)
            xSpeed = -projectileSpeed;
        else
            xSpeed = projectileSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelManagerModel.getLevelData()))
            hitbox.x += xSpeed;
    }

    @Override
    protected void updateTimer() {
        activeTimer -= timer.getTimeDelta();

        if (activeTimer <= 0) {
            active = false;
            PlayerBubblesManagerModel.getInstance().createEmptyBubble(hitbox.x, hitbox.y, direction);
        }
    }

    @Override
    protected void checkEnemyHit(EnemyModel enemyModel, PlayerModel playerModel) {
        // Parameters: enemy  = enemy that is being checked for collision with projectile
        //             player = player to add score to if the enemy is killed (not used in this case)

        if (!enemyModel.isActive() || enemyModel.isImmune())
            return;

        if (hitbox.intersects(enemyModel.getHitbox())) {
            if (hitbox.intersects(enemyModel.getHitbox())) {
                enemyModel.bubbleCapture(direction);
                active = false;
            }
        }
    }

    @Override
    protected void checkPlayerHit(PlayerModel playerModel) {
        // not used, playerBubbles can't hit players
    }
}
package model.projectiles;

import entities.Enemy;
import model.entities.PlayerModel;
import model.utilz.Constants.Direction;

import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.Projectiles.*;
import static model.utilz.HelpMethods.CanMoveHere;
import static model.utilz.Constants.Projectiles.ProjectileType.PLAYER_BUBBLE;

public class PlayerBubbleProjectileModel extends ProjectileModel {

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
    protected void checkEnemyHit(Enemy enemy, PlayerModel playerModel) {
        // Parameters: enemy  = enemy that is being checked for collision with projectile
        //             player = player to add score to if the enemy is killed (not used in this case)

        if (!enemy.isActive() || enemy.isImmune())
            return;

        if (hitbox.intersects(enemy.getHitbox())) {
            if (hitbox.intersects(enemy.getHitbox())) {
                enemy.bubbleCapture(direction);
                active = false;
            }
        }
    }

    @Override
    protected void checkPlayerHit(PlayerModel playerModel) {
        // not used, playerBubbles can't hit players
    }
}
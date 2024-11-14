package model.projectiles;

import entities.Enemy;
import model.entities.PlayerModel;
import model.utilz.Constants.Direction;

import static model.utilz.Constants.Direction.*;
import static model.utilz.HelpMethods.CanMoveHere;
import static model.utilz.Constants.Projectiles.*;
import static model.utilz.Constants.Projectiles.ProjectileState.IMPACT;
import static model.utilz.Constants.Projectiles.ProjectileState.MOVING;
import static model.utilz.Constants.Projectiles.ProjectileType.MAITA_FIREBALL;

public class MaitaFireProjectileModel extends ProjectileModel {

    public MaitaFireProjectileModel(float x, float y, Direction direction) {
        super(x, y, direction, MAITA_FIREBALL);
    }

    @Override
    protected void updatePos() {

        if (state == MOVING) {

            float xSpeed;
            if (direction == LEFT)
                xSpeed = -MAITA_FIREBALL_SPEED;
            else
                xSpeed = MAITA_FIREBALL_SPEED;

            if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelManagerModel.getLevelData()))
                hitbox.x += xSpeed;
            else {
                state = IMPACT;

                // Reset animation
                animationIndex = 0;
                animationTick = 0;
            }
        }
    }

    @Override
    public void checkEnemyHit(Enemy enemy, PlayerModel playerModel) {
        // not used, MaitaFireProjectiles can't hit enemies
    }

    @Override
    public void checkPlayerHit(PlayerModel playerModel) {
        // Parameters: player = player that is being checked for collision with projectile

        if (!playerModel.isActive() || playerModel.isImmune())
            return;

        if (hitbox.intersects(playerModel.getHitbox()) && !playerModel.isRespawning()) {
            playerModel.death();
            state = IMPACT;

            // Reset animation
            animationIndex = 0;
            animationTick = 0;
        }
    }
}

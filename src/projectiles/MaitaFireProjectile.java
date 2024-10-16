package projectiles;

import java.awt.*;

import entities.Enemy;
import entities.Player;
import model.utilz.Constants.Direction;

import static model.utilz.Constants.Direction.*;
import static model.utilz.HelpMethods.CanMoveHere;
import static model.utilz.Constants.Projectiles.*;
import static model.utilz.Constants.Projectiles.ProjectileState.IMPACT;
import static model.utilz.Constants.Projectiles.ProjectileState.MOVING;
import static model.utilz.Constants.Projectiles.ProjectileType.MAITA_FIREBALL;

public class MaitaFireProjectile extends Projectile {

    public MaitaFireProjectile(float x, float y, Direction direction) {
        super(x, y, direction, MAITA_FIREBALL);
    }

    @Override
    protected void draw(Graphics g) {
        g.drawImage(projectileManager.getSprites(MAITA_FIREBALL)[getAnimation(state)][animationIndex], (int) hitbox.x + OFFSET_X, (int) hitbox.y + OFFSET_Y, W, H, null);
    }

    @Override
    protected void updatePos() {

        if (state == MOVING) {

            float xSpeed;
            if (direction == LEFT)
                xSpeed = -MAITA_FIREBALL_SPEED;
            else
                xSpeed = MAITA_FIREBALL_SPEED;

            if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelManager.getLevelData()))
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
    public void checkEnemyHit(Enemy enemy, Player player) {
        // not used, MaitaFireProjectiles can't hit enemies
    }

    @Override
    public void checkPlayerHit(Player player) {
        // Parameters: player = player that is being checked for collision with projectile

        if (!player.isActive() || player.isImmune())
            return;

        if (hitbox.intersects(player.getHitbox()) && !player.isRespawning()) {
            player.death();
            state = IMPACT;

            // Reset animation
            animationIndex = 0;
            animationTick = 0;
        }
    }
}

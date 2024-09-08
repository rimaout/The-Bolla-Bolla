package entities;

import levels.LevelManager;
import utilz.Constants.Direction;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.Direction.*;
import static utilz.Constants.Projectiles.*;
import static utilz.Constants.Projectiles.ProjectileState.IMPACT;
import static utilz.Constants.Projectiles.ProjectileState.MOVING;
import static utilz.HelpMethods.CanMoveHere;

public class MaitaProjectile extends Projectile {

    public MaitaProjectile(float x, float y, Direction direction) {
        super(x, y, direction);
    }

    @Override
    protected void draw(Graphics g, BufferedImage[][] sprites) {
        g.drawImage(sprites[getAnimation(state)][animationIndex], (int) hitbox.x + OFFSET_X, (int) hitbox.y + OFFSET_Y, W, H, null);
        drawHitbox(g);
    }

    @Override
    protected void updatePos() {

        if (state == MOVING) {

            float xSpeed;
            if (direction == LEFT)
                xSpeed = -FIREBALL_SPEED;
            else
                xSpeed = FIREBALL_SPEED;

            int[][] levelData = LevelManager.getInstance().getCurrentLevel().getLevelData();
            if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData))
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
    protected void checkPlayerCollision(Player player) {
        if (hitbox.intersects(player.getHitbox()) && !player.isRespawning()) {
            player.death();
            state = IMPACT;

            // Reset animation
            animationIndex = 0;
            animationTick = 0;
        }
    }

}

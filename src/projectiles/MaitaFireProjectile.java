package projectiles;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import levels.LevelManager;
import utilz.Constants.Direction;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.Direction.*;
import static utilz.Constants.Projectiles.*;
import static utilz.Constants.Projectiles.ProjectileState.IMPACT;
import static utilz.Constants.Projectiles.ProjectileState.MOVING;
import static utilz.Constants.Projectiles.ProjectileType.MAITA_FIREBALL;
import static utilz.HelpMethods.CanMoveHere;

public class MaitaFireProjectile extends Projectile {

    public MaitaFireProjectile(float x, float y, Direction direction) {
        super(x, y, direction, MAITA_FIREBALL);
    }

    @Override
    protected void draw(Graphics g, BufferedImage[][] sprites) {
        g.drawImage(sprites[getAnimation(state)][animationIndex], (int) hitbox.x + OFFSET_X, (int) hitbox.y + OFFSET_Y, W, H, null);
    }

    @Override
    protected void updatePos() {

        if (state == MOVING) {

            float xSpeed;
            if (direction == LEFT)
                xSpeed = -MAITA_FIREBALL_SPEED;
            else
                xSpeed = MAITA_FIREBALL_SPEED;

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
    public void checkEntityHit(Entity player) {
        if (!player.isActive())
            return;

        if (!(player instanceof Player p))
            throw new IllegalArgumentException("MaitaFireProjectile can only hit Player, use a Player has parameter");

        if (hitbox.intersects(p.getHitbox()) && !p.isRespawning()) {
            p.death();
            state = IMPACT;

            // Reset animation
            animationIndex = 0;
            animationTick = 0;
        }
    }

}

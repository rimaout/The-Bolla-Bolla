package entities;

import utilz.Constants.Direction;
import utilz.Constants.Projectiles.ProjectileState;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.Projectiles.ProjectileState.IMPACT;
import static utilz.Constants.Projectiles.ProjectileState.MOVING;
import static utilz.Constants.Projectiles.*;

import java.awt.*;
import java.awt.image.BufferedImage;


public abstract class Projectile extends Entity{
    protected ProjectileState state = MOVING;
    protected Direction direction;

    public Projectile(float x, float y, Direction direction) {
        super(x, y, H, W);
        this.direction = direction;

        initHitbox(HITBOX_W, HITBOX_H);
    }

    public void update(Player player) {
        updatePos();
        checkPlayerCollision(player);
        updateAnimationTick();
    }

    protected abstract void draw(Graphics g, BufferedImage[][] sprites);
    protected abstract void updatePos();
    protected abstract void checkPlayerCollision(Player player);

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(state)) {
                animationIndex = 0;
            }
        }

        if (state == IMPACT && animationIndex == getSpriteAmount(state) - 1) {
           active = false;
        }
    }
}

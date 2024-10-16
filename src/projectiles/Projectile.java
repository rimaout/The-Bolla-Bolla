package projectiles;

import java.awt.*;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import model.utilz.Constants.Direction;
import model.utilz.Constants.Projectiles.ProjectileState;

import static model.utilz.Constants.Projectiles.*;
import static model.utilz.Constants.ANIMATION_SPEED;
import static model.utilz.Constants.Projectiles.ProjectileState.IMPACT;
import static model.utilz.Constants.Projectiles.ProjectileState.MOVING;

public abstract class Projectile extends Entity {
    protected final ProjectileManager projectileManager = ProjectileManager.getInstance();

    protected ProjectileState state = MOVING;
    protected ProjectileType type;
    protected Direction direction;

    public Projectile(float x, float y, Direction direction, ProjectileType type) {
        super(x, y, H, W);
        this.direction = direction;
        this.type = type;

        initHitbox(HITBOX_W, HITBOX_H);
    }

    public void update() {
        updatePos();
        updateAnimationTick();
    }

    protected abstract void draw(Graphics g);
    protected abstract void updatePos();
    protected abstract void checkEnemyHit(Enemy enemy, Player player);
    protected abstract void checkPlayerHit(Player player);

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(state, type)) {
                animationIndex = 0;
            }
        }

        if (state == IMPACT && animationIndex == getSpriteAmount(state, type) - 1) {
           active = false;
        }
    }
}

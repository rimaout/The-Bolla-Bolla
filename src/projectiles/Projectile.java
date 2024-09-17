package projectiles;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import levels.Level;
import levels.LevelManager;
import utilz.Constants.Direction;
import utilz.Constants.Projectiles.ProjectileState;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.Projectiles.ProjectileState.IMPACT;
import static utilz.Constants.Projectiles.ProjectileState.MOVING;
import static utilz.Constants.Projectiles.*;

import java.awt.*;


public abstract class Projectile extends Entity {
    protected ProjectileState state = MOVING;
    protected ProjectileType type;
    protected Direction direction;

    protected ProjectileManager projectileManager = ProjectileManager.getInstance();
    protected Level currentLevel = LevelManager.getInstance().getCurrentLevel();

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

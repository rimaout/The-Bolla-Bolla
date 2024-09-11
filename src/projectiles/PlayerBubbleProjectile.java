package projectiles;

import bubbles.BubbleManager;
import bubbles.EmptyBubble;
import bubbles.EnemyBubble;
import entities.Enemy;
import entities.Entity;
import levels.LevelManager;
import utilz.Constants.Direction;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.Direction.LEFT;
import static utilz.Constants.Direction.RIGHT;
import static utilz.Constants.Projectiles.*;
import static utilz.Constants.Projectiles.ProjectileType.PLAYER_BUBBLE;
import static utilz.HelpMethods.CanMoveHere;

public class PlayerBubbleProjectile extends Projectile {

    public PlayerBubbleProjectile(float x, float y, Direction direction) {
        super(x, y, direction, PLAYER_BUBBLE);
        this.direction = direction;
    }

    public void draw(Graphics g, BufferedImage[][] sprites) {
        g.drawImage(sprites[0][animationIndex], (int) hitbox.x, (int) hitbox.y, W, H, null);
    }

    public void update() {

        updateAnimationTick();
        updatePos();
    }

    @Override
    protected void updateAnimationTick() {
        float projectileSpeedMultiplier = BubbleManager.getInstance().getProjectileSpeedMultiplier();
        float projectileDistanceMultiplier = BubbleManager.getInstance().getProjectileDistanceMultiplier();
        animationTick += projectileSpeedMultiplier / projectileDistanceMultiplier;

        animationTick++;
        if (animationTick > PROJECTILE_ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= 4) {
                active = false;
                //BubbleManager.getInstance().addBubble(new EmptyBubble(hitbox.x, hitbox.y, direction));
            }
        }
    }

    protected void updatePos() {

        int[][] levelData = LevelManager.getInstance().getCurrentLevel().getLevelData();
        float projectileSpeed = PLAYER_BUBBLE_SPEED * BubbleManager.getInstance().getProjectileSpeedMultiplier();

        float xSpeed;
        if (direction == LEFT)
            xSpeed = -projectileSpeed;
        else
            xSpeed = projectileSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData))
            hitbox.x += xSpeed;
    }

    @Override
    protected void checkEntityHit(Entity enemy) {

        if (!(enemy instanceof Enemy e))
            throw new IllegalArgumentException("PlayerBubbleProjectile can only hit enemies, use an Entity as parameter");

        if (hitbox.intersects(e.getHitbox())) {
            e.bubbleCapture();
            BubbleManager.getInstance().addBubble(new EnemyBubble(e, e.getHitbox().x, e.getHitbox().y, direction));
            active = false;}
    }
}

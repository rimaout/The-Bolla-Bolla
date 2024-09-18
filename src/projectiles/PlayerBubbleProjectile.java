package projectiles;

import audio.AudioPlayer;
import bubbles.playerBubbles.EmptyBubble;
import bubbles.playerBubbles.PlayerBubblesManager;
import entities.Enemy;
import entities.Player;
import utilz.Constants.Direction;
import utilz.Constants.AudioConstants;

import java.awt.*;

import static utilz.Constants.Direction.LEFT;
import static utilz.Constants.Projectiles.*;
import static utilz.Constants.Projectiles.ProjectileType.PLAYER_BUBBLE;
import static utilz.HelpMethods.CanMoveHere;

public class PlayerBubbleProjectile extends Projectile {
    private boolean playSound = true;

    public PlayerBubbleProjectile(float x, float y, Direction direction) {
        super(x, y, direction, PLAYER_BUBBLE);
        this.direction = direction;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(projectileManager.getSprites(PLAYER_BUBBLE)[0][animationIndex], (int) hitbox.x, (int) hitbox.y, W, H, null);

        if (playSound) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.BUUBBLE_SHOOT);
            playSound = false;
        }
    }

    @Override
    protected void updatePos() {

        float projectileSpeed = PLAYER_BUBBLE_SPEED * ProjectileManager.getInstance().getPlayerProjectileSpeedMultiplier();

        float xSpeed;
        if (direction == LEFT)
            xSpeed = -projectileSpeed;
        else
            xSpeed = projectileSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, currentLevel.getLevelData()))
            hitbox.x += xSpeed;
    }

    @Override
    protected void checkEnemyHit(Enemy enemy, Player player) {
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
    protected void checkPlayerHit(Player player) {
        // not used, playerBubbles can't hit players
    }

    @Override
    protected void updateAnimationTick() {
        float projectileSpeedMultiplier = ProjectileManager.getInstance().getPlayerProjectileSpeedMultiplier();
        float projectileDistanceMultiplier = ProjectileManager.getInstance().getPlayerProjectileDistanceMultiplier();

        animationTick += projectileSpeedMultiplier / projectileDistanceMultiplier;

        animationTick++;
        if (animationTick > PROJECTILE_ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= 4) {
                active = false;
                PlayerBubblesManager.getInstance().addBubble(new EmptyBubble(hitbox.x, hitbox.y, direction));
            }
        }
    }
}
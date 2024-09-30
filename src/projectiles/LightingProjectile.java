package projectiles;

import java.awt.*;

import main.Game;
import entities.Enemy;
import entities.Player;
import audio.AudioPlayer;
import utilz.Constants.Direction;
import utilz.Constants.AudioConstants;
import utilz.Constants.Projectiles.ProjectileType;

import static utilz.Constants.Direction.LEFT;
import static utilz.Constants.Projectiles.*;
import static utilz.Constants.Projectiles.H;
import static utilz.HelpMethods.IsPerimeterWallTile;
import static utilz.Constants.Projectiles.ProjectileType.LIGHTNING;

public class LightingProjectile extends Projectile{
    private boolean playSound = true;

    public LightingProjectile(float x, float y, ProjectileType type) {
        super(x, y, CalculateLightingDirection(x), type);
    }

    @Override
    protected void draw(Graphics g) {
       g.drawImage(projectileManager.getSprites(LIGHTNING)[1][0],(int) hitbox.x + OFFSET_X, (int) hitbox.y + OFFSET_Y, W, H, null);

        if (playSound) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.LIGHTNING);
            playSound = false;
        }
    }

    @Override
    protected void updatePos() {

        float xSpeed;
        if (direction == LEFT)
            xSpeed = -LIGHTNING_SPEED;
        else
            xSpeed = LIGHTNING_SPEED;

        if (!IsPerimeterWallTile(hitbox.x))
            hitbox.x += xSpeed;
        else
            active = false;
    }

    @Override
    protected void checkEnemyHit(Enemy enemy, Player player) {
        //Parameters: enemy  = enemy that is being checked for collision with projectile
        //            player = player to add score to if the enemy is killed

        if (!enemy.isActive() || enemy.isImmune())
            return;

        if (hitbox.intersects(enemy.getHitbox())) {
            enemy.instantKill(player);
            active = false;
        }
    }

    @Override
    protected void checkPlayerHit(Player player) {
        // not used, lighting can't hit players
    }

    private static Direction CalculateLightingDirection(float x) {
        // if lighting is on the left side of the screen, go right
        if (x < Game.GAME_WIDTH / 2f)  return Direction.RIGHT;

        // if lighting is on the right side of the screen, go left
        else return Direction.LEFT;
    }
}

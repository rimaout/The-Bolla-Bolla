package bubbles;

import entities.Enemy;
import entities.EnemyManager;
import entities.Player;
import utilz.Constants.Direction;

import javax.swing.*;

import static utilz.Constants.Direction.LEFT;
import static utilz.Constants.Direction.RIGHT;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utilz.Constants.Bubble.*;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;

public class EnemyBubble extends PlayerBubble{
    Enemy enemy;
    private boolean playerPopped;
    private Random random = new Random();

    private float ySpeedDead;
    private float xSpeedDead;

    public EnemyBubble(float x, float y, Direction direction, int[][] levelData, Direction[][] windLevelData, Enemy enemy) {
        super(x, y, direction, levelData, windLevelData);
        this.state = NORMAL;
        this.enemy = enemy;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage[][] enemySprites = EnemyManager.getInstance().getEnemySprite(enemy.getEnemyType());

        if (state == NORMAL)
            g.drawImage(enemySprites[BOBBLE_GREEN_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);

        else if (state == RED || state == BLINKING)
            g.drawImage(enemySprites[BOBBLE_RED_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);

        else if (state == POP_NORMAL)
            g.drawImage(enemySprites[BOBBLE_GREEN_POP_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);

        else if (state == POP_RED)
            g.drawImage(enemySprites[BOBBLE_RED_POP_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);
        else if (state == DEAD)
            g.drawImage(enemySprites[DEAD_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);
    }


    public void respawnEnemy() {
        if (!playerPopped) {
            enemy.resetEnemy();
            enemy.getHitbox().x = hitbox.x;
            enemy.getHitbox().y = hitbox.y;
            enemy.setEnemyState(HUNGRY_STATE);
            enemy.setActive(true);
        }
    }



    protected void updateDeadAnimation() {
        ySpeed += GRAVITY;

        if (direction == LEFT)
            xSpeed = -xSpeedDead;
        else
            xSpeed = xSpeedDead;

        // Going up
        if (ySpeed < 1) {
            hitbox.y += ySpeed;

            if (willBubbleBeInPerimeterWall(xSpeed))
                changeDirection();

            hitbox.x += xSpeed;
        }
        // Going down
        else if (CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, levelData)) {
            hitbox.y += ySpeed;
            updateXPos(xSpeed, levelData);

        } else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, ySpeed, levelData);
            conpenetrationSafeUpdateXPos(xSpeed, levelData);
            //Todo: Swap item (fruit)
            active = false;
        }
    }


    @Override
    public void playerPop(Player player) {

        BubbleManager.getInstance().triggerChainExplosion(this);

        // calculate the speed of the bubble (random values between 50% and 100% of the Max speed)
        ySpeedDead = - (0.5f + random.nextFloat() * 0.5f) * DEAD_Y_SPEED;
        ySpeed = ySpeedDead;
        xSpeedDead = (0.5f + random.nextFloat() * 0.5f) * DEAD_X_SPEED;
        xSpeed = xSpeedDead;

        // Set the direction of the bubble (following the player direction)
        direction = player.getDirection();

        // Set Bubble state
        state = DEAD;
        playerPopped = true;
        animationIndex = 0;
        animationTick = 0;

        // TODO: remove when you can set the enemy to dead after the end of the animation
        enemy.setAlive(false);
    }

    private boolean willBubbleBeInPerimeterWall(float xSpeed) {
        if (direction == LEFT)
            return IsPerimeterWallTile(hitbox.x + xSpeed);
        else
            return IsPerimeterWallTile(hitbox.x + hitbox.width + xSpeed);
    }

}

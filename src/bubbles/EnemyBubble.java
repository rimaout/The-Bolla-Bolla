package bubbles;

import entities.Enemy;
import entities.EnemyManager;
import entities.Player;
import itemesAndRewards.ItemManager;
import itemesAndRewards.RewardPointsManager;
import utilz.Constants.Direction;

import static utilz.Constants.Direction.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Bubble.*;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.Items.BubbleRewardType.*;
import static utilz.HelpMethods.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class EnemyBubble extends EmptyBubble {
    Enemy enemy;

    private int consecutivePopsCounter;
    private boolean playerPopped;
    private float ySpeedDead;
    private float xSpeedDead;


    private Random random = new Random();

    public EnemyBubble(Enemy enemy, float x, float y, Direction direction) {
        super(x, y, direction);
        this.state = NORMAL;
        this.enemy = enemy;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage[][] enemySprites = EnemyManager.getInstance().getEnemySprite(enemy.getEnemyType());

        if (state == NORMAL)
            g.drawImage(enemySprites[BOBBLE_GREEN_ANIMATION][animationIndex], (int) (hitbox.x - ENEMY_HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMMAGE_W, IMMAGE_H, null);

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
        if (ySpeed < 0.5) {
            hitbox.y += ySpeed;

            if (willBubbleBeInPerimeterWall(xSpeed))
                changeDirection();

            hitbox.x += xSpeed;
        }
        // Going down
        else if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + ySpeed, levelData)) {
            hitbox.y += ySpeed;
            updateXPos(xSpeed, levelData);
        } else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, ySpeed, levelData);
            conpenetrationSafeUpdateXPos(xSpeed, levelData);
            active = false;

            // Spawn Reward
            ItemManager.getInstance().addBubbleReward((int) hitbox.x, (int) hitbox.y, GetBubbleRewardType(consecutivePopsCounter));
        }
    }

    @Override
    public void playerPop(Player player, int EnemyBubblePopCounte, ChainExplosionManager chainExplosionManager) {

        if (!popped) {
            chainExplosionManager.increaseEnemyBubblePopCounter();
            consecutivePopsCounter = chainExplosionManager.getEnemyBubblePopCounter();
            RewardPointsManager.getInstance().addChainReactionReward(consecutivePopsCounter);
        }
        
       playerPop(player);
    }

    public void playerPop(Player player) {

        // calculate the speed of the bubble (random values between 50% and 100% of the Max speed)
        ySpeedDead = - (0.5f + random.nextFloat() * 0.5f) * DEAD_Y_SPEED;
        ySpeed = ySpeedDead;
        xSpeedDead = (0.5f + random.nextFloat() * 0.5f) * DEAD_X_SPEED;
        xSpeed = xSpeedDead;

        // Set the direction of the bubble (following the player direction)
        direction = player.getDirection();

        // Set Bubble state
        state = DEAD;
        popped = true;
        playerPopped = true;
        animationIndex = 0;
        animationTick = 0;

        enemy.setAlive(false);
    }


    private boolean willBubbleBeInPerimeterWall(float xSpeed) {
        if (direction == LEFT)
            return IsPerimeterWallTile(hitbox.x + xSpeed);
        else
            return IsPerimeterWallTile(hitbox.x + hitbox.width + xSpeed);
    }

}
package bubbles.playerBubbles;

import model.audio.AudioPlayer;
import entities.Enemy;
import entities.EnemyManager;
import model.entities.PlayerModel;
import itemesAndRewards.ItemManager;
import itemesAndRewards.RewardPointsManager;
import model.utilz.Constants.AudioConstants;
import model.utilz.Constants.Direction;

import java.awt.*;
import java.util.Random;

import static model.utilz.Constants.Bubble.*;
import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.EnemyConstants.*;
import static model.utilz.Constants.GRAVITY;
import static model.utilz.Constants.Items.BubbleRewardType.GetBubbleRewardType;
import static model.utilz.HelpMethods.*;

public class EnemyBubble extends EmptyBubble {

    private final EnemyManager enemyManager = EnemyManager.getInstance();
    private final Random random = new Random();
    private final Enemy enemy;

    private float ySpeedDead;
    private float xSpeedDead;
    private boolean playerPopped;
    private int consecutivePopsCounter;
    private boolean playPopSound = false;

    public EnemyBubble(Enemy enemy, float x, float y, Direction direction) {
        super(x, y, direction);
        this.state = NORMAL;
        this.enemy = enemy;
    }

    @Override
    public void draw(Graphics g) {

        if (state == NORMAL)
            g.drawImage(enemyManager.getEnemySprite(enemy.getEnemyType())[BOBBLE_GREEN_ANIMATION][animationIndex], (int) (hitbox.x - ENEMY_HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMAGE_W, IMAGE_H, null);

        else if (state == RED || state == BLINKING)
            g.drawImage(enemyManager.getEnemySprite(enemy.getEnemyType())[BOBBLE_RED_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMAGE_W, IMAGE_H, null);

        else if (state == POP_NORMAL)
            g.drawImage(enemyManager.getEnemySprite(enemy.getEnemyType())[BOBBLE_GREEN_POP_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMAGE_W, IMAGE_H, null);

        else if (state == POP_RED)
            g.drawImage(enemyManager.getEnemySprite(enemy.getEnemyType())[BOBBLE_RED_POP_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMAGE_W, IMAGE_H, null);
        else if (state == DEAD)
            g.drawImage(enemyManager.getEnemySprite(enemy.getEnemyType())[DEAD_ANIMATION][animationIndex], (int) (hitbox.x - HITBOX_OFFSET_X), (int) (hitbox.y - HITBOX_OFFSET_Y), IMAGE_W, IMAGE_H, null);

        if (playPopSound) {
            playPopSound = false;
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.ENEMY_BUBBLE_POP);
        }
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
        else if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + ySpeed, levelManagerModel.getLevelData())) {
            hitbox.y += ySpeed;
            updateXPos(xSpeed);
        } else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, ySpeed, levelManagerModel.getLevelData());
            conpenetrationSafeUpdateXPos(xSpeed);
            active = false;

            // Spawn Reward
            ItemManager.getInstance().addBubbleReward((int) hitbox.x, (int) hitbox.y, GetBubbleRewardType(consecutivePopsCounter));
        }
    }

    @Override
    public void pop() {
        if (state != POP_NORMAL && state != POP_RED) {
            if (state == RED)
                state = POP_RED;
            else
                state = POP_NORMAL;

            animationIndex = 0;
            animationTick = 0;
        }

        enemy.setAlive(false);
    }


    @Override
    public void playerPop(PlayerModel playerModel, int EnemyBubblePopCounter, ChainExplosionManager chainExplosionManager) {

        if (!popped) {
            chainExplosionManager.increaseEnemyBubblePopCounter();
            consecutivePopsCounter = chainExplosionManager.getEnemyBubblePopCounter();
            RewardPointsManager.getInstance().addChainReactionReward(consecutivePopsCounter);
        }
        
       playerPop(playerModel);
    }

    public void playerPop(PlayerModel playerModel) {

        // calculate the speed of the bubble (random values between 50% and 100% of the Max speed)
        ySpeedDead = - (0.5f + random.nextFloat() * 0.5f) * DEAD_Y_SPEED;
        ySpeed = ySpeedDead;
        xSpeedDead = (0.5f + random.nextFloat() * 0.5f) * DEAD_X_SPEED;
        xSpeed = xSpeedDead;

        // Set the direction of the bubble (following the player direction)
        direction = playerModel.getDirection();

        // Set Bubble state
        state = DEAD;
        popped = true;
        playerPopped = true;
        animationIndex = 0;
        animationTick = 0;

        enemy.setAlive(false);

        playPopSound = true;
    }


    private boolean willBubbleBeInPerimeterWall(float xSpeed) {
        if (direction == LEFT)
            return IsPerimeterWallTile(hitbox.x + xSpeed);
        else
            return IsPerimeterWallTile(hitbox.x + hitbox.width + xSpeed);
    }
}
package model.bubbles.playerBubbles;

import model.entities.EnemyModel;
import model.entities.PlayerModel;
import model.itemesAndRewards.ItemManagerModel;
import model.itemesAndRewards.RewardPointsManagerModel;
import model.utilz.Constants.Direction;

import java.util.Random;

import static model.utilz.Constants.Bubble.*;
import static model.utilz.Constants.Bubble.BubbleType.ENEMY_BUBBLE;
import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.EnemyConstants.*;
import static model.utilz.Constants.GRAVITY;
import static model.utilz.Constants.Items.BubbleRewardType.GetBubbleRewardType;
import static model.entities.HelpMethods.*;

public class EnemyBubbleModel extends EmptyBubbleModel {

    private final Random random = new Random();
    private final EnemyModel enemyModel;

    private float ySpeedDead;
    private float xSpeedDead;
    private boolean playerPopped;
    private int consecutivePopsCounter;

    public EnemyBubbleModel(EnemyModel enemyModel, float x, float y, Direction direction) {
        super(x, y, direction);
        this.state = NORMAL;
        this.enemyModel = enemyModel;
        this.bubbleType = ENEMY_BUBBLE;
    }

    public void respawnEnemy() {
        if (!playerPopped) {
            enemyModel.resetEnemy();
            enemyModel.getHitbox().x = hitbox.x;
            enemyModel.getHitbox().y = hitbox.y;
            enemyModel.setEnemyState(HUNGRY_STATE);
            enemyModel.setActive(true);
        }
    }

    protected void updateDeadAction() {

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
            ItemManagerModel.getInstance().addBubbleReward((int) hitbox.x, (int) hitbox.y, GetBubbleRewardType(consecutivePopsCounter));
        }
    }

    @Override
    public void pop() {
        if (state != POP_NORMAL && state != POP_RED) {
            if (state == RED)
                state = POP_RED;
            else
                state = POP_NORMAL;
        }

        enemyModel.setAlive(false);
    }


    @Override
    public void playerPop(PlayerModel playerModel, int EnemyBubblePopCounter, ChainExplosionManager chainExplosionManager) {

        if (!popped) {
            chainExplosionManager.increaseEnemyBubblePopCounter();
            consecutivePopsCounter = chainExplosionManager.getEnemyBubblePopCounter();
            RewardPointsManagerModel.getInstance().addChainReactionReward(consecutivePopsCounter);
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

        enemyModel.setAlive(false);
    }

    private boolean willBubbleBeInPerimeterWall(float xSpeed) {
        if (direction == LEFT)
            return IsPerimeterWallTile(hitbox.x + xSpeed);
        else
            return IsPerimeterWallTile(hitbox.x + hitbox.width + xSpeed);
    }

    public EnemyModel getEnemyModel() {
        return enemyModel;
    }

    public boolean isPlayerPopped() {
        return playerPopped;
    }
}
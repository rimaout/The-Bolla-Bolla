package model.bubbles.playerBubbles;

import model.entities.EnemyModel;
import model.entities.PlayerModel;
import model.utilz.Constants.Direction;
import model.itemesAndRewards.ItemManagerModel;
import model.itemesAndRewards.RewardPointsManagerModel;

import java.util.Random;

import static model.utilz.HelpMethods.*;
import static model.utilz.Constants.GRAVITY;
import static model.utilz.Constants.Bubble.*;
import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.EnemyConstants.*;
import static model.utilz.Constants.Bubble.BubbleType.ENEMY_BUBBLE;
import static model.utilz.Constants.Items.BubbleRewardType.GetBubbleRewardType;

/**
 * Represents an enemy bubble created by the player.
 *
 * <p>This class extends the EmptyBubbleModel and provides specific behavior for enemy bubbles.
 * Enemy bubbles contain an enemy model and have additional logic for handling the enemy's state and interactions.
 */
public class EnemyBubbleModel extends EmptyBubbleModel {

    private final Random random = new Random(); // Random number generator used to randomize direction and speed of the bubble when popped
    private final EnemyModel enemyModel;

    private float ySpeedDead;
    private float xSpeedDead;
    private boolean playerPopped;
    private int consecutivePopsCounter;

    /**
     * Constructs a new EnemyBubbleModel.
     *
     * @param enemyModel the enemy model of the captured enemy
     * @param x the starting x coordinate
     * @param y the starting y coordinate
     * @param direction the starting direction
     */
    public EnemyBubbleModel(EnemyModel enemyModel, float x, float y, Direction direction) {
        super(x, y, direction);
        this.state = NORMAL;
        this.enemyModel = enemyModel;
        this.bubbleType = ENEMY_BUBBLE;
    }

    /**
     * Respawns the enemy if the bubble was not popped by the player.
     *
     * <p>If the bubble was not popped by the player, this method resets the enemy's state,
     * repositions the enemy to the bubble's current coordinates, and sets the enemy to an active state.
     */
    public void respawnEnemy() {
        if (!playerPopped) {
            enemyModel.resetEnemy();
            enemyModel.getHitbox().x = hitbox.x;
            enemyModel.getHitbox().y = hitbox.y;
            enemyModel.setEnemyState(HUNGRY_STATE);
            enemyModel.setActive(true);
        }
    }

    /**
     * Updates the bubble's movement after it has been popped by the player.
     *
     * <p>This method handles the bubble's movement based on its current state and direction.
     * <p>If the bubble is moving upwards, it updates the y-coordinate and checks for perimeter wall collisions to change direction if necessary.
     * <p>If the bubble is moving downwards, it updates the y-coordinate and checks for collisions with solid objects to stop the bubble and spawn a reward.
     */
    protected void updateDeadAction() {

        ySpeed += GRAVITY;

        if (direction == LEFT)
            xSpeed = -xSpeedDead;
        else
            xSpeed = xSpeedDead;

        // Going up
        if (ySpeed < 0.5) {
            hitbox.y += ySpeed;

            if (willBubbleCollideWithInPerimeterWall(xSpeed))
                changeDirection();

            hitbox.x += xSpeed;
        }
        // Going down
        else if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + ySpeed, levelManagerModel.getLevelTileData())) {
            hitbox.y += ySpeed;
            updateXPos(xSpeed);
        } else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, ySpeed, levelManagerModel.getLevelTileData());
            conpenetrationSafeUpdateXPos(xSpeed);
            active = false;

            // Spawn Reward
            ItemManagerModel.getInstance().addBubbleReward((int) hitbox.x, (int) hitbox.y, GetBubbleRewardType(consecutivePopsCounter));
        }
    }

    /**
     * Pops the bubble and updates its state.
     *
     * <p>If the bubble's current state is not POP\_NORMAL or POP\_RED, this method changes the state to the appropriate pop state based on the previous state.
     * <p>Additionally, it sets the enemy's alive status to false.
     */
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

    /**
     * Handles the player popping the bubble, this method is only used by the {@link ChainReactionManager}.
     *
     * <p>If the bubble has not been popped yet, this method increases the enemy bubble pop counter,
     * updates the consecutive pops counter, and adds a chain reaction reward.
     * <p>Then, it calls the {@link #playerPop(PlayerModel)} method to handle the actual popping logic.
     *
     * @param playerModel the player model
     * @param EnemyBubblePopCounter the number of enemy bubbles popped
     * @param chainReactionManager the chain explosion manager
     */
    @Override
    public void playerPop(PlayerModel playerModel, int EnemyBubblePopCounter, ChainReactionManager chainReactionManager) {

        if (!popped) {
            chainReactionManager.increaseEnemyBubblePopCounter();
            consecutivePopsCounter = chainReactionManager.getEnemyBubblePopCounter();
            RewardPointsManagerModel.getInstance().addChainReactionReward(consecutivePopsCounter);
        }
        
       playerPop(playerModel);
    }

    /**
     * Handles the player popping the enemy bubble.
     *
     * <p>This method calculates the speed and direction of the bubble after it has been popped by the player.
     * <p>It sets the bubble's state to DEAD, marks it as popped, and updates the enemy's alive status to false.
     *
     * @param playerModel the player model
     */
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

    /**
     * Checks if the bubble will collide with a perimeter wall based on its current direction and speed.
     *
     * <p>If the bubble is moving to the left, it checks the tile at the left edge of the bubble's hitbox.
     * <p>If the bubble is moving to the right, it checks the tile at the right edge of the bubble's hitbox.
     *
     * @param xSpeed the horizontal speed of the bubble
     * @return true if the bubble will collide with a perimeter wall, false otherwise
     */
    private boolean willBubbleCollideWithInPerimeterWall(float xSpeed) {
        if (direction == LEFT)
            return IsPerimeterWallTile(hitbox.x + xSpeed);
        else
            return IsPerimeterWallTile(hitbox.x + hitbox.width + xSpeed);
    }

    // ------- Getters Methods ------- //

    public EnemyModel getEnemyModel() {
        return enemyModel;
    }

    public boolean isPlayerPopped() {
        return playerPopped;
    }
}
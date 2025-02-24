package com.rima.model.entities;

import com.rima.model.utilz.Constants;
import com.rima.model.bubbles.playerBubbles.EnemyBubbleModel;
import com.rima.model.bubbles.playerBubbles.PlayerBubblesManagerModel;

import static com.rima.model.utilz.Constants.Direction;
import static com.rima.model.utilz.Constants.Direction.*;
import static com.rima.model.utilz.Constants.EnemyConstants.*;
import static com.rima.model.utilz.HelpMethods.GetEntityYPosAboveFloor;

/**
 * Represents an enemy in the game, including its state, movement, and interactions with the player.
 *
 * <p>This abstract class provides the basic properties and methods for enemy entities, such as movement variables,
 * state management, and interactions with the player. Specific enemy types should extend this class and implement
 * the abstract methods.
 */
public abstract class EnemyModel extends EntityModel {

    protected boolean active = true;
    protected boolean alive = true;
    protected int enemyState;
    protected EnemyType enemyType;

    // Enemy Movement Variables
    protected float xSpeed;
    protected float ySpeed;
    protected float fallSpeed;
    protected float flySpeed;
    protected float walkSpeed;
    protected Direction walkingDir;
    protected Direction previousWalkingDir;
    protected Direction startWalkingDir;

    // Vertical Movement Variables
    protected boolean goUp = false;
    protected boolean goDown = false;
    protected boolean isFalling = false;
    protected boolean isJumping = false;

    // Spawn Info
    protected float spawnY;
    protected boolean reachedSpawn = false;

    // Player info
    protected int playerTileX, playerTileY;
    protected int updatePlayerPosMaxInterval;

    /**
     * Constructs an EnemyModel with the specified position, dimensions, enemy type, and initial walking direction.
     *
     * @param x the x-coordinate of the enemy's initial position
     * @param y the y-coordinate of the enemy's initial position
     * @param width the width of the enemy
     * @param height the height of the enemy
     * @param enemyType the type of the enemy
     * @param startWalkingDir the initial walking direction of the enemy
     */
    public EnemyModel(float x, float y, int width, int height, EnemyType  enemyType, Direction startWalkingDir) {
        super(x, INITIAL_SPAWN_POINT_Y, width, height);

        this.spawnY = y;
        this.enemyType = enemyType;
        this.walkingDir = startWalkingDir;
        this.startWalkingDir = startWalkingDir;
    }

    /**
     * Updates the state of the enemy based on the player's position.
     *
     * <p>This method is abstract and should be implemented by subclasses to define how the enemy's state
     * and behavior are updated in response to the player's actions and position.
     *
     * @param playerModel the player model used to update the enemy's state
     */
    public abstract void update(PlayerModel playerModel);

    /**
     * Updates the spawning state of the enemy.
     *
     * <p>This method checks if the enemy has reached its spawn point. If the enemy has not reached the spawn point,
     * it moves the enemy towards the spawn point and sets it to be immune. Once the enemy reaches the spawn point,
     * it sets the enemy's position above the floor, marks it as having reached the spawn point, and removes its immunity.
     */
    protected void updateSpawning() {

        // check if enemy will reach spawn point
        if (hitbox.y + SPAWN_TRANSITION_SPEED < spawnY) {
            hitbox.y += SPAWN_TRANSITION_SPEED;
            immune = true;
        }

        else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, SPAWN_TRANSITION_SPEED , levelManagerModel.getLevelTileData());
            reachedSpawn = true;
            immune = false;
        }
    }

    /**
     * Updates the state variables of the enemy based on its current state.
     *
     * <p>This method adjusts the enemy's movement speeds and the interval for updating the player's position
     * based on the enemy's current state. If the enemy is in the dead state, it sets the enemy to inactive.
     */
    protected void updateStateVariables() {
        switch (enemyState) {
            case NORMAL_STATE:
                fallSpeed = NORMAL_FALL_SPEED;
                flySpeed = NORMAL_FLY_SPEED;
                walkSpeed = NORMAL_WALK_SPEED;
                updatePlayerPosMaxInterval = NORMAL_PLAYER_INFO_MAX_UPDATE_INTERVAL;
                break;

            case HUNGRY_STATE:
                fallSpeed = HUNGRY_FALL_SPEED;
                flySpeed = HUNGRY_FLY_SPEED;
                walkSpeed = HUNGRY_WALK_SPEED;
                updatePlayerPosMaxInterval = HUNGRY_PLAYER_INFO_MAX_UPDATE_INTERVAL;
                break;

            case BOBBLE_STATE:
                break;

            case DEAD_STATE:
                active = false;
                break;
        }
    }

    /**
     * Checks if the player is within the enemy's viewing range.
     *
     * <p>This method calculates the horizontal distance between the enemy and the player and checks if it is within the defined viewing range.
     *
     * @param playerModel the player model used to determine the player's position
     * @return true if the player is within the viewing range, false otherwise
     */
    protected boolean isPlayerInViewingRange(PlayerModel playerModel) {
        return Math.abs(playerModel.getHitbox().x - hitbox.x) <= VIEWING_RANGE;
    }

    /**
     * Calculates the player's position in tile coordinates.
     *
     * <p>This method converts the player's position from pixel coordinates to tile coordinates
     * and updates the player's tile position variables.
     *
     * @param playerModel the player model used to determine the player's position
     */
    protected void calculatePlayersPos(PlayerModel playerModel) {
        playerTileX = (int) (playerModel.getHitbox().x / Constants.TILES_SIZE);
        playerTileY = (int)(playerModel.getHitbox().y / Constants.TILES_SIZE);
    }

    /**
     * Changes the walking direction of the enemy to the opposite direction of the current direction.
     */
    protected void changeWalkingDir() {
        if (walkingDir == LEFT)
            walkingDir = RIGHT;
        else
            walkingDir = LEFT;
    }

    /**
     * Returns the x-coordinate offset for flipping the enemy's sprite horizontally.
     */
    public int flipX() {
        Direction direction = switch (walkingDir) {
            case UP, DOWN -> previousWalkingDir;
            default -> walkingDir;
        };

        return (direction == RIGHT) ? width : 0;
    }
    /**
     * Returns the width multiplier for flipping the enemy's sprite horizontally.
     */
    public int flipW() {
        Direction direction = switch (walkingDir) {
            case UP, DOWN -> previousWalkingDir;
            default -> walkingDir;
        };

        return (direction == RIGHT) ? -1 : 1;
    }

    /**
     * Updates the walking direction of the enemy based on the player's position.
     *
     * <p>This method compares the player's tile position with the enemy's tile position
     * and sets the walking direction of the enemy to left or right accordingly.
     */
    protected void updateWalkingDir() {
        if (playerTileX < getTileX())
            walkingDir = LEFT;
        else if (playerTileX > getTileX())
            walkingDir = RIGHT;
    }

    /**
     * Determines if the player is to the left or right of the enemy.
     *
     * <p>This method compares the player's tile x-coordinate with the enemy's tile x-coordinate
     * and returns the direction indicating whether the player is to the left or right of the enemy.
     * If the player is on the same tile as the enemy, it returns NONE.
     *
     * @param playerModel the player model used to determine the player's position
     * @return the direction indicating if the player is to the left or right of the enemy, or NONE if they are on the same tile
     */
    protected Direction isPlayerLeftOrRight(PlayerModel playerModel) {
        if (playerModel.getTileX() < getTileX())
            return LEFT;
        else if (playerModel.getTileX() > getTileX())
            return RIGHT;
        else
            return NONE;
    }

    /**
     * Determines if the player is above or below the enemy.
     *
     * <p>This method compares the player's tile y-coordinate with the enemy's tile y-coordinate
     * and returns the direction indicating whether the player is above or below the enemy.
     * If the player is on the same tile as the enemy, it returns NONE.
     *
     * @param playerModel the player model used to determine the player's position
     * @return the direction indicating if the player is above or below the enemy, or NONE if they are on the same tile
     */
    protected Direction isPlayerUpOrDown(PlayerModel playerModel) {
        if (playerModel.getTileY() < getTileY())
            return UP;
        else if (playerModel.getTileY() > getTileY())
            return DOWN;
        else
            return NONE;
    }

    /**
     * Checks if the enemy can fall.
     *
     * <p>This method determines if the enemy can fall by checking if the tile below the enemy is within the level boundaries.
     *
     * @return true if the enemy can fall, false otherwise
     */
    protected boolean canFall(){
        // check if the under is not out of the level
        return getTileY() + 1 < Constants.TILES_IN_HEIGHT - 1;
    }

    /**
     * Resets the enemy to its initial state.
     *
     * <p>This method resets the enemy's position to its initial coordinates, sets the enemy to be active,
     * changes its state to NORMAL_STATE, and sets its walking direction to the initial direction.
     */
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        active = true;
        enemyState = NORMAL_STATE;
        walkingDir = startWalkingDir;
    }

    /**
     * Captures the enemy in a bubble.
     *
     * <p>This method creates a new `EnemyBubbleModel` instance with the enemy's current position and the specified direction,
     * adds it to the `PlayerBubblesManagerModel`, and sets the enemy's state to `BOBBLE_STATE` and inactive.
     *
     * @param direction the direction in which the bubble will move
     */
    public void bubbleCapture(Direction direction) {

        PlayerBubblesManagerModel.getInstance().addBubble(new EnemyBubbleModel(this, hitbox.x, hitbox.y, direction));
        active = false;
        enemyState = BOBBLE_STATE;
    }

    /**
     * Instantly kills the enemy and creates an enemy bubble.
     *
     * <p>This method sets the enemy's state to DEAD_STATE, marks it as inactive, and creates an enemy bubble
     * using the PlayerBubblesManagerModel with the current enemy and player model.
     * <p>Since the enemy is in the dead state, the bubble will be updated by {@link EnemyBubbleModel#updateDeadAction()} method and will not behave as a normal "floating" bubble.
     *
     * @param playerModel the player model used to create the enemy bubble
     */
    public void instantKill(PlayerModel playerModel) {
        active = false;
        enemyState = DEAD_STATE;
        PlayerBubblesManagerModel.getInstance().createEnemyBubble(this, playerModel);
    }

    // ------- Setters ------- //

    /**
     * Sets the alive status of the enemy.
     *
     * <p>This method updates the alive status of the enemy to the specified value.
     *
     * @param alive the new alive status to set
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Sets the active status of the enemy.
     *
     * <p>This method updates the active status of the enemy to the specified value.
     *
     * @param active the new active status to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Sets the state of the enemy.
     */
    public void setEnemyState(int state) {
        this.enemyState = state;
    }

    // ------- Getters ------- //

    /**
     * Returns the active status of the enemy.
     *
     * @return true if the enemy is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the alive status of the enemy.
     *
     * @return true if the enemy is alive, false otherwise
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Returns the current state of the enemy.
     *
     * @return the current state of the enemy
     */
    public int getEnemyState() {
        return enemyState;
    }

    /**
     * Returns whether the enemy has reached its spawn point.
     *
     * @return true if the enemy has reached its spawn point, false otherwise
     */
    public boolean getReachedSpawn() {
        return reachedSpawn;
    }

    /**
     * Returns the type of the enemy, (see {@link EnemyType}).
     *
     * @return the type of the enemy
     */
    public abstract EnemyType getEnemyType();
}
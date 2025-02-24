package com.rima.model.entities;

import com.rima.model.utilz.Constants.Direction;

import static com.rima.model.utilz.Constants.Direction.*;
import static com.rima.model.utilz.Constants.EnemyConstants.*;
import static com.rima.model.utilz.Constants.EnemyConstants.EnemyType.SKEL_MONSTA;

/**
 * SkelMonstaModel class is responsible for the skel monsta enemy logic
 *
 */
public class SkelMonstaModel extends EnemyModel {

    private float walkedDistance = 0;

    private boolean moving = false;
    private boolean spawning = true;
    private boolean despawning = false;

    private int nextMoveTimer = SKEL_MONSTA_MOVEMENT_TIMER;
    private int spawningTimer = SKEL_MONSTA_SPAWNING_TIMER;
    private int despawningTimer = SKEL_MONSTA_DESPAWNING_TIMER;

    /**
     * Constructs a new SkelMonstaModel with default spawn position and state.
     */
    public SkelMonstaModel() {
        super(SKEL_MONSTA_SPAWN_X, SKEL_MONSTA_SPAWN_Y, ENEMY_W, ENEMY_H, SKEL_MONSTA, RIGHT);

        // enemyModel custom variables for SkelMonsta
        active = false;
        y = SKEL_MONSTA_SPAWN_Y;    // Set the y position to the spawn point (the super constructor sets it outside the screen, but skelMonsta spawns from the ground)
        walkingDir = RIGHT;
        previousWalkingDir = RIGHT;
        initHitbox(ENEMY_HITBOX_W, ENEMY_HITBOX_H);
    }

    /**
     * Updates the state and behavior of the SkelMonsta enemy.
     *
     * @param playerModel the player model to interact with
     */
    @Override
    public void update(PlayerModel playerModel) {
        initLevelManager(); // Load the level manager if it's not loaded (enemies are created before the level manager use this method to avoid null pointer exceptions)

        updateState();
        updateTimer();
        calculateNextMove(playerModel);
        updateMove(playerModel);
        checkPlayerHit(playerModel);
    }

    /**
     * Checks if the SkelMonsta hits the player and handles the player's death if hit.
     *
     * <p>This method checks for collision between the SkelMonsta and the player. If a collision is detected,
     * it triggers the player's death and restarts the HurryUpManager.
     *
     * @param playerModel the player model to check for collision
     */
    private void checkPlayerHit(PlayerModel playerModel) {

        if (spawning || despawning || !playerModel.isActive())
            return;

        if (hitbox.intersects(playerModel.getHitbox())) {
            playerModel.death();
            HurryUpManagerModel.getInstance().restart();
        }
    }

    /**
     * Updates the state of the SkelMonsta, including spawning and despawning.
     */
    private void updateState() {

        if (EnemyManagerModel.getInstance().getActiveEnemiesCount() == 0 && !despawning)
            activateDespawn();

        if (spawning && spawningTimer <= 0) {
            spawning = false;
            moving = true;
            return;
        }

        if (despawning && despawningTimer <= 0)
            deactivate();
    }

    /**
     * Updates the timers for movement, spawning, and despawning.
     */
    private void updateTimer() {
        nextMoveTimer -= (int)  timer.getTimeDelta();

        if (spawning)
            spawningTimer -= (int)  timer.getTimeDelta();

        if (despawning)
            despawningTimer -= (int)  timer.getTimeDelta();
    }

    /**
     * Calculates the next move direction based on the player's position.
     *
     * @param playerModel the player model to determine the direction
     */
    private void calculateNextMove(PlayerModel playerModel) {

        if (nextMoveTimer <= 0 && !moving) {
            if (walkingDir != UP && walkingDir != DOWN)
                previousWalkingDir = walkingDir;

            walkingDir = getDirectionToPlayer(playerModel);
        }

        if (nextMoveTimer <= 0 && !spawning && !despawning)
            moving = true;
        else
            moving = false;
    }

    /**
     * Updates the movement of the SkelMonsta based on the current direction.
     *
     * @param playerModel the player model to interact with
     */
    private void updateMove(PlayerModel playerModel) {
        if (!moving)
            return;

        switch (walkingDir) {
            case UP -> moveOnYAxis(UP, playerModel);
            case DOWN -> moveOnYAxis(DOWN, playerModel);
            case LEFT -> moveOnXAxis(LEFT, playerModel);
            case RIGHT -> moveOnXAxis(RIGHT, playerModel);
        }

        updateWalkedDistance();
    }

    /**
     * Moves the SkelMonsta on the Y-axis, using the given direction.
     *
     * @param direction   the direction to move (UP or DOWN)
     * @param playerModel the player model to interact with
     */
    private void moveOnYAxis(Direction direction, PlayerModel playerModel) {
        if (playerModel.getTileY() == getTileY())
            return;

        switch (direction) {
            case UP -> hitbox.y -= NORMAL_WALK_SPEED;
            case DOWN -> hitbox.y += NORMAL_WALK_SPEED;
        }
    }

    /**
     * Moves the SkelMonsta on the X-axis, using the given direction.
     *
     * @param direction   the direction to move (LEFT or RIGHT)
     * @param playerModel the player model to interact with
     */
    private void moveOnXAxis(Direction direction, PlayerModel playerModel) {
        if (playerModel.getTileX() == getTileX())
            return;

        switch (direction) {
            case LEFT -> hitbox.x -= NORMAL_WALK_SPEED;
            case RIGHT -> hitbox.x += NORMAL_WALK_SPEED;
        }
    }

    /**
     * This method increments the distance walked by the SkelMonsta.
     *
     * <p> If the distance exceeds the maximum distance, the SkelMonsta pause the moving action.
     */
    private void updateWalkedDistance() {
        walkedDistance += NORMAL_WALK_SPEED;

        if (walkedDistance >= SKEL_MONSTA_MOVEMENT_MAX_DISTANCE)
            stopMove();
    }

    /**
     * Returns the direction to the player based on the player's position.
     *
     * @param playerModel the player model to determine the direction
     * @return the direction to the player
     */
    private Direction getDirectionToPlayer(PlayerModel playerModel) {
        Direction upOrDown = isPlayerUpOrDown(playerModel);
        Direction leftOrRight = isPlayerLeftOrRight(playerModel);

        if (upOrDown == UP)
            return UP;
        else if (upOrDown == DOWN)
            return DOWN;
        else if (leftOrRight == LEFT)
            return LEFT;
        else if (leftOrRight == RIGHT)
            return RIGHT;
        else
            return NONE;
    }

    /**
     * Pause the movement of the SkelMonsta and resets the move timer.
     */
    private void stopMove() {
        nextMoveTimer = SKEL_MONSTA_MOVEMENT_TIMER;
        walkedDistance = 0;
    }

    /**
     * Resets the SkelMonsta to its initial state.
     */
    public void reset() {
        active = false;

        hitbox.x = SKEL_MONSTA_SPAWN_X;
        hitbox.y = SKEL_MONSTA_SPAWN_Y;
        walkingDir = RIGHT;
        previousWalkingDir = RIGHT;

        nextMoveTimer = SKEL_MONSTA_MOVEMENT_TIMER;
        spawningTimer = SKEL_MONSTA_SPAWNING_TIMER;
        despawningTimer = SKEL_MONSTA_DESPAWNING_TIMER;

        spawning = true;
        moving = false;
        despawning = false;
        walkedDistance = 0;
    }

    /**
     * Activates the SkelMonsta, setting it to active state.
     */
    public void activate() {
        active = true;
    }

    /**
     * Deactivates the SkelMonsta, setting it to inactive state.
     */
    @Override
    public void deactivate(){
        active = false;
        nextMoveTimer = SKEL_MONSTA_MOVEMENT_TIMER;
        spawningTimer = SKEL_MONSTA_SPAWNING_TIMER;
        despawningTimer = SKEL_MONSTA_DESPAWNING_TIMER;

        HurryUpManagerModel.getInstance().newLevelReset();
    }

    /**
     * Activates the despawning state for the SkelMonsta.
     */
    public void activateDespawn() {
        despawning = true;
        moving = false;
    }

    /**
     * Gets the type of the enemy.
     *
     * @return the enemy type
     */
    @Override
    public EnemyType getEnemyType() {
        return SKEL_MONSTA;
    }

    /**
     * Checks if the SkelMonsta is in the despawning state.
     *
     * @return true if despawning, false otherwise
     */
    public boolean isDespawning() {
        return despawning;
    }

    /**
     * Checks if the SkelMonsta is in the spawning state.
     *
     * @return true if spawning, false otherwise
     */
    public boolean isSpawning() {
        return spawning;
    }

    /**
     * Checks if the SkelMonsta is moving.
     *
     * @return true if moving, false otherwise
     */
    public boolean isMoving() {
        return moving;
    }
}
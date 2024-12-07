package model.entities;

import model.Constants.Direction;

import static model.Constants.Direction.*;
import static model.Constants.EnemyConstants.*;
import static model.Constants.EnemyConstants.EnemyType.SKEL_MONSTA;

public class SkelMonstaModel extends EnemyModel {

    private float walkedDistance = 0;

    private boolean moving = false;
    private boolean spawning = true;
    private boolean despawning = false;

    private int nextMoveTimer = SKEL_MONSTA_MOVEMENT_TIMER;
    private int spawningTimer = SKEL_MONSTA_SPAWNING_TIMER;
    private int despawningTimer = SKEL_MONSTA_DESPAWNING_TIMER;

    public SkelMonstaModel() {
        super(SKEL_MONSTA_SPAWN_X, SKEL_MONSTA_SPAWN_Y, ENEMY_W, ENEMY_H, SKEL_MONSTA, RIGHT);

        // enemyModel custom variables for SkelMonsta
        active = false;
        y = SKEL_MONSTA_SPAWN_Y;    // Set the y position to the spawn point (the super constructor sets it outside the screen, but skelMonsta spawns from the ground)
        walkingDir = RIGHT;
        previousWalkingDir = RIGHT;
        initHitbox(ENEMY_HITBOX_W, ENEMY_HITBOX_H);
    }

    @Override
    public void update(PlayerModel playerModel) {
        loadLevelManager(); // Load the level manager if it's not loaded (enemies are created before the level manager use this method to avoid null pointer exceptions)

        updateState();
        updateTimer();
        calculateNextMove(playerModel);
        updateMove(playerModel);
        checkPlayerHit(playerModel);
    }

    private void checkPlayerHit(PlayerModel playerModel) {

        if (spawning || despawning || !playerModel.isActive())
            return;

        if (hitbox.intersects(playerModel.getHitbox())) {
            playerModel.death();
            HurryUpManagerModel.getInstance().restart();
        }
    }

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

    private void updateTimer() {
        nextMoveTimer -= (int)  timer.getTimeDelta();

        if (spawning)
            spawningTimer -= (int)  timer.getTimeDelta();

        if (despawning)
            despawningTimer -= (int)  timer.getTimeDelta();
    }

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

    private void moveOnYAxis(Direction direction, PlayerModel playerModel) {
        if (playerModel.getTileY() == getTileY())
            return;

        switch (direction) {
            case UP -> hitbox.y -= NORMAL_WALK_SPEED;
            case DOWN -> hitbox.y += NORMAL_WALK_SPEED;
        }
    }

    private void moveOnXAxis(Direction direction, PlayerModel playerModel) {
        if (playerModel.getTileX() == getTileX())
            return;

        switch (direction) {
            case LEFT -> hitbox.x -= NORMAL_WALK_SPEED;
            case RIGHT -> hitbox.x += NORMAL_WALK_SPEED;
        }
    }

    private void updateWalkedDistance() {
        walkedDistance += NORMAL_WALK_SPEED;

        if (walkedDistance >= SKEL_MONSTA_MOVEMENT_MAX_DISTANCE)
            stopMove();
    }

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

    private void stopMove() {
        nextMoveTimer = SKEL_MONSTA_MOVEMENT_TIMER;
        walkedDistance = 0;
    }

    public void activate() {
        active = true;
    }

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

    @Override
    public void deactivate(){
        active = false;
        nextMoveTimer = SKEL_MONSTA_MOVEMENT_TIMER;
        spawningTimer = SKEL_MONSTA_SPAWNING_TIMER;
        despawningTimer = SKEL_MONSTA_DESPAWNING_TIMER;

        HurryUpManagerModel.getInstance().newLevelReset();
    }

    public void activateDespawn() {
        despawning = true;
        moving = false;
    }

    @Override
    public EnemyType getEnemyType() {
        return SKEL_MONSTA;
    }

    public boolean isDespawning() {
        return despawning;
    }

    public boolean isSpawning() {
        return spawning;
    }

    public boolean isMoving() {
        return moving;
    }

}
package entities;

import bubbles.BubbleManager;
import levels.LevelManager;
import main.Game;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.HelpMethods.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.EnemyConstants.EnemyType;
import static utilz.Constants.Direction;
import static utilz.Constants.Direction.*;

public abstract class Enemy extends Entity {
    protected boolean active = true;
    protected boolean alive = true;
    protected int animationAction = WALKING_ANIMATION_NORMAL;
    protected int enemyState;
    protected EnemyType enemyType;
    protected boolean firstUpdate = true;

    // Enemy Movement Variables
    protected float xSpeed;
    protected float ySpeed;
    protected float fallSpeed;
    protected float flySpeed;
    protected float walkSpeed;
    protected Direction walkingDir;
    protected Direction startWalkingDir;

    // Spawn Info
    protected float spawnY;
    protected boolean reachedSpawn = false;

    // Player info
    protected int playerTileX, playerTileY;
    protected int updatePlayerPosMaxInterval;

    public Enemy(float x, float y, int width, int height, EnemyType  enemyType, Direction startWalkingDir) {
        super(x, INITIAL_SPAWN_POINT_Y, width, height);

        this.spawnY = y;
        this.enemyType = enemyType;
        this.walkingDir = startWalkingDir;
        this.startWalkingDir = startWalkingDir;
    }

    public abstract void update(Player player);

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(enemyType, enemyState)) {
                animationIndex = 0;
            }
        }
    }

    protected void updateSpawning() {

        // check if enemy will reach spawn point
        if (hitbox.y + SPAWN_TRANSITION_SPEED < spawnY) {
            hitbox.y += SPAWN_TRANSITION_SPEED;
            immune = true;
        }

        else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, SPAWN_TRANSITION_SPEED , LevelManager.getInstance().getCurrentLevel().getLevelData());
            reachedSpawn = true;
            immune = false;
        }
    }

    protected void updateStateVariables() {
        switch (enemyState) {
            case NORMAL_STATE:
                fallSpeed = NORMAL_FALL_SPEED;
                flySpeed = NORMAL_FLY_SPEED;
                walkSpeed = NORMAL_WALK_SPEED;
                animationAction = WALKING_ANIMATION_NORMAL;
                updatePlayerPosMaxInterval = NORMAL_PLAYER_INFO_MAX_UPDATE_INTERVAL;
                break;

            case HUNGRY_STATE:
                fallSpeed = HUNGRY_FALL_SPEED;
                flySpeed = HUNGRY_FLY_SPEED;
                walkSpeed = HUNGRY_WALK_SPEED;
                animationAction = WALKING_ANIMATION_HUNGRY;
                updatePlayerPosMaxInterval = HUNGRY_PLAYER_INFO_MAX_UPDATE_INTERVAL;
                break;

            case BOBBLE_STATE:
                animationAction = BOBBLE_GREEN_ANIMATION;
                break;

            case DEAD_STATE:
                active = false;
                animationAction = DEAD_ANIMATION;
                break;
        }
    }

    protected boolean canSeePlayer(int[][] levelData, Player player) {
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);

        return playerTileY == getTileY()                                             // Same row
                && isPlayerInViewingRange(player)                               // Player is in range
                && IsSightClear(levelData, hitbox, player.hitbox, getTileY());       // No obstacles in the way
    }

    protected boolean isPlayerInViewingRange(Player player) {
        return Math.abs(player.getHitbox().x - hitbox.x) <= VIEWING_RANGE;
    }

    protected void calculatePlayersPos(Player player) {
        playerTileX = (int) (player.getHitbox().x / Game.TILES_SIZE);
        playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE);
    }

    protected void changeWalkingDir() {
        if (walkingDir == LEFT)
            walkingDir = RIGHT;
        else
            walkingDir = LEFT;
    }

    protected int flipX() {
        if (walkingDir == RIGHT)
            return width;
        else
            return 0;
    }

    protected int flipW() {
        if (walkingDir == RIGHT)
            return -1;
        else
            return 1;
    }

    protected void updateWalkingDir() {
        if (playerTileX < getTileX())
            walkingDir = LEFT;
        else if (playerTileX > getTileX())
            walkingDir = RIGHT;
    }

    protected Direction isPlayerLeftOrRight(Player player) {
        if (player.getTileX() < getTileX())
            return LEFT;
        else if (player.getTileX() > getTileX())
            return RIGHT;
        else
            return NONE;
    }

    protected Direction isPlayerUpOrDown(Player player) {
        if (player.getTileY() < getTileY())
            return UP;
        else if (player.getTileY() > getTileY())
            return DOWN;
        else
            return NONE;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        animationIndex = 0;
        animationTick = 0;
        firstUpdate = true;
        active = true;
        enemyState = NORMAL_STATE;
        walkingDir = startWalkingDir;
    }

    public void bubbleCapture() {
        active = false;
        enemyState = BOBBLE_STATE;
        animationIndex = 0;
        animationTick = 0;
    }

    public void death(Player player) {
        active = false;
        enemyState = DEAD_STATE;
        BubbleManager.getInstance().addDeadEnemy(this, player);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setEnemyState(int state) {
        this.enemyState = state;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public boolean getReachedSpawn() {
        return reachedSpawn;
    }

    public abstract EnemyType getEnemyType();
}
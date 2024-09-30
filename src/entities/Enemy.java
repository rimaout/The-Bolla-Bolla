package entities;

import bubbles.playerBubbles.PlayerBubblesManager;
import bubbles.playerBubbles.EnemyBubble;
import levels.LevelManager;
import main.Game;

import static utilz.Constants.Direction;
import static utilz.Constants.Direction.*;
import static utilz.HelpMethods.IsSightClear;
import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.GetEntityYPosAboveFloor;

public abstract class Enemy extends Entity {

    protected boolean active = true;
    protected boolean alive = true;
    protected int animationAction = WALKING_ANIMATION_NORMAL;
    protected float animationSpeedMultiplier = NORMAL_ANIMATION_SPEED_MULTIPLIER;
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

    public Enemy(float x, float y, int width, int height, EnemyType  enemyType, Direction startWalkingDir) {
        super(x, INITIAL_SPAWN_POINT_Y, width, height);

        this.spawnY = y;
        this.enemyType = enemyType;
        this.walkingDir = startWalkingDir;
        this.startWalkingDir = startWalkingDir;
    }

    public abstract void update(Player player);

    protected void loadLevelManager() {
        // Load the level manager if it's not loaded (enemies are created before the level manager use this method to avoid null pointer exceptions)
        // use this method at the beginning of the update method for each enemy class

        if (levelManager == null)
            levelManager = LevelManager.getInstance();
    }

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED * animationSpeedMultiplier) {
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
            hitbox.y = GetEntityYPosAboveFloor(hitbox, SPAWN_TRANSITION_SPEED , levelManager.getLevelData());
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
                animationSpeedMultiplier = NORMAL_ANIMATION_SPEED_MULTIPLIER;
                break;

            case HUNGRY_STATE:
                fallSpeed = HUNGRY_FALL_SPEED;
                flySpeed = HUNGRY_FLY_SPEED;
                walkSpeed = HUNGRY_WALK_SPEED;
                animationAction = WALKING_ANIMATION_HUNGRY;
                updatePlayerPosMaxInterval = HUNGRY_PLAYER_INFO_MAX_UPDATE_INTERVAL;
                animationSpeedMultiplier = HUNGRY_ANIMATION_SPEED_MULTIPLIER;
                break;

            case BOBBLE_STATE:
                animationAction = BOBBLE_GREEN_ANIMATION;
                animationSpeedMultiplier = NORMAL_ANIMATION_SPEED_MULTIPLIER;
                break;

            case DEAD_STATE:
                active = false;
                animationAction = DEAD_ANIMATION;
                animationSpeedMultiplier = NORMAL_ANIMATION_SPEED_MULTIPLIER;
                break;
        }
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
        Direction direction = switch (walkingDir) {
            case UP, DOWN -> previousWalkingDir;
            default -> walkingDir;
        };

        return (direction == RIGHT) ? width : 0;
    }

    protected int flipW() {
        Direction direction = switch (walkingDir) {
            case UP, DOWN -> previousWalkingDir;
            default -> walkingDir;
        };

        return (direction == RIGHT) ? -1 : 1;
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

    protected boolean canFall(){
        // check if the under is not out of the level
        return getTileY() + 1 < Game.TILES_IN_HEIGHT - 1;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        animationIndex = 0;
        animationTick = 0;
        active = true;
        enemyState = NORMAL_STATE;
        walkingDir = startWalkingDir;
    }

    public void bubbleCapture(Direction direction) {

        PlayerBubblesManager.getInstance().addBubble(new EnemyBubble(this, hitbox.x, hitbox.y, direction));
        active = false;
        enemyState = BOBBLE_STATE;
        animationIndex = 0;
        animationTick = 0;
    }

    public void instantKill(Player player) {
        active = false;
        enemyState = DEAD_STATE;
        PlayerBubblesManager.getInstance().addDeadEnemy(this, player);
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
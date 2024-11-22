package model.entities;

import bubbles.playerBubbles.PlayerBubblesManager;
import bubbles.playerBubbles.EnemyBubble;
import model.levels.LevelManagerModel;
import model.utilz.Constants;

import static model.utilz.Constants.Direction;
import static model.utilz.Constants.Direction.*;
import static model.utilz.Constants.EnemyConstants.*;
import static model.utilz.HelpMethods.GetEntityYPosAboveFloor;

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

    public EnemyModel(float x, float y, int width, int height, EnemyType  enemyType, Direction startWalkingDir) {
        super(x, INITIAL_SPAWN_POINT_Y, width, height);

        this.spawnY = y;
        this.enemyType = enemyType;
        this.walkingDir = startWalkingDir;
        this.startWalkingDir = startWalkingDir;
    }

    public abstract void update(PlayerModel playerModel);

    protected void loadLevelManager() {
        // Load the level manager if it's not loaded (enemies are created before the level manager use this method to avoid null pointer exceptions)
        // use this method at the beginning of the update method for each enemy class

        if (levelManagerModel == null)
            levelManagerModel = LevelManagerModel.getInstance();
    }

    protected void updateSpawning() {

        // check if enemy will reach spawn point
        if (hitbox.y + SPAWN_TRANSITION_SPEED < spawnY) {
            hitbox.y += SPAWN_TRANSITION_SPEED;
            immune = true;
        }

        else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, SPAWN_TRANSITION_SPEED , levelManagerModel.getLevelData());
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

    protected boolean isPlayerInViewingRange(PlayerModel playerModel) {
        return Math.abs(playerModel.getHitbox().x - hitbox.x) <= VIEWING_RANGE;
    }

    protected void calculatePlayersPos(PlayerModel playerModel) {
        playerTileX = (int) (playerModel.getHitbox().x / Constants.TILES_SIZE);
        playerTileY = (int)(playerModel.getHitbox().y / Constants.TILES_SIZE);
    }

    protected void changeWalkingDir() {
        if (walkingDir == LEFT)
            walkingDir = RIGHT;
        else
            walkingDir = LEFT;
    }

    public int flipX() {
        Direction direction = switch (walkingDir) {
            case UP, DOWN -> previousWalkingDir;
            default -> walkingDir;
        };

        return (direction == RIGHT) ? width : 0;
    }

    public int flipW() {
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

    protected Direction isPlayerLeftOrRight(PlayerModel playerModel) {
        if (playerModel.getTileX() < getTileX())
            return LEFT;
        else if (playerModel.getTileX() > getTileX())
            return RIGHT;
        else
            return NONE;
    }

    protected Direction isPlayerUpOrDown(PlayerModel playerModel) {
        if (playerModel.getTileY() < getTileY())
            return UP;
        else if (playerModel.getTileY() > getTileY())
            return DOWN;
        else
            return NONE;
    }

    protected boolean canFall(){
        // check if the under is not out of the level
        return getTileY() + 1 < Constants.TILES_IN_HEIGHT - 1;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        //animationIndex = 0;   //todo: check if is ok with out this two
        //animationTick = 0;
        active = true;
        enemyState = NORMAL_STATE;
        walkingDir = startWalkingDir;
    }

    public void bubbleCapture(Direction direction) {

        PlayerBubblesManager.getInstance().addBubble(new EnemyBubble(this, hitbox.x, hitbox.y, direction));
        active = false;
        enemyState = BOBBLE_STATE;
        //animationIndex = 0;   //todo: check if it ok with this two
        //animationTick = 0;
    }

    public void instantKill(PlayerModel playerModel) {
        active = false;
        enemyState = DEAD_STATE;
        PlayerBubblesManager.getInstance().addDeadEnemy(this, playerModel);
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
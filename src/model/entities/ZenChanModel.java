package model.entities;

import model.utilz.Constants;
import model.utilz.Constants.Direction;

import static model.utilz.HelpMethods.*;
import static model.utilz.Constants.GRAVITY;
import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.Direction.RIGHT;
import static model.utilz.Constants.EnemyConstants.*;
import static model.utilz.Constants.EnemyConstants.EnemyType.ZEN_CHAN;

/**
 * ZenChanModel class is responsible for the ZenChan enemy logic
 */
public class ZenChanModel extends EnemyModel {
    private boolean firstUpdate = true;

    // Fly Variables
    private int flyDirectionChangeCounter = 0;
    private boolean isFlyingFirstUpdate = true;
    private double flyStartTime = 0;
    private boolean didFlyInsideSolid = false;

    // Player Info Update variables
    private int playerUpdateTimer;

    // Jump Variables
    private int jumpDistance = 0;

    /**
     * Constructs a new ZenChanModel with the specified position and starting direction.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param startWalkingDir starting walking direction
     */
    public ZenChanModel(float x, float y, Direction startWalkingDir) {
        super(x, y, ENEMY_W, ENEMY_H, ZEN_CHAN, startWalkingDir);
        this.startWalkingDir = startWalkingDir;
        initHitbox(ENEMY_HITBOX_W, ENEMY_HITBOX_H);
    }

    /**
     * Updates the state of the ZenChan logic and behavior.
     *
     * @param playerModel the player model used to update the ZenChan's behavior
     */
    @Override
    public void update(PlayerModel playerModel) {
        initLevelManager(); // Load the level manager if it's not loaded (enemies are created before the level manager use this method to avoid null pointer exceptions)

        if (!EnemyManagerModel.getInstance().didAllEnemiesReachedSpawn()) {
            if (!reachedSpawn)
                updateSpawning();
            return;
        }

        if (firstUpdate)
            firstUpdate();

        updateTimers();
        updatePlayerInfo(playerModel);
        updateMove();
        updateStateVariables();
    }

    /**
     * Performs the first update.
     *
     * <p>If the ZenChan is not on the floor, it sets the goDown flag to true.
     */
    private void firstUpdate() {
        if (!IsEntityOnFloor(hitbox, levelManagerModel.getLevelTileData()))
            goDown = true;

        firstUpdate = false;
    }

    /**
     * Updates the timers; these timers are used to decide when to update the player's position
     */
    private void updateTimers() {
        playerUpdateTimer -= (int) timer.getTimeDelta();
    }

    /**
     * Updates the movement of the ZenChan enemy.
     *
     * <p>This method handles the logic for moving the ZenChan enemy, including falling, flying, jumping,
     * and moving on the ground. It also checks if the ZenChan is stuck in a wall and adjusts its position accordingly.
     */
    private void updateMove() {
        if (!IsEntityInsideMap(hitbox))
            pacManEffect();

        if(!IsEntityOnFloor(hitbox, levelManagerModel.getLevelTileData()) && !isJumping && !goUp && !goDown)
            goOnFloor();

        if (isFalling) {
            fall();
            return;
        }
        if(goUp){
            fly();
            return;
        }
        if(isJumping){
            jump(jumpDistance);
            return;
        }

        // enemy stuck in a wall
        if(IsEntityInsideSolid(hitbox, levelManagerModel.getLevelTileData()))
            hitbox.y += 1;

        moveOnGround();

        //Go up
        if (playerTileY < getTileY() && canFly()) {
            goUp = true;
            goDown = false;
        }

        //Go down
        if (playerTileY > getTileY()) {
            goDown = true;
            goUp = false;
        }
    }

    /**
     * Moves the ZenChan enemy on the ground.
     *
     * <p>This method handles the logic for moving the ZenChan enemy on the ground, including checking for obstacles,
     * handling falling, and initiating jumps if necessary.
     */
    private void moveOnGround() {
        if (walkingDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        // check if there is a solid tile in front of the enemy
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelManagerModel.getLevelTileData())) {

            // check if there is a solid tile below the enemy
            if (!isNextPosFloorSolid()) {

                if(goDown){
                    if(!canFall()){
                        goDown = false;
                        return;
                    }

                    hitbox.x += xSpeed;
                    isFalling = true;
                    goDown = false;
                    fall();
                    return;
                }

                jumpDistance = calculateJumpDistance();

                if(canJump(jumpDistance)) {
                    isJumping = true;
                    ySpeed = JUMP_Y_SPEED;
                    jump(jumpDistance);
                    return;
                }
                changeWalkingDir();
            }

            hitbox.x += xSpeed;
        }
        else
            changeWalkingDir();
    }

    /**
     * Makes the iZenChan enemy fly (going up to next platform).
     */
    private void fly() {

        if(isFlyingFirstUpdate){
            // wait half a second before flying
            flyStartTime = System.currentTimeMillis();
            isFlyingFirstUpdate = false;
            changeWalkingDir();
            flyDirectionChangeCounter++;
        }

        if(System.currentTimeMillis() - flyStartTime < 800)
            return;

        if (flyDirectionChangeCounter == 1) {
            changeWalkingDir();
            flyDirectionChangeCounter++;
        }

        if(System.currentTimeMillis() - flyStartTime < 1000)
            return;

        if(IsEntityInsideSolid(hitbox, levelManagerModel.getLevelTileData())){
            didFlyInsideSolid = true;
            hitbox.y -= flySpeed;
        }
        else if(didFlyInsideSolid){

            // fly ended
            hitbox.y = GetEntityYPosAboveFloor(hitbox, flySpeed, levelManagerModel.getLevelTileData()) - 1;
            updateWalkingDir();

            // Reset fly variables
            goUp = false;
            isFlyingFirstUpdate = true;
            didFlyInsideSolid = false;
            flyDirectionChangeCounter = 0;
        }
        else{
            hitbox.y -= flySpeed;
        }
    }

    /**
     * Makes the ZenChan enemy jump, in particular handles the different stages of the jump, such as going up, going down, and colliding with objects.
     *
     * @param jumpDistance the distance the ZenChan will jump
     */
    private void jump(int jumpDistance) {
        float jumpXSpeed;

        switch (walkingDir) {
            case LEFT -> jumpXSpeed = -JUMP_X_SPEED;
            case RIGHT -> jumpXSpeed = JUMP_X_SPEED;
            default -> jumpXSpeed = 0;
        }

        // long jump, increase speed
        if (jumpDistance > 6)
            jumpXSpeed *= 1.3f;

        // Going up
        if (ySpeed < 0){
            hitbox.y += ySpeed;
            ySpeed += GRAVITY;
            updateXPos(jumpXSpeed);
        }

        // Going down
        else if (ySpeed <= -JUMP_Y_SPEED){
            if (CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, levelManagerModel.getLevelTileData())) {
                hitbox.y += ySpeed;
                ySpeed += GRAVITY;
                updateXPos(jumpXSpeed);
            } else {
                isJumping = false;
                hitbox.y = GetEntityYPosAboveFloor(hitbox, ySpeed, levelManagerModel.getLevelTileData());
                updateXPos(jumpXSpeed);
            }
        } else {
            isJumping = false;
            updateXPos(jumpXSpeed);
        }
    }

    /**
     * Handles the falling logic for the ZenChan enemy.
     *
     * <p>This method updates the y-coordinate position of the ZenChan while it is falling.
     * If the ZenChan reaches the ground, it stops falling and adjusts its position accordingly.
     */
    private void fall() {
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelManagerModel.getLevelTileData()))
            hitbox.y += fallSpeed;
        else {
            // fall ended
            hitbox.y = GetEntityYPosAboveFloor(hitbox, fallSpeed, levelManagerModel.getLevelTileData());
            isFalling = false;
         }
    }

    /**
     * Accurately positions the ZenChan enemy on the floor.
     */
    private void goOnFloor() {
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelManagerModel.getLevelTileData()))
            hitbox.y += fallSpeed;
        else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, fallSpeed, levelManagerModel.getLevelTileData());
            goDown = false;
        }
    }


    /**
     * Checks if the next two tiles in front of the enemy are solid.
     *
     * <p>This method determines if the enemy can jump or fall by checking if the next two tiles
     * in front of the enemy are solid.
     *
     * @return true if the next two tiles are solid, false otherwise
     */
    private boolean isNextPosFloorSolid() {
        // This method checks if the next two tiles in front of the enemy are solid or not
        // It's used to check if the enemy can jump/fall or not

        float yPos =  hitbox.y + hitbox.height + 1;
        float xPos1 = 0, xPos2 = 0;

        //check if there are 2 solid tiles in front of the enemy
        if (walkingDir == LEFT) {
            xPos1 = hitbox.x - 1;
            xPos2 = hitbox.x - Constants.TILES_SIZE - 1;
        }

        else if (walkingDir == RIGHT) {
            xPos1 = hitbox.x + hitbox.width + 1;
            xPos2 = hitbox.x + hitbox.width + Constants.TILES_SIZE + 1;
        }

        return IsSolid(xPos1, yPos, levelManagerModel.getLevelTileData()) && IsSolid(xPos2, yPos, levelManagerModel.getLevelTileData());
    }

    /**
     * Checks if the ZenChan enemy can jump the specified distance.
     *
     * @param jumpDistance the distance the ZenChan will jump
     * @return true if the ZenChan can jump the specified distance, false otherwise
     */
    private boolean canJump(int jumpDistance) {
        return jumpDistance != -1;
    }

    /**
     * Calculates the jump distance for the ZenChan enemy.
     *
     * <p>This method determines the distance the ZenChan can jump by checking for the presence of a perimeter wall
     * and solid tiles within a certain range. It returns the distance to the nearest obstacle or floor.
     *
     * @return the distance the ZenChan can jump, or -1 if no valid jump distance is found
     */
    private int calculateJumpDistance() {
        int tileDistanceToPerimeterWall = -1;
        int tileDistanceToFloor = -1;

        int yFlorTile = (int) (hitbox.y + hitbox.height + 1) / Constants.TILES_SIZE;

        // Find the distance to the perimeter wall (max distance 5 tiles)
        if (walkingDir == LEFT) {
            for (int i = 2; i < 8; i++)
                if (IsTilePerimeterWall(getTileX() - i)) {
                    tileDistanceToPerimeterWall = i;
                    break;
                }
        }

        else if (walkingDir == RIGHT) {
            for (int i = 2; i < 8; i++)
                if (IsTilePerimeterWall(getTileX() + i)) {
                    tileDistanceToPerimeterWall = i;
                    break;
                }
        }

        // check if between 2 and 6 tiles there is a floor
        if (walkingDir == LEFT) {
            for (int i = 2; i < 8; i++)
                if (IsTileSolid(getTileX() - i, yFlorTile, levelManagerModel.getLevelTileData())) {
                    tileDistanceToFloor = i;
                    break;
                }
        }

        else if (walkingDir == RIGHT) {
            for (int i = 2; i  < 8 ; i++)
                if (IsTileSolid(getTileX() + i +1 , yFlorTile, levelManagerModel.getLevelTileData())) {
                    tileDistanceToFloor = i + 1;
                    break;
                }
        }

        // if value is -1, there is no floor or perimeter wall in the range
        if (tileDistanceToFloor == -1)
            return -1;

        if (tileDistanceToPerimeterWall == -1)
            return tileDistanceToFloor;

        if (tileDistanceToPerimeterWall > tileDistanceToFloor)
            return tileDistanceToFloor;

        return -1;
    }

    /**
     * Checks if the ZenChan enemy can fly.
     *
     * <p>This method checks if there is a solid tile to fly on, and if there is, it checks if there is an empty tile on top.
     * It determines if the ZenChan can fly by checking for solid tiles above the enemy within a certain range.
     *
     * @return true if the ZenChan can fly, false otherwise
     */
    private boolean canFly(){

        // check if there is a ceiling above (if there isn't a solid in 3 tiles --> can't fly)

        int oneTileAbove = getTileY()-1;
        boolean oneUpSolid = IsTileSolid(getTileX(), oneTileAbove, levelManagerModel.getLevelTileData()) &&  IsTileSolid(getTileX()+1, oneTileAbove, levelManagerModel.getLevelTileData());

        int twoTilesAbove = getTileY()-2;
        boolean twoUpSolid = IsTileSolid(getTileX(), twoTilesAbove, levelManagerModel.getLevelTileData()) &&  IsTileSolid(getTileX()+1, twoTilesAbove, levelManagerModel.getLevelTileData());
        boolean twoUpEmpty = !IsTileSolid(getTileX(), twoTilesAbove, levelManagerModel.getLevelTileData()) &&  IsTileSolid(getTileX()+1, twoTilesAbove, levelManagerModel.getLevelTileData());

        int threeTilesAbove = getTileY()-3;
        boolean threeUpSolid = IsTileSolid(getTileX(), threeTilesAbove, levelManagerModel.getLevelTileData()) && IsTileSolid(getTileX()+1, threeTilesAbove, levelManagerModel.getLevelTileData());
        boolean threeUpEmpty = !IsTileSolid(getTileX(), threeTilesAbove, levelManagerModel.getLevelTileData()) || IsTileSolid(getTileX()+1, threeTilesAbove, levelManagerModel.getLevelTileData());

        int fourTilesAbove = getTileY()-4;
        boolean fourUpEmpty = !IsTileSolid(getTileX(), fourTilesAbove, levelManagerModel.getLevelTileData()) || IsTileSolid(getTileX()+1, fourTilesAbove, levelManagerModel.getLevelTileData());

        return (oneUpSolid && twoUpEmpty) || (twoUpSolid && threeUpEmpty) || (threeUpSolid && fourUpEmpty);
    }

    /**
     * Updates the player's information at random intervals between 0 to 8 seconds.
     *
     * <p>This method calculates the player's position and updates the playerUpdateTimer
     * to a random value within the specified interval.
     *
     * @param playerModel the player model used to update the player's position
     */
    private void updatePlayerInfo(PlayerModel playerModel){

        if (playerUpdateTimer <= 0) {
            calculatePlayersPos(playerModel);
            playerUpdateTimer = (int) (Math.random() * updatePlayerPosMaxInterval);
        }
    }

    /**
     * Resets the state of the ZenChan enemy to his default state.
     */
    @Override
    public void resetEnemy() {
        super.resetEnemy();

        isFalling = false;
        isJumping = false;
        goUp = false;
        goDown = false;
        isFlyingFirstUpdate = true;
        flyDirectionChangeCounter = 0;
        flyStartTime = 0;
        didFlyInsideSolid = false;
        playerUpdateTimer = 0;
    }

    /**
     * Captures the ZenChan enemy in a bubble.
     *
     * <p>This method is called when the ZenChan enemy is captured in a bubble. It resets various state variables
     * of the ZenChan enemy, including flags for falling, jumping, and flying.
     *
     * <p>It also calls the superclass's bubbleCapture method, that created a {@link model.bubbles.playerBubbles.EnemyBubbleModel} object with inside the ZenChan enemy.
     *
     * @param direction the direction in which the bubble is moving
     */
    @Override
    public void bubbleCapture(Direction direction) {
        super.bubbleCapture(direction);
        
        isFalling = false;
        isJumping = false;
        goUp = false;
        goDown = false;
        isFlyingFirstUpdate = true;
        flyDirectionChangeCounter = 0;
        flyStartTime = 0;
        didFlyInsideSolid = false;
        playerUpdateTimer = 0;
    }

    /**
     * Returns the type of the enemy.
     *
     * <p>This method overrides the getEnemyType method from the superclass to return the specific type of the enemy, which is ZEN_CHAN.
     *
     * @return the type of the enemy
     */
    @Override
    public EnemyType getEnemyType() {
        return ZEN_CHAN;
    }
}
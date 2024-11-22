package model.entities;

import model.utilz.Constants;
import model.utilz.Constants.Direction;

import static model.utilz.HelpMethods.*;
import static model.utilz.Constants.GRAVITY;
import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.Direction.RIGHT;
import static model.utilz.Constants.EnemyConstants.*;
import static model.utilz.Constants.EnemyConstants.EnemyType.ZEN_CHAN;

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

    public ZenChanModel(float x, float y, Direction startWalkingDir) {
        super(x, y, ENEMY_W, ENEMY_H, ZEN_CHAN, startWalkingDir);
        this.startWalkingDir = startWalkingDir;
        initHitbox(ENEMY_HITBOX_W, ENEMY_HITBOX_H);
    }

    @Override
    public void update(PlayerModel playerModel) {
        loadLevelManager(); // Load the level manager if it's not loaded (enemies are created before the level manager use this method to avoid null pointer exceptions)

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

    private void firstUpdate() {
        if (!IsEntityOnFloor(hitbox, levelManagerModel.getLevelData()))
            goDown = true;

        firstUpdate = false;
    }

    private void updateTimers() {
        playerUpdateTimer -= (int) timer.getTimeDelta();
    }

    private void updateMove() {

        if(!IsEntityOnFloor(hitbox, levelManagerModel.getLevelData()) && !isJumping && !goUp && !goDown)
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
        if(IsEntityInsideSolid(hitbox, levelManagerModel.getLevelData()))
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

    private void moveOnGround() {
        if (walkingDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        // check if there is a solid tile in front of the enemy
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelManagerModel.getLevelData())) {

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

        if(IsEntityInsideSolid(hitbox, levelManagerModel.getLevelData())){
            didFlyInsideSolid = true;
            hitbox.y -= flySpeed;
        }
        else if(didFlyInsideSolid){

            // fly ended
            hitbox.y = GetEntityYPosAboveFloor(hitbox, flySpeed, levelManagerModel.getLevelData()) - 1;
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

    private void jump(int jumpDistance) {
        float jumpXSpeed;

        switch (walkingDir) {
            case LEFT -> jumpXSpeed = -JUMP_X_SPEED;
            case RIGHT -> jumpXSpeed = JUMP_X_SPEED;
            default -> jumpXSpeed = 0;
        }

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
            if (CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, levelManagerModel.getLevelData())) {
                hitbox.y += ySpeed;
                ySpeed += GRAVITY;
                updateXPos(jumpXSpeed);
            } else {
                isJumping = false;
                hitbox.y = GetEntityYPosAboveFloor(hitbox, ySpeed, levelManagerModel.getLevelData());
                updateXPos(jumpXSpeed);
            }
        } else {
            isJumping = false;
            updateXPos(jumpXSpeed);
        }
    }

    private void fall() {
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelManagerModel.getLevelData()))
            hitbox.y += fallSpeed;
        else {
            // fall ended
            hitbox.y = GetEntityYPosAboveFloor(hitbox, fallSpeed, levelManagerModel.getLevelData());
            isFalling = false;
         }
    }

    private void goOnFloor() {
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelManagerModel.getLevelData()))
            hitbox.y += fallSpeed;
        else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, fallSpeed, levelManagerModel.getLevelData());
            goDown = false;
        }
    }

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

        return IsSolid(xPos1, yPos, levelManagerModel.getLevelData()) && IsSolid(xPos2, yPos, levelManagerModel.getLevelData());
    }

    private boolean canJump(int jumpDistance) {
        return jumpDistance != -1;
    }

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
                if (IsTileSolid(getTileX() - i, yFlorTile, levelManagerModel.getLevelData())) {
                    tileDistanceToFloor = i;
                    break;
                }
        }

        else if (walkingDir == RIGHT) {
            for (int i = 2; i  < 8 ; i++)
                if (IsTileSolid(getTileX() + i +1 , yFlorTile, levelManagerModel.getLevelData())) {
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


    private boolean canFly(){

        // TODO: Refactor - this method checks fi there is a solid tile to fly on, ig there is checks if there is a empty tile on top

        // check if there is a ceiling above (if there isn't a solid in 3 tiles --> can't fly)

        int oneTileAbove = getTileY()-1;
        boolean oneUpSolid = IsTileSolid(getTileX(), oneTileAbove, levelManagerModel.getLevelData()) &&  IsTileSolid(getTileX()+1, oneTileAbove, levelManagerModel.getLevelData());

        int twoTilesAbove = getTileY()-2;
        boolean twoUpSolid = IsTileSolid(getTileX(), twoTilesAbove, levelManagerModel.getLevelData()) &&  IsTileSolid(getTileX()+1, twoTilesAbove, levelManagerModel.getLevelData());
        boolean twoUpEmpty = !IsTileSolid(getTileX(), twoTilesAbove, levelManagerModel.getLevelData()) &&  IsTileSolid(getTileX()+1, twoTilesAbove, levelManagerModel.getLevelData());

        int threeTilesAbove = getTileY()-3;
        boolean threeUpSolid = IsTileSolid(getTileX(), threeTilesAbove, levelManagerModel.getLevelData()) && IsTileSolid(getTileX()+1, threeTilesAbove, levelManagerModel.getLevelData());
        boolean threeUpEmpty = !IsTileSolid(getTileX(), threeTilesAbove, levelManagerModel.getLevelData()) || IsTileSolid(getTileX()+1, threeTilesAbove, levelManagerModel.getLevelData());

        int fourTilesAbove = getTileY()-4;
        boolean fourUpEmpty = !IsTileSolid(getTileX(), fourTilesAbove, levelManagerModel.getLevelData()) || IsTileSolid(getTileX()+1, fourTilesAbove, levelManagerModel.getLevelData());

        return (oneUpSolid && twoUpEmpty) || (twoUpSolid && threeUpEmpty) || (threeUpSolid && fourUpEmpty);
    }

    private void updatePlayerInfo(PlayerModel playerModel){

        if (playerUpdateTimer <= 0) {
            calculatePlayersPos(playerModel);
            playerUpdateTimer = (int) (Math.random() * updatePlayerPosMaxInterval);
        }
    }

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

    @Override
    public EnemyType getEnemyType() {
        return ZEN_CHAN;
    }
}
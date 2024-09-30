package entities;

import main.Game;
import utilz.Constants.Direction;

import static utilz.HelpMethods.*;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.Direction.LEFT;
import static utilz.Constants.Direction.RIGHT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.EnemyConstants.EnemyType.ZEN_CHAN;

public class ZenChan extends Enemy {
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

    public ZenChan(float x, float y, Direction startWalkingDir) {
        super(x, y, ENEMY_W, ENEMY_H, ZEN_CHAN, startWalkingDir);
        this.startWalkingDir = startWalkingDir;
        initHitbox(ENEMY_HITBOX_W, ENEMY_HITBOX_H);
    }

    @Override
    public void update(Player player) {
        loadLevelManager(); // Load the level manager if it's not loaded (enemies are created before the level manager use this method to avoid null pointer exceptions)

        updateAnimationTick();

        if (!EnemyManager.getInstance().didAllEnemiesReachedSpawn()) {
            if (!reachedSpawn)
                updateSpawning();
            return;
        }

        if (firstUpdate)
            firstUpdate();

        updateTimers();
        updatePlayerInfo(player);
        updateMove();
        updateStateVariables();
    }

    private void firstUpdate() {
        if (!IsEntityOnFloor(hitbox, levelManager.getLevelData()))
            goDown = true;

        firstUpdate = false;
    }

    private void updateTimers() {
        playerUpdateTimer -= (int) timer.getTimeDelta();
    }

    private void updateMove() {

        if(!IsEntityOnFloor(hitbox, levelManager.getLevelData()) && !isJumping && !goUp && !goDown)
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
        if(IsEntityInsideSolid(hitbox, levelManager.getLevelData()))
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
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelManager.getLevelData())) {

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

        if(IsEntityInsideSolid(hitbox, levelManager.getLevelData())){
            didFlyInsideSolid = true;
            hitbox.y -= flySpeed;
        }
        else if(didFlyInsideSolid){

            // fly ended
            hitbox.y = GetEntityYPosAboveFloor(hitbox, flySpeed, levelManager.getLevelData()) - 1;
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
            if (CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, levelManager.getLevelData())) {
                hitbox.y += ySpeed;
                ySpeed += GRAVITY;
                updateXPos(jumpXSpeed);
            } else {
                isJumping = false;
                hitbox.y = GetEntityYPosAboveFloor(hitbox, ySpeed, levelManager.getLevelData());
                updateXPos(jumpXSpeed);
            }
        } else {
            isJumping = false;
            updateXPos(jumpXSpeed);
        }
    }

    private void fall() {
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelManager.getLevelData()))
            hitbox.y += fallSpeed;
        else {
            // fall ended
            hitbox.y = GetEntityYPosAboveFloor(hitbox, fallSpeed, levelManager.getLevelData());
            isFalling = false;
         }
    }

    private void goOnFloor() {
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelManager.getLevelData()))
            hitbox.y += fallSpeed;
        else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, fallSpeed, levelManager.getLevelData());
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
            xPos2 = hitbox.x - Game.TILES_SIZE - 1;
        }

        else if (walkingDir == RIGHT) {
            xPos1 = hitbox.x + hitbox.width + 1;
            xPos2 = hitbox.x + hitbox.width + Game.TILES_SIZE + 1;
        }

        return IsSolid(xPos1, yPos,levelManager.getLevelData()) && IsSolid(xPos2, yPos, levelManager.getLevelData());
    }

    private boolean canJump(int jumpDistance) {
        return jumpDistance != -1;
    }

    private int calculateJumpDistance() {
        int tileDistanceToPerimeterWall = -1;
        int tileDistanceToFloor = -1;

        int yFlorTile = (int) (hitbox.y + hitbox.height + 1) / Game.TILES_SIZE;

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
                if (IsTileSolid(getTileX() - i, yFlorTile, levelManager.getLevelData())) {
                    tileDistanceToFloor = i;
                    break;
                }
        }

        else if (walkingDir == RIGHT) {
            for (int i = 2; i  < 8 ; i++)
                if (IsTileSolid(getTileX() + i +1 , yFlorTile, levelManager.getLevelData())) {
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
        boolean oneUpSolid = IsTileSolid(getTileX(), oneTileAbove, levelManager.getLevelData()) &&  IsTileSolid(getTileX()+1, oneTileAbove, levelManager.getLevelData());

        int twoTilesAbove = getTileY()-2;
        boolean twoUpSolid = IsTileSolid(getTileX(), twoTilesAbove, levelManager.getLevelData()) &&  IsTileSolid(getTileX()+1, twoTilesAbove, levelManager.getLevelData());
        boolean twoUpEmpty = !IsTileSolid(getTileX(), twoTilesAbove, levelManager.getLevelData()) &&  IsTileSolid(getTileX()+1, twoTilesAbove, levelManager.getLevelData());

        int threeTilesAbove = getTileY()-3;
        boolean threeUpSolid = IsTileSolid(getTileX(), threeTilesAbove, levelManager.getLevelData()) && IsTileSolid(getTileX()+1, threeTilesAbove, levelManager.getLevelData());
        boolean threeUpEmpty = !IsTileSolid(getTileX(), threeTilesAbove, levelManager.getLevelData()) || IsTileSolid(getTileX()+1, threeTilesAbove, levelManager.getLevelData());

        int fourTilesAbove = getTileY()-4;
        boolean fourUpEmpty = !IsTileSolid(getTileX(), fourTilesAbove, levelManager.getLevelData()) || IsTileSolid(getTileX()+1, fourTilesAbove, levelManager.getLevelData());

        return (oneUpSolid && twoUpEmpty) || (twoUpSolid && threeUpEmpty) || (threeUpSolid && fourUpEmpty);
    }

    private void updatePlayerInfo(Player player){

        if (playerUpdateTimer <= 0) {
            calculatePlayersPos(player);
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
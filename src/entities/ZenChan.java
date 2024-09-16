package entities;

import levels.LevelManager;
import main.Game;
import utilz.Constants.Direction;

import static utilz.Constants.Direction.LEFT;
import static utilz.Constants.Direction.RIGHT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.EnemyConstants.EnemyType.ZEN_CHAN;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;

public class ZenChan extends Enemy {

    // Fly Variables
    private int flyDirectionChangeCounter = 0;
    private boolean isFlyingFirstUpdate = true;
    private double flyStartTime = 0;
    private boolean didFlyInsideSolid = false;

    // Player Info Update variables
    private int playerUpdateTimer;
    private long lastTimerUpdate;

    // Jump Variables
    private int jumpDistance = 0;

    public ZenChan(float x, float y, Direction startWalkingDir) {
        super(x, y, ENEMY_W, ENEMY_H, ZEN_CHAN, startWalkingDir);
        this.startWalkingDir = startWalkingDir;
        initHitbox(ENEMY_HITBOX_W, ENEMY_HITBOX_H);
    }

    @Override
    public void update(Player player) {

        updateAnimationTick();

        if (!EnemyManager.getInstance().getAllEnemiesReachedSpawn()) {
            if (!reachedSpawn)
                updateSpawning();
            return;
        }

        if (firstUpdate)
            firstUpdate();

        updateTimers();
        updatePlayerInfo(player);
        checkIfStuck();
        updateMove();
        updateStateVariables();
    }

    private void firstUpdate() {
        if (!IsEntityOnFloor(hitbox, LevelManager.getInstance().getCurrentLevel().getLevelData()))
            goDown = true;

        lastTimerUpdate = System.currentTimeMillis();
        firstUpdate = false;
    }

    private void updateTimers() {
        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();

        playerUpdateTimer -= timeDelta;
    }

    private void updateMove() {
        int[][] levelData = LevelManager.getInstance().getCurrentLevel().getLevelData();

        if(!IsEntityOnFloor(hitbox, levelData) && !isJumping && !goUp && !goDown)
            goOnFloor(levelData);

        if (isFalling) {
            fall(levelData);
            return;
        }
        if(goUp){
            fly(levelData);
            return;
        }
        if(isJumping){
            jump(levelData, jumpDistance);
            return;
        }

        // enemy stuck in a wall
        if(IsEntityInsideSolid(hitbox, levelData))
            hitbox.y += 1;


        moveOnGround(levelData);

        //Go up
        if (playerTileY < getTileY() && canFly(levelData)) {
            goUp = true;
            goDown = false;
        }

        //Go down
        if (playerTileY > getTileY()) {
            goDown = true;
            goUp = false;
        }
    }

    private void moveOnGround(int[][] levelData) {
        float hitboxX = 0;

        if (walkingDir == LEFT) {
            xSpeed = -walkSpeed;
            hitboxX = hitbox.x;
        }
        else {
            xSpeed = walkSpeed;
            hitboxX = hitbox.x + hitbox.width;
        }

        if (CanMoveHere(hitboxX + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {

            if (!IsSolid(hitboxX + xSpeed, hitbox.y + hitbox.height + 1, levelData)){

                if(goDown){

                    if(!canFall()){
                        goDown = false;
                        return;
                    }

                    hitbox.x += xSpeed;
                    isFalling = true;
                    goDown = false;
                    fall(levelData);
                    return;
                }

                jumpDistance = calculateJumpDistance(levelData);

                if(canJump(jumpDistance)) {
                    isJumping = true;
                    ySpeed = JUMP_Y_SPEED;
                    jump(levelData, jumpDistance);
                    return;
                }
                changeWalkingDir();
            }

            hitbox.x += xSpeed;
        }
        else
            changeWalkingDir();
    }

    private void fly(int[][] levelData) {

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

        if(IsEntityInsideSolid(hitbox, levelData)){
            didFlyInsideSolid = true;
            hitbox.y -= flySpeed;
        }
        else if(didFlyInsideSolid){

            // fly ended
            hitbox.y = GetEntityYPosAboveFloor(hitbox, flySpeed, levelData) - 1;
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

    private void jump(int[][] levelData, int jumpDistance) {
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
            updateXPos(jumpXSpeed, levelData);
        }

        // Going down
        else if (ySpeed <= -JUMP_Y_SPEED){
            if (CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += ySpeed;
                ySpeed += GRAVITY;
                updateXPos(jumpXSpeed, levelData);
            } else {
                isJumping = false;
                hitbox.y = GetEntityYPosAboveFloor(hitbox, ySpeed, levelData);
                updateXPos(jumpXSpeed, levelData);
            }
        } else {
            isJumping = false;
            updateXPos(jumpXSpeed, levelData);
        }
    }

    private void fall(int [][] levelData) {
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelData))
            hitbox.y += fallSpeed;
        else {
            // fall ended
            hitbox.y = GetEntityYPosAboveFloor(hitbox, fallSpeed, levelData);
            isFalling = false;
         }
    }

    private void goOnFloor(int[][] levelData) {
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelData))
            hitbox.y += fallSpeed;
        else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, fallSpeed, levelData);
            goDown = false;
        }
    }

    private boolean canJump(int jumpDistance) {
        return jumpDistance != -1;
    }

    private int calculateJumpDistance(int[][] levelData) {
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
                if (IsTileSolid(getTileX() - i, yFlorTile, levelData)) {
                    tileDistanceToFloor = i;
                    break;
                }
        }

        else if (walkingDir == RIGHT) {
            for (int i = 2; i  < 8 ; i++)
                if (IsTileSolid(getTileX() + i +1 , yFlorTile, levelData)) {
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


    private boolean canFly(int[][] levelData){

        // TODO: Refactor - this method checks fi there is a solid tile to fly on, ig there is checks if there is a empty tile on top

        // check if there is a ceiling above (if there isn't a solid in 3 tiles --> can't fly)

        int oneTileAbove = getTileY()-1;
        boolean oneUpSolid = IsTileSolid(getTileX(), oneTileAbove, levelData) &&  IsTileSolid(getTileX()+1, oneTileAbove, levelData);

        int twoTilesAbove = getTileY()-2;
        boolean twoUpSolid = IsTileSolid(getTileX(), twoTilesAbove, levelData) &&  IsTileSolid(getTileX()+1, twoTilesAbove, levelData);
        boolean twoUpEmpty = !IsTileSolid(getTileX(), twoTilesAbove, levelData) &&  IsTileSolid(getTileX()+1, twoTilesAbove, levelData);

        int threeTilesAbove = getTileY()-3;
        boolean threeUpSolid = IsTileSolid(getTileX(), threeTilesAbove, levelData) && IsTileSolid(getTileX()+1, threeTilesAbove, levelData);
        boolean threeUpEmpty = !IsTileSolid(getTileX(), threeTilesAbove, levelData) || IsTileSolid(getTileX()+1, threeTilesAbove, levelData);

        int fourTilesAbove = getTileY()-4;
        boolean fourUpEmpty = !IsTileSolid(getTileX(), fourTilesAbove, levelData) || IsTileSolid(getTileX()+1, fourTilesAbove, levelData);

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
        lastTimerUpdate = 0;
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
        lastTimerUpdate = 0;
    }

    @Override
    public EnemyType getEnemyType() {
        return ZEN_CHAN;
    }
}
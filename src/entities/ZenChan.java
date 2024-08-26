package entities;

import levels.LevelManager;
import main.Game;
import utilz.Constants.Direction;
import static utilz.Constants.Direction.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.EnemyConstants.EnemyType.ZEN_CHAN;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;

public class ZenChan extends Enemy {

    // Movement Variables
    private boolean goUp = false;
    private boolean goDown = false;
    private boolean isFalling = false;
    private boolean isJumping = false;

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
        initHitbox(ZEN_CHAN_HITBOX_W, ZEN_CHAN_HITBOX_H);
    }

    @Override
    public void update(Player player) {
        tileX = (int) (hitbox.x / Game.TILES_SIZE);
        tileY = (int) (hitbox.y / Game.TILES_SIZE);

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
        if (playerTileY < tileY && canFly(levelData)) {
            goUp = true;
            goDown = false;
        }

        //Go down
        if (playerTileY > tileY) {
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

    private boolean canFall(){
        // check if the under is not out of the level
        return tileY + 1 < Game.TILES_IN_HEIGHT - 1;
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
        if (jumpDistance == -1)
            return false;

        return true;
    }

    private int calculateJumpDistance(int[][] levelData) {
        int tileDistanceToPerimeterWall = -1;
        int tileDistanceToFloor = -1;

        int yFlorTile = (int) (hitbox.y + hitbox.height + 1) / Game.TILES_SIZE;

        // Find the distance to the perimeter wall (max distance 5 tiles)
        if (walkingDir == LEFT) {
            for (int i = 2; i < 8; i++)
                if (IsTilePerimeterWall(tileX - i)) {
                    tileDistanceToPerimeterWall = i;
                    break;
                }
        }

        else if (walkingDir == RIGHT) {
            for (int i = 2; i < 8; i++)
                if (IsTilePerimeterWall(tileX + i)) {
                    tileDistanceToPerimeterWall = i;
                    break;
                }
        }

        // check if between 2 and 6 tiles there is a floor
        if (walkingDir == LEFT) {
            for (int i = 2; i < 8; i++)
                if (IsTileSolid(tileX - i, yFlorTile, levelData)) {
                    tileDistanceToFloor = i;
                    break;
                }
        }

        else if (walkingDir == RIGHT) {
            for (int i = 2; i  < 8 ; i++)
                if (IsTileSolid(tileX + i +1 , yFlorTile, levelData)) {
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

        int oneTileAbove = tileY-1;
        boolean oneUpSolid = IsTileSolid(tileX, oneTileAbove, levelData) &&  IsTileSolid(tileX+1, oneTileAbove, levelData);

        int twoTilesAbove = tileY-2;
        boolean twoUpSolid = IsTileSolid(tileX, twoTilesAbove, levelData) &&  IsTileSolid(tileX+1, twoTilesAbove, levelData);
        boolean twoUpEmpty = !IsTileSolid(tileX, twoTilesAbove, levelData) &&  IsTileSolid(tileX+1, twoTilesAbove, levelData);

        int threeTilesAbove = tileY-3;
        boolean threeUpSolid = IsTileSolid(tileX, threeTilesAbove, levelData) && IsTileSolid(tileX+1, threeTilesAbove, levelData);
        boolean threeUpEmpty = !IsTileSolid(tileX, threeTilesAbove, levelData) || IsTileSolid(tileX+1, threeTilesAbove, levelData);

        int fourTilesAbove = tileY-4;
        boolean fourUpEmpty = !IsTileSolid(tileX, fourTilesAbove, levelData) || IsTileSolid(tileX+1, fourTilesAbove, levelData);

        if ( (oneUpSolid && twoUpEmpty) || (twoUpSolid && threeUpEmpty) || (threeUpSolid && fourUpEmpty))
            return true;

        return false;
    }

    private void updateWalkingDir() {
        if (playerTileX < tileX)
            walkingDir = LEFT;
        else if (playerTileX > tileX)
            walkingDir = RIGHT;
    }

    private void updatePlayerInfo(Player player){

        // update player info in a random interval between 0-8 seconds

        if (playerUpdateTimer <= 0) {
            calculatePlayersPos(player);
            playerUpdateTimer = (int) (Math.random() * PLAYER_INFO_MAX_UPDATE_INTERVAL);
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
    public void bubbleCapture() {
        super.bubbleCapture();
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
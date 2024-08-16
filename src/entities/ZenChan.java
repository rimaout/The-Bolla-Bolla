package entities;

import main.Game;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;
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

    // Player Info Update Interval variables
    private int nextUpdateInterval;
    private double latsUpdateTime = System.currentTimeMillis();

    public ZenChan(float x, float y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, ZEN_CHAN);
        initHitbox(ZEN_CHAN_HITBOX_WIDTH, ZEN_CHAN_HITBOX_HEIGHT);
    }

    public void update(int[][] lvlData, Player player) {

        if (firstUpdate)
            firstUpdate(lvlData);

        updatePlayerInfo(player);
        updateMove(lvlData);
        updateAnimationTick();
    }

    private void firstUpdate(int[][] levelData) {
        if (!IsEntityOnFloor(hitbox, levelData))
            goDown = true;
        firstUpdate = false;
    }

    private void updateMove(int[][] levelData) {
        tileX = (int) (hitbox.x / Game.TILES_SIZE);
        tileY = (int) (hitbox.y / Game.TILES_SIZE);

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
            jump(levelData);
            return;
        }

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
        xSpeed = 0;
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
                    hitbox.x += xSpeed;
                    isFalling = true;
                    goDown = false;
                    fall(levelData);
                    return;
                }

                if(canJump(levelData)) {
                    isJumping = true;
                    ySpeed = jumpSpeed;
                    jump(levelData);
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
            hitbox.y = GetEntityYPosAboveFloor(hitbox, flySpeed, levelData) - 1;

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

    private void jump(int[][] levelData) {
        float jumpXSpeed = xSpeed * 1.4f;

        // Going up
        if (ySpeed < 0){
            hitbox.y += ySpeed;
            ySpeed += GRAVITY;
            updateXPos(jumpXSpeed, levelData);
        }

        // Going down
        else if (ySpeed <= -jumpSpeed){
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

    private boolean canJump(int[][] levelData) {
        // check if after 3 tiles there is a floor
        int yFlorTile = (int) (hitbox.y + hitbox.height + 1) / Game.TILES_SIZE;

        if (walkingDir == LEFT) {

            if(IsTilePerimeterWall(tileX - 4))
                return false;

            if (IsTileSolid(tileX - 4, yFlorTile , levelData))
                return true;
        }

        else if (walkingDir == RIGHT)

           if(IsTilePerimeterWall(tileX + 5))
                return false;

            if (IsTileSolid(tileX + 5, yFlorTile, levelData))
                return true;

        return false;
    }

    private boolean canFly(int[][] levelData){
        // check if there is a ceiling above (if there isn't a solid in 3 tiles --> can't fly)

        int OneTileAbove = tileY-1;
        int TwoTilesAbove = tileY-2;
        int ThreeTilesAbove = tileY-3;

        return (IsTileSolid(tileX, OneTileAbove, levelData) &&  IsTileSolid(tileX+1, OneTileAbove, levelData))||
                (IsTileSolid(tileX, TwoTilesAbove, levelData) &&  IsTileSolid(tileX+1, TwoTilesAbove, levelData))||
                (IsTileSolid(tileX, ThreeTilesAbove, levelData) && IsTileSolid(tileX+1, ThreeTilesAbove, levelData));
    }

    private void updatePlayerInfo(Player player){

        // update player info in a random interval between 0-8 seconds

        if (System.currentTimeMillis() - latsUpdateTime > nextUpdateInterval){
            calculatePlayersPos(player);
            latsUpdateTime = System.currentTimeMillis();
            nextUpdateInterval = (int) (Math.random() * PLAYER_INFO_MAX_UPDATE_INTERVAL);

            if(playerTileX < tileX)
                walkingDir = LEFT;

            else
                walkingDir = RIGHT;
        }
    }
}
package entities;

import levels.LevelManager;
import main.Game;
import projectiles.MaitaFireProjectile;
import projectiles.ProjectileManager;
import utilz.Constants.Direction;

import static utilz.Constants.Direction.LEFT;
import static utilz.Constants.Direction.RIGHT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.EnemyConstants.EnemyType.MAITA;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;

public class Maita extends Enemy {

    // Fly Variables
    private int flyDirectionChangeCounter = 0;
    private boolean isFlyingFirstUpdate = true;
    private double flyStartTime = 0;
    private boolean didFlyInsideSolid = false;

    // Player Info Update variables
    private int playerUpdateTimer;
    private int fireBallTimer;
    private long lastTimerUpdate;

    private boolean fireBallReady = false;

    // Jump Variables
    private int jumpDistance = 0;

    public Maita(float x, float y, Direction startWalkingDir) {
        super(x, y, ENEMY_W, ENEMY_H, MAITA, startWalkingDir);
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

        updateTimers(player);
        updatePlayerInfo(player);
        updateMove();
        updateStateVariables();

        checkFireBall(player);
    }

    private void firstUpdate() {
        if (!IsEntityOnFloor(hitbox, LevelManager.getInstance().getCurrentLevel().getLevelData()))
            goDown = true;

        lastTimerUpdate = System.currentTimeMillis();
        fireBallTimer = FIREBALL_INITIAL_TIMER;
        firstUpdate = false;
    }

    private void updateTimers(Player player) {
        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();

        playerUpdateTimer -= timeDelta;

        if (player.getTileY() == getTileY())
            fireBallTimer -= timeDelta;

        if (fireBallTimer <= 0)
            fireBallReady = true;
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
        if (walkingDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {

            if (walkingDir==LEFT && !IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelData)
                    || walkingDir==RIGHT && !IsSolid(hitbox.x + xSpeed + hitbox.width, hitbox.y + hitbox.height + 1, levelData)) {

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

    private boolean isNextPosFloorSolid() {
        // This method checks if the next two tiles in front of the enemy are solid or not
        // It's used to check if the enemy can jump/fall or not

        int yTile = 0, xTile1 = 0, xTile2 = 0;

        //check if there are 2 solid tiles in front of the enemy
        if (walkingDir == LEFT) {
            yTile = (int) (hitbox.y + hitbox.height + 1) / Game.TILES_SIZE;
            xTile1 = (int) hitbox.x / Game.TILES_SIZE;
            xTile2 = xTile1 - 1;
        }

        else if (walkingDir == RIGHT) {
            yTile = (int) (hitbox.y + hitbox.height + 1) / Game.TILES_SIZE;
            xTile1 = (int) (hitbox.x + hitbox.width) / Game.TILES_SIZE;
            xTile2 = xTile1 + 1;
        }

        return IsTileSolid(xTile1, yTile, LevelManager.getInstance().getCurrentLevel().getLevelData())
                || IsTileSolid(xTile2, yTile, LevelManager.getInstance().getCurrentLevel().getLevelData());
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

    private void checkFireBall(Player player) {
        if (fireBallReady && player.getTileY() == getTileY() && isPlayerInViewingRange(player)) {
            ProjectileManager.getInstance().addProjectile(new MaitaFireProjectile(hitbox.x, hitbox.y, isPlayerLeftOrRight(player)));

            fireBallTimer = FIREBALL_TIMER;
            fireBallReady = false;
        }
    }

    private void updatePlayerInfo(Player player){

        // update player info in a random interval between 0-8 seconds

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
        fireBallTimer = FIREBALL_INITIAL_TIMER;
        fireBallReady = false;
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
        return MAITA;
    }
}
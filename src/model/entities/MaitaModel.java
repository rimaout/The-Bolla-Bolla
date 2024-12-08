package model.entities;

import model.Constants;
import model.Constants.Direction;
import model.projectiles.ProjectileManagerModel;
import model.projectiles.MaitaFireProjectileModel;

import static model.entities.HelpMethods.*;
import static model.Constants.GRAVITY;
import static model.Constants.Direction.LEFT;
import static model.Constants.Direction.RIGHT;
import static model.Constants.EnemyConstants.*;
import static model.Constants.EnemyConstants.EnemyType.MAITA;

public class MaitaModel extends EnemyModel {

    // Fly Variables
    private int flyDirectionChangeCounter = 0;
    private boolean isFlyingFirstUpdate = true;
    private double flyStartTime = 0;
    private boolean didFlyInsideSolid = false;

    // Player Info Update variables
    private int playerUpdateTimer;
    private int fireBallTimer;

    private boolean fireBallReady = false;
    private boolean firstUpdate = true;

    // Jump Variables
    private int jumpDistance = 0;

    public MaitaModel(float x, float y, Direction startWalkingDir) {
        super(x, y, ENEMY_W, ENEMY_H, MAITA, startWalkingDir);
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

        updateTimers(playerModel);
        updatePlayerInfo(playerModel);
        updateMove();
        updateStateVariables();

        checkFireBall(playerModel);
    }

    private void firstUpdate() {
        if (!IsEntityOnFloor(hitbox, levelManagerModel.getLevelData()))
            goDown = true;

        fireBallTimer = FIREBALL_INITIAL_TIMER;
        firstUpdate = false;
    }

    private void updateTimers(PlayerModel playerModel) {
        playerUpdateTimer -= (int) timer.getTimeDelta();

        if (playerModel.getTileY() == getTileY())
            fireBallTimer -= (int) timer.getTimeDelta();

        if (fireBallTimer <= 0)
            fireBallReady = true;
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

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelManagerModel.getLevelData())) {

            if (walkingDir==LEFT && !IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelManagerModel.getLevelData())
                    || walkingDir==RIGHT && !IsSolid(hitbox.x + xSpeed + hitbox.width, hitbox.y + hitbox.height + 1, levelManagerModel.getLevelData())) {

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

        int yTile = 0, xTile1 = 0, xTile2 = 0;

        //check if there are 2 solid tiles in front of the enemy
        if (walkingDir == LEFT) {
            yTile = (int) (hitbox.y + hitbox.height + 1) / Constants.TILES_SIZE;
            xTile1 = (int) hitbox.x / Constants.TILES_SIZE;
            xTile2 = xTile1 - 1;
        }

        else if (walkingDir == RIGHT) {
            yTile = (int) (hitbox.y + hitbox.height + 1) / Constants.TILES_SIZE;
            xTile1 = (int) (hitbox.x + hitbox.width) / Constants.TILES_SIZE;
            xTile2 = xTile1 + 1;
        }

        return IsTileSolid(xTile1, yTile, levelManagerModel.getLevelData())
                || IsTileSolid(xTile2, yTile, levelManagerModel.getLevelData());
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

    protected boolean canFly(){

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

    private void checkFireBall(PlayerModel playerModel) {
        if (fireBallReady && playerModel.getTileY() == getTileY() && isPlayerInViewingRange(playerModel)) {
            ProjectileManagerModel.getInstance().addProjectile(new MaitaFireProjectileModel(hitbox.x, hitbox.y, isPlayerLeftOrRight(playerModel)));

            fireBallTimer = FIREBALL_TIMER;
            fireBallReady = false;
        }
    }

    private void updatePlayerInfo(PlayerModel playerModel){

        // update player info in a random interval between 0-8 seconds

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
        fireBallTimer = FIREBALL_INITIAL_TIMER;
        fireBallReady = false;

        firstUpdate = true;
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
        return MAITA;
    }
}
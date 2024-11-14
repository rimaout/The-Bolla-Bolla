package model.entities;

import entities.Entity;
import view.audio.AudioPlayer;
import model.utilz.Constants;
import model.utilz.PlayingTimer;
import model.levels.LevelManagerModel;
import model.projectiles.ProjectileManagerModel;
import itemesAndRewards.PowerUpManager;
import model.projectiles.PlayerBubbleProjectileModel;

import static model.utilz.Constants.*;
import static model.utilz.HelpMethods.*;
import static model.utilz.Constants.Direction.LEFT;
import static model.utilz.Constants.Direction.RIGHT;
import static model.utilz.Constants.PlayerConstants.*;

public class PlayerModel extends Entity {
    private final LevelManagerModel levelManagerModel = LevelManagerModel.getInstance();
    private final PlayingTimer timer = PlayingTimer.getInstance();

    // Movement values and variables
    private boolean left, right, jump, isJumping, inAir;
    private boolean moving, attacking, attackingAnimation, respawning, canRespawn;
    private float xSpeed = 0;
    private float airSpeed = 0.0f;

    // General Variables
    private int lives = 3;
    private int points = 0;
    private int flipX = 0;
    private int flipW = 1;

    // Timers
    private int immuneTimer;
    private int attackTimer = 150;

    // PowerUp Values
    private float speedMultiplier = 1;         // shoes
    private float bubbleCadenceMultiplier = 1; // greenCandy
    private int jumpPoints = 0;                // emeraldRing
    private int walkPoints = 0;                // crystalRing
    private int bubbleShotPoints = 0;          // rubyRing

    // Sound Variables
    private boolean playJumpSound, playDeathSound;

    public PlayerModel() {
        super(-3* Constants.TILES_SIZE, -3 * Constants.TILES_SIZE, IMAGE_W, IMAGE_H); // Set the player outside the map (so it doesn't get drawn)
        initHitbox(HITBOX_W, HITBOX_H);
    }

    public void update() {

        updateTimers();
        updatePosition();
        updateSounds();

        if (attacking && canAttack())
            attack();
    }

    private boolean canAttack() {
        return !respawning && IsEntityInsideMap(hitbox) && !IsTileRoof((int) hitbox.y / Constants.TILES_SIZE);
    }

    private void attack() {
        Direction direction;

        if (flipW == -1)
            direction = LEFT;
        else
            direction = RIGHT;

        int xOffset = 10 * Constants.SCALE;
        int yOffset = 3 * Constants.SCALE;

        if (direction == LEFT)
            ProjectileManagerModel.getInstance().addProjectile(new PlayerBubbleProjectileModel(hitbox.x - xOffset, hitbox.y - yOffset, direction));
        else
            ProjectileManagerModel.getInstance().addProjectile(new PlayerBubbleProjectileModel(hitbox.x + hitbox.width - xOffset, hitbox.y - yOffset, direction));

        attackingAnimation = true;
        attackTimer = (int) (ATTACK_TIMER * bubbleCadenceMultiplier);

        PowerUpManager.getInstance().increaseBubbleShootCounter();
        addPoints(bubbleShotPoints);     // rubyRing powerUp
    }

    private void updateTimers() {

        if (immune) {
            immuneTimer -= (int) timer.getTimeDelta();
            if (immuneTimer <= 0)
                immune = false;
        }

        attackTimer -= (int) timer.getTimeDelta();
        if (attackTimer > 0)
                attacking = false;
    }

    private void updatePosition() {

        if (respawning) {
            respawn();
            return;
        }

        updateMovementValues();

        if (!left && !right && !inAir)
            return;

        if(!IsEntityInsideMap(hitbox))
            pacManEffect();

        // MOVE
        if (IsEntityInsideSolid(hitbox, levelManagerModel.getLevelData()))
            handleMovementInsideSolid();

        else if (inAir)
            handleInAirMovement();

        else
           handleOnFloorMovement();
    }

    private void updateMovementValues() {
        moving = false;

        if (jump && !inAir) {
            inAir = true;
            isJumping = true;
            playJumpSound = true;
            
            if(!IsEntityInsideSolid(hitbox, levelManagerModel.getLevelData())) {  // can't jump if is inside solid
                airSpeed = JUMP_SPEED;
                PowerUpManager.getInstance().increaseJumpCounter();
                addPoints(jumpPoints);  //emeraldRing powerUp
            }
        }

        if (!left && !right && !inAir)
            return;

        xSpeed = 0;

        if (left) {
            xSpeed -= WALK_SPEED * speedMultiplier;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += WALK_SPEED * speedMultiplier;
            flipX = 0;
            flipW = 1;
        }

        if (!inAir)
            if (!IsEntityOnFloor(hitbox, levelManagerModel.getLevelData()))
                inAir = true;
    }

    private void handleInAirMovement(){
        if (isJumping)
            jumping();
        else
            falling();
    }

    private void handleOnFloorMovement(){
        updateXPos(xSpeed);
        addPoints(walkPoints);  // crystalRing powerUp
        moving = true;          // Activate running animation
    }

    private void handleMovementInsideSolid() {
        // JUMPING
        if (airSpeed < 0) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
            conpenetrationSafeUpdateXPos(xSpeed);
        }
        // FALLING
        else {
            hitbox.y += airSpeed;
            airSpeed = FALL_SPEED;
            isJumping = false;
            conpenetrationSafeUpdateXPos(xSpeed);
        }
    }

    public void jumpOnBubble() {
        airSpeed = JUMP_SPEED;
        inAir = true;
        isJumping = true;
        playJumpSound = true;
        PowerUpManager.getInstance().increaseJumpCounter();
    }

    private void jumping(){

        // Going up
        if (airSpeed < 0){
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
            conpenetrationSafeUpdateXPos(xSpeed);
        }

        // Going down
        else if (airSpeed <= -JUMP_SPEED){
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelManagerModel.getLevelData())) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosAboveFloor(hitbox, airSpeed, levelManagerModel.getLevelData());
                resetInAir();
                updateXPos(xSpeed);
            }
        } else {
            isJumping = false;
            updateXPos(xSpeed);
        }
    }

    private void falling(){
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelManagerModel.getLevelData())) {
                hitbox.y += airSpeed;
                airSpeed = FALL_SPEED;
                updateXPos(xSpeed / 3);
        } else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, airSpeed, levelManagerModel.getLevelData());
            updateXPos(xSpeed / 3);
            resetInAir();
        }
    }

    private void pacManEffect() {
        if (hitbox.y > Constants.TILES_SIZE * Constants.TILES_IN_HEIGHT)
            hitbox.y = -2 * Constants.TILES_SIZE;
    }

    public void death() {

        if (!immune) {
            immune = true;
            immuneTimer = IMMUNE_TIME_AFTER_RESPAWN;
            respawning = true;
            playDeathSound = true;
            resetMovements();
            resetInAir();
        }
    }

    private void updateSounds() {
        if (playJumpSound) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.JUMP);
            playJumpSound = false;
        }

        if (playDeathSound) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.PLAYER_DEATH);
            playDeathSound = false;
        }
    }

    private void respawn() {

        if (canRespawn) { // Last frame of the dying animation
            respawning = false;
            immuneTimer = IMMUNE_TIME_AFTER_RESPAWN;
            hitbox.x = SPAWN_X;
            hitbox.y = SPAWN_Y;
            lives--;

            PowerUpManager.getInstance().reset();    // Reset all powerUps when player dies
        }
    }

    private void resetInAir() {
        inAir = false;
        isJumping = false;
        airSpeed = 0;
    }

    public void resetMovements() {
        left = false;
        right = false;
        jump = false;
        attacking = false;
    }

    private void resetPowerUps() {
        speedMultiplier = 1;
        bubbleCadenceMultiplier = 1;
        jumpPoints = 0;
        walkPoints = 0;
        bubbleShotPoints = 0;
    }

    public void reset(Boolean resetLives, Boolean resetPoints) {
        resetMovements();
        resetInAir();
        resetPowerUps();

        active = true;
        immune = false;
        immuneTimer = 0;
        attackTimer = 100;
        xSpeed = 0;
        airSpeed = 0.0f;
        flipX = 0;
        flipW = 1;

        playJumpSound = false;
        playDeathSound = false;

        if (resetLives)
            lives = 3;

        if (resetPoints)
            points = 0;

        if (!IsEntityOnFloor(hitbox, levelManagerModel.getLevelData()))
            inAir = true;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public int getLives() {
        return lives;
    }

    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    public int getPoints() {
        return points;
    }

    public float getXSpeed(){
        return xSpeed;
    }

    public boolean isJumpActive() {
        return jump;
    }

    public boolean isRespawning() {
        return respawning;
    }

    public Direction getDirection(){
        if (flipW == -1)
            return LEFT;
        else
            return RIGHT;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public void setBubbleCadenceMultiplier(float bubbleCadenceMultiplier) {
        this.bubbleCadenceMultiplier = bubbleCadenceMultiplier;
    }

    public void setChacknHeartImmunity(int immunityTime) {
        immune = true;
        immuneTimer = immunityTime;
    }

    public void setJumpPoints(int jumpPoints) {
        this.jumpPoints = jumpPoints;
    }

    public void setWalkPoints(int walkPoints) {
        this.walkPoints = walkPoints;
    }

    public void setBubbleShotPoints(int bubbleShotPoints) {
        this.bubbleShotPoints = bubbleShotPoints;
    }

    public int getImmuneTimer() {
        return immuneTimer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFlipX() {
        return flipX;
    }

    public int getFlipW() {
        return flipW;
    }

    public void setAttackingAnimation(boolean attackingAnimation) {
        this.attackingAnimation = attackingAnimation;
    }

    public void setRespawning(boolean respawning) {
        this.respawning = respawning;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isInAir() {
        return inAir;
    }

    public float getAirSpeed() {
        return airSpeed;
    }

    public boolean isAttackingAnimation() {
        return attackingAnimation;
    }

    public void setCanRespawn(boolean canRespawn) {
        this.canRespawn = canRespawn;
    }
}
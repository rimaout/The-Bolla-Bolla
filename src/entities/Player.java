package entities;

import bubbles.BubbleManager;
import itemesAndRewards.PowerUpManager;
import itemesAndRewards.RewardPointsManager;
import levels.LevelManager;
import utilz.LoadSave;
import gameStates.Playing;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.Direction;
import static utilz.Constants.Direction.*;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity{
    private int[][] levelData;
    private Playing playing;
    private BubbleManager bubbleManager;
    private boolean isFirstUpdate = true;

    // Animation values and variables
    private BufferedImage[][] animations;
    private int playerAnimation = IDLE_ANIMATION;

    // Movement values and variables
    private boolean left, right, jump, isJumping, inAir;
    private boolean moving, attacking, attackingAnimation, respawning;
    private float xSpeed = 0;
    private float airSpeed = 0.0f;

    // General Variables
    private int lives = 3;
    private int points = 0;
    private  int flipX = 0;
    private int flipW = 1;

    // Timers
    private long lastTimerUpdate;
    private int immuneTimer;
    private int attackTimer = 150;

    // PowerUp Values
    private float speedMultiplier = 1;         // shoes
    private float bubbleCadenceMultiplier = 1; // greenCandy
    private int jumpPoints = 0;                // emeraldRing
    private int walkPoints = 0;                // crystalRing
    private int bubbleShotPoints = 0;          // rubyRing

    public Player(Playing playing) {

        super(-3* Game.TILES_SIZE, -3 * Game.TILES_SIZE, IMMAGE_W, IMMAGE_H); // Set the player outside the map (so it doesn't get drawn)
        this.playing = playing;
        this.bubbleManager = BubbleManager.getInstance(this);

        loadAnimation();
        initHitbox(HITBOX_W, HITBOX_H);
    }

    public void update() {

        updateTimers();
        setAnimation();
        updatePosition();
        updateAnimationTick();

        if (attacking && canAttack())
            attack();
    }

    public void draw(Graphics2D g) {
        if (immune && !respawning) {
            if (immuneTimer % 100 < 40) { // Transparency blink effect
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55F)); // Set transparency
                g.drawImage(animations[playerAnimation][animationIndex], (int) (hitbox.x - OFFSET_X) + flipX, (int) (hitbox.y - OFFSET_Y), width * flipW, height, null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F)); // Set transparency
                return;
            }
        }

        g.drawImage(animations[playerAnimation][animationIndex],  (int) (hitbox.x - OFFSET_X) + flipX, (int) (hitbox.y - OFFSET_Y), width * flipW, height, null);
    }

    private boolean canAttack() {
        return !respawning && IsEntityInsideMap(hitbox) && !IsTileRoof((int) hitbox.y / Game.TILES_SIZE);
    }

    private void attack() {
        Direction direction;

        if (flipW == -1)
            direction = LEFT;
        else
            direction = RIGHT;

        int xOffset = 10 * Game.SCALE;
        int yOffset = 3 * Game.SCALE;

        if (direction == LEFT)
            bubbleManager.addBubble(hitbox.x , hitbox.y -yOffset, direction);
        else
            bubbleManager.addBubble(hitbox.x + hitbox.width - xOffset, hitbox.y - yOffset, direction);

        attackingAnimation = true;
        attackTimer = (int) (ATTACK_TIMER * bubbleCadenceMultiplier);

        PowerUpManager.getInstance().increaseBubbleShootCounter();
        addPoints(bubbleShotPoints);     // rubyRing powerUp
    }

    private void setAnimation() {
        int startAnimation = playerAnimation;

        if (moving)
            playerAnimation = RUNNING_ANIMATION;
        else
            playerAnimation = IDLE_ANIMATION;

        if (inAir) {
            if (airSpeed < 0)
                playerAnimation = JUMPING_ANIMATION;
            else
                playerAnimation = FALLING_ANIMATION;
        }

        if (attackingAnimation)
            playerAnimation = ATTACK_ANIMATION;

        if (respawning)
            playerAnimation = DEAD_ANIMATION;

        if (startAnimation != playerAnimation){
            animationTick = 0;
            animationIndex = 0;
        }
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick > ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(playerAnimation)) {
                animationIndex = 0;
                attackingAnimation = false;
                respawning = false;
            }
        }
    }

    private void updateTimers() {
        if (isFirstUpdate) {
            isFirstUpdate = false;
            lastTimerUpdate = System.currentTimeMillis();
        }

        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();

        if (immune) {
            immuneTimer -= timeDelta;
            if (immuneTimer <= 0)
                immune = false;
        }

        attackTimer -= timeDelta;
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
        if (IsEntityInsideSolid(hitbox, levelData))
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
            
            if(!IsEntityInsideSolid(hitbox, levelData)) {  // can't jump if is inside solid
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
            if (!IsEntityOnFloor(hitbox, levelData))
                inAir = true;
    }

    private void handleInAirMovement(){
        if (isJumping)
            jumping();
        else
            falling();
    }

    private void handleOnFloorMovement(){
        updateXPos(xSpeed, levelData);
        addPoints(walkPoints);  // crystalRing powerUp
        moving = true;          // Activate running animation
    }

    private void handleMovementInsideSolid() {
        // JUMPING
        if (airSpeed < 0) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
            conpenetrationSafeUpdateXPos(xSpeed, levelData);
        }
        // FALLING
        else {
            hitbox.y += airSpeed;
            airSpeed = FALL_SPEED;
            isJumping = false;
            conpenetrationSafeUpdateXPos(xSpeed, levelData);
        }
    }
    private void jumping(){

        // Going up
        if (airSpeed < 0){
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
            conpenetrationSafeUpdateXPos(xSpeed, levelData);
        }

        // Going down
        else if (airSpeed <= -JUMP_SPEED){
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed, levelData);
            } else {
                hitbox.y = GetEntityYPosAboveFloor(hitbox, airSpeed, levelData);
                resetInAir();
                updateXPos(xSpeed, levelData);
            }
        } else {
            isJumping = false;
            updateXPos(xSpeed, levelData);
        }
    }

    private void falling(){
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += airSpeed;
                airSpeed = FALL_SPEED;
                updateXPos(xSpeed / 3, levelData);
        } else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, airSpeed, levelData);
            updateXPos(xSpeed / 3, levelData);
            resetInAir();
        }
    }

    private void pacManEffect() {
        if (hitbox.y > Game.TILES_SIZE * Game.TILES_IN_HEIGHT)
            hitbox.y = -2 * Game.TILES_SIZE;
    }

    public void death() {

        if (!immune) {
            immune = true;
            immuneTimer = IMMUNE_TIME_AFTER_RESPAWN;
            respawning = true;
            resetMovements();
            resetInAir();
        }
    }

    private void respawn() {

        if (animationIndex == getSpriteAmount(DEAD_ANIMATION)-1) { // Last frame of the dying animation
            respawning = false;
            playerAnimation = IDLE_ANIMATION;
            immuneTimer = IMMUNE_TIME_AFTER_RESPAWN;
            hitbox.x = SPAWN_X;
            hitbox.y = SPAWN_Y;
            lives--;

            PowerUpManager.getInstance().resetAll();    // Reset all powerUps when player dies
        }
    }

    private void loadAnimation() {
        BufferedImage img = LoadSave.GetSprite(LoadSave.PLAYER_SPRITE);

        animations = new BufferedImage[6][7];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i * DEFAULT_W, j* DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    public void loadLevelData() {
        this.levelData = LevelManager.getInstance().getCurrentLevel().getLevelData();

        if (!IsEntityOnFloor(hitbox, levelData))
            inAir = true;
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

    public void resetAll(Boolean resetLives, Boolean resetPoints) {
        resetMovements();
        resetInAir();
        isFirstUpdate = true;
        immune = false;
        immuneTimer = 0;
        attackTimer = 100;
        xSpeed = 0;
        airSpeed = 0.0f;
        flipX = 0;
        flipW = 1;
        playerAnimation = IDLE_ANIMATION;

        if (resetLives)
            lives = 3;

        if (resetPoints)
            points = 0;

        if (!IsEntityOnFloor(hitbox, levelData))
            inAir = true;
    }

    public void jumpOnBubble() {
        airSpeed = JUMP_SPEED;
        inAir = true;
        isJumping = true;
        PowerUpManager.getInstance().increaseJumpCounter();
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

    public void addLive() {
        lives++;
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

    public Direction getDirection(){
        if (flipW == -1)
            return LEFT;
        else
            return RIGHT;
    }

    public int getYTile() {
        return (int) hitbox.y / Game.TILES_SIZE;
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
}
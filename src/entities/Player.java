package entities;

import bubbles.BubbleManager;
import utilz.Constants;
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
    private boolean isImmune;
    private  int flipX = 0;
    private int flipW = 1;

    // Timers
    private long lastTimerUpdate;
    private int immuneTimer;
    private int attackTimer = 100;

    public Player(Playing playing, BubbleManager bubbleManager) {
        super(SPAWN_X, SPAWN_Y, IMMAGE_W, IMMAGE_H);
        this.playing = playing;
        this.bubbleManager = bubbleManager;

        loadAnimation();
        initHitbox(HITBOX_W, HITBOX_H);
    }

    public void update() {
        if(lives <= 0) {
            playing.setGameOver(true);
            return;
        }

        updateTimers();
        setAnimation();
        updatePosition();
        updateAnimationTick();

        if (attacking && !respawning && IsEntityInsideMap(hitbox) && !IsTileRoof((int) hitbox.y / Game.TILES_SIZE) )
            attack();
    }

    public void draw(Graphics g) {
        g.drawImage(animations[playerAnimation][animationIndex],  (int) (hitbox.x - DRAWOFFSET_X) + flipX, (int) (hitbox.y - DRAWOFFSET_Y), width * flipW, height, null);
    }

    private void attack() {
        Direction direction;

        switch (flipW) {
            case -1 -> direction = LEFT;
            default -> direction = RIGHT;
        }

        if (direction == LEFT)
            bubbleManager.addBubble(hitbox.x - hitbox.width-5*Game.SCALE, hitbox.y-3*Game.SCALE, direction);
        else
            bubbleManager.addBubble(hitbox.x + hitbox.width, hitbox.y-3*Game.SCALE, direction);

        attackTimer = ATTACK_TIMER;
        attackingAnimation = true;
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
            playerAnimation = ATTACK_AMATION;

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

        if (isImmune) {
            immuneTimer -= timeDelta;
            if (immuneTimer <= 0)
                isImmune = false;
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
            
            if(!IsEntityInsideSolid(hitbox, levelData))    // can't jump if is inside solid
                airSpeed = JUMP_SPEED;
        }

        if (!left && !right && !inAir)
            return;

        xSpeed = 0;

        if (left) {
            xSpeed -= WALK_SPEED;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += WALK_SPEED;
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
        moving = true; // Activate running animation
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

        if (!isImmune) {
            isImmune = true;
            immuneTimer = IMMUNE_TIME_AFTER_RESPAWN;
            respawning = true;
            resetDirection();
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
        }
    }

    private void loadAnimation() {
        BufferedImage img = LoadSave.GetSprite(LoadSave.PLAYER_SPRITE);

        animations = new BufferedImage[6][7];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i * DEFAULT_W, j* DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;

        if (!IsEntityOnFloor(hitbox, levelData))
            inAir = true;
    }


    private void resetInAir() {
        inAir = false;
        isJumping = false;
        airSpeed = 0;
    }

    public void resetDirection() {
        left = false;
        right = false;
    }

    public void resetAll() {
        resetDirection();
        resetInAir();
        isFirstUpdate = true;
        isImmune = false;
        immuneTimer = 0;
        attackTimer = 100;
        hitbox.x = SPAWN_X;
        hitbox.y = SPAWN_Y;
        lives = 3;
        xSpeed = 0;
        airSpeed = 0.0f;
        flipX = 0;
        flipW = 1;
        playerAnimation = IDLE_ANIMATION;


        if (!IsEntityOnFloor(hitbox, levelData))
            inAir = true;
    }

    public void jumpOnBubble() {
        airSpeed = JUMP_SPEED;
        inAir = true;
        isJumping = true;
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

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public float getXSpeed(){
        return xSpeed;
    }

       public boolean isJumpActive() {
        return jump;
    }
}
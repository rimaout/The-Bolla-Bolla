package entities;

import audio.AudioPlayer;
import gameStates.Playing;
import itemesAndRewards.PowerUpManager;
import levels.LevelManager;
import main.Game;
import projectiles.PlayerBubbleProjectile;
import projectiles.ProjectileManager;
import utilz.LoadSave;
import utilz.PlayingTimer;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.*;
import static utilz.Constants.Direction.LEFT;
import static utilz.Constants.Direction.RIGHT;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity{
    private final LevelManager levelManager = LevelManager.getInstance();
    private final PlayingTimer timer = PlayingTimer.getInstance();

    // Animation values and variables
    private BufferedImage[][] sprites;
    private int playerAnimation = IDLE_ANIMATION;

    // Movement values and variables
    private boolean left, right, jump, isJumping, inAir;
    private boolean moving, attacking, attackingAnimation, respawning;
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

    public Player() {
        super(-3* Game.TILES_SIZE, -3 * Game.TILES_SIZE, IMMAGE_W, IMMAGE_H); // Set the player outside the map (so it doesn't get drawn)

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
                g.drawImage(sprites[playerAnimation][animationIndex], (int) (hitbox.x - OFFSET_X) + flipX, (int) (hitbox.y - OFFSET_Y), width * flipW, height, null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F)); // Set transparency
                return;
            }
        }

        g.drawImage(sprites[playerAnimation][animationIndex],  (int) (hitbox.x - OFFSET_X) + flipX, (int) (hitbox.y - OFFSET_Y), width * flipW, height, null);

        if (playJumpSound) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.JUMP);
            playJumpSound = false;
        }

        if (playDeathSound) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.PLAYER_DEATH);
            playDeathSound = false;
        }
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
            ProjectileManager.getInstance().addProjectile(new PlayerBubbleProjectile(hitbox.x - xOffset, hitbox.y - yOffset, direction));
        else
            ProjectileManager.getInstance().addProjectile(new PlayerBubbleProjectile(hitbox.x + hitbox.width - xOffset, hitbox.y - yOffset, direction));

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
        if (IsEntityInsideSolid(hitbox, levelManager.getLevelData()))
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
            
            if(!IsEntityInsideSolid(hitbox, levelManager.getLevelData())) {  // can't jump if is inside solid
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
            if (!IsEntityOnFloor(hitbox, levelManager.getLevelData()))
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
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelManager.getLevelData())) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosAboveFloor(hitbox, airSpeed, levelManager.getLevelData());
                resetInAir();
                updateXPos(xSpeed);
            }
        } else {
            isJumping = false;
            updateXPos(xSpeed);
        }
    }

    private void falling(){
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelManager.getLevelData())) {
                hitbox.y += airSpeed;
                airSpeed = FALL_SPEED;
                updateXPos(xSpeed / 3);
        } else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, airSpeed, levelManager.getLevelData());
            updateXPos(xSpeed / 3);
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
            playDeathSound = true;
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

            PowerUpManager.getInstance().reset();    // Reset all powerUps when player dies
        }
    }

    private void loadAnimation() {
        BufferedImage img = LoadSave.GetSprite(LoadSave.PLAYER_SPRITE);

        sprites = new BufferedImage[6][7];
        for (int j = 0; j < sprites.length; j++)
            for (int i = 0; i < sprites[j].length; i++)
                sprites[j][i] = img.getSubimage(i * DEFAULT_W, j* DEFAULT_H, DEFAULT_W, DEFAULT_H);
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
        playerAnimation = IDLE_ANIMATION;

        playJumpSound = false;
        playDeathSound = false;

        if (resetLives)
            lives = 3;

        if (resetPoints)
            points = 0;

        if (!IsEntityOnFloor(hitbox, levelManager.getLevelData()))
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

    public BufferedImage[][] getSprites() {
        return sprites;
    }
}
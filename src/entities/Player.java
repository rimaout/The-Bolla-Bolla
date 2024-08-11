package entities;

import Utillz.LoadSave;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Utillz.Constants.PlayerConstants.*;
import static Utillz.HelpMethods.*;

public class Player extends Entity{
    private int[][] levelData;

    // Animation values and variables
    private BufferedImage[][] animations;
    private int animationTick, animationIndex;
    private int playerAnimation = IDLE_ANIMATION;
    private boolean left, right, jump, isJumping;
    private boolean moving, attacking;

    // Hitbox values and variables
    private final float X_DRAW_OFFSET = 3 * Game.SCALE;
    private final float Y_DRAW_OFFSET = 3 * Game.SCALE;

    // Movement values and variables
    private final float PLAYER_SPEED = 0.4f * Game.SCALE;
    private final float FALL_SPEED = 0.35f * Game.SCALE;
    private final float GRAVITY = 0.01f * Game.SCALE;
    private final float JUMP_SPEED = -1f * Game.SCALE;
    private final float fallSpeedAfterCollision = 0.1f * Game.SCALE;

    private float xSpeed = 0;
    private float airSpeed = 0.0f;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimation();
        initHitbox(x, y, 13*Game.SCALE, 13*Game.SCALE);
    }

    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAnimation][animationIndex],  (int) (hitbox.x - X_DRAW_OFFSET), (int) (hitbox.y - Y_DRAW_OFFSET), width, height, null);
        drawHitbox(g);
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick > ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(playerAnimation)) {
                animationIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAnimation = playerAnimation;

        if (moving)
            playerAnimation = RUNNING_ANIMATION;
        else
            playerAnimation = IDLE_ANIMATION;

        if (attacking)
            playerAnimation = ATTACK_AMATION;

        if (inAir) {
            if (airSpeed < 0)
                playerAnimation = JUMPING_ANIMATION;
            else
                playerAnimation = FALLING_ANIMATION;
        }

        if (startAnimation != playerAnimation){
            animationTick = 0;
            animationIndex = 0;
        }
    }

    private void updatePosition() {
        updateMovementValues();

        if (!left && !right && !inAir)
            return;

        if(!IsEntityInsideMap(hitbox))
            pacManEffect();

        if (inAir){
            if (IsEntityInsideSolid(hitbox, levelData))
               airMovementInsideSolid();
            else
                airMovement();
        }

        if (!inAir) {
            updateXPos(xSpeed);
            moving = true;
        }
    }

    private void updateMovementValues() {
        moving = false;

        if (jump && !inAir) {
            inAir = true;
            isJumping = true;
            airSpeed = JUMP_SPEED;
        }

        if (!left && !right && !inAir)
            return;

        xSpeed = 0;

        if (left)
            xSpeed -= PLAYER_SPEED;
        if (right)
            xSpeed += PLAYER_SPEED;

        if (!inAir)
            if (!IsEntityOnFloor(hitbox, levelData))
                inAir = true;
    }

    private void airMovement(){
        if (isJumping)
            jumping();
        else
            falling();
    }

    private void airMovementInsideSolid() {
        // JUMPING
        if (airSpeed < 0) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;

            if (!WillEntityCollideWall(hitbox, xSpeed)) {
                hitbox.x += xSpeed;
            }
        }
        // FALLING
        else {
            hitbox.y += airSpeed;
            airSpeed = FALL_SPEED;
            isJumping = false;

            if (!WillEntityCollideWall(hitbox, xSpeed)) {
                hitbox.x += xSpeed;
            }
        }
    }

    private void jumping(){

        // Going up
        if (airSpeed < 0){
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
            updateXPos(xSpeed);
        }

        // Going down
        else if (airSpeed <= -JUMP_SPEED){
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosAboveFloor(hitbox, airSpeed, levelData);
                resetInAir();
                updateXPos(xSpeed);
            }
        }
        else {
            isJumping = false;
            updateXPos(xSpeed);
        }
    }

    private void falling(){
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += airSpeed;
                airSpeed = FALL_SPEED;
                updateXPos(xSpeed / 3);
        } else {
            hitbox.y = GetEntityYPosAboveFloor(hitbox, airSpeed, levelData);
            updateXPos(xSpeed / 3);
            resetInAir();
        }
    }

    private void pacManEffect() {
        if (hitbox.y > Game.TILES_SIZE * Game.TILES_IN_HEIGHT)
            hitbox.y = -2 * Game.TILES_SIZE;
    }

    private void updateXPos(float xMovement) {
        if (CanMoveHere(hitbox.x + xMovement, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            hitbox.x += xMovement;
        }
    }

    private void loadAnimation() {
        BufferedImage img = LoadSave.GetSprite(LoadSave.PLAYER_SPRITE);

        animations = new BufferedImage[6][4];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 18, j*18, 18, 18);
            }
        }
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
}
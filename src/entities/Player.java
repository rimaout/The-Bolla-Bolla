package entities;

import Utillz.LoadSave;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Utillz.Constants.PlayerConstants.*;
import static Utillz.HelpMethods.*;

public class Player extends Entity{
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 50;

    private int playerAnimation = IDLE_ANIMATION;
    private boolean left, right, jump;
    private boolean moving, attacking;
    private float playerSpeed = 0.7f * Game.SCALE;

    private int[][] levelData;
    private float xDrawOffset = 3 * Game.SCALE;
    private float yDrawOffset = 2 * Game.SCALE;

    // Jumping / Gravity
    private float airSpeed = 0.0f;
    private float gravity = 0.05f * Game.SCALE;
    private float jumpSpeed = -2.24f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.1f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimation();
        initHitbox(x, y, 11*Game.SCALE, 13*Game.SCALE);
    }

    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAnimation][animationIndex],  (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null);
        drawHitbox(g);
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick > animationSpeed) {
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
        moving = false;

        if (jump && !inAir) {
            inAir = true;
            airSpeed = jumpSpeed;
        }

        if (!left && !right && !inAir)
            return;

        float xSpeed = 0;

        if (left)
            xSpeed -= playerSpeed;
        if (right)
            xSpeed += playerSpeed;

        if (!inAir)
            if (!IsEntityOnFloor(hitbox, levelData))
                inAir = true;

        if (inAir) {
            if (airSpeed < 0) {
                // Jumping
                if (!WillCollideRoof(hitbox, airSpeed)) {
                    hitbox.y += airSpeed;
                    airSpeed += gravity;

                } else {
                    hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                    airSpeed = fallSpeedAfterCollision;
                }
                if (!WillEntityCollideWall(hitbox, xSpeed)){
                    hitbox.x += xSpeed;
                }
            } else {
                // Falling
                if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
                    hitbox.y += airSpeed;
                    //airSpeed += gravity;
                    airSpeed = 1;
                    updateXPos(xSpeed);
                } else {
                    hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                    if (airSpeed > 0)
                        resetInAir();
                    else
                        airSpeed = fallSpeedAfterCollision;
                    updateXPos(xSpeed);
                }
            }
        } else {
            updateXPos(xSpeed);
            moving = true;
        }

    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;

    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void loadAnimation() {
        BufferedImage img = LoadSave.getSprite(LoadSave.PLAYER_SPRITE);

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

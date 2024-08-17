package bubbles;

import entities.Entity;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.PlayerBubble.*;
import static utilz.Constants.Directions.*;

public class PlayerBubble extends Entity {
    private int[][] levelData;
    private int[][] windLevelData;

    private boolean isFirstUpdate = true;
    private boolean active = true;
    private int state = PROJECTILE;

    private int normalTimer, redTimer, blinkingTimer, deactivationTimer;
    private long lastTimerUpdate;

    // Movement variables
    private boolean isMoving;
    private int direction;
    private float xSpeed, ySpeed;

    public PlayerBubble(float x, float y, int direction, int[][] levelData, int[][] windLevelData) {
        super(x, y, IMMAGE_W, IMMAGE_H);
        this.direction = direction;
        this.levelData = levelData;
        this.windLevelData = windLevelData;

        initHitbox(HITBOX_W, HITBOX_H);
    }

    public void update() {
        if (isFirstUpdate)
            firstUpdate();


        updateTimers();
        updateAnimationTick();
        setState();

        updatePosition();
    }

    public void firstUpdate() {
        isFirstUpdate = false;

        isMoving = true;
        normalTimer = NORMAL_TIMER;
        redTimer = RED_TIMER;
        blinkingTimer = BLINKING_TIMER;
        //deactivationTimer = DEACTIVATION_TIMER;
        lastTimerUpdate = System.currentTimeMillis();
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick > ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(state))
                animationIndex = 0;
        }
    }

    private void updateTimers() {
        long timeDelta = System.currentTimeMillis() - lastTimerUpdate;
        lastTimerUpdate = System.currentTimeMillis();

        if (state == NORMAL)
            normalTimer -= timeDelta;

        if (state == RED)
            redTimer -= timeDelta;

        if (state == BLINKING)
            blinkingTimer -= timeDelta;
    }

    private void setState() {
        int startAnimation = state;

        if (state == PROJECTILE && animationIndex == 3) {
            state = NORMAL;
        }

        if (state == NORMAL && normalTimer <= 0)
            state = RED;

        if (state == RED && redTimer <= 0)
            state = BLINKING;

        if (state == BLINKING && blinkingTimer <= 0)
            state = POP;

        if (state == POP && animationIndex == 2)
            active = false;

        if (startAnimation != state){
            animationTick = 0;
            animationIndex = 0;
        }
    }

    private void updateDirection() {
        if (state == PROJECTILE)
            return;

        int tileX = (int) (hitbox.x / Game.TILES_SIZE);
        int tileY = (int) (hitbox.y / Game.TILES_SIZE);

        int upX = tileX;
        int upY = tileY - 1;

        int downX = tileX;
        int downY = tileY + 1;

        int leftX = tileX - 1;
        int leftY = tileY;

        int rightX = tileX + 1;
        int rightY = tileY;


    }

    private void updatePosition() {
//        if (state == PROJECTILE) {
//            if (direction == RIGHT)
//                updateXPos(xSpeed, levelData );
//            else
//                updateXPos(-xSpeed, levelData);
//        }

    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
    }

    public void loadWindLevelData(int[][] windLevelData) {
        this.windLevelData = windLevelData;
    }

    public boolean isActive() {
        return active;
    }

    public int getState() {
        return state;
    }
}

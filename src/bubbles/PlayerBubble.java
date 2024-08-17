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

    private boolean isFirstUpdate = true;
    private boolean active;
    private int state;

    private int normalTimer, redTimer, blinkingTimer;
    private long lastTimerUpdate;

    // Movement variables
    private boolean isMoving;
    private int direction;
    private float xSpeed, ySpeed;

    private BufferedImage[][] animations; // TODO: Move to Bubble manager

    public PlayerBubble(float x, float y, int direction) {
        super(x, y, IMMAGE_W, IMMAGE_H);
        this.direction = direction;

        loadAnimation();
        initHitbox(HITBOX_W, HITBOX_H);
    }

    public void draw(Graphics g) {
        g.drawImage(animations[state][animationIndex],  (int) (hitbox.x - DRAWOFFSET_X), (int) (hitbox.y - DRAWOFFSET_Y), width, height, null);
        drawHitbox(g);
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
        state = NORMAL;
        active = true;
        normalTimer = NORMAL_TIMER;
        redTimer = RED_TIMER;
        blinkingTimer = BLINKING_TIMER;
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
        else
            normalTimer = NORMAL_TIMER;

        if (state == RED)
            redTimer -= timeDelta;
        else
            redTimer = RED_TIMER;

        if (state == BLINKING)
            blinkingTimer -= timeDelta;
        else
            blinkingTimer = BLINKING_TIMER;
    }

    private void setState() {
        int startAnimation = state;

        if (state == PROJECTILE && animationIndex == 5)
            state = NORMAL;

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
        if (state == PROJECTILE) {
            if (direction == RIGHT)
                updateXPos(xSpeed, levelData );
            else
                updateXPos(-xSpeed, levelData);
        }

    }

    private void loadAnimation() {
        // TODO: Move to Bubble manager

        BufferedImage img = LoadSave.GetSprite(LoadSave.BUBBLE_BUD_SPRITE);

        animations = new BufferedImage[5][4];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i * DEFAULT_W, j*DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
    }

    public boolean isActive() {
        return active;
    }

    public void setInactive(boolean active) {
        this.active = active;
    }
}

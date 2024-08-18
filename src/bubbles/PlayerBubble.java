package bubbles;

import entities.Entity;
import main.Game;
import utilz.Constants;

import static utilz.HelpMethods.*;
import static utilz.Constants.PlayerBubble.*;
import static utilz.Constants.Directions.*;

public class PlayerBubble extends Entity {
    private int[][] levelData;
    private int[][] windLevelData;

    private boolean isFirstUpdate = true;
    private boolean active = true;
    private int state = PROJECTILE;

    private int normalTimer, redTimer, blinkingTimer;
    private long lastTimerUpdate;

    // Movement variables
    private int direction;

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

        updateDirection();
        updatePosition();
    }

    public void firstUpdate() {
        isFirstUpdate = false;

        normalTimer = NORMAL_TIMER;
        redTimer = RED_TIMER;
        blinkingTimer = BLINKING_TIMER;
        lastTimerUpdate = System.currentTimeMillis();
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick > BUBBLE_ANIMATION_SPEED) {
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

        direction = windLevelData[tileY][tileX];
    }

    private void updatePosition() {
        if (state == PROJECTILE) {
           if (direction == RIGHT) {
               if (CanMoveHere((int) (hitbox.x + PROJECTILE_SPEED), (int) hitbox.y, hitbox.width, hitbox.height, levelData))
                   hitbox.x += PROJECTILE_SPEED;
           }
           else if (CanMoveHere((int) (hitbox.x - PROJECTILE_SPEED), (int) hitbox.y, hitbox.width, hitbox.height, levelData))
               hitbox.x -= PROJECTILE_SPEED;
        }
        else {
            switch (direction) {
                case LEFT -> hitbox.x -= BUBBLE_SPEED;
                case RIGHT -> hitbox.x += BUBBLE_SPEED;
                case UP -> hitbox.y -= BUBBLE_SPEED;
                case DOWN -> hitbox.y += BUBBLE_SPEED;
            }
        }

    }

    public boolean isActive() {
        return active;
    }

    public int getState() {
        return state;
    }
}

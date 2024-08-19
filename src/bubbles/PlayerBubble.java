package bubbles;

import main.Game;
import entities.Entity;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.HelpMethods.*;
import static utilz.Constants.Direction;
import static utilz.Constants.Direction.*;
import static utilz.Constants.PlayerBubble.*;

public class PlayerBubble extends Entity {
    private int[][] levelData;
    private Direction[][] windLevelData;
    private Rectangle2D.Float collisionBox;

    private boolean isFirstUpdate = true;
    private boolean active = true;
    private int state = PROJECTILE;

    private int normalTimer, redTimer, blinkingTimer;
    private long lastTimerUpdate;

    // Movement variables
    private float xSpeed, ySpeed;
    private Direction direction;
    private Direction previousDirection;

    // External Entity Push Variables
    private Direction pushDirection = NONE;
    private float pushSpeed;

    public PlayerBubble(float x, float y, Direction direction, int[][] levelData, Direction[][] windLevelData) {
        super(x, y, IMMAGE_W, IMMAGE_H);
        this.direction = direction;
        this.previousDirection = direction;
        this.levelData = levelData;
        this.windLevelData = windLevelData;

        initHitbox(HITBOX_W, HITBOX_H);
        initCollisionBox();
    }

    private void initCollisionBox() {
        collisionBox = new Rectangle2D.Float(hitbox.x + COLLISIONBOX_DRAWOFFSET_X, hitbox.y + COLLISIONBOX_DRAWOFFSET_Y, COLLISIONBOX_W, COLLISIONBOX_H);
    }

    public void drawCollisionBox(Graphics g) {
        // For debugging purposes
        g.setColor(Color.BLUE);
        g.drawRect((int) collisionBox.x, (int) collisionBox.y, (int) collisionBox.width, (int) collisionBox.height);
    }

    private void updateCollisionBox() {
        collisionBox.x = hitbox.x + COLLISIONBOX_DRAWOFFSET_X;
        collisionBox.y = hitbox.y + COLLISIONBOX_DRAWOFFSET_Y;
    }

    public void update() {
        if (isFirstUpdate)
            firstUpdate();

        updateTimers();
        updateAnimationTick();
        setState();

        updateDirection();
        updatePosition();
        updateCollisionBox();
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

        if (direction != NONE)
            previousDirection = direction;

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

        if (state != PROJECTILE && state != POP){
            xSpeed = 0;
            ySpeed = 0;

            // Wind Movement
            switch (direction) {
                case LEFT -> xSpeed -= BUBBLE_SPEED;
                case RIGHT -> xSpeed += BUBBLE_SPEED;
                case UP -> ySpeed -= BUBBLE_SPEED;
                case DOWN -> ySpeed += BUBBLE_SPEED;
                case NONE -> bubbleShaking();
            }

            // External Push Movement
            if (pushDirection == LEFT && !IsTilePerimeterWall((int) (hitbox.x - pushSpeed) / Game.TILES_SIZE))
                xSpeed -= pushSpeed;

            if (pushDirection == RIGHT && !IsTilePerimeterWall((int) (hitbox.x + pushSpeed + hitbox.width) / Game.TILES_SIZE))
                xSpeed += pushSpeed;

            if (pushDirection == DOWN)
                ySpeed -= pushSpeed;

            // Update Position
            hitbox.x += xSpeed;
            hitbox.y += ySpeed;
            pushSpeed = 0;
        }
    }

    private void bubbleShaking() {

        if (previousDirection == UP) {
            if (animationIndex % 2 == 0)
                ySpeed -= SHAKING_SPEED;
            else
                ySpeed += SHAKING_SPEED;
        }

        if (previousDirection == DOWN) {
            if (animationIndex % 2 == 0)
                ySpeed += SHAKING_SPEED;
            else
                ySpeed -= SHAKING_SPEED;
        }

        if (previousDirection == LEFT) {
            if (animationIndex % 2 == 0)
                xSpeed -= SHAKING_SPEED;
            else
                xSpeed += SHAKING_SPEED;
        }

        if (previousDirection == RIGHT) {
            if (animationIndex % 2 == 0)
                xSpeed += SHAKING_SPEED;
            else
                xSpeed -= SHAKING_SPEED;
        }
    }

    public void pop() {
        if (state != PROJECTILE && state != POP) {
            state = POP;
            animationIndex = 0;
            animationTick = 0;
            BubbleManager bubbleManager = BubbleManager.getInstance();
            bubbleManager.triggerChainExplosion(this);
        }
    }

    public Point getCenter() {
        float x = hitbox.x + hitbox.width / 2;
        float y = hitbox.y + hitbox.height / 2;

        return new Point((int) x, (int) y);
    }

    public boolean isActive() {
        return active;
    }

    public int getState() {
        return state;
    }

    public Rectangle2D.Float getCollisionBox() {
        return collisionBox;
    }

    public void setPush(Direction pushDirection, float pushSpeed) {
        this.pushDirection = pushDirection;
        this.pushSpeed = pushSpeed;
    }
}

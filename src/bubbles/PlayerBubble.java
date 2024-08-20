package bubbles;

import main.Game;
import entities.Entity;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import static utilz.HelpMethods.*;
import static utilz.Constants.Direction;
import static utilz.Constants.Direction.*;
import static utilz.Constants.PlayerBubble.*;

public class PlayerBubble extends Entity {
    private int[][] levelData;
    private Direction[][] windLevelData;
    private Rectangle2D.Float internalCollisionBox;
    private Ellipse2D.Float externalCollisionBox;

    private boolean isFirstUpdate = true;
    private boolean active = true;
    private int state = PROJECTILE;

    private int normalTimer, redTimer, blinkingTimer;
    private long lastTimerUpdate;

    // Movement variables
    private float xSpeed, ySpeed;
    private Direction direction;
    private Direction previousDirection;

    public PlayerBubble(float x, float y, Direction direction, int[][] levelData, Direction[][] windLevelData) {
        super(x, y, IMMAGE_W, IMMAGE_H);
        this.direction = direction;
        this.previousDirection = direction;
        this.levelData = levelData;
        this.windLevelData = windLevelData;

        initHitbox(HITBOX_W, HITBOX_H);
        initCollisionBoxes();
    }

    private void initCollisionBoxes() {
        internalCollisionBox = new Rectangle2D.Float(hitbox.x + INTERNAL_BOX_OFFSET_X, hitbox.y + INTERNAL_BOX_OFFSET_Y, INTERNAL_BOX_W, INTERNAL_BOX_H);
        externalCollisionBox = new Ellipse2D.Float(hitbox.x + EXTERNAL_BOX_OFFSET_X, hitbox.y, EXTERNAL_BOX_W, EXTERNAL_BOX_H);
    }

    public void drawCollisionBox(Graphics g) {
        // For debugging purposes
        g.setColor(Color.GREEN);
        g.drawRect((int) internalCollisionBox.x, (int) internalCollisionBox.y, (int) internalCollisionBox.width, (int) internalCollisionBox.height);

        g.setColor(Color.BLUE);
        g.drawOval((int) externalCollisionBox.x, (int) externalCollisionBox.y, (int) externalCollisionBox.width, (int) externalCollisionBox.height);
    }

    private void updateCollisionBoxes() {
        internalCollisionBox.x = hitbox.x + INTERNAL_BOX_OFFSET_X;
        internalCollisionBox.y = hitbox.y + INTERNAL_BOX_OFFSET_Y;
        externalCollisionBox.x = hitbox.x + EXTERNAL_BOX_OFFSET_X;
        externalCollisionBox.y = hitbox.y;
    }

    public void update() {
        if (isFirstUpdate)
            firstUpdate();

        updateTimers();
        updateAnimationTick();
        setState();

        updateDirection();
        updatePosition();
        updateCollisionBoxes();
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

            // Update Position
            hitbox.x += xSpeed;
            hitbox.y += ySpeed;
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

    public Rectangle2D.Float getInternalCollisionBox() {
        return internalCollisionBox;
    }

    public Ellipse2D.Float getExternalCollisionBox() {
        return externalCollisionBox;
    }
}

package bubbles;

import entities.Entity;
import entities.Player;
import levels.LevelManager;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Bubble.*;
import static utilz.Constants.Direction;
import static utilz.Constants.Direction.*;

public abstract class Bubble extends Entity {

    protected int[][] levelData;
    protected Direction[][] windLevelData;
    protected Rectangle2D.Float internalCollisionBox;
    protected Ellipse2D.Float externalCollisionBox;

    protected boolean isFirstUpdate = true;
    protected boolean active = true;
    protected boolean popped = false;
    protected int state = NORMAL;
    protected int previousState = NORMAL;

    protected int normalTimer, redTimer, blinkingTimer;
    protected long lastTimerUpdate;

    // Movement variables
    protected float xSpeed, ySpeed;
    protected Direction direction;
    protected Direction previousDirection;

    public Bubble(float x, float y, Direction direction) {
        super(x, y, IMMAGE_W, IMMAGE_H);
        this.direction = direction;
        this.previousDirection = direction;
        initHitbox(HITBOX_W, HITBOX_H);

        loadLevelData();
        initCollisionBoxes();
    }

    private void initCollisionBoxes() {
        internalCollisionBox = new Rectangle2D.Float(hitbox.x + INTERNAL_BOX_OFFSET_X, hitbox.y + INTERNAL_BOX_OFFSET_Y, INTERNAL_BOX_W, INTERNAL_BOX_H);
        externalCollisionBox = new Ellipse2D.Float(hitbox.x + EXTERNAL_BOX_OFFSET_X, hitbox.y, EXTERNAL_BOX_W, EXTERNAL_BOX_H);
    }

    public abstract void draw(Graphics g);

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

        if (state == DEAD)
            updateDeadAnimation();

        else {
            updateDirection();
            updatePosition();
        }

        updateCollisionBoxes();
    }

    abstract void updateDeadAnimation();

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

        if (state == NORMAL && normalTimer <= 0) {
            previousState = state;
            state = RED;
        }

        if (state == RED && redTimer <= 0) {
            previousState = state;
            state = BLINKING;
        }

        if (state == BLINKING && blinkingTimer <= 0) {
            previousState = state;
            state = POP_RED;

            if (this instanceof EnemyBubble)
                ((EnemyBubble) this).respawnEnemy();
        }

        if (state == POP_RED || state == POP_NORMAL)
            if (animationIndex == 2)
                active = false;

        if (startAnimation != state){
            animationTick = 0;
            animationIndex = 0;
        }
    }

    private void updateDirection() {

        if (direction != NONE)
            previousDirection = direction;

        direction = windLevelData[getTileY()][getTileX()];
    }

    private void updatePosition() {

        if (state != POP_NORMAL || state != POP_RED) {
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

    private void loadLevelData() {
        this.levelData = LevelManager.getInstance().getCurrentLevel().getLevelData();
        this.windLevelData = LevelManager.getInstance().getCurrentLevel().getWindDirectionData();
    }

    public abstract void playerPop(Player player, int EnemyBubblePopCounter, ChainExplosionManager chainExplosionManager);

    public Point getCenter() {
        float x = hitbox.x + hitbox.width / 2;
        float y = hitbox.y + hitbox.height / 2;

        return new Point((int) x, (int) y);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int getState() {
        return state;
    }

    public Direction getDirection() {
        return direction;
    }

    public Rectangle2D.Float getInternalCollisionBox() {
        return internalCollisionBox;
    }

    public Ellipse2D.Float getExternalCollisionBox() {
        return externalCollisionBox;
    }

    protected void changeDirection() {
        direction = (direction == LEFT) ? RIGHT : LEFT;
    }

}
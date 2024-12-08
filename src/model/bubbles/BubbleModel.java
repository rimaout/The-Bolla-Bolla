package model.bubbles;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import model.entities.EntityModel;
import model.entities.PlayerModel;
import model.levels.LevelManagerModel;
import model.Constants;
import model.PlayingTimer;

import static java.lang.Math.abs;
import static model.Constants.Bubble.*;
import static model.Constants.Direction;
import static model.Constants.Direction.*;
import static model.entities.HelpMethods.IsEntityInsideMap;

public abstract class BubbleModel extends EntityModel {

    protected final LevelManagerModel levelManagerModel = LevelManagerModel.getInstance();
    protected final PlayingTimer timer = PlayingTimer.getInstance();

    protected Rectangle2D.Float internalCollisionBox;
    protected Ellipse2D.Float externalCollisionBox;

    protected boolean active = true;
    protected boolean popped = false;
    protected BubbleType bubbleType;

    protected int state = NORMAL;
    protected int previousState = NORMAL;

    protected int normalTimer = NORMAL_TIMER;
    protected int redTimer = RED_TIMER;
    protected int blinkingTimer = BLINKING_TIMER;
    protected int popTimer = POP_TIMER;
    protected int shakeTimer = SHAKE_TIMER;
    protected boolean shake;

    protected float xSpeed, ySpeed;
    protected Direction direction;
    protected Direction previousDirection;

    public BubbleModel(float x, float y, Direction direction) {
        super(x, y, W, H);
        this.direction = direction;
        this.previousDirection = direction;

        initHitbox(HITBOX_W, HITBOX_H);
        initCollisionBoxes();
    }

    public abstract void update();

    public abstract void checkCollisionWithPlayer(PlayerModel playerModel);

    protected void initCollisionBoxes() {
        internalCollisionBox = new Rectangle2D.Float(hitbox.x + INTERNAL_BOX_OFFSET_X, hitbox.y + INTERNAL_BOX_OFFSET_Y, INTERNAL_BOX_W, INTERNAL_BOX_H);
        externalCollisionBox = new Ellipse2D.Float(hitbox.x + EXTERNAL_BOX_OFFSET_X, hitbox.y, EXTERNAL_BOX_W, EXTERNAL_BOX_H);
    }

    protected void updateTimers() {
        shakeTimer -= (int) timer.getTimeDelta();

        if (!shake & shakeTimer <= 0) {
            shake = true;
            shakeTimer = SHAKE_TIMER;
        }
    }

    protected void updateCollisionBoxes() {
        internalCollisionBox.x = hitbox.x + INTERNAL_BOX_OFFSET_X;
        internalCollisionBox.y = hitbox.y + INTERNAL_BOX_OFFSET_Y;
        externalCollisionBox.x = hitbox.x + EXTERNAL_BOX_OFFSET_X;
        externalCollisionBox.y = hitbox.y;
    }

    protected void updateDirection() {

        if (!IsEntityInsideMap(hitbox))
            return;

        if (direction != NONE)
            previousDirection = direction;

        direction = levelManagerModel.getWindDirectionData()[getTileY()][getTileX()];
    }

    protected void updatePosition() {

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

    protected void pacManEffect() {
        if (direction == DOWN && getTileY() == Constants.TILES_IN_HEIGHT + 1)
            hitbox.y = -2 * Constants.TILES_SIZE;

        if (direction == UP && getTileY() == -1)
            hitbox.y = (Constants.TILES_IN_HEIGHT + 2) * Constants.TILES_SIZE;
    }

    public void bounceDown() {
        // Make the bubble bounce down, used when the player jumps on the bubble
        hitbox.y += 2 * Constants.SCALE;
    }

    protected void bubbleShaking() {

        if (previousDirection == UP) {
            if (shake)
                ySpeed -= SHAKING_SPEED;
            else
                ySpeed += SHAKING_SPEED;
        }

        if (previousDirection == DOWN) {
            if (shake)
                ySpeed += SHAKING_SPEED;
            else
                ySpeed -= SHAKING_SPEED;
        }

        if (previousDirection == LEFT) {
            if (shake)
                xSpeed -= SHAKING_SPEED;
            else
                xSpeed += SHAKING_SPEED;
        }

        if (previousDirection == RIGHT) {
            if (shake)
                xSpeed += SHAKING_SPEED;
            else
                xSpeed -= SHAKING_SPEED;
        }
    }

    protected boolean isPlayerPoppingBubble(PlayerModel playerModel) {
        // if player is colliding with the internal box of the bubble, the bubble is popped
        return internalCollisionBox.intersects(playerModel.getHitbox());
    }

    protected boolean isPlayerJumpingOnBubble(PlayerModel playerModel) {
        return hitbox.y > playerModel.getHitbox().y && playerModel.isJumpActive();
    }

    protected boolean isPlayerTouchingBubble(PlayerModel playerModel) {
        return externalCollisionBox.intersects(playerModel.getHitbox());
    }

    protected void handlePlayerPush(PlayerModel playerModel) {
        int correctionOffset = 5 * Constants.SCALE;

        // left push
        if (hitbox.x + hitbox.width - correctionOffset <= playerModel.getHitbox().x)
            hitbox.x -= abs(playerModel.getXSpeed());

            // right push
        else if (hitbox.x + correctionOffset >= playerModel.getHitbox().x + playerModel.getHitbox().width)
            hitbox.x += abs(playerModel.getXSpeed());
    }

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

    protected void changeDirection() {
        direction = (direction == LEFT) ? RIGHT : LEFT;
    }

    public boolean isPopped() {
        return popped;
    }

    public BubbleType getBubbleType() {
        return bubbleType;
    }
}
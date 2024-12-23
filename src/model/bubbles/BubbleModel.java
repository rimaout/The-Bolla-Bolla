package model.bubbles;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import model.utilz.Constants;
import model.utilz.PlayingTimer;
import model.entities.EntityModel;
import model.entities.PlayerModel;
import model.levels.LevelManagerModel;

import static java.lang.Math.abs;
import static model.utilz.Constants.Bubble.*;
import static model.utilz.Constants.Direction;
import static model.utilz.Constants.Direction.*;
import static model.utilz.HelpMethods.IsEntityInsideMap;

/**
 * Represents a bubble created by the player.
 *
 * <p>This abstract class provides the basic structure and behavior for bubbles created by the player.
 * It includes methods for updating the bubble's state, handling collisions with the player, and managing timers.
 */
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

    /**
     * Constructs a new BubbleModel.
     *
     * @param x the starting x coordinate
     * @param y the starting y coordinate
     * @param direction the starting direction
     */
    public BubbleModel(float x, float y, Direction direction) {
        super(x, y, W, H);
        this.direction = direction;
        this.previousDirection = direction;

        initHitbox(HITBOX_W, HITBOX_H);
        initCollisionBoxes();
    }

    /**
     * Updates the bubble model.
     */
    public abstract void update();

    /**
     * Checks for collision with the player & contains the logic to handle the collisions.
     *
     * @param playerModel the player model
     */
    public abstract void checkCollisionWithPlayer(PlayerModel playerModel);

    protected void initCollisionBoxes() {
        internalCollisionBox = new Rectangle2D.Float(hitbox.x + INTERNAL_BOX_OFFSET_X, hitbox.y + INTERNAL_BOX_OFFSET_Y, INTERNAL_BOX_W, INTERNAL_BOX_H);
        externalCollisionBox = new Ellipse2D.Float(hitbox.x + EXTERNAL_BOX_OFFSET_X, hitbox.y, EXTERNAL_BOX_W, EXTERNAL_BOX_H);
    }

    /**
     * Updates the timers.
     */
    protected void updateTimers() {
        shakeTimer -= (int) timer.getTimeDelta();

        if (!shake & shakeTimer <= 0) {
            shake = true;
            shakeTimer = SHAKE_TIMER;
        }
    }

    /**
     * Updates the hitbox of the collision boxes to align them with bubble position.
     */
    protected void updateCollisionBoxes() {
        internalCollisionBox.x = hitbox.x + INTERNAL_BOX_OFFSET_X;
        internalCollisionBox.y = hitbox.y + INTERNAL_BOX_OFFSET_Y;
        externalCollisionBox.x = hitbox.x + EXTERNAL_BOX_OFFSET_X;
        externalCollisionBox.y = hitbox.y;
    }
    /**
     * Updates the direction of the bubble based on the current wind direction data from the level manager.
     *
     * <p>If the bubble is outside the map, the direction is not updated.
     * <p>If the current direction is not NONE, the previous direction is updated to the current direction.
     * <p>The new direction is then set based on the wind direction data at the bubble's current tile position.
     */
    protected void updateDirection() {

        if (!IsEntityInsideMap(hitbox))
            return;

        if (direction != NONE)
            previousDirection = direction;

        direction = levelManagerModel.getWindDirectionData()[getTileY()][getTileX()];
    }

    /**
     * Updates the position of the bubble based on its current state and direction.
     *
     * <p>If the bubble is not popped it calculates the x and y speeds based oni the wind direction and updates the bubble's position accordingly.
     * <p>If the direction is NONE, the bubble will shake in place.
     */
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

    /**
     * Applies the Pac-Man effect to the bubble, allowing it to wrap around the screen vertically.
     *
     * <p>If the bubble moves beyond the bottom of the screen, it reappears at the top.
     * <p>If the bubble moves beyond the top of the screen, it reappears at the bottom.
     */
    protected void pacManEffect() {
        if (direction == DOWN && getTileY() == Constants.TILES_IN_HEIGHT + 1)
            hitbox.y = -2 * Constants.TILES_SIZE;

        if (direction == UP && getTileY() == -1)
            hitbox.y = (Constants.TILES_IN_HEIGHT + 2) * Constants.TILES_SIZE;
    }

    /**
     * Makes the bubble bounce down when the player jumps on it.
     *
     * <p>This method is called when the player jumps on the bubble, causing the bubble to move downwards by a small amount.
     */
    public void bounceDown() {
        // Make the bubble bounce down, used when the player jumps on the bubble
        hitbox.y += 2 * Constants.SCALE;
    }

    /**
     * Makes the bubble shake in place when the wind direction is NONE.
     *
     * <p>The shaking direction is determined by the previous direction of the bubble.
     * <p>If the previous direction was UP or DOWN, the bubble will shake vertically.
     * <p>If the previous direction was LEFT or RIGHT, the bubble will shake horizontally.
     * <p>The shaking speed alternates based on the shake state.
     */
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

    /**
     * Checks if the player is popping the bubble.
     *
     * <p>If the player is colliding with the internal collision box of the bubble, the bubble is considered popped.
     *
     * @param playerModel the player model
     * @return true if the player is colliding with the internal collision box of the bubble, false otherwise*/
    protected boolean isPlayerPoppingBubble(PlayerModel playerModel) {
        // if player is colliding with the internal box of the bubble, the bubble is popped
        return internalCollisionBox.intersects(playerModel.getHitbox());
    }

    /**
     * Checks if the player is jumping on the bubble.
     *
     * <p>This method determines if the player is currently jumping on the bubble by comparing the y-coordinate of the bubble's hitbox with the player's hitbox and checking if the player's jump is active.
     *
     * @param playerModel the player model
     * @return true if the player is jumping on the bubble, false otherwise
     */
    protected boolean isPlayerJumpingOnBubble(PlayerModel playerModel) {
        return hitbox.y > playerModel.getHitbox().y && playerModel.isJumpActive();
    }

    /**
     * Checks if the player is touching the bubble.
     *
     * <p>This method determines if the player is currently touching the bubble by checking if the player's hitbox intersects with the bubble's external collision box.
     *
     * @param playerModel the player model
     * @return true if the player's hitbox intersects with the bubble's external collision box, false otherwise
     */
    protected boolean isPlayerTouchingBubble(PlayerModel playerModel) {
        return externalCollisionBox.intersects(playerModel.getHitbox());
    }

    /**
     * Handles the player's push interaction with the bubble.
     *
     * <p>This method adjusts the bubble's position based on the player's push direction.
     * <p>If the player is pushing from the left, the bubble moves to the left.
     * <p>If the player is pushing from the right, the bubble moves to the right.
     *
     * @param playerModel the player model
     */
    protected void handlePlayerPush(PlayerModel playerModel) {
        int correctionOffset = 5 * Constants.SCALE;

        // left push
        if (hitbox.x + hitbox.width - correctionOffset <= playerModel.getHitbox().x)
            hitbox.x -= abs(playerModel.getXSpeed());

            // right push
        else if (hitbox.x + correctionOffset >= playerModel.getHitbox().x + playerModel.getHitbox().width)
            hitbox.x += abs(playerModel.getXSpeed());
    }

    /**
     * Gets the center point of the bubble's hitbox.
     *
     * <p>This method calculates the center coordinates of the bubble's hitbox and returns them as a Point object.
     *
     * @return the center point of the bubble's hitbox
     */
    public Point getCenter() {
        float x = hitbox.x + hitbox.width / 2;
        float y = hitbox.y + hitbox.height / 2;

        return new Point((int) x, (int) y);
    }

    /**
     * Changes the direction of the bubble, to the opposite of the current direction.
     */
    protected void changeDirection() {
        direction = (direction == LEFT) ? RIGHT : LEFT;
    }

    // Setters Methods

    public void setActive(boolean active) {
        this.active = active;
    }

    // Getters Methods

    public boolean isActive() {
        return active;
    }

    public int getState() {
        return state;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isPopped() {
        return popped;
    }

    public BubbleType getBubbleType() {
        return bubbleType;
    }
}
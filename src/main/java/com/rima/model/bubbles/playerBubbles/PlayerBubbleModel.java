package com.rima.model.bubbles.playerBubbles;

import com.rima.model.utilz.Constants;
import com.rima.model.bubbles.BubbleModel;
import com.rima.model.entities.PlayerModel;

import static com.rima.model.utilz.Constants.Bubble.*;

/**
 * Abstract model for player bubbles.
 *
 * <p>This class provides the basic structure and behavior for bubbles created by the player.
 * It includes methods for updating the bubble's state, handling collisions with the player, and managing timers.
 */
public abstract class PlayerBubbleModel extends BubbleModel {
    protected final PlayerBubblesManagerModel bubbleManager = PlayerBubblesManagerModel.getInstance();

    /**
     * Constructs a new PlayerBubbleModel.
     *
     * @param x the starting x coordinate
     * @param y the starting y coordinate
     * @param direction the starting direction
     */
    public PlayerBubbleModel(float x, float y, Constants.Direction direction) {
        super(x, y, direction);
    }

    /**
     * Updates the bubble's movement after it has been popped by the player.
     */
    protected abstract void updateDeadAction();

    /**
     * Pops the bubble and updates its state.
     */
    public abstract void pop();

    /**
     * Handles the player popping the bubble, this overloaded method is used only by the {@link ChainReactionManager}.
     *
     * @param playerModel the player model
     * @param EnemyBubblePopCounter the number of enemy bubbles popped
     * @param chainReactionManager the chain explosion manager
     */
    public abstract void playerPop(PlayerModel playerModel, int EnemyBubblePopCounter, ChainReactionManager chainReactionManager);

    /**
     * Updates the bubble's state and position.
     */
    @Override
    public void update() {
        initLevelManager();
        updateTimers();
        setState();

        if (state == DEAD)
            updateDeadAction();

        else {
            updateDirection();
            updatePosition();
        }

        updateCollisionBoxes();
        pacManEffect();
    }

    /**
     * Checks for collisions with the player and handles the interaction.
     *
     * @param playerModel the player model
     */
    @Override
    public void checkCollisionWithPlayer(PlayerModel playerModel) {
        if (!playerModel.isActive())
            return;

        if (!active || state == DEAD)
            return;

        // check if bubble pop
        if (bubbleManager.getPopTimer() <= 0 && isPlayerPoppingBubble(playerModel)) {
            bubbleManager.startChainReaction(this);
            return;
        }

        if (isPlayerTouchingBubble(playerModel)) {

            if (isPlayerJumpingOnBubble(playerModel)) {
                bounceDown();
                playerModel.jumpOnBubble();
                return;
            }

            handlePlayerPush(playerModel);
        }
    }

    /**
     * Updates the timers for the bubble's state transitions.
     */
    @Override
    protected void updateTimers() {
        super.updateTimers();

        if (state == NORMAL)
            normalTimer -= (int) timer.getTimeDelta();

        if (state == RED)
            redTimer -= (int) timer.getTimeDelta();

        if (state == BLINKING)
            blinkingTimer -= (int) timer.getTimeDelta();

        if (state == POP_NORMAL || state == POP_RED)
            popTimer -= (int) timer.getTimeDelta();
    }

    /**
     * Sets the bubble's state based on the timers.
     */
    private void setState() {
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

            if (this instanceof EnemyBubbleModel)
                ((EnemyBubbleModel) this).respawnEnemy();
        }

        if (state == POP_RED || state == POP_NORMAL && popTimer <= 0)
                active = false;
    }
}
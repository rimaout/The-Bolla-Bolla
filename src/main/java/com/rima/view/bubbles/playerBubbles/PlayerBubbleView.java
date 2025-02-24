package com.rima.view.bubbles.playerBubbles;

import com.rima.model.bubbles.playerBubbles.PlayerBubbleModel;
import com.rima.view.bubbles.BubbleView;
import com.rima.model.bubbles.BubbleModel;

import java.awt.*;

import static com.rima.model.utilz.Constants.Bubble.NORMAL;

/**
 * The PlayerBubbleView class is an abstract class that represents the view for a {@link PlayerBubbleModel}.
 * It handles updating the bubble's animation and drawing the bubble on the screen.
 */
public abstract class PlayerBubbleView extends BubbleView {
    protected int previousModelState = NORMAL;

    /**
     * Constructs a PlayerBubbleView with the specified BubbleModel.
     *
     * @param bubbleModel the model for the bubble entity
     */
    public PlayerBubbleView(BubbleModel bubbleModel) {
        super(bubbleModel);
    }

    /**
     * Draws the player bubble on the screen.
     *
     * @param g the Graphics object to draw with
     */
    public abstract void draw(Graphics g);

    /**
     * Updates the bubble's state and animation.
     */
    @Override
    public void update() {
        updateAnimationTick();
        checkForAnimationChange();
        checkAnimationResetAfterPop();
    }

    /**
     * Checks if the model state has changed and resets the animation index and tick if it has.
     */
    private void checkForAnimationChange() {
        // method that check if model state has changed and if so resets animation index and tick

        if (bubbleModel.getState() != previousModelState) {
            previousModelState = bubbleModel.getState();
            animationIndex = 0;
            animationTick = 0;
        }
    }

    /**
     * Checks and resets the animation after the bubble is popped by the player.
     */
    private void checkAnimationResetAfterPop() {
        if (animationReset)
            return;

        // Reset animation index and tick when bubble is popped by player
        if (bubbleModel.isPopped()) {
            animationReset = true;
            animationIndex = 0;
            animationTick = 0;
        }
    }
}
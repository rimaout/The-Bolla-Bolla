package view.bubbles;

import model.bubbles.BubbleModel;
import java.awt.*;

import static view.utilz.Constants.Bubble.BUBBLE_ANIMATION_SPEED;
import static view.utilz.Constants.Bubble.getPlayerBubbleSpriteAmount;

/**
 * The BubbleView is a abstract class that represents the view for a {@link BubbleModel}.
 * It handles updating the bubble's animation and drawing the bubble on the screen.
 */
public abstract class BubbleView{
    protected BubbleModel bubbleModel;

    protected int animationIndex, animationTick;
    protected boolean animationReset;   // used to reset animation index and tick after bubble is popped
    protected boolean soundPlayed;

    /**
     * Constructs a BubbleView with the specified BubbleModel.
     *
     * @param bubbleModel the model for the bubble entity
     */
    public BubbleView(BubbleModel bubbleModel) {
        this.bubbleModel = bubbleModel;
    }

    /**
     * Updates the bubble's state and animation.
     */
    public abstract void update();

    /**
     * Draws the bubble on the screen.
     *
     * @param g the Graphics object to draw with
     */
    public abstract void draw(Graphics g);

    /**
     * Updates the animation tick and index based on the animation speed.
     */
    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick > BUBBLE_ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getPlayerBubbleSpriteAmount(bubbleModel.getState()))
                animationIndex = 0;
        }
    }

    /**
     * Checks if the bubble is active.
     *
     * @return true if the bubble is active, false otherwise
     */
    public boolean isActive() {
        return bubbleModel.isActive();
    }

    /**
     * Returns the BubbleModel associated with this view.
     *
     * @return the BubbleModel associated with this view
     */
    public BubbleModel getBubbleModel() {
        return bubbleModel;
    }
}
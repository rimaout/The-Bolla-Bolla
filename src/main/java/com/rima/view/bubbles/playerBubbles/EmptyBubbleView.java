package com.rima.view.bubbles.playerBubbles;

import com.rima.model.bubbles.BubbleModel;
import com.rima.model.bubbles.playerBubbles.EmptyBubbleModel;

import java.awt.*;

import static com.rima.model.utilz.Constants.Bubble.*;

/**
 * The EmptyBubbleView class represents the view for an {@link EmptyBubbleModel}.
 * It handles drawing the empty bubble on the screen.
 */
public class EmptyBubbleView extends PlayerBubbleView{
    private PlayerBubblesManagerView playerBubblesManagerView = PlayerBubblesManagerView.getInstance();

    /**
     * Constructs an EmptyBubbleView with the specified BubbleModel.
     *
     * @param bubbleModel the model for the bubble entity
     */
    public EmptyBubbleView(BubbleModel bubbleModel) {
        super(bubbleModel);
    }

    /**
     * Draws the empty bubble on the screen.
     *
     * @param g the Graphics object to draw with
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(playerBubblesManagerView.getPlayerBubbleSprites()[bubbleModel.getState()][animationIndex], (int) (bubbleModel.getHitbox().x - HITBOX_OFFSET_X), (int) (bubbleModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);
    }
}
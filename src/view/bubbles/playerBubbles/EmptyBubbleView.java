package view.bubbles.playerBubbles;

import model.bubbles.BubbleModel;
import model.bubbles.playerBubbles.EmptyBubbleModel;

import java.awt.*;

import static model.utilz.Constants.Bubble.*;
import static model.utilz.Constants.Bubble.H;

/**
 * The EmptyBubbleView class represents the view for an {@link EmptyBubbleModel}.
 * It handles drawing the empty bubble on the screen.
 */
public class EmptyBubbleView extends PlayerBubbleView{

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
        g.drawImage(PlayerBubblesManagerView.getInstance().getPlayerBubbleSprites()[bubbleModel.getState()][animationIndex], (int) (bubbleModel.getHitbox().x - HITBOX_OFFSET_X), (int) (bubbleModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);
    }
}
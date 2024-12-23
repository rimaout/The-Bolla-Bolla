package view.bubbles.specialBubbles;

import model.bubbles.specialBubbles.LightningBubbleModel;
import view.bubbles.BubbleView;
import model.bubbles.BubbleModel;

import java.awt.*;

import static model.utilz.Constants.Bubble.*;
import static model.utilz.Constants.Bubble.H;

/**
 * The LightningBubbleView class represents the view for a {@link LightningBubbleModel}.
 * It handles updating the bubble's animation and drawing the bubble on the screen.
 */
public class LightningBubbleView extends BubbleView {
    SpecialBubbleManagerView bubbleManager = SpecialBubbleManagerView.getInstance();

    /**
     * Constructs a LightningBubbleView with the specified BubbleModel.
     *
     * @param bubbleModel the model for the bubble entity
     */
    public LightningBubbleView(BubbleModel bubbleModel) {
        super(bubbleModel);
    }

    /**
     * Updates the bubble's animation state.
     */
    @Override
    public void update() {
        updateAnimationTick();
    }

    /**
     * Draws the lightning bubble on the screen.
     *
     * @param g the Graphics object to draw with
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(bubbleManager.getLightningBubbleSprites()[0][0], (int) (bubbleModel.getHitbox().x - HITBOX_OFFSET_X), (int) (bubbleModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);
    }
}
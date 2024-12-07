package view.bubbles.specialBubbles;

import model.bubbles.BubbleModel;
import view.bubbles.BubbleView;

import java.awt.*;

import static model.Constants.Bubble.*;
import static model.Constants.Bubble.H;

public class LightningBubbleView extends BubbleView {
    SpecialBubbleManagerView bubbleManager = SpecialBubbleManagerView.getInstance();

    public LightningBubbleView(BubbleModel bubbleModel) {
        super(bubbleModel);
    }

    @Override
    public void update() {
        updateAnimationTick();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bubbleManager.getLightningBubbleSprites()[0][0], (int) (bubbleModel.getHitbox().x - HITBOX_OFFSET_X), (int) (bubbleModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);
    }
}

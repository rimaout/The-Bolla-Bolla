package view.bubbles.specialBubbles;

import model.bubbles.BubbleModel;
import view.bubbles.BubbleView;

import java.awt.*;

import static model.utilz.Constants.Bubble.*;

public class WaterBubbleView extends BubbleView {
    private final SpecialBubbleManagerView bubbleManager = SpecialBubbleManagerView.getInstance();

    public WaterBubbleView(BubbleModel bubbleModel) {
        super(bubbleModel);
    }

    @Override
    public void update() {
        updateAnimationTick();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bubbleManager.getWaterBubbleSprites()[0][0], (int) (bubbleModel.getHitbox().x - HITBOX_OFFSET_X), (int) (bubbleModel.getHitbox().y - HITBOX_OFFSET_Y), IMAGE_W, IMAGE_H, null);
    }
}

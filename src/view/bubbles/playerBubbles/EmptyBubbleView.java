package view.bubbles.playerBubbles;

import model.bubbles.BubbleModel;

import java.awt.*;

import static model.utilz.Constants.Bubble.*;
import static model.utilz.Constants.Bubble.H;

public class EmptyBubbleView extends PlayerBubbleView{
    public EmptyBubbleView(BubbleModel bubbleModel) {
        super(bubbleModel);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(PlayerBubblesManagerView.getInstance().getPlayerBubbleSprites()[bubbleModel.getState()][animationIndex], (int) (bubbleModel.getHitbox().x - HITBOX_OFFSET_X), (int) (bubbleModel.getHitbox().y - HITBOX_OFFSET_Y), W, H, null);
    }
}

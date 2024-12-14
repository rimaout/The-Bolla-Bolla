package view.bubbles;

import model.bubbles.BubbleModel;
import java.awt.*;

import static view.utilz.Constants.Bubble.BUBBLE_ANIMATION_SPEED;
import static view.utilz.Constants.Bubble.getPlayerBubbleSpriteAmount;

public abstract class BubbleView{
    protected BubbleModel bubbleModel;

    protected int animationIndex, animationTick;
    protected boolean animationReset;   // used to reset animation index and tick after bubble is popped
    protected boolean soundPlayed;

    public BubbleView(BubbleModel bubbleModel) {
        this.bubbleModel = bubbleModel;
    }

    public abstract void update();
    public abstract void draw(Graphics g);

    protected void updateAnimationTick() {
        animationTick++;
        if (animationTick > BUBBLE_ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getPlayerBubbleSpriteAmount(bubbleModel.getState()))
                animationIndex = 0;
        }
    }

    public boolean isActive() {
        return bubbleModel.isActive();
    }

    public BubbleModel getBubbleModel() {
        return bubbleModel;
    }
}
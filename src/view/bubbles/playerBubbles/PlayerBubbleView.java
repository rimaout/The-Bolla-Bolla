package view.bubbles.playerBubbles;

import view.bubbles.BubbleView;
import model.bubbles.BubbleModel;

import java.awt.*;

import static model.utilz.Constants.Bubble.*;

public abstract class PlayerBubbleView extends BubbleView {
    protected int previousModelState = NORMAL;

    public abstract void draw(Graphics g);

    public PlayerBubbleView(BubbleModel bubbleModel) {
        super(bubbleModel);
    }

    @Override
    public void update() {
        updateAnimationTick();
        checkForAnimationChange();
        checkAnimationResetAfterPop();
    }

    private void checkForAnimationChange() {
        // method that check if model state has changed and if so resets animation index and tick

        if (bubbleModel.getState() != previousModelState) {
            previousModelState = bubbleModel.getState();
            animationIndex = 0;
            animationTick = 0;
        }
    }

    private void checkAnimationResetAfterPop() {
        if (animationReset)
            return;

        // Reset animation index and tick when bubble is popped by player
        else if (bubbleModel.isPopped()) {
            animationReset = true;
            animationIndex = 0;
            animationTick = 0;
        }
    }
}
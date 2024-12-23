package model.bubbles.playerBubbles;

import model.entities.PlayerModel;

import static model.utilz.Constants.Bubble.*;
import static model.utilz.Constants.Bubble.BubbleType.EMPTY_BUBBLE;
import static model.utilz.Constants.Direction;

/**
 * Represents an empty bubble created by the player.
 *
 * <p>This class extends the PlayerBubbleModel and provides specific behavior for empty bubbles.
 * Empty bubbles do not have any special behavior when they are in the dead state.
 */
public class EmptyBubbleModel extends PlayerBubbleModel {

    /**
     * Constructs a new EmptyBubbleModel.
     *
     * @param x the starting x coordinate
     * @param y the starting y coordinate
     * @param direction the starting direction
     */
    public EmptyBubbleModel(float x, float y, Direction direction) {
        super(x, y, direction);
        this.bubbleType = EMPTY_BUBBLE;
    }

    /**
     * {@inheritDoc} Overrides the updateDeadAction method from {@link PlayerBubbleModel}.
     *
     * <p>Empty bubbles do not have any special behavior, so this method is empty.
     */
    @Override
    protected void updateDeadAction(){
        // Empty implementation, only used by EnemyBubble
    }

    /**
     * {@inheritDoc} Overrides the pop method from {@link PlayerBubbleModel} to pop the empty bubble.
     *
     * <p>Called when the bubble pops and is used to change the state of the bubble to the correct pop state based on the previous state before the bubble was popped.
     * */
    @Override
    public void pop() {
        if (state != POP_NORMAL && state != POP_RED) {

            if (state == RED)
                state = POP_RED;
            else
                state = POP_NORMAL;
        }
    }

    /**
     * {@inheritDoc} Overrides the playerPop method from {@link PlayerBubbleModel} to handle the player popping the bubble.
     *
     * <p>Called when the player pops the bubble, and is used to change the state of the bubble to the correct pop state based on the previous state before the bubble was popped.
     * <p>In this case when and empty bubble is popped, it will just simply pop using the {@link #pop()} method.
     *
     * @param playerModel the player model
     * @param EnemyBubblePopCounter the number of enemy bubbles popped
     * @param chainReactionManager the chain explosion manager
     */
    @Override
    public void playerPop(PlayerModel playerModel, int EnemyBubblePopCounter, ChainReactionManager chainReactionManager) {
       pop();
    }
}
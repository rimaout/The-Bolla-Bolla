package com.rima.model.itemesAndRewards;

import static com.rima.model.utilz.Constants.Items.*;
import static com.rima.model.utilz.Constants.Items.BubbleRewardType.*;

/**
 * Represents a bubble reward item in the game.
 *
 * <p>The `BubbleRewardModel` class extends the {@link ItemModel} class and represents a specific type of item
 * that provides rewards to the player.
 */
public class BubbleRewardModel extends ItemModel {

    private final BubbleRewardType bubbleRewardType;

    /**
     * Constructs a new BubbleRewardModel with the specified position and reward type.
     *
     * @param x the x-coordinate of the reward
     * @param y the y-coordinate of the reward
     * @param bubbleRewardType the type of bubble reward
     */
    public BubbleRewardModel(int x, int y, BubbleRewardType bubbleRewardType) {
        super(x, y, ItemType.BUBBLE_REWARD);
        this.bubbleRewardType = bubbleRewardType;
    }

    /**
     * Adds points to the player's score based on the type of bubble reward.
     */
    @Override
    public void addPoints() {
        RewardPointsManagerModel.getInstance().addSmallPoints(GetPoints(bubbleRewardType));
    }

    /**
     * {@inheritDoc}
     *
     * <p>Note: Bubble rewards do not apply effects to the player.
     */
    @Override
    public void applyEffect() {
        // Bubble Rewards do not apply effects to the player
    }

    /**
     * Returns the type of bubble reward.
     *
     * @return the type of bubble reward
     */
    public BubbleRewardType getBubbleRewardType() {
        return bubbleRewardType;
    }
}
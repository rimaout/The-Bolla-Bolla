package model.itemesAndRewards;

import static model.utilz.Constants.Items.*;
import static model.utilz.Constants.Items.BubbleRewardType.*;

public class BubbleRewardModel extends ItemModel {

    private final BubbleRewardType bubbleRewardType;

    public BubbleRewardModel(int x, int y, BubbleRewardType bubbleRewardType) {
        super(x, y, ItemType.BUBBLE_REWARD);
        this.bubbleRewardType = bubbleRewardType;
    }

    @Override
    public void addPoints() {
        RewardPointsManagerModel.getInstance().addSmallPoints(GetPoints(bubbleRewardType));
    }

    @Override
    public void applyEffect() {
        // Bubble Rewards do not apply effects to the player
    }

    public BubbleRewardType getBubbleRewardType() {
        return bubbleRewardType;
    }
}
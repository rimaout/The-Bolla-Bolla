package view.itemsAndRewards;

import model.itemesAndRewards.BubbleRewardModel;
import model.itemesAndRewards.ItemModel;
import model.utilz.Constants;
import view.audio.AudioPlayer;

import java.awt.*;

import static model.utilz.Constants.Items.BubbleRewardType.GetRewardImageIndex;
import static model.utilz.Constants.Items.H;
import static model.utilz.Constants.Items.W;

public class BubbleRewardView extends ItemView{

    public BubbleRewardView(ItemModel itemModel) {
        super(itemModel);
    }

    @Override
    public void draw(Graphics g) {

        BubbleRewardModel model = (BubbleRewardModel) itemModel;

        if (!model.isDeSpawning())
            g.drawImage(itemManagerView.getBubbleRewardImages()[GetRewardImageIndex(model.getBubbleRewardType())], (int) model.getHitbox().x, (int) model.getHitbox().y, W, H, null);
        else
            g.drawImage(itemManagerView.getDeSpawnImages()[animationIndex], (int) model.getHitbox().x, (int) model.getHitbox().y, W, H, null);
    }

    @Override
    public void audioEffects() {
        if (itemModel.isCollected() && !soundPlayed) {
            AudioPlayer.getInstance().playSoundEffect(Constants.AudioConstants.REWARD_COLLECTED);
            soundPlayed = true;
        }
    }
}

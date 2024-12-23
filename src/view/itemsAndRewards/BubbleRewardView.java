package view.itemsAndRewards;

import model.itemesAndRewards.ItemModel;
import model.itemesAndRewards.BubbleRewardModel;
import view.audio.AudioPlayer;
import view.utilz.Constants.AudioConstants;

import java.awt.*;

import static view.utilz.Constants.Items.GetRewardImageIndex;
import static model.utilz.Constants.Items.H;
import static model.utilz.Constants.Items.W;

/**
 * The BubbleRewardView class represents the view for the {@link BubbleRewardModel}.
 * It handles drawing the bubble reward item and playing audio effects when the item is collected.
 */
public class BubbleRewardView extends ItemView{

    /**
     * Constructs a BubbleRewardView with the specified ItemModel.
     *
     * @param itemModel the model of the bubble reward item
     */
    public BubbleRewardView(ItemModel itemModel) {
        super(itemModel);
    }

    /**
     * Draws the bubble reward item on the screen.
     * If the item is de-spawning, it draws the de-spawning animation.
     *
     * @param g the Graphics object to draw with
     */
    @Override
    public void draw(Graphics g) {

        BubbleRewardModel model = (BubbleRewardModel) itemModel;

        if (!model.isDeSpawning())
            g.drawImage(itemManagerView.getBubbleRewardImages()[GetRewardImageIndex(model.getBubbleRewardType())], (int) model.getHitbox().x, (int) model.getHitbox().y, W, H, null);
        else
            g.drawImage(itemManagerView.getDeSpawnImages()[animationIndex], (int) model.getHitbox().x, (int) model.getHitbox().y, W, H, null);
    }

    /**
     * Plays the audio effects for the bubble reward item when it is collected.
     * Ensures the sound is played only once.
     */
    @Override
    public void audioEffects() {
        if (itemModel.isCollected() && !soundPlayed) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.REWARD_COLLECTED);
            soundPlayed = true;
        }
    }
}

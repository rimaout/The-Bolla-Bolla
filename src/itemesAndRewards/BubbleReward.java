package itemesAndRewards;

import java.awt.*;

import model.entities.PlayerModel;
import model.audio.AudioPlayer;
import model.utilz.Constants.AudioConstants;

import static model.utilz.Constants.Items.*;
import static model.utilz.Constants.Items.BubbleRewardType.*;

public class BubbleReward extends Item{

    private final BubbleRewardType type;

    public BubbleReward(int x, int y, BubbleRewardType type) {
        super(x, y);
        this.type = type;
    }

    @Override
    public void draw(Graphics g) {

        if (!deSpawning)
            g.drawImage(itemManager.getBubbleRewardImages()[GetRewardImageIndex(type)], x, y, W, H, null);
        else
            g.drawImage(itemManager.getDeSpawnImages()[animationIndex], x, y, W, H, null);
    }

    @Override
    public void audioEffects() {
        if (playSound) {
            AudioPlayer.getInstance().playSoundEffect(AudioConstants.REWARD_COLLECTED);
            playSound = false;
        }
    }

    @Override
    public void addPoints(PlayerModel playerModel) {
        RewardPointsManager.getInstance(playerModel).addSmallPoints(GetPoints(type));
    }

    @Override
    public void applyEffect(PlayerModel playerModel) {
        // Bubble Rewards do not apply effects to the player
    }

}

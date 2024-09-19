package itemesAndRewards;

import audio.AudioPlayer;
import entities.Player;
import utilz.Constants.AudioConstants;

import java.awt.*;

import static utilz.Constants.Items.*;
import static utilz.Constants.Items.BubbleRewardType.*;

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
    public void addPoints(Player player) {
        RewardPointsManager.getInstance(player).addSmallPoints(GetPoints(type));
    }

    @Override
    public void applyEffect(Player player) {
        // Bubble Rewards do not apply effects to the player
    }

}

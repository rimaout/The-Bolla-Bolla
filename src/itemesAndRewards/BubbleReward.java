package itemesAndRewards;

import audio.AudioPlayer;
import entities.Player;
import utilz.Constants.AudioConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.Items.*;
import static utilz.Constants.Items.BubbleRewardType.*;

public class BubbleReward extends Item{
    BubbleRewardType type;
    BufferedImage[] itemImages;
    BufferedImage[] deSpawnImages;

    public BubbleReward(int x, int y, BubbleRewardType type) {
        super(x, y);
        this.type = type;

        itemImages = ItemManager.getInstance().getBubbleRewardImages(); //TODO: use directly from ItemManager LIGHT
        deSpawnImages = ItemManager.getInstance().getDeSpawnImages();
    }

    @Override
    public void draw(Graphics g) {

        if (!deSpawning)
            g.drawImage(itemImages[GetRewardImageIndex(type)], x, y, W, H, null);
        else
            g.drawImage(deSpawnImages[animationIndex], x, y, W, H, null);
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

package itemes;

import utilz.LoadSave;
import static utilz.Constants.Items.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ItemManager {
    private static ItemManager instance;

    private ArrayList<BubbleReward> bubbleRewards;

    // Sprites
    private BufferedImage[] deSpawnImages;
    private BufferedImage[] bubbleRewardImages;

    private ItemManager() {
        bubbleRewards = new ArrayList<>();
        loadSprites();
    }

    public static ItemManager getInstance() {
        if (instance == null)
            instance = new ItemManager();
        return instance;
    }

    private void loadSprites() {
        BufferedImage bubbleRewardsSprite = LoadSave.GetSprite(LoadSave.ITEM_BUBBLE_REWARD_SPRITE);
        bubbleRewardImages = new BufferedImage[7];

        for (int i = 0; i < 7; i++)
            bubbleRewardImages[i] = bubbleRewardsSprite.getSubimage(i * ITEM_W, 0, ITEM_W, ITEM_H);

        BufferedImage deSpawnSprite = LoadSave.GetSprite(LoadSave.ITEM_DESPAWN_SPRITE);
        deSpawnImages = new BufferedImage[4];

        for (int i = 0; i < 4; i++)
            deSpawnImages[i] = deSpawnSprite.getSubimage(i * ITEM_W, 0, ITEM_W, ITEM_H);
    }

    public BufferedImage[] getBubbleRewardImages() {
        return bubbleRewardImages;
    }

    public BufferedImage[] getDeSpawnImages() {
        return deSpawnImages;
    }
}

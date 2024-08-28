package itemes;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.Items.BubbleRewardType;
import static utilz.Constants.Items.BubbleRewardType.*;

public class BubbleReward extends Item{
    BubbleRewardType type;
    BufferedImage[] itemImages;
    BufferedImage[] deSpawnImages;

    public BubbleReward(int x, int y, BubbleRewardType type) {
        super(x, y);
        this.type = type;
        itemImages = ItemManager.getInstance().getBubbleRewardImages();
        deSpawnImages = ItemManager.getInstance().getDeSpawnImages();
    }

    @Override
    void draw(Graphics g) {

        if (deSpawning)
            g.drawImage(itemImages[animationIndex], x, y, null);
        else
            g.drawImage(itemImages[GetItemImageIndex(type)], x, y, null);
    }

}

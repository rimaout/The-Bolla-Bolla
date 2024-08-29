package itemes;

import entities.Player;

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

        itemImages = ItemManager.getInstance().getBubbleRewardImages();
        deSpawnImages = ItemManager.getInstance().getDeSpawnImages();
    }

    @Override
    void draw(Graphics g) {

        if (!deSpawning)
            g.drawImage(itemImages[GetItemImageIndex(type)], x, y, ITEM_W, ITEM_H, null);
        else
            g.drawImage(deSpawnImages[animationIndex], x, y, ITEM_W, ITEM_H, null);
    }

    @Override
    public void addPoints(Player player) {
        player.addPoints(getPoints(type));
        System.out.println("Points: " + player.getPoints());
        //TODO: make points and lives manager to handle points and draw them
    }

    @Override
    void applyEffect(Player player) {
        // Bubble Rewards do not apply effects to the player
    }

}

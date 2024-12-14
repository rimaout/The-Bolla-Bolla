package view.itemsAndRewards;

import view.utilz.LoadSave;
import model.itemesAndRewards.ItemManagerModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static model.utilz.Constants.Items.*;

public class ItemManagerView {
    private static ItemManagerView instance;
    private final ItemManagerModel itemManagerModel = ItemManagerModel.getInstance();

    private final ArrayList<ItemView> itemsViews = new ArrayList<>();

    // Sprites
    private BufferedImage[] deSpawnImages;
    private BufferedImage[] bubbleRewardImages;
    private BufferedImage[] powerUpImages;

    private ItemManagerView(){
        loadSprites();
    }

    public static ItemManagerView getInstance(){
        if (instance == null)
            instance = new ItemManagerView();
        return instance;
    }

    public void update() {

    }

    public void draw(Graphics g) {
        syncItemsViewsWithModel();

        // Draw Reward Bubble Items
        for (ItemView i : itemsViews) {
            if (i.isActive())
                i.draw(g);

            i.audioEffects();
        }
    }

    private void syncItemsViewsWithModel(){
        for (var i : itemManagerModel.getItemsModels())
            if (itemsViews.stream().noneMatch(iv -> iv.getItemModel().equals(i)))
                switch (i.getItemType()) {
                    case BUBBLE_REWARD -> itemsViews.add(new BubbleRewardView(i));
                    case POWER_UP -> itemsViews.add(new PowerUpView(i));
                }
    }

    private void loadSprites() {
        // Load bubble reward sprites
        BufferedImage bubbleRewardsSprite = LoadSave.GetSprite(LoadSave.ITEM_BUBBLE_REWARD_SPRITE);
        bubbleRewardImages = new BufferedImage[8];
        for (int i = 0; i < bubbleRewardImages.length; i++)
            bubbleRewardImages[i] = bubbleRewardsSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);

        // Load power-up sprites
        BufferedImage powerUpSprite = LoadSave.GetSprite(LoadSave.ITEM_POWER_UP_SPRITE);
        powerUpImages = new BufferedImage[10];
        for (int i = 0; i < powerUpImages.length; i++)
            powerUpImages[i] = powerUpSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);

        // Load de-spawn sprites
        BufferedImage deSpawnSprite = LoadSave.GetSprite(LoadSave.ITEM_DESPAWN_SPRITE);
        deSpawnImages = new BufferedImage[2];
        for (int i = 0; i < deSpawnImages.length; i++)
            deSpawnImages[i] = deSpawnSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);
    }

    public BufferedImage[] getBubbleRewardImages() {
        return bubbleRewardImages;
    }

    public BufferedImage[] getPowerUpImages() {
        return powerUpImages;
    }

    public BufferedImage[] getDeSpawnImages() {
        return deSpawnImages;
    }

    public void newLevelReset() {
        itemsViews.clear();
    }

    public void newPlayReset() {
        newLevelReset();
    }
}
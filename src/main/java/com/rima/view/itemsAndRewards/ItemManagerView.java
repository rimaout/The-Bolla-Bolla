package com.rima.view.itemsAndRewards;

import com.rima.view.utilz.Load;
import com.rima.model.itemesAndRewards.ItemManagerModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.rima.model.utilz.Constants.Items.DEFAULT_H;
import static com.rima.model.utilz.Constants.Items.DEFAULT_W;

/**
 * The ItemManagerView class manages the rendering of item views in the game.
 * It handles loading the sprites for the items, synchronizing the view with the model, and drawing the items on the screen.
 */
public class ItemManagerView {
    private static ItemManagerView instance;
    private final ItemManagerModel itemManagerModel = ItemManagerModel.getInstance();

    private final ArrayList<ItemView> itemsViews = new ArrayList<>();

    // Sprites
    private BufferedImage[] deSpawnImages;
    private BufferedImage[] bubbleRewardImages;
    private BufferedImage[] powerUpImages;

    /**
     * Private constructor for singleton design patter.
     * Loads the item sprites.
     */
    private ItemManagerView(){
        loadSprites();
    }

    /**
     * Returns the singleton instance of ItemManagerView, creating it if necessary.
     *
     * @return the singleton instance of ItemManagerView
     */
    public static ItemManagerView getInstance(){
        if (instance == null)
            instance = new ItemManagerView();
        return instance;
    }

    /**
     * Updates the state of the item views.
     */
    public void update() {
        // empty method
    }

    /**
     * Draws all active item views on the screen.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        syncItemsViewsWithModel();

        // Draw Reward Bubble Items
        for (ItemView i : itemsViews) {
            if (i.isActive())
                i.draw(g);

            i.audioEffects();
        }
    }

    /**
     * Synchronizes the item views with the model.
     * Adds new ItemView instances for any ItemModel instances that do not already have a corresponding view.
     */
    private void syncItemsViewsWithModel(){
        for (var i : itemManagerModel.getItemsModels())
            if (itemsViews.stream().noneMatch(iv -> iv.getItemModel().equals(i)))
                switch (i.getItemType()) {
                    case BUBBLE_REWARD -> itemsViews.add(new BubbleRewardView(i));
                    case POWER_UP -> itemsViews.add(new PowerUpView(i));
                }
    }

    /**
     * Loads the sprites for bubble rewards, power-ups, and de-spawning items from the sprite sheets.
     */
    private void loadSprites() {
        // Load bubble reward sprites
        BufferedImage bubbleRewardsSprite = Load.GetSprite(Load.ITEM_BUBBLE_REWARD_SPRITE);
        bubbleRewardImages = new BufferedImage[8];
        for (int i = 0; i < bubbleRewardImages.length; i++)
            bubbleRewardImages[i] = bubbleRewardsSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);

        // Load power-up sprites
        BufferedImage powerUpSprite = Load.GetSprite(Load.ITEM_POWER_UP_SPRITE);
        powerUpImages = new BufferedImage[10];
        for (int i = 0; i < powerUpImages.length; i++)
            powerUpImages[i] = powerUpSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);

        // Load de-spawn sprites
        BufferedImage deSpawnSprite = Load.GetSprite(Load.ITEM_DESPAWN_SPRITE);
        deSpawnImages = new BufferedImage[2];
        for (int i = 0; i < deSpawnImages.length; i++)
            deSpawnImages[i] = deSpawnSprite.getSubimage(i * DEFAULT_W, 0, DEFAULT_W, DEFAULT_H);
    }

    /**
     * Resets the item views for a new level.
     */
    public void newLevelReset() {
        itemsViews.clear();
    }

    /**
     * Resets the item views for a new play session.
     */
    public void newPlayReset() {
        newLevelReset();
    }

    // ------------------- Getters Methods -------------------

    /**
     * Returns the sprite images for bubble rewards.
     *
     * @return an array of BufferedImage objects representing bubble reward images
     */
    public BufferedImage[] getBubbleRewardImages() {
        return bubbleRewardImages;
    }

    /**
     * Returns the sprite images for power-ups.
     *
     * @return an array of BufferedImage objects representing power-up images
     */
    public BufferedImage[] getPowerUpImages() {
        return powerUpImages;
    }

    /**
     * Returns the sprite images for de-spawning items.
     *
     * @return an array of BufferedImage objects representing de-spawn images
     */
    public BufferedImage[] getDeSpawnImages() {
        return deSpawnImages;
    }
}
package com.rima.view.bubbles.specialBubbles;

import com.rima.model.bubbles.BubbleModel;
import com.rima.model.bubbles.specialBubbles.WaterFlowModel;
import com.rima.model.bubbles.specialBubbles.SpecialBubbleManagerModel;
import com.rima.view.utilz.Load;
import com.rima.view.bubbles.BubbleView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import static com.rima.model.utilz.Constants.PlayerConstants.DEFAULT_H;
import static com.rima.model.utilz.Constants.PlayerConstants.DEFAULT_W;

/**
 * The SpecialBubbleManagerView class manages the view for special bubbles in the game.
 * It handles loading bubble sprites, updating and drawing bubble views, and synchronizing bubble views with their models.
 */
public class SpecialBubbleManagerView {
    private static SpecialBubbleManagerView instance;

    private final LinkedList<BubbleView> bubblesViews;
    private final LinkedList<WaterFlowView> waterFlowsViews;
    private final SpecialBubbleManagerModel bubblesManagerModel = SpecialBubbleManagerModel.getInstance();

    private BufferedImage[][] waterBubbleSprites;
    private BufferedImage[][] lightningBubbleSprites;
    private BufferedImage[][] playerSprites;

    /**
     * Constructs a SpecialBubbleManagerView and initializes the bubble views list and loads bubble sprites.
     * <p>Private constructor for singleton design pattern.
     */
    private SpecialBubbleManagerView() {
        bubblesViews = new LinkedList<>();
        waterFlowsViews = new LinkedList<>();

        loadBubbleSprites();
    }

    /**
     * Returns the singleton instance of SpecialBubbleManagerView.
     *
     * @return the singleton instance of SpecialBubbleManagerView
     */
    public static SpecialBubbleManagerView getInstance() {
        if (instance == null) {
            instance = new SpecialBubbleManagerView();
        }
        return instance;
    }

    /**
     * Draws all active bubble views and water flow views on the screen.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        syncBubblesViewsWithModel();
        syncWaterFlowsViewsWithModel();

        for (BubbleView b : bubblesViews) {
            if (b.isActive())
                b.draw(g);
        }

        for (WaterFlowView w : waterFlowsViews) {
            if (w.isActive())
                w.draw(g);
        }
    }


    /**
     * Updates the state of all active bubble views.
     */
    public void update() {
        for (BubbleView b : bubblesViews) {
            if (b.isActive())
                b.update();
        }
    }

    /**
     * Synchronizes the bubble views with their corresponding models.
     * Adds new bubble views if they are not already present in the view.
     */
    private void syncBubblesViewsWithModel() {
        for (BubbleModel bm : bubblesManagerModel.getBubblesModels()) {
            // if a projectile is not in the view, add it
            if (bubblesViews.stream().noneMatch(bv -> bv.getBubbleModel().equals(bm))) { //todo: use hashmap instead of list to increase performance

                switch (bm.getBubbleType()) {
                    case WATER_BUBBLE -> bubblesViews.add(new WaterBubbleView(bm));
                    case LIGHTNING_BUBBLE -> bubblesViews.add(new LightningBubbleView(bm));
                }
            }
        }
    }

    /**
     * Synchronizes the water flow views with their corresponding models.
     * Adds new water flow views if they are not already present in the view.
     */
    private void syncWaterFlowsViewsWithModel() {
        for (WaterFlowModel wm : bubblesManagerModel.getWaterFlowsModels()) {
            // if a projectile is not in the view, add it
            if (waterFlowsViews.stream().noneMatch(wv -> wv.getWaterFlowModel().equals(wm))) //todo: use hashmap instead of list to increase performance
                waterFlowsViews.add(new WaterFlowView(wm));
        }
    }

    /**
     * Loads the bubble sprites from the sprite sheet.
     */
    private void loadBubbleSprites() {
        waterBubbleSprites = new BufferedImage[2][1];
        BufferedImage temp = Load.GetSprite(Load.WATER_BUBBLE_SPRITE);
        waterBubbleSprites[0][0] = temp.getSubimage(0 , 0, DEFAULT_W, DEFAULT_H);
        waterBubbleSprites[1][0] = temp.getSubimage(DEFAULT_W , 0, DEFAULT_W, DEFAULT_H);

        lightningBubbleSprites = new BufferedImage[2][1];
        temp = Load.GetSprite(Load.LIGHTNING_BUBBLE_SPRITE);
        lightningBubbleSprites[0][0] = temp.getSubimage(0 , 0, DEFAULT_W, DEFAULT_H);
        lightningBubbleSprites[1][0] = temp.getSubimage(DEFAULT_W , 0, DEFAULT_W, DEFAULT_H);

        temp = Load.GetSprite(Load.PLAYER_SPRITE);
        playerSprites = new BufferedImage[6][7];
        for (int j = 0; j < playerSprites.length; j++)
            for (int i = 0; i < playerSprites[j].length; i++)
                playerSprites[j][i] = temp.getSubimage(i * DEFAULT_W, j* DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    /**
     * Resets the bubble views and water flow views for a new level.
     */
    public void newLevelReset() {
        bubblesViews.clear();
        waterFlowsViews.clear();
    }

    /**
     * Resets the bubble views and water flow views for a new play session.
     */
    public void newPlayReset() {
        newLevelReset();
    }

    /**
     * Returns the loaded water bubble sprites.
     *
     * @return the loaded water bubble sprites
     */
    public BufferedImage[][] getWaterBubbleSprites() {
        return waterBubbleSprites;
    }

    /**
     * Returns the loaded lightning bubble sprites.
     *
     * @return the loaded lightning bubble sprites
     */
    public BufferedImage[][] getLightningBubbleSprites() {
        return lightningBubbleSprites;
    }

    /**
     * Returns the loaded player sprites.
     *
     * @return the loaded player sprites
     */
    public BufferedImage[][] getPlayerSprites() {
        return playerSprites;
    }
}
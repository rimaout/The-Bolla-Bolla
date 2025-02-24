package com.rima.view.bubbles.playerBubbles;

import com.rima.view.utilz.Load;
import com.rima.model.bubbles.BubbleModel;
import com.rima.model.bubbles.playerBubbles.PlayerBubblesManagerModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import static com.rima.model.utilz.Constants.Bubble.DEFAULT_H;
import static com.rima.model.utilz.Constants.Bubble.DEFAULT_W;


/**
 * The PlayerBubblesManagerView class manages the view for player bubbles in the game.
 * It handles loading bubble sprites, updating and drawing bubble views, and synchronizing bubble views with their models.
 */
public class PlayerBubblesManagerView {
    private static PlayerBubblesManagerView instance;
    private final PlayerBubblesManagerModel playerBubblesManagerModel = PlayerBubblesManagerModel.getInstance();
    private BufferedImage[][] playerBubbleSprites;
    private final LinkedList<PlayerBubbleView> bubblesViews;

    /**
     * Constructs a PlayerBubblesManagerView, initializes the bubble views list and loads bubble sprites.
     * <p>Private constructor for singleton design pattern.
     */
    private PlayerBubblesManagerView() {
        bubblesViews = new LinkedList<>();
        loadBubbleSprites();
    }

    /**
     * Returns the singleton instance of PlayerBubblesManagerView.
     *
     * @return the singleton instance of PlayerBubblesManagerView
     */
    public static PlayerBubblesManagerView getInstance() {
        if (instance == null) {
            instance = new PlayerBubblesManagerView();
        }
        return instance;
    }

    /**
     * Updates the state of all active bubble views.
     */
    public void update() {
        for (PlayerBubbleView b : bubblesViews) {
            if (b.isActive())
                b.update();
        }
    }

    /**
     * Draws all active bubble views on the screen.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        syncBubblesViewsWithModel();

        for (PlayerBubbleView b : bubblesViews) {
            if (b.isActive())
                b.draw(g);
        }
    }

    /**
     * Synchronizes the bubble views with their corresponding models.
     * Adds new bubble views if they are not already present in the view.
     */
    private void syncBubblesViewsWithModel() {
        for (BubbleModel bm : playerBubblesManagerModel.getBubblesModels()) {
            // if a projectile is not in the view, add it
            if (bubblesViews.stream().noneMatch(bv -> bv.getBubbleModel().equals(bm))) { //todo: use hashmap instead of list to increase performance

                switch (bm.getBubbleType()) {
                    case EMPTY_BUBBLE -> bubblesViews.add(new EmptyBubbleView(bm));
                    case ENEMY_BUBBLE -> bubblesViews.add(new EnemyBubbleView(bm));
                }
            }
        }
    }

    /**
     * Loads the bubble sprites from the sprite sheet.
     */
    private void loadBubbleSprites() {
        BufferedImage img = Load.GetSprite(Load.BUBBLE_BUD_SPRITE);

        playerBubbleSprites = new BufferedImage[6][4];
        for (int j = 0; j < playerBubbleSprites.length; j++)
            for (int i = 0; i < playerBubbleSprites[j].length; i++)
                playerBubbleSprites[j][i] = img.getSubimage(i * DEFAULT_W, j * DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    /**
     * Resets the bubble views for a new level.
     */
    public void newLevelReset() {
        bubblesViews.clear();
    }

    /**
     * Resets the bubble views for a new play session.
     */
    public void newPlayReset() {
        newLevelReset();
    }

    /**
     * Returns the loaded player bubble sprites.
     *
     * @return the loaded player bubble sprites
     */
    public BufferedImage[][] getPlayerBubbleSprites() {
        return playerBubbleSprites;
    }
}
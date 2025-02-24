package com.rima.view.entities;

import com.rima.model.entities.EnemyManagerModel;
import com.rima.model.utilz.Constants.EnemyConstants.EnemyType;
import com.rima.view.utilz.Load;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.rima.model.utilz.Constants.EnemyConstants.*;

/**
 * The EnemyManagerView class manages the view for all enemy entities in the game.
 * It handles loading enemy sprites, updating enemy views, and drawing enemies on the screen.
 */
public class EnemyManagerView {
    private static EnemyManagerView instance;
    private static EnemyManagerModel enemyManagerModel = EnemyManagerModel.getInstance();

    private BufferedImage[][] zenChanSprites;
    private BufferedImage[][] maitaSprites;
    private BufferedImage[][] skelMonstaSprites;

    private ArrayList<EnemyView> enemiesViews = new ArrayList<>();

    /**
     * Private constructor for singleton design pattern.
     * Initializes the enemy sprites.
     */
    private EnemyManagerView() {
        loadSprites();
    }

    /**
     * Returns the singleton instance of EnemyManagerView.
     *
     * @return the singleton instance of EnemyManagerView
     */
    public static EnemyManagerView getInstance() {
        if (instance == null) {
            instance = new EnemyManagerView();
        }
        return instance;
    }

    /**
     * Updates all active enemy views.
     */
    public void update() {
        for (EnemyView e : enemiesViews)
            if (e.isActive())
                e.update();
    }

    /**
     * Draws all active enemy views on the screen.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {
        syncEnemyViewsWithModel();

        for (EnemyView e : enemiesViews)
            if(e.isActive())
                g.drawImage(getEnemySprite(e.enemyModel.getEnemyType())[e.enemyModel.getEnemyState()][e.getAnimationIndex()], (int) (e.enemyModel.getHitbox().x - ENEMY_HITBOX_OFFSET_X) + e.enemyModel.flipX(), (int) (e.enemyModel.getHitbox().y - ENEMY_HITBOX_OFFSET_Y), ENEMY_W * e.enemyModel.flipW(), ENEMY_H, null);
    }

    /**
     * Synchronizes the enemy views with the enemy models.
     * Adds new enemy views if they are not already in the view.
     */
    private void syncEnemyViewsWithModel() {
        for (var em : enemyManagerModel.getEnemiesModels()) {

            // if a projectile is not in the view, add it
            if (enemiesViews.stream().noneMatch(ev -> ev.enemyModel.equals(em)))
                switch (em.getEnemyType()) {
                    case MAITA -> enemiesViews.add(new EnemyView(em));
                    case ZEN_CHAN -> enemiesViews.add(new EnemyView(em));
                }
        }
    }

    /**
     * Loads the enemy sprites from the sprite sheets.
     */
    private void loadSprites() {
        zenChanSprites = new BufferedImage[9][4];
        BufferedImage temp = Load.GetSprite(Load.ZEN_CHAN_ENEMY_SPRITE);

        for (int j = 0; j < zenChanSprites.length; j++)
            for (int i = 0; i < zenChanSprites[j].length; i++)
                zenChanSprites[j][i] = temp.getSubimage(i * ENEMY_DEFAULT_W, j * ENEMY_DEFAULT_H, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);

        maitaSprites = new BufferedImage[9][4];
        temp = Load.GetSprite(Load.MAITA_ENEMY_SPRITE);
        for (int j = 0; j < maitaSprites.length; j++)
            for (int i = 0; i < maitaSprites[j].length; i++)
                maitaSprites[j][i] = temp.getSubimage(i * ENEMY_DEFAULT_W, j * ENEMY_DEFAULT_H, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);

        skelMonstaSprites = new BufferedImage[3][2];
        temp = Load.GetSprite(Load.SKEL_MONSTA_ENEMY_SPRITE);
        for (int j = 0; j < skelMonstaSprites.length; j++)
            for (int i = 0; i < skelMonstaSprites[j].length; i++)
                skelMonstaSprites[j][i] = temp.getSubimage(i * ENEMY_DEFAULT_W, j * ENEMY_DEFAULT_H, ENEMY_DEFAULT_W, ENEMY_DEFAULT_H);
    }

    /**
     * Resets the enemy views for a new level.
     */
    public void newLevelReset() {
        enemiesViews.clear();
    }

    /**
     * Resets the enemy views for a new play session.
     */
    public void newPlayReset() {
        newLevelReset();
    }

    // ------------------- Getters -------------------

    /**
     * Returns the sprite array for the specified enemy type.
     *
     * @param enemyType the type of the enemy
     * @return the sprite array for the specified enemy type
     */
    public BufferedImage[][] getEnemySprite(EnemyType enemyType) {
        return switch (enemyType) {
            case ZEN_CHAN -> zenChanSprites;
            case MAITA -> maitaSprites;
            case SKEL_MONSTA -> skelMonstaSprites;
        };
    }
}
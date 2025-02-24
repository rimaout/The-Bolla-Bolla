package com.rima.view.levels;

import com.rima.view.utilz.Load;
import com.rima.model.utilz.Constants;
import com.rima.model.levels.LevelManagerModel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The LevelManagerView class is responsible for the {@link LevelManagerModel} class.
 * It loads the necessary sprites and draws the tiles based on the current level data.
 */
public class LevelManagerView {
    private static LevelManagerView instance;
    private final LevelManagerModel levelManagerModel;

    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;

    /**
     * Private constructor to implement singleton design pattern.
     * Initializes the LevelManagerModel and loads the sprites.
     */
    private LevelManagerView(){
        this.levelManagerModel = LevelManagerModel.getInstance();
        loadSprites();
    }

    /**
     * Returns the singleton instance of LevelManagerView, creating it if necessary.
     *
     * @return the singleton instance of LevelManagerView
     */
    public static LevelManagerView getInstance() {
        if (instance == null) {
            instance = new LevelManagerView();
        }
        return instance;
    }

    /**
     * Draws the level tiles and number tiles on the screen.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(Graphics g) {

        int index;
        BufferedImage tile;

        for (int y = 0; y < Constants.TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < Constants.TILES_IN_WIDTH; x++) {
                index = levelManagerModel.getLevels().get(levelManagerModel.getLevelIndex()).getTileSpriteIndex(x, y);

                if (index >= 120)
                    tile = numbersTiles[index - 120];
                else
                    tile = levelTiles[index];

                g.drawImage(tile, x * Constants.TILES_SIZE, y * Constants.TILES_SIZE, Constants.TILES_SIZE, Constants.TILES_SIZE, null);
            }
        }
    }

    /**
     * Loads the sprites for the level tiles and number tiles.
     */
    private void loadSprites() {

        // load numbers
        numbersTiles = new BufferedImage[10];
        BufferedImage numbersSprite = Load.GetSprite(Load.NUMBERS_TILES_SPRITE);
        for (int i = 0; i < numbersTiles.length; i++) {
            numbersTiles[i] = numbersSprite.getSubimage(i * 8, 0, 8, 8);
        }

        // load level tiles
        int x, y;
        levelTiles = new BufferedImage[116];
        BufferedImage levelSprite = Load.GetSprite(Load.LEVEL_TILES_SPRITE);

        levelTiles[0] = levelSprite.getSubimage(0, 0, 8, 8); // background tile
        for (int i = 0; i < levelTiles.length-1; i++) {
            x = 12 + (i / 25) * 48;
            y = 26 + (i % 25) * 19;
            levelTiles[i+1] = levelSprite.getSubimage(x, y, 8, 8);
        }
    }

    /**
     * Returns the array of number tiles.
     *
     * @return the array of number tiles
     */
    public BufferedImage[] getNumbersTiles() {
        return numbersTiles;
    }

    /**
     * Returns the array of level tiles.
     *
     * @return the array of level tiles
     */
    public BufferedImage[] getLevelTiles() {
        return levelTiles;
    }
}
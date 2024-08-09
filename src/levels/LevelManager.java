package levels;

import Utillz.LoadSave;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;
    private Level levelOne;

    public LevelManager(Game game) {
        this.game = game;
        loadSprites();
        levelOne = new Level(LoadSave.GetLevelData());
    }

    public void draw(Graphics g) {
        int i = 0;
        int index;
        BufferedImage tile;

        for (int y = 0; y < Game.TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < Game.TILES_IN_WIDTH; x++) {
                index = levelOne.getSpriteIndex(x, y);

                if (index >= 120)
                    tile = numbersTiles[index - 120];
                else
                    tile = levelTiles[index];

                g.drawImage(tile, x * Game.TILES_SIZE, y * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levelOne;
    }

    private void loadSprites() {

        // load numbers
        numbersTiles = new BufferedImage[10];
        BufferedImage numbersSprite = LoadSave.GetSprite(LoadSave.NUMBERS_TILES_SPRITE);
        for (int i = 0; i < numbersTiles.length; i++) {
            numbersTiles[i] = numbersSprite.getSubimage(i * 8, 0, 8, 8);
        }

        // load level tiles
        int x, y;
        levelTiles = new BufferedImage[116];
        BufferedImage levelSprite = LoadSave.GetSprite(LoadSave.LEVEL_TILES_SPRITE);

        levelTiles[0] = levelSprite.getSubimage(0, 0, 8, 8); // background tile
        for (int i = 0; i < levelTiles.length-1; i++) {
            x = 12 + (i / 25) * 48;
            y = 26 + (i % 25) * 19;
            levelTiles[i+1] = levelSprite.getSubimage(x, y, 8, 8);
        }
    }
}


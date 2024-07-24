package levels;

import Utillz.LoadSave;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelTiles;
    private Level levelOne;

    public LevelManager(Game game) {
        this.game = game;
        loadLevelSprites();
        levelOne = new Level(LoadSave.getLevelData());
    }

    public void draw(Graphics g) {
        int i = 0;

        for (int y = 0; y < Game.TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < Game.TILES_IN_WIDTH; x++) {
                int index = levelOne.getSpriteIndex(x, y);
                g.drawImage(levelTiles[index], x * Game.TILES_SIZE, y * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levelOne;
    }

    private void loadLevelSprites() {
        BufferedImage levelSprite = LoadSave.getSprite(LoadSave.LEVEL_SPRITE);

        // LOAD SMALL LEVEL TILES
        for (int i=0; i<levelTiles.length; i++) {
            levelTiles[i] = levelSprite.getSubimage(i*16, 0, 16, 16);
        }
    }
}


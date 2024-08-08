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
        levelOne = new Level(LoadSave.GetLevelData());
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
        int xSmall, ySmall;
        levelTiles = new BufferedImage[116];

        BufferedImage levelSprite = LoadSave.GetSprite(LoadSave.LEVEL_SPRITE);

        levelTiles[0] = levelSprite.getSubimage(0, 0, 8, 8); // background tile
        for (int i = 0; i < levelTiles.length-1; i++) {
            xSmall = 12 + (i / 25) * 48;
            ySmall = 26 + (i % 25) * 19;
            levelTiles[i+1] = levelSprite.getSubimage(xSmall, ySmall, 8, 8);
        }
    }
}


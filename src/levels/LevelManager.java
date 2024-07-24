package levels;

import Utillz.LoadSave;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {

    private Game game;
    private BufferedImage[] smallLevelTiles;
    private BufferedImage[] bigLevelTiles;
    private Level levelOne;

    public LevelManager(Game game) {
        this.game = game;
        loadLevelSprites();
        levelOne = new Level(LoadSave.getLevelData());
    }

    public void draw(Graphics g) {
        int i = 0;

        for( int y = 0; y < Game.TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < Game.TILES_IN_WIDTH; x++) {
                int index = levelOne.getSpriteIndex(x, y);
                g.drawImage(smallLevelTiles[index], x * Game.TILES_SIZE, y * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levelOne;
    }

    private void loadLevelSprites() {

        int xSmall, ySmall, xBig, yBig;
        smallLevelTiles = new BufferedImage[116];
        bigLevelTiles = new BufferedImage[116];

        BufferedImage levelSprite = LoadSave.getSprite(LoadSave.LEVEL_SPRITE);

        // LOAD SMALL LEVEL TILES
        smallLevelTiles[0] = levelSprite.getSubimage(0, 0, 8, 8); // background tile
        for (int i = 0; i < smallLevelTiles.length-1; i++) {
            xSmall = 12 + (i / 25) * 48;
            ySmall = 26 + (i % 25) * 19;
            smallLevelTiles[i+1] = levelSprite.getSubimage(xSmall, ySmall, 8, 8);
        }

        // LOAD BIG LEVEL TILES
        bigLevelTiles[0] = levelSprite.getSubimage(0, 0, 16, 16); // background tile
        for (int i = 0; i < bigLevelTiles.length-1; i++) {
            xBig = 39 + (i / 25) * 48;
            yBig = 26 + (i % 25) * 19;
            bigLevelTiles[i+1] = levelSprite.getSubimage(xBig, yBig, 16, 16);
        }

    }

}


package view.levels;

import model.levels.LevelManagerModel;
import model.Constants;
import view.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManagerView {
    private static LevelManagerView instance;
    private final LevelManagerModel levelManagerModel;

    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;

    private LevelManagerView(){
        this.levelManagerModel = LevelManagerModel.getInstance();
        loadSprites();
    }

    public static LevelManagerView getInstance() {
        if (instance == null) {
            instance = new LevelManagerView();
        }
        return instance;
    }

    public void draw(Graphics g) {

        int index;
        BufferedImage tile;

        for (int y = 0; y < Constants.TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < Constants.TILES_IN_WIDTH; x++) {
                index = levelManagerModel.getLevels().get(levelManagerModel.getLevelIndex()).getSpriteIndex(x, y);

                if (index >= 120)
                    tile = numbersTiles[index - 120];
                else
                    tile = levelTiles[index];

                g.drawImage(tile, x * Constants.TILES_SIZE, y * Constants.TILES_SIZE, Constants.TILES_SIZE, Constants.TILES_SIZE, null);
            }
        }
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

    public BufferedImage[] getNumbersTiles() {
        return numbersTiles;
    }

    public BufferedImage[] getLevelTiles() {
        return levelTiles;
    }
}

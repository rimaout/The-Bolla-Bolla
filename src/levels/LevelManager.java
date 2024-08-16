package levels;

import gameStates.Playing;
import utilz.LoadSave;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {

    private Playing playing;
    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;
    private ArrayList<Level> levels;
    private int levelIndex = 0;

    public LevelManager(Playing playing) {
        this.playing = playing;
        loadSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void loadNextLevel() {
        levelIndex++;
        if (levelIndex >= levels.size()) {
            levelIndex = 0;
            playing.setGameCompleted(true);
        }

        Level newLevel = levels.get(levelIndex);
        playing.getEnemyManager().loadEnemies(newLevel);
        playing.getPlayer().loadLevelData(newLevel.getLevelData());
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();

        for (BufferedImage level : allLevels)
            levels.add(new Level(level));
    }

    public void draw(Graphics g) {
        int index;
        BufferedImage tile;

        for (int y = 0; y < Game.TILES_IN_HEIGHT; y++) {
            for (int x = 0; x < Game.TILES_IN_WIDTH; x++) {
                index = levels.get(levelIndex).getSpriteIndex(x, y);

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
        return levels.get(levelIndex);
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

    public int getLevelsAmount() {
        return levels.size();
    }
}


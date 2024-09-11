package levels;

import gameStates.GameState;
import gameStates.Playing;
import utilz.LoadSave;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
    private static LevelManager instance;

    private Playing playing;

    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;
    private ArrayList<Level> levels;
    private int levelIndex = 0;

    private boolean allLevelsCompleted = false;

    private LevelManager(Playing playing) {
        this.playing = playing;

        loadSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public static LevelManager getInstance(Playing playing) {
        if (instance == null) {
            instance = new LevelManager(playing);
        }
        return instance;
    }

    public static LevelManager getInstance() {
        return instance;
    }

    public void loadNextLevel() {

        if (levelIndex >= levels.size()-1) {
            allLevelsCompleted = true;
            return;
        }

        playing.getGame().getLevelTransition().setOldLevel(getCurrentLevel());
        playing.getGame().getLevelTransition().setNewLevel(getLevelWithIndex(levelIndex+1));
        GameState.state = GameState.LEVEL_TRANSITION;
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

    public void resetAll() {
        // reset all levels (use only when game is restarted)

        levelIndex = 0;
        allLevelsCompleted = false;
    }

    public BufferedImage[] getNumbersTiles() {
        return numbersTiles;
    }

    public BufferedImage[] getLevelTiles() {
        return levelTiles;
    }

    public Level getCurrentLevel() {
        return levels.get(levelIndex);
    }

    public Level getLevelWithIndex(int levelIndex) {
        return levels.get(levelIndex);
    }

    public void increaseLevelIndex() {
        levelIndex++;
    }

    public boolean areAllLevelsCompleted() {
        return allLevelsCompleted;
    }

}


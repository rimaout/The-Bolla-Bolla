package levels;


import main.Game;
import utilz.LoadSave;
import gameStates.PlayingModel;
import gameStates.GameState;
import utilz.Constants.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class LevelManager {
    private static LevelManager instance;

    private final PlayingModel playingModel;

    private final ArrayList<Level> levels;
    private int levelIndex = 0;
    private boolean allLevelsCompleted = false;

    private BufferedImage[] levelTiles;
    private BufferedImage[] numbersTiles;

    private LevelManager(PlayingModel playingModel) {
        this.playingModel = playingModel;


        levels = new ArrayList<>();
        loadSprites();
        buildAllLevels();
    }

    public static LevelManager getInstance(PlayingModel playingModel) {
        if (instance == null) {
            instance = new LevelManager(playingModel);
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

        playingModel.getGame().getLevelTransition().setOldLevel(getCurrentLevel());
        playingModel.getGame().getLevelTransition().setNewLevel(getLevelWithIndex(levelIndex+1));
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

    public void newPlayReset() {
        levelIndex = 0;
        allLevelsCompleted = false;

        // reset all levels
        for (Level level : levels)
            level.newGameReset();
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

    public int[][] getLevelData() {
        return levels.get(levelIndex).getLevelData();
    }

    public Direction[][] getWindDirectionData() {
        return levels.get(levelIndex).getWindDirectionData();
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


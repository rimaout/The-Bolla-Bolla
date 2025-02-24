package com.rima.model.levels;

import com.rima.model.gameStates.GameState;
import com.rima.model.gameStates.LevelTransitionModel;
import com.rima.model.utilz.Constants.Direction;

import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * Manages the {@link Level} in the game.
 *
 * <p>This singleton class is responsible for loading, managing, and transitioning between levels.
 * It keeps track of the current level, handles level transitions, and provides access to level data.
 */
public class LevelManagerModel {
    private static LevelManagerModel instance;

    private final ArrayList<Level> levels;
    private int levelIndex = 0;
    private boolean allLevelsCompleted = false;

    /**
     * Private constructor for the LevelManagerModel.
     *
     * <p>Initializes the levels list and builds all levels by loading them from images.
     */
    private LevelManagerModel() {

        levels = new ArrayList<>();
        buildAllLevels();
    }

    /**
     * Returns the singleton instance of the LevelManagerModel.
     *
     * <p>If the instance is null, it initializes the LevelManagerModel instance.
     *
     * @return the singleton instance of the LevelManagerModel
     */
    public static LevelManagerModel getInstance() {
        if (instance == null)
            instance = new LevelManagerModel();
        return instance;
    }

    /**
     * Loads the next level and sets the game state to level transition.
     *
     * <p>If the current level is the last level, sets allLevelsCompleted to true.
     */
    public void loadNextLevel() {

        if (levelIndex >= levels.size()-1) {
            allLevelsCompleted = true;
            return;
        }

        LevelTransitionModel.getInstance().setOldLevel(getCurrentLevel());
        LevelTransitionModel.getInstance().setNewLevel(getLevelWithIndex(levelIndex+1));
        GameState.state = GameState.LEVEL_TRANSITION;
    }

    /**
     * This method gets all level map images using the LevelImportMethods.GetAllLevels() method,
     * and then creates a new Level object for each image, adding it to the levels list.
     */
    private void buildAllLevels() {
        BufferedImage[] allLevels = LevelImportMethods.GetAllLevels();

        for (BufferedImage level : allLevels)
            levels.add(new Level(level));
    }

    /**
     * Resets the game to the first level and resets all levels.
     */
    public void newPlayReset() {
        levelIndex = 0;
        allLevelsCompleted = false;

        // reset all model.levels
        for (Level level : levels)
            level.newGameReset();
    }

    /**
     * @return the current level
     */
    public Level getCurrentLevel() {
        return levels.get(levelIndex);
    }

    /**
     * Returns the level tiles tpe data of the current level.
     *
     * @return a 2D array representing the tiles in map
     */
    public int[][] getLevelTileData() {
        return levels.get(levelIndex).getLevelTileData();
    }

    /**
     * Returns the wind direction data of the current level.
     *
     * @return a 2D array representing the wind direction data
     */
    public Direction[][] getWindDirectionData() {
        return levels.get(levelIndex).getWindDirectionData();
    }

    /**
     * Returns the level associated to the specified index.
     *
     * @param levelIndex the index of the level to get
     * @return the level associated to the specified index
     */
    public Level getLevelWithIndex(int levelIndex) {
        return levels.get(levelIndex);
    }

    /**
     * Increases the level index by one.
     */
    public void increaseLevelIndex() {
        levelIndex++;
    }


    /**
     * Checks if all levels are completed.
     *
     * @return true if all levels are completed, false otherwise
     */
    public boolean areAllLevelsCompleted() {
        return allLevelsCompleted;
    }

    /**
     * Returns the list of all levels.
     *
     * @return an ArrayList of all levels
     */
    public ArrayList<Level> getLevels() {
        return levels;
    }

    /**
     * Returns the current level index.
     *
     * @return the current level index
     */
    public int getLevelIndex(){
        return levelIndex;
    }
}
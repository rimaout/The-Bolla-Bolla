package model.levels;


import model.gameStates.PlayingModel;
import model.gameStates.GameState;
import model.Constants.Direction;

import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class LevelManagerModel {
    private static LevelManagerModel instance;

    private final PlayingModel playingModel;

    private final ArrayList<Level> levels;
    private int levelIndex = 0;
    private boolean allLevelsCompleted = false;

    private LevelManagerModel(PlayingModel playingModel) {
        this.playingModel = playingModel;


        levels = new ArrayList<>();
        buildAllLevels();
    }

    public static LevelManagerModel getInstance(PlayingModel playingModel) {
        if (instance == null) {
            instance = new LevelManagerModel(playingModel);
        }
        return instance;
    }

    public static LevelManagerModel getInstance() {
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
        BufferedImage[] allLevels = LevelImportMethods.GetAllLevels();

        for (BufferedImage level : allLevels)
            levels.add(new Level(level));
    }

    public void newPlayReset() {
        levelIndex = 0;
        allLevelsCompleted = false;

        // reset all model.levels
        for (Level level : levels)
            level.newGameReset();
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

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public int getLevelIndex(){
        return levelIndex;
    }
}


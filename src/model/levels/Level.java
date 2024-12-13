package model.levels;

import model.entities.EnemyModel;
import model.utilz.Constants.Direction;
import model.bubbles.specialBubbles.BubbleGenerator;

import java.util.ArrayList;
import java.awt.image.BufferedImage;    // used for level map data

public class Level {

    private BufferedImage levelIMap;
    private int[][] levelData;
    private Direction[][] windDirectionData;
    private BubbleGenerator bubbleGenerator;
    private ArrayList<EnemyModel> Enemies;

    public Level(BufferedImage levelIMap) {
        this.levelIMap = levelIMap;
        createLevelData();
        createWindDirectionData();
        createBubbleGenerator();
        createEnemies();
    }

    private void createLevelData() {
        levelData = LevelImportMethods.GetLevelData(levelIMap);
    }

    private void createWindDirectionData() {
        windDirectionData = LevelImportMethods.GetWindsDirectionsData(levelIMap);
    }

    private void createBubbleGenerator() {
        bubbleGenerator = LevelImportMethods.GetBubbleGenerator(levelIMap);
    }

    private void createEnemies() {
        Enemies = LevelImportMethods.GetEnemies(levelIMap);
    }

    public int getSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

    protected int[][] getLevelData() {
        return levelData;
    }

    protected Direction[][] getWindDirectionData() {
        return windDirectionData;
    }

    public BubbleGenerator getBubbleGenerator() {
        return bubbleGenerator;
    }

    public ArrayList<EnemyModel> getEnemies() {
        return Enemies;
    }

    public void newGameReset() {
        createEnemies();
        createBubbleGenerator();
    }
}
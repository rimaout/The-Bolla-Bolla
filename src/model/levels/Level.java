package model.levels;

import entities.Enemy;
import model.utilz.LoadSave;
import model.utilz.Constants.Direction;
import bubbles.specialBubbles.BubbleGenerator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level {

    private BufferedImage levelIMap;
    private int[][] levelData;
    private Direction[][] windDirectionData;
    private BubbleGenerator bubbleGenerator;
    private ArrayList<Enemy> Enemies;

    public Level(BufferedImage levelIMap) {
        this.levelIMap = levelIMap;
        createLevelData();
        createWindDirectionData();
        createBubbleGenerator();
        createEnemies();
    }

    private void createLevelData() {
        levelData = LoadSave.GetLevelData(levelIMap);
    }

    private void createWindDirectionData() {
        windDirectionData = LoadSave.GetWindsDirectionsData(levelIMap);
    }

    private void createBubbleGenerator() {
        bubbleGenerator = LoadSave.GetBubbleGenerator(levelIMap);
    }

    private void createEnemies() {
        Enemies = LoadSave.GetEnemies(levelIMap);
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

    public ArrayList<Enemy> getEnemies() {
        return Enemies;
    }

    public void newGameReset() {
        createEnemies();
        createBubbleGenerator();
    }
}

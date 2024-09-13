package levels;

import bubbles.specialBubbles.BubbleGenerator;
import entities.Enemy;
import utilz.Constants.Direction;
import utilz.LoadSave;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level {

    private BufferedImage levelImage;
    private int[][] levelData;
    private Direction[][] windDirectionData;
    private BubbleGenerator bubbleGenerator;
    private ArrayList<Enemy> Enemies;

    public Level(BufferedImage levelImage) {
        this.levelImage = levelImage;
        createLevelData();
        createWindDirectionData();
        createBubbleGenerator();
        createEnemies();
    }

    private void createLevelData() {
        levelData = LoadSave.GetLevelData(levelImage);
    }

    private void createWindDirectionData() {
        windDirectionData = LoadSave.GetWindsDirectionsData(levelImage);
    }

    private void createBubbleGenerator() {
        bubbleGenerator = LoadSave.GetBubbleGenerator(levelImage);
    }

    private void createEnemies() {
        Enemies = LoadSave.GetEnemies(levelImage);
    }

    public int getSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

    public int[][] getLevelData() {
        return levelData;
    }

    public Direction[][] getWindDirectionData() {
        return windDirectionData;
    }

    public BubbleGenerator getBubbleGenerator() {
        return bubbleGenerator;
    }

    public ArrayList<Enemy> getEnemies() {
        return Enemies;
    }
}

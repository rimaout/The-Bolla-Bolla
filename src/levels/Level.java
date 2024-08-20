package levels;

import entities.Enemy;
import utilz.Constants.Direction;
import static utilz.LoadSave.GetLevelData;
import static utilz.LoadSave.GetWindsDirectionsData;
import static utilz.LoadSave.GetEnemies;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Level {

    private BufferedImage levelImage;
    private int[][] levelData;
    private Direction[][] windDirectionData;
    private ArrayList<Enemy> Enemies;

    public Level(BufferedImage levelImage) {
        this.levelImage = levelImage;
        createLevelData();
        createWindDirectionData();
        createEnemies();
    }

    private void createLevelData() {
        levelData = GetLevelData(levelImage);
    }

   private void createWindDirectionData() {
        windDirectionData = GetWindsDirectionsData(levelImage);
    }

    private void createEnemies() {
        Enemies = GetEnemies(levelImage);
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

    public ArrayList<Enemy> getEnemies() {
        return Enemies;
    }
}

package levels;

import entities.ZenChan;
import utilz.HelpMethods;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.GetLevelData;

public class Level {

    private BufferedImage levelImage;
    private int[][] levelData;
    private ArrayList<ZenChan> zenChans;

    public Level(BufferedImage levelImage) {
        this.levelImage = levelImage;
        createLevelData();
        createEnemies();
    }

    private void createLevelData() {
        levelData = GetLevelData(levelImage);
    }

    private void createEnemies() {
        zenChans = HelpMethods.getZenChans(levelImage);
    }

    public int getSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

    public int[][] getLevelData() {
        return levelData;
    }

    public ArrayList<ZenChan> getZenChans() {
        return zenChans;
    }
}

package levels;

import entities.ZenChan;
import static utilz.LoadSave.GetLevelData;
import static utilz.LoadSave.GetWindsCurrent;
import static utilz.LoadSave.GetZenChans;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Level {

    private BufferedImage levelImage;
    private int[][] levelData;
    private int[][] windCurrentData;
    private ArrayList<ZenChan> zenChans;

    public Level(BufferedImage levelImage) {
        this.levelImage = levelImage;
        createLevelData();
        createEnemies();
    }

    private void createLevelData() {
        levelData = GetLevelData(levelImage);
    }

   private void createWindCurrentData() {
        windCurrentData = GetWindsCurrent(levelImage);
    }

    private void createEnemies() {
        zenChans = GetZenChans(levelImage);
    }

    public int getSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

    public int[][] getLevelData() {
        return levelData;
    }

    public int[][] getWindCurrentData() {
        return windCurrentData;
    }

    public ArrayList<ZenChan> getZenChans() {
        return zenChans;
    }
}

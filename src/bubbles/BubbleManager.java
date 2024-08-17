package bubbles;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import static utilz.Constants.PlayerBubble.*;


public class BubbleManager {
    private BufferedImage[][] bubbleSprites;
    private int[][] levelData, windCurrentData;
    private LinkedList<PlayerBubble> bubbles;

    public BubbleManager() {
        bubbles = new LinkedList<>();
        loadBubbleSprites();
    }

    public void update() {
        for (PlayerBubble b : bubbles) {
            if (b.isActive())
                b.update();
//            else
//                bubbles.remove(b);
        }
    }

    public void draw(Graphics g) {

        for (PlayerBubble b : bubbles) {
            g.drawImage(bubbleSprites[b.getState()][b.getAnimationIndex()], (int) (b.getHitbox().x - DRAWOFFSET_X), (int) (b.getHitbox().y - DRAWOFFSET_Y), IMMAGE_W, IMMAGE_H, null);
        }
    }

    public void addBubble(float x, float y, int direction) {
        bubbles.add(new PlayerBubble(x, y, direction, levelData, windCurrentData));
    }

    public void loadBubbleSprites() {
        // Load bubble sprites
        BufferedImage img = LoadSave.GetSprite(LoadSave.BUBBLE_BUD_SPRITE);

        bubbleSprites = new BufferedImage[5][4];
        for (int j = 0; j < bubbleSprites.length; j++)
            for (int i = 0; i < bubbleSprites[j].length; i++)
                bubbleSprites[j][i] = img.getSubimage(i * DEFAULT_W, j*DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
    }

    public void loadWindData(int[][] windCurrentData) {
        this.windCurrentData = windCurrentData;
    }

    public void resetAll() {
        // Reset the bubble manager
        bubbles = new LinkedList<>();
    }


}

package bubbles.specialBubbles;

import entities.Player;
import levels.LevelManager;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import static utilz.Constants.Bubble.*;

public class SpecialBubbleManager {

    // This class is responsible for managing the special bubbles (not generated by the player), such as waterBubble and fireBubble

    private static SpecialBubbleManager instance;
    private final Player player;

    private BufferedImage[][] waterBubbleSprites;
    private BufferedImage[][] fireBubbleSprites;

    private LinkedList<SpecialBubble> bubbles;
    private LinkedList<WaterFlow> waterFlows;

    private BubbleGenerator bubbleGenerator;


    private SpecialBubbleManager(Player player) {
        this.player = player;
        bubbles = new LinkedList<>();
        waterFlows = new LinkedList<>();

        loadBubbleSprites();
        loadBubbleGenerator();
    }

    public static SpecialBubbleManager getInstance(Player player) {
        if (instance == null) {
            instance = new SpecialBubbleManager(player);
        }
        return instance;
    }

    public static SpecialBubbleManager getInstance() {
        return instance;
    }

    public void draw(Graphics g) {
        for (SpecialBubble b : bubbles) {
            if (b.isActive())
                b.draw(g);
        }

        for (WaterFlow w : waterFlows) {
            if (w.isActive())
                w.draw(g);
        }
    }

    public void update() {
        bubbleGenerator.update();

        for (SpecialBubble b : bubbles) {
            if (b.isActive()) {
                b.update();
                b.checkCollisionWithPlayer(player);
            }
        }

        for (WaterFlow w : waterFlows) {
            if (w.isActive())
                w.update();
        }
    }

    private void loadBubbleGenerator() {
        bubbleGenerator = LevelManager.getInstance().getCurrentLevel().getBubbleGenerator();
    }

    private void loadBubbleSprites() {
        waterBubbleSprites = new BufferedImage[2][1];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.WATER_BUBBLE_SPRITE);
        waterBubbleSprites[0][0] = temp.getSubimage(0 , 0, DEFAULT_W, DEFAULT_H);
        waterBubbleSprites[1][0] = temp.getSubimage(DEFAULT_W , 0, DEFAULT_W, DEFAULT_H);

//        temp = LoadSave.GetSprite(LoadSave.FIRE_BUBBLE_SPRITE);
//        for (int i = 0; i < fireBubbleSprites.length; i++) {
//            for (int j = 0; j < fireBubbleSprites[1].length; j++)
//                fireBubbleSprites[i][j] = temp.getSubimage(j * DEFAULT_W, i * DEFAULT_H, DEFAULT_W, DEFAULT_H);
//        }
    }

    public void resetAll() {
        bubbles.clear();
        waterFlows.clear();
        loadBubbleGenerator();
    }

    public void addBubble(SpecialBubble bubble) {
        bubbles.add(bubble);
    }

    public void addWaterFlow(WaterFlow waterFlow) {
        waterFlows.add(waterFlow);
    }

    public BufferedImage[][] getWaterBubbleSprites() {
        return waterBubbleSprites;
    }

    public BufferedImage[][] getFireBubbleSprites() {
        return fireBubbleSprites;
    }

    public int getActiveBubblesCount() {
        int count = 0;
        for (SpecialBubble b : bubbles) {
            if (b.isActive())
                count++;
        }
        return count;
    }
}

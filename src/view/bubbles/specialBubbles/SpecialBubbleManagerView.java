package view.bubbles.specialBubbles;

import model.bubbles.BubbleModel;
import model.bubbles.specialBubbles.WaterFlowModel;
import model.bubbles.specialBubbles.SpecialBubbleManagerModel;
import view.utilz.LoadSave;
import view.bubbles.BubbleView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import static model.utilz.Constants.PlayerConstants.DEFAULT_H;
import static model.utilz.Constants.PlayerConstants.DEFAULT_W;

public class SpecialBubbleManagerView {
    private static SpecialBubbleManagerView instance;

    private final LinkedList<BubbleView> bubblesViews;
    private final LinkedList<WaterFlowView> waterFlowsViews;
    private final SpecialBubbleManagerModel bubblesManagerModel = SpecialBubbleManagerModel.getInstance();

    private BufferedImage[][] waterBubbleSprites;
    private BufferedImage[][] lightningBubbleSprites;
    private BufferedImage[][] playerSprites;

    private SpecialBubbleManagerView() {
        bubblesViews = new LinkedList<>();
        waterFlowsViews = new LinkedList<>();

        loadBubbleSprites();
    }

    public static SpecialBubbleManagerView getInstance() {
        if (instance == null) {
            instance = new SpecialBubbleManagerView();
        }
        return instance;
    }

    public void draw(Graphics g) {
        syncBubblesViewsWithModel();
        syncWaterFlowsViewsWithModel();

        for (BubbleView b : bubblesViews) {
            if (b.isActive())
                b.draw(g);
        }

        for (WaterFlowView w : waterFlowsViews) {
            if (w.isActive())
                w.draw(g);
        }
    }

    public void update() {
        for (BubbleView b : bubblesViews) {
            if (b.isActive())
                b.update();
        }
    }

    //todo: use hashmap instead of list to increase performance
    private void syncBubblesViewsWithModel() {
        for (BubbleModel bm : bubblesManagerModel.getBubblesModels()) {
            // if a projectile is not in the view, add it
            if (bubblesViews.stream().noneMatch(bv -> bv.getBubbleModel().equals(bm))) {

                switch (bm.getBubbleType()) {
                    case WATER_BUBBLE -> bubblesViews.add(new WaterBubbleView(bm));
                    case LIGHTNING_BUBBLE -> bubblesViews.add(new LightningBubbleView(bm));
                }
            }
        }
    }

    //todo: use hashmap instead of list to increase performance
    private void syncWaterFlowsViewsWithModel() {
        for (WaterFlowModel wm : bubblesManagerModel.getWaterFlowsModels()) {
            // if a projectile is not in the view, add it
            if (waterFlowsViews.stream().noneMatch(wv -> wv.getWaterFlowModel().equals(wm)))
                waterFlowsViews.add(new WaterFlowView(wm));
        }
    }

    private void loadBubbleSprites() {
        waterBubbleSprites = new BufferedImage[2][1];
        BufferedImage temp = LoadSave.GetSprite(LoadSave.WATER_BUBBLE_SPRITE);
        waterBubbleSprites[0][0] = temp.getSubimage(0 , 0, DEFAULT_W, DEFAULT_H);
        waterBubbleSprites[1][0] = temp.getSubimage(DEFAULT_W , 0, DEFAULT_W, DEFAULT_H);

        lightningBubbleSprites = new BufferedImage[2][1];
        temp = LoadSave.GetSprite(LoadSave.LIGHTNING_BUBBLE_SPRITE);
        lightningBubbleSprites[0][0] = temp.getSubimage(0 , 0, DEFAULT_W, DEFAULT_H);
        lightningBubbleSprites[1][0] = temp.getSubimage(DEFAULT_W , 0, DEFAULT_W, DEFAULT_H);

        temp = LoadSave.GetSprite(LoadSave.PLAYER_SPRITE);
        playerSprites = new BufferedImage[6][7];
        for (int j = 0; j < playerSprites.length; j++)
            for (int i = 0; i < playerSprites[j].length; i++)
                playerSprites[j][i] = temp.getSubimage(i * DEFAULT_W, j* DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    public void newLevelReset() {
        bubblesViews.clear();
        waterFlowsViews.clear();
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public BufferedImage[][] getWaterBubbleSprites() {
        return waterBubbleSprites;
    }

    public BufferedImage[][] getLightningBubbleSprites() {
        return lightningBubbleSprites;
    }

    public BufferedImage[][] getPlayerSprites() {
        return playerSprites;
    }
}
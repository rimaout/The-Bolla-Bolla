package view.bubbles.playerBubbles;

import view.utilz.LoadSave;
import model.bubbles.BubbleModel;
import model.bubbles.playerBubbles.PlayerBubblesManagerModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import static model.utilz.Constants.Bubble.DEFAULT_H;
import static model.utilz.Constants.Bubble.DEFAULT_W;

public class PlayerBubblesManagerView {
    private static PlayerBubblesManagerView instance;
    private final PlayerBubblesManagerModel playerBubblesManagerModel = PlayerBubblesManagerModel.getInstance();
    private BufferedImage[][] playerBubbleSprites;
    private final LinkedList<PlayerBubbleView> bubblesViews;

    private PlayerBubblesManagerView() {
        bubblesViews = new LinkedList<>();
        loadBubbleSprites();
    }

    public static PlayerBubblesManagerView getInstance() {
        if (instance == null) {
            instance = new PlayerBubblesManagerView();
        }
        return instance;
    }

    public void update() {
        for (PlayerBubbleView b : bubblesViews) {
            if (b.isActive())
                b.update();
        }
    }

    public void draw(Graphics g) {
        syncBubblesViewsWithModel();

        for (PlayerBubbleView b : bubblesViews) {
            if (b.isActive())
                b.draw(g);
        }
    }

    //todo: use hashmap instead of list to increase performance
    private void syncBubblesViewsWithModel() {
        for (BubbleModel bm : playerBubblesManagerModel.getBubblesModels()) {
            // if a projectile is not in the view, add it
            if (bubblesViews.stream().noneMatch(bv -> bv.getBubbleModel().equals(bm))) {

                switch (bm.getBubbleType()) {
                    case EMPTY_BUBBLE -> bubblesViews.add(new EmptyBubbleView(bm));
                    case ENEMY_BUBBLE -> bubblesViews.add(new EnemyBubbleView(bm));
                }
            }
        }
    }

    private void loadBubbleSprites() {
        BufferedImage img = LoadSave.GetSprite(LoadSave.BUBBLE_BUD_SPRITE);

        playerBubbleSprites = new BufferedImage[6][4];
        for (int j = 0; j < playerBubbleSprites.length; j++)
            for (int i = 0; i < playerBubbleSprites[j].length; i++)
                playerBubbleSprites[j][i] = img.getSubimage(i * DEFAULT_W, j * DEFAULT_H, DEFAULT_W, DEFAULT_H);
    }

    public void newLevelReset() {
        bubblesViews.clear();
    }

    public void newPlayReset() {
        newLevelReset();
    }

    public BufferedImage[][] getPlayerBubbleSprites() {
        return playerBubbleSprites;
    }
}
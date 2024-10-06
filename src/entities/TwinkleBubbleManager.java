package entities;

import main.Game;
import utilz.Constants;
import utilz.LoadSave;
import gameStates.HomeModel;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import static utilz.Constants.Home.BUBBLE_DEFAULT_H;
import static utilz.Constants.Home.BUBBLE_DEFAULT_W;

public class TwinkleBubbleManager {
    private static TwinkleBubbleManager instance;

    private final HomeModel homeModel;

    private BufferedImage[] twinkleBubbleSprite;
    private List<TwinkleBubble> bubbles;

    private TwinkleBubbleManager(HomeModel homeModel) {
        this.homeModel = homeModel;

        loadTwinkleBubble();
        initializeBubbles();
    }

    public static TwinkleBubbleManager getInstance(HomeModel homeModel) {
        if (instance == null) {
            instance = new TwinkleBubbleManager(homeModel);
        }
        return instance;
    }

    public static TwinkleBubbleManager getInstance() {
        return instance;
    }

    public void update() {
        bubbles.forEach(TwinkleBubble::update);
    }

    public void draw(java.awt.Graphics g) {
        bubbles.forEach(bubble -> bubble.draw((java.awt.Graphics2D) g));
    }

    private void loadTwinkleBubble() {
        BufferedImage img = LoadSave.GetSprite(LoadSave.BUBBLE_TWINKLE);

        twinkleBubbleSprite = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            twinkleBubbleSprite[i] = img.getSubimage(i * BUBBLE_DEFAULT_W, 0, BUBBLE_DEFAULT_W, BUBBLE_DEFAULT_H);
        }
    }

    private void initializeBubbles() {
        bubbles = new ArrayList<>();
        int bubbleCount = 50;

        Random random = new Random();

        for (int i = 0; i < bubbleCount; i++) {
            int x = random.nextInt(Constants.GAME_WIDTH);
            int y = random.nextInt(Constants.GAME_HEIGHT);
            bubbles.add(new TwinkleBubble(twinkleBubbleSprite, x, y, this));
        }
    }

    public boolean IsHomeLogoInPosition() {
        return homeModel.IsLogoInPosition();
    }
}
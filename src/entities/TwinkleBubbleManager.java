package entities;

import model.utilz.Constants;
import model.utilz.LoadSave;
import view.gameStates.HomeView;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import static model.utilz.Constants.Home.BUBBLE_DEFAULT_H;
import static model.utilz.Constants.Home.BUBBLE_DEFAULT_W;

public class TwinkleBubbleManager {
    private static TwinkleBubbleManager instance;

    private final HomeView homeView;

    private BufferedImage[] twinkleBubbleSprite;
    private List<TwinkleBubble> bubbles;

    private TwinkleBubbleManager(HomeView homeView) {
        this.homeView = homeView;

        loadTwinkleBubble();
        initializeBubbles();
    }

    public static TwinkleBubbleManager getInstance(HomeView homeView) {
        if (instance == null) {
            instance = new TwinkleBubbleManager(homeView);
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
        return homeView.IsLogoInPosition();
    }
}
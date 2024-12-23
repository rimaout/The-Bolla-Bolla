package view.overlays.menuOverlays;

import model.utilz.Constants;
import view.utilz.Load;
import view.gameStates.HomeView;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import static view.utilz.Constants.Home.BUBBLE_DEFAULT_H;
import static view.utilz.Constants.Home.BUBBLE_DEFAULT_W;

/**
 * The MenuTwinkleBubbleManager class manages the {@link MenuTwinkleBubble} in the menu.
 * It handles loading the bubble sprites, initializing the bubbles, and updating and drawing them.
 */
public class MenuTwinkleBubbleManager {
    private static MenuTwinkleBubbleManager instance;

    private HomeView homeView;

    private BufferedImage[] twinkleBubbleSprite;
    private List<MenuTwinkleBubble> twinkleBubbles;

    /**
     * Private constructor to prevent instantiation.
     * Loads the twinkle bubble sprites and initializes the bubbles.
     */
    private MenuTwinkleBubbleManager() {
        loadTwinkleBubble();
        initializeBubbles();
    }

    /**
     * Returns the singleton instance of MenuTwinkleBubbleManager, creating it if necessary.
     *
     * @return the singleton instance of MenuTwinkleBubbleManager
     */
    public static MenuTwinkleBubbleManager getInstance() {
        if (instance == null) {
            instance = new MenuTwinkleBubbleManager();
        }
        return instance;
    }

    /**
     * Sets the HomeView instance for this manager.
     *
     * @param homeView the HomeView instance
     */
    public void setHomeView(HomeView homeView) {
        this.homeView = homeView;
    }

    /**
     * Updates all the twinkleBubbles.
     */
    public void update() {
        twinkleBubbles.forEach(MenuTwinkleBubble::update);
    }

    /**
     * Draws all the twinkleBubbles.
     *
     * @param g the Graphics object to draw with
     */
    public void draw(java.awt.Graphics g) {
        twinkleBubbles.forEach(bubble -> bubble.draw((java.awt.Graphics2D) g));
    }

    /**
     * Loads the twinkle twinkleBubbles.
     */
    private void loadTwinkleBubble() {
        BufferedImage img = Load.GetSprite(Load.BUBBLE_TWINKLE);

        twinkleBubbleSprite = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            twinkleBubbleSprite[i] = img.getSubimage(i * BUBBLE_DEFAULT_W, 0, BUBBLE_DEFAULT_W, BUBBLE_DEFAULT_H);
        }
    }

    /**
     * Initializes the twinkleBubbles with random positions.
     */
    private void initializeBubbles() {
        twinkleBubbles = new ArrayList<>();
        int bubbleCount = 50;

        Random random = new Random();

        for (int i = 0; i < bubbleCount; i++) {
            int x = random.nextInt(Constants.GAME_WIDTH);
            int y = random.nextInt(Constants.GAME_HEIGHT);
            twinkleBubbles.add(new MenuTwinkleBubble(twinkleBubbleSprite, x, y, this));
        }
    }

    /**
     * Checks if the home logo is in position.
     *
     * @return true if the home logo is in position, false otherwise
     */
    public boolean IsHomeLogoInPosition() {
        return homeView.IsLogoInPosition();
    }
}
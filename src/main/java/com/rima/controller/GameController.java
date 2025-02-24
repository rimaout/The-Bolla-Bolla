package com.rima.controller;

import com.rima.controller.classes.HomeController;
import com.rima.controller.classes.MenuController;
import com.rima.controller.classes.PlayingController;
import com.rima.model.gameStates.GameState;
import com.rima.model.gameStates.LevelTransitionModel;
import com.rima.model.gameStates.PlayingModel;
import com.rima.model.entities.HurryUpManagerModel;
import com.rima.model.gameStates.MenuModel;
import com.rima.view.GamePanel;
import com.rima.view.GameFrame;
import com.rima.view.audio.AudioPlayer;
import com.rima.model.users.UsersManagerModel;
import com.rima.view.entities.HurryUpManagerView;
import com.rima.view.gameStates.HomeView;
import com.rima.view.gameStates.LevelTransitionView;
import com.rima.view.gameStates.MenuView;
import com.rima.view.gameStates.PlayingView;

import com.rima.model.utilz.Constants;

import java.awt.*;

/**
 * Controller for the game, handles:
 * - the creation of the GameStates
 * - the FPS and UPS re-fresh rate (game loop, updates, and rendering).
 */
public class GameController implements Runnable {

    private final GameFrame gameFrame;
    private final GamePanel gamePanel;
    private Thread gameThread;

    private AudioPlayer audioPlayer;
    private UsersManagerModel usersManagerModel;

    private HomeView homeView;
    private HomeController homeController;

    private MenuModel menuModel;
    private MenuView menuView;
    private MenuController menuController;

    private PlayingModel playingModel;
    private PlayingView playingView;
    private PlayingController playingController;

    private LevelTransitionModel levelTransitionModel;
    private LevelTransitionView levelTransitionView;

    /**
     * Constructs a new GameController.
     */
    public GameController() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameFrame = new GameFrame(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();  // Requests focus for the game panel, This ensures that the game panel is the component that receives keyboard input.

        startGameLoop();
    }

    /**
     * Starts the game loop.
     */
    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Initializes the classes of the GameStates.
     */
    private void initClasses() {
        usersManagerModel = UsersManagerModel.getInstance();
        audioPlayer = AudioPlayer.getInstance();

        homeView = new HomeView(this);
        homeController = new HomeController(homeView);

        menuModel = new MenuModel();
        menuView = new MenuView(menuModel);
        menuController = new MenuController(menuModel, menuView);

        playingModel = new PlayingModel();
        playingView = new PlayingView(playingModel);
        playingController = new PlayingController(playingModel, playingView);

        levelTransitionModel = LevelTransitionModel.getInstance();
        levelTransitionModel.initPlayingModel(playingModel);
        levelTransitionView = new LevelTransitionView(levelTransitionModel);

        playingModel.addObserver(playingView);
        HurryUpManagerModel.getInstance().addObserver(HurryUpManagerView.getInstance());
    }

    /**
     * Updates the game (UPS) calling the update method in the right class based in the game state.
     */
    public void update() {

        switch (GameState.state) {
            case MENU:
                menuModel.update();
                menuView.update();
                break;
            case PLAYING:
                playingModel.update();
                playingView.update();
                break;
            case OPTIONS:
            case QUIT:
                System.exit(0);
                break;
            case LEVEL_TRANSITION:
                levelTransitionModel.update();
                levelTransitionView.updatePlayerAnimation();
                break;
        }
    }

    /**
     * Renders the game (FPS) calling the draw method in the right class based in the game state.
     *
     * @param g the graphics object
     */
    public void render(Graphics g) {

        switch (GameState.state) {
            case HOME:
                homeView.draw(g);
                break;
            case MENU:
                menuView.draw(g);
                break;
            case PLAYING:
                playingView.draw(g);
                break;
            case LEVEL_TRANSITION:
                levelTransitionView.draw(g);
                break;
        }
    }

    /**
     * {@inheritDoc} Overrides run method from {@link Runnable} interface.
     * <p>
     * Used to ensure that the game runs at a constant frame rate.
     */
    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / Constants.FPS_SET;
        long lastFrameTime = System.currentTimeMillis();
        int frames = 0;
        double deltaF = 0; // Delta Frame

        double timePerUpdate = 1000000000.0 / Constants.UPS_SET;
        long lastUpdateTime = System.nanoTime();
        int updates = 0;
        double deltaU = 0; // Delta Update


        // Game Loop
        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - lastUpdateTime) / timePerUpdate;
            deltaF += (currentTime - lastUpdateTime) / timePerFrame;
            lastUpdateTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            // FPS | UPS Calculation
            if (System.currentTimeMillis() - lastFrameTime >= 1000) {
                lastFrameTime = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    /**
     * Handles the window focus lost event.
     */
    public void windowFocusLost() {
        // If the game is in the playing state, call the windowFocusLost method in the playing class.
        // If in the menu state, do nothing.
        if (GameState.state == GameState.PLAYING) {
            playingModel.windowFocusLost();
        }
    }

    // ----------------- Getters -----------------

    /**
     * Returns the MenuController instance.
     *
     * @return the MenuController instance
     */
    public MenuController getMenuController() {
        return menuController;
    }

    /**
     * Returns the PlayingModel instance.
     *
     * @return the PlayingModel instance
     */
    public PlayingModel getPlaying() {
        return playingModel;
    }

    /**
     * Returns the PlayingController instance.
     *
     * @return the PlayingController instance
     */
    public PlayingController getPlayingController() {
        return playingController;
    }

    /**
     * Returns the HomeController instance.
     *
     * @return the HomeController instance
     */
    public HomeController getHomeController() {
        return homeController;
    }
}
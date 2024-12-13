package controller;

import controller.classes.*;
import model.entities.HurryUpManagerModel;
import model.gameStates.*;
import model.gameStates.MenuModel;
import view.GamePanel;
import view.GameWindow;
import view.audio.AudioPlayer;
import model.users.UsersManagerModel;
import view.entities.HurryUpManagerView;
import view.gameStates.HomeView;
import view.gameStates.LevelTransitionView;
import view.gameStates.MenuView;
import view.gameStates.PlayingView;
import model.overlays.MenuScoreBoardOverlayModel;
import model.overlays.MenuUserCreationOverlayModel;
import model.overlays.MenuUserSelectionOverlayModel;

import model.utilz.Constants;
import view.overlays.*;

import java.awt.*;

public class GameController implements Runnable {

    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private Thread gameThread;

    private AudioPlayer audioPlayer;
    private UsersManagerModel usersManagerModel;

    private HomeView homeView;
    private HomeController homeController;

    private MenuModel menuModel;
    private MenuView menuView;
    private MenuController menuController;

    private MenuUserCreationOverlayModel menuUserCreationOverlayModel;
    private MenuUserCreationOverlayView menuUserCreationOverlayView;
    private MenuUserCreationOverlayController menuUserCreationOverlayController;

    private MenuUserSelectionOverlayModel menuUserSelectionOverlayModel;
    private MenuUserSelectionOverlayView menuUserSelectionOverlayView;
    private MenuUserSelectionOverlayController menuUserSelectionOverlayController;

    private MenuScoreBoardOverlayModel menuScoreBoardOverlayModel;
    private MenuScoreBoardOverlayView menuScoreBoardOverlayView;
    private MenuScoreBoardOverlayController menuScoreBoardOverlayController;

    private GamePausedOverlayView gamePausedOverlayView;
    private GamePausedOverlayController gamePausedOverlayController;

    private GameOverOverlayView gameOverOverlayView;
    private GameOverOverlayController gameOverOverlayController;

    private GameCompletedOverlayView gameCompletedOverlayView;
    private GameCompletedOverlayController gameCompletedOverlayController;

    private PlayingModel playingModel;
    private PlayingView playingView;
    private PlayingController playingController;

    private LevelTransitionModel levelTransitionModel;
    private LevelTransitionView levelTransitionView;

    public GameController() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();  // Requests focus for the game panel, This ensures that the game panel is the component that receives keyboard input.

        startGameLoop();
    }

    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void initClasses() {
        usersManagerModel = UsersManagerModel.getInstance();
        audioPlayer = AudioPlayer.getInstance();

        homeView = new HomeView(this);
        homeController = new HomeController(homeView);

        menuModel = new MenuModel(this);
        menuView = new MenuView(menuModel, this);
        menuController = new MenuController(this, menuModel);

        menuUserCreationOverlayModel = new MenuUserCreationOverlayModel(menuModel);
        menuUserCreationOverlayView = new MenuUserCreationOverlayView(menuUserCreationOverlayModel);
        menuUserCreationOverlayController = new MenuUserCreationOverlayController(menuModel, menuUserCreationOverlayModel, menuUserCreationOverlayView);

        menuUserSelectionOverlayModel = new MenuUserSelectionOverlayModel(menuModel);
        menuUserSelectionOverlayView = new MenuUserSelectionOverlayView(menuUserSelectionOverlayModel);
        menuUserSelectionOverlayController = new MenuUserSelectionOverlayController(menuModel, menuUserSelectionOverlayModel, menuUserSelectionOverlayView);

        menuScoreBoardOverlayModel = new MenuScoreBoardOverlayModel(menuModel);
        menuScoreBoardOverlayView = new MenuScoreBoardOverlayView(menuScoreBoardOverlayModel);
        menuScoreBoardOverlayController = new MenuScoreBoardOverlayController(menuModel);

        playingModel = new PlayingModel(this);

        gamePausedOverlayView = new GamePausedOverlayView(playingModel);
        gamePausedOverlayController = new GamePausedOverlayController(playingModel);

        gameCompletedOverlayView = new GameCompletedOverlayView(playingModel);
        gameCompletedOverlayController = new GameCompletedOverlayController(gameCompletedOverlayView, playingModel);

        gameOverOverlayView = new GameOverOverlayView(playingModel);
        gameOverOverlayController = new GameOverOverlayController(gameOverOverlayView, playingModel);

        playingController = new PlayingController(playingModel, gamePausedOverlayController, gameCompletedOverlayController, gameOverOverlayController);
        playingView = new PlayingView(playingModel, gamePausedOverlayView, gameOverOverlayView, gameCompletedOverlayView, playingController);

        levelTransitionModel = LevelTransitionModel.getInstance();
        levelTransitionModel.initPlayingModel(playingModel);
        levelTransitionView = new LevelTransitionView(levelTransitionModel);

        playingModel.addObserver(playingView);
        HurryUpManagerModel.getInstance().addObserver(HurryUpManagerView.getInstance());
    }

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

    public void windowFocusLost() {
        // If the game is in the playing state, call the windowFocusLost method in the playing class.
        // If in the menu state, do nothing.
        if (GameState.state == GameState.PLAYING) {
            playingModel.windowFocusLost();
        }
    }

    // ----------------- Getters -----------------

    public HomeView getHome() {
        return homeView;
    }

    public MenuModel getMenu() {
        return menuModel;
    }

    public MenuController getMenuController() {
        return menuController;
    }

    public PlayingModel getPlaying() {
        return playingModel;
    }

    public PlayingController getPlayingController() {
        return playingController;
    }

    public LevelTransitionModel getLevelTransition() {
        return levelTransitionModel;
    }

    public HomeController getHomeController() {
        return homeController;
    }

    public MenuUserCreationOverlayModel getMenuUserCreationOverlayModel() {
        return menuUserCreationOverlayModel;
    }

    public MenuUserCreationOverlayController getMenuUserCreationOverlayController() {
        return menuUserCreationOverlayController;
    }

    public MenuUserSelectionOverlayModel getMenuUserSelectionOverlayModel() {
        return menuUserSelectionOverlayModel;
    }

    public MenuUserSelectionOverlayController getMenuUserSelectionOverlayController() {
        return menuUserSelectionOverlayController;
    }

    public MenuScoreBoardOverlayModel getMenuScoreBoardOverlayModel() {
        return menuScoreBoardOverlayModel;
    }

    public MenuScoreBoardOverlayController getMenuScoreBoardOverlayController() {
        return menuScoreBoardOverlayController;
    }

    public MenuUserSelectionOverlayView getMenuUserSelectionOverlayView() {
        return menuUserSelectionOverlayView;
    }

    public MenuUserCreationOverlayView getMenuUserCreationOverlayView() {
        return menuUserCreationOverlayView;
    }

    public MenuScoreBoardOverlayView getMenuScoreBoardOverlayView() {
        return menuScoreBoardOverlayView;
    }

    public PlayingView getPlayingView() {
        return playingView;
    }
}
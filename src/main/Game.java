package main;

import controller.*;
import gameStates.*;
import gameStates.MenuModel;
import audio.AudioPlayer;
import users.UsersManager;
import view.gameStates.PlayingView;
import view.overlays.MenuScoreBoardOverlayModel;
import view.overlays.MenuUserCreationOverlayModel;
import view.overlays.MenuUserSelectionOverlayModel;

import java.awt.*;

public class Game implements Runnable {

    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private Thread gameThread;

    private AudioPlayer audioPlayer;
    private UsersManager usersManager;

    private HomeModel homeModel;
    //private HomeView homeView;
    private HomeController homeController;

    private MenuModel menuModel;
    //private MenuView menuView;
    private MenuController menuController;

    private MenuUserCreationOverlayModel menuUserCreationOverlayModel;
    private MenuUserCreationOverlayController menuUserCreationOverlayController;

    private MenuUserSelectionOverlayModel menuUserSelectionOverlayModel;
    private MenuUserSelectionOverlayController menuUserSelectionOverlayController;

    private MenuScoreBoardOverlayModel menuScoreBoardOverlayModel;
    private MenuScoreBoardOverlayController menuScoreBoardOverlayController;

    private PlayingModel playingModel;
    private PlayingView playingView;
    private PlayingController playingController;


    private LevelTransition levelTransition;

    private final int FPS_SET = 60;
    private final int UPS_SET = 180;

    public final static int TILES_DEFAULT_SIZE = 8;
    public final static int SCALE = 3;
    public final static int TILES_IN_WIDTH = 32;
    public final static int TILES_IN_HEIGHT = 28;
    public final static int TILES_SIZE = TILES_DEFAULT_SIZE * SCALE;
    public final static int GAME_WIDTH = TILES_IN_WIDTH * TILES_SIZE;
    public final static int GAME_HEIGHT = TILES_IN_HEIGHT * TILES_SIZE;

    public Game() {
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
        usersManager = UsersManager.getInstance(this);
        audioPlayer = AudioPlayer.getInstance();

        homeModel = new HomeModel(this);
        homeController = new HomeController(homeModel);

        menuModel = new MenuModel(this);
        menuController = new MenuController(this, menuModel);

        menuUserCreationOverlayModel = new MenuUserCreationOverlayModel(menuModel);
        menuUserCreationOverlayController = new MenuUserCreationOverlayController(menuModel, menuUserCreationOverlayModel);

        menuUserSelectionOverlayModel = new MenuUserSelectionOverlayModel(menuModel);
        menuUserSelectionOverlayController = new MenuUserSelectionOverlayController(menuModel, menuUserSelectionOverlayModel);

        menuScoreBoardOverlayModel = new MenuScoreBoardOverlayModel(menuModel);
        menuScoreBoardOverlayController = new MenuScoreBoardOverlayController(menuModel);

        playingModel = new PlayingModel(this);
        playingView = new PlayingView(playingModel);
        playingController = new PlayingController(playingModel);

        levelTransition = new LevelTransition(this);
    }


    public void update() {

        switch (GameState.state) {
            case HOME:
                homeModel.update();
                break;
            case MENU:
                menuModel.update();
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
                levelTransition.update();
                break;
        }
    }

    public void render(Graphics g) {

        switch (GameState.state) {
            case HOME:
                homeModel.draw(g);
                break;
            case MENU:
                menuModel.draw(g);
                break;
            case PLAYING:
                playingView.draw(g);
                break;
            case LEVEL_TRANSITION:
                levelTransition.draw(g);
                break;
        }
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        long lastFrameTime = System.currentTimeMillis();
        int frames = 0;
        double deltaF = 0; // Delta Frame

        double timePerUpdate = 1000000000.0 / UPS_SET;
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

    public HomeModel getHome() {
        return homeModel;
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

    public LevelTransition getLevelTransition() {
        return levelTransition;
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
}
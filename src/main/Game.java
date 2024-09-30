package main;

import gameStates.*;
import gameStates.Menu;
import audio.AudioPlayer;
import users.UsersManager;

import java.awt.*;

public class Game implements Runnable {

    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private Thread gameThread;

    private AudioPlayer audioPlayer;
    private UsersManager usersManager;
    private Home home;
    private Menu menu;
    private Playing playing;
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
        home = new Home(this);
        menu = new Menu(this);
        playing = new Playing(this);
        levelTransition = new LevelTransition(this);
    }


    public void update() {

        switch (GameState.state) {
            case HOME:
                home.update();
                break;
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
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
                home.draw(g);
                break;
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
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
            playing.windowFocusLost();
        }
    }

    public Home getHome() {
        return home;
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public LevelTransition getLevelTransition() {
        return levelTransition;
    }
}
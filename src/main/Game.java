package main;

import entities.Player;
import levels.LevelManager;

import java.awt.*;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;

    private Player player;
    private LevelManager levelManager;

    private final int FPS_SET = 60;
    private final int UPS_SET = 180;

    public final static int TILES_DEFAULT_SIZE = 16;
    public final static int SCALE = 3;
    public final static int TILES_IN_WIDTH = 16;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_IN_WIDTH * TILES_SIZE;
    public final static int GAME_HEIGHT = TILES_IN_HEIGHT * TILES_SIZE;

    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();  // Requests focus for the game panel, This ensures that the game panel is the component that receives keyboard input.

        startGameLoop();
    }

    public void startGameLoop() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    private void initClasses() {

        levelManager = new LevelManager(this);
        player = new Player(100, 100, 18*SCALE, 18*SCALE);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
    }


    public void update() {
        player.update();
        levelManager.update();
    }

    public void render(Graphics g) {
        levelManager.draw(g);
        player.render(g);
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

            if(deltaU >= 1) {
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
            if(System.currentTimeMillis() - lastFrameTime >= 1000) {
                lastFrameTime = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }

        }
    }

    public void windowFocusLost() {
       player.resetDirection();
    }

    public Player getPlayer() {
        return player;
    }
}

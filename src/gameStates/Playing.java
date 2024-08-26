package gameStates;

import bubbles.BubbleManager;
import ui.GameCompletedOverlay;
import utilz.LoadSave;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.PauseOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private BubbleManager bubbleManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private BufferedImage[] numbersTiles = new BufferedImage[10];

    private boolean paused;
    private boolean gameOver;
    private boolean gameCompleted;
    private boolean levelCompleted;

    public Playing(Game game) {
        super(game);
        initClasses();
        loadNumberTiles();
        loadStartLevel();
    }

    public void initClasses() {
        levelManager = LevelManager.getInstance(this);

        player = new Player(this);
        player.loadLevelData();

        enemyManager = EnemyManager.getInstance(this, player);

        bubbleManager = BubbleManager.getInstance(this);

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);
    }

    @Override
    public void update() {
        if (paused)
            pauseOverlay.update();

        else if (levelCompleted)
            loadNextLevel();

        else if(!gameOver && !gameCompleted) {
            levelManager.update();
            player.update();
            bubbleManager.update();
            enemyManager.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g);
        enemyManager.draw(g);
        player.draw(g);
        bubbleManager.draw(g);
        drawUI(g);

        if (paused)
            pauseOverlay.draw(g);

        else if (gameOver)
            gameOverOverlay.draw(g);

        else if (gameCompleted)
            gameCompletedOverlay.draw(g);
    }

    private void drawUI(Graphics g) {
        // Draw Points

        // Draw Lives
        BufferedImage liveTile = numbersTiles[player.getLives()];
        g.drawImage(liveTile, 1*Game.TILES_SIZE, 26*Game.TILES_SIZE, 8 * Game.SCALE, 8 * Game.SCALE, null);
    }

    public void resetAll() {
        paused = false;
        levelCompleted = false;
        gameOver = false;
        gameCompleted = false;
        player.resetAll(false);
        enemyManager.resetAll();
        bubbleManager.resetAll();
    }

    public void loadNextLevel() {
        enemyManager.resetAll();
        bubbleManager.resetAll();
        levelManager.loadNextLevel();
        player.resetAll(false);
        levelCompleted = false;
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies();
        enemyManager.loadLevelData();
    }

    private void loadNumberTiles() {

        // load numbers
        numbersTiles = new BufferedImage[10];
        BufferedImage numbersSprite = LoadSave.GetSprite(LoadSave.NUMBERS_TILES_SPRITE);
        for (int i = 0; i < numbersTiles.length; i++) {
            numbersTiles[i] = numbersSprite.getSubimage(i * 8, 0, 8, 8);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver || !paused || !levelCompleted)
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!gameOver || !gameCompleted)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver || !gameCompleted) {
            if (paused)
                pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver || !gameCompleted) {
            if (paused)
                pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver || !gameCompleted) {
            if (paused)
                pauseOverlay.mouseMoved(e);
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else if (gameCompleted)
            gameCompletedOverlay.keyPressed(e);
        else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ENTER:
                    player.setAttacking(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver || !gameCompleted) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
                case KeyEvent.VK_ENTER:
                    player.setAttacking(false);
                    break;
            }
        }
    }

    public void unpauseGame() {
        paused = false;
    }

    public void windowFocusLost() {
        player.resetMovements();
        paused = true;
    }

    public Player getPlayer() {
        return player;
    }

    public BubbleManager getBubbleManager() {
        return bubbleManager;
    }



    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.levelCompleted = levelCompleted;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setGameCompleted(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
    }
}

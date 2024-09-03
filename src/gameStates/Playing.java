package gameStates;

import bubbles.BubbleManager;
import itemesAndRewards.ItemManager;
import itemesAndRewards.PowerUpManager;
import itemesAndRewards.RewardPointsManager;
import ui.GameCompletedOverlay;
import ui.PlayingHud;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.PauseOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements StateMethods {

    private Player playerOne;
    private Player playerTwo = null;

    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private BubbleManager bubbleManager;
    private ItemManager itemManager;
    private RewardPointsManager rewardPointsManager;
    private PowerUpManager powerUpManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private PlayingHud playingHud;

    private boolean paused;
    private boolean gameOver;
    private boolean gameCompleted;
    private boolean levelCompleted;

    public Playing(Game game) {
        super(game);
        initClasses();
        loadStartLevel();
    }

    public void initClasses() {
        levelManager = LevelManager.getInstance(this);

        playerOne = new Player(this);
        playerOne.loadLevelData();

        enemyManager = EnemyManager.getInstance(this, playerOne);
        bubbleManager = BubbleManager.getInstance(playerOne);
        itemManager = ItemManager.getInstance(this);
        rewardPointsManager = RewardPointsManager.getInstance(playerOne);
        powerUpManager = PowerUpManager.getInstance(playerOne);

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);
        playingHud = new PlayingHud(playerOne, playerTwo);
    }

    @Override
    public void update() {

        if (paused)
            pauseOverlay.update();

        else if (levelCompleted)
            loadNextLevel();

        else if(!gameOver && !gameCompleted) {
            levelManager.update();
            playerOne.update();
            bubbleManager.update();
            enemyManager.update();
            itemManager.update();
            rewardPointsManager.update();
            powerUpManager.update();
        }

        updateBolleans();
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g);
        itemManager.draw(g);
        playingHud.draw(g);
        enemyManager.draw(g);
        bubbleManager.draw(g);
        rewardPointsManager.draw((Graphics2D) g);

        playerOne.draw((Graphics2D) g);

        if (paused)
            pauseOverlay.draw(g);

        else if (gameOver)
            gameOverOverlay.draw(g);

        else if (gameCompleted)
            gameCompletedOverlay.draw(g);
    }


    private void updateBolleans(){
        if (playerOne.getLives() == 0)
            gameOver = true;

        if (enemyManager.areAllEnemiesDead() && itemManager.areAllRewardsDeSpawned()) {
            levelCompleted = true;
        }

        if (levelManager.areAllLevelsCompleted())
            gameCompleted = true;
    }

    public void resetAll() {
        paused = false;
        levelCompleted = false;
        gameOver = false;
        gameCompleted = false;
        playerOne.resetAll(true, true);
        enemyManager.resetAll();
        bubbleManager.resetAll();
        itemManager.resetAll();
        powerUpManager.resetAll();
        rewardPointsManager.resetAll();
    }

    public void loadNextLevel() {
        enemyManager.resetAll();
        bubbleManager.resetAll();
        levelManager.loadNextLevel();
        playerOne.resetAll(false, false);
        itemManager.resetAll();
        rewardPointsManager.resetAll();

        levelCompleted = false;
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies();
        enemyManager.loadLevelData();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver || !paused || !levelCompleted)
            if (e.getButton() == MouseEvent.BUTTON1)
                playerOne.setAttacking(true);
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
                    playerOne.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    playerOne.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    playerOne.setJump(true);
                    break;
                case KeyEvent.VK_ENTER:
                    playerOne.setAttacking(true);
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
                    playerOne.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    playerOne.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    playerOne.setJump(false);
                    break;
                case KeyEvent.VK_ENTER:
                    playerOne.setAttacking(false);
                    break;
            }
        }
    }

    public void unpauseGame() {
        paused = false;
    }

    public void windowFocusLost() {
        playerOne.resetMovements();
        paused = true;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

}

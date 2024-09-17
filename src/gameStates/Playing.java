package gameStates;

import bubbles.playerBubbles.PlayerBubblesManager;
import bubbles.specialBubbles.SpecialBubbleManager;
import entities.EnemyManager;
import entities.HurryUpManager;
import entities.Player;
import itemesAndRewards.ItemManager;
import itemesAndRewards.PowerUpManager;
import itemesAndRewards.RewardPointsManager;
import levels.LevelManager;
import main.Game;
import projectiles.ProjectileManager;
import ui.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements StateMethods {
    private Player playerOne;
    private final Player playerTwo = null;

    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private HurryUpManager hurryUpManager;
    private PlayerBubblesManager playerBubblesManager;
    private SpecialBubbleManager specialBubbleManager;
    private ProjectileManager projectileManager;
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

        enemyManager = EnemyManager.getInstance(playerOne);
        hurryUpManager = HurryUpManager.getInstance();
        playerBubblesManager = PlayerBubblesManager.getInstance(playerOne);
        specialBubbleManager = SpecialBubbleManager.getInstance(playerOne);
        projectileManager = ProjectileManager.getInstance(playerOne);
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

        if (levelCompleted)
            loadNextLevel();

        else if(!paused && !gameOver && !gameCompleted) {

            if (playerOne.isActive())
                playerOne.update();

            levelManager.update();
            playerBubblesManager.update();
            specialBubbleManager.update();
            enemyManager.update();
            hurryUpManager.update(playerOne);
            projectileManager.update();
            itemManager.update();
            rewardPointsManager.update();
            powerUpManager.update();
        }

        updateBooleans();
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g);
        itemManager.draw(g);
        playingHud.draw(g);
        enemyManager.draw(g);
        hurryUpManager.draw(g);
        playerBubblesManager.draw(g);
        specialBubbleManager.draw(g);
        projectileManager.draw(g);
        rewardPointsManager.draw((Graphics2D) g);

        if (playerOne.isActive())
            playerOne.draw((Graphics2D) g);

        if (paused)
            pauseOverlay.draw(g);

        else if (gameOver)
            gameOverOverlay.draw(g);

        else if (gameCompleted)
            gameCompletedOverlay.draw(g);
    }


    private void updateBooleans(){
        if (playerOne.getLives() == 0)
            gameOver = true;

        if (enemyManager.getAllEnemiesDeadChronometer() >= 6000)
            levelCompleted = true;

        if (levelManager.areAllLevelsCompleted())
            gameCompleted = true;
    }

    public void resetAll() {
        paused = false;
        levelCompleted = false;
        gameOver = false;
        gameCompleted = false;
        playerOne.resetAll(true, true);
        levelManager.resetAll();
        enemyManager.resetAll();
        hurryUpManager.resetAll();
        projectileManager.resetNewGame();
        playerBubblesManager.resetAll();
        specialBubbleManager.resetAll();
        itemManager.resetAll();
        powerUpManager.resetAll();
        rewardPointsManager.resetAll();
    }

    public void loadNextLevel() {
        levelManager.loadNextLevel();
        enemyManager.resetAll();
        hurryUpManager.resetAll();
        playerBubblesManager.resetAll();
        specialBubbleManager.resetAll();
        projectileManager.resetNewLevel();
        playerOne.resetAll(false, false);
        itemManager.resetAll();
        rewardPointsManager.resetAll();

        levelCompleted = false;
    }

    public void startNewLevel() {
        levelManager.increaseLevelIndex();
        enemyManager.loadEnemies();
        itemManager.resetForNewLevel();
        specialBubbleManager.loadBubbleGenerator();
        playerOne.loadLevelData();
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
        // not used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // not used
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // not used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (paused)
            pauseOverlay.keyPressed(e);

        else if (gameOver)
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
        if (!paused || !gameOver || !gameCompleted) {
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

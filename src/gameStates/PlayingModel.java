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
import view.overlays.*;
import utilz.PlayingTimer;

public class PlayingModel extends State {
    private Player playerOne;
    private Player playerTwo = null;

    private PlayingTimer playingTimer;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private HurryUpManager hurryUpManager;
    private PlayerBubblesManager playerBubblesManager;
    private SpecialBubbleManager specialBubbleManager;
    private ProjectileManager projectileManager;
    private ItemManager itemManager;
    private RewardPointsManager rewardPointsManager;
    private PowerUpManager powerUpManager;
    private Intro intro;

    private boolean intoRunning = true;
    private boolean paused;
    private boolean gameOver;
    private boolean gameCompleted;
    private boolean levelCompleted;

    public PlayingModel(Game game) {
        super(game);
        initClasses();
        loadFirstLevel();
    }

    public void initClasses() {
        playingTimer = PlayingTimer.getInstance();
        levelManager = LevelManager.getInstance(this);

        playerOne = new Player();

        enemyManager = EnemyManager.getInstance(playerOne);
        hurryUpManager = HurryUpManager.getInstance();
        playerBubblesManager = PlayerBubblesManager.getInstance(playerOne);
        specialBubbleManager = SpecialBubbleManager.getInstance(playerOne);
        projectileManager = ProjectileManager.getInstance(playerOne);
        itemManager = ItemManager.getInstance(this);
        rewardPointsManager = RewardPointsManager.getInstance(playerOne);
        powerUpManager = PowerUpManager.getInstance(playerOne);
        intro = new Intro(this);
    }

    public void update() {
        updateBooleans();

        if (paused)
            playingTimer.reset();
        else
            playingTimer.update();

        if (levelCompleted)
            loadNextLevel();

        else if(!paused && !gameOver && !gameCompleted) {

            if (intoRunning) {
                intro.update();
                return;
            }

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
    }

    private void updateBooleans(){
        if (playerOne.getLives() == 0)
            gameOver = true;

        if (enemyManager.getAllEnemiesDeadChronometer() >= 6000)
            levelCompleted = true;

        if (levelManager.areAllLevelsCompleted())
            gameCompleted = true;
    }

    public void newPlayReset() {

        // reset booleans
        intoRunning = true;
        levelCompleted = false;

        // reset classes
        levelManager.newPlayReset();
        enemyManager.newPlayReset();
        hurryUpManager.newPlayReset();
        projectileManager.newPlayReset();
        playerBubblesManager.newPlayReset();
        specialBubbleManager.newPlayReset();
        itemManager.newPlayReset();
        powerUpManager.newPlayReset();
        rewardPointsManager.newPlayReset();
        intro.newPlayReset();

        playerOne.reset(true, true);

        // todo: remove this (doesn't respect mvc) use observer pattern to notify the views to reset
        // Oppure sposta suoni in controlle e non in view, cosi non devi fare il reset (puoi rimuovere first update)
        GameCompletedOverlay.getInstance(this).newPlayReset();
        GameOverOverlay.getInstance(this).newPlayReset();
        GamePauseOverlay.getInstance(this).newPlayReset();

        loadFirstLevel();
    }

    public void restartGame() {
        // used at the end of game over or game completed

        paused = false;
        gameOver = false;
        gameCompleted = false;
    }

    public void loadNextLevel() {
        levelManager.loadNextLevel();
        enemyManager.newLevelReset();
        hurryUpManager.newLevelReset();
        playerBubblesManager.newLevelReset();
        specialBubbleManager.newLevelReset();
        projectileManager.newLevelReset();
        playerOne.reset(false, false);
        itemManager.newPlayReset();
        rewardPointsManager.newPlayReset();

        levelCompleted = false;
    }

    public void startNewLevel() {
        levelManager.increaseLevelIndex();
        enemyManager.loadEnemies();
        itemManager.newLevelReset();
        specialBubbleManager.loadBubbleGenerator();
    }

    private void loadFirstLevel() {
        enemyManager.loadEnemies();
        specialBubbleManager.loadBubbleGenerator();
    }

    public void windowFocusLost() {
        playerOne.resetMovements();
        paused = true;
    }

    public void endIntro() {
        intoRunning = false;
    }

    public void unpauseGame() {
        paused = false;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    public boolean isIntoRunning() {
        return intoRunning;
    }

    // todo: remove this when mvc is complete
    public Intro getIntro() {
        return intro;
    }
}
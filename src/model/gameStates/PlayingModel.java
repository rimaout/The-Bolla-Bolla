package model.gameStates;

import bubbles.playerBubbles.PlayerBubblesManager;
import bubbles.specialBubbles.SpecialBubbleManager;
import entities.EnemyManager;
import entities.HurryUpManager;
import model.entities.PlayerModel;
import itemesAndRewards.ItemManager;
import itemesAndRewards.PowerUpManager;
import itemesAndRewards.RewardPointsManager;
import model.levels.LevelManagerModel;
import main.Game;
import projectiles.ProjectileManager;
import model.utilz.PlayingTimer;

public class PlayingModel extends State {
    private PlayerModel playerModelOne;
    private PlayerModel playerModelTwo = null;

    private PlayingTimer playingTimer;
    private LevelManagerModel levelManagerModel;
    private EnemyManager enemyManager;
    private HurryUpManager hurryUpManager;
    private PlayerBubblesManager playerBubblesManager;
    private SpecialBubbleManager specialBubbleManager;
    private ProjectileManager projectileManager;
    private ItemManager itemManager;
    private RewardPointsManager rewardPointsManager;
    private PowerUpManager powerUpManager;
    private IntroModel introModel;

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
        levelManagerModel = LevelManagerModel.getInstance(this);

        playerModelOne = new PlayerModel();

        enemyManager = EnemyManager.getInstance(playerModelOne);
        hurryUpManager = HurryUpManager.getInstance();
        playerBubblesManager = PlayerBubblesManager.getInstance(playerModelOne);
        specialBubbleManager = SpecialBubbleManager.getInstance(playerModelOne);
        projectileManager = ProjectileManager.getInstance(playerModelOne);
        itemManager = ItemManager.getInstance(this);
        rewardPointsManager = RewardPointsManager.getInstance(playerModelOne);
        powerUpManager = PowerUpManager.getInstance(playerModelOne);
        introModel = new IntroModel(this);
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
                introModel.update();
                return;
            }

            if (playerModelOne.isActive())
                playerModelOne.update();

            playerBubblesManager.update();
            specialBubbleManager.update();
            enemyManager.update();
            hurryUpManager.update(playerModelOne);
            projectileManager.update();
            itemManager.update();
            rewardPointsManager.update();
            powerUpManager.update();
        }
    }

    private void updateBooleans(){
        if (playerModelOne.getLives() == 0)
            gameOver = true;

        if (enemyManager.getAllEnemiesDeadChronometer() >= 6000)
            levelCompleted = true;

        if (levelManagerModel.areAllLevelsCompleted())
            gameCompleted = true;
    }

    public void newPlayReset() {

        // reset booleans
        intoRunning = true;
        levelCompleted = false;

        // reset classes
        levelManagerModel.newPlayReset();
        enemyManager.newPlayReset();
        hurryUpManager.newPlayReset();
        projectileManager.newPlayReset();
        playerBubblesManager.newPlayReset();
        specialBubbleManager.newPlayReset();
        itemManager.newPlayReset();
        powerUpManager.newPlayReset();
        rewardPointsManager.newPlayReset();
        introModel.newPlayReset();

        playerModelOne.reset(true, true);

        loadFirstLevel();
    }

    public void restartGame() {
        // used at the end of game over or game completed

        paused = false;
        gameOver = false;
        gameCompleted = false;
    }

    public void loadNextLevel() {
        levelManagerModel.loadNextLevel();
        enemyManager.newLevelReset();
        hurryUpManager.newLevelReset();
        playerBubblesManager.newLevelReset();
        specialBubbleManager.newLevelReset();
        projectileManager.newLevelReset();
        playerModelOne.reset(false, false);
        itemManager.newPlayReset();
        rewardPointsManager.newPlayReset();

        levelCompleted = false;
    }

    public void startNewLevel() {
        levelManagerModel.increaseLevelIndex();
        enemyManager.loadEnemies();
        itemManager.newLevelReset();
        specialBubbleManager.loadBubbleGenerator();
    }

    private void loadFirstLevel() {
        enemyManager.loadEnemies();
        specialBubbleManager.loadBubbleGenerator();
    }

    public void windowFocusLost() {
        playerModelOne.resetMovements();
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

    public PlayerModel getPlayerOne() {
        return playerModelOne;
    }

    public PlayerModel getPlayerTwo() {
        return playerModelTwo;
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

    public IntroModel getIntroModel() {
        return introModel;
    }
}
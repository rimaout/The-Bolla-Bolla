package model.gameStates;

import bubbles.playerBubbles.PlayerBubblesManager;
import bubbles.specialBubbles.SpecialBubbleManager;
import entities.EnemyManager;
import entities.HurryUpManager;
import model.entities.PlayerModel;
import model.itemesAndRewards.ItemManagerModel;
import model.itemesAndRewards.PowerUpManagerModel;
import model.itemesAndRewards.RewardPointsManagerModel;
import model.levels.LevelManagerModel;
import main.Game;
import model.projectiles.ProjectileManagerModel;
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
    private ProjectileManagerModel projectileManagerModel;
    private ItemManagerModel itemManagerModel;
    private RewardPointsManagerModel rewardPointsManagerModel;
    private PowerUpManagerModel powerUpManagerModel;
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
        projectileManagerModel = ProjectileManagerModel.getInstance(playerModelOne);
        itemManagerModel = ItemManagerModel.getInstance(this);
        rewardPointsManagerModel = RewardPointsManagerModel.getInstance(playerModelOne);
        powerUpManagerModel = PowerUpManagerModel.getInstance(playerModelOne);
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
            projectileManagerModel.update();
            itemManagerModel.update();
            rewardPointsManagerModel.update();
            powerUpManagerModel.update();
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
        projectileManagerModel.newPlayReset();
        playerBubblesManager.newPlayReset();
        specialBubbleManager.newPlayReset();
        itemManagerModel.newPlayReset();
        powerUpManagerModel.newPlayReset();
        rewardPointsManagerModel.newPlayReset();
        introModel.newPlayReset();

        playerModelOne.reset(true, true);

        loadFirstLevel();


        // rest View todo: remove from here, use observer observable.
        game.getPlayingView().newPlayReset();
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
        projectileManagerModel.newLevelReset();
        playerModelOne.reset(false, false);
        itemManagerModel.newPlayReset();
        rewardPointsManagerModel.newLevelReset();

        levelCompleted = false;

        // reset View todo: remove from here, use observer observable.
        game.getPlayingView().newLevelReset();
    }

    public void startNewLevel() {
        levelManagerModel.increaseLevelIndex();
        enemyManager.loadEnemies();
        itemManagerModel.newLevelReset();
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

    public PlayerModel getPlayerOneModel() {
        return playerModelOne;
    }

    public PlayerModel getPlayerTwoModel() {
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
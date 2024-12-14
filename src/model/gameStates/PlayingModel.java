package model.gameStates;

import model.bubbles.playerBubbles.PlayerBubblesManagerModel;
import model.bubbles.specialBubbles.SpecialBubbleManagerModel;
import model.entities.EnemyManagerModel;
import model.entities.HurryUpManagerModel;
import model.entities.PlayerModel;
import model.itemesAndRewards.ItemManagerModel;
import model.itemesAndRewards.PowerUpManagerModel;
import model.itemesAndRewards.RewardPointsManagerModel;
import model.levels.LevelManagerModel;
import model.projectiles.ProjectileManagerModel;
import model.utilz.PlayingTimer;

import java.util.Observable;

public class PlayingModel extends Observable {
    private PlayerModel playerModelOne;
    private PlayerModel playerModelTwo = null;

    private PlayingTimer playingTimer;
    private LevelManagerModel levelManagerModel;
    private EnemyManagerModel enemyManagerModel;
    private HurryUpManagerModel hurryUpManagerModel;
    private PlayerBubblesManagerModel playerBubblesManagerModel;
    private SpecialBubbleManagerModel specialBubbleManagerModel;
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

    // Observer State
    private boolean newLevelReset;
    private boolean newPlayReset;

    public PlayingModel() {
        initClasses();
        loadFirstLevel();
    }

    public void initClasses() {
        playingTimer = PlayingTimer.getInstance();
        levelManagerModel = LevelManagerModel.getInstance();
        playerModelOne = PlayerModel.getInstance();
        enemyManagerModel = EnemyManagerModel.getInstance();
        hurryUpManagerModel = HurryUpManagerModel.getInstance();
        playerBubblesManagerModel = PlayerBubblesManagerModel.getInstance();
        specialBubbleManagerModel = SpecialBubbleManagerModel.getInstance();
        projectileManagerModel = ProjectileManagerModel.getInstance();
        itemManagerModel = ItemManagerModel.getInstance();
        rewardPointsManagerModel = RewardPointsManagerModel.getInstance();
        powerUpManagerModel = PowerUpManagerModel.getInstance();
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

            playerBubblesManagerModel.update();
            specialBubbleManagerModel.update();
            enemyManagerModel.update();
            hurryUpManagerModel.update(playerModelOne);
            projectileManagerModel.update();
            itemManagerModel.update();
            rewardPointsManagerModel.update();
            powerUpManagerModel.update();
        }
    }

    private void updateBooleans(){
        if (playerModelOne.getLives() == 0)
            gameOver = true;

        if (enemyManagerModel.getAllEnemiesDeadChronometer() >= 6000)
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
        enemyManagerModel.newPlayReset();
        hurryUpManagerModel.newPlayReset();
        projectileManagerModel.newPlayReset();
        playerBubblesManagerModel.newPlayReset();
        specialBubbleManagerModel.newPlayReset();
        itemManagerModel.newPlayReset();
        powerUpManagerModel.newPlayReset();
        rewardPointsManagerModel.newPlayReset();
        introModel.newPlayReset();

        playerModelOne.reset(true, true);

        loadFirstLevel();

        // notify view to reset
        newPlayReset = true;
        newLevelReset = false;
        setChanged();
        notifyObservers();
    }

    public void restartGame() {
        // used at the end of game over or game completed

        paused = false;
        gameOver = false;
        gameCompleted = false;
    }

    public void loadNextLevel() {
        levelManagerModel.loadNextLevel();

        enemyManagerModel.newLevelReset();
        hurryUpManagerModel.newLevelReset();
        playerBubblesManagerModel.newLevelReset();
        specialBubbleManagerModel.newLevelReset();
        projectileManagerModel.newLevelReset();
        playerModelOne.reset(false, false);
        itemManagerModel.newPlayReset();
        rewardPointsManagerModel.newLevelReset();

        levelCompleted = false;

        // notify view to reset
        newLevelReset = true;
        newPlayReset = false;
        setChanged();
        notifyObservers();
    }

    public void startNewLevel() {
        levelManagerModel.increaseLevelIndex();
        enemyManagerModel.loadEnemies();
        itemManagerModel.newLevelReset();
        specialBubbleManagerModel.loadBubbleGenerator();
    }

    private void loadFirstLevel() {
        enemyManagerModel.loadEnemies();
        specialBubbleManagerModel.loadBubbleGenerator();
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

    public boolean isNewLevelReset() {
        return newLevelReset;
    }

    public boolean isNewPlayReset() {
        return newPlayReset;
    }
}
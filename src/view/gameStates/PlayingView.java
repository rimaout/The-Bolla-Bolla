package view.gameStates;

import bubbles.playerBubbles.PlayerBubblesManager;
import bubbles.specialBubbles.SpecialBubbleManager;
import entities.EnemyManager;
import entities.HurryUpManager;
import gameStates.PlayingModel;
import itemesAndRewards.ItemManager;
import itemesAndRewards.RewardPointsManager;
import levels.LevelManager;
import projectiles.ProjectileManager;
import view.entities.PlayerView;
import view.overlays.GameCompletedOverlay;
import view.overlays.GameOverOverlay;
import view.overlays.GamePauseOverlay;
import view.overlays.PlayingHud;

import java.awt.*;

public class PlayingView {
    private static PlayingModel playingModel;

    private PlayerView playerOneView;
    private PlayerView playerTwoView = null;

//    private EnemyManagerView enemyManagerView;
//    private HurryUpManagerView hurryUpManagerView;
//    private PlayerBubblesManagerView playerBubblesManagerView;
//    private SpecialBubbleManagerView specialBubbleManagerView;
//    private ProjectileManagerView projectileManagerView;
//    private ItemManagerView itemManagerView;
//    private RewardPointsManagerView rewardPointsManagerView;
//    private PowerUpManagerView powerUpManagerView;
//    private IntroView introView;

    private GamePauseOverlay gamePauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private PlayingHud playingHud;

    public PlayingView(PlayingModel playingModel) {
        this.playingModel = playingModel;

        initClasses();
    }

    public void draw(Graphics g) {

        if (playingModel.isIntoRunning()) {
            playingModel.getIntro().draw(g);
        }
        else {
            LevelManager.getInstance().draw(g);
            ItemManager.getInstance().draw(g);
            playingHud.draw(g);
            EnemyManager.getInstance().draw(g);
            HurryUpManager.getInstance().draw(g);
            PlayerBubblesManager.getInstance().draw(g);
            SpecialBubbleManager.getInstance().draw(g);
            ProjectileManager.getInstance().draw(g);
            RewardPointsManager.getInstance().draw((Graphics2D) g);

            if (playingModel.getPlayerOne().isActive())
                playerOneView.draw((Graphics2D) g);
        }

        if (playingModel.isPaused())
            gamePauseOverlay.draw(g);

        else if (playingModel.isGameOver())
            gameOverOverlay.draw(g);

        else if (playingModel.isGameCompleted())
            gameCompletedOverlay.draw(g);
    }

    private void resetNewLevel() {

    }

    private void resetNewGame() {

    }

    public void initClasses() {
        playerOneView = new PlayerView(playingModel.getPlayerOne());

//        enemyManagerView = new EnemyManagerView();
//        hurryUpManagerView = new HurryUpManagerView();
//        playerBubblesManagerView = new PlayerBubblesManagerView();
//        specialBubbleManagerView = new SpecialBubbleManagerView();
//        projectileManagerView = new ProjectileManagerView();
//        itemManagerView = new ItemManagerView();
//        rewardPointsManagerView = new RewardPointsManagerView();
//        powerUpManagerView = new PowerUpManagerView();
//        introView = new IntroView();

        gamePauseOverlay = GamePauseOverlay.getInstance(playingModel);
        gameOverOverlay = GameOverOverlay.getInstance(playingModel);
        gameCompletedOverlay = GameCompletedOverlay.getInstance(playingModel);
        playingHud = PlayingHud.getInstance(playingModel);
    }

    public void update() {
        playerOneView.update();
    }
}
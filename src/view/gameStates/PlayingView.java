package view.gameStates;

import bubbles.playerBubbles.PlayerBubblesManager;
import bubbles.specialBubbles.SpecialBubbleManager;
import controller.PlayerController;
import controller.PlayingController;
import entities.EnemyManager;
import entities.HurryUpManager;
import model.gameStates.PlayingModel;
import itemesAndRewards.ItemManager;
import itemesAndRewards.RewardPointsManager;
import projectiles.ProjectileManager;
import view.entities.PlayerView;
import view.levels.LevelManagerView;
import view.overlays.GameCompletedOverlayView;
import view.overlays.GameOverOverlayView;
import view.overlays.GamePausedOverlayView;
import view.overlays.PlayingHud;

import java.awt.*;

public class PlayingView {
    private PlayingModel playingModel;

    private PlayerView playerOneView;
    private PlayerView playerTwoView = null;

    private LevelManagerView levelManagerView;
//    private EnemyManagerView enemyManagerView;
//    private HurryUpManagerView hurryUpManagerView;
//    private PlayerBubblesManagerView playerBubblesManagerView;
//    private SpecialBubbleManagerView specialBubbleManagerView;
//    private ProjectileManagerView projectileManagerView;
//    private ItemManagerView itemManagerView;
//    private RewardPointsManagerView rewardPointsManagerView;
//    private PowerUpManagerView powerUpManagerView;

    private GamePausedOverlayView gamePauseOverlayView;
    private GameOverOverlayView gameOverOverlayView;
    private GameCompletedOverlayView gameCompletedOverlayView;
    private IntroView introView;
    private PlayingHud playingHud;

    public PlayingView(PlayingModel playingModel, GamePausedOverlayView gamePausedOverlayView,
                       GameOverOverlayView gameOverOverlayView, GameCompletedOverlayView gameCompletedOverlayView, PlayingController playingController) {

        this.playingModel = playingModel;
        this.gamePauseOverlayView = gamePausedOverlayView;
        this.gameOverOverlayView = gameOverOverlayView;
        this.gameCompletedOverlayView = gameCompletedOverlayView;

        initClasses(playingController.getPlayerController());
    }

    public void draw(Graphics g) {

        if (playingModel.isIntoRunning()) {
            introView.draw(g);
        }
        else {
            levelManagerView.draw(g);
            ItemManager.getInstance().draw(g);
            playingHud.draw(g);
            EnemyManager.getInstance().draw(g);
            HurryUpManager.getInstance().draw(g);
            PlayerBubblesManager.getInstance().draw(g);
            SpecialBubbleManager.getInstance().draw(g);
            ProjectileManager.getInstance().draw(g);
            RewardPointsManager.getInstance().draw((Graphics2D) g);

            if (playingModel.getPlayerOneModel().isActive())
                playerOneView.draw((Graphics2D) g);
        }

        if (playingModel.isPaused())
            gamePauseOverlayView.draw(g);

        else if (playingModel.isGameOver())
            gameOverOverlayView.draw(g);

        else if (playingModel.isGameCompleted())
            gameCompletedOverlayView.draw(g);
    }

    private void resetNewLevel() {

    }

    private void resetNewGame() {
        gamePauseOverlayView.reset();
    }
     
    public void initClasses(PlayerController playerController) {
        playerOneView = new PlayerView(playingModel.getPlayerOneModel(), playerController);

        levelManagerView = LevelManagerView.getInstance();

        //        enemyManagerView = new EnemyManagerView();
//        hurryUpManagerView = new HurryUpManagerView();
//        playerBubblesManagerView = new PlayerBubblesManagerView();
//        specialBubbleManagerView = new SpecialBubbleManagerView();
//        projectileManagerView = new ProjectileManagerView();
//        itemManagerView = new ItemManagerView();
//        rewardPointsManagerView = new RewardPointsManagerView();
//        powerUpManagerView = new PowerUpManagerView();
        introView = new IntroView(playingModel.getIntroModel());

        playingHud = PlayingHud.getInstance(playingModel);
    }

    public void update() {
        playerOneView.update();
        introView.updatePlayerAnimation();
    }
}
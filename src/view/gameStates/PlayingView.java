package view.gameStates;

import bubbles.playerBubbles.PlayerBubblesManager;
import bubbles.specialBubbles.SpecialBubbleManager;
import controller.PlayerController;
import controller.PlayingController;
import model.gameStates.PlayingModel;
import view.entities.EnemyManagerView;
import view.entities.HurryUpManagerView;
import view.entities.PlayerView;
import view.itemsAndRewards.ItemManagerView;
import view.itemsAndRewards.RewardPointsManagerView;
import view.levels.LevelManagerView;
import view.overlays.GameCompletedOverlayView;
import view.overlays.GameOverOverlayView;
import view.overlays.GamePausedOverlayView;
import view.overlays.PlayingHud;
import view.projectiles.ProjectileManagerView;

import java.awt.*;

public class PlayingView {
    private PlayingModel playingModel;

    private PlayerView playerOneView;
    private PlayerView playerTwoView = null;

    private GamePausedOverlayView gamePauseOverlayView;
    private GameOverOverlayView gameOverOverlayView;
    private GameCompletedOverlayView gameCompletedOverlayView;
    private IntroView introView;

    public PlayingView(PlayingModel playingModel, GamePausedOverlayView gamePausedOverlayView,
                       GameOverOverlayView gameOverOverlayView, GameCompletedOverlayView gameCompletedOverlayView, PlayingController playingController) {

        this.playingModel = playingModel;
        this.gamePauseOverlayView = gamePausedOverlayView;
        this.gameOverOverlayView = gameOverOverlayView;
        this.gameCompletedOverlayView = gameCompletedOverlayView;

        initClasses(playingController.getPlayerController());
    }

    public void update() {
        playerOneView.update();
        introView.updatePlayerAnimation();
        ProjectileManagerView.getInstance().update();
        EnemyManagerView.getInstance().update();
        HurryUpManagerView.getInstance().update();
    }

    public void draw(Graphics g) {

        if (playingModel.isIntoRunning()) {
            introView.draw(g);
        }
        else {
            LevelManagerView.getInstance().draw(g);
            ItemManagerView.getInstance().draw(g);
            PlayingHud.getInstance(playingModel).draw(g);
            EnemyManagerView.getInstance().draw(g);
            HurryUpManagerView.getInstance().draw(g);
            PlayerBubblesManager.getInstance().draw(g);
            SpecialBubbleManager.getInstance().draw(g);
            ProjectileManagerView.getInstance().draw(g);
            RewardPointsManagerView.getInstance().draw((Graphics2D) g);

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

    public void newLevelReset() {
        //TODO: sistemare reset per tutte le view

        ProjectileManagerView.getInstance().newLevelReset();
        RewardPointsManagerView.getInstance().newLevelReset();
        ItemManagerView.getInstance().newLevelReset();
        EnemyManagerView.getInstance().newLevelReset();
        HurryUpManagerView.getInstance().newLevelReset();
    }

    public void newPlayReset() {
        gamePauseOverlayView.reset();
        ProjectileManagerView.getInstance().newPlayReset();
        RewardPointsManagerView.getInstance().newPlayReset();
        ItemManagerView.getInstance().newPlayReset();
        EnemyManagerView.getInstance().newPlayReset();
        HurryUpManagerView.getInstance().newPlayReset();
    }
     
    public void initClasses(PlayerController playerController) {
        playerOneView = new PlayerView(playingModel.getPlayerOneModel());
        introView = new IntroView(playingModel.getIntroModel());
    }
}
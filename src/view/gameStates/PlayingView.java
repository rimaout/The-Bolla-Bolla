package view.gameStates;

import controller.PlayerController;
import controller.PlayingController;
import model.gameStates.PlayingModel;
import view.bubbles.playerBubbles.PlayerBubblesManagerView;
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
import view.bubbles.specialBubbles.SpecialBubbleManagerView;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PlayingView implements Observer {
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
        PlayerBubblesManagerView.getInstance().update();
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
            PlayerBubblesManagerView.getInstance().draw(g);
            SpecialBubbleManagerView.getInstance().draw(g);
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
        ProjectileManagerView.getInstance().newLevelReset();
        RewardPointsManagerView.getInstance().newLevelReset();
        ItemManagerView.getInstance().newLevelReset();
        EnemyManagerView.getInstance().newLevelReset();
        HurryUpManagerView.getInstance().newLevelReset();
        PlayerBubblesManagerView.getInstance().newLevelReset();
        SpecialBubbleManagerView.getInstance().newLevelReset();
    }

    public void newPlayReset() {
        gamePauseOverlayView.reset();
        ProjectileManagerView.getInstance().newPlayReset();
        RewardPointsManagerView.getInstance().newPlayReset();
        ItemManagerView.getInstance().newPlayReset();
        EnemyManagerView.getInstance().newPlayReset();
        HurryUpManagerView.getInstance().newPlayReset();
        PlayerBubblesManagerView.getInstance().newPlayReset();
        SpecialBubbleManagerView.getInstance().newPlayReset();
    }
     
    public void initClasses(PlayerController playerController) {
        playerOneView = new PlayerView(playingModel.getPlayerOneModel());
        introView = new IntroView(playingModel.getIntroModel());
    }

    @Override
    public void update(Observable o, Object arg) {
        PlayingModel playingModel = (PlayingModel) o;

        if (playingModel.isNewPlayReset())
            newPlayReset();

        if (playingModel.isNewLevelReset())
            newLevelReset();
    }
}
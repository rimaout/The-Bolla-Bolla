package view.gameStates;

import bubbles.playerBubbles.PlayerBubblesManager;
import bubbles.specialBubbles.SpecialBubbleManager;
import controller.PlayerController;
import controller.PlayingController;
import entities.HurryUpManager;
import model.gameStates.PlayingModel;
import view.entities.EnemyManagerView;
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

    private LevelManagerView levelManagerView;
//    private HurryUpManagerView hurryUpManagerView;
//    private PlayerBubblesManagerView playerBubblesManagerView;
//    private SpecialBubbleManagerView specialBubbleManagerView;

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

    public void update() {
        playerOneView.update();
        introView.updatePlayerAnimation();
        ProjectileManagerView.getInstance().update();
        EnemyManagerView.getInstance().update();
    }

    public void draw(Graphics g) {

        if (playingModel.isIntoRunning()) {
            introView.draw(g);
        }
        else {
            levelManagerView.draw(g);
            ItemManagerView.getInstance().draw(g);
            playingHud.draw(g);
            EnemyManagerView.getInstance().draw(g);
            HurryUpManager.getInstance().draw(g);
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
    }

    public void newPlayReset() {
        gamePauseOverlayView.reset();
        ProjectileManagerView.getInstance().newPlayReset();
        RewardPointsManagerView.getInstance().newPlayReset();
        ItemManagerView.getInstance().newPlayReset();
        EnemyManagerView.getInstance().newPlayReset();
    }
     
    public void initClasses(PlayerController playerController) {
        playerOneView = new PlayerView(playingModel.getPlayerOneModel());

        levelManagerView = LevelManagerView.getInstance();

//        hurryUpManagerView = new HurryUpManagerView();
//        playerBubblesManagerView = new PlayerBubblesManagerView();
//        specialBubbleManagerView = new SpecialBubbleManagerView();
        introView = new IntroView(playingModel.getIntroModel());

        playingHud = PlayingHud.getInstance(playingModel);
    }
}
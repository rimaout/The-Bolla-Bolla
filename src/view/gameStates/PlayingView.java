package view.gameStates;

import gameStates.Playing;
import view.overlays.GameCompletedOverlay;
import view.overlays.GameOverOverlay;
import view.overlays.GamePauseOverlay;
import view.overlays.PlayingHud;

public class PlayingView {
    private static Playing playing;

//    private PlayerView playerOneView;
//    private PlayerView playerTwoView = null;
//
//    private EnemyManagerView enemyManagerView;
//    private HurryUpManagerView hurryUpManagerView;
//    private PlayerBubblesManagerView playerBubblesManagerView;
//    private SpecialBubbleManagerView specialBubbleManagerView;
//    private ProjectileManagerView projectileManagerView;
//    private ItemManagerView itemManagerView;
//    private RewardPointsManagerView rewardPointsManagerView;
//    private PowerUpManagerView powerUpManagerView;
//    private IntroView introView;

    private GamePauseOverlay gamePauseOverlayView;
    private GameOverOverlay gameOverOverlayView;
    private GameCompletedOverlay gameCompletedOverlayView;
    private PlayingHud playingHudView;

    public PlayingView(Playing playing) {
        this.playing = playing;

        initClasses();
    }

    public void initClasses() {
//        playerOneView = new PlayerView();

//        enemyManagerView = new EnemyManagerView();
//        hurryUpManagerView = new HurryUpManagerView();
//        playerBubblesManagerView = new PlayerBubblesManagerView();
//        specialBubbleManagerView = new SpecialBubbleManagerView();
//        projectileManagerView = new ProjectileManagerView();
//        itemManagerView = new ItemManagerView();
//        rewardPointsManagerView = new RewardPointsManagerView();
//        powerUpManagerView = new PowerUpManagerView();
//        introView = new IntroView();

        gamePauseOverlayView = new GamePauseOverlay(playing);           // todo: use singleton
        gameOverOverlayView = new GameOverOverlay(playing);             // todo: use singleton
        gameCompletedOverlayView = new GameCompletedOverlay(playing);   // todo: use singleton
        playingHudView = new PlayingHud(playing);
    }


}
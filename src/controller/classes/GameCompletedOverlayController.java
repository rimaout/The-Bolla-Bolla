package controller.classes;

import model.gameStates.GameState;
import model.gameStates.PlayingModel;
import model.users.UsersManager;
import view.overlays.GameCompletedOverlayView;

import java.awt.event.KeyEvent;

public class GameCompletedOverlayController {
    private final GameCompletedOverlayView gameCompletedOverlayView;
    private final PlayingModel playingModel;

    public GameCompletedOverlayController(GameCompletedOverlayView gameCompletedOverlayView, PlayingModel playingModel) {
        this.gameCompletedOverlayView = gameCompletedOverlayView;
        this.playingModel = playingModel;
    }


    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            UsersManager.getInstance().updateCurrentUserInfo(false);
            playingModel.newPlayReset();
            playingModel.restartGame();
            GameState.state = GameState.MENU;

            gameCompletedOverlayView.reset();
        }

        if (e.getKeyCode() == KeyEvent.VK_R) {
            UsersManager.getInstance().updateCurrentUserInfo(false);
            playingModel.newPlayReset();
            playingModel.restartGame();
            GameState.state = GameState.PLAYING;

            gameCompletedOverlayView.reset();
        }
    }
}

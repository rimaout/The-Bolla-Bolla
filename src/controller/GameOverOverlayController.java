package controller;

import gameStates.GameState;
import gameStates.PlayingModel;
import users.UsersManager;
import view.overlays.GameOverOverlayView;

import java.awt.event.KeyEvent;

public class GameOverOverlayController {
    private final GameOverOverlayView gameOverOverlayView;
    private final PlayingModel playingModel;

    public GameOverOverlayController(GameOverOverlayView gameOverOverlayView, PlayingModel playingModel) {
        this.gameOverOverlayView = gameOverOverlayView;
        this.playingModel = playingModel;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            UsersManager.getInstance().updateCurrentUserInfo(false);
            playingModel.newPlayReset();
            playingModel.restartGame();
            GameState.state = GameState.MENU;

            gameOverOverlayView.reset();
        }

        if (e.getKeyCode() == KeyEvent.VK_R) {
            UsersManager.getInstance().updateCurrentUserInfo(false);
            playingModel.newPlayReset();
            playingModel.restartGame();
            GameState.state = GameState.PLAYING;

            gameOverOverlayView.reset();
        }
    }
}

package controller;

import audio.AudioPlayer;
import gameStates.GameState;
import gameStates.PlayingModel;
import view.overlays.GamePausedOverlayView;

import java.awt.event.KeyEvent;

public class GamePausedOverlayController {
    private final PlayingModel playingModel;
    private final GamePausedOverlayView gamePauseOverlayView;

    public GamePausedOverlayController(PlayingModel playingModel) {
        this.gamePauseOverlayView = GamePausedOverlayView.getInstance(playingModel);
        this.playingModel = playingModel;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            playingModel.newPlayReset();
            playingModel.restartGame();
            GameState.state = GameState.MENU;
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playingModel.unpauseGame();
            AudioPlayer.getInstance().startSong();
        }
    }
}
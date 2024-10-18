package controller;

import audio.AudioPlayer;
import model.gameStates.GameState;
import model.gameStates.PlayingModel;

import java.awt.event.KeyEvent;

public class GamePausedOverlayController {
    private final PlayingModel playingModel;

    public GamePausedOverlayController(PlayingModel playingModel) {
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
package controller.classes;

import controller.inputs.InputMethods;
import model.gameStates.PlayingModel;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlayingController implements InputMethods {
    private final PlayingModel playingModel;

    private final GamePausedOverlayController gamePausedOverlayController;
    private final GameCompletedOverlayController gameCompletedOverlayController;
    private final GameOverOverlayController gameOverOverlayController;

    private final PlayerController playerController;

    public PlayingController(PlayingModel playingModel, GamePausedOverlayController gamePausedOverlayController,
                             GameCompletedOverlayController gameCompletedOverlayController,
                             GameOverOverlayController gameOverOverlayController) {
        this.playingModel = playingModel;
        this.gamePausedOverlayController = gamePausedOverlayController;
        this.gameCompletedOverlayController = gameCompletedOverlayController;
        this.gameOverOverlayController = gameOverOverlayController;

        playerController = new PlayerController(playingModel.getPlayerOneModel());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!playingModel.isGameOver() || !playingModel.isPaused() || !playingModel.isLevelCompleted())
            if (e.getButton() == MouseEvent.BUTTON1)
                playingModel.getPlayerOneModel().setAttacking(true);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // not used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // not used
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // not used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (playingModel.isPaused())
            gamePausedOverlayController.keyPressed(e);

        else if (playingModel.isGameOver())
            gameOverOverlayController.keyPressed(e);

        else if (playingModel.isGameCompleted())
            gameCompletedOverlayController.keyPressed(e);

        else if (KeyEvent.VK_ESCAPE == e.getKeyCode())
            playingModel.setPaused(!playingModel.isPaused());

        else
            playerController.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!playingModel.isPaused() || !playingModel.isGameOver() || !playingModel.isGameCompleted()) {
            playerController.keyReleased(e);
        }
    }

    public PlayerController getPlayerController() {
        return playerController;
    }
}

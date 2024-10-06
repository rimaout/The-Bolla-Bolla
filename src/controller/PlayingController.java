package controller;

import controller.inputs.InputMethods;
import gameStates.PlayingModel;
import view.overlays.GameCompletedOverlay;
import view.overlays.GameOverOverlay;
import view.overlays.GamePauseOverlay;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlayingController implements InputMethods {
    private final PlayingModel playingModel;

    public PlayingController(PlayingModel playingModel) {
        this.playingModel = playingModel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!playingModel.isGameOver() || !playingModel.isPaused() || !playingModel.isLevelCompleted())
            if (e.getButton() == MouseEvent.BUTTON1)
                playingModel.getPlayerOne().setAttacking(true);
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
            GamePauseOverlay.getInstance(playingModel).keyPressed(e);

        else if (playingModel.isGameOver())
            GameOverOverlay.getInstance(playingModel).keyPressed(e);

        else if (playingModel.isGameCompleted())
            GameCompletedOverlay.getInstance(playingModel).keyPressed(e);

        else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    playingModel.getPlayerOne().setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    playingModel.getPlayerOne().setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    playingModel.getPlayerOne().setJump(true);
                    break;
                case KeyEvent.VK_ENTER:
                    playingModel.getPlayerOne().setAttacking(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    playingModel.setPaused(!playingModel.isPaused());
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!playingModel.isPaused() || !playingModel.isGameOver() || !playingModel.isGameCompleted()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    playingModel.getPlayerOne().setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    playingModel.getPlayerOne().setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    playingModel.getPlayerOne().setJump(false);
                    break;
                case KeyEvent.VK_ENTER:
                    playingModel.getPlayerOne().setAttacking(false);
                    break;
            }
        }
    }
}

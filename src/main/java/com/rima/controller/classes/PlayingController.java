package com.rima.controller.classes;

import com.rima.controller.inputs.InputMethods;
import com.rima.model.gameStates.PlayingModel;
import com.rima.view.gameStates.PlayingView;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Controller for handling the user interaction during the playing state of the game.
 */
public class PlayingController implements InputMethods {
    private final PlayingModel playingModel;

    private final GamePausedOverlayController gamePausedOverlayController;
    private final GameCompletedOverlayController gameCompletedOverlayController;
    private final GameOverOverlayController gameOverOverlayController;

    private final PlayerController playerController;

    /**
     * Constructs a new PlayingController.
     *
     * @param playingModel the model for the playing state
     */
    public PlayingController(PlayingModel playingModel, PlayingView playingView) {
        this.playingModel = playingModel;

        gamePausedOverlayController = new GamePausedOverlayController(playingModel);
        gameCompletedOverlayController = new GameCompletedOverlayController(playingView.getGameCompletedOverlayView(), playingModel);
        gameOverOverlayController = new GameOverOverlayController(playingView.getGameOverOverlayView(), playingModel);

        playerController = new PlayerController(playingModel.getPlayerOneModel());
    }

    /**
     * {@inheritDoc} Overrides mouseClicked method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!playingModel.isGameOver() || !playingModel.isPaused() || !playingModel.isLevelCompleted())
            if (e.getButton() == MouseEvent.BUTTON1)
                playingModel.getPlayerOneModel().setAttacking(true);
    }

    /**
     * {@inheritDoc} Overrides mouseDragged method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides mousePressed method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides mouseReleased method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides mouseMoved method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides keyPressed method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
     */
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

    /**
     * {@inheritDoc} Overrides keyReleased method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (!playingModel.isPaused() || !playingModel.isGameOver() || !playingModel.isGameCompleted()) {
            playerController.keyReleased(e);
        }
    }
}
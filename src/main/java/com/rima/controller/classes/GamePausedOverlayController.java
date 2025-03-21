package com.rima.controller.classes;

import com.rima.view.audio.AudioPlayer;
import com.rima.model.gameStates.GameState;
import com.rima.model.gameStates.PlayingModel;
import com.rima.controller.inputs.InputMethods;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Controller for handling the user interaction with the game paused overlay.
 */
public class GamePausedOverlayController implements InputMethods {
    private final PlayingModel playingModel;

    /**
     * Constructs a new GamePausedOverlayController.
     *
     * @param playingModel used to change the playing state (restart new game or unpause the game)
     */
    public GamePausedOverlayController(PlayingModel playingModel) {
        this.playingModel = playingModel;
    }

    /**
     * {@inheritDoc} Overrides mouseClicked method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides mousePressed method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides mouseReleased method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
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
     * {@inheritDoc} Overrides mouseDragged method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides keyPressed method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
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

    /**
     * {@inheritDoc} Overrides keyReleased method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // not used
    }
}
package com.rima.controller.classes;

import com.rima.model.gameStates.GameState;
import com.rima.model.gameStates.PlayingModel;
import com.rima.model.users.UsersManagerModel;
import com.rima.controller.inputs.InputMethods;
import com.rima.view.overlays.gameOverlays.GameOverOverlayView;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Controller for handling the user interaction with the game over overlay.
 */
public class GameOverOverlayController implements InputMethods {
    private final GameOverOverlayView gameOverOverlayView;
    private final PlayingModel playingModel;

    /**
     * Constructs a new GameOverOverlayController.
     *
     * @param gameOverOverlayView the view for the game over overlay used to display the overlay
     * @param playingModel used to change the playing state (restart new game or go back to menu)
     */
    public GameOverOverlayController(GameOverOverlayView gameOverOverlayView, PlayingModel playingModel) {
        this.gameOverOverlayView = gameOverOverlayView;
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
            UsersManagerModel.getInstance().updateCurrentUserInfo(false);
            playingModel.newPlayReset();
            playingModel.restartGame();
            GameState.state = GameState.MENU;

            gameOverOverlayView.reset();
        }

        if (e.getKeyCode() == KeyEvent.VK_R) {
            UsersManagerModel.getInstance().updateCurrentUserInfo(false);
            playingModel.newPlayReset();
            playingModel.restartGame();
            GameState.state = GameState.PLAYING;

            gameOverOverlayView.reset();
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
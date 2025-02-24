package com.rima.controller.inputs;

import com.rima.view.GamePanel;
import com.rima.model.gameStates.GameState;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * This class implements the {@link MouseMotionListener} interface.
 * Controller for listening to mouse motion inputs and handling them, rerouting the information to the right input controller based on the game state.
 */
public class MouseMotionInputs implements MouseMotionListener {
    private GamePanel gamePanel;

    /**
     * Constructs a new MouseMotionInputs.
     *
     * @param gamePanel the game panel
     */
    public MouseMotionInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * {@inheritDoc} Overrides mouseDragged method from {@link MouseMotionListener} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        switch (GameState.state) {

            case PLAYING:
                gamePanel.getGameController().getPlayingController().mouseDragged(e);
                break;
        }
    }

    /**
     * {@inheritDoc} Overrides mouseMoved method from {@link MouseMotionListener} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGameController().getMenuController().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGameController().getPlayingController().mouseMoved(e);
                break;
        }
    }
}
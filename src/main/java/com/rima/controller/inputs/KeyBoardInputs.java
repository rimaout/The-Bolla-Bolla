package com.rima.controller.inputs;

import com.rima.view.GamePanel;
import com.rima.model.gameStates.GameState;

import java.awt.event.KeyListener;

/**
 * This class implements the {@link KeyListener} interface.
 * Controller for listening to keyboard inputs and handling them, rerouting the information to the right input controller based on the game state.
 */
public class KeyBoardInputs implements KeyListener {
    private final GamePanel gamePanel;

    /**
     * Constructs a new KeyBoardInputs.
     *
     * @param gamePanel the game panel
     */
    public KeyBoardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * {@inheritDoc} Overrides keyTyped method from {@link KeyListener} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
        // Auto-generated method stub
    }

    /**
     * {@inheritDoc} Overrides keyPressed method from {@link KeyListener} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        switch (GameState.state) {
            case HOME:
                gamePanel.getGameController().getHomeController().keyPressed(e);
                break;
            case MENU:
                gamePanel.getGameController().getMenuController().keyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGameController().getPlayingController().keyPressed(e);
                break;
        }
    }

    /**
     * {@inheritDoc} Overrides keyReleased method from {@link KeyListener} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        switch (GameState.state) {
            case HOME:
                gamePanel.getGameController().getHomeController().keyReleased(e);
                break;
            case MENU:
                gamePanel.getGameController().getMenuController().keyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGameController().getPlayingController().keyReleased(e);
                break;
        }
    }
}
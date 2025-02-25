package com.rima.controller.classes;

import com.rima.model.entities.PlayerModel;
import com.rima.controller.inputs.InputMethods;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Controller for handling the user interaction with the player.
 */
public class PlayerController implements InputMethods {
    private final PlayerModel playerModel;

    /**
     * Constructs a new PlayerController.
     *
     * @param playerModel the model for the player
     */
    public PlayerController(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    /**
     * {@inheritDoc} Overrides mouseClicked method from {@link InputMethods} interface.
     *
     * @param e the MouseEvent to handle
     */
    @Override
    public void mouseClicked(MouseEvent e) {
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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A, KeyEvent.VK_LEFT, KeyEvent.VK_H:
                playerModel.setLeft(true);
                break;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT, KeyEvent.VK_L:
                playerModel.setRight(true);
                break;
            case KeyEvent.VK_SPACE, KeyEvent.VK_X:
                playerModel.setJump(true);
                break;
            case KeyEvent.VK_ENTER, KeyEvent.VK_Z:
                playerModel.setAttacking(true);
                break;
        }
    }

    /**
     * {@inheritDoc} Overrides keyReleased method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A, KeyEvent.VK_LEFT, KeyEvent.VK_H:
                playerModel.setLeft(false);
                break;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT, KeyEvent.VK_L:
                playerModel.setRight(false);
                break;
            case KeyEvent.VK_SPACE, KeyEvent.VK_X:
                playerModel.setJump(false);
                break;
            case KeyEvent.VK_ENTER, KeyEvent.VK_Z:
                playerModel.setAttacking(false);
                break;
        }
    }
}
package com.rima.controller.classes;

import com.rima.model.gameStates.MenuModel;
import com.rima.controller.inputs.InputMethods;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Controller for handling the user interaction with the score board overlay in the menu.
 */
public class MenuScoreBoardOverlayController implements InputMethods {
    private final MenuModel menuModel;

    /**
     * Constructs a new MenuScoreBoardOverlayController.
     *
     * @param menuModel the model for the menu
     */
    public MenuScoreBoardOverlayController(MenuModel menuModel) {
        this.menuModel = menuModel;
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
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            menuModel.setScoreBoardOverlayActive(false);
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
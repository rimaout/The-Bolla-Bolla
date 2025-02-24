package com.rima.controller.classes;

import com.rima.model.gameStates.MenuModel;
import com.rima.model.users.UsersManagerModel;
import com.rima.model.overlays.MenuUserCreationOverlayModel;
import com.rima.controller.inputs.InputMethods;
import com.rima.view.gameStates.MenuView;
import com.rima.view.overlays.menuOverlays.MenuUserCreationOverlayView;
import com.rima.view.users.UsersManagerView;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Controller for handling the user interaction with the user creation overlay in the menu.
 */
public class MenuUserCreationOverlayController implements InputMethods {
    private final MenuModel menuModel;

    private final UsersManagerModel usersManagerModel = UsersManagerModel.getInstance();
    private final UsersManagerView usersManagerView = UsersManagerView.getInstance();
    private final MenuUserCreationOverlayModel menuUserCreationOverlayModel;
    private final MenuUserCreationOverlayView menuUserCreationOverlayView;

    /**
     * Constructs a new MenuUserCreationOverlayController.
     *
     * @param menuModel the model for the menu
     * @param menuView the view for the menu
     */
    public MenuUserCreationOverlayController(MenuModel menuModel, MenuView menuView) {
        this.menuModel = menuModel;
        this.menuUserCreationOverlayModel = menuModel.getMenuUserCreationOverlayModel();
        this.menuUserCreationOverlayView = menuView.getMenuUserCreationOverlayView();
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
     * @param e the KeyEvent to handle
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        // not used
    }

    /**
     * {@inheritDoc} Overrides mouseDragged method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
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
            menuModel.setUserSelectionOverlayActive(true);
            menuModel.setUserCreationOverlayActive(false);
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER && !menuUserCreationOverlayModel.isEnterNameDeactivated()) {
            usersManagerModel.createUser(menuUserCreationOverlayModel.getNewUserName(), menuUserCreationOverlayModel.getNewUserPictureIndex());
            menuModel.setUserSelectionOverlayActive(false);
            menuModel.setUserCreationOverlayActive(false);
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            menuUserCreationOverlayView.setUpArrowIndex(1); // animate the arrow

            if (menuUserCreationOverlayModel.getNewUserPictureIndex() + 1 == usersManagerView.getUserPicturesCount())
                menuUserCreationOverlayModel.setNewUserPictureIndex(0);
            else
                menuUserCreationOverlayModel.increaseNewUserPictureIndex();
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            menuUserCreationOverlayView.setDownArrowIndex(1); // animate the arrow

            if (menuUserCreationOverlayModel.getNewUserPictureIndex() - 1 == -1 || menuUserCreationOverlayModel.getNewUserPictureIndex() - 1 == -2)
                menuUserCreationOverlayModel.setNewUserPictureIndex(usersManagerView.getUserPicturesCount() - 1);
            else
                menuUserCreationOverlayModel.decreaseNewUserPictureIndex();
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (!menuUserCreationOverlayModel.getNewUserName().isEmpty()) {
                menuUserCreationOverlayModel.setNewUserName(menuUserCreationOverlayModel.getNewUserName().substring(0, menuUserCreationOverlayModel.getNewUserName().length() - 1));
            }
        } else {
            char keyChar = Character.toLowerCase(e.getKeyChar());
            if (Character.isLetterOrDigit(keyChar) && menuUserCreationOverlayModel.getNewUserName().length() < 8) {
                menuUserCreationOverlayModel.setNewUserName(menuUserCreationOverlayModel.getNewUserName() + keyChar);
            }
        }
    }

    /**
     * {@inheritDoc} Overrides keyReleased method from {@link InputMethods} interface.
     *
     * @param e the KeyEvent to handle
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            menuUserCreationOverlayView.setUpArrowIndex(0); // stop animating the arrow

        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            menuUserCreationOverlayView.setDownArrowIndex(0); // stop animating the arrow
    }
}
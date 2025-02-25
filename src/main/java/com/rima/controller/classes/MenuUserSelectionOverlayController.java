package com.rima.controller.classes;

import com.rima.model.gameStates.MenuModel;
import com.rima.model.users.UsersManagerModel;
import com.rima.controller.inputs.InputMethods;
import com.rima.model.overlays.MenuUserSelectionOverlayModel;
import com.rima.view.gameStates.MenuView;
import com.rima.view.overlays.menuOverlays.MenuUserSelectionOverlayView;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Controller for handling the user interaction with the user selection overlay in the menu.
 */
public class MenuUserSelectionOverlayController implements InputMethods {
    private final MenuUserSelectionOverlayModel menuUserSelectionOverlayModel;
    private final MenuUserSelectionOverlayView menuUserSelectionOverlayView;
    private final MenuModel menuModel;
    private final UsersManagerModel usersManagerModel = UsersManagerModel.getInstance();

    /**
     * Constructs a new MenuUserSelectionOverlayController.
     *
     * @param menuModel the model for the menu
     * @param menuView the view for the menu
     */
    public MenuUserSelectionOverlayController(MenuModel menuModel, MenuView menuView) {
        this.menuModel = menuModel;
        this.menuUserSelectionOverlayModel = menuModel.getMenuUserSelectionOverlayModel();
        this.menuUserSelectionOverlayView = menuView.getMenuUserSelectionOverlayView();
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

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && usersManagerModel.getCurrentUser() != null) {
            menuModel.setUserSelectionOverlayActive(false);
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            usersManagerModel.setCurrentUser(menuUserSelectionOverlayModel.getSelectedUser());
            menuModel.setUserSelectionOverlayActive(false);
        }

        if (e.getKeyCode() == KeyEvent.VK_C) {
            menuModel.setUserSelectionOverlayActive(false);
            menuModel.setUserCreationOverlayActive(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A  || e.getKeyCode() == KeyEvent.VK_H) {
            menuUserSelectionOverlayView.setLeftArrowIndex(1); // animate the arrow
            int index = menuUserSelectionOverlayModel.getUsers().indexOf(menuUserSelectionOverlayModel.getSelectedUser());
            if (index == 0) {
                menuUserSelectionOverlayModel.setSelectedUser(menuUserSelectionOverlayModel.getUsers().getLast());
            } else {
                menuUserSelectionOverlayModel.setSelectedUser(menuUserSelectionOverlayModel.getUsers().get(index - 1));
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_L) {
            menuUserSelectionOverlayView.setRightArrowIndex(1); // animate the arrow
            int index = menuUserSelectionOverlayModel.getUsers().indexOf(menuUserSelectionOverlayModel.getSelectedUser());
            if (index == menuUserSelectionOverlayModel.getUsers().size() - 1) {
                menuUserSelectionOverlayModel.setSelectedUser(menuUserSelectionOverlayModel.getUsers().getFirst());
            } else {
                menuUserSelectionOverlayModel.setSelectedUser(menuUserSelectionOverlayModel.getUsers().get(index + 1));
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
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_H)
            menuUserSelectionOverlayView.setLeftArrowIndex(0); // stop animating the arrow

        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_L)
            menuUserSelectionOverlayView.setRightArrowIndex(0); // stop animating the arrow
    }
}